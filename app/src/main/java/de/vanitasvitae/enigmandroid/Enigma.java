package de.vanitasvitae.enigmandroid;

import de.vanitasvitae.enigmandroid.rotors.Reflector;
import de.vanitasvitae.enigmandroid.rotors.Rotor;

/**
 * Main component of the Enigma machine
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
public class Enigma
{
    private Plugboard plugboard;

    //Slots for the rotors
    private Rotor r1;
    private Rotor r2;
    private Rotor r3;

    //Slot for the reflector
    private Reflector reflector;

    private boolean doAnomaly = false;  //Has the time come to handle an anomaly?

    private boolean prefAnomaly;  //Do you WANT to simulate the anomaly?
    private InputPreparator inputPreparator;

    /**
     * Create new Enigma with standard configuration.
     * Empty Plugboard, reflector B, rotors I,II,III, Positions 0,0,0
     */
    public Enigma()
    {
        initialize();
    }

    private void initialize()
    {
        this.r1 = Rotor.createRotorI(0, 0);
        this.r2 = Rotor.createRotorII(0, 0);
        this.r3 = Rotor.createRotorIII(0, 0);
        this.reflector = Reflector.createReflectorB();
        plugboard = new Plugboard();
        prefAnomaly = true; //TODO: Is this necessary?
    }

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
     * Substitute char k by sending the signal through the enigma.
     * This rotates the first rotor and eventually also the second/third beforehand.
     * Also this method handles the anomaly in case it should happen
     *
     * @param k input char
     * @return substituted output char
     */
    public char encryptChar(char k)
    {
        //Rotate rotors
        r1.rotate();
        //Eventually turn next rotor (usual turnOver or anomaly)
        if (r1.isAtTurnoverPosition() || (this.doAnomaly && prefAnomaly))
        {
            r2.rotate();
            //Set doAnomaly for next call of encryptChar
            this.doAnomaly = r2.doubleTurnAnomaly();
            //Eventually rotate next rotor
            if (r2.isAtTurnoverPosition())
            {
                r3.rotate();
            }
        }
        int x = ((int) k)-65;   //Cast to int and remove Unicode Offset (A=65 in Unicode.)

        //Encryption
        //forward direction
        x = plugboard.encrypt(x);
        x = normalize(x + r1.getRotation());
        x = r1.encryptForward(x);
        x = normalize(x + r2.getRotation() - r1.getRotation());
        x = r2.encryptForward(x);
        x = normalize(x + r3.getRotation() - r2.getRotation());
        x = r3.encryptForward(x);
        x = normalize(x - r3.getRotation());
        //backward direction
        x = reflector.encrypt(x);
        x = normalize(x + r3.getRotation());
        x = r3.encryptBackward(x);
        x = normalize(x - r3.getRotation() + r2.getRotation());
        x = r2.encryptBackward(x);
        x = normalize(x - r2.getRotation() + r1.getRotation());
        x = r1.encryptBackward(x);
        x = normalize(x - r1.getRotation());
        x = plugboard.encrypt(x);

        return (char) (x + 65);     //Add Offset again and cast back to char
    }

    /**
     * Normalize the input.
     * Normalizing means keeping the input via modulo in the range from 0 to n-1, where n is equal
     * to the size of the rotors (in this case fixed to 26, TODO?).
     * This is necessary since java allows negative modulo values,
     * which can break this implementation
     * @param input input signal
     * @return "normalized" input signal
     */
    public int normalize(int input)
    {
        return (26+input)%26;
    }

    /**
     * Set config of the enigma
     *
     * @param conf configuration
     */
    public void setConfiguration(int[] conf)
    {
        if (conf == null || conf.length != 10)
        {
            initialize();
        }
        else
        {
            int ro1 = conf[0];
            int ro2 = conf[1];
            int ro3 = conf[2];
            int ref = conf[3];
            int ro1rot = normalize(conf[4] - 1);
            int ro2rot = normalize(conf[5] - 1);
            int ro3rot = normalize(conf[6] - 1);
            int ro1Ring = conf[7];
            int ro2Ring = conf[8];
            int ro3Ring = conf[9];

            //Set rotors
            r1 = Rotor.createRotor(ro1, ro1Ring, ro1rot);
            r2 = Rotor.createRotor(ro2, ro2Ring, ro2rot);
            r3 = Rotor.createRotor(ro3, ro3Ring, ro3rot);

            //Set reflector
            reflector = Reflector.createReflector(ref);

            //catch double turn anomaly on step by step basis
            this.doAnomaly = r2.doubleTurnAnomaly();
        }
    }

    /**
     * Set the plugboard
     * @param p Plugboard
     */
    public void setPlugboard(Plugboard p)
    {
        this.plugboard = p;
    }

    /**
     * Return the configuration, the enigma machine is in right NOW
     *
     * @return array containing configuration
     */
    public int[] getConfiguration()
    {
        int[] configuration = new int[10];
        {
            configuration[0] = r1.getType();
            configuration[1] = r2.getType();
            configuration[2] = r3.getType();
            configuration[3] = reflector.getType();
            configuration[4] = r1.getRotation();
            configuration[5] = r2.getRotation();
            configuration[6] = r3.getRotation();
            configuration[7] = r1.getRingSetting();
            configuration[8] = r2.getRingSetting();
            configuration[9] = r3.getRingSetting();
        }
        return configuration;
    }

    /**
     * Set the inputPreparator
     * @param preparator concrete InputPreparator
     */
    public void setInputPreparator(InputPreparator preparator)
    {
        this.inputPreparator = preparator;
    }

    /**
     * Return the inputPreparator
     * @return inputPreparator
     */
    public InputPreparator getInputPreparator()
    {
        return this.inputPreparator;
    }

    /**
     * set prefAnomaly variable
     * @param b boolean
     */
    public void setPrefAnomaly(boolean b)
    {
        this.prefAnomaly = b;
    }
}
