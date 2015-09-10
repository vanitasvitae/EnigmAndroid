package de.vanitasvitae.enigmandroid.enigma.rotors;

import java.util.ArrayList;

import de.vanitasvitae.enigmandroid.enigma.Plugboard;

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
    protected Integer[] connections;

    /**
     * This constructor is not accessible from outside this class file.
     * Use the one of the createReflector* methods instead to create concrete Reflectors from
     * outside this class file
     * @param type type indicator of the reflector
     * @param connections wiring of the reflector as Integer array
     */
    protected Reflector(String type, int number, Integer[] connections)
    {
        this.type = type;
        this.number = number;
        this.connections = connections;
    }

    public int getRotation()
    {
        return 0;
    }

    public int getRingSetting()
    {
        return 0;
    }

    public void setRotation(int rotation)
    {
        //Nope, do it yourself by overriding
    }

    public void setRingSetting(int ringSetting)
    {
        //Nopedydope
    }

    /**
     * Factory method to create reflectors.
     * @param type type of the created reflector
     *             "A" -> ReflectorA
     *             "B" -> ReflectorB
     *             "C" -> ReflectorC
     *             "ThinB" -> ReflectorThinB
     *             "ThinC" -> ReflectorThinC
     *             "ReflectorD" -> ReflectorEnigmaD
     *             default -> ReflectorB
     * @return Reflector
     */
    public static Reflector createReflector(int type)
    {
        switch (type)
        {
            case 1: return new ReflectorA();
            case 2: return new ReflectorB();
            case 3: return new ReflectorC();
            case 4: return new ReflectorThinB();
            case 5: return new ReflectorThinC();
            case 6: return new ReflectorEnigmaD();
            default: return new ReflectorB();
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
            super("A", 1, new Integer[]{4,9,12,25,0,11,24,23,21,1,22,5,2,17,16,20,14,13,19,18,15,8,10,7,6,3});
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
            super("B", 2, new Integer[]{24,17,20,7,16,18,11,3,15,23,13,6,14,10,12,8,4,1,5,25,2,22,21,9,0,19});
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
            super("C", 3, new Integer[]{5,21,15,9,8,0,14,24,4,3,17,25,23,22,6,2,19,10,20,16,18,1,13,12,7,11});
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
            super("ThinB", 4, new Integer[]{4,13,10,16,0,20,24,22,9,8,2,14,15,1,11,12,3,23,25,21,5,19,7,17,6,18});
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
            super("ThinC", 5, new Integer[]{17,3,14,1,9,13,19,10,21,4,7,12,11,5,2,22,25,0,23,6,24,8,15,18,20,16});
        }
    }

    /**
     * Plugable Reflector of the Enigma machine of type D
     * Standard wiring: AI,BM,CE,DT,FG,HR,JY,KS,LQ,NZ,OX,PW,UV
     * Has additional ringSetting and can rotate
     */
    public static class ReflectorEnigmaD extends Reflector
    {
        public static final String defaultWiring = "AI,BM,CE,DT,FG,HR,JY,KS,LQ,NZ,OX,PW,UV";
        private Plugboard connections;
        private int ringSetting;
        private int rotation;
        public ReflectorEnigmaD()
        {
            super("Ref-D", 6, null);
            connections = new Plugboard();
            reset();
        }

        public ReflectorEnigmaD(int[][] conf)
        {
            super("Ref-D", 6, null);
            connections = new Plugboard(conf);
        }

        public void reset()
        {
            connections.setConfiguration(Plugboard.parseConfigurationString(defaultWiring));
        }

        public boolean setWiring(String input)
        {
            if(input.replace(" ","").length() != 38)
                return false;
            return connections.setConfiguration(Plugboard.parseConfigurationString(input));
        }

        public String getConfiguration()
        {
            return connections.getConfigurationString();
        }

        @Override
        public int getRingSetting()
        {
            return this.ringSetting;
        }

        @Override
        public int getRotation()
        {
            return this.rotation;
        }

        @Override
        public void setRingSetting(int ringSetting)
        {
            this.ringSetting = ringSetting;
        }

        @Override
        public void setRotation(int rotation)
        {
            this.rotation = rotation;
        }

        @Override
        public int encrypt(int input)
        {
            return this.connections.encrypt(input);
        }

        public boolean isValidConfiguration()
        {
            return getConfiguration().length() == 38;
        }

        public static ArrayList<Character> getDuplicatePlugs(String conf)
        {
            String s = conf.toUpperCase().replace(" ","").replace(",","");
            ArrayList<Character> duplicates = new ArrayList<>();
            for(int i=0; i<26; i++)
            {
                char c = (char) (i+65);
                if(s.indexOf(c) == s.lastIndexOf(c)) duplicates.add(c);
            }
            if(duplicates.size() == 0) return null;
            return duplicates;
        }

        public static ArrayList<Character> getMissingPlugs(String conf)
        {
            String s = conf.toUpperCase().replace(" ","").replace(",","");
            ArrayList<Character> missing = new ArrayList<Character>();
            for(int i=0; i<26; i++)
            {
                missing.add((char) (i + 65));
            }
            for(Character c : missing)
            {
                if(s.indexOf(c) != -1) missing.remove(c);
            }
            if(missing.size() == 0) return null;
            return missing;
        }
    }

}
