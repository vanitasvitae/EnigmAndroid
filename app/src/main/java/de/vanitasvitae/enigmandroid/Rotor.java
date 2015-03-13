package de.vanitasvitae.enigmandroid;

/**
 * Class representing a rotor of the Enigma machine (I-V,A-C)
 *Copyright (C) 2015  Paul Schaub

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
 * @author vanitas
 */
public class Rotor
{
    //	                    0	1	2	3	4	5	6	7	8	9	10	11	12	13	14	15	16	17	18	19	20	21	22	23	24	25
    //ABC:		            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
    //Rotor1:	            'E','K','M','F','L','G','D','Q','V','Z','N','T','O','W','Y','H','X','U','S','P','A','I','B','R','C','J'
    //Rotor2:	            'A','J','D','K','S','I','R','U','X','B','L','H','W','T','M','C','Q','G','Z','N','P','Y','F','V','O','E'
    //Rotor3:	            'B','D','F','H','J','L','C','P','R','T','X','V','Z','N','Y','E','I','W','G','A','K','M','U','S','Q','O'
    //Rotor4:	            'E','S','O','V','P','Z','J','A','Y','Q','U','I','R','H','X','L','N','F','T','G','K','D','C','M','W','B'
    //Rotor5:	            'V','Z','B','R','G','I','T','Y','U','P','S','D','N','H','L','X','A','W','M','J','Q','O','F','E','C','K'
    //Reversing Rotor A:    AE  BJ  CM  DZ  FL  GY  HX  IV  KW  NR  OQ  PU  ST
    //Reversing Rotor B:	AY  BR  CU  DH  EQ  FS  GL  IP  JX  KN  MO  TZ  VW
    //Reversing Rotor C:	AF  BV  CP  DJ  EI  GO  HY  KR  LZ  MX  NW  QT  SU

    //Original rotors
    public static final Integer[] rotor1 = {4, 10, 12, 5, 11, 6, 3, 16, 21, 25, 13, 19, 14, 22, 24, 7, 23, 20, 18, 15, 0, 8, 1, 17, 2, 9};
    public static final Integer[] rotor2 = {0, 9, 3, 10, 18, 8, 17, 20, 23, 1, 11, 7, 22, 19, 12, 2, 16, 6, 25, 13, 15, 24, 5, 21, 14, 4};
    public static final Integer[] rotor3 = {1, 3, 5, 7, 9, 11, 2, 15, 17, 19, 23, 21, 25, 13, 24, 4, 8, 22, 6, 0, 10, 12, 20, 18, 16, 14};
    public static final Integer[] rotor4 = {4, 18, 14, 21, 15, 25, 9, 0, 24, 16, 20, 8, 17, 7, 23, 11, 13, 5, 19, 6, 10, 3, 2, 12, 22, 1};
    public static final Integer[] rotor5 = {21, 25, 1, 17, 6, 8, 19, 24, 20, 15, 18, 3, 13, 7, 11, 23, 0, 22, 12, 9, 16, 14, 5, 4, 2, 10};
    //Original rotors backwards direction
    public static final Integer[] backwardsRotor1 = {20, 22, 24, 6, 0, 3, 5, 15, 21, 25, 1, 4, 2, 10, 12, 19, 7, 23, 18, 11, 17, 8, 13, 16, 14, 9};
    public static final Integer[] backwardsRotor2 = {0, 9, 15, 2, 25, 22, 17, 11, 5, 1, 3, 10, 14, 19, 24, 20, 16, 6, 4, 13, 7, 23, 12, 8, 21, 18};
    public static final Integer[] backwardsRotor3 = {19, 0, 6, 1, 15, 2, 18, 3, 16, 4, 20, 5, 21, 13, 25, 7, 24, 8, 23, 9, 22, 11, 17, 10, 14, 12};
    public static final Integer[] backwardsRotor4 = {7, 25, 22, 21, 0, 17, 19, 13, 11, 6, 20, 15, 23, 16, 2, 4, 9, 12, 1, 18, 10, 3, 24, 14, 8, 5};
    public static final Integer[] backwardsRotor5 = {16, 2, 24, 11, 23, 22, 4, 13, 5, 19, 25, 14, 18, 12, 21, 9, 20, 3, 10, 6, 8, 0, 17, 15, 7, 1};

    //Original reflector rotors					  	A,B,C, D, E,F, G, H, I, J,K, L,M,N, O, P, Q, R, S, T, U, V,W, X,Y,Z
    public static final Integer[] reflectorA = {4, 9, 12, 25, 0, 11, 24, 23, 21, 1, 22, 5, 2, 17, 16, 20, 14, 13, 19, 18, 15, 8, 10, 7, 6, 3};
    public static final Integer[] reflectorB = {24, 17, 20, 7, 16, 18, 11, 3, 15, 23, 13, 6, 14, 10, 12, 8, 4, 1, 5, 25, 2, 22, 21, 9, 0, 19};
    public static final Integer[] reflectorC = {5, 21, 15, 9, 8, 0, 14, 24, 4, 3, 17, 25, 23, 22, 6, 2, 19, 10, 20, 16, 18, 1, 13, 12, 7, 11};

    //Attributes of the rotor
    private Integer[] rotor;        //choose one of rotor1-5
    private Integer[] backwardsRotor;       //choose one of backwardsRotor1-5
    private int ringsetting;
    private int turnOver;
    private int counter;
    private String name;
    private int type;

    /**
     * Create new Rotor (1 - 5 = normal rotor, -1 - -3 = reversing rotor)
     *
     * @param type    integer determines type
     * @param setting setting (rotation) of the rotor
     */
    public Rotor(char type, int setting, int ring)
    {
        this.ringsetting = ring;
        switch (type)
        {
            case '1':
            {
                this.rotor = rotor1;
                this.backwardsRotor = backwardsRotor1;
                this.counter = setting;
                this.name = "I";
                this.turnOver = 17; //"Royal"
                this.type = 1;
                break;
            }
            case '2':
            {
                this.rotor = rotor2;
                this.backwardsRotor = backwardsRotor2;
                this.counter = setting;
                this.name = "II";
                this.turnOver = 5; //"Flags"
                this.type = 2;
                break;
            }
            case '3':
            {
                this.rotor = rotor3;
                this.backwardsRotor = backwardsRotor3;
                this.counter = setting;
                this.name = "III";
                this.turnOver = 22; //"Wave"
                this.type = 3;
                break;
            }
            case '4':
            {
                this.rotor = rotor4;
                this.backwardsRotor = backwardsRotor4;
                this.counter = setting;
                this.name = "IV";
                this.turnOver = 10; //"Kings"
                this.type = 4;
                break;
            }
            case '5':
            {
                this.rotor = rotor5;
                this.backwardsRotor = backwardsRotor5;
                this.counter = setting;
                this.name = "V";
                this.turnOver = 0; //"Above"
                this.type = 5;
                break;
            }
            case 'A':
            {
                this.rotor = reflectorA;
                this.backwardsRotor = null;
                this.counter = 0;
                this.name = "A";
                this.type = 1;
                break;
            }
            case 'B':
            {
                this.rotor = reflectorB;
                this.backwardsRotor = null;
                this.counter = 0;
                this.name = "B";
                this.type = 2;
                break;
            }
            case 'C':
            {
                this.rotor = reflectorC;
                this.backwardsRotor = null;
                this.counter = 0;
                this.name = "C";
                this.type = 3;
                break;
            }
        }
    }

    /**
     * encrypt in forward direction (forward means first half of the cycle through the rotors)
     *
     * @param x incoming character
     * @return encrypted character
     */
    public int encryptForward(int x)
    {
        return this.rotor[(26 + x) % 26];
    }

    /**
     * encrypt in backward direction (coming from the reversing rotor)
     *
     * @param x incoming character
     * @return encrypted character
     */
    public int encryptBackward(int x)
    {
        if (this.backwardsRotor == null) return this.rotor[(26 + x) % 26];
        else return this.backwardsRotor[(26 + x) % 26];
    }

    /**
     * return rotation of the rotor - the ringsetting
     *
     * @return rotation
     */
    public int getCounter()
    {
        return this.counter - this.getRingsetting();
    }

    /**
     * increment rotation of the rotor by one.
     */
    public void incrementCounter()
    {
        this.counter = (this.counter + 1) % 26;
    }

    /**
     * Return true, if rotor is at a position, where it turns over the next rotor
     *
     * @return boolean
     */
    public boolean isAtTurnoverPosition()
    {
        return this.counter == this.turnOver;
    }

    /**
     * The Double Turn Anomaly (deutsch: Doppelsprung Anomalie) is aa anomaly in the rotor movement caused by the mechanical implementation of the enigma.
     * Whenever the rightmost rotor turns the middle rotor AND the middle rotor is only one move from turning the leftmost rotor, the middle rotor turns again with the next character.
     * So technically there are only 26*25*26 possible rotor settings for any but firmly 3 rotors.
     *
     * @return boolean
     */
    public boolean doubleTurnAnomaly()
    {
        return this.counter == this.turnOver - 1;
    }

    @SuppressWarnings("unused")
    /**
     * Returns the position of the turnover notch
     *
     * @return turnOver
     */
    public int getTurnOver()
    {
        return this.turnOver;
    }

    @SuppressWarnings("unused")
    /**
     * Return the name of the rotor
     * @return name
     */
    public String getName()
    {
        return name;
    }

    @SuppressWarnings("unused")
    /**
     * Return ringsettings of the rotor
     * @return ringsetting
     */
    public int getRingsetting()
    {
        return this.ringsetting;
    }

    /**
     * Returns the integer, which was used to create this rotor.
     *
     * @return the type of the rotor
     */
    public int getType()
    {
        return this.type;
    }
}
