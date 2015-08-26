package de.vanitasvitae.enigmandroid.enigma;

import android.util.Log;

import de.vanitasvitae.enigmandroid.enigma.rotors.Reflector;
import de.vanitasvitae.enigmandroid.enigma.rotors.Rotor;

/**
 * Concrete implementation of an enigma machine model M3
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
public class Enigma_M3 extends Enigma_I
{
    public Enigma_M3()
    {
        machineType = "M3";
    }
    @Override
    public void initialize()
    {
        this.setPlugboard(new Plugboard());
        //I,II,III, B, 0,0,0, 0,0,0
        this.setConfiguration(new int[]{1,2,3,2,0,0,0,0,0,0});
    }
    @Override
    public boolean setRotor(int pos, int type, int ringSetting, int rotation)
    {
        if(pos >= 1 && pos <= 3) {
            if (type >= 1 && type <= 8) {
                Rotor rotor = Rotor.createRotor(type, ringSetting, rotation);
                switch (pos) {
                    case 1:
                        rotor1 = rotor;
                        break;
                    case 2:
                        rotor2 = rotor;
                        break;
                    default:
                        rotor3 = rotor;
                        break;
                }
                return true;
            }
        }
        Log.d("EnigmAndroid/M3/setRot", "Error: Type " + type + " at position " + pos);
        return false;
    }

    @Override
    public boolean setReflector(int type)
    {
        if(type >= 2 && type <= 3)
        {
            reflector = Reflector.createReflector(type);
            return true;
        }
        Log.d("EnigmAndroid/M3/setRef", "Error: Can't set type "+type);
        return false;
    }
}
