package de.vanitasvitae.enigmandroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.math.BigInteger;
import java.security.SecureRandom;

import de.vanitasvitae.enigmandroid.enigma.Enigma;
import de.vanitasvitae.enigmandroid.enigma.EnigmaStateBundle;
import de.vanitasvitae.enigmandroid.enigma.inputPreparer.InputPreparer;
import de.vanitasvitae.enigmandroid.layout.LayoutContainer;
import de.vanitasvitae.enigmandroid.layout.PassphraseDialogBuilder;

/**
 * Main Android Activity of the app
 * Copyright (C) 2015  Paul Schaub

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along
 with this program; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * @author vanitasvitae
 */
public class MainActivity extends Activity
{
	private static final int RESULT_SETTINGS = 1;
	private static final String URI_CHANGELOG =
			"https://github.com/vanitasvitae/EnigmAndroid/blob/master/CHANGELOG.txt";
	public static final String APP_ID = "EnigmAndroid";
	public static final int latest_protocol_version = 1;
	public static final int max_protocol_version = 256;

	private LayoutContainer layoutContainer;

	private SecureRandom secureRandom;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		secureRandom = new SecureRandom();
		ActivitySingleton.getInstance().setActivity(this);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		SettingsActivity.SettingsSingleton.getInstance(prefs, getResources());
		layoutContainer = LayoutContainer.createLayoutContainer();

		//Handle whats-new dialog
		handleVersionUpdate();

		//Handle shared text
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();
		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type))
			{
				String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
				if (sharedText != null)
				{
					//If shared text consists of an encoded configuration, try to restore it
					if(sharedText.startsWith(APP_ID+"/"))
						restoreStateFromCode(sharedText);
						//Else put it in the input text box
					else
						layoutContainer.getInput().setRawText(sharedText);
				}
			}
		}
	}

	public SecureRandom getSecureRandom()
	{
		return this.secureRandom;
	}

	public void onDialogFinished(EnigmaStateBundle state)
	{
		layoutContainer.getEnigma().setState(state);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		this.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	/**
	 * Handle Options menu clicks
	 */
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_reset)
		{
			layoutContainer.resetLayout();
			Toast.makeText(getApplicationContext(), R.string.message_reset,
						   Toast.LENGTH_SHORT).show();
			return true;
		}
		else if (id == R.id.action_send_message)
		{
			if(layoutContainer.getOutput().getText().length() == 0)
			{
				Toast.makeText(this, R.string.error_no_text_to_send, Toast.LENGTH_SHORT).show();
			}
			else
			{
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, layoutContainer.getOutput().getModifiedText());
				sendIntent.setType("text/plain");
				startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
			}
		}
		else if (id == R.id.action_choose_ringsetting)
		{
			layoutContainer.showRingSettingsDialog();
			return true;
		}
		else if(id == R.id.action_share_configuration)
		{
			showShareConfigurationDialog();
		}
		else if (id == R.id.action_restore_configuration)
		{
			showReceiveConfigurationDialog();
			return true;
		}
		else if (id == R.id.action_random_configuration)
		{
			layoutContainer.getEnigma().randomState();
			layoutContainer.syncStateFromEnigmaToLayout();
			Toast.makeText(getApplicationContext(), R.string.message_random,
						   Toast.LENGTH_SHORT).show();
			layoutContainer.getOutput().setText("");
			return true;
		}
		else if (id == R.id.action_settings)
		{
			Intent i = new Intent(this, SettingsActivity.class);
			startActivityForResult(i, RESULT_SETTINGS);
		}
		else if (id == R.id.action_about)
		{
			showAboutDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Set the chosen Configuration to the enigma, get the input string from the input text box and
	 * prepare it, set the input to the prepared text, encrypt the prepared input and set the
	 * encrypted string to the output text box and update the spinners to their new positions.
	 * @param v View
	 */
	public void doCrypto(View v)
	{
		layoutContainer.doCrypto();
	}

	/**
	 * Start an intent to share the configuration as QR-Code via Barcode Scanner
	 */
	private void shareConfigurationAsQR()
	{
		IntentIntegrator QRIntegrator = new IntentIntegrator(this);
		layoutContainer.syncStateFromLayoutToEnigma();
		String encoded_state = APP_ID+"/"+layoutContainer.getEnigma().getEncodedState().toString(16);
		Log.d(APP_ID, "Sharing configuration to QR: "+encoded_state);
		QRIntegrator.shareText(encoded_state);
	}

	/**
	 * Start an intent to share the configuration as text
	 */
	private void shareConfigurationAsText()
	{
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT,
							APP_ID+"/"+layoutContainer.getEnigma().getEncodedState().toString(16));
		sendIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
	}

	/**
	 * Start the barcode app to scan a barcode for configuration
	 */
	private void receiveConfigurationQR()
	{
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
	}

	/**
	 * Show a dialog to restore a configuration
	 */
	private void receiveConfigurationText()
	{
		new PassphraseDialogBuilder().showDialog();
	}

	/**
	 * Check, whether the app has been updated
	 */
	private void handleVersionUpdate()
	{
		int currentVersionNumber = 0;
		int savedVersionNumber = SettingsActivity.SettingsSingleton.getInstance().getVersionNumber();
		try
		{
			PackageInfo p = getPackageManager().getPackageInfo(getPackageName(), 0);
			currentVersionNumber = p.versionCode;
		}
		catch (Exception ignored) {}
		if(currentVersionNumber > savedVersionNumber)
		{
			showWhatsNewDialog();
			SettingsActivity.SettingsSingleton.getInstance().setVersionNumber(currentVersionNumber);
		}

	}

	/**
	 * Show a dialog that informs the user about the latest important changes in the app
	 * The dialog appears whenever the app starts after an update or after data has been
	 * deleted
	 */
	private void showWhatsNewDialog()
	{
		PackageInfo pInfo = null;
		try{ pInfo = getPackageManager().getPackageInfo(this.getPackageName(), 0);}
		catch (PackageManager.NameNotFoundException e){ e.printStackTrace();}
		assert pInfo != null;
		String version = pInfo.versionName;
		LayoutInflater li = LayoutInflater.from(this);
		@SuppressLint("InflateParams")
		View dialog = li.inflate(R.layout.dialog_whats_new, null);
		((TextView) dialog.findViewById(R.id.dialog_whats_new_header)).setText(
				String.format(getResources().getText(R.string.dialog_whats_new_header).toString(),version));
		((TextView) dialog.findViewById(R.id.dialog_whats_new_details)).setText(
				R.string.dialog_whats_new_content);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(dialog).setTitle(R.string.dialog_whats_new_title)
				.setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
		builder.create().show();
	}
	/**
	 * Show a Dialog containing information about the app, license, usage, author and a link
	 * to the changelog
	 */
	private void showAboutDialog()
	{
		final View aboutView = View.inflate(this, R.layout.dialog_about, null);
		//Get and set Version code
		PackageInfo pInfo = null;
		try{ pInfo = getPackageManager().getPackageInfo(this.getPackageName(), 0);}
		catch (PackageManager.NameNotFoundException e){ e.printStackTrace();}
		assert pInfo != null;
		String version = pInfo.versionName+ " ("+pInfo.versionCode+")";
		TextView versionText = (TextView) aboutView.findViewById(R.id.about_version_section);
		versionText.setText(version);

		//Build and show dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.title_about_dialog);
		builder.setView(aboutView)
				.setCancelable(true)
				.setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						dialog.dismiss();
					}
				})
				.setNegativeButton(R.string.button_show_changelog, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						dialog.cancel();
						openWebPage(URI_CHANGELOG);
					}
				}).show();
	}

	/**
	 * Show a dialog where the user can choose between sharing the configuration via QR-code or
	 * via string (intent or copy-to-clipboard)
	 */
	private void showShareConfigurationDialog()
	{
		final String configuration = APP_ID+"/"+layoutContainer.getEnigma().getEncodedState().toString(16);
		final View shareView = View.inflate(this, R.layout.dialog_two_options, null);
		LinearLayout l = (LinearLayout) shareView.findViewById(R.id.dialog_two_options_lay);
		EditText e = new EditText(this);
		e.setText(configuration);
		e.setInputType(InputType.TYPE_NULL);
		e.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB){
					android.content.ClipboardManager clipboard =  (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
					ClipData clip;
					clip = ClipData.newPlainText("label", configuration);
					clipboard.setPrimaryClip(clip);
				} else{
					@SuppressWarnings("deprecation")
					android.text.ClipboardManager clipboard = (android.text.ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
					clipboard.setText(configuration);
				}
				Toast.makeText(getApplicationContext(), R.string.message_clipboard, Toast.LENGTH_SHORT).show();
			}
		});
		l.addView(e);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_title_share_configuration)
				.setView(shareView).setCancelable(true);
		final Dialog d = builder.create();
		Button one = (Button) shareView.findViewById(R.id.dialog_two_options_1);
		one.setText(R.string.dialog_share_qr);
		one.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				shareConfigurationAsQR();
				d.dismiss();
			}
		});
		Button two = (Button) shareView.findViewById(R.id.dialog_two_options_2);
		two.setText(R.string.dialog_share_code);
		two.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				shareConfigurationAsText();
				d.dismiss();
			}
		});
		d.show();
	}

	/**
	 * Show a dialog, where the user can choose between scanning QR-code and entering a string to
	 * restore the encoded configuration
	 */
	private void showReceiveConfigurationDialog()
	{
		final View shareView = View.inflate(this, R.layout.dialog_two_options, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_title_restore_configuration)
				.setView(shareView).setCancelable(true);
		final Dialog d = builder.create();
		Button one = (Button) shareView.findViewById(R.id.dialog_two_options_1);
		one.setText(R.string.dialog_restore_qr);
		one.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				receiveConfigurationQR();
				d.dismiss();
			}
		});
		Button two = (Button) shareView.findViewById(R.id.dialog_two_options_2);
		two.setText(R.string.dialog_restore_code);
		two.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				receiveConfigurationText();
				d.dismiss();
			}
		});
		d.show();
	}

	/**
	 * Handle Activity Results
	 * @param requestCode requestCode
	 * @param resultCode resultCode (RESULT_SETTINGS is defined at the top)
	 * @param data data
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			//Come back from Settings
			case RESULT_SETTINGS:
			{
				applyPreferenceChanges();
				break;
			}
			// Receive from QR
			case IntentIntegrator.REQUEST_CODE:
				IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
				if (scanResult != null) {
					String content = scanResult.getContents();
					if(content == null) Log.e(APP_ID, "Error! Received nothing from QR-Code!");
					else {
						Log.d(APP_ID, "Received " + content + " from QR-Code!");
						restoreStateFromCode(content);
					}
				}
		}
	}

	/**
	 * Handle changes in preferences and apply those changes to the app
	 */
	private void applyPreferenceChanges()
	{
		SettingsActivity s = SettingsActivity.SettingsSingleton.getInstance();
		if(s.prefMachineTypeChanged())
		{
			layoutContainer = LayoutContainer.createLayoutContainer();
		}
		if(s.prefMessageFormattingChanged())
		{
			layoutContainer.setEditTextAdapter(s.getPrefMessageFormatting());
		}
		if(s.prefNumericLanguageChanged())
		{
			layoutContainer.setInputPreparer(InputPreparer.createInputPreparer());
		}
	}

	/**
	 * Set EnigmAndroid into a certain state as described in the QR-Code
	 * @param mem content of the QR-Code
	 */
	public void restoreStateFromCode(String mem)
	{
		if(!mem.startsWith(APP_ID+"/"))
		{
			Toast.makeText(this, R.string.error_no_valid_qr, Toast.LENGTH_LONG).show();
		}
		else
		{
			String inputString = layoutContainer.getInput().getText();
			mem = mem.substring((APP_ID+"/").length());
			BigInteger s = new BigInteger(mem, 16);
			int protocol_version = Enigma.getValue(s, max_protocol_version);
			s = Enigma.removeDigit(s, max_protocol_version);
			Log.d(APP_ID,
				  "Try to restore configuration from BigInteger value "+s.toString()+" in protocol version "+protocol_version+".");
			SettingsActivity.SettingsSingleton.getInstance()
					.setPrefMachineType(Enigma.chooseEnigmaFromSave(s));
			layoutContainer = LayoutContainer.createLayoutContainer();
			layoutContainer.getEnigma().restoreState(Enigma.removeDigit(s,20), protocol_version);
			layoutContainer.setInputPreparer(InputPreparer.createInputPreparer());
			layoutContainer.syncStateFromEnigmaToLayout();
			layoutContainer.getInput().setText(inputString);
			layoutContainer.getOutput().setText("");
		}
	}

	/**
	 * Set EnigmAndroid into a state calculated from the seed.
	 * @param seed seed
	 */
	public void applyStateFromSeed(String seed)
	{
		String inputString = layoutContainer.getInput().getText();
		SettingsActivity.SettingsSingleton.getInstance()
		.setPrefMachineType(Enigma.chooseEnigmaFromSeed(seed));
		layoutContainer = LayoutContainer.createLayoutContainer();
		layoutContainer.getEnigma().setStateFromSeed(seed);
		layoutContainer.setInputPreparer(InputPreparer.createInputPreparer());
		layoutContainer.syncStateFromEnigmaToLayout();
		layoutContainer.getInput().setText(inputString);
		layoutContainer.getOutput().setText("");
	}

	/**
	 * Open the web page with the URL url
	 * @param url URL of the website
	 */
	private void openWebPage(String url) {
		Uri webPage = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
		if (intent.resolveActivity(getPackageManager()) != null) {
			startActivity(intent);
		}
	}

	/**
	 * Singleton that grants access to an Activity from anywhere within the app
	 */
	public static class ActivitySingleton
	{
		private static ActivitySingleton instance = null;
		private Activity activity;

		//private constructor
		private ActivitySingleton(){}
		//Singleton method
		public static ActivitySingleton getInstance()
		{
			if(instance == null) instance = new ActivitySingleton();
			return instance;
		}

		/**
		 * Set an Activity that the Singleton returns
		 * @param activity activity that's stored
		 */
		public void setActivity(Activity activity)
		{
			this.activity = activity;
		}

		/**
		 * Returns the stored Activity
		 * @return stored Activity
		 */
		public Activity getActivity()
		{
			return activity;
		}

	}
}
