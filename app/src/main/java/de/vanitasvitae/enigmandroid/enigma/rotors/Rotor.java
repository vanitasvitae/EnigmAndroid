package de.vanitasvitae.enigmandroid.enigma.rotors;

import android.util.Log;

import de.vanitasvitae.enigmandroid.MainActivity;

/**
 * Rotor super class and inner concrete implementations
 * The rotors were the key feature of the enigma used to scramble up input signals into
 * encrypted signals difficult to predict. The rotors rotated to achieve a poly-alphabetic
 * substitution which was hard to break. Each signal passes the rotor twice. Once in "forward"-
 * direction and once in "backwards"-direction. There was a set of 3 out of 5 rotors inside the
 * enigma machine M4.
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
public class Rotor
{
    protected String type;
    protected int number;
    protected Integer[] connections;
    protected Integer[] reversedConnections;
    protected Integer[] turnOverNotches;

    protected int ringSetting;
    protected int rotation;

    /**
     * This constructor is not accessible from outside this class file.
     * Use one of the createRotor* factory methods instead to create concrete Rotors.
     * Note that connections and reversedConnections MUST be of the same size and that
     * neither connections nor reversedConnections respectively MUST have any number between
     * 0 and connections.length-1 only once (ie they represent permutations)
     * @param type type indicator
     * @param connections wiring of the rotor as Integer array
     * @param reversedConnections inverse wiring used to encryptString in the opposite direction
     *                            (connections[reversedConnections[i]] = i
     *                            for all i in 0..getRotorSize()-1.
     * @param turnOverNotches Position(s) of the turnover notch(es)
     * @param ringSetting setting of the ring that holds the letters
     * @param rotation rotation of the rotor
     */
    protected Rotor(String type, Integer[] connections, Integer[] reversedConnections,
                    Integer[] turnOverNotches, int ringSetting, int rotation)
    {
        this.type = type;
        this.connections = connections;
        this.reversedConnections = reversedConnections;
        this.turnOverNotches = turnOverNotches;
        this.ringSetting = ringSetting;
        this.rotation = rotation;
    }

    protected Rotor setTypeNumber(int typenr)
    {
        this.number = typenr;
        return this;
    }

    /**
     * Factory method that creates a rotor accordingly to the type.
     * Also initialize the rotor with ringSetting and rotation.
     * @param type type indicator (1..10)
     *             1..8 -> I..VIII
     *             9,10 -> Beta, Gamma
     *             11..13 -> DI..DIII
     * @param ringSetting setting of the outer ring (0..25)
     * @param rotation rotation of the rotor
     * @return Concrete rotor
     */
    public static Rotor createRotor(int type, int rotation, int ringSetting)
    {
        Log.d(MainActivity.APP_ID, "Rotorcreation: "+type);
        switch (type)
        {
            case 1: return new EntryWheel_QWERTZ().setTypeNumber(type);
            case 2: return new EntryWheel_T().setTypeNumber(type);

            //Enigma I
            case 10: return new Rotor_I(rotation, ringSetting).setTypeNumber(type);
            case 11: return new Rotor_II(rotation, ringSetting).setTypeNumber(type);
            case 12: return new Rotor_III(rotation, ringSetting).setTypeNumber(type);
            case 13: return new Rotor_IV(rotation, ringSetting).setTypeNumber(type);
            case 14: return new Rotor_V(rotation, ringSetting).setTypeNumber(type);

            //Enigma M3
            case 20: return new Rotor_I(rotation, ringSetting).setTypeNumber(type);
            case 21: return new Rotor_II(rotation, ringSetting).setTypeNumber(type);
            case 22: return new Rotor_III(rotation, ringSetting).setTypeNumber(type);
            case 23: return new Rotor_IV(rotation, ringSetting).setTypeNumber(type);
            case 24: return new Rotor_V(rotation, ringSetting).setTypeNumber(type);
            case 25: return new Rotor_VI(rotation, ringSetting).setTypeNumber(type);
            case 26: return new Rotor_VII(rotation, ringSetting).setTypeNumber(type);
            case 27: return new Rotor_VIII(rotation, ringSetting).setTypeNumber(type);

            //Enigma M4
            case 30: return new Rotor_I(rotation, ringSetting).setTypeNumber(type);
            case 31: return new Rotor_II(rotation, ringSetting).setTypeNumber(type);
            case 32: return new Rotor_III(rotation, ringSetting).setTypeNumber(type);
            case 33: return new Rotor_IV(rotation, ringSetting).setTypeNumber(type);
            case 34: return new Rotor_V(rotation, ringSetting).setTypeNumber(type);
            case 35: return new Rotor_VI(rotation, ringSetting).setTypeNumber(type);
            case 36: return new Rotor_VII(rotation, ringSetting).setTypeNumber(type);
            case 37: return new Rotor_VIII(rotation, ringSetting).setTypeNumber(type);
            case 38: return new Rotor_M4_Beta(rotation, ringSetting).setTypeNumber(type);
            case 39: return new Rotor_M4_Gamma(rotation, ringSetting).setTypeNumber(type);

            //Enigma G31
            case 40: return new Rotor_G31_I(rotation, ringSetting).setTypeNumber(type);
            case 41: return new Rotor_G31_II(rotation, ringSetting).setTypeNumber(type);
            case 42: return new Rotor_G31_III(rotation, ringSetting).setTypeNumber(type);

            //Enigma G312
            case 50: return new Rotor_G312_I(rotation, ringSetting).setTypeNumber(type);
            case 51: return new Rotor_G312_II(rotation, ringSetting).setTypeNumber(type);
            case 52: return new Rotor_G312_III(rotation, ringSetting).setTypeNumber(type);

            //Enigma G260
            case 60: return new Rotor_G260_I(rotation, ringSetting).setTypeNumber(type);
            case 61: return new Rotor_G260_II(rotation, ringSetting).setTypeNumber(type);
            case 62: return new Rotor_G260_III(rotation, ringSetting).setTypeNumber(type);

            //Enigma D
            case 70: return new Rotor_D_I(rotation, ringSetting).setTypeNumber(type);
            case 71: return new Rotor_D_II(rotation, ringSetting).setTypeNumber(type);
            case 72: return new Rotor_D_III(rotation, ringSetting).setTypeNumber(type);

            //Enigma K
            case 80: return new Rotor_K_I(rotation, ringSetting).setTypeNumber(type);
            case 81: return new Rotor_K_II(rotation, ringSetting).setTypeNumber(type);
            case 82: return new Rotor_K_III(rotation, ringSetting).setTypeNumber(type);

            //Enigma K Swiss
            case 90: return new Rotor_KSwiss_Standard_I(rotation, ringSetting).setTypeNumber(type);
            case 91: return new Rotor_KSwiss_Standard_II(rotation, ringSetting).setTypeNumber(type);
            case 92: return new Rotor_KSwiss_Standard_III(rotation, ringSetting).setTypeNumber(type);

            //Enigma K Swiss Airforce
            case 100: return new Rotor_K_Swiss_Airforce_I(rotation, ringSetting).setTypeNumber(type);
            case 101: return new Rotor_K_Swiss_Airforce_II(rotation, ringSetting).setTypeNumber(type);
            case 102: return new Rotor_K_Swiss_Airforce_III(rotation, ringSetting).setTypeNumber(type);

            //Enigma R
            case 110: return new Rotor_R_I(rotation, ringSetting).setTypeNumber(type);
            case 111: return new Rotor_R_II(rotation, ringSetting).setTypeNumber(type);
            case 112: return new Rotor_R_III(rotation, ringSetting).setTypeNumber(type);

            //Enigma T
            case 120: return new Rotor_T_I(rotation, ringSetting).setTypeNumber(type);
            case 121: return new Rotor_T_II(rotation, ringSetting).setTypeNumber(type);
            case 122: return new Rotor_T_III(rotation, ringSetting).setTypeNumber(type);
            case 123: return new Rotor_T_IV(rotation, ringSetting).setTypeNumber(type);
            case 124: return new Rotor_T_V(rotation, ringSetting).setTypeNumber(type);
            case 125: return new Rotor_T_VI(rotation, ringSetting).setTypeNumber(type);
            case 126: return new Rotor_T_VII(rotation, ringSetting).setTypeNumber(type);
            case 127: return new Rotor_T_VIII(rotation, ringSetting).setTypeNumber(type);

            default: Log.e(MainActivity.APP_ID," Tried to create Rotor of invalid type "+type);
                return null;
        }
    }

    /**
     * Encrypt an input signal via the internal wiring in "forward" direction towards the reflector
     * (using connections)
     * @param input signal
     * @return encrypted signal
     */
    public int encryptForward(int input)
    {
        return this.connections[normalize(input)];
    }

    /**
     * Encrypt an input signal via the internal wiring in "backwards" direction (using
     * reversedConnections)
     * @param input signal
     * @return encrypted signal
     */
    public int encryptBackward(int input)
    {
        return this.reversedConnections[normalize(input)];
    }

    /**
     * Return the type indicator (usually 1..5)
     * @return type indicator
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
     * Return the current rotation of the rotor.
     * The rotation consists of the actual rotation - the ringSetting
     * @return rotation-ringSetting
     */
    public int getRotation()
    {
        return this.rotation;
    }

    /**
     * Increment rotation of the rotor by one.
     */
    public void rotate()
    {
        this.rotation = normalize(this.getRotation()+1);
    }

    /**
     * Return true, if the rotor is at a position, where it turns over the next rotor by one
     * @return rotation==turnOverNotch
     */
    public boolean isAtTurnoverPosition()
    {
        for(int x : getTurnOverNotches())
        {
            //if(x == this.rotation + this.ringSetting) return true;
            if(x == this.rotation) return true;
        }
        return false;
    }

    /**
     * Return true, if the rotor is in a position where the double turn anomaly happens.
     * The double turn anomaly (german: Doppelsprung-Anomalie) is an anomaly in the rotor movement
     * caused by the mechanical implementation of the enigma.
     * Whenever the rightmost rotor turns the middle rotor AND the middle rotor is only one move
     * from turning the leftmost rotor, the middle rotor turns again with the next character.
     * So technically there are only 26*25*26 possible rotor settings for any but firmly 3 rotors.
     * @return rotation == turnOverNotch-1
     */
    public boolean doubleTurnAnomaly()
    {
        for(int x : getTurnOverNotches())
        {
            if(this.rotation == x-1) return true;
        }
        return false;
    }

    /**
     * Returns the positions of the turnover notches in a array
     * @return turnOverNotches
     */
    public Integer[] getTurnOverNotches()
    {
        return this.turnOverNotches;
    }

    /**
     * Return ringSettings of the rotor
     * @return ringSetting
     */
    public int getRingSetting()
    {
        return this.ringSetting;
    }


    /**
     * Returns the size (ie the number of wires/size of the connections array)
     * of the rotor
     * @return size
     */
    public int getRotorSize()
    {
        return this.connections.length;
    }

    /**
     * Normalize the input.
     * Normalizing means keeping the input via modulo in the range from 0 to n-1, where n is equal
     * to the size of the rotor. This is necessary since java allows negative modulo values,
     * which can break this implementation
     * @param input input signal
     * @return "normalized" input signal
     */
    public int normalize(int input)
    {
        return (input+this.getRotorSize())%this.getRotorSize();
    }

    /**
     * Concrete implementation of Rotor of type 1 (I)
     * Used in Enigma I, M3, M4
     * E K M F L G D Q V Z N T O W Y H X U S P A I B R C J
     */
    private static class Rotor_I extends Rotor
    {
        public Rotor_I(int rotation, int ringSetting)
        {
            super("I",
                    new Integer[]{4, 10, 12, 5, 11, 6, 3, 16, 21, 25, 13, 19, 14, 22, 24, 7, 23, 20, 18, 15, 0, 8, 1, 17, 2, 9},
                    new Integer[]{20, 22, 24, 6, 0, 3, 5, 15, 21, 25, 1, 4, 2, 10, 12, 19, 7, 23, 18, 11, 17, 8, 13, 16, 14, 9},
                    new Integer[]{17}, ringSetting, rotation);
        }
    }

    /**
     * Concrete implementation of Rotor of type 2 (II)
     * Used in Enigma I, M3, M4
     * A J D K S I R U X B L H W T M C Q G Z N P Y F V O E
     */
    private static class Rotor_II extends Rotor
    {
        public Rotor_II(int rotation, int ringSetting)
        {
            super("II",
                    new Integer[]{0, 9, 3, 10, 18, 8, 17, 20, 23, 1, 11, 7, 22, 19, 12, 2, 16, 6, 25, 13, 15, 24, 5, 21, 14, 4},
                    new Integer[]{0, 9, 15, 2, 25, 22, 17, 11, 5, 1, 3, 10, 14, 19, 24, 20, 16, 6, 4, 13, 7, 23, 12, 8, 21, 18},
                    new Integer[]{5}, ringSetting, rotation);
        }
    }

    /**
     * Concrete implementation of Rotor of type 3 (III)
     * Used in Enigma I, M3, M4
     * B D F H J L C P R T X V Z N Y E I W G A K M U S Q O
     */
    private static class Rotor_III extends Rotor
    {
        public Rotor_III(int rotation, int ringSetting)
        {
            super("III",
                    new Integer[]{1, 3, 5, 7, 9, 11, 2, 15, 17, 19, 23, 21, 25, 13, 24, 4, 8, 22, 6, 0, 10, 12, 20, 18, 16, 14},
                    new Integer[]{19, 0, 6, 1, 15, 2, 18, 3, 16, 4, 20, 5, 21, 13, 25, 7, 24, 8, 23, 9, 22, 11, 17, 10, 14, 12},
                    new Integer[]{22}, ringSetting, rotation);
        }
    }

    /**
     * Concrete implementation of Rotor of type 4 (IV)
     * Used in Enigma M3, M4
     * E S O V P Z J A Y Q U I R H X L N F T G K D C M W B
     */
    private static class Rotor_IV extends Rotor
    {
        public Rotor_IV(int rotation, int ringSetting)
        {
            super("IV",
                    new Integer[]{4, 18, 14, 21, 15, 25, 9, 0, 24, 16, 20, 8, 17, 7, 23, 11, 13, 5, 19, 6, 10, 3, 2, 12, 22, 1},
                    new Integer[]{7, 25, 22, 21, 0, 17, 19, 13, 11, 6, 20, 15, 23, 16, 2, 4, 9, 12, 1, 18, 10, 3, 24, 14, 8, 5},
                    new Integer[]{10}, ringSetting, rotation);
        }
    }

    /**
     * Concrete implementation of Rotor of type 5 (V)
     * Used in Enigma M3, M4
     * V Z B R G I T Y U P S D N H L X A W M J Q O F E C K
     */
    private static class Rotor_V extends Rotor
    {
        public Rotor_V(int rotation, int ringSetting)
        {
            super("V",
                    new Integer[]{21, 25, 1, 17, 6, 8, 19, 24, 20, 15, 18, 3, 13, 7, 11, 23, 0, 22, 12, 9, 16, 14, 5, 4, 2, 10},
                    new Integer[]{16, 2, 24, 11, 23, 22, 4, 13, 5, 19, 25, 14, 18, 12, 21, 9, 20, 3, 10, 6, 8, 0, 17, 15, 7, 1},
                    new Integer[]{0}, ringSetting, rotation);
        }
    }

    /**
     * Concrete implementation of Rotor of type 6 (VI)
     * Used in Enigma M3, M4
     * J P G V O U M F Y Q B E N H Z R D K A S X L I C T W
     */
    private static class Rotor_VI extends Rotor
    {
        public Rotor_VI(int rotation, int ringSetting)
        {
            super("VI",
                    new Integer[]{9,15,6,21,14,20,12,5,24,16,1,4,13,7,25,17,3,10,0,18,23,11,8,2,19,22},
                    new Integer[]{18,10,23,16,11,7,2,13,22,0,17,21,6,12,4,1,9,15,19,24,5,3,25,20,8,14},
                    new Integer[]{0,13}, ringSetting, rotation);
        }
    }

    /**
     * Concrete implementation of Rotor of type 7 (VII)
     * Used in Enigma M3, M4
     * N Z J H G R C X M Y S W B O U F A I V L P E K Q D T
     */
    private static class Rotor_VII extends Rotor
    {
        public Rotor_VII(int rotation, int ringSetting)
        {
            super("VII",
                    new Integer[]{13,25,9,7,6,17,2,23,12,24,18,22,1,14,20,5,0,8,21,11,15,4,10,16,3,19},
                    new Integer[]{16,12,6,24,21,15,4,3,17,2,22,19,8,0,13,20,23,5,10,25,14,18,11,7,9,1},
                    new Integer[]{0,13}, ringSetting, rotation);
        }
    }

    /**
     * Concrete implementation of Rotor of type 8 (VIII)
     * Used in Enigma M3, M4
     * F K Q H T L X O C B J S P D Z R A M E W N I U Y G V
     */
    private static class Rotor_VIII extends Rotor
    {
        public Rotor_VIII(int rotation, int ringSetting)
        {
            super("VIII",
                    new Integer[]{5,10,16,7,19,11,23,14,2,1,9,18,15,3,25,17,0,12,4,22,13,8,20,24,6,21},
                    new Integer[]{16,9,8,13,18,0,24,3,21,10,1,5,17,20,7,12,2,15,11,4,22,25,19,6,23,14},
                    new Integer[]{0,13}, ringSetting, rotation);
        }
    }

    /**
     * Concrete implementation of Rotor of type beta (Griechenwalze Beta)
     * Beta was used as a "thin" rotor in the M4. It was thinner than a "normal" rotor, so it
     * could be used together with one of the two thin reflectors as one rotor.
     * When used together with ReflectorThinB, Beta was equivalent to Reflector B (if rotation == 0)
     * That way the M4 was backwards compatible to the M3
     * Used in M4
     */
    private static class Rotor_M4_Beta extends Rotor
    {
        public Rotor_M4_Beta(int rotation, int ringSetting)
        {
            super("Beta",
                    new Integer[]{11,4,24,9,21,2,13,8,23,22,15,1,16,12,3,17,19,0,10,25,6,5,20,7,14,18},
                    new Integer[]{17,11,5,14,1,21,20,23,7,3,18,0,13,6,24,10,12,15,25,16,22,4,9,8,2,19},
                    new Integer[]{}, ringSetting, rotation);
        }
        @Override
        public void rotate()
        {
            //Thin rotors are fixed in position, so they dont rotate
        }

        @Override
        public boolean doubleTurnAnomaly()
        {
            //Nope, no anomaly
            return false;
        }
    }

    /**
     * Concrete implementation of Rotor of type gamma (Griechenwalze Gamma)
     * Gamma was used as a "thin" rotor in the M4. It was thinner than a "normal" rotor, so it
     * could be used together with one of the two thin reflectors as one rotor.
     * When used together with ReflectorThinC, Gamma is equivalent to Reflector C
     * (if rotation == 0). That way the M4 was backwards compatible to the M3
     * Used in M4
     */
    private static class Rotor_M4_Gamma extends Rotor
    {
        public Rotor_M4_Gamma(int rotation, int ringSetting)
        {
            super("Gamma",
                    new Integer[]{5,18,14,10,0,13,20,4,17,7,12,1,19,8,24,2,22,11,16,15,25,23,21,6,9,3},
                    new Integer[]{4,11,15,25,7,0,23,9,13,24,3,17,10,5,2,19,18,8,1,12,6,22,16,21,14,20},
                    new Integer[]{}, ringSetting, rotation);
        }
        @Override
        public void rotate()
        {
            //Thin rotors are fixed in position, so they don't rotate
        }

        @Override
        public boolean doubleTurnAnomaly()
        {
            //Thin rotors don't do such weird stuff, they're normal just like you and me.
            return false;
        }
    }

    /**
     * EntryWheel as used in the Enigma models D, K, G
     * Q W E R T Z U I O A S D F G H J K P Y X C V B N M L
     */
    private static class EntryWheel_QWERTZ extends Rotor
    {
        public EntryWheel_QWERTZ()
        {
            super("ETW-D",
                    new Integer[]{9,22,20,11,2,12,13,14,7,15,16,25,24,23,8,17,0,3,10,4,6,21,1,19,18,5},
                    new Integer[]{16,22,4,17,19,25,20,8,14,0,18,3,5,6,7,9,10,15,24,23,2,21,1,13,12,11},
                    new Integer[]{}, 0, 0);
        }
        @Override
        public void rotate()
        {
            //EntryWheel doesn't rotate
        }

        @Override
        public boolean doubleTurnAnomaly()
        {
            //\forall s \in States : nope
            return false;
        }
    }

    /**
     * Rotor I as used in the Enigma Type D
     * L P G S Z M H A E O Q K V X R F Y B U T N I C J D W
     * Turnover Z
     */
    private static class Rotor_D_I extends Rotor
    {
        public Rotor_D_I(int rotation, int ringSetting)
        {
            super("D-I",
                    new Integer[]{11,15,6,18,25,12,7,0,4,14,16,10,21,23,17,5,24,1,20,19,13,8,2,9,3,22},
                    new Integer[]{7,17,22,24,8,15,2,6,21,23,11,0,5,20,9,1,10,14,3,19,18,12,25,13,16,4},
                    new Integer[]{25}, ringSetting, rotation);
        }
    }

    /**
     * Rotor II as used in the Enigma Type D
     * S L V G B T F X J Q O H E W I R Z Y A M K P C N D U
     * Turnover F
     */
    private static class Rotor_D_II extends Rotor
    {
        public Rotor_D_II(int rotation, int ringSetting)
        {
            super("D-II",
                    new Integer[]{18,11,21,6,1,19,5,23,9,16,14,7,4,22,8,17,25,24,0,12,10,15,2,13,3,20},
                    new Integer[]{18,4,22,24,12,6,3,11,14,8,20,1,19,23,10,21,9,15,0,5,25,2,13,7,17,16},
                    new Integer[]{5}, ringSetting, rotation);
        }
    }

    /**
     * Rotor III as used in the Enigma Type D
     * C J G D P S H K T U R A W Z X F M Y N Q O B V L I E
     * Turnover O
     */
    private static class Rotor_D_III extends Rotor
    {
        public Rotor_D_III(int rotation, int ringSetting)
        {
            super("D-III",
                    new Integer[]{2,9,6,3,15,18,7,10,19,20,17,0,22,25,23,5,12,24,13,16,14,1,21,11,8,4},
                    new Integer[]{11,21,0,3,25,15,2,6,24,1,7,23,16,18,20,4,19,10,5,8,9,22,12,14,17,13},
                    new Integer[]{14}, ringSetting, rotation);
        }
    }

    /**
     * Rotor I as used in the Enigma Type K (Switzerland)
     * P E Z U O H X S C V F M T B G L R I N Q J W A Y D K
     * Turnover Z
     */
    private static class Rotor_KSwiss_Standard_I extends Rotor
    {
        public Rotor_KSwiss_Standard_I(int rotation, int ringSetting)
        {
            super("KS-I",
                    new Integer[]{15,4,25,20,14,7,23,18,2,21,5,12,19,1,6,11,17,8,13,16,9,22,0,24,3,10},
                    new Integer[]{22,13,8,24,1,10,14,5,17,20,25,15,11,18,4,0,19,16,7,12,3,9,21,6,23,2},
                    new Integer[]{25}, ringSetting, rotation);
        }
    }

    /**
     * Rotor II as used in the Enigma Type K (Switzerland)
     * Z O U E S Y D K F W P C I Q X H M V B L G N J R A T
     * Turnover F
     */
    private static class Rotor_KSwiss_Standard_II extends Rotor
    {
        public Rotor_KSwiss_Standard_II(int rotation, int ringSetting)
        {
            super("KS-II",
                    new Integer[]{25,14,20,4,18,24,3,10,5,22,15,2,8,16,23,7,12,21,1,11,6,13,9,17,0,19},
                    new Integer[]{24,18,11,6,3,8,20,15,12,22,7,19,16,21,1,10,13,23,4,25,2,17,9,14,5,0},
                    new Integer[]{5}, ringSetting, rotation);
        }
    }

    /**
     * Rotor III as used in the Enigma Type K (Switzerland)
     * E H R V X G A O B Q U S I M Z F L Y N W K T P D J C
     * Turnover O
     */
    private static class Rotor_KSwiss_Standard_III extends Rotor
    {
        public Rotor_KSwiss_Standard_III(int rotation, int ringSetting)
        {
            super("KS-III",
                    new Integer[]{4,7,17,21,23,6,0,14,1,16,20,18,8,12,25,5,11,24,13,22,10,19,15,3,9,2},
                    new Integer[]{6,8,25,23,0,15,5,1,12,24,20,16,13,18,7,22,9,2,11,21,10,3,19,4,17,14},
                    new Integer[]{14}, ringSetting, rotation);
        }
    }

    /**
     * EntryWheel as used only in the Enigma Type T Tirpitz
     * K Z R O U Q H Y A I G B L W V S T D X F P N M C J E
     */
    private static class EntryWheel_T extends Rotor
    {
        public EntryWheel_T()
        {
            super("T-ETW",
                    new Integer[]{8,11,23,17,25,19,10,6,9,24,0,12,22,21,3,20,5,2,15,16,4,14,13,18,7,1},
                    new Integer[]{10,25,17,14,20,16,7,24,0,8,6,1,11,22,21,18,19,3,23,5,15,13,12,2,9,4},
                    new Integer[]{}, 0, 0);
        }
        @Override
        public void rotate()
        {
            //EntryWheel doesn't rotate
        }

        @Override
        public boolean doubleTurnAnomaly()
        {
            //\forall s \in States : nope
            return false;
        }
    }
    /**
     * Rotor I as used in the Enigma Type T Tirpitz
     * K P T Y U E L O C V G R F Q D A N J M B S W H Z X I
     * Turnover X A F L R
     */
    private static class Rotor_T_I extends Rotor
    {
        public Rotor_T_I(int rotation, int ringSetting)
        {
            super("T-I",
                    new Integer[]{10,15,19,24,20,4,11,14,2,21,6,17,5,16,3,0,13,9,12,1,18,22,7,25,23,8},
                    new Integer[]{15,19,8,14,5,12,10,22,25,17,0,6,18,16,7,1,13,11,20,2,4,9,21,24,3,23},
                    new Integer[]{23,0,5,11,17}, ringSetting, rotation);
        }
    }

    /**
     * Rotor II as used in the Enigma Type T Tirpitz
     * U P H Z L W E Q M T D J X C A K S O I G V B Y F N R
     * Turnover X A G M S
     */
    private static class Rotor_T_II extends Rotor
    {
        public Rotor_T_II(int rotation, int ringSetting)
        {
            super("T-II",
                    new Integer[]{20,15,7,25,11,22,4,16,12,19,3,9,23,2,0,10,18,14,8,6,21,1,24,5,13,17},
                    new Integer[]{14,21,13,10,6,23,19,2,18,11,15,4,8,24,17,1,7,25,16,9,0,20,5,12,22,3},
                    new Integer[]{23,0,6,12,18}, ringSetting, rotation);
        }
    }

    /**
     * Rotor III as used in the Enigma Type T Tirpitz
     * Q U D L Y R F E K O N V Z A X W H M G P J B S I C T
     * Turnover X A F L R
     */
    private static class Rotor_T_III extends Rotor
    {
        public Rotor_T_III(int rotation, int ringSetting) {
            super("T-III",
                    new Integer[]{16,20,3,11,24,17,5,4,10,14,13,21,25,0,23,22,7,12,6,15,9,1,18,8,2,19},
                    new Integer[]{13,21,24,2,7,6,18,16,23,20,8,3,17,10,9,19,0,5,22,25,1,11,15,14,4,12},
                    new Integer[]{23,0,5,11,17}, ringSetting, rotation);
        }
    }

    /**
     * Rotor IV as used in the Enigma Type T Tirpitz
     * C I W T B K X N R E S P F L Y D A G V H Q U O J Z M
     * Turnover X A G M S
     */
    private static class Rotor_T_IV extends Rotor
    {
        public Rotor_T_IV(int rotation, int ringSetting)
        {
            super("T-IV",
                    new Integer[]{2,8,22,19,1,10,23,13,17,4,18,15,5,11,24,3,0,6,21,7,16,20,14,9,25,12},
                    new Integer[]{16,4,0,15,9,12,17,19,1,23,5,13,25,7,22,11,20,8,10,3,21,18,2,6,14,24},
                    new Integer[]{23,0,6,12,18}, ringSetting, rotation);
        }
    }

    /**
     * Rotor V as used in the Enigma Type T Tirpitz
     * U A X G I S N J B V E R D Y L F Z W T P C K O H M Q
     * Turnover Z D G L S
     */
    private static class Rotor_T_V extends Rotor
    {
        public Rotor_T_V(int rotation, int ringSetting)
        {
            super("T-V",
                    new Integer[]{20,0,23,6,8,18,13,9,1,21,4,17,3,24,11,5,25,22,19,15,2,10,14,7,12,16},
                    new Integer[]{1,8,20,12,10,15,3,23,4,7,21,14,24,6,22,19,25,11,5,18,0,9,17,2,13,16},
                    new Integer[]{25,3,6,11,18}, ringSetting, rotation);
        }
    }

    /**
     * Rotor VI as used in the Enigma Type T Tirpitz
     * X F U Z G A L V H C N Y S E W Q T D M R B K P I O J
     * Turnover Y F J N R
     */
    private static class Rotor_T_VI extends Rotor
    {
        public Rotor_T_VI(int rotation, int ringSetting)
        {
            super("T-VI",
                    new Integer[]{23,5,20,25,6,0,11,21,7,2,13,24,18,4,22,16,19,3,12,17,1,10,15,8,14,9},
                    new Integer[]{5,20,9,17,13,1,4,8,23,25,21,6,18,10,24,22,15,19,12,16,2,7,14,0,11,3},
                    new Integer[]{24,5,9,13,17}, ringSetting, rotation);
        }
    }

    /**
     * Rotor VII as used in the Enigma Type T Tirpitz
     * B J V F T X P L N A Y O Z I K W G D Q E R U C H S M
     * Turnover Z D G L S
     */
    private static class Rotor_T_VII extends Rotor
    {
        public Rotor_T_VII(int rotation, int ringSetting)
        {
            super("T-VII",
                    new Integer[]{1,9,21,5,19,23,15,11,13,0,24,14,25,8,10,22,6,3,16,4,17,20,2,7,18,12},
                    new Integer[]{9,0,22,17,19,3,16,23,13,1,14,7,25,8,11,6,18,20,24,4,21,2,15,5,10,12},
                    new Integer[]{25,3,6,11,18}, ringSetting, rotation);
        }
    }

    /**
     * Rotor VIII as used in the Enigma Type T Tirpitz
     * Y M T P N Z H W K O D A J X E L U Q V G C B I S F R
     * Turnover Y F J N R
     */
    private static class Rotor_T_VIII extends Rotor
    {
        public Rotor_T_VIII(int rotation, int ringSetting)
        {
            super("T-VIII",
                    new Integer[]{24,12,19,15,13,25,7,22,10,14,3,0,9,23,4,11,20,16,21,6,2,1,8,18,5,17},
                    new Integer[]{11,21,20,10,14,24,19,6,22,12,8,15,1,4,9,3,17,25,23,2,16,18,7,13,0,5},
                    new Integer[]{24,5,9,13,17}, ringSetting, rotation);
        }
    }

    /**
     * Rotor I as used in the Enigma Type G312 Abwehr
     * D M T W S I L R U Y Q N K F E J C A Z B P G X O H V
     * Turnover T V W X A B C D F G H J L M P Q R
     */
    private static class Rotor_G312_I extends Rotor
    {
        public Rotor_G312_I(int rotation, int ringSetting)
        {
            super("G312-I",
                    new Integer[]{3,12,19,22,18,8,11,17,20,24,16,13,10,5,4,9,2,0,25,1,15,6,23,14,7,21},
                    new Integer[]{17,19,16,0,14,13,21,24,5,15,12,6,1,11,23,20,10,7,4,2,8,25,3,22,9,18},
                    new Integer[]{19,21,22,23,0,1,2,3,5,6,7,9,11,12,15,16,17}, ringSetting, rotation);
        }
    }

    /**
     * Rotor II as used in the Enigma Type G312 Abwehr
     * H Q Z G P J T M O B L N C I F D Y A W V E U S R K X
     * Turnover T U W Z A B D E G H I L N O R
     */
    private static class Rotor_G312_II extends Rotor
    {
        public Rotor_G312_II(int rotation, int ringSetting)
        {
            super("G312-II",
                    new Integer[]{7,16,25,6,15,9,19,12,14,1,11,13,2,8,5,3,24,0,22,21,4,20,18,17,10,23},
                    new Integer[]{17,9,12,15,20,14,3,0,13,5,24,10,7,11,8,4,1,23,22,6,21,19,18,25,16,2},
                    new Integer[]{19,20,22,25,0,1,3,4,6,7,8,11,13,14,17}, ringSetting, rotation);
        }
    }

    /**
     * Rotor III as used in the Enigma Type G312 Abwehr
     * U Q N T L S Z F M R E H D P X K I B V Y G J C W O A
     * Turnover V X Y B F G I L N O S
     */
    private static class Rotor_G312_III extends Rotor
    {
        public Rotor_G312_III(int rotation, int ringSetting)
        {
            super("G312-III",
                    new Integer[]{20,16,13,19,11,18,25,5,12,17,4,7,3,15,23,10,8,1,21,24,6,9,2,22,14,0},
                    new Integer[]{25,17,22,12,10,7,20,11,16,21,15,4,8,2,24,13,1,9,5,3,0,18,23,14,19,6},
                    new Integer[]{21,23,24,1,5,6,8,11,13,14,18}, ringSetting, rotation);
        }
    }

    /**
     * Rotor I as used in the Enigma Type G260 Abwehr
     * R C S P B L K Q A U M H W Y T I F Z V G O J N E X D
     * Turnover T V W X A B C D F G H J L M P Q R
     */
    private static class Rotor_G260_I extends Rotor
    {
        public Rotor_G260_I(int rotation, int ringSetting)
        {
            super("G260-I",
                    new Integer[]{17,2,18,15,1,11,10,16,0,20,12,7,22,24,19,8,5,25,21,6,14,9,13,4,23,3},
                    new Integer[]{8,4,1,25,23,16,19,11,15,21,6,5,10,22,20,3,7,0,2,14,9,18,12,24,13,17},
                    new Integer[]{19,21,22,23,0,1,2,3,5,6,7,9,11,12,15,16,17}, ringSetting, rotation);
        }
    }

    /**
     * Rotor II as used in the Enigma Type G260 Abwehr
     * W C M I B V P J X A R O S G N D L Z K E Y H U F Q T
     * Turnover T U W Z A B D E G H I L N O R
     */
    private static class Rotor_G260_II extends Rotor
    {
        public Rotor_G260_II(int rotation, int ringSetting)
        {
            super("G260-II",
                    new Integer[]{22,2,12,8,1,21,15,9,23,0,17,14,18,6,13,3,11,25,10,4,24,7,20,5,16,19},
                    new Integer[]{9,4,1,15,19,23,13,21,3,7,18,16,2,14,11,6,24,10,12,25,22,5,0,8,20,17},
                    new Integer[]{19,20,22,25,0,1,3,4,6,7,8,11,13,14,17}, ringSetting, rotation);
        }
    }

    /**
     * Rotor III as used in the Enigma Type G260 Abwehr
     * F V D H Z E L S Q M A X O K Y I W P G C B U J T N R
     * Turnover V X Y B F G I L N O S
     */
    private static class Rotor_G260_III extends Rotor
    {
        public Rotor_G260_III(int rotation, int ringSetting)
        {
            super("G260-III",
                    new Integer[]{5,21,3,7,25,4,11,18,16,12,0,23,14,10,24,8,22,15,6,2,1,20,9,19,13,17},
                    new Integer[]{10,20,19,2,5,0,18,3,15,22,13,6,9,24,12,17,8,25,7,23,21,1,16,11,14,4},
                    new Integer[]{21,23,24,1,5,6,8,11,13,14,18}, ringSetting, rotation);
        }
    }

    /**
     * Rotor I as used in the Enigma Type G31 Abwehr
     * L P G S Z M H A E O Q K V X R F Y B U T N I C J D W
     * Turnover T V W X A B C D F G H J L M P Q R
     */
    private static class Rotor_G31_I extends Rotor
    {
        public Rotor_G31_I(int rotation, int ringSetting)
        {
            super("G31-I",
                    new Integer[]{11,15,6,18,25,12,7,0,4,14,16,10,21,23,17,5,24,1,20,19,13,8,2,9,3,22},
                    new Integer[]{7,17,22,24,8,15,2,6,21,23,11,0,5,20,9,1,10,14,3,19,18,12,25,13,16,4},
                    new Integer[]{19,21,22,23,0,1,2,3,5,6,7,9,11,12,15,16,17}, ringSetting, rotation);
        }
    }

    /**
     * Rotor II as used in the Enigma Type G31 Abwehr
     * S L V G B T F X J Q O H E W I R Z Y A M K P C N D U
     * Turnover T U W Z A B D E G H I L N O R
     */
    private static class Rotor_G31_II extends Rotor
    {
        public Rotor_G31_II(int rotation, int ringSetting)
        {
            super("G31_II",
                    new Integer[]{18,11,21,6,1,19,5,23,9,16,14,7,4,22,8,17,25,24,0,12,10,15,2,13,3,20},
                    new Integer[]{18,4,22,24,12,6,3,11,14,8,20,1,19,23,10,21,9,15,0,5,25,2,13,7,17,16},
                    new Integer[]{19,20,22,25,0,1,3,4,6,7,8,11,13,14,17}, ringSetting, rotation);
        }
    }

    /**
     * Rotor III as used in the Enigma Type G31 Abwehr
     * C J G D P S H K T U R A W Z X F M Y N Q O B V L I E
     * Turnover V X Y B F G I L N O S
     */
    private static class Rotor_G31_III extends Rotor
    {
        public Rotor_G31_III(int rotation, int ringSetting)
        {
            super("G31_III",
                    new Integer[]{2,9,6,3,15,18,7,10,19,20,17,0,22,25,23,5,12,24,13,16,14,1,21,11,8,4},
                    new Integer[]{11,21,0,3,25,15,2,6,24,1,7,23,16,18,20,4,19,10,5,8,9,22,12,14,17,13},
                    new Integer[]{21,23,24,1,5,6,8,11,13,14,18}, ringSetting, rotation);
        }
    }

    /**
     * Rotor I as used in the Enigma Type K
     * L P G S Z M H A E O Q K V X R F Y B U T N I C J D W
     * Turnover Z
     */
    private static class Rotor_K_I extends Rotor
    {
        public Rotor_K_I(int rotation, int ringSetting)
        {
            super("K-I",
                    new Integer[]{11,15,6,18,25,12,7,0,4,14,16,10,21,23,17,5,24,1,20,19,13,8,2,9,3,22},
                    new Integer[]{7,17,22,24,8,15,2,6,21,23,11,0,5,20,9,1,10,14,3,19,18,12,25,13,16,4},
                    new Integer[]{25}, ringSetting, rotation);
        }
    }

    /**
     * Rotor II as used in the Enigma type K
     * S L V G B T F X J Q O H E W I R Z Y A M K P C N D U
     * Turnover F
     */
    private static class Rotor_K_II extends Rotor
    {
        public Rotor_K_II(int rotation, int ringSetting)
        {
            super("K-II",
                    new Integer[]{18,11,21,6,1,19,5,23,9,16,14,7,4,22,8,17,25,24,0,12,10,15,2,13,3,20},
                    new Integer[]{18,4,22,24,12,6,3,11,14,8,20,1,19,23,10,21,9,15,0,5,25,2,13,7,17,16},
                    new Integer[]{5}, ringSetting, rotation);
        }
    }

    /**
     * Rotor III as used in the Enigma Type K
     * C J G D P S H K T U R A W Z X F M Y N Q O B V L I E
     * Turnover O
     */
    private static class Rotor_K_III extends Rotor
    {
        public Rotor_K_III(int rotation, int ringSetting)
        {
            super("K-III",
                    new Integer[]{2,9,6,3,15,18,7,10,19,20,17,0,22,25,23,5,12,24,13,16,14,1,21,11,8,4},
                    new Integer[]{11,21,0,3,25,15,2,6,24,1,7,23,16,18,20,4,19,10,5,8,9,22,12,14,17,13},
                    new Integer[]{14}, ringSetting, rotation);
        }
    }

    /**
     * Rotor I as used in the Enigma Type K (Swiss, Airforce)
     * PEZUOHXSCVFMTBGLRINQJWAYDK
     * Turnover Z
     */
    private static class Rotor_K_Swiss_Airforce_I extends Rotor
    {
        public Rotor_K_Swiss_Airforce_I(int rotation, int ringSetting)
        {
            super("KSA-I",
                    new Integer[]{15,4,25,20,14,7,23,18,2,21,5,12,19,1,6,11,17,8,13,16,9,22,0,24,3,10},
                    new Integer[]{22,13,8,24,1,10,14,5,17,20,25,15,11,18,4,0,19,16,7,12,3,9,21,6,23,2},
                    new Integer[]{25}, ringSetting, rotation);
        }
    }
    /**
     * Rotor II as used in the Enigma Type K (Swiss, Airforce)
     * ZOUESYDKFWPCIQXHMVBLGNJRAT
     * Turnover F
     */
    private static class Rotor_K_Swiss_Airforce_II extends Rotor
    {
        public Rotor_K_Swiss_Airforce_II(int rotation, int ringSetting)
        {
            super("KSA-II",
                    new Integer[]{25,14,20,4,18,24,3,10,5,22,15,2,8,16,23,7,12,21,1,11,6,13,9,17,0,19},
                    new Integer[]{24,18,11,6,3,8,20,15,12,22,7,19,16,21,1,10,13,23,4,25,2,17,9,14,5,0},
                    new Integer[]{5}, ringSetting, rotation);
        }
    }
    /**
     * Rotor III as used in the Enigma Type K (Swiss, Airforce)
     * EHRVXGAOBQUSIMZFLYNWKTPDJC
     * Turnover O
     */
    private static class Rotor_K_Swiss_Airforce_III extends Rotor
    {
        public Rotor_K_Swiss_Airforce_III(int rotation, int ringSetting)
        {
            super("KSA-III",
                    new Integer[]{4,7,17,21,23,6,0,14,1,16,20,18,8,12,25,5,11,24,13,22,10,19,15,3,9,2},
                    new Integer[]{6,8,25,23,0,15,5,1,12,24,20,16,13,18,7,22,9,2,11,21,10,3,19,4,17,14},
                    new Integer[]{14}, ringSetting, rotation);
        }
    }

    /**
     * Rotor I as used in the Enigma Type R (Rocket)
     * JGDQOXUSCAMIFRVTPNEWKBLZYH
     * Turnover O
     */
    private static class Rotor_R_I extends Rotor
    {
        public Rotor_R_I(int rotation, int ringSetting)
        {
            super("R-I",
                    new Integer[]{9,6,3,16,14,23,20,18,2,0,12,8,5,17,21,19,15,13,4,22,10,1,11,25,24,7},
                    new Integer[]{9,21,8,2,18,12,1,25,11,0,20,22,10,17,4,16,3,13,7,15,6,14,19,5,24,23},
                    new Integer[]{14}, ringSetting, rotation);
        }
    }

    /**
     * Rotor II as used in the Enigma Type R (Rocket)
     * NTZPSFBOKMWRCJDIVLAEYUXHGQ
     * Turnover F
     */
    private static class Rotor_R_II extends Rotor
    {
        public Rotor_R_II(int rotation, int ringSetting)
        {
            super("R-II",
                    new Integer[]{13,19,25,15,18,5,1,14,10,12,22,17,2,9,3,8,21,11,0,4,24,20,23,7,6,16},
                    new Integer[]{18,6,12,14,19,5,24,23,15,13,8,17,9,0,7,3,25,11,4,1,21,16,10,22,20,2},
                    new Integer[]{5}, ringSetting, rotation);
        }
    }

    /**
     * Rotor III as used in the Enigma Type R (Rocket)
     * JVIUBHTCDYAKEQZPOSGXNRMWFL
     * Turnover Z
     */
    private static class Rotor_R_III extends Rotor
    {
        public Rotor_R_III(int rotation, int ringSetting)
        {
            super("R-III",
                    new Integer[]{9,21,8,20,1,7,19,2,3,24,0,10,4,16,25,15,14,18,6,23,13,17,12,22,5,11},
                    new Integer[]{10,4,7,8,12,24,18,5,2,0,11,25,22,20,16,15,13,21,17,6,3,1,23,19,9,14},
                    new Integer[]{25}, ringSetting, rotation);
        }
    }
}
