package de.vanitasvitae.enigmandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.math.BigInteger;
import java.security.SecureRandom;

import de.vanitasvitae.enigmandroid.enigma.Enigma;
import de.vanitasvitae.enigmandroid.layout.LayoutContainer;
import de.vanitasvitae.enigmandroid.layout.PassphraseDialogBuilder;

/**
 * Reimplementation of MainActivity
 * Created by vanitas on 11.10.15.
 */
public class Main2 extends Activity
{
	private static final int RESULT_SETTINGS = 1;
	private static final String URI_CHANGELOG =
			"https://github.com/vanitasvitae/EnigmAndroid/blob/master/CHANGELOG.txt";
	public static final String APP_ID = "EnigmAndroid";

	private LayoutContainer layoutContainer;
	private SecureRandom numberGenerator;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.numberGenerator = new SecureRandom();
		MainActivity.ActivitySingleton singleton = MainActivity.ActivitySingleton.getInstance();
		singleton.setActivity(this);
		numberGenerator = new SecureRandom();
		restoreEnigmaModelAndState();
		
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
					layoutContainer.getInput().setRawText(sharedText);
				}
			}
		}
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
		else if (id == R.id.action_send)
		{
			actionShareMessage();
		}
		else if (id == R.id.action_choose_ringsetting)
		{
			layoutContainer.showRingSettingsDialog();
			return true;
		}
		else if(id == R.id.action_enter_seed)
		{
			new PassphraseDialogBuilder().showDialog();
			return true;
		}
		else if (id == R.id.action_receive_scan)
		{
			IntentIntegrator integrator = new IntentIntegrator(this);
			integrator.initiateScan();
			return true;
		}
		else if(id == R.id.action_share_scan)
		{
			actionShareConfiguration();
			return true;
		}
		else if (id == R.id.action_random_configuration)
		{
			actionRandomConfiguration();
			return true;
		}
		else if (id == R.id.action_settings)
		{
			Intent i = new Intent(this, SettingsActivity.class);
			startActivityForResult(i, RESULT_SETTINGS);
		}
		else if (id == R.id.action_about)
		{
			actionAbout();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Handle preference changes
	 * @param requestCode requestCode
	 * @param resultCode resultCode (RESULT_SETTINGS is defined at the top)
	 * @param data data (not important here)
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			//Settings
			case RESULT_SETTINGS:
			{
				restoreEnigmaModelAndState();
				break;
			}
			//QR_Scanner
			case IntentIntegrator.REQUEST_CODE:
				IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
				if (scanResult != null) {
					String content = scanResult.getContents();
					if(content == null) Log.e(APP_ID, "Error! Received nothing from QR-Code!");
					else {
						Log.d(APP_ID, "Received " + content + " from QR-Code!");
						restoreEnigmaModelAndState(content);
					}
				}
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		//TODO: Save state
	}

	private boolean restoreEnigmaModelAndState()
	{
			String savedState = SettingsActivity.SettingsSingleton.getInstance().getPrefSavedEnigmaState();
			if (savedState.equals("-1") || SettingsActivity.SettingsSingleton.getInstance().prefSavedEnigmaStateChanged())
			{
				layoutContainer = LayoutContainer.createLayoutContainer(
						SettingsActivity.SettingsSingleton.getInstance().getPrefMachineType());
				return false;
			}
		//No changes
		return false;
	}

	private boolean restoreEnigmaModelAndState(BigInteger savedState)
	{
		SettingsActivity.SettingsSingleton.getInstance().setPrefMachineType(
				Enigma.chooseEnigmaFromSave(savedState));
		String savedInput = "";
		if(layoutContainer != null) savedInput = layoutContainer.getInput().getText();
		layoutContainer = LayoutContainer.createLayoutContainer(
				SettingsActivity.SettingsSingleton.getInstance().getPrefMachineType());
		layoutContainer.getEnigma().restoreState(Enigma.removeDigit(savedState, 20));
		layoutContainer.syncStateFromEnigmaToLayout();
		layoutContainer.getInput().setText(savedInput);
		layoutContainer.getOutput().setText("");
		return true;
	}

	/**
	 * Restore Enigma state from String. String has to start with "EnigmAndroid/"
	 * @param savedState String
	 * @return success
	 */
	private boolean restoreEnigmaModelAndState(String savedState)
	{
		if(!savedState.startsWith(APP_ID+"/"))
		{
			Toast.makeText(this, R.string.error_no_valid_qr, Toast.LENGTH_LONG).show();
			return false;
		}
		else
		{
			savedState = savedState.substring((APP_ID+"/").length());
			BigInteger s = new BigInteger(savedState, 16);
			return restoreEnigmaModelAndState(s);
		}
	}

	/**
	 * Set EnigmAndroid into a state calculated from the seed.
	 * @param seed seed
	 */
	public void createStateFromSeed(String seed)
	{
		String savedInput = "";
		if(layoutContainer != null) savedInput = layoutContainer.getInput().getText();
		SettingsActivity.SettingsSingleton.getInstance().setPrefMachineType(
				Enigma.chooseEnigmaFromSeed(seed));
		layoutContainer = LayoutContainer.createLayoutContainer();
		layoutContainer.getEnigma().setStateFromSeed(seed);
		layoutContainer.syncStateFromEnigmaToLayout();
		layoutContainer.getInput().setText(savedInput);
		layoutContainer.getOutput().setText("");
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
	 * If there is any message inside the right text field, share it via intent.
	 * Otherwise show a Toast.
	 */
	private void actionShareMessage()
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

	/**
	 * Share current enigma configuration via QR-Code
	 */
	private void actionShareConfiguration()
	{
		IntentIntegrator QRIntegrator = new IntentIntegrator(this);
		layoutContainer.syncStateFromLayoutToEnigma();
		Log.d(APP_ID,
			  "Sharing configuration to QR: "+layoutContainer.getEnigma().stateToString());
		QRIntegrator.shareText(APP_ID+"/"+layoutContainer.getEnigma().stateToString());
	}

	/**
	 * Set the enigma to a random state.
	 * Do not change enigma model.
	 */
	private void actionRandomConfiguration()
	{
		layoutContainer.getEnigma().randomState();
		layoutContainer.syncStateFromEnigmaToLayout();
		Toast.makeText(getApplicationContext(), R.string.message_random,
					   Toast.LENGTH_SHORT).show();
		layoutContainer.getOutput().setText("");
	}

	/**
	 * Show the credits dialog of the app
	 */
	private void actionAbout()
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
						Uri webPage = Uri.parse(URI_CHANGELOG);
						Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
						if (intent.resolveActivity(getPackageManager()) != null) {
							startActivity(intent);
						}
					}
				}).show();
	}

	public SecureRandom getNumberGenerator()
	{
		return this.numberGenerator;
	}
}
