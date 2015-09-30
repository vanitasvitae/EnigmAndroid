package de.vanitasvitae.enigmandroid.enigma;

import de.vanitasvitae.enigmandroid.MainActivity;

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
public class Enigma_G312 extends Enigma_G31
{
    public Enigma_G312()
    {
        super(50);
        machineType = "G312";
    }

    @Override
    public String stateToString()
    {
        String save = MainActivity.APP_ID+"/";
        long s = reflector.getRingSetting();
        s = addDigit(s, reflector.getRotation(), 26);
        s = addDigit(s, rotor3.getRingSetting(), 26);
        s = addDigit(s, rotor3.getRotation(), 26);
        s = addDigit(s, rotor2.getRingSetting(), 26);
        s = addDigit(s, rotor2.getRotation(), 26);
        s = addDigit(s, rotor1.getRingSetting(), 26);
        s = addDigit(s, rotor1.getRotation(), 26);

        s = addDigit(s, rotor3.getNumber(), 10);
        s = addDigit(s, rotor2.getNumber(), 10);
        s = addDigit(s, rotor1.getNumber(), 10);

        s = addDigit(s, 4, 20); //Machine #4

        save = save+s;
        return save;
    }
}
