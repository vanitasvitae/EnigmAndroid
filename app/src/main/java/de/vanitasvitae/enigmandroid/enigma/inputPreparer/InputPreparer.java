package de.vanitasvitae.enigmandroid.enigma.inputPreparer;

/**
 * Preparer class that prepares input text to only consist of [A..Z]
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
public abstract class InputPreparer
{
    /**
     * Prepare the input String in a way that it only contains letters from [A..Z].
     * Replace special characters, remove spaces and spell numbers.
     * @param input String
     * @return prepared String
     */
    public String prepareString(String input)
    {
        input = input.toUpperCase();
        input = input.replace("Ä","AE").replace("Ö","OE").replace("Ü","UE").replace("ß","SS");
        String output = "";

        for (char x : input.toCharArray())
        {
            if (x >= 65 && x <= 90)         //x in [A..Z]
            {
                output = output + x;
            }
            else if (x >= 48 && x <= 57)    //x in [0..9]
            {
                output = output + replaceNumber(x);
            }
            //x is special symbol
            else if (x != ' ')
            {
                output = output + 'X';
            }
        }
        return output;
    }

    /**
     * Abstract method that spells numbers in a certain language specified by the implementation
     * @param input character
     * @return spelled number
     */
    public abstract String replaceNumber(char input);

    /**
     * Factory method that creates a specific InputPreparer
     * @param language language alias that specifies the language (de,fr,en)
     * @return concrete InputPreparer
     */
    public static InputPreparer createInputPreparer(String language)
    {
        switch (language)
        {
            case "de": return new InputPreparerGerman();
            case "fr": return new InputPreparerFrench();
            default: return new InputPreparerEnglish();
        }
    }
}

/**
 * Concrete implementation of a german InputPreparer
 */
class InputPreparerGerman extends InputPreparer
{
    @Override
    public String replaceNumber(char input) {
        switch (input)
        {
            case '0': return "NULL";
            case '1': return "EINS";
            case '2': return "ZWEI";
            case '3': return "DREI";
            case '4': return "VIER";
            case '5': return "FUENF";
            case '6': return "SECHS";
            case '7': return "SIEBEN";
            case '8': return "ACHT";
            default: return "NEUN";
        }
    }
}

/**
 * Concrete implementation of an english InputPreparer
 */
class InputPreparerEnglish extends InputPreparer
{
    @Override
    public String replaceNumber(char input)
    {
        switch (input) {
            case '0': return "ZERO";
            case '1': return "ONE";
            case '2': return "TWO";
            case '3': return "THREE";
            case '4': return "FOUR";
            case '5': return "FIVE";
            case '6': return "SIX";
            case '7': return "SEVEN";
            case '8': return "EIGHT";
            default: return "NINE";
        }
    }
}

/**
 * Concrete implementation of a french InputPreparer
 */
class InputPreparerFrench extends InputPreparer
{

    @Override
    public String replaceNumber(char input)
    {
        switch (input) {
            case '0': return "ZERO";
            case '1': return "UN";
            case '2': return "DEUX";
            case '3': return "TROIS";
            case '4': return "QUATRE";
            case '5': return "CINQ";
            case '6': return "SIX";
            case '7': return "SEPT";
            case '8': return "HUIT";
            default: return "NEUF";
        }
    }
}