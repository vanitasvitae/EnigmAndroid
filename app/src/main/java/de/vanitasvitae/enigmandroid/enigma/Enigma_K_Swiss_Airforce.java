package de.vanitasvitae.enigmandroid.enigma;

import java.math.BigInteger;

import de.vanitasvitae.enigmandroid.enigma.rotors.EntryWheel;
import de.vanitasvitae.enigmandroid.enigma.rotors.Reflector;
import de.vanitasvitae.enigmandroid.enigma.rotors.Rotor;

/**
 * Implementation of the Enigma machine of name K (Switzerland, Airforce)
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
public class Enigma_K_Swiss_Airforce extends Enigma_K
{
    public Enigma_K_Swiss_Airforce()
    {
        super();
        machineType = "KSA";
    }

    @Override
    protected void establishAvailableParts()
    {
        addAvailableEntryWheel(new EntryWheel.EntryWheel_QWERTZ());
        addAvailableRotor(new Rotor.Rotor_K_Swiss_Airforce_I(0,0));
        addAvailableRotor(new Rotor.Rotor_K_Swiss_Airforce_II(0,0));
        addAvailableRotor(new Rotor.Rotor_K_Swiss_Airforce_III(0,0));
        addAvailableReflector(new Reflector.Reflector_K_G260());
    }

    @Override
    public String stateToString()
    {
        BigInteger s = BigInteger.valueOf(reflector.getRingSetting());
        s = addDigit(s, reflector.getRotation(), 26);
        s = addDigit(s, rotor3.getRingSetting(),26);
        s = addDigit(s, rotor3.getRotation(), 26);
        s = addDigit(s, rotor2.getRingSetting(),26);
        s = addDigit(s, rotor2.getRotation(), 26);
        s = addDigit(s, rotor1.getRingSetting(), 26);
        s = addDigit(s, rotor1.getRotation(), 26);
        s = addDigit(s, rotor3.getIndex(), availableRotors.size());
        s = addDigit(s, rotor2.getIndex(), availableRotors.size());
        s = addDigit(s, rotor1.getIndex(), availableRotors.size());
        s = addDigit(s, 9, 20); //Machine #9

        return s.toString(16);
    }
}
