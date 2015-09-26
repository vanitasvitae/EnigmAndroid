package de.vanitasvitae.enigmandroid.enigma;

import android.util.Log;

import java.security.SecureRandom;
import java.util.Random;

import de.vanitasvitae.enigmandroid.enigma.rotors.Reflector;
import de.vanitasvitae.enigmandroid.enigma.rotors.Rotor;

/**
 * Implementation of the Enigma machine of type G31 (Abwehr)
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
public class Enigma_G31 extends Enigma
{
    protected Rotor entryWheel;
    protected Rotor rotor1;
    protected Rotor rotor2;
    protected Rotor rotor3;

    protected Reflector reflector;

    public Enigma_G31()
    {
        super();
        machineType = "G31";
    }
    @Override
    public void initialize()
    {
        this.entryWheel = Rotor.createRotor(1, 0, 0);
        this.rotor1 = Rotor.createRotor(40, 0, 0);
        this.rotor2 = Rotor.createRotor(41, 0, 0);
        this.rotor3 = Rotor.createRotor(42, 0, 0);
        this.reflector = Reflector.createReflector(40);
    }

    @Override
    public void nextState()
    {
        Log.d("Anomaly",""+prefAnomaly+","+doAnomaly);
        rotor1.rotate();
        if (rotor1.isAtTurnoverPosition())
        {
            rotor2.rotate();
            if (rotor2.isAtTurnoverPosition())
            {
                rotor3.rotate();
                if(rotor3.isAtTurnoverPosition())
                {
                    reflector.setRotation(reflector.getRotation()+1);
                }
            }
        }
    }

    @Override
    public void randomState()
    {
        Random rand = new SecureRandom();

        int rotor1, rotor2=-1, rotor3=-1;
        rotor1 = rand.nextInt(3);
        while(rotor2 == -1 || rotor2 == rotor1) rotor2 = rand.nextInt(3);
        rotor3 = 3 - rotor1 - rotor2;

        int rot1 = rand.nextInt(26);
        int rot2 = rand.nextInt(26);
        int rot3 = rand.nextInt(26);
        int rotRef = rand.nextInt(26);
        int ring1 = rand.nextInt(26);
        int ring2 = rand.nextInt(26);
        int ring3 = rand.nextInt(26);
        int ringRef = rand.nextInt(26);

        this.rotor1 = Rotor.createRotor(40 + rotor1, rot1, ring1);
        this.rotor2 = Rotor.createRotor(40 + rotor2, rot2, ring2);
        this.rotor3 = Rotor.createRotor(40 + rotor3, rot3, ring3);
        this.reflector = Reflector.createReflector(40);
        reflector.setRotation(rotRef);
        reflector.setRingSetting(ringRef);
    }

    @Override
    public char encryptChar(char k) {
        nextState();
        int x = ((int) k)-65;   //Cast to int and remove Unicode Offset (A=65 in Unicode.)
        //Encryption
        //forward direction
        x = entryWheel.encryptForward(x);
        x = rotor1.normalize(x + rotor1.getRotation() - rotor1.getRingSetting());
        x = rotor1.encryptForward(x);
        x = rotor1.normalize(x - rotor1.getRotation() + rotor1.getRingSetting() + rotor2.getRotation() - rotor2.getRingSetting());
        x = rotor2.encryptForward(x);
        x = rotor1.normalize(x - rotor2.getRotation() + rotor2.getRingSetting() + rotor3.getRotation() - rotor3.getRingSetting());
        x = rotor3.encryptForward(x);
        x = rotor1.normalize(x - rotor3.getRotation() + rotor3.getRingSetting() + reflector.getRotation() - reflector.getRingSetting());
        //backward direction
        x = reflector.encrypt(x);
        x = rotor1.normalize(x + rotor3.getRotation() - rotor3.getRingSetting() - reflector.getRotation() + reflector.getRingSetting());
        x = rotor3.encryptBackward(x);
        x = rotor1.normalize(x + rotor2.getRotation() - rotor2.getRingSetting() - rotor3.getRotation() + rotor3.getRingSetting());
        x = rotor2.encryptBackward(x);
        x = rotor1.normalize(x + rotor1.getRotation() - rotor1.getRingSetting() - rotor2.getRotation() + rotor2.getRingSetting());
        x = rotor1.encryptBackward(x);
        x = rotor1.normalize(x - rotor1.getRotation() + rotor1.getRingSetting());
        x = entryWheel.encryptBackward(x);
        return (char) (x + 65);     //Add Offset again, cast back to char and return
    }

    @Override
    public void setState(EnigmaStateBundle state)
    {
        this.entryWheel = Rotor.createRotor(state.getTypeEntryWheel(), 0, 0);
        this.rotor1 = Rotor.createRotor(state.getTypeRotor1(), state.getRotationRotor1(), state.getRingSettingRotor1());
        this.rotor2 = Rotor.createRotor(state.getTypeRotor2(), state.getRotationRotor2(), state.getRingSettingRotor2());
        this.rotor3 = Rotor.createRotor(state.getTypeRotor3(), state.getRotationRotor3(), state.getRingSettingRotor3());
        this.reflector = Reflector.createReflector(state.getTypeReflector());
        this.reflector.setRotation(state.getRotationReflector());
        this.reflector.setRingSetting(state.getRingSettingReflector());
    }

    @Override
    public EnigmaStateBundle getState() {
        EnigmaStateBundle state = new EnigmaStateBundle();

        state.setTypeEntryWheel(entryWheel.getNumber());

        state.setTypeRotor1(rotor1.getNumber());
        state.setTypeRotor2(rotor2.getNumber());
        state.setTypeRotor3(rotor3.getNumber());

        state.setRotationRotor1(rotor1.getRotation());
        state.setRotationRotor2(rotor2.getRotation());
        state.setRotationRotor3(rotor3.getRotation());

        state.setRingSettingRotor1(rotor1.getRingSetting());
        state.setRingSettingRotor2(rotor2.getRingSetting());
        state.setRingSettingRotor3(rotor3.getRingSetting());

        state.setTypeReflector(reflector.getNumber());
        state.setRotationReflector(reflector.getRotation());
        state.setRingSettingReflector(reflector.getRingSetting());

        return state;
    }
}
