/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.vanitasvitae.enigmandroid.layout;

import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import de.vanitasvitae.enigmandroid.R;
import de.vanitasvitae.enigmandroid.enigma.EnigmaStateBundle;
import de.vanitasvitae.enigmandroid.enigma.Enigma_M3;

/**
 * Concrete LayoutContainer for the M3 layout.
 * This class contains the layout and controls the layout elements such as spinners and stuff
 * Copyright (C) 2015  Paul Schaub
 */
public class LayoutContainer_M3 extends LayoutContainer_I
{
    private Enigma_M3 enigma;

    public LayoutContainer_M3()
    {
        super();
        main.setTitle("M3 - EnigmAndroid");
        this.resetLayout();
    }

    @Override
    protected void assembleLayout()
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
                new PluggableDialogBuilder(getEnigma().getState()).showDialogPlugboard();
            }
        });

        Character[] rotorPositionArray = new Character[26];
        for(int i=0; i<26; i++) {rotorPositionArray[i] = (char) (65+i); /*Fill with A..Z*/}

        prepareSpinnerAdapter(rotor1View, R.array.rotors_1_8);
        prepareSpinnerAdapter(rotor2View, R.array.rotors_1_8);
        prepareSpinnerAdapter(rotor3View, R.array.rotors_1_8);
        prepareSpinnerAdapter(reflectorView, R.array.reflectors_b_c);
        prepareSpinnerAdapter(rotor1PositionView, rotorPositionArray);
        prepareSpinnerAdapter(rotor2PositionView, rotorPositionArray);
        prepareSpinnerAdapter(rotor3PositionView, rotorPositionArray);
    }

    @Override
    public void resetLayout()
    {
        enigma = new Enigma_M3();
        setLayoutState(enigma.getState());
        output.setText("");
        input.setText("");
    }

    @Override
    public void setLayoutState(EnigmaStateBundle state)
    {
        this.rotor1View.setSelection(state.getTypeRotor1());
        this.rotor2View.setSelection(state.getTypeRotor2());
        this.rotor3View.setSelection(state.getTypeRotor3());
        this.reflectorView.setSelection(state.getTypeReflector());
        this.rotor1PositionView.setSelection(state.getRotationRotor1());
        this.rotor2PositionView.setSelection(state.getRotationRotor2());
        this.rotor3PositionView.setSelection(state.getRotationRotor3());
    }

    @Override
    public void syncStateFromLayoutToEnigma()
    {
        EnigmaStateBundle state = getEnigma().getState();
        state.setTypeRotor1(rotor1View.getSelectedItemPosition());
        state.setTypeRotor2(rotor2View.getSelectedItemPosition());
        state.setTypeRotor3(rotor3View.getSelectedItemPosition());
        state.setTypeReflector(reflectorView.getSelectedItemPosition());
        state.setRotationRotor1(rotor1PositionView.getSelectedItemPosition());
        state.setRotationRotor2(rotor2PositionView.getSelectedItemPosition());
        state.setRotationRotor3(rotor3PositionView.getSelectedItemPosition());
        getEnigma().setState(state);
    }

    @Override
    public Enigma_M3 getEnigma()
    {
        return this.enigma;
    }
}