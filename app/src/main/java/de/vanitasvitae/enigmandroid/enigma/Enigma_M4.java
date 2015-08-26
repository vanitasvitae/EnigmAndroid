package de.vanitasvitae.enigmandroid.enigma;

import android.util.Log;

import de.vanitasvitae.enigmandroid.enigma.rotors.Reflector;
import de.vanitasvitae.enigmandroid.enigma.rotors.Rotor;

/**
 * Concrete Implementation of the Enigma Machine type M4 of the german Kriegsmarine
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
public class Enigma_M4 extends Enigma
{
    private Rotor rotor1;
    private Rotor rotor2;
    private Rotor rotor3;

    private Rotor rotor4;
    private Reflector thinReflector;

    private Plugboard plugboard;

    public Enigma_M4()
    {
        machineType = "M4";
    }

    @Override
    public boolean setRotor(int pos, int type, int ringSetting, int rotation)
    {
        if(pos >= 1 && pos <= 3)
        {
            if(type >= 1 && type <= 8)
            {
                Rotor rotor = Rotor.createRotor(type, ringSetting, rotation);
                switch (pos)
                {
                    case 1: rotor1 = rotor;
                        break;
                    case 2: rotor2 = rotor;
                        break;
                    default: rotor3 = rotor;
                }
                return true;
            }
            else
            {
                Log.d("EnigmAndroid/M4/setRot", "Error: Type " + type + " at position " + pos);
                return false;
            }
        }
        //Thin rotor
        else if(pos == 4)
        {
            if(type >=9 && type <=10) {
                rotor4 = Rotor.createRotor(type, ringSetting, rotation);
                return true;
            }
        }
        Log.d("EnigmAndroid/M3/setRot", "Error: Type " + type + " at position " + pos);
        return false;
    }

    @Override
    public boolean setReflector(int type)
    {
        if(type >= 4 && type <=5)
        {
            this.thinReflector = Reflector.createReflector(type);
            return true;
        }
        Log.d("EnigmAndroid/M4/setRef","Error: Can't set type "+type);
        return false;
    }

    @Override
    public void initialize()
    {
        this.setPlugboard(new Plugboard());
        this.setConfiguration(new int[]{1,2,3,9,4,0,0,0,0,0,0,0,0});
        this.prefAnomaly = true;
    }

    @Override
    /**
     * Set the enigma into the next mechanical state.
     * This rotates the first rotor and eventually also the second/third.
     * Also this method handles the anomaly in case it should happen.
     */
    public void nextState()
    {
        //Rotate rotors
        rotor1.rotate();
        //Eventually turn next rotor (usual turnOver or anomaly)
        if (rotor1.isAtTurnoverPosition() || (this.doAnomaly && prefAnomaly))
        {
            rotor2.rotate();
            //Set doAnomaly for next call of encryptChar
            this.doAnomaly = rotor2.doubleTurnAnomaly();
            //Eventually rotate next rotor
            if (rotor2.isAtTurnoverPosition())
            {
                rotor3.rotate();
            }
        }
    }

    @Override
    /**
     * Substitute char k by sending the signal through the enigma.
     * The signal passes the plugboard, the rotors and returns back after going through the
     * reflector wheel.
     *
     * @param k input char
     * @return substituted output char
     */
    public char encryptChar(char k)
    {
        nextState();            //Rotate rotors
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
        x = normalize(x - rotor3.getRotation() + rotor3.getRingSetting() + rotor4.getRotation() - rotor4.getRingSetting());
        x = rotor4.encryptForward(x);
        x = normalize(x - rotor4.getRotation() + rotor4.getRingSetting());
        //backward direction
        x = thinReflector.encrypt(x);
        x = normalize(x + rotor4.getRotation() - rotor4.getRingSetting());
        x = rotor4.encryptBackward(x);
        x = normalize(x + rotor3.getRotation() - rotor3.getRingSetting() - rotor4.getRotation() + rotor4.getRingSetting());
        x = rotor3.encryptBackward(x);
        x = normalize(x + rotor2.getRotation() - rotor2.getRingSetting() - rotor3.getRotation() + rotor3.getRingSetting());
        x = rotor2.encryptBackward(x);
        x = normalize(x + rotor1.getRotation() - rotor1.getRingSetting() - rotor2.getRotation() + rotor2.getRingSetting());
        x = rotor1.encryptBackward(x);
        x = normalize(x - rotor1.getRotation() + rotor1.getRingSetting());
        x = plugboard.encrypt(x);
        return (char) (x + 65);     //Add Offset again and cast back to char
    }

    @Override
    public int normalize(int input) {
        return (input + 26) % 26;
    }

    @Override
    public void setPlugboard(Plugboard p)
    {
        this.plugboard = p;
    }

    @Override
    public int[] getConfiguration() {
        int[] configuration = new int[13];
        configuration[0] = rotor1.getType();
        configuration[1] = rotor2.getType();
        configuration[2] = rotor3.getType();
        configuration[3] = rotor4.getType();

        configuration[4] = thinReflector.getType();

        configuration[5] = rotor1.getRotation();
        configuration[6] = rotor2.getRotation();
        configuration[7] = rotor3.getRotation();
        configuration[8] = rotor4.getRotation();

        configuration[9] = rotor1.getRingSetting();
        configuration[10] = rotor2.getRingSetting();
        configuration[11] = rotor3.getRingSetting();

        configuration[12] = rotor4.getRingSetting();
        return configuration;
    }

    @Override
    public void setConfiguration(int[] conf)
    {
        if(conf.length != 13)
        {
            Log.d("EnigmAndroid/M4/setCon", "Invalid conf array length. conf array length " +
                    "should be 13 (is "+conf.length+")");
        }
        else
        {
            setRotor(1, conf[0], conf[9], conf[5]);
            setRotor(2, conf[1], conf[10], conf[6]);
            setRotor(3, conf[2], conf[11], conf[7]);
            setRotor(4, conf[3], conf[12], conf[8]);
            setReflector(conf[4]);
        }
    }
}
