package de.vanitasvitae.enigmandroid.layout;

import android.view.View;
import android.widget.Button;
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
        Button setPlugboardButton = (Button) main.findViewById(R.id.button_plugboard);
        setPlugboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PluggableDialogBuilder(getEnigma().getState()).showDialogPlugboard();
            }
        });

        Character[] rotorPositionArray = new Character[26];
        for(int i=0; i<26; i++) {rotorPositionArray[i] = (char) (65+i); /*Fill with A..Z*/}

        prepareSpinnerAdapter(rotor1View, R.array.rotors_1_8);
        prepareSpinnerAdapter(rotor2View, R.array.rotors_1_8);
        prepareSpinnerAdapter(rotor3View, R.array.rotors_1_8);
        prepareSpinnerAdapter(rotor4View, R.array.rotors_beta_gamma);
        prepareSpinnerAdapter(reflectorView, R.array.reflectors_b_c);
        prepareSpinnerAdapter(rotor1PositionView, rotorPositionArray);
        prepareSpinnerAdapter(rotor2PositionView, rotorPositionArray);
        prepareSpinnerAdapter(rotor3PositionView, rotorPositionArray);
        prepareSpinnerAdapter(rotor4PositionView, rotorPositionArray);
    }

    @Override
    public void resetLayout() {
        enigma = new Enigma_M4();
        setLayoutState(enigma.getState());
        output.setText("");
        input.setText("");
    }

    @Override
    public void setLayoutState(EnigmaStateBundle state) {
        this.rotor1View.setSelection(state.getTypeRotor1());
        this.rotor2View.setSelection(state.getTypeRotor2());
        this.rotor3View.setSelection(state.getTypeRotor3());
        this.rotor4View.setSelection(state.getTypeRotor4());
        this.reflectorView.setSelection(state.getTypeReflector());
        this.rotor1PositionView.setSelection(state.getRotationRotor1());
        this.rotor2PositionView.setSelection(state.getRotationRotor2());
        this.rotor3PositionView.setSelection(state.getRotationRotor3());
        this.rotor4PositionView.setSelection(state.getRotationRotor4());
    }

    @Override
    public void syncStateFromLayoutToEnigma() {
        EnigmaStateBundle state = getEnigma().getState();
        state.setTypeRotor1(rotor1View.getSelectedItemPosition());
        state.setTypeRotor2(rotor2View.getSelectedItemPosition());
        state.setTypeRotor3(rotor3View.getSelectedItemPosition());
        state.setTypeRotor4(rotor4View.getSelectedItemPosition());
        state.setTypeReflector(reflectorView.getSelectedItemPosition());
        state.setRotationRotor1(rotor1PositionView.getSelectedItemPosition());
        state.setRotationRotor2(rotor2PositionView.getSelectedItemPosition());
        state.setRotationRotor3(rotor3PositionView.getSelectedItemPosition());
        state.setRotationRotor4(rotor4PositionView.getSelectedItemPosition());
        getEnigma().setState(state);
    }

    @Override
    public void showRingSettingsDialog()
    {
        new RingSettingsDialogBuilder.RingSettingsDialogBuilderRotRotRotRot().
                createRingSettingsDialog(getEnigma().getState());
    }
}