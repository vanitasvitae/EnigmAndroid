package de.vanitasvitae.enigmandroid.enigma;

import de.vanitasvitae.enigmandroid.MainActivity;
import de.vanitasvitae.enigmandroid.enigma.inputPreparer.InputPreparer;

/**
 * Main component of the Enigma machine
 * This is the mostly abstract base of any enigma machine.
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
public abstract class Enigma
{
    protected static String machineType;
    protected boolean doAnomaly = false;  //Has the time come to handle an anomaly?
    protected boolean prefAnomaly;  //Do you WANT to simulate the anomaly?
    protected InputPreparer inputPreparer;

    /**
     * Set the enigma to an initial state
     */
    public abstract void initialize();

    /**
     * Set the reflector of the enigma machine to one of type type.
     * @param type type indicator of the reflector
     * @return success (not every reflector fits in every machine)
     */
    public abstract  boolean setReflector(int type);

    /**
     * Set the rotor on position pos to a rotor of type type with ring-setting ringSetting and
     * rotation rotation.
     * @param pos position of the rotor
     * @param type type indicator
     * @param ringSetting ringSetting
     * @param rotation rotation
     * @return success
     */
    public abstract boolean setRotor(int pos, int type, int ringSetting, int rotation);

    /**
     * Encrypt / Decrypt a given String w.
     * w must be prepared using prepare(w) beforehand.
     * Doing so changes the state of the rotors but not the state of the plugboard and the
     * ringSettings
     *
     * @param w Text to decrypt/encrypt
     * @return encrypted/decrypted string
     */
    public String encrypt(String w)
    {
        //output string
        String output = "";
        //for each char x in k
        for (char x : w.toCharArray())
        {
            output = output + this.encryptChar(x);
        }
        //return en-/decrypted string
        return output;
    }

    /**
     * Set the enigma into the next mechanical state.
     * This rotates the first rotor and eventually also the second/third.
     * Also this method handles the anomaly in case it should happen.
     */
    public abstract void nextState();

    /**
     * Substitute char k by sending the signal through the enigma.
     * The signal passes the plugboard, the rotors and returns back after going through the
     * reflector wheel.
     *
     * @param k input char
     * @return substituted output char
     */
    public abstract char encryptChar(char k);

    /**
     * Normalize the input.
     * Normalizing means keeping the input via modulo in the range from 0 to n-1, where n is equal
     * to the size of the rotors.
     * This is necessary since java allows negative modulo values,
     * which can break this implementation
     * @param input input signal
     * @return "normalized" input signal
     */
    public abstract int normalize(int input);

    /**
     * Set config of the enigma
     *
     * @param conf configuration
     */
    public abstract void setConfiguration(int[] conf);

    /**
     * Set the plugboard
     * @param p Plugboard
     */
    public abstract void setPlugboard(Plugboard p);

    /**
     * Return the configuration, the enigma machine is in right NOW
     *
     * @return array containing configuration
     */
    public abstract int[] getConfiguration();

    /**
     * Set the inputPreparer
     * @param preparer concrete InputPreparer
     */
    public void setInputPreparer(InputPreparer preparer)
    {
        this.inputPreparer = preparer;
    }



    /**
     * Return the inputPreparer
     * @return inputPreparer
     */
    public InputPreparer getInputPreparer()
    {
        if(inputPreparer == null)
        {
            MainActivity main = (MainActivity) MainActivity.ActivitySingleton.getInstance().getActivity();
            inputPreparer = InputPreparer.createInputPreparer(main.getPrefNumericLanguage());
        }
        return this.inputPreparer;
    }

    /**
     * set prefAnomaly variable
     * @param b boolean
     */
    public void setPrefAnomaly(boolean b)
    {
        this.prefAnomaly = b;
    }

    /**
     * Return the type indicator of the enigma machine
     * @return type
     */
    public String getMachineType()
    {
        return machineType;
    }
}
