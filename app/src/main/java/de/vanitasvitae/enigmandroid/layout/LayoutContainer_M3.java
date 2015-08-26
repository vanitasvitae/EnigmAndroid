package de.vanitasvitae.enigmandroid.layout;

import android.widget.ArrayAdapter;

import de.vanitasvitae.enigmandroid.R;
import de.vanitasvitae.enigmandroid.enigma.Enigma_M3;

/**
 * Concrete LayoutContainer for the M3 layout.
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
public class LayoutContainer_M3 extends LayoutContainer_I
{
    public LayoutContainer_M3()
    {
        super();
        main.setTitle("M3 - EnigmAndroid");
        this.enigma = new Enigma_M3();
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
    }

    @Override
    public void updateLayout()
    {
        int[] conf = enigma.getConfiguration();
        rotor1View.setSelection(conf[0]-1);
        rotor2View.setSelection(conf[1]-1);
        rotor3View.setSelection(conf[2]-1);
        reflectorView.setSelection(conf[3]-2);      //B=2 -> 0
        rotor1PositionView.setSelection(conf[4]);
        rotor2PositionView.setSelection(conf[5]);
        rotor3PositionView.setSelection(conf[6]);
        ringSettings[0] = conf[7];
        ringSettings[1] = conf[8];
        ringSettings[2] = conf[9];
    }

    @Override
    public int[] createConfiguration()
    {
        int[] conf = new int[10];
        //Rotors 1..3
        conf[0] = rotor1View.getSelectedItemPosition() + 1;
        conf[1] = rotor2View.getSelectedItemPosition() + 1;
        conf[2] = rotor3View.getSelectedItemPosition() + 1;
        //Reflector
        conf[3] = reflectorView.getSelectedItemPosition() + 2;  //M3 has no B

        conf[4] = rotor1PositionView.getSelectedItemPosition();
        conf[5] = rotor2PositionView.getSelectedItemPosition();
        conf[6] = rotor3PositionView.getSelectedItemPosition();

        conf[7] = ringSettings[0];
        conf[8] = ringSettings[1];
        conf[9] = ringSettings[2];

        return conf;
    }
}