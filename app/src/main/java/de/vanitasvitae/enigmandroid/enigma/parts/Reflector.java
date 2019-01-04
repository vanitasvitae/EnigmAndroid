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
package de.vanitasvitae.enigmandroid.enigma.parts;

import android.util.Log;

import java.math.BigInteger;
import java.util.Arrays;

import de.vanitasvitae.enigmandroid.MainActivity;
import de.vanitasvitae.enigmandroid.enigma.Enigma;

/**
 * Reflector of the enigma machine.
 * The reflector was used to reflect the scrambled signal at the end of the wiring back to
 * go through another (reversed but not inverting) process of scrambling.
 * Copyright (C) 2015  Paul Schaub
 */
public class Reflector
{
	private final int type;
	private final String name;
	private int index;
	private final String summary;
	private int[] connections;
	private int rotation;
	private int ringSetting;

	/**
	 * This constructor is not accessible from outside this class file.
	 * Use the one of the createReflector* methods instead to create concrete Reflectors from
	 * outside this class file
	 * @param type name indicator of the reflector
	 * @param connections wiring of the reflector as Integer array
	 */
	Reflector(int type, String name, String summary, int[] connections)
	{
		this.type = type;
		this.name = name;
		this.summary = summary;
		this.connections = connections;
	}

	public Reflector getInstance()
	{
		//noinspection ConstantConditions
		return createReflector(this.type).setIndex(this.getIndex());
	}

	public Reflector getInstance(int rotation, int ringSetting)
	{
		//noinspection ConstantConditions
		return createReflector(this.type).setIndex(this.getIndex())
				.setRotation(rotation).setRingSetting(ringSetting);
	}

	public Reflector setIndex(int index)
	{
		this.index = index;
		return this;
	}

	public int getIndex()
	{
		return this.index;
	}

	public int getRotation()
	{
		return rotation;
	}

	public int getRingSetting()
	{
		return ringSetting;
	}

	public Reflector setRotation(int rotation)
	{
		this.rotation = rotation;
		return this;
	}

	public Reflector setRingSetting(int ringSetting)
	{
		this.ringSetting = ringSetting;
		return this;
	}

	public Reflector setConfiguration(int[] c)
	{
		this.connections = c;
		return this;
	}

	@SuppressWarnings("UnusedReturnValue")
	public BigInteger setConfiguration(BigInteger b)
	{
		String s = "";

		int x;
		while((x = Enigma.getValue(b, 27)) != 26 || b.compareTo(BigInteger.ZERO) > 1)
		{
			s = ((char) (x+65))+s;
			b = Enigma.removeDigit(b, 27);
		}
		Log.d(MainActivity.APP_ID, "Restored: " + s);
		this.setConfiguration(Plugboard.stringToConfiguration(s));
		return b;
	}

	public int[] getConfiguration()
	{
		return connections;
	}

	/**
	 * Factory method to create reflectors.
	 * @param type name of the created reflector
	 *             1 -> ReflectorA
	 *             2 -> ReflectorB
	 *             3 -> ReflectorC
	 *             4 -> ReflectorThinB
	 *             5 -> ReflectorThinC
	 *             6 -> ReflectorEnigma_D_KD_G31
	 *             7 -> Reflector_K
	 *             8 -> Reflector_T
	 *             9 -> Reflector_G312
	 *             10 -> Reflector_G260
	 *             11 -> Reflector_R
	 *             default -> ReflectorB
	 * @return Reflector
	 */
	private static Reflector createReflector(int type)
	{
		switch (type)
		{
			case 0: return new Reflector_A();
			case 1: return new Reflector_B();
			case 2: return new Reflector_C();
			case 10: return new Reflector_Thin_B();
			case 11: return new ReflectorThinC();
			case 20: return new ReflectorEnigma_D_G31();
			case 21: return new ReflectorEnigma_KD();
			case 30: return new Reflector_G312();
			case 40: return new Reflector_K_G260();
			case 50: return new Reflector_R();
			case 60: return new ReflectorEnigma_T();

			default:
				Log.e(MainActivity.APP_ID," Tried to create Reflector of invalid name "+type);
				return null;
		}
	}

	/**
	 * Substitute an input signal via the wiring of the reflector with a different (!) output.
	 * The output MUST not be equal to the input for any input, since this was not possible
	 * due to the electronic implementation of the historical enigma machine.
	 * @param input input signal
	 * @return encrypted (substituted) output
	 */
	public int encrypt(int input)
	{
		return this.connections[normalize(input)];
	}

	/**
	 * Return the size (ie the number of wires/length of the connections array) of the reflector
	 * @return size
	 */
	private int getRotorSize()
	{
		return this.connections.length;
	}

	/**
	 * Normalize the input.
	 * Normalizing means keeping the input via modulo in the range from 0 to n-1, where n is equal
	 * to the size of the reflector. This is necessary since java allows negative modulo values,
	 * which can break this implementation
	 * @param input input signal
	 * @return "normalized" input signal
	 */
	private int normalize(int input)
	{
		return (input + this.getRotorSize()) % this.getRotorSize();
	}

	/**
	 * Concrete implementation of ReflectorA
	 * Used in Enigma I
	 * AE  BJ  CM  DZ  FL  GY  HX  IV  KW  NR  OQ  PU  ST
	 */
	public static class Reflector_A extends Reflector
	{
		public Reflector_A()
		{
			super(0, "A", "EJMZALYXVBWFCRQUONTSPIKHGD",
				  new int[]{4,9,12,25,0,11,24,23,21,1,22,5,2,17,16,20,14,13,19,18,15,8,10,7,6,3});
		}
	}

	/**
	 * Concrete implementation of ReflectorB
	 * Used in Enigma I, M3
	 * AY  BR  CU  DH  EQ  FS  GL  IP  JX  KN  MO  TZ  VW
	 */
	public static class Reflector_B extends Reflector
	{
		public Reflector_B()
		{
			super(1, "B", "YRUHQSLDPXNGOKMIEBFZCWVJAT",
				  new int[]{24,17,20,7,16,18,11,3,15,23,13,6,14,10,12,8,4,1,5,25,2,22,21,9,0,19});
		}
	}

	/**
	 * Concrete implementation of ReflectorC
	 * Used in Enigma I, M3
	 * AF  BV  CP  DJ  EI  GO  HY  KR  LZ  MX  NW  QT  SU
	 */
	public static class Reflector_C extends Reflector
	{
		public Reflector_C()
		{
			super(2, "C", "FVPJIAOYEDRZXWGCTKUGSBNMHL",
				  new int[]{5,21,15,9,8,0,14,24,4,3,17,25,23,22,6,2,19,10,20,16,18,1,13,12,7,11});
		}
	}

	/**
	 * Concrete implementation of thin reflector name b (not equal to normal name b!)
	 * When used with Rotor Beta on rotation 0, the pair was equivalent to normal reflector B
	 * S->Beta->ThinB->Beta'->X == X->UKWB->S
	 * Used in Enigma M4
	 * E N K Q A U Y W J I C O P B L M D X Z V F T H R G S
	 */
	public static class Reflector_Thin_B extends Reflector
	{
		public Reflector_Thin_B()
		{
			super(10, "Thin-B", "ENKQAUYWJICOPBLMDXZVFTHRGS",
				  new int[]{4,13,10,16,0,20,24,22,9,8,2,14,15,1,11,12,3,23,25,21,5,19,7,17,6,18});
		}
	}

	/**
	 * Concrete implementation of thin reflector name c (not equal to normal name c!)
	 * When used with Rotor Gamma on rotation 0, the pair was equivalent to normal reflector C
	 * S->Gamma->ThinC->Gamma'->X == X->UKWC->S
	 * Used in Enigma M4
	 * R D O B J N T K V E H M L F C W Z A X G Y I P S U Q
	 */
	public static class ReflectorThinC extends Reflector
	{
		public ReflectorThinC()
		{
			super(11, "ThinC", "RDOBJNTKVEHMLFCWZAXGYIPSUQ",
				  new int[]{17,3,14,1,9,13,19,10,21,4,7,12,11,5,2,22,25,0,23,6,24,8,15,18,20,16});
		}
	}

	/**
	 * Pluggable Reflector of the Enigma machine of name D and G31
	 * Standard wiring: AI,BM,CE,DT,FG,HR,JY,KS,LQ,NZ,OX,PW,UV
	 * Has additional ringSetting and can rotate
	 */
	public static class ReflectorEnigma_D_G31 extends Reflector
	{
		public static final int[] defaultWiring_D_G31 =  {8,12,4,19,2,6,5,17,0,24,18,16,1,25,23,22,11,7,10,3,21,20,15,14,9,13};
		public ReflectorEnigma_D_G31()
		{
			super(20, "Ref-D", "Default: IMETCGFRAYSQBZXWLHKDVUPOJN", Arrays.copyOf(defaultWiring_D_G31,defaultWiring_D_G31.length));
		}
	}

	/**
	 * Pluggable Reflector as used in the Enigma of Type KD
	 * Standard wiring: KOTVPNLMJIAGHFBEWYXCZDQSRU
	 * Has additional ringSetting and can rotate
	 */
	public static class ReflectorEnigma_KD extends Reflector
	{
		public static final int[] defaultWiring_KD = {10,14,19,21,15,13,11,12,9,8,0,6,7,5,1,4,22,24,23,2,25,3,16,18,17,20};
		public ReflectorEnigma_KD()
		{
			super(21, "Ref-KD", "Default: KOTVPNLMJIAGHFBEWYXCZDQSRU", Arrays.copyOf(defaultWiring_KD, defaultWiring_KD.length));
		}
	}

	/**
	 * Reflector as used in the Enigma name G-312 Abwehr
	 * R U L Q M Z J S Y G O C E T K W D A H N B X P V I F
	 */
	public static class Reflector_G312 extends Reflector
	{
		public Reflector_G312()
		{
			super(30, "Ref-G312", "RULQMZJSYGOCETKWDAHNBXPVIF",
				  new int[]{17,20,11,16,12,25,9,18,24,6,14,2,4,19,10,22,3,0,7,13,1,23,15,21,8,5});
		}
	}

	/**
	 * Reflector as used in the Enigma name G-260 Abwehr
	 * I M E T C G F R A Y S Q B Z X W L H K D V U P O J N
	 */
	public static class Reflector_K_G260 extends Reflector
	{
		public Reflector_K_G260()
		{
			super(40,"Ref-K/G260", "IMETCGFRAYSQBZXWLHKDVUPOJN",
				  new int[]{8,12,4,19,2,6,5,17,0,24,18,16,1,25,23,22,11,7,10,3,21,20,15,14,9,13});
		}
	}

	/**
	 * Reflector as used in the Enigma Type R "Rocket" (Reichsbahn)
	 * Q Y H O G N E C V P U Z T F D J A X W M K J S R B L
	 */
	public static class Reflector_R extends Reflector
	{
		public Reflector_R()
		{
			super(50, "Ref-R", "QYHOGNECVPUZTFDJAXWMKJSRBL",
				  new int[]{16,24,7,14,6,13,4,2,21,15,20,25,19,5,3,9,0,23,22,12,10,8,18,17,1,11});
		}
	}

	/**
	 * Reflector as used in the Enigma name T (Tirpitz)
	 * G E K P B T A U M O C N I L J D X Z Y F H W V Q S R
	 */
	public static class ReflectorEnigma_T extends Reflector
	{
		public ReflectorEnigma_T()
		{
			super(60, "Ref-T", "GEKPBTAUMOCNILJDXZYFHWVQSR",
				  new int[]{6,4,10,15,1,19,0,20,12,14,2,13,8,11,9,3,23,25,24,5,7,22,21,16,18,17});
		}
	}
}
