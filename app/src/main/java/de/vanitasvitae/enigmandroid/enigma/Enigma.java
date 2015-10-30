package de.vanitasvitae.enigmandroid.enigma;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import de.vanitasvitae.enigmandroid.MainActivity;
import de.vanitasvitae.enigmandroid.enigma.rotors.EntryWheel;
import de.vanitasvitae.enigmandroid.enigma.rotors.Reflector;
import de.vanitasvitae.enigmandroid.enigma.rotors.Rotor;

/**
 * Main component of the Enigma machine
 * This is the mostly abstract base of any enigma machine.
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
public abstract class Enigma
{
	protected static String machineType;

	protected boolean doAnomaly = false;  //Has the time come to handle an anomaly?

	protected ArrayList<EntryWheel> availableEntryWheels;
	protected ArrayList<Rotor> availableRotors;
	protected ArrayList<Reflector> availableReflectors;

	protected Random rand;

	public Enigma()
	{
		establishAvailableParts();
		initialize();
	}

	/**
	 * In this method, available EntryWheels, Rotors and Reflectors can be defined.
	 */
	protected abstract void establishAvailableParts();

	/**
	 * Add a Rotor to the ArrayList of available rotors for this machine.
	 * Also set the index of the Rotor.
	 * @param r Rotor
	 */
	protected void addAvailableRotor(Rotor r)
	{
		if(availableRotors == null) availableRotors = new ArrayList<>();
		availableRotors.add(availableRotors.size(), r.setIndex(availableRotors.size()));
	}

	protected void addAvailableEntryWheel(EntryWheel e)
	{
		if(availableEntryWheels == null) availableEntryWheels = new ArrayList<>();
		availableEntryWheels.add(availableEntryWheels.size(), e.setIndex(availableEntryWheels.size()));
	}

	protected void addAvailableReflector(Reflector r)
	{
		if(availableReflectors == null) availableReflectors = new ArrayList<>();
		availableReflectors.add(availableReflectors.size(), r.setIndex(availableReflectors.size()));
	}

	public ArrayList<EntryWheel> getAvailableEntryWheels()
	{
		return availableEntryWheels;
	}

	public ArrayList<Rotor> getAvailableRotors()
	{
		return availableRotors;
	}

	public ArrayList<Reflector> getAvailableReflectors()
	{
		return availableReflectors;
	}

	public EntryWheel getEntryWheel(int index)
	{
		if(availableEntryWheels == null || availableEntryWheels.size() == 0) return null;
		return availableEntryWheels.get(index % availableEntryWheels.size()).getInstance();
	}

	public Rotor getRotor(int index)
	{
		if(availableRotors == null || availableRotors.size() == 0) return null;
		return availableRotors.get(index % availableRotors.size()).getInstance();
	}

	public Rotor getRotor(int index, int rotation, int ringSetting)
	{
		return getRotor(index).setRotation(rotation).setRingSetting(ringSetting);
	}

	public Reflector getReflector(int index)
	{
		if(availableReflectors == null || availableReflectors.size() == 0) return null;
		return availableReflectors.get(index%availableReflectors.size()).getInstance();
	}

	public Reflector getReflector(int index, int rotation, int ringSetting)
	{
		return getReflector(index).setRotation(rotation).setRingSetting(ringSetting);
	}

	/**
	 * Set the enigma to an initial state
	 */
	public abstract void initialize();

	/**
	 * Encrypt / Decrypt a given String w.
	 * w must be prepared using prepare(w) beforehand.
	 * Doing so changes the state of the rotors but not the state of the plugboard and the
	 * ringSettings
	 *
	 * @param w Text to decrypt/encryptString
	 * @return encrypted/decrypted string
	 */
	public String encryptString(String w)
	{
		//output string
		String output = "";
		//for each char x in k
		for (char x : w.toCharArray())
		{
			output = output + this.encryptChar(x);
		}
		//return en-/decrypted string
		return output;
	}

	/**
	 * Set the enigma into the next mechanical state.
	 * This rotates the first rotor and eventually also the second/third.
	 * Also this method handles the anomaly in case it should happen.
	 */
	public abstract void nextState();

	/**
	 * Set the enigma into a completely random state using a unseeded SecureRandom object.
	 */
	public void randomState()
	{
		this.rand = ((MainActivity) (MainActivity.ActivitySingleton.getInstance().getActivity()))
				.getSecureRandom();
		generateState();
	}

	/**
	 * Set the enigma to a random state based on the initialization of rand.
	 * Don not choose a rotor twice, set random rotations, ringSettings, ukw and possibly
	 * plugboard / rewirable ukw configurations.
	 */
	protected abstract void generateState();

	/**
	 * Substitute char k by sending the signal through the enigma.
	 * The signal passes the plugboard, the rotors and returns back after going through the
	 * reflector wheel.
	 *
	 * @param k input char
	 * @return substituted output char
	 */
	public abstract char encryptChar(char k);

	/**
	 * Set the state of the enigma
	 * @param state new state
	 */
	public abstract void setState(EnigmaStateBundle state);

	/**
	 * Return an object representing the current state of the enigma
	 * @return state
	 */
	public abstract EnigmaStateBundle getState();

	/**
	 * Set the rand into a certain state based on seed.
	 * Then set the enigmas state.
	 * @param seed passphrase
	 */
	public void setStateFromSeed(String seed)
	{
		rand = new Random(seed.hashCode());
		generateState();
	}

	public abstract void restoreState(BigInteger mem, int protocol_version);

	public BigInteger getEncodedState()
	{
		return getEncodedState(MainActivity.latest_protocol_version);
	}
	public abstract BigInteger getEncodedState(int protocol_version);

	public static String numToMachineType(int n)
	{
		int m = 13;
		n = (m+(n+m)%m)%m; //Problem? Trolololo
		switch (n) {
			case 0: return "I";
			case 1: return "M3";
			case 2: return "M4";
			case 3: return "G31";
			case 4: return "G312";
			case 5: return "G260";
			case 6: return "D";
			case 7: return "K";
			case 8: return "KS";
			case 9: return "KSA";
			case 10: return "R";
			case 11: return "T";
			case 12: return "KD";
			default: return "KD";
		}
	}

	public static String chooseEnigmaFromSeed(String seed)
	{
		return numToMachineType(seed.hashCode() % 13);
	}

	public static String chooseEnigmaFromSave(BigInteger save)
	{
		return numToMachineType(getValue(save, 20));
	}

	/**
	 * Return the name indicator of the enigma machine
	 * @return name
	 */
	public String getMachineType()
	{
		return machineType;
	}

	/**
	 *
	 * @param s source
	 * @param d domain (max value) of the value
	 * @return value
	 */
	public static int getValue(BigInteger s, int d)
	{
		BigInteger o = s.mod(BigInteger.valueOf(d)).add(BigInteger.valueOf(d)).mod(BigInteger.valueOf(d));
		return Integer.valueOf(o.toString());
	}

	/**
	 * remove a digit of domain d from source s
	 * @param s source
	 * @param d domain (max value)
	 * @return trimmed source
	 */
	public static BigInteger removeDigit(BigInteger s, int d)
	{
		s = s.subtract(s.mod(BigInteger.valueOf(d)));
		s = s.divide(BigInteger.valueOf(d));
		return s;
	}

	/**
	 *
	 * @param s source
	 * @param b base (max value)
	 * @param v actual value
	 * @return lengthened source
	 */
	public static BigInteger addDigit(BigInteger s, int v, int b)
	{
		s = s.multiply(BigInteger.valueOf(b));
		s = s.add(BigInteger.valueOf(v % b));
		return s;
	}
}
