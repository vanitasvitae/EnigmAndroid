package de.vanitasvitae.enigmandroid.enigma;

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

    public Enigma()
    {
        initialize();
    }

    /**
     * Set the enigma to an initial state
     */
    public abstract void initialize();

    /**
     * Encrypt / Decrypt a given String w.
     * w must be prepared using prepare(w) beforehand.
     * Doing so changes the state of the rotors but not the state of the plugboard and the
     * ringSettings
     *
     * @param w Text to decrypt/encryptString
     * @return encrypted/decrypted string
     */
    public String encryptString(String w)
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
     * Set the enigma to a random state.
     * Don not choose a rotor twice, set random rotations, ringSettings, ukw and possibly
     * plugboard / rewirable ukw configurations.
     */
    public abstract void randomState();

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
     * Set the state of the enigma
     * @param state new state
     */
    public abstract void setState(EnigmaStateBundle state);

    /**
     * Return an object representing the current state of the enigma
     * @return state
     */
    public abstract EnigmaStateBundle getState();

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
