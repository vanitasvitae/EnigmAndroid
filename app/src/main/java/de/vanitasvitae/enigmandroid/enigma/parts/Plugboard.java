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
import java.util.Random;

import de.vanitasvitae.enigmandroid.MainActivity;
import de.vanitasvitae.enigmandroid.enigma.Enigma;
import de.vanitasvitae.enigmandroid.enigma.inputPreparer.InputPreparer;

/**
 * Plugboard of the enigma
 * Copyright (C) 2015  Paul Schaub
 */
public class Plugboard
{
    private static final int[] empty = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};
    private int[] plugs;

    public Plugboard()
    {
        plugs = Arrays.copyOf(empty, empty.length);
    }

    public Plugboard(int[] conf)
    {
        this.plugs = conf;
    }

    public void setConfiguration(int[] conf)
    {
        this.plugs = conf;
    }

    @SuppressWarnings("UnusedReturnValue")
    public BigInteger setConfiguration(BigInteger b)
    {
        String s = "";

        int x;
        while((x = Enigma.getValue(b, 27)) != 26 && b.compareTo(BigInteger.ZERO) > 1)
        {
            s = ((char) (x+65))+s;
            b = Enigma.removeDigit(b, 27);
        }
        this.setConfiguration(stringToConfiguration(s));
        return b;
    }

    public int[] getConfiguration()
    {
        return this.plugs;
    }

    /**
     * Encrypt input via plugboard connections
     * @param input input symbol to encryptString
     * @return encrypted symbol
     */
    public int encrypt(int input)
    {
        return plugs[(input+plugs.length)%plugs.length];
    }

    /**
     * Interpret a String of Pairs as a configuration for the plugboard
     * Any char with an even index is connected to the successor.
     * in must not contain duplicates!
     * @param in String representation of pairs (eg. AXBHCS...)
     * @return connections as int[]
     */
    public static int[] stringToConfiguration(String in)
    {
        String pairs = trimString(new InputPreparer.RemoveIllegalCharacters().prepareString(in));
        int[] out = Arrays.copyOf(empty, empty.length);
        //Check if in is too long or odd
        int l = pairs.length();
        if(l>1 && (pairs.length() > 26 || pairs.length()/2 == (pairs.length()-1)/2))
        {
            //Odd length. remove last char. Information loss!
            pairs = pairs.substring(0,pairs.length()-1);
        }
        //Modify out
        for(int i=0; i<pairs.length()/2; i++)
        {
            int a = (int) (pairs.toCharArray()[i*2])-65;
            int b = (int) (pairs.toCharArray()[(i*2)+1])-65;
            out[a] = b;
            out[b] = a;
        }
        return out;
    }

    /**
     * Remove all duplicate chars x from the String except the first appearance of x.
     * String MUST be in upper case!
     * @param in String
     * @return String
     */
    private static String trimString(String in)
    {
        in = in.toUpperCase();
        for(int i=0; i<26; i++)
        {
            char x = (char)(i+65);
            int index = in.indexOf(""+x);
            if(index != in.lastIndexOf(""+x))
            {
                //Remove all duplicates of x
                in = in.substring(0, index) + in.substring(index+1).replace(""+x,"");
            }
        }
        return in;
    }

    /**
     * Generate a configuration from a seeded PRNG.
     * Not all connections have to be done.
     * @param rand Seeded PRNG
     * @return configuration
     */
    public static int[] seedToPlugboardConfiguration(Random rand)
    {
        int connectionCount = rand.nextInt(14); //0..13
        int[] out = empty;
        for(int i=0; i<connectionCount; i++)
        {
            int rA = rand.nextInt(26);
            while(out[rA] != rA) rA = (rA+1)%26;
            int rB = rand.nextInt(26);
            while(out[rB] != rB) rB = (rB+1)%26;
            out[rA] = rB;
            out[rB] = rA;
        }
        return out;
    }

    /**
     * Generate a configuration with full set of connections from a seeded PRNG
     * @param rand Seeded PRNG
     * @return configuration
     */
    public static int[] seedToReflectorConfiguration(Random rand)
    {
        int[] out = Arrays.copyOf(empty,empty.length);
        for (int i=0; i<out.length/2; i++) {
            int rA = rand.nextInt(26);
            while (out[rA] != rA) rA = (rA + 27) % 26;
            int rB = rand.nextInt(26);
            while (out[rB] != rB || rA == rB) rB = (rB + 27) % 26;
            out[rA] = rB;
            out[rB] = rA;
        }
        return out;
    }

    /**
     * Converts a configurations array back to a String representation
     * @param c array
     * @return String representation
     */
    private static String configurationToString(int[] c)
    {
        String out = "";
        for(int i=0; i<c.length; i++) // c.length = 26 (mostly)
        {
            if(c[i] != i && out.indexOf((char)(c[i])+65)==-1)
            {
                out += (char) (i+65);
                out += (char) (c[i]+65);
            }
        }
        return out;
    }

    public static BigInteger configurationToBigInteger(int[] a)
    {
        String s = configurationToString(a);
        BigInteger l = BigInteger.ZERO;
        l = Enigma.addDigit(l,26,27);
        for(char c : s.toCharArray())
        {
            int i = (int) (c);
            i-=65;
            l = Enigma.addDigit(l,i,27);
        }
        Log.d(MainActivity.APP_ID, "Save configuration plugs: "+l.toString());
        return l;
    }

    public static int[] bigIntegerToConfiguration(BigInteger b)
    {
        String s = "";
        int x;
        while((x = Enigma.getValue(b, 27)) != 26 && b.compareTo(BigInteger.ZERO) > 1)
        {
            s = ((char) (x+65))+s;
            b = Enigma.removeDigit(b, 27);
        }
        Log.d(MainActivity.APP_ID, "Restored: "+s);
        return stringToConfiguration(s);
    }
}
