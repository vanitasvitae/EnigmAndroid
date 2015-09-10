
/**
 * Used to create wiring arrays from strings
 * Use strings like "E K M F L G D Q V Z N T O W Y H X U S P A I B R C J" as input
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
public class RotorMaker
{
    public static final int l = 26;
    public static void main(String[] args)
    {
        if(args.length == l) makeRotor(args);
        else if(args.length == 1)
        {
            if(args[0].length() > 2)
                makeRotor(prepare(args[0]));
            else intToChar(Integer.valueOf(args[0]));
        }
        else System.out.println("wrong input format!");
    }

    public static char intToChar(int x)
    {
        char o = (char) (x+65);
        System.out.println(o);
        return o;
    }

    public static int charToNumber(char x)
    {
        x = Character.toUpperCase(x);
        int i = (int) x;
        i = i-65;
        System.out.println(i);
        return i;
    }
    public static void makeRotor(String input)
    {
        makeRotor(prepare(input));
    }

    /**
     * Prepare the string (add spaces)
     * @param in input string
     * @return prepared string
     */
    public static String[] prepare(String in)
    {
        String[] out = new String[l];
        int pos = 0;
        for(char x : in.toCharArray())
        {
            if(x != ' ')
            {
                try
                {
                    out[pos] = ""+x;
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    System.out.println("String too long!");
                    return null;
                }
                pos++;
            }
        }
        if(pos!=l)
        {
            System.out.println("String too short!");
            return null;
        }
        return out;
    }

    /**
     * Generate Array initializer for the given rotor configuration
     * @param input String describing the rotor
     */
    public static void makeRotor(String[] input)
    {
        if(input.length != l) System.out.println("Wrong length! Input must have length "+l+"!");
        else
        {
            Integer[] out1 = new Integer[l];
            for(int i=0; i<l; i++)
            {
                String x = input[i].toUpperCase();
                char xc = x.charAt(0);
                int xint = (int) xc;
                xint = xint - 65;
                out1[i] = xint;
            }
            Integer[] out2 = new Integer[l];
            for(int i=0; i<l; i++)
            {
                out2[out1[i]] = i;
            }
            String output1 = "{";
            String output2 = "{";
            for(int i=0; i<l; i++)
            {
                output1 = output1 + out1[i]+",";
                output2 = output2 + out2[i]+",";
            }
            output1 = output1.substring(0,output1.length()-1) + "}";
            output2 = output2.substring(0,output2.length()-1) + "}";
            System.out.println("connections :" + output1);
            System.out.println("revConnects :" + output2);
        }
    }
}
