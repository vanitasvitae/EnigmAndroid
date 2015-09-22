package de.vanitasvitae.enigmandroid.enigma.rotors;

import android.util.Log;

/**
 * Reflector of the enigma machine.
 * The reflector was used to reflect the scrambled signal at the end of the wiring back to
 * go through another (reversed but not inverting) process of scrambling.
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
public class Reflector
{
    protected String type;
    protected int number;
    protected int[] connections;
    protected int rotation;
    protected int ringSetting;

    public static final int[] defaultWiring_D_KD_G31 = {8,12,4,19,2,6,5,17,0,24,18,16,1,25,23,22,11,7,10,3,21,20,15,14,9,13};

    /**
     * This constructor is not accessible from outside this class file.
     * Use the one of the createReflector* methods instead to create concrete Reflectors from
     * outside this class file
     * @param type type indicator of the reflector
     * @param connections wiring of the reflector as Integer array
     */
    protected Reflector(String type, int[] connections)
    {
        this.type = type;
        this.connections = connections;
    }

    public int getRotation()
    {
        return rotation;
    }

    public int getRingSetting()
    {
        return ringSetting;
    }

    public void setRotation(int rotation)
    {
        this.rotation = rotation;
    }

    public void setRingSetting(int ringSetting)
    {
        this.ringSetting = ringSetting;
    }

    public void setConfiguration(int[] c)
    {
        this.connections = c;
    }

    public int[] getConfiguration()
    {
        return connections;
    }

    /**
     * Factory method to create reflectors.
     * @param type type of the created reflector
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
    public static Reflector createReflector(int type)
    {
        switch (type)
        {
            //Enigma I
            case 10: return new ReflectorA().setTypeNumer(type);
            case 11: return new ReflectorB().setTypeNumer(type);
            case 12: return new ReflectorC().setTypeNumer(type);

            //Enigma M3
            case 20: return new ReflectorB().setTypeNumer(type);
            case 21: return new ReflectorC().setTypeNumer(type);

            //Enigma M4
            case 30: return new ReflectorThinB().setTypeNumer(type);
            case 31: return new ReflectorThinC().setTypeNumer(type);

            //Enigma G31
            case 40: return new ReflectorEnigma_D_KD_G31().setTypeNumer(type);

            //Enigma G312
            case 50: return new Reflector_G312().setTypeNumer(type);

            //Enigma G260
            case 60: return new Reflector_G260().setTypeNumer(type);

            //Enigma D
            case 70: return new ReflectorEnigma_D_KD_G31().setTypeNumer(type);

            //Enigma K
            case 80: return new ReflectorEnigma_K().setTypeNumer(type);

            //Enigma K Swiss
            case 90: return new ReflectorEnigma_K().setTypeNumer(type);

            //Enigma K Swiss Airforce
            case 100: return new ReflectorEnigma_K().setTypeNumer(type);

            //Enigma R
            case 110: return new Reflector_R().setTypeNumer(type);

            //Enigma T
            case 120: return new ReflectorEnigma_T().setTypeNumer(type);

            default:
                Log.d("Reflector:","Fail! "+type);
                return null;
        }
    }

    public Reflector setTypeNumer(int nr)
    {
        this.number = nr;
        return this;
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
     * Return the type indicator of the reflector
     * @return type
     */
    public String getType()
    {
        return this.type;
    }

    public int getNumber()
    {
        return this.number;
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
    private static class ReflectorA extends Reflector
    {
        public ReflectorA()
        {
            super("A", new int[]{4,9,12,25,0,11,24,23,21,1,22,5,2,17,16,20,14,13,19,18,15,8,10,7,6,3});
        }
    }

    /**
     * Concrete implementation of ReflectorB
     * Used in Enigma I, M3
     * AY  BR  CU  DH  EQ  FS  GL  IP  JX  KN  MO  TZ  VW
     */
    private static class ReflectorB extends Reflector
    {
        public ReflectorB()
        {
            super("B", new int[]{24,17,20,7,16,18,11,3,15,23,13,6,14,10,12,8,4,1,5,25,2,22,21,9,0,19});
        }
    }

    /**
     * Concrete implementation of ReflectorC
     * Used in Enigma I, M3
     * AF  BV  CP  DJ  EI  GO  HY  KR  LZ  MX  NW  QT  SU
     */
    private static class ReflectorC extends Reflector
    {
        public ReflectorC()
        {
            super("C", new int[]{5,21,15,9,8,0,14,24,4,3,17,25,23,22,6,2,19,10,20,16,18,1,13,12,7,11});
        }
    }

    /**
     * Concrete implementation of thin reflector type b (not equal to normal type b!)
     * When used with Rotor Beta on rotation 0, the pair was equivalent to normal reflector B
     * S->Beta->ThinB->Beta'->X == X->UKWB->S
     * Used in Enigma M4
     * E N K Q A U Y W J I C O P B L M D X Z V F T H R G S
     */
    private static class ReflectorThinB extends Reflector
    {
        public ReflectorThinB()
        {
            super("ThinB", new int[]{4,13,10,16,0,20,24,22,9,8,2,14,15,1,11,12,3,23,25,21,5,19,7,17,6,18});
        }
    }

    /**
     * Concrete implementation of thin reflector type c (not equal to normal type c!)
     * When used with Rotor Gamma on rotation 0, the pair was equivalent to normal reflector C
     * S->Gamma->ThinC->Gamma'->X == X->UKWC->S
     * Used in Enigma M4
     * R D O B J N T K V E H M L F C W Z A X G Y I P S U Q
     */
    private static class ReflectorThinC extends Reflector
    {
        public ReflectorThinC()
        {
            super("ThinC", new int[]{17,3,14,1,9,13,19,10,21,4,7,12,11,5,2,22,25,0,23,6,24,8,15,18,20,16});
        }
    }

    /**
     * Pluggable Reflector of the Enigma machine of type D and KD
     * Standard wiring: AI,BM,CE,DT,FG,HR,JY,KS,LQ,NZ,OX,PW,UV
     * Has additional ringSetting and can rotate
     */
    public static class ReflectorEnigma_D_KD_G31 extends Reflector
    {
        public ReflectorEnigma_D_KD_G31()
        {
            super("Ref-D", defaultWiring_D_KD_G31);
        }
    }

    /**
     * Reflector as used in various Enigma models of the K-Series
     * I M E T C G F R A Y S Q B Z X W L H K D V U P O J N
     */
    private static class ReflectorEnigma_K extends Reflector
    {
        public ReflectorEnigma_K()
        {
            super("Ref-K", new int[]{8,12,4,19,2,6,5,17,0,24,18,16,1,25,23,22,11,7,10,3,21,20,15,14,9,13});
        }
    }
    /**
     * Reflector as used in the Enigma type T (Tirpitz)
     * G E K P B T A U M O C N I L J D X Z Y F H W V Q S R
     */
    private static class ReflectorEnigma_T extends Reflector
    {
        public ReflectorEnigma_T()
        {
            super("Ref-T", new int[]{6,4,10,15,1,19,0,20,12,14,2,13,8,11,9,3,23,25,24,5,7,22,21,16,18,17});
        }
    }

    /**
     * Reflector as used in the Enigma type G-312 Abwehr
     * R U L Q M Z J S Y G O C E T K W D A H N B X P V I F
     */
    private static class Reflector_G312 extends Reflector
    {
        public Reflector_G312()
        {
            super("Ref-G312", new int[]{17,20,11,16,12,25,9,18,24,6,14,2,4,19,10,22,3,0,7,13,1,23,15,21,8,5});
        }
    }

    /**
     * Reflector as used in the Enigma type G-260 Abwehr
     * I M E T C G F R A Y S Q B Z X W L H K D V U P O J N
     */
    private static class Reflector_G260 extends Reflector
    {
        public Reflector_G260()
        {
            super("Ref-G260", new int[]{8,12,4,19,2,6,5,17,0,24,18,16,1,25,23,22,11,7,10,3,21,20,15,14,9,13});
        }
    }

    /**
     * Reflector as used in the Enigma Type R "Rocket" (Reichsbahn)
     */
    private static class Reflector_R extends Reflector
    {
        public Reflector_R()
        {
            super("Ref-R", new int[]{16,24,7,14,6,13,4,2,21,15,20,25,19,5,3,9,0,23,22,12,10,8,18,17,1,11});
        }
    }

}
