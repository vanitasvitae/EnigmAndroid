package de.vanitasvitae.enigmandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import de.vanitasvitae.enigmandroid.enigma.inputPreparer.InputPreparer;
import de.vanitasvitae.enigmandroid.layout.LayoutContainer;

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

    LayoutContainer layoutContainer;
    protected String prefMachineType;
    protected boolean prefAnomaly;
    protected String prefNumericLanguage;
    protected String prefMessageFormatting;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("Activity","OnCreate!");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.prefMachineType = sharedPreferences.getString(SettingsActivity.PREF_MACHINE_TYPE, getResources().
                getStringArray(R.array.pref_alias_machine_type)[0]);
        ActivitySingleton singleton = ActivitySingleton.getInstance();
        singleton.setActivity(this);
        updateContentView();
        layoutContainer = LayoutContainer.createLayoutContainer(prefMachineType);
        updatePreferenceValues();
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

    /**
     * Unfortunately we have to overwrite this, because on orientation changes the LayoutContainer
     * will reset and we would lose information about plugboard, reflector-wiring and ring settings.
     * These info are we saving here.
     * TODO: Find more elegant solution
     * @param outState state
     */
    @Override
    protected void onSaveInstanceState (Bundle outState)
    {
        ArrayList<Integer> plugboard = new ArrayList<>();
        if(layoutContainer.getState().getConfigurationPlugboard() != null) {
            for (int i : layoutContainer.getState().getConfigurationPlugboard()) plugboard.add(i);
        }
        outState.putIntegerArrayList("plugboard",plugboard);

        ArrayList<Integer> reflector = new ArrayList<>();
        if(layoutContainer.getState().getConfigurationReflector() != null) {
            for (int i : layoutContainer.getState().getConfigurationReflector()) reflector.add(i);
        }
        outState.putIntegerArrayList("reflector", reflector);

        outState.putInt("ring1", layoutContainer.getState().getRingSettingRotor1());
        outState.putInt("ring2", layoutContainer.getState().getRingSettingRotor2());
        outState.putInt("ring3", layoutContainer.getState().getRingSettingRotor3());
        outState.putInt("ring4", layoutContainer.getState().getRingSettingRotor4());
        outState.putInt("ringR", layoutContainer.getState().getRingSettingReflector());

        super.onSaveInstanceState(outState);
    }

    /**
     * Here we get back values previously saved int onSaveInstanceState
     * @param savedInstanceState state
     */
    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState)
    {
        ArrayList<Integer> plugboard = savedInstanceState.getIntegerArrayList("plugboard");
        if(plugboard != null && plugboard.size() != 0) {
            int[] p = new int[plugboard.size()];
            for (int i = 0; i < plugboard.size(); i++) p[i] = plugboard.get(i);
            layoutContainer.getState().setConfigurationPlugboard(p);
        }

        ArrayList<Integer> reflector = savedInstanceState.getIntegerArrayList("reflector");
        if(reflector != null && reflector.size() != 0) {
            int[] r = new int[reflector.size()];
            for (int i = 0; i < reflector.size(); i++) r[i] = reflector.get(i);
            layoutContainer.getState().setConfigurationReflector(r);
        }

        layoutContainer.getState().setRingSettingRotor1(savedInstanceState.getInt("ring1"));
        layoutContainer.getState().setRingSettingRotor2(savedInstanceState.getInt("ring2"));
        layoutContainer.getState().setRingSettingRotor3(savedInstanceState.getInt("ring3"));
        layoutContainer.getState().setRingSettingRotor4(savedInstanceState.getInt("ring4"));
        layoutContainer.getState().setRingSettingReflector(savedInstanceState.getInt("ringR"));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.updateContentView();
    }

    private void updateContentView()
    {
        switch (prefMachineType)
        {
            case "I":
                this.setContentView(R.layout.activity_main_i_m3);
                break;
            case "M3":
                this.setContentView(R.layout.activity_main_i_m3);
                break;
            case "M4":
                this.setContentView(R.layout.activity_main_m4);
                break;
            case "D":
                this.setContentView(R.layout.activity_main_d);
                break;
            case "K":
            case "KS":
            case "KSA":
            case "T":
            case "R":
            case "G31":
            case "G312":
            case "G260":
                this.setContentView(R.layout.activity_main_g_k_t);
                break;
            default:
                this.setContentView(R.layout.activity_main_i_m3);
                break;
        }
    }

    private void updatePreferenceValues()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.setPrefMachineType(sharedPreferences.getString(SettingsActivity.PREF_MACHINE_TYPE, getResources().
                getStringArray(R.array.pref_alias_machine_type)[0]));
        this.setPrefAnomaly(sharedPreferences.getBoolean(SettingsActivity.PREF_ANOMALY, true));
        this.setPrefNumericLanguage(sharedPreferences.getString(SettingsActivity.PREF_NUMERIC_LANGUAGE, getResources().
                getStringArray(R.array.pref_alias_numeric_spelling_language)[0]));
        this.setPrefMessageFormatting(sharedPreferences.getString(SettingsActivity.PREF_MESSAGE_FORMATTING, getResources().
                getStringArray(R.array.pref_alias_message_formatting)[0]));
    }

    public void setPrefMachineType(String type)
    {
        if(prefMachineType == null || !prefMachineType.equals(type))
        {
            prefMachineType = type;
            String savedInput = "";
            if(layoutContainer != null)
            {
                savedInput = layoutContainer.getInput().getText();
            }
            updateContentView();
            layoutContainer = LayoutContainer.createLayoutContainer(prefMachineType);
            layoutContainer.setInputPreparer(InputPreparer.createInputPreparer());
            layoutContainer.getInput().setText(savedInput);
        }
    }

    public String getPrefMachineType()
    {
        if(prefMachineType != null) return prefMachineType;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.prefMachineType = sharedPreferences.getString(SettingsActivity.PREF_MACHINE_TYPE, getResources().
                    getStringArray(R.array.pref_alias_machine_type)[0]);
        return prefMachineType;
    }

    public void setPrefAnomaly(boolean anomaly)
    {
        if(prefAnomaly !=anomaly)
        {
            prefAnomaly = anomaly;
            if(layoutContainer != null && layoutContainer.getEnigma() != null) layoutContainer.getEnigma().setPrefAnomaly(anomaly);
        }
    }

    public boolean getPrefAnomaly()
    {
        return prefAnomaly;
    }

    public void setPrefNumericLanguage(String lang)
    {
        if(prefNumericLanguage == null || !prefNumericLanguage.equals(lang))
        {
            prefNumericLanguage = lang;
            layoutContainer.setInputPreparer(InputPreparer.createInputPreparer());
        }
    }

    public String getPrefNumericLanguage()
    {
        if(prefNumericLanguage != null) return prefNumericLanguage;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.prefNumericLanguage = sharedPreferences.getString(SettingsActivity.PREF_NUMERIC_LANGUAGE, getResources().
                getStringArray(R.array.pref_alias_numeric_spelling_language)[0]);
        return prefNumericLanguage;
    }

    public void setPrefMessageFormatting(String messageFormatting)
    {
        if(prefMessageFormatting == null || !prefMessageFormatting.equals(messageFormatting))
        {
            prefMessageFormatting = messageFormatting;
            layoutContainer.setEditTextAdapter(messageFormatting);
        }
    }

    public String getPrefMessageFormatting()
    {
        if(prefMessageFormatting != null) return prefMessageFormatting;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.prefMessageFormatting = sharedPreferences.getString(SettingsActivity.PREF_MESSAGE_FORMATTING, getResources().
                getStringArray(R.array.pref_alias_message_formatting)[0]);
        return prefMessageFormatting;
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
        else if (id == R.id.action_random_configuration)
        {
            layoutContainer.getEnigma().randomState();
            layoutContainer.setLayoutState(layoutContainer.getEnigma().getState());
            Toast.makeText(getApplicationContext(), R.string.message_random,
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.action_choose_ringstellung)
        {
            layoutContainer.showRingSettingsDialog();
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
        else if (id == R.id.action_send)
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
        return super.onOptionsItemSelected(item);
    }

    /**
     * Set the chosen Configuration to the enigma, get the input string from the input textbox and
     * prepare it, set the input to the prepared text, encrypt the prepared input and set the
     * encrypted string to the output textbox and update the spinners to their new positions.
     * @param v View
     */
    public void doCrypto(View v)
    {
        layoutContainer.doCrypto();
    }

    /**
     * Show a Dialog containing information about the app, license, usage, author and a link
     * to the changelog
     */
    public void showAboutDialog()
    {
        final View aboutView = View.inflate(this, R.layout.dialog_about, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_about_dialog);
        builder.setView(aboutView)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_positiv, new DialogInterface.OnClickListener()
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
     * Handle preference changes
     * @param requestCode requestCode
     * @param resultCode resultCode (RESULT_SETTINGS is defined at the top)
     * @param data data (not important here)
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
            {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                this.setPrefMachineType(sharedPrefs.getString(SettingsActivity.PREF_MACHINE_TYPE, getResources()
                        .getStringArray(R.array.pref_alias_machine_type)[0]));
                this.setPrefAnomaly(sharedPrefs.getBoolean(SettingsActivity.PREF_ANOMALY, true));
                this.setPrefNumericLanguage(sharedPrefs.getString(SettingsActivity.PREF_NUMERIC_LANGUAGE, getResources().
                        getStringArray(R.array.pref_alias_numeric_spelling_language)[0]));
                this.setPrefMessageFormatting(sharedPrefs.getString(SettingsActivity.PREF_MESSAGE_FORMATTING,
                        getResources().getStringArray(R.array.pref_alias_message_formatting)[0]));
                break;
            }

        }
    }

    /**
     * Open the web page with the URL url
     * @param url URL of the website
     */
    public void openWebPage(String url) {
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
