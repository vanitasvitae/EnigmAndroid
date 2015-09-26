package de.vanitasvitae.enigmandroid.layout;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import de.vanitasvitae.enigmandroid.R;
import de.vanitasvitae.enigmandroid.enigma.EnigmaStateBundle;
import de.vanitasvitae.enigmandroid.enigma.Enigma_I;

/**
 * Concrete LayoutContainer for the Enigma I layout.
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
public class LayoutContainer_I extends LayoutContainer
{
    private Enigma_I enigma;

    protected Spinner rotor1View;
    protected Spinner rotor2View;
    protected Spinner rotor3View;
    protected Spinner reflectorView;
    protected Spinner rotor1PositionView;
    protected Spinner rotor2PositionView;
    protected Spinner rotor3PositionView;

    public LayoutContainer_I()
    {
        super();
        main.setTitle("I - EnigmAndroid");
        this.resetLayout();
    }

    @Override
    protected void initializeLayout()
    {
        this.rotor1View = (Spinner) main.findViewById(R.id.rotor1);
        this.rotor2View = (Spinner) main.findViewById(R.id.rotor2);
        this.rotor3View = (Spinner) main.findViewById(R.id.rotor3);
        this.rotor1PositionView = (Spinner) main.findViewById(R.id.rotor1position);
        this.rotor2PositionView = (Spinner) main.findViewById(R.id.rotor2position);
        this.rotor3PositionView = (Spinner) main.findViewById(R.id.rotor3position);
        this.reflectorView = (Spinner) main.findViewById(R.id.reflector);
        Button setPlugboardButton = (Button) main.findViewById(R.id.button_plugboard);
        setPlugboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PluggableDialogBuilder(state).showDialogPlugboard();
            }
        });

        Character[] rotorPositionArray = new Character[26];
        for(int i=0; i<26; i++) {rotorPositionArray[i] = (char) (65+i); /*Fill with A..Z*/}

        prepareSpinnerAdapter(rotor1View, R.array.rotors_1_5);
        prepareSpinnerAdapter(rotor2View, R.array.rotors_1_5);
        prepareSpinnerAdapter(rotor3View, R.array.rotors_1_5);
        prepareSpinnerAdapter(reflectorView, R.array.reflectors_a_c);
        prepareSpinnerAdapter(rotor1PositionView, rotorPositionArray);
        prepareSpinnerAdapter(rotor2PositionView, rotorPositionArray);
        prepareSpinnerAdapter(rotor3PositionView, rotorPositionArray);
    }

    @Override
    public void resetLayout()
    {
        enigma = new Enigma_I();
        setLayoutState(enigma.getState());
        output.setText("");
        input.setText("");
    }

    @Override
    public void setLayoutState(EnigmaStateBundle state)
    {
        this.state = state;
        this.rotor1View.setSelection(state.getTypeRotor1() - 10);
        this.rotor2View.setSelection(state.getTypeRotor2() - 10);
        this.rotor3View.setSelection(state.getTypeRotor3() - 10);
        this.reflectorView.setSelection(state.getTypeReflector() - 10);
        this.rotor1PositionView.setSelection(state.getRotationRotor1());
        this.rotor2PositionView.setSelection(state.getRotationRotor2());
        this.rotor3PositionView.setSelection(state.getRotationRotor3());
    }

    @Override
    protected void refreshState()
    {
        state.setTypeRotor1(rotor1View.getSelectedItemPosition() + 10);
        state.setTypeRotor2(rotor2View.getSelectedItemPosition() + 10);
        state.setTypeRotor3(rotor3View.getSelectedItemPosition() + 10);
        state.setTypeReflector(reflectorView.getSelectedItemPosition() + 10);
        state.setRotationRotor1(rotor1PositionView.getSelectedItemPosition());
        state.setRotationRotor2(rotor2PositionView.getSelectedItemPosition());
        state.setRotationRotor3(rotor3PositionView.getSelectedItemPosition());
    }

    public Enigma_I getEnigma()
    {
        return this.enigma;
    }

    @Override
    public void showRingSettingsDialog()
    {
        new RingSettingsDialogBuilder.RingSettingsDialogBuilderRotRotRot().
                createRingSettingsDialog(state);
    }
}