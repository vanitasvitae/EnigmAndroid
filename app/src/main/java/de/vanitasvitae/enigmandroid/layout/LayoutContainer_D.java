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

import de.vanitasvitae.enigmandroid.MainActivity;
import de.vanitasvitae.enigmandroid.R;
import de.vanitasvitae.enigmandroid.enigma.EnigmaStateBundle;
import de.vanitasvitae.enigmandroid.enigma.Enigma_D;

/**
 * Concrete LayoutContainer for the D layout.
 * This class contains the layout and controls the layout elements such as spinners and stuff
 * Copyright (C) 2015  Paul Schaub
 */
public class LayoutContainer_D extends LayoutContainer
{
    private Enigma_D enigma;

    private Spinner rotor1PositionView;
    private Spinner rotor2PositionView;
    private Spinner rotor3PositionView;
    private Spinner reflectorPositionView;

    public LayoutContainer_D()
    {
        super();
        main.setTitle("D - EnigmAndroid");
        this.resetLayout();
    }

    @Override
    protected void setEnigmaLayout()
    {
        MainActivity.ActivitySingleton.getInstance().getActivity().setContentView(R.layout.activity_main_d);
    }

    @Override
    protected void assembleLayout()
    {
        this.rotor1PositionView = (Spinner) main.findViewById(R.id.rotor1position);
        this.rotor2PositionView = (Spinner) main.findViewById(R.id.rotor2position);
        this.rotor3PositionView = (Spinner) main.findViewById(R.id.rotor3position);
        this.reflectorPositionView = (Spinner) main.findViewById(R.id.reflector_position);
        Button reflectorWiring = (Button) main.findViewById(R.id.button_reflector);
        reflectorWiring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PluggableDialogBuilder(getEnigma().getState()).showDialogReflector();
            }
        });

        Character[] rotorPositionArray = new Character[26];
        for(int i=0; i<26; i++) {rotorPositionArray[i] = (char) (65+i); /*Fill with A..Z*/}

        prepareSpinnerAdapter(rotor1PositionView, rotorPositionArray);
        prepareSpinnerAdapter(rotor2PositionView, rotorPositionArray);
        prepareSpinnerAdapter(rotor3PositionView, rotorPositionArray);
        prepareSpinnerAdapter(reflectorPositionView, rotorPositionArray);
    }

    @Override
    public void resetLayout()
    {
        enigma = new Enigma_D();
        setLayoutState(enigma.getState());
        output.setText("");
        input.setText("");
    }

    @Override
    public void setLayoutState(EnigmaStateBundle state)
    {
        this.rotor1PositionView.setSelection(state.getRotationRotor1());
        this.rotor2PositionView.setSelection(state.getRotationRotor2());
        this.rotor3PositionView.setSelection(state.getRotationRotor3());
        this.reflectorPositionView.setSelection(state.getRotationReflector());
    }

    @Override
    public void syncStateFromLayoutToEnigma()
    {
        EnigmaStateBundle state = getEnigma().getState();
        state.setRotationRotor1(rotor1PositionView.getSelectedItemPosition());
        state.setRotationRotor2(rotor2PositionView.getSelectedItemPosition());
        state.setRotationRotor3(rotor3PositionView.getSelectedItemPosition());
        state.setRotationReflector(reflectorPositionView.getSelectedItemPosition());
        getEnigma().setState(state);
    }

    public Enigma_D getEnigma()
    {
        return this.enigma;
    }

    @Override
    public void showRingSettingsDialog()
    {
        new RingSettingsDialogBuilder.RingSettingsDialogBuilderRotRotRotRef().
                createRingSettingsDialog(getEnigma().getState());
    }
}
