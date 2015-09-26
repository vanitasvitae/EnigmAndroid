package de.vanitasvitae.enigmandroid.enigma;

import java.security.SecureRandom;
import java.util.Random;

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

    private Reflector reflector;

    private Plugboard plugboard;

    public Enigma_M4()
    {
        super();
        machineType = "M4";
    }

    @Override
    public void initialize()
    {
        this.plugboard = new Plugboard();
        this.rotor1 = Rotor.createRotor(30, 0, 0);
        this.rotor2 = Rotor.createRotor(31, 0, 0);
        this.rotor3 = Rotor.createRotor(32, 0, 0);
        this.rotor4 = Rotor.createRotor(38, 0, 0);
        this.reflector = Reflector.createReflector(30);
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
    public void randomState()
    {
        Random rand = new SecureRandom();

        int rotor1, rotor2=-1, rotor3=-1;
        int rotor4;
        int ref;
        rotor1 = rand.nextInt(8);
        while(rotor2 == -1 || rotor2 == rotor1) rotor2 = rand.nextInt(8);
        while(rotor3 == -1 || rotor3 == rotor2 || rotor3 == rotor1) rotor3 = rand.nextInt(8);
        rotor4 = rand.nextInt(2);
        ref = rand.nextInt(2);

        int rot1 = rand.nextInt(26);
        int rot2 = rand.nextInt(26);
        int rot3 = rand.nextInt(26);
        int rot4 = rand.nextInt(26);
        int rotRef = rand.nextInt(26);
        int ring1 = rand.nextInt(26);
        int ring2 = rand.nextInt(26);
        int ring3 = rand.nextInt(26);
        int ring4 = rand.nextInt(26);
        int ringRef = rand.nextInt(26);

        this.rotor1 = Rotor.createRotor(30 + rotor1, rot1, ring1);
        this.rotor2 = Rotor.createRotor(30 + rotor2, rot2, ring2);
        this.rotor3 = Rotor.createRotor(30 + rotor3, rot3, ring3);
        this.rotor4 = Rotor.createRotor(38 + rotor4, rot4, ring4);
        this.reflector = Reflector.createReflector(30 + ref);
        reflector.setRotation(rotRef);
        reflector.setRingSetting(ringRef);
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
        x = rotor1.normalize(x + rotor1.getRotation() - rotor1.getRingSetting());
        x = rotor1.encryptForward(x);
        x = rotor1.normalize(x - rotor1.getRotation() + rotor1.getRingSetting() + rotor2.getRotation() - rotor2.getRingSetting());
        x = rotor2.encryptForward(x);
        x = rotor1.normalize(x - rotor2.getRotation() + rotor2.getRingSetting() + rotor3.getRotation() - rotor3.getRingSetting());
        x = rotor3.encryptForward(x);
        x = rotor1.normalize(x - rotor3.getRotation() + rotor3.getRingSetting() + rotor4.getRotation() - rotor4.getRingSetting());
        x = rotor4.encryptForward(x);
        x = rotor1.normalize(x - rotor4.getRotation() + rotor4.getRingSetting());
        //backward direction
        x = reflector.encrypt(x);
        x = rotor1.normalize(x + rotor4.getRotation() - rotor4.getRingSetting());
        x = rotor4.encryptBackward(x);
        x = rotor1.normalize(x + rotor3.getRotation() - rotor3.getRingSetting() - rotor4.getRotation() + rotor4.getRingSetting());
        x = rotor3.encryptBackward(x);
        x = rotor1.normalize(x + rotor2.getRotation() - rotor2.getRingSetting() - rotor3.getRotation() + rotor3.getRingSetting());
        x = rotor2.encryptBackward(x);
        x = rotor1.normalize(x + rotor1.getRotation() - rotor1.getRingSetting() - rotor2.getRotation() + rotor2.getRingSetting());
        x = rotor1.encryptBackward(x);
        x = rotor1.normalize(x - rotor1.getRotation() + rotor1.getRingSetting());
        x = plugboard.encrypt(x);
        return (char) (x + 65);     //Add Offset again and cast back to char
    }

    @Override
    public void setState(EnigmaStateBundle state)
    {
        rotor1 = Rotor.createRotor(state.getTypeRotor1(), state.getRotationRotor1(), state.getRingSettingRotor1());
        rotor2 = Rotor.createRotor(state.getTypeRotor2(), state.getRotationRotor2(), state.getRingSettingRotor2());
        rotor3 = Rotor.createRotor(state.getTypeRotor3(), state.getRotationRotor3(), state.getRingSettingRotor3());
        rotor4 = Rotor.createRotor(state.getTypeRotor4(), state.getRotationRotor4(), state.getRingSettingRotor4());
        reflector = Reflector.createReflector(state.getTypeReflector());
        plugboard.setConfiguration(state.getConfigurationPlugboard());

    }

    @Override
    public EnigmaStateBundle getState()
    {
        EnigmaStateBundle state = new EnigmaStateBundle();
        state.setTypeRotor1(rotor1.getNumber());
        state.setTypeRotor2(rotor2.getNumber());
        state.setTypeRotor3(rotor3.getNumber());
        state.setTypeRotor4(rotor4.getNumber());

        state.setRotationRotor1(rotor1.getRotation());
        state.setRotationRotor2(rotor2.getRotation());
        state.setRotationRotor3(rotor3.getRotation());
        state.setRotationRotor4(rotor4.getRotation());

        state.setRingSettingRotor1(rotor1.getRingSetting());
        state.setRingSettingRotor2(rotor2.getRingSetting());
        state.setRingSettingRotor3(rotor3.getRingSetting());
        state.setRingSettingRotor4(rotor4.getRingSetting());

        state.setTypeReflector(reflector.getNumber());

        state.setConfigurationPlugboard(plugboard.getConfiguration());

        return state;
    }
}
