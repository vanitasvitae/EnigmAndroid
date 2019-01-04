/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.vanitasvitae.enigmandroid.enigma;

import android.util.Log;

import java.math.BigInteger;

import de.vanitasvitae.enigmandroid.MainActivity;
import de.vanitasvitae.enigmandroid.enigma.parts.EntryWheel;
import de.vanitasvitae.enigmandroid.enigma.parts.Plugboard;
import de.vanitasvitae.enigmandroid.enigma.parts.Reflector;
import de.vanitasvitae.enigmandroid.enigma.parts.Rotor;

/**
 * Concrete implementation of an enigma machine model M3
 * Copyright (C) 2015  Paul Schaub
 */
public class Enigma_M3 extends Enigma_I
{
	public Enigma_M3()
	{
		super();
		machineType = "M3";
		Log.d(MainActivity.APP_ID, "Created Enigma M3");
	}

	@Override
	protected void establishAvailableParts()
	{
		addAvailableEntryWheel(new EntryWheel.EntryWheel_ABCDEF());
		addAvailableRotor(new Rotor.Rotor_I(0, 0));
		addAvailableRotor(new Rotor.Rotor_II(0,0));
		addAvailableRotor(new Rotor.Rotor_III(0,0));
		addAvailableRotor(new Rotor.Rotor_IV(0,0));
		addAvailableRotor(new Rotor.Rotor_V(0,0));
		addAvailableRotor(new Rotor.Rotor_VI(0,0));
		addAvailableRotor(new Rotor.Rotor_VII(0,0));
		addAvailableRotor(new Rotor.Rotor_VIII(0,0));
		addAvailableReflector(new Reflector.Reflector_B());
		addAvailableReflector(new Reflector.Reflector_C());
	}

	@Override
	protected void generateState() {
		int r1, r2=-1, r3=-1;
		r1 = rand.nextInt(8);
		while(r2 == -1 || r2 == r1) r2 = rand.nextInt(8);
		while(r3 == -1 || r3 == r2 || r3 == r1) r3 = rand.nextInt(8);
		int ref = rand.nextInt(2);

		int rot1 = rand.nextInt(26);
		int rot2 = rand.nextInt(26);
		int rot3 = rand.nextInt(26);
		int ring1 = rand.nextInt(26);
		int ring2 = rand.nextInt(26);
		int ring3 = rand.nextInt(26);

		this.entryWheel = getEntryWheel(0);
		this.rotor1 = getRotor(r1, rot1, ring1);
		this.rotor2 = getRotor(r2, rot2, ring2);
		this.rotor3 = getRotor(r3, rot3, ring3);
		this.reflector = getReflector(ref);

		this.plugboard = new Plugboard();
		plugboard.setConfiguration(Plugboard.seedToPlugboardConfiguration(rand));
	}

	@Override
	public BigInteger getEncodedState(int protocol_version) {
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
		s = addDigit(s, 1, 20); //Machine #1
		s = addDigit(s, protocol_version, MainActivity.max_protocol_version);

		return s;
	}
}
