package de.vanitasvitae.enigmandroid.layout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import de.vanitasvitae.enigmandroid.R;
import de.vanitasvitae.enigmandroid.enigma.Enigma_M4;
import de.vanitasvitae.enigmandroid.enigma.Plugboard;

/**
 * Concrete LayoutContainer for the M4 layout.
 * This class contains the layout and controls the layout elements such as spinners and stuff
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
public class LayoutContainer_M4 extends LayoutContainer
{
    private Spinner rotor1View;
    private Spinner rotor2View;
    private Spinner rotor3View;
    private Spinner rotor4View;
    private Spinner reflectorView;
    private Spinner rotor1PositionView;
    private Spinner rotor2PositionView;
    private Spinner rotor3PositionView;
    private Spinner rotor4PositionView;

    private EditText plugboardView;

    public LayoutContainer_M4()
    {
        super();
        main.setTitle("M4 - EnigmAndroid");
        this.enigma = new Enigma_M4();
        this.ringSettings = new int[]{0,0,0,0};
        this.rotor1View = (Spinner) main.findViewById(R.id.rotor1);
        this.rotor2View = (Spinner) main.findViewById(R.id.rotor2);
        this.rotor3View = (Spinner) main.findViewById(R.id.rotor3);
        this.rotor4View = (Spinner) main.findViewById(R.id.thin_rotor);
        this.reflectorView = (Spinner) main.findViewById(R.id.reflector);
        this.rotor1PositionView = (Spinner) main.findViewById(R.id.rotor1position);
        this.rotor2PositionView = (Spinner) main.findViewById(R.id.rotor2position);
        this.rotor3PositionView = (Spinner) main.findViewById(R.id.rotor3position);
        this.rotor4PositionView = (Spinner) main.findViewById(R.id.thin_rotor_position);
        this.plugboardView = (EditText) main.findViewById(R.id.plugboard);
        this.inputView = (EditText) main.findViewById(R.id.input);
        this.outputView = (EditText) main.findViewById(R.id.output);

        initialize();
        reset();
    }

    @Override
    void initialize()
    {
        Character[] rotorPositionArray = new Character[26];
        for(int i=0; i<26; i++) {rotorPositionArray[i] = (char) (65+i); /**Fill with A..Z*/}

        ArrayAdapter<CharSequence> rotor1Adapter = ArrayAdapter.createFromResource(main, R.array.rotors_1_8,
                android.R.layout.simple_spinner_item);
        rotor1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rotor1View.setAdapter(rotor1Adapter);
        ArrayAdapter<CharSequence> rotor2Adapter = ArrayAdapter.createFromResource(main, R.array.rotors_1_8,
                android.R.layout.simple_spinner_item);
        rotor2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rotor2View.setAdapter(rotor2Adapter);
        ArrayAdapter<CharSequence> rotor3Adapter = ArrayAdapter.createFromResource(main, R.array.rotors_1_8,
                android.R.layout.simple_spinner_item);
        rotor3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rotor3View.setAdapter(rotor3Adapter);
        ArrayAdapter<CharSequence> rotor4Adapter = ArrayAdapter.createFromResource(main, R.array.rotors_beta_gamma,
                android.R.layout.simple_spinner_item);
        rotor4Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rotor4View.setAdapter(rotor4Adapter);

        ArrayAdapter<CharSequence> reflectorAdapter = ArrayAdapter.createFromResource(main, R.array.reflectors_b_c,
                android.R.layout.simple_spinner_item);
        reflectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reflectorView.setAdapter(reflectorAdapter);

        ArrayAdapter<Character> rotor1PositionAdapter = new ArrayAdapter<>(main.getApplicationContext(),
                android.R.layout.simple_spinner_item,rotorPositionArray);
        rotor1PositionAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        rotor1PositionView.setAdapter(rotor1PositionAdapter);
        ArrayAdapter<Character> rotor2PositionAdapter = new ArrayAdapter<>(main.getApplicationContext(),
                android.R.layout.simple_spinner_item,rotorPositionArray);
        rotor2PositionAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        rotor2PositionView.setAdapter(rotor2PositionAdapter);
        ArrayAdapter<Character> rotor3PositionAdapter = new ArrayAdapter<>(main.getApplicationContext(),
                android.R.layout.simple_spinner_item,rotorPositionArray);
        rotor3PositionAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        rotor3PositionView.setAdapter(rotor3PositionAdapter);
        ArrayAdapter<Character> rotor4PositionAdapter = new ArrayAdapter<>(main.getApplicationContext(),
                android.R.layout.simple_spinner_item,rotorPositionArray);
        rotor4PositionAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        rotor4PositionView.setAdapter(rotor4PositionAdapter);
    }

    @Override
    public void reset()
    {
        rotor1View.setSelection(0);         //I
        rotor2View.setSelection(1);         //II
        rotor3View.setSelection(2);         //III
        rotor4View.setSelection(0);         //Beta
        reflectorView.setSelection(0);      //B
        rotor1PositionView.setSelection(0); //0
        rotor2PositionView.setSelection(0); //0
        rotor3PositionView.setSelection(0); //0
        rotor4PositionView.setSelection(0); //0
        ringSettings = new int[]{0,0,0,0};
        inputView.setText("");
        outputView.setText("");
        plugboardView.setText("");
        enigma.setConfiguration(createConfiguration());
        enigma.setPlugboard(new Plugboard());
    }

    @Override
    public void updateLayout()
    {
        int[] conf = enigma.getConfiguration();
        rotor1View.setSelection(conf[0]-1);
        rotor2View.setSelection(conf[1]-1);
        rotor3View.setSelection(conf[2]-1);
        rotor4View.setSelection(conf[3]-9);
        reflectorView.setSelection(conf[4]-4);
        rotor1PositionView.setSelection(conf[5]);
        rotor2PositionView.setSelection(conf[6]);
        rotor3PositionView.setSelection(conf[7]);
        rotor4PositionView.setSelection(conf[8]);
        ringSettings[0] = conf[9];
        ringSettings[1] = conf[10];
        ringSettings[2] = conf[11];
        ringSettings[3] = conf[12];
    }

    @Override
    public int[] createConfiguration()
    {
        int[] conf = new int[13];
        //Rotors 1..4
        conf[0] = rotor1View.getSelectedItemPosition() + 1;
        conf[1] = rotor2View.getSelectedItemPosition() + 1;
        conf[2] = rotor3View.getSelectedItemPosition() + 1;
        conf[3] = rotor4View.getSelectedItemPosition() + 9;     //Beta is rotor #9
        //Reflector
        conf[4] = reflectorView.getSelectedItemPosition() + 4;  //thinB is reflector #4

        conf[5] = rotor1PositionView.getSelectedItemPosition();
        conf[6] = rotor2PositionView.getSelectedItemPosition();
        conf[7] = rotor3PositionView.getSelectedItemPosition();
        conf[8] = rotor4PositionView.getSelectedItemPosition();

        conf[9] = ringSettings[0];
        conf[10] = ringSettings[1];
        conf[11] = ringSettings[2];
        conf[12] = ringSettings[3];

        return conf;
    }

    @Override
    public int[][] createPlugboardConfiguration() {
        return Plugboard.parseConfigurationString(plugboardView.getText().toString());
    }

    @Override
    public void showRingSettingsDialog()
    {
        Integer[] ringArray = new Integer[26];
        for(int i=1; i<=26; i++) {ringArray[i-1] = i;}
        View ringSettingsView = View.inflate(main, R.layout.dialog_ringsettings_m4, null);
        final Spinner ring1 = (Spinner) ringSettingsView.findViewById(R.id.rotor1ring);
        ArrayAdapter<Integer> ring1Adapter = new ArrayAdapter<>(main,
                android.R.layout.simple_spinner_item,ringArray);
        ring1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ring1.setAdapter(ring1Adapter);
        ring1.setSelection(ringSettings[0]);

        final Spinner ring2 = (Spinner) ringSettingsView.findViewById(R.id.rotor2ring);
        ArrayAdapter<Integer> ring2Adapter = new ArrayAdapter<>(main,
                android.R.layout.simple_spinner_item,ringArray);
        ring2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ring2.setAdapter(ring2Adapter);
        ring2.setSelection(ringSettings[1]);

        final Spinner ring3 = (Spinner) ringSettingsView.findViewById(R.id.rotor3ring);
        ArrayAdapter<Integer> ring3Adapter = new ArrayAdapter<>(main,
                android.R.layout.simple_spinner_item,ringArray);
        ring3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ring3.setAdapter(ring3Adapter);
        ring3.setSelection(ringSettings[2]);

        final Spinner ring4 = (Spinner) ringSettingsView.findViewById(R.id.rotor4ring);
        ArrayAdapter<Integer> ring4Adapter = new ArrayAdapter<>(main,
                android.R.layout.simple_spinner_item, ringArray);
        ring4Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ring4.setAdapter(ring4Adapter);
        ring4.setSelection(ringSettings[3]);

        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder.setTitle(R.string.title_ringsetting);
        builder.setView(ringSettingsView)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_positiv, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        ringSettings[0] = ring1.getSelectedItemPosition();
                        ringSettings[1] = ring2.getSelectedItemPosition();
                        ringSettings[2] = ring3.getSelectedItemPosition();
                        ringSettings[3] = ring4.getSelectedItemPosition();
                        String message = main.getResources().getString(
                                R.string.dialog_ringsettings_success) + " " +
                                (ringSettings[3]+1) + ", " +
                                (ringSettings[2]+1) + ", " +
                                (ringSettings[1]+1) + ", " +
                                (ringSettings[0]+1) + ".";
                        Toast.makeText(main, message, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.dialog_negativ, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Toast.makeText(main, R.string.dialog_ringsettings_abort,
                                Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }
}