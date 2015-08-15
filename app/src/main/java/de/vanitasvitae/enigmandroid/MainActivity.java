/**
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
 */

package de.vanitasvitae.enigmandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class MainActivity extends Activity
{
    private Menu menu;
    private Spinner rotor1;
    private Spinner rotor2;
    private Spinner rotor3;
    private Spinner reflector;
    private Spinner rotor1Position;
    private Spinner rotor2Position;
    private Spinner rotor3Position;

    private EditText plugboard;
    private EditText input;
    private EditText output;

    private static final int RESULT_SETTINGS = 1;
    private static final String URI_CHANGELOG = "https://github.com/vanitasvitae/EnigmAndroid/blob/master/CHANGELOG.txt";

    private Enigma enigma;
    //memory for the ringSettings
    private int[] ringSettings = {0,0,0};
    private boolean prefAnomaly;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.initLayout();
        this.prefAnomaly = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("prefAnomaly", true);
        this.resetLayout();
        ActivitySingleton singleton = ActivitySingleton.getInstance();
        singleton.setActivity(this);

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
                    input.setText(sharedText);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.menu = menu;
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
            this.resetLayout();
            Toast.makeText(getApplicationContext(), R.string.message_reset,
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.action_choose_ringstellung)
        {
            showRingsettingsDialog();
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
            if(output.getText().length() == 0)
            {
                Toast.makeText(this, R.string.error_no_text_to_send, Toast.LENGTH_LONG).show();
            }
            else
            {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, output.getText().toString());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Updates the enigma to the chosen rotors and plugboard
     *
     * @param v View
     */
    public void updateEnigma(View v)
    {
        int[] conf = new int[10];
        conf[0] = rotor1.getSelectedItemPosition() + 1;
        conf[1] = rotor2.getSelectedItemPosition() + 1;
        conf[2] = rotor3.getSelectedItemPosition() + 1;
        conf[3] = reflector.getSelectedItemPosition() + 1;
        conf[4] = rotor1Position.getSelectedItemPosition() + 1;
        conf[5] = rotor2Position.getSelectedItemPosition() + 1;
        conf[6] = rotor3Position.getSelectedItemPosition() + 1;
        conf[7] = ringSettings[0];
        conf[8] = ringSettings[1];
        conf[9] = ringSettings[2];

        enigma = new Enigma();

        int[][] plugboardConfiguration = null;
        plugboard.setText(plugboard.getText().toString().toUpperCase());
        plugboardConfiguration = Plugboard.parseConfigurationString(plugboard.getText().toString());
        enigma.setConfiguration(conf);
        enigma.setPlugboard(new Plugboard(plugboardConfiguration));
        enigma.setPrefAnomaly(prefAnomaly);
    }

    /**
     * Set the chosen Configuration to the enigma, get the input string from the input textbox and
     * prepare it, set the input to the prepared text, encrypt the prepared input and set the
     * encrypted string to the output textbox and update the spinners to their new positions.
     * @param v View
     */
    public void doCrypto(View v)
    {
        if(input.getText().length()!=0) {
            updateEnigma(null);
            String m = input.getText().toString();
            m = Enigma.prepare(m);
            input.setText(m);
            output.setText(enigma.encrypt(m));
            updateSpinner(enigma.getConfiguration());
        }
    }


    /**
     * Reset all the spinners and textboxes and the ringsettings memory
     */
    private void resetLayout()
    {
        rotor1.setSelection(0);
        rotor2.setSelection(1);
        rotor3.setSelection(2);
        reflector.setSelection(1);
        rotor1Position.setSelection(0);
        rotor2Position.setSelection(0);
        rotor3Position.setSelection(0);
        ringSettings = new int[]{0,0,0};
        plugboard.setText("");
        input.setText("");
        output.setText("");
    }

    /**
     * Initialize the Layout
     */
    private void initLayout()
    {
        Character[] charArray = new Character[26];
        for(int i=0; i<26; i++) {charArray[i] = (char) (65+i);}

        rotor1 = (Spinner) findViewById(R.id.rotor1);
        ArrayAdapter<CharSequence> rotor1Adapter = ArrayAdapter.createFromResource(this,
                R.array.enigma_rotors, android.R.layout.simple_spinner_item);
        rotor1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rotor1.setAdapter(rotor1Adapter);


        rotor2 = (Spinner) findViewById(R.id.rotor2);
        ArrayAdapter<CharSequence> rotor2Adapter = ArrayAdapter.createFromResource(this,
                R.array.enigma_rotors, android.R.layout.simple_spinner_item);
        rotor2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rotor2.setAdapter(rotor2Adapter);

        rotor3 = (Spinner) findViewById(R.id.rotor3);
        ArrayAdapter<CharSequence> rotor3Adapter = ArrayAdapter.createFromResource(this,
                R.array.enigma_rotors, android.R.layout.simple_spinner_item);
        rotor3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rotor3.setAdapter(rotor3Adapter);

        reflector = (Spinner) findViewById(R.id.reflector);
        ArrayAdapter<CharSequence> relfectorAdapter = ArrayAdapter.createFromResource(this,
                R.array.enigma_reflectors, android.R.layout.simple_spinner_item);
        relfectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reflector.setAdapter(relfectorAdapter);

        rotor1Position = (Spinner) findViewById(R.id.rotor1position);
        ArrayAdapter<Character> rotor1PositionAdapter = new ArrayAdapter<>(this.getApplicationContext(),
                android.R.layout.simple_spinner_item,charArray);
        rotor1PositionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rotor1Position.setAdapter(rotor1PositionAdapter);

        rotor2Position = (Spinner) findViewById(R.id.rotor2position);
        ArrayAdapter<Character> rotor2PositionAdapter = new ArrayAdapter<>(this.getApplicationContext(),
                android.R.layout.simple_spinner_item,charArray);
        rotor2PositionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rotor2Position.setAdapter(rotor2PositionAdapter);

        rotor3Position = (Spinner) findViewById(R.id.rotor3position);
        ArrayAdapter<Character> rotor3PositionAdapter = new ArrayAdapter<>(this.getApplicationContext(),
                android.R.layout.simple_spinner_item,charArray);
        rotor3PositionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rotor3Position.setAdapter(rotor3PositionAdapter);

        plugboard = (EditText) findViewById(R.id.plugboard);
        plugboard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    plugboard.setText(plugboard.getText().toString().toUpperCase());
                }
            }
        });
        input = (EditText) findViewById(R.id.input);
        output = (EditText) findViewById(R.id.output);

        input.requestFocus();
    }

    /**
     * Update the Spinners to their new Positions
     * @param c Configuration
     */
    public void updateSpinner(int[] c)
    {
        rotor1.setSelection(c[0] - 1);
        rotor2.setSelection(c[1] - 1);
        rotor3.setSelection(c[2] - 1);
        rotor1Position.setSelection(c[4]);
        rotor2Position.setSelection(c[5]);
        rotor3Position.setSelection(c[6]);
    }

    /**
     * Show the dialog where the user can pick the ringsettings and set them if the user doesn't
     * abort.
     */
    public void showRingsettingsDialog()
    {
        View ringsettingsView = View.inflate(this, R.layout.dialog_ringsettings, null);

        Integer[] ringArray = new Integer[26];
        for(int i=1; i<=26; i++) {ringArray[i-1] = i;}

        final Spinner ring1 = (Spinner) ringsettingsView.findViewById(R.id.rotor1ring);
        ArrayAdapter<Integer> ring1Adapter = new ArrayAdapter<>(this.getApplicationContext(),
                android.R.layout.simple_spinner_item,ringArray);
        ring1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ring1.setAdapter(ring1Adapter);
        ring1.setSelection(ringSettings[0]);

        final Spinner ring2 = (Spinner) ringsettingsView.findViewById(R.id.rotor2ring);
        ArrayAdapter<Integer> ring2Adapter = new ArrayAdapter<>(this.getApplicationContext(),
                android.R.layout.simple_spinner_item,ringArray);
        ring2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ring2.setAdapter(ring2Adapter);
        ring2.setSelection(ringSettings[1]);

        final Spinner ring3 = (Spinner) ringsettingsView.findViewById(R.id.rotor3ring);
        ArrayAdapter<Integer> ring3Adapter = new ArrayAdapter<>(this.getApplicationContext(),
                android.R.layout.simple_spinner_item,ringArray);
        ring3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ring3.setAdapter(ring3Adapter);
        ring3.setSelection(ringSettings[2]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_ringsetting);
        builder.setView(ringsettingsView)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_positiv, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        ringSettings = new int[]{ring1.getSelectedItemPosition(),
                                ring2.getSelectedItemPosition(), ring3.getSelectedItemPosition()};
                        String message = getResources().getString(R.string.dialog_ringsettings_success) + " " + (ringSettings[2]+1) + ", " +
                                (ringSettings[1]+1) + ", " + (ringSettings[0]+1) + ".";
                        Toast.makeText(getApplicationContext(), message,
                                Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.dialog_negativ, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(), R.string.dialog_ringsettings_abort,
                                Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
            {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                this.prefAnomaly = sharedPrefs.getBoolean("prefAnomaly", true);
                break;
            }
        }
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public static class ActivitySingleton
    {
        private static ActivitySingleton instance = null;
        private Activity activity;

        private ActivitySingleton(){}
        public static ActivitySingleton getInstance()
        {
            if(instance == null) instance = new ActivitySingleton();
            return instance;
        }

        public void setActivity(Activity activity)
        {
            this.activity = activity;
        }

        public Activity getActivity()
        {
            return activity;
        }

    }
}
