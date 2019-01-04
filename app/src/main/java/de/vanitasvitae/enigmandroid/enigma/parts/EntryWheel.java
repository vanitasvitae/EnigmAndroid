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

/**
 * Implementation of several EntryWheels
 * Copyright (C) 2015  Paul Schaub
 */
public class EntryWheel
{
    private final int type;
    private final String name;
    private int index;
    private final String summary;
    private final Integer[] connections;
    private final Integer[] reversedConnections;

    EntryWheel(int type, String name, String summary, Integer[] connections, Integer[] reversedConnections)
    {
        this.type = type;
        this.name = name;
        this.summary = summary;
        this.connections = connections;
        this.reversedConnections = reversedConnections;
    }

    /**
     * Returns a new EntryWheel of the same type as the callee
     * @return new EntryWheel
     */
    public EntryWheel getInstance()
    {
        return createEntryWheel(this.type).setIndex(this.getIndex());
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

    private int normalize(int input)
    {
        return (input+this.connections.length)%this.connections.length;
    }

    public EntryWheel setIndex(int index)
    {
        this.index = index;
        return this;
    }

    public int getIndex()
    {
        return this.index;
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

    private EntryWheel createEntryWheel(int type)
    {
        switch(type)
        {
            case 1:
                return new EntryWheel_QWERTZ();
            case 2:
                return new EntryWheel_T();
            default: return new EntryWheel_ABCDEF();
        }
    }

    public static class EntryWheel_ABCDEF extends EntryWheel
    {
        public EntryWheel_ABCDEF()
        {
            super(0, "ABCDEF", "abcdefghijklmnopqrstuvwxyz",
                    new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25},
                    new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25});
        }
    }

    /**
     * EntryWheel as used in the Enigma models D, K, G
     * Q W E R T Z U I O A S D F G H J K P Y X C V B N M L
     */
    public static class EntryWheel_QWERTZ extends EntryWheel
    {
        public EntryWheel_QWERTZ()
        {
            super(1, "QWERTZ", "qwertzuioasdfghjkpyxcvbnml",
                    new Integer[]{9,22,20,11,2,12,13,14,7,15,16,25,24,23,8,17,0,3,10,4,6,21,1,19,18,5},
                    new Integer[]{16,22,4,17,19,25,20,8,14,0,18,3,5,6,7,9,10,15,24,23,2,21,1,13,12,11});
        }
    }

    /**
     * EntryWheel as used only in the Enigma Type T Tirpitz
     * K Z R O U Q H Y A I G B L W V S T D X F P N M C J E
     */
    public static class EntryWheel_T extends EntryWheel
    {
        public EntryWheel_T()
        {
            super(2, "KZROUQ", "kzrouqhyaigblwvstdxfpnmcje",
                    new Integer[]{8,11,23,17,25,19,10,6,9,24,0,12,22,21,3,20,5,2,15,16,4,14,13,18,7,1},
                    new Integer[]{10,25,17,14,20,16,7,24,0,8,6,1,11,22,21,18,19,3,23,5,15,13,12,2,9,4});
        }
    }
}
