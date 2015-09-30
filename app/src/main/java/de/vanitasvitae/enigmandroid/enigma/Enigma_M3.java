package de.vanitasvitae.enigmandroid.enigma;

import java.security.SecureRandom;
import java.sql.Ref;
import java.util.Random;

import de.vanitasvitae.enigmandroid.enigma.rotors.Reflector;
import de.vanitasvitae.enigmandroid.enigma.rotors.Rotor;

/**
 * Concrete implementation of an enigma machine model M3
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
public class Enigma_M3 extends Enigma_I
{
    protected static int machineTypeOffset = 20;

    public Enigma_M3()
    {
        super();
        machineType = "M3";
    }

    @Override
    protected void generateState() {
        int rotor1, rotor2=-1, rotor3=-1;
        rotor1 = rand.nextInt(8);
        while(rotor2 == -1 || rotor2 == rotor1) rotor2 = rand.nextInt(8);
        while(rotor3 == -1 || rotor3 == rotor2 || rotor3 == rotor1) rotor3 = rand.nextInt(8);
        int ref = rand.nextInt(2);

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
        s = addDigit(s, 1, 12); //Machine #1

        save = save+s;
        save = save + ":p" + Plugboard.configurationToString(getState().getConfigurationPlugboard());
        return save;
    }
}
