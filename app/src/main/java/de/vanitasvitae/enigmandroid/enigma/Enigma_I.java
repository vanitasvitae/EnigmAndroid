package de.vanitasvitae.enigmandroid.enigma;

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
public class Enigma_I extends Enigma
{
    protected Rotor rotor1;
    protected Rotor rotor2;
    protected Rotor rotor3;

    protected Reflector reflector;

    protected Plugboard plugboard;
    protected static int machineTypeOffset = 10;

    public Enigma_I()
    {
        super();
        machineType = "I";
    }

    @Override
    public void initialize()
    {
        this.plugboard= new Plugboard();
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
        rotor1 = rand.nextInt(5);
        while(rotor2 == -1 || rotor2 == rotor1) rotor2 = rand.nextInt(5);
        while(rotor3 == -1 || rotor3 == rotor2 || rotor3 == rotor1) rotor3 = rand.nextInt(5);
        int ref = rand.nextInt(3);

        int rot1 = rand.nextInt(26);
        int rot2 = rand.nextInt(26);
        int rot3 = rand.nextInt(26);
        int ring1 = rand.nextInt(26);
        int ring2 = rand.nextInt(26);
        int ring3 = rand.nextInt(26);

        this.rotor1 = Rotor.createRotor(machineTypeOffset + rotor1, rot1, ring1);
        this.rotor2 = Rotor.createRotor(machineTypeOffset + rotor2, rot2, ring2);
        this.rotor3 = Rotor.createRotor(machineTypeOffset + rotor3, rot3, ring3);
        this.reflector = Reflector.createReflector(machineTypeOffset + ref);

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
        x = plugboard.encrypt(x);
        return (char) (x + 65);     //Add Offset again, cast back to char and return
    }

    @Override
    public void setState(EnigmaStateBundle state)
    {
        plugboard.setConfiguration(state.getConfigurationPlugboard());
        rotor1 = Rotor.createRotor(state.getTypeRotor1(), state.getRotationRotor1(), state.getRingSettingRotor1());
        rotor2 = Rotor.createRotor(state.getTypeRotor2(), state.getRotationRotor2(), state.getRingSettingRotor2());
        rotor3 = Rotor.createRotor(state.getTypeRotor3(), state.getRotationRotor3(), state.getRingSettingRotor3());
        reflector = Reflector.createReflector(state.getTypeReflector());
    }

    @Override
    public EnigmaStateBundle getState()
    {
        EnigmaStateBundle state = new EnigmaStateBundle();

        state.setConfigurationPlugboard(plugboard.getConfiguration());

        state.setTypeRotor1(rotor1.getNumber());
        state.setTypeRotor2(rotor2.getNumber());
        state.setTypeRotor3(rotor3.getNumber());

        state.setTypeReflector(reflector.getNumber());

        state.setRotationRotor1(rotor1.getRotation());
        state.setRotationRotor2(rotor2.getRotation());
        state.setRotationRotor3(rotor3.getRotation());

        state.setRingSettingRotor1(rotor1.getRingSetting());
        state.setRingSettingRotor2(rotor2.getRingSetting());
        state.setRingSettingRotor3(rotor3.getRingSetting());

        return state;
    }

    @Override
    public void restoreState(String mem)
    {
        String plugboardConf = mem.substring(mem.lastIndexOf(":p") + 2);
        long s = Long.valueOf(mem.substring(0, mem.indexOf(":p")));

        s = removeDigit(s, 12);  //Remove machine type
        int r1 = getValue(s, 10);
        s = removeDigit(s, 10);
        int r2 = getValue(s, 10);
        s = removeDigit(s, 10);
        int r3 = getValue(s, 10);
        s = removeDigit(s, 10);
        int ref = getValue(s, 10);
        s = removeDigit(s, 10);
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

        this.rotor1 = Rotor.createRotor(machineTypeOffset + r1, rot1, ring1);
        this.rotor2 = Rotor.createRotor(machineTypeOffset + r2, rot2, ring2);
        this.rotor3 = Rotor.createRotor(machineTypeOffset + r3, rot3, ring3);
        this.reflector = Reflector.createReflector(machineTypeOffset + ref);

        this.plugboard = new Plugboard();
        plugboard.setConfiguration(Plugboard.stringToConfiguration(plugboardConf));
    }

    @Override
    public String stateToString() {
        String save = "";
        long s = rotor3.getRingSetting();
        s = addDigit(s, rotor3.getRotation(), 26);
        s = addDigit(s, rotor2.getRingSetting(), 26);
        s = addDigit(s, rotor2.getRotation(), 26);
        s = addDigit(s, rotor1.getRingSetting(), 26);
        s = addDigit(s, rotor1.getRotation(), 26);
        s = addDigit(s, rotor3.getNumber(), 10);
        s = addDigit(s, rotor2.getNumber(), 10);
        s = addDigit(s, rotor1.getNumber(), 10);
        s = addDigit(s, 0, 12); //Machine #0

        save = save+s;
        save = save + ":p" + Plugboard.configurationToString(getState().getConfigurationPlugboard());
        return save;
    }
}
