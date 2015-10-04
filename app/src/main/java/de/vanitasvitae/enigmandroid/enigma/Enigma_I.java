package de.vanitasvitae.enigmandroid.enigma;

import java.math.BigInteger;

import de.vanitasvitae.enigmandroid.enigma.rotors.EntryWheel;
import de.vanitasvitae.enigmandroid.enigma.rotors.Reflector;
import de.vanitasvitae.enigmandroid.enigma.rotors.Rotor;

/**
 * Concrete implementation of an enigma machine of name I
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
public class Enigma_I extends Enigma
{
    protected EntryWheel entryWheel;
    protected Rotor rotor1;
    protected Rotor rotor2;
    protected Rotor rotor3;
    protected Reflector reflector;

    protected Plugboard plugboard;

    public Enigma_I()
    {
        super();
        machineType = "I";
    }

    @Override
    protected void establishAvailableParts() {
        addAvailableEntryWheel(new EntryWheel.EntryWheel_ABCDEF());
        addAvailableRotor(new Rotor.Rotor_I(0, 0));
        addAvailableRotor(new Rotor.Rotor_II(0,0));
        addAvailableRotor(new Rotor.Rotor_III(0,0));
        addAvailableRotor(new Rotor.Rotor_IV(0,0));
        addAvailableRotor(new Rotor.Rotor_V(0,0));
        addAvailableReflector(new Reflector.Reflector_A());
        addAvailableReflector(new Reflector.Reflector_B());
        addAvailableReflector(new Reflector.Reflector_C());
    }

    @Override
    public void initialize()
    {
        this.plugboard= new Plugboard();
        this.entryWheel = getEntryWheel(0);
        this.rotor1 = getRotor(0, 0, 0);
        this.rotor2 = getRotor(1, 0, 0);
        this.rotor3 = getRotor(2, 0, 0);
        this.reflector = getReflector(0);
    }

    @Override
    public void nextState()
    {
        rotor1.rotate();
        if (rotor1.isAtTurnoverPosition() || this.doAnomaly)
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
        int r1, r2=-1, r3=-1;
        r1 = rand.nextInt(5);
        while(r2 == -1 || r2 == r1) r2 = rand.nextInt(5);
        while(r3 == -1 || r3 == r2 || r3 == r1) r3 = rand.nextInt(5);
        int ref = rand.nextInt(3);

        int rot1 = rand.nextInt(26);
        int rot2 = rand.nextInt(26);
        int rot3 = rand.nextInt(26);
        int ring1 = rand.nextInt(26);
        int ring2 = rand.nextInt(26);
        int ring3 = rand.nextInt(26);

        this.rotor1 = getRotor(r1, rot1, ring1);
        this.rotor2 = getRotor(r2, rot2, ring2);
        this.rotor3 = getRotor(r3, rot3, ring3);
        this.reflector = getReflector(ref);

        this.plugboard = new Plugboard();
        plugboard.setConfiguration(Plugboard.seedToPlugboardConfiguration(rand));
    }

    @Override
    public char encryptChar(char k)
    {
        nextState();
        int x = ((int) k)-65;   //Cast to int and remove Unicode Offset (A=65 in Unicode.)
        //Encryption
        //forward direction
        x = plugboard.encrypt(x);
        x = entryWheel.encryptForward(x);
        x = rotor1.normalize(x + rotor1.getRotation() - rotor1.getRingSetting());
        x = rotor1.encryptForward(x);
        x = rotor1.normalize(x - rotor1.getRotation() + rotor1.getRingSetting() + rotor2.getRotation() - rotor2.getRingSetting());
        x = rotor2.encryptForward(x);
        x = rotor1.normalize(x - rotor2.getRotation() + rotor2.getRingSetting() + rotor3.getRotation() - rotor3.getRingSetting());
        x = rotor3.encryptForward(x);
        x = rotor1.normalize(x - rotor3.getRotation() + rotor3.getRingSetting());
        //backward direction
        x = reflector.encrypt(x);
        x = rotor1.normalize(x + rotor3.getRotation() - rotor3.getRingSetting());
        x = rotor3.encryptBackward(x);
        x = rotor1.normalize(x + rotor2.getRotation() - rotor2.getRingSetting() - rotor3.getRotation() + rotor3.getRingSetting());
        x = rotor2.encryptBackward(x);
        x = rotor1.normalize(x + rotor1.getRotation() - rotor1.getRingSetting() - rotor2.getRotation() + rotor2.getRingSetting());
        x = rotor1.encryptBackward(x);
        x = rotor1.normalize(x - rotor1.getRotation() + rotor1.getRingSetting());
        x = entryWheel.encryptBackward(x);
        x = plugboard.encrypt(x);
        return (char) (x + 65);     //Add Offset again, cast back to char and return
    }

    @Override
    public void setState(EnigmaStateBundle state)
    {
        plugboard.setConfiguration(state.getConfigurationPlugboard());
        entryWheel = getEntryWheel(state.getTypeEntryWheel());
        rotor1 = getRotor(state.getTypeRotor1(), state.getRotationRotor1(), state.getRingSettingRotor1());
        rotor2 = getRotor(state.getTypeRotor2(), state.getRotationRotor2(), state.getRingSettingRotor2());
        rotor3 = getRotor(state.getTypeRotor3(), state.getRotationRotor3(), state.getRingSettingRotor3());
        reflector = getReflector(state.getTypeReflector());
    }

    @Override
    public EnigmaStateBundle getState()
    {
        EnigmaStateBundle state = new EnigmaStateBundle();

        state.setConfigurationPlugboard(plugboard.getConfiguration());

        state.setTypeEntryWheel(entryWheel.getIndex());

        state.setTypeRotor1(rotor1.getIndex());
        state.setTypeRotor2(rotor2.getIndex());
        state.setTypeRotor3(rotor3.getIndex());

        state.setTypeReflector(reflector.getIndex());

        state.setRotationRotor1(rotor1.getRotation());
        state.setRotationRotor2(rotor2.getRotation());
        state.setRotationRotor3(rotor3.getRotation());

        state.setRingSettingRotor1(rotor1.getRingSetting());
        state.setRingSettingRotor2(rotor2.getRingSetting());
        state.setRingSettingRotor3(rotor3.getRingSetting());

        return state;
    }

    @Override
    public void restoreState(BigInteger s)
    {
        int r1 = getValue(s, availableRotors.size());
        s = removeDigit(s, availableRotors.size());
        int r2 = getValue(s, availableRotors.size());
        s = removeDigit(s, availableRotors.size());
        int r3 = getValue(s, availableRotors.size());
        s = removeDigit(s, availableRotors.size());
        int ref = getValue(s, availableReflectors.size());
        s = removeDigit(s, availableReflectors.size());
        int rot1 = getValue(s, 26);
        s = removeDigit(s, 26);
        int ring1 = getValue(s, 26);
        s = removeDigit(s, 26);
        int rot2 = getValue(s, 26);
        s = removeDigit(s, 26);
        int ring2 = getValue(s, 26);
        s = removeDigit(s, 26);
        int rot3 = getValue(s, 26);
        s = removeDigit(s, 26);
        int ring3 = getValue(s, 26);
        s = removeDigit(s, 26);

        this.entryWheel = getEntryWheel(0);
        this.rotor1 = getRotor(r1, rot1, ring1);
        this.rotor2 = getRotor(r2, rot2, ring2);
        this.rotor3 = getRotor(r3, rot3, ring3);
        this.reflector = getReflector(ref);

        this.plugboard = new Plugboard();
        plugboard.setConfiguration(s);
    }

    @Override
    public String stateToString() {
        BigInteger s = Plugboard.configurationToBigInteger(plugboard.getConfiguration());
        s = addDigit(s, rotor3.getRingSetting(), 26);
        s = addDigit(s, rotor3.getRotation(), 26);
        s = addDigit(s, rotor2.getRingSetting(), 26);
        s = addDigit(s, rotor2.getRotation(), 26);
        s = addDigit(s, rotor1.getRingSetting(), 26);
        s = addDigit(s, rotor1.getRotation(), 26);
        s = addDigit(s, reflector.getIndex(), availableReflectors.size());
        s = addDigit(s, rotor3.getIndex(), availableRotors.size());
        s = addDigit(s, rotor2.getIndex(), availableRotors.size());
        s = addDigit(s, rotor1.getIndex(), availableRotors.size());
        s = addDigit(s, 0, 20); //Machine #0

        return s.toString(16);
    }
}
