package de.vanitasvitae.enigmandroid;

import android.app.Activity;
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
    private Spinner w1;
    private Spinner w2;
    private Spinner w3;
    private Spinner reversingRotor;
    private Spinner w1pos;
    private Spinner w2pos;
    private Spinner w3pos;
    private EditText plugboard;
    private EditText input;
    private EditText output;

    private Enigma enigma;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        this.initLayout();
        this.reset();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_version)
        {
            Toast.makeText(this.getApplicationContext(), R.string.version,
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(id == R.id.action_reset)
        {
            this.reset();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setEnigma(View v)
    {
        int[] conf = new int[7];
        conf[0] = w1.getSelectedItemPosition()+1;
        conf[1] = w2.getSelectedItemPosition()+1;
        conf[2] = w3.getSelectedItemPosition()+1;
        conf[3] = reversingRotor.getSelectedItemPosition()+1;
        conf[4] = w1pos.getSelectedItemPosition()+1;
        conf[5] = w2pos.getSelectedItemPosition()+1;
        conf[6] = w3pos.getSelectedItemPosition()+1;

        try
        {
            enigma = new Enigma(null,null);
        }
        catch(Plugboard.PlugAlreadyUsedException e)
        {
            //There is nothing that could possibly go wrong here.
        }

        char[][] pbconf = null;
        try
        {
            pbconf = Enigma.parsePlugs(plugboard.getText().toString());
        }
        catch(Enigma.InvalidPlugboardConfigurationFormatException e)
        {
            String error = this.getResources().getString(R.string.error_parsing_plugs)+": "+e.getMessage();
            Toast.makeText(getApplicationContext(), error,
                    Toast.LENGTH_SHORT).show();
        }
        try
        {
            enigma.setConfiguration(conf);
            enigma.setPlugboard(pbconf);

        }
        catch(Plugboard.PlugAlreadyUsedException e)
        {
            Toast.makeText(this.getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void crypto(View v)
    {
        setEnigma(null);
        String m = input.getText().toString();
        m = Enigma.prepare(m);
        input.setText(m);
        output.setText(enigma.encrypt(m));
        updateSpinner(enigma.getConfiguration());

    }

    private void reset()
    {
        w1.setSelection(0);
        w2.setSelection(1);
        w3.setSelection(2);
        reversingRotor.setSelection(1);
        w1pos.setSelection(0);
        w2pos.setSelection(0);
        w3pos.setSelection(0);
        plugboard.setText("");
        input.setText("");
        output.setText("");
    }

    private void initLayout()
    {
        w1 = (Spinner) findViewById(R.id.w1);
        ArrayAdapter<CharSequence> w1adapter = ArrayAdapter.createFromResource(this,
                R.array.enigma_walzen, android.R.layout.simple_spinner_item);
        w1adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        w1.setAdapter(w1adapter);


        w2 = (Spinner) findViewById(R.id.w2);
        ArrayAdapter<CharSequence> w2adapter = ArrayAdapter.createFromResource(this,
                R.array.enigma_walzen, android.R.layout.simple_spinner_item);
        w2adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        w2.setAdapter(w2adapter);

        w3 = (Spinner) findViewById(R.id.w3);
        ArrayAdapter<CharSequence> w3adapter = ArrayAdapter.createFromResource(this,
                R.array.enigma_walzen, android.R.layout.simple_spinner_item);
        w3adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        w3.setAdapter(w3adapter);

        reversingRotor = (Spinner) findViewById(R.id.ukw);
        ArrayAdapter<CharSequence> ukwadapter = ArrayAdapter.createFromResource(this,
                R.array.enigma_ukw, android.R.layout.simple_spinner_item);
        ukwadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reversingRotor.setAdapter(ukwadapter);

        w1pos = (Spinner) findViewById(R.id.w1pos);
        ArrayAdapter<CharSequence> w1posadapter = ArrayAdapter.createFromResource(this,
                R.array.positions, android.R.layout.simple_spinner_item);
        w1posadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        w1pos.setAdapter(w1posadapter);

        w2pos = (Spinner) findViewById(R.id.w2pos);
        ArrayAdapter<CharSequence> w2posadapter = ArrayAdapter.createFromResource(this,
                R.array.positions, android.R.layout.simple_spinner_item);
        w2posadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        w2pos.setAdapter(w2posadapter);

        w3pos = (Spinner) findViewById(R.id.w3pos);
        ArrayAdapter<CharSequence> w3posadapter = ArrayAdapter.createFromResource(this,
                R.array.positions, android.R.layout.simple_spinner_item);
        w3posadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        w3pos.setAdapter(w3posadapter);

        plugboard = (EditText) findViewById(R.id.plugboard);
        input = (EditText) findViewById(R.id.input);
        output = (EditText) findViewById(R.id.output);

        input.requestFocus();
    }

    public void updateSpinner(int[] c)
    {
        w1.setSelection(c[0]-1);
        w2.setSelection(c[1]-1);
        w3.setSelection(c[2]-1);
        w1pos.setSelection(c[4]);
        w2pos.setSelection(c[5]);
        w3pos.setSelection(c[6]);
    }
}
