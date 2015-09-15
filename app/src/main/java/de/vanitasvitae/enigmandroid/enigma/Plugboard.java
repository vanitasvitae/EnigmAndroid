package de.vanitasvitae.enigmandroid.enigma;

/**
 * Plugboard of the enigma
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
public class Plugboard
{
    public static final int[] empty = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};
    private int[] plugs;

    public Plugboard()
    {
        plugs = empty;
    }

    public Plugboard(int[] conf)
    {
        this.plugs = conf;
    }

    public void setConfiguration(int[] conf)
    {
        this.plugs = conf;
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
}
