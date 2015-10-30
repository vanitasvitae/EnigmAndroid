package de.vanitasvitae.enigmandroid.enigma;

import android.util.Log;

import java.math.BigInteger;
import java.util.ArrayList;

import de.vanitasvitae.enigmandroid.MainActivity;
import de.vanitasvitae.enigmandroid.enigma.rotors.EntryWheel;
import de.vanitasvitae.enigmandroid.enigma.rotors.Reflector;
import de.vanitasvitae.enigmandroid.enigma.rotors.Rotor;

/**
 * Concrete Implementation of the Enigma Machine name M4 of the german Kriegsmarine
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
	private ArrayList<Rotor> availableThinRotors;
	private EntryWheel entryWheel;
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
		Log.d(MainActivity.APP_ID, "Created Enigma M4");
	}

	protected void addAvailableThinRotor(Rotor r)
	{
		if(availableThinRotors == null) availableThinRotors = new ArrayList<>();
		availableThinRotors.add(availableThinRotors.size(), r.setIndex(availableThinRotors.size()));
	}

	public Rotor getThinRotor(int index)
	{
		if(availableThinRotors == null || availableThinRotors.size() == 0) return null;
		return availableThinRotors.get(index % availableThinRotors.size()).getInstance();
	}

	public Rotor getThinRotor(int index, int rotation, int ringSettings)
	{
		Rotor r = getThinRotor(index);
		if(r == null) return null;
		return r.setRotation(rotation).setRingSetting(ringSettings);
	}

	@Override
	protected void establishAvailableParts()
	{
		Log.d(MainActivity.APP_ID, "Established");
		addAvailableEntryWheel(new EntryWheel.EntryWheel_ABCDEF());
		addAvailableRotor(new Rotor.Rotor_I(0, 0));
		addAvailableRotor(new Rotor.Rotor_II(0,0));
		addAvailableRotor(new Rotor.Rotor_III(0,0));
		addAvailableRotor(new Rotor.Rotor_IV(0,0));
		addAvailableRotor(new Rotor.Rotor_V(0,0));
		addAvailableRotor(new Rotor.Rotor_VI(0,0));
		addAvailableRotor(new Rotor.Rotor_VII(0, 0));
		addAvailableRotor(new Rotor.Rotor_VIII(0,0));
		addAvailableThinRotor(new Rotor.Rotor_M4_Beta(0, 0));
		addAvailableThinRotor(new Rotor.Rotor_M4_Gamma(0, 0));
		addAvailableReflector(new Reflector.Reflector_Thin_B());
		addAvailableReflector(new Reflector.ReflectorThinC());
	}

	@Override
	public void initialize()
	{
		Log.d(MainActivity.APP_ID, "Initialized");
		this.plugboard = new Plugboard();
		this.entryWheel = getEntryWheel(0);
		this.rotor1 = getRotor(0, 0, 0);
		this.rotor2 = getRotor(1, 0, 0);
		this.rotor3 = getRotor(2, 0, 0);
		this.rotor4 = getThinRotor(0, 0, 0);
		this.reflector = getReflector(0);
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
		if (rotor1.isAtTurnoverPosition() || this.doAnomaly)
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
	protected void generateState() {
		int r1, r2=-1, r3=-1;
		int r4;
		int ref;
		r1 = rand.nextInt(8);
		while(r2 == -1 || r2 == r1) r2 = rand.nextInt(8);
		while(r3 == -1 || r3 == r2 || r3 == r1) r3 = rand.nextInt(8);
		r4 = rand.nextInt(2);
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

		this.entryWheel = getEntryWheel(0);
		this.rotor1 = getRotor(r1, rot1, ring1);
		this.rotor2 = getRotor(r2, rot2, ring2);
		this.rotor3 = getRotor(r3, rot3, ring3);
		this.rotor4 = getThinRotor(r4, rot4, ring4);

		this.reflector = getReflector(ref, rotRef, ringRef);

		this.plugboard = new Plugboard();
		this.plugboard.setConfiguration(Plugboard.seedToPlugboardConfiguration(rand));
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
		x = entryWheel.encryptForward(x);
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
		x = entryWheel.encryptBackward(x);
		x = plugboard.encrypt(x);
		return (char) (x + 65);     //Add Offset again and cast back to char
	}

	@Override
	public void setState(EnigmaStateBundle state)
	{
		rotor1 = getRotor(state.getTypeRotor1(), state.getRotationRotor1(), state.getRingSettingRotor1());
		rotor2 = getRotor(state.getTypeRotor2(), state.getRotationRotor2(), state.getRingSettingRotor2());
		rotor3 = getRotor(state.getTypeRotor3(), state.getRotationRotor3(), state.getRingSettingRotor3());
		rotor4 = getThinRotor(state.getTypeRotor4(), state.getRotationRotor4(), state.getRingSettingRotor4());
		reflector = getReflector(state.getTypeReflector());
		plugboard.setConfiguration(state.getConfigurationPlugboard());

	}

	@Override
	public EnigmaStateBundle getState()
	{
		EnigmaStateBundle state = new EnigmaStateBundle();
		state.setTypeEntryWheel(entryWheel.getIndex());

		state.setTypeRotor1(rotor1.getIndex());
		state.setTypeRotor2(rotor2.getIndex());
		state.setTypeRotor3(rotor3.getIndex());
		state.setTypeRotor4(rotor4.getIndex());

		state.setRotationRotor1(rotor1.getRotation());
		state.setRotationRotor2(rotor2.getRotation());
		state.setRotationRotor3(rotor3.getRotation());
		state.setRotationRotor4(rotor4.getRotation());

		state.setRingSettingRotor1(rotor1.getRingSetting());
		state.setRingSettingRotor2(rotor2.getRingSetting());
		state.setRingSettingRotor3(rotor3.getRingSetting());
		state.setRingSettingRotor4(rotor4.getRingSetting());

		state.setTypeReflector(reflector.getIndex());

		state.setConfigurationPlugboard(plugboard.getConfiguration());

		return state;
	}

	@Override
	public void restoreState(BigInteger s, int protocol_version)
	{
		switch (protocol_version)
		{
			case 1:
				int r1 = getValue(s, availableRotors.size());
				s = removeDigit(s, availableRotors.size());
				int r2 = getValue(s, availableRotors.size());
				s = removeDigit(s,availableRotors.size());
				int r3 = getValue(s, availableRotors.size());
				s = removeDigit(s,availableRotors.size());
				int r4 = getValue(s, availableThinRotors.size());
				s = removeDigit(s,availableThinRotors.size());
				int ref = getValue(s, availableReflectors.size());
				s = removeDigit(s,availableReflectors.size());

				int rot1 = getValue(s, 26);
				s = removeDigit(s,26);
				int ring1 = getValue(s, 26);
				s = removeDigit(s,26);
				int rot2 = getValue(s, 26);
				s = removeDigit(s,26);
				int ring2 = getValue(s, 26);
				s = removeDigit(s,26);
				int rot3 = getValue(s, 26);
				s = removeDigit(s,26);
				int ring3 = getValue(s, 26);
				s = removeDigit(s,26);
				int rot4 = getValue(s, 26);
				s = removeDigit(s,26);
				int ring4 = getValue(s, 26);
				s = removeDigit(s,26);
				int rotRef = getValue(s, 26);
				s = removeDigit(s,26);
				int ringRef = getValue(s, 26);
				s = removeDigit(s, 26);

				this.rotor1 = getRotor(r1, rot1, ring1);
				this.rotor2 = getRotor(r2, rot2, ring2);
				this.rotor3 = getRotor(r3, rot3, ring3);
				this.rotor4 = getThinRotor(r4, rot4, ring4);
				this.reflector = getReflector(ref, rotRef, ringRef);
				this.plugboard = new Plugboard();
				plugboard.setConfiguration(s);
				break;

			default: Log.e(MainActivity.APP_ID, "Unsupported protocol version "+protocol_version);
		}
	}

	@Override
	public BigInteger getEncodedState(int protocol_version) {
		BigInteger s = Plugboard.configurationToBigInteger(plugboard.getConfiguration());
		s = addDigit(s, reflector.getRingSetting(), 26);
		s = addDigit(s, reflector.getRotation(), 26);
		s = addDigit(s, rotor4.getRingSetting(), 26);
		s = addDigit(s, rotor4.getRotation(), 26);
		s = addDigit(s, rotor3.getRingSetting(), 26);
		s = addDigit(s, rotor3.getRotation(), 26);
		s = addDigit(s, rotor2.getRingSetting(), 26);
		s = addDigit(s, rotor2.getRotation(), 26);
		s = addDigit(s, rotor1.getRingSetting(), 26);
		s = addDigit(s, rotor1.getRotation(), 26);

		s = addDigit(s, reflector.getIndex(), availableReflectors.size());
		s = addDigit(s, rotor4.getIndex(), availableThinRotors.size());
		s = addDigit(s, rotor3.getIndex(), availableRotors.size());
		s = addDigit(s, rotor2.getIndex(), availableRotors.size());
		s = addDigit(s, rotor1.getIndex(), availableRotors.size());

		s = addDigit(s, 2, 20);
		s = addDigit(s, protocol_version, MainActivity.max_protocol_version);

		return s;
	}
}
