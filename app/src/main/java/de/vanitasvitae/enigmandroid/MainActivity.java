package de.vanitasvitae.enigmandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class MainActivity extends Activity
{
    private Spinner rotor1;
    private Spinner rotor2;
    private Spinner rotor3;
    private Spinner reversingRotor;
    private Spinner rotor1Position;
    private Spinner rotor2Position;
    private Spinner rotor3Position;
    private EditText plugboard;
    private EditText input;
    private EditText output;

    private Enigma enigma;
    //memory for the ringsettings
    private int[] ringsettings = {0,0,0};

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.initLayout();
        this.reset();

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
        if (id == R.id.action_version)
        {
            Toast.makeText(this.getApplicationContext(), R.string.version,
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.action_reset)
        {
            this.reset();
            Toast.makeText(getApplicationContext(), R.string.message_reset,
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.action_choose_ringstellung)
        {
            showRingsettingsDialog();
            return true;
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
        conf[3] = reversingRotor.getSelectedItemPosition() + 1;
        conf[4] = rotor1Position.getSelectedItemPosition() + 1;
        conf[5] = rotor2Position.getSelectedItemPosition() + 1;
        conf[6] = rotor3Position.getSelectedItemPosition() + 1;
        conf[7] = ringsettings[0];
        conf[8] = ringsettings[1];
        conf[9] = ringsettings[2];

        try
        {
            enigma = new Enigma(null, null);
        } catch (Plugboard.PlugAlreadyUsedException e)
        {
            //There is nothing that could possibly go wrong here.
        }

        char[][] plugboardConfiguration = null;
        try
        {
            plugboardConfiguration = Enigma.parsePlugs(plugboard.getText().toString());
        } catch (Enigma.InvalidPlugboardConfigurationFormatException e)
        {
            String error = this.getResources().getString(R.string.error_parsing_plugs) + ": " + e.getMessage();
            Toast.makeText(getApplicationContext(), error,
                    Toast.LENGTH_LONG).show();
        }
        try
        {
            enigma.setConfiguration(conf);
            enigma.setPlugboard(plugboardConfiguration);

        } catch (Plugboard.PlugAlreadyUsedException e)
        {
            Toast.makeText(this.getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Set the chosen Configuration to the enigma, get the input string from the input textbox and prepare it,
     * set the input to the prepared text, encrypt the prepared input and set the encrypted string to the
     * output textbox and update the spinners to their new positions.
     * @param v View
     */
    public void doCrypto(View v)
    {
        updateEnigma(null);
        String m = input.getText().toString();
        m = Enigma.prepare(m);
        input.setText(m);
        output.setText(enigma.encrypt(m));
        updateSpinner(enigma.getConfiguration());

    }


    /**
     * Reset all the spinners and textboxes and the ringsettings memory
     */
    private void reset()
    {
        rotor1.setSelection(0);
        rotor2.setSelection(1);
        rotor3.setSelection(2);
        reversingRotor.setSelection(1);
        rotor1Position.setSelection(0);
        rotor2Position.setSelection(0);
        rotor3Position.setSelection(0);
        ringsettings = new int[]{0,0,0};
        plugboard.setText("");
        input.setText("");
        output.setText("");
    }

    /**
     * Initialize the Layout
     */
    private void initLayout()
    {
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

        reversingRotor = (Spinner) findViewById(R.id.reflector);
        ArrayAdapter<CharSequence> relfectorAdapter = ArrayAdapter.createFromResource(this,
                R.array.enigma_reflectors, android.R.layout.simple_spinner_item);
        relfectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reversingRotor.setAdapter(relfectorAdapter);

        rotor1Position = (Spinner) findViewById(R.id.rotor1position);
        ArrayAdapter<CharSequence> rotor1PositionAdapter = ArrayAdapter.createFromResource(this,
                R.array.rotor_positions, android.R.layout.simple_spinner_item);
        rotor1PositionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rotor1Position.setAdapter(rotor1PositionAdapter);

        rotor2Position = (Spinner) findViewById(R.id.rotor2position);
        ArrayAdapter<CharSequence> rotor2PositionAdapter = ArrayAdapter.createFromResource(this,
                R.array.rotor_positions, android.R.layout.simple_spinner_item);
        rotor2PositionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rotor2Position.setAdapter(rotor2PositionAdapter);

        rotor3Position = (Spinner) findViewById(R.id.rotor3position);
        ArrayAdapter<CharSequence> rotor3PositionAdapter = ArrayAdapter.createFromResource(this,
                R.array.rotor_positions, android.R.layout.simple_spinner_item);
        rotor3PositionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rotor3Position.setAdapter(rotor3PositionAdapter);

        plugboard = (EditText) findViewById(R.id.plugboard);
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
     * Show the dialog where the user can pick the ringsettings and set them if the user doesnt abort.
     */
    public void showRingsettingsDialog()
    {
        View ringsettingsView = View.inflate(this, R.layout.dialog_ringsettings, null);

        final Spinner ring1 = (Spinner) ringsettingsView.findViewById(R.id.rotor1ring);
        ArrayAdapter<CharSequence> ring1Adapter = ArrayAdapter.createFromResource(this, R.array.ring_positions, android.R.layout.simple_spinner_item);
        ring1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ring1.setAdapter(ring1Adapter);
        ring1.setSelection(ringsettings[0]);

        final Spinner ring2 = (Spinner) ringsettingsView.findViewById(R.id.rotor2ring);
        ArrayAdapter<CharSequence> ring2Adapter = ArrayAdapter.createFromResource(this, R.array.ring_positions, android.R.layout.simple_spinner_item);
        ring2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ring2.setAdapter(ring2Adapter);
        ring2.setSelection(ringsettings[1]);

        final Spinner ring3 = (Spinner) ringsettingsView.findViewById(R.id.rotor3ring);
        ArrayAdapter<CharSequence> ring3Adapter = ArrayAdapter.createFromResource(this, R.array.ring_positions, android.R.layout.simple_spinner_item);
        ring3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ring3.setAdapter(ring3Adapter);
        ring3.setSelection(ringsettings[2]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_ringsetting);
        builder.setView(ringsettingsView)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_positiv, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        ringsettings = new int[]{ring1.getSelectedItemPosition(), ring2.getSelectedItemPosition(), ring3.getSelectedItemPosition()};
                        String message = getResources().getString(R.string.dialog_ringsettings_success) + " " + (ringsettings[2]+1) + ", " + (ringsettings[1]+1) + ", " + (ringsettings[0]+1) + ".";
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
}
