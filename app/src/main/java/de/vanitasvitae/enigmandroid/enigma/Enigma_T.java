package de.vanitasvitae.enigmandroid.enigma;

import android.util.Log;

import java.security.SecureRandom;
import java.util.Random;

import de.vanitasvitae.enigmandroid.enigma.rotors.Reflector;
import de.vanitasvitae.enigmandroid.enigma.rotors.Rotor;

/**
 * Implementation of the Enigma machine of type T Tirpitz
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
public class Enigma_T extends Enigma
{
    protected Rotor entryWheel;
    protected Rotor rotor1;
    protected Rotor rotor2;
    protected Rotor rotor3;
    protected Reflector reflector;

    protected static int machineTypeOffset = 120;

    public Enigma_T()
    {
        super();
        machineType = "T";
    }

    @Override
    public void initialize() {
        this.entryWheel = Rotor.createRotor(2,0,0);
        this.rotor1 = Rotor.createRotor(machineTypeOffset, 0, 0);
        this.rotor2 = Rotor.createRotor(machineTypeOffset+1, 0, 0);
        this.rotor3 = Rotor.createRotor(machineTypeOffset+2, 0, 0);
        this.reflector = Reflector.createReflector(machineTypeOffset);
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
    protected void generateState() {
        int rotor1, rotor2=-1, rotor3=-1;
        rotor1 = rand.nextInt(8);
        while(rotor2 == -1 || rotor2 == rotor1) rotor2 = rand.nextInt(8);
        while(rotor3 == -1 || rotor3 == rotor2 || rotor3 == rotor1) rotor3 = rand.nextInt(8);

        int rot1 = rand.nextInt(26);
        int rot2 = rand.nextInt(26);
        int rot3 = rand.nextInt(26);
        int rotRef = rand.nextInt(26);
        int ring1 = rand.nextInt(26);
        int ring2 = rand.nextInt(26);
        int ring3 = rand.nextInt(26);
        int ringRef = rand.nextInt(26);

        this.rotor1 = Rotor.createRotor(machineTypeOffset + rotor1, rot1, ring1);
        this.rotor2 = Rotor.createRotor(machineTypeOffset + rotor2, rot2, ring2);
        this.rotor3 = Rotor.createRotor(machineTypeOffset + rotor3, rot3, ring3);
        this.reflector = Reflector.createReflector(machineTypeOffset);
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
    public void setState(EnigmaStateBundle state) {
        this.entryWheel = Rotor.createRotor(state.getTypeEntryWheel(), 0, 0);
        this.rotor1 = Rotor.createRotor(state.getTypeRotor1(), state.getRotationRotor1(), state.getRingSettingRotor1());
        this.rotor2 = Rotor.createRotor(state.getTypeRotor2(), state.getRotationRotor2(), state.getRingSettingRotor2());
        this.rotor3 = Rotor.createRotor(state.getTypeRotor3(), state.getRotationRotor3(), state.getRingSettingRotor3());
        this.reflector = Reflector.createReflector(state.getTypeReflector());
        this.reflector.setRotation(state.getRotationReflector());
        this.reflector.setRingSetting(state.getRingSettingReflector());
    }

    @Override
    public EnigmaStateBundle getState()
    {
        EnigmaStateBundle state = new EnigmaStateBundle();

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
        state.setTypeEntryWheel(entryWheel.getNumber());

        return state;
    }

    @Override
    public void restoreState(String mem)
    {
        long s = Long.valueOf(mem);
        s = removeDigit(s,12);  //Remove machine type
        int r1 = getValue(s,10);
        s = removeDigit(s,10);
        int r2 = getValue(s,10);
        s = removeDigit(s,10);
        int r3 = getValue(s,10);
        s = removeDigit(s,10);

        int rot1 = getValue(s,26);
        s = removeDigit(s,26);
        int ring1 = getValue(s,26);
        s = removeDigit(s,26);
        int rot2 = getValue(s,26);
        s = removeDigit(s,26);
        int ring2 = getValue(s,26);
        s = removeDigit(s,26);
        int rot3 = getValue(s,26);
        s = removeDigit(s,26);
        int ring3 = getValue(s,26);
        s = removeDigit(s,26);
        int rotRef = getValue(s,26);
        s = removeDigit(s,26);
        int ringRef = getValue(s,26);

        this.rotor1 = Rotor.createRotor(machineTypeOffset + r1, rot1, ring1);
        this.rotor2 = Rotor.createRotor(machineTypeOffset + r2, rot2, ring2);
        this.rotor3 = Rotor.createRotor(machineTypeOffset + r3, rot3, ring3);
        this.reflector = Reflector.createReflector(machineTypeOffset);
        this.reflector.setRotation(rotRef);
        this.reflector.setRingSetting(ringRef);
    }

    @Override
    public String stateToString()
    {
        String save = "";
        long t = reflector.getRingSetting();
        t = addDigit(t, reflector.getRotation(), 26);
        t = addDigit(t, rotor3.getRingSetting(),26);
        t = addDigit(t, rotor3.getRotation(), 26);
        t = addDigit(t, rotor2.getRingSetting(),26);
        t = addDigit(t, rotor2.getRotation(), 26);
        t = addDigit(t, rotor1.getRingSetting(), 26);
        t = addDigit(t, rotor1.getRotation(), 26);
        t = addDigit(t, rotor3.getNumber(), 10);
        t = addDigit(t, rotor2.getNumber(), 10);
        t = addDigit(t, rotor1.getNumber(), 10);
        t = addDigit(t, 11, 12); //Machine #11

        save = save+t;
        return save;
    }
}
