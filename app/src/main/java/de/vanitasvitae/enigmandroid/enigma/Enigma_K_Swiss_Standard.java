package de.vanitasvitae.enigmandroid.enigma;

import java.security.SecureRandom;
import java.util.Random;

import de.vanitasvitae.enigmandroid.MainActivity;
import de.vanitasvitae.enigmandroid.enigma.rotors.Reflector;
import de.vanitasvitae.enigmandroid.enigma.rotors.Rotor;

/**
 * Implementation of the Enigma machine of type K (Switzerland)
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
public class Enigma_K_Swiss_Standard extends Enigma_K
{
    public Enigma_K_Swiss_Standard()
    {
        super(90);
        machineType = "KS";
    }

    @Override
    public String stateToString()
    {
        String save = MainActivity.APP_ID+"/";
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
        t = addDigit(t, 8, 20); //Machine #8

        save = save+t;
        return save;
    }

}
