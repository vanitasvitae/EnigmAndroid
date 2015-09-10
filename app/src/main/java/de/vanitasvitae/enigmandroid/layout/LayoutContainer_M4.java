package de.vanitasvitae.enigmandroid.layout;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import de.vanitasvitae.enigmandroid.R;
import de.vanitasvitae.enigmandroid.enigma.EnigmaStateBundle;
import de.vanitasvitae.enigmandroid.enigma.Enigma_M4;

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
    private Enigma_M4 enigma;

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
        this.resetLayout();
    }



    public Enigma_M4 getEnigma()
    {
        return this.enigma;
    }

    @Override
    protected void initializeLayout() {
        this.rotor1View = (Spinner) main.findViewById(R.id.rotor1);
        this.rotor2View = (Spinner) main.findViewById(R.id.rotor2);
        this.rotor3View = (Spinner) main.findViewById(R.id.rotor3);
        this.rotor4View = (Spinner) main.findViewById(R.id.thin_rotor);
        this.rotor1PositionView = (Spinner) main.findViewById(R.id.rotor1position);
        this.rotor2PositionView = (Spinner) main.findViewById(R.id.rotor2position);
        this.rotor3PositionView = (Spinner) main.findViewById(R.id.rotor3position);
        this.rotor4PositionView = (Spinner) main.findViewById(R.id.thin_rotor_position);
        this.reflectorView = (Spinner) main.findViewById(R.id.reflector);
        this.plugboardView = (EditText) main.findViewById(R.id.plugboard);

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
    public void resetLayout() {
        enigma = new Enigma_M4();
        setLayoutState(enigma.getState());
        outputView.setText("");
        inputView.setText("");
    }

    @Override
    protected void setLayoutState(EnigmaStateBundle state) {
        this.state = state;
        this.rotor1View.setSelection(state.getTypeRotor1()-1);
        this.rotor2View.setSelection(state.getTypeRotor2() - 1);
        this.rotor3View.setSelection(state.getTypeRotor3() - 1);
        this.rotor4View.setSelection(state.getTypeRotor4() - 9);
        this.reflectorView.setSelection(state.getTypeReflector() - 4);
        this.rotor1PositionView.setSelection(state.getRotationRotor1());
        this.rotor2PositionView.setSelection(state.getRotationRotor2());
        this.rotor3PositionView.setSelection(state.getRotationRotor3());
        this.rotor4PositionView.setSelection(state.getRotationRotor4());
        this.plugboardView.setText(state.getConfigurationPlugboard());
    }

    @Override
    protected void refreshState() {
        state.setTypeRotor1(rotor1View.getSelectedItemPosition()+1);
        state.setTypeRotor2(rotor2View.getSelectedItemPosition()+1);
        state.setTypeRotor3(rotor3View.getSelectedItemPosition()+1);
        state.setTypeRotor4(rotor4View.getSelectedItemPosition()+9);
        state.setTypeReflector(reflectorView.getSelectedItemPosition()+4);
        state.setRotationRotor1(rotor1PositionView.getSelectedItemPosition());
        state.setRotationRotor2(rotor2PositionView.getSelectedItemPosition());
        state.setRotationRotor3(rotor3PositionView.getSelectedItemPosition());
        state.setRotationRotor4(rotor4PositionView.getSelectedItemPosition());
        state.setConfigurationPlugboard(plugboardView.getText().toString());
    }

    @Override
    public void showRingSettingsDialog()
    {
        new RingSettingsDialogBuilder.RingSettingsDialogBuilderRotRotRotRot().
                createRingSettingsDialog(state);
    }

    @Override
    protected boolean isValidConfiguration() {
        return true; //TODO:
    }
}