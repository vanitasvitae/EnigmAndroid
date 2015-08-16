package de.vanitasvitae.enigmandroid.rotors;

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
    protected String name;
    protected int type;
    protected Integer[] connections;
    protected Integer[] reversedConnections;
    protected int turnOverNotch;

    protected int ringSetting;
    protected int rotation;

    /**
     * This constructor is not accessible from outside this class file.
     * Use one of the createRotor* factory methods instead to create concrete Rotors.
     * Note that connections and reversedConnections MUST be of the same size and that
     * neither connections nor reversedConnections respectively MUST have any number between
     * 0 and connections.length-1 only once (ie they represent permutations)
     * @param name phonetic name of the rotor (usually I,II,...V)
     * @param type type indicator (I -> 1,...,V -> 5)
     * @param connections wiring of the rotor as Integer array
     * @param reversedConnections inverse wiring used to encrypt in the opposite direction
     *                            (connections[reversedConnections[i]] = i
     *                            for all i in 0..getRotorSize()-1.
     * @param turnOverNotch Position of the turnover notch
     * @param ringSetting setting of the ring that holds the letters
     * @param rotation rotation of the rotor
     */
    protected Rotor(String name, int type, Integer[] connections, Integer[] reversedConnections,
                    int turnOverNotch, int ringSetting, int rotation)
    {
        this.name = name;
        this.type = type;
        this.connections = connections;
        this.reversedConnections = reversedConnections;
        this.turnOverNotch = turnOverNotch;
        this.ringSetting = ringSetting;
        this.rotation = rotation;
    }

    /**
     * Factory method that creates a rotor accordingly to the type.
     * Also initialize the rotor with ringSetting and rotation.
     * @param type type indicator (1..5)
     * @param ringSetting setting of the outer ring (0..25)
     * @param rotation rotation of the rotor
     * @return Concrete rotor
     */
    public static Rotor createRotor(int type, int ringSetting, int rotation)
    {
        switch (type)
        {
            case 1: return createRotorI(ringSetting, rotation);
            case 2: return createRotorII(ringSetting, rotation);
            case 3: return createRotorIII(ringSetting, rotation);
            case 4: return createRotorIV(ringSetting, rotation);
            default: return createRotorV(ringSetting, rotation);
        }
    }

    /**
     * Create concrete Rotor of type 1 (I) initialized with ringSetting and rotation
     * @param ringSetting setting of the outer ring
     * @param rotation rotation of the rotor
     * @return RotorI
     */
    public static Rotor createRotorI(int ringSetting, int rotation)
    {
        return new RotorI(ringSetting, rotation);
    }

    /**
     * Create concrete Rotor of type 2 (II) initialized with ringSetting and rotation
     * @param ringSetting setting of the outer ring
     * @param rotation rotation of the rotor
     * @return RotorI
     */
    public static Rotor createRotorII(int ringSetting, int rotation)
    {
        return new RotorII(ringSetting, rotation);
    }

    /**
     * Create concrete Rotor of type 3 (III) initialized with ringSetting and rotation
     * @param ringSetting setting of the outer ring
     * @param rotation rotation of the rotor
     * @return RotorI
     */
    public static Rotor createRotorIII(int ringSetting, int rotation)
    {
        return new RotorIII(ringSetting, rotation);
    }

    /**
     * Create concrete Rotor of type 4 (IV) initialized with ringSetting and rotation
     * @param ringSetting setting of the outer ring
     * @param rotation rotation of the rotor
     * @return RotorI
     */
    public static Rotor createRotorIV(int ringSetting, int rotation)
    {
        return new RotorIV(ringSetting, rotation);
    }

    /**
     * Create concrete Rotor of type 5 (V) initialized with ringSetting and rotation
     * @param ringSetting setting of the outer ring
     * @param rotation rotation of the rotor
     * @return RotorI
     */
    public static Rotor createRotorV(int ringSetting, int rotation)
    {
        return new RotorV(ringSetting, rotation);
    }

    /**
     * Encrypt an input signal via the internal wiring in "forward" direction (using connections)
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
    public int getType()
    {
        return this.type;
    }

    /**
     * Return the current rotation of the rotor.
     * The rotation consists of the actual rotation - the ringSetting
     * @return rotation-ringSetting
     */
    public int getRotation()
    {
        return this.rotation - this.getRingSetting();
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
        return this.rotation == this.turnOverNotch;
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
        return this.rotation == this.getTurnOver() - 1;
    }

    /**
     * Returns the position of the turnover notch
     * @return turnOverNotch
     */
    public int getTurnOver()
    {
        return this.turnOverNotch;
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
     * Returns the phonetic name of the rotor
     * @return name
     */
    public String getName()
    {
        return this.name;
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
        return (input + this.getRotorSize()) % this.getRotorSize();
    }

    /**
     * Concrete implementation of Rotor of type 1 (I)
     */
    private static class RotorI extends Rotor
    {
        public RotorI(int ringSetting, int rotation)
        {
            super("I", 1,
                    new Integer[]{4, 10, 12, 5, 11, 6, 3, 16, 21, 25, 13, 19, 14, 22, 24, 7, 23, 20, 18, 15, 0, 8, 1, 17, 2, 9},
                    new Integer[]{20, 22, 24, 6, 0, 3, 5, 15, 21, 25, 1, 4, 2, 10, 12, 19, 7, 23, 18, 11, 17, 8, 13, 16, 14, 9},
                    17, ringSetting, rotation);
        }
    }

    /**
     * Concrete implementation of Rotor of type 2 (II)
     */
    private static class RotorII extends Rotor
    {
        public RotorII(int ringSetting, int rotation)
        {
            super("II", 2,
                    new Integer[]{0, 9, 3, 10, 18, 8, 17, 20, 23, 1, 11, 7, 22, 19, 12, 2, 16, 6, 25, 13, 15, 24, 5, 21, 14, 4},
                    new Integer[]{0, 9, 15, 2, 25, 22, 17, 11, 5, 1, 3, 10, 14, 19, 24, 20, 16, 6, 4, 13, 7, 23, 12, 8, 21, 18},
                    5, ringSetting, rotation);
        }
    }

    /**
     * Concrete implementation of Rotor of type 3 (III)
     */
    private static class RotorIII extends Rotor
    {
        public RotorIII(int ringSetting, int rotation)
        {
            super("III", 3,
                    new Integer[]{1, 3, 5, 7, 9, 11, 2, 15, 17, 19, 23, 21, 25, 13, 24, 4, 8, 22, 6, 0, 10, 12, 20, 18, 16, 14},
                    new Integer[]{19, 0, 6, 1, 15, 2, 18, 3, 16, 4, 20, 5, 21, 13, 25, 7, 24, 8, 23, 9, 22, 11, 17, 10, 14, 12},
                    22, ringSetting, rotation);
        }
    }

    /**
     * Concrete implementation of Rotor of type 4 (IV)
     */
    private static class RotorIV extends Rotor
    {
        public RotorIV(int ringSetting, int rotation)
        {
            super("IV", 4,
                    new Integer[]{4, 18, 14, 21, 15, 25, 9, 0, 24, 16, 20, 8, 17, 7, 23, 11, 13, 5, 19, 6, 10, 3, 2, 12, 22, 1},
                    new Integer[]{7, 25, 22, 21, 0, 17, 19, 13, 11, 6, 20, 15, 23, 16, 2, 4, 9, 12, 1, 18, 10, 3, 24, 14, 8, 5},
                    10, ringSetting, rotation);
        }
    }

    /**
     * Concrete implementation of Rotor of type 5 (V)
     */
    private static class RotorV extends Rotor
    {
        public RotorV(int ringSetting, int rotation)
        {
            super("V", 5,
                    new Integer[]{21, 25, 1, 17, 6, 8, 19, 24, 20, 15, 18, 3, 13, 7, 11, 23, 0, 22, 12, 9, 16, 14, 5, 4, 2, 10},
                    new Integer[]{16, 2, 24, 11, 23, 22, 4, 13, 5, 19, 25, 14, 18, 12, 21, 9, 20, 3, 10, 6, 8, 0, 17, 15, 7, 1},
                    0, ringSetting, rotation);
        }
    }
}
