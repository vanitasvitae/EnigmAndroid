package de.vanitasvitae.enigmandroid.enigma;

import java.security.SecureRandom;
import java.util.Random;

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
    protected int machineTypeOffset = 0;
    protected boolean doAnomaly = false;  //Has the time come to handle an anomaly?
    protected boolean prefAnomaly;  //Do you WANT to simulate the anomaly?
    protected Random rand;
    public Enigma(int off)
    {
        this.machineTypeOffset = off;
        initialize();
    }

    public Enigma()
    {
        initialize();
    }

    public int getMachineTypeOffset()
    {
        return machineTypeOffset;
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
     * Set the enigma into a completely random state using a unseeded SecureRandom object.
     */
    public void randomState()
    {
        this.rand = new SecureRandom();
        generateState();
    }

    /**
     * Set the enigma to a random state based on the initialization of rand.
     * Don not choose a rotor twice, set random rotations, ringSettings, ukw and possibly
     * plugboard / rewirable ukw configurations.
     */
     protected abstract void generateState();

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
     * Set the rand into a certain state based on seed.
     * Then set the enigmas state.
     * @param seed
     */
    public void setStateFromSeed(String seed)
    {
        rand = new Random(seed.hashCode());
        generateState();
    }

    public abstract void restoreState(String mem);

    public abstract String stateToString();

    public static String numToMachineType(int n)
    {
        switch (n) {
            case 0: return "I";
            case 1: return "M3";
            case 2: return "M4";
            case 3: return "G31";
            case 4: return "G312";
            case 5: return "G260";
            case 6: return "D";
            case 7: return "K";
            case 8: return "KS";
            case 9: return "KSA";
            case 10: return "R";
            default: return "T";
        }
    }

    public static String chooseEnigmaFromSeed(String seed)
    {
        return numToMachineType(seed.hashCode() % 12);
    }

    public static String chooseEnigmaFromSave(String save)
    {
        int index = save.indexOf(":");
        if(index != -1) save = save.substring(0, index);
        long s = Long.valueOf(save);
        return numToMachineType(getValue(s,12));
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

    /**
     *
     * @param s source
     * @param d domain (max value) of the value
     * @return value
     */
    protected static int getValue(long s, int d)
    {
        return (int) ((s%d)+d)%d;
    }

    /**
     * remove a digit of domain d from source s
     * @param s source
     * @param d domain (max value)
     * @return trimmed source
     */
    protected static long removeDigit(long s, int d)
    {
        return (s-(s%d))/d;
    }

    /**
     *
     * @param s source
     * @param b base (max value)
     * @param v actual value
     * @return lengthened source
     */
    protected static long addDigit(long s, int v, int b)
    {
        long x = s;
        x*=b;
        x+=(v%b);
        return x;
    }
}
