package de.vanitasvitae.enigmandroid.enigma.inputPreparer;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import de.vanitasvitae.enigmandroid.MainActivity;
import de.vanitasvitae.enigmandroid.R;
import de.vanitasvitae.enigmandroid.SettingsActivity;

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
public abstract class InputPreparer {
    InputPreparer child;

    public String prepareString(String in) {
        if (child != null)
            return child.prepareString(this.prepare(in));
        else
            return prepare(in);
    }

    protected abstract String prepare(String input);

    public static InputPreparer createInputPreparer()
    {
        MainActivity main = (MainActivity) MainActivity.ActivitySingleton.getInstance().getActivity();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(main);

        boolean replaceSpecialChars = sharedPreferences.getBoolean(SettingsActivity.PREF_REPLACE_SPECIAL_CHARACTERS, true);
        String num_lang = sharedPreferences.getString(SettingsActivity.PREF_NUMERIC_LANGUAGE, main.getResources().
                getStringArray(R.array.pref_alias_numeric_spelling_language)[0]);
        InputPreparer inPrep = new RemoveIllegalCharacters();
        if(replaceSpecialChars) inPrep = new ReplaceSpecialCharacters(inPrep);
        switch (num_lang)
        {
            case "de": inPrep = new ReplaceNumbersGerman(inPrep);
                break;
            case "en": inPrep = new ReplaceNumbersEnglish(inPrep);
                break;
            case "fr": inPrep = new ReplaceNumbersFrench(inPrep);
                break;
			case "sp": inPrep = new ReplaceNumbersSpanish(inPrep);
				break;
			case "it": inPrep = new ReplaceNumbersItalian(inPrep);
				break;
            default:
                break;
        }
        return inPrep;
    }

    public static class ReplaceSpecialCharacters extends InputPreparer {
        public ReplaceSpecialCharacters() {
            this.child = null;
        }

        public ReplaceSpecialCharacters(InputPreparer child) {
            this.child = child;
        }

        protected String prepare(String input) {
            input = input.toUpperCase();
            return input.replace("Ä", "AE").replace("Ö", "OE").replace("Ü", "UE").replace("ß", "SS").replace(" ","");
        }
    }

    /**
     * Concrete implementation of a german InputPreparer
     */
    public static class ReplaceNumbersGerman extends InputPreparer {
        public ReplaceNumbersGerman() {
            this.child = null;
        }

        public ReplaceNumbersGerman(InputPreparer child) {
            this.child = child;
        }

        protected String prepare(String input) {
            input = input.replace("0", "NULL")
                    .replace("1", "EINS")
                    .replace("2", "ZWEI")
                    .replace("3", "DREI")
                    .replace("4", "VIER")
                    .replace("5", "FUENF")
                    .replace("6", "SECHS")
                    .replace("7", "SIEBEN")
                    .replace("8", "ACHT")
                    .replace("9", "NEUN");
            return input;
        }
    }

    /**
     * Concrete implementation of a french InputPreparer
     */
    public static class ReplaceNumbersFrench extends InputPreparer {
        public ReplaceNumbersFrench() {
            this.child = null;
        }

        public ReplaceNumbersFrench(InputPreparer child) {
            this.child = child;
        }

        protected String prepare(String input) {
            input = input.replace("0", "ZERO")
                    .replace("1", "UN")
                    .replace("2", "DEUX")
                    .replace("3", "TROIS")
                    .replace("4", "QUATRE")
                    .replace("5", "CINQ")
                    .replace("6", "SIX")
                    .replace("7", "SEPT")
                    .replace("8", "HUIT")
                    .replace("9", "NEUF");
            return input;
        }
    }

    /**
     * Concrete implementation of an english InputPreparer
     */
    public static class ReplaceNumbersEnglish extends InputPreparer {
        public ReplaceNumbersEnglish() {
            this.child = null;
        }

        public ReplaceNumbersEnglish(InputPreparer child) {
            this.child = child;
        }

        protected String prepare(String input) {
            input = input.replace("0", "ZERO")
                    .replace("1", "ONE")
                    .replace("2", "TWO")
                    .replace("3", "THREE")
                    .replace("4", "FOUR")
                    .replace("5", "FIVE")
                    .replace("6", "SIX")
                    .replace("7", "SEVEN")
                    .replace("8", "EIGHT")
                    .replace("9", "NINE");
            return input;
        }
    }

    /**
     * Concrete implementation of a spanish InputPreparer
     */
    public static class ReplaceNumbersSpanish extends InputPreparer {
        public ReplaceNumbersSpanish() {
            this.child = null;
        }

        public ReplaceNumbersSpanish(InputPreparer child) {
            this.child = child;
        }

        protected String prepare(String input) {
            input = input.replace("0", "CERO")
                    .replace("1", "UNO")
                    .replace("2", "DOS")
                    .replace("3", "TRES")
                    .replace("4", "CUATRO")
                    .replace("5", "CINCO")
                    .replace("6", "SEIS")
                    .replace("7", "SIETE")
                    .replace("8", "OCHO")
                    .replace("9", "NUEVE");
            return input;
        }
    }

	/**
	 * Concrete implementation of a spanish InputPreparer
	 */
	public static class ReplaceNumbersItalian extends InputPreparer {
		public ReplaceNumbersItalian() {
			this.child = null;
		}

		public ReplaceNumbersItalian(InputPreparer child) {
			this.child = child;
		}

		protected String prepare(String input) {
			input = input.replace("0", "ZERO")
					.replace("1", "UNO")
					.replace("2", "DUE")
					.replace("3", "TRE")
					.replace("4", "QUATTRO")
					.replace("5", "CINQUE")
					.replace("6", "SEI")
					.replace("7", "SETTE")
					.replace("8", "OTTO")
					.replace("9", "NOVE");
			return input;
		}
	}

    /**
     * "Final Stage" of Input preparing. This should always be called last
     * (choose this as the inner most capsule of InputPreparers)
     * This cant have child InputPreparers.
     * This Input preparer removes all characters from the string besides A..Z
     */
    public static class RemoveIllegalCharacters extends InputPreparer
    {
        public RemoveIllegalCharacters()
        {
            this.child = null;
        }

        protected String prepare(String in)
        {
            String out = "";
            for(char c : in.toUpperCase().toCharArray())
            {
                if(c>=65 && c<=90) out = out+c;
            }
            return out;
        }
    }
}