package de.vanitasvitae.enigmandroid.enigma;

import android.util.Log;

import de.vanitasvitae.enigmandroid.enigma.rotors.Reflector;
import de.vanitasvitae.enigmandroid.enigma.rotors.Rotor;

/**
 * Concrete implementation of an enigma machine of type I
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
public class Enigma_I extends Enigma {
    protected Rotor rotor1;
    protected Rotor rotor2;
    protected Rotor rotor3;

    protected Reflector reflector;

    protected Plugboard plugboard;

    public Enigma_I() {
        machineType = "I";
    }

    @Override
    public void initialize()
    {
        this.setPlugboard(new Plugboard());
        //I,II,III, A, 0,0,0, 0,0,0
        this.setConfiguration(new int[]{1,2,3,1,0,0,0,0,0,0});
    }

    @Override
    public void nextState()
    {
        rotor1.rotate();
        if (rotor1.isAtTurnoverPosition() || (this.doAnomaly && prefAnomaly))
        {
            rotor2.rotate();
            this.doAnomaly = rotor2.doubleTurnAnomaly();
            if (rotor2.isAtTurnoverPosition())
            {
                rotor3.rotate();
            }
        }
    }

    @Override
    public char encryptChar(char k)
    {
        nextState();
        int x = ((int) k)-65;   //Cast to int and remove Unicode Offset (A=65 in Unicode.)
        //Encryption
        //forward direction
        x = plugboard.encrypt(x);
        x = normalize(x + rotor1.getRotation() - rotor1.getRingSetting());
        x = rotor1.encryptForward(x);
        x = normalize(x - rotor1.getRotation() + rotor1.getRingSetting() + rotor2.getRotation() - rotor2.getRingSetting());
        x = rotor2.encryptForward(x);
        x = normalize(x - rotor2.getRotation() + rotor2.getRingSetting() + rotor3.getRotation() - rotor3.getRingSetting());
        x = rotor3.encryptForward(x);
        x = normalize(x - rotor3.getRotation() + rotor3.getRingSetting());
       //TODO: CHECK
        //backward direction
        x = reflector.encrypt(x);
        x = normalize(x + rotor3.getRotation() - rotor3.getRingSetting());
        x = rotor3.encryptBackward(x);
        x = normalize(x + rotor2.getRotation() - rotor2.getRingSetting() - rotor3.getRotation() + rotor3.getRingSetting());
        x = rotor2.encryptBackward(x);
        x = normalize(x + rotor1.getRotation() - rotor1.getRingSetting() - rotor2.getRotation() + rotor2.getRingSetting());
        x = rotor1.encryptBackward(x);
        x = normalize(x - rotor1.getRotation() + rotor1.getRingSetting());
        x = plugboard.encrypt(x);
        return (char) (x + 65);     //Add Offset again, cast back to char and return
    }

    @Override
    public int normalize(int input) {
        return (input+26) % 26;
    }

    @Override
    public void setPlugboard(Plugboard p)
    {
        this.plugboard = p;
    }

    @Override
    public int[] getConfiguration()
    {
        int[] conf = new int[10];
        conf[0] = rotor1.getType();
        conf[1] = rotor2.getType();
        conf[2] = rotor3.getType();
        conf[3] = reflector.getType();
        conf[4] = rotor1.getRotation();
        conf[5] = rotor2.getRotation();
        conf[6] = rotor3.getRotation();
        conf[7] = rotor1.getRingSetting();
        conf[8] = rotor2.getRingSetting();
        conf[9] = rotor3.getRingSetting();
        return conf;
    }

    @Override
    /**
     * conf:
     * 0..2 -> rotor1..rotor3 type
     * 3 -> reflector type
     * 4..6 -> rotor1..rotor3 rotation
     * 7..9 -> rotor1..rotor3 ringSetting
     */
    public void setConfiguration(int[] conf)
    {
        this.setRotor(1,conf[0],conf[7],conf[4]);
        this.setRotor(2,conf[1],conf[8],conf[5]);
        this.setRotor(3,conf[2],conf[9],conf[6]);
        this.setReflector(conf[3]);
    }

    @Override
    public boolean setRotor(int pos, int type, int ringSetting, int rotation)
    {
        if(pos >= 1 && pos <= 3) {
            if (type >= 1 && type <= 5) {
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
        if(type >= 1 && type <= 3)
        {
            reflector = Reflector.createReflector(type);
            return true;
        }
        Log.d("EnigmAndroid/I/setRef", "Error: Can't set type "+type);
        return false;
    }
}
