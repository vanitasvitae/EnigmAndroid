package de.vanitasvitae.enigmandroid;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Class that represents the settings activity.
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
 *@author vanitasvitae
 */
public class SettingsActivity extends PreferenceActivity
{
    public static final String PREF_NUMERIC_LANGUAGE = "prefNumericLanguage";
    public static final String PREF_MACHINE_TYPE = "prefMachineType";
    public static final String PREF_MESSAGE_FORMATTING = "prefMessageFormatting";
    public static final String PREF_REPLACE_SPECIAL_CHARACTERS = "prefReplaceSpecialCharacters";
	public static final String PREF_SAVED_ENIGMA_STATE = "prefSavedEnigmaState";

	private String previousPrefNumericLanguage;
	private String previousPrefMachineType;
	private String previousPrefMessageFormatting;
	private boolean previousPrefReplaceSpecialCharacters;
	private String previousPrefSavedEnigmaState;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //noinspection deprecation
        addPreferencesFromResource(R.xml.pref_page);
		this.previousPrefMachineType = getPrefMachineType();
		this.previousPrefSavedEnigmaState = getPrefSavedEnigmaState();
		this.previousPrefMessageFormatting = getPrefMessageFormatting();
		this.previousPrefNumericLanguage = getPrefNumericLanguage();
    }

	public String getPrefNumericLanguage()
	{
		return PreferenceManager.getDefaultSharedPreferences(this).getString(
				PREF_NUMERIC_LANGUAGE,
				getResources().getStringArray(R.array.pref_alias_message_formatting)[0]);
	}

	public void setPrefNumericLanguage(String lang)
	{
		PreferenceManager.getDefaultSharedPreferences(this).edit()
				.putString(PREF_NUMERIC_LANGUAGE, lang).apply();
	}

	public boolean prefNumericLanguageChanged()
	{
		if(this.previousPrefNumericLanguage == null || !this.previousPrefNumericLanguage.equals(getPrefNumericLanguage()))
		{
			this.previousPrefNumericLanguage = this.getPrefNumericLanguage();
			return true;
		}
		return false;
	}

	public boolean getPrefReplaceSpecialCharacters()
	{
		return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				PREF_REPLACE_SPECIAL_CHARACTERS, true);
	}

	public void setPrefReplaceSpecialCharacters(boolean replace)
	{
		PreferenceManager.getDefaultSharedPreferences(this).edit()
				.putBoolean(PREF_REPLACE_SPECIAL_CHARACTERS, replace).apply();
	}

	public boolean prefReplaceSpecialCharactersChanged()
	{
		boolean changed = previousPrefReplaceSpecialCharacters != getPrefReplaceSpecialCharacters();
		if(changed)
		{
			previousPrefReplaceSpecialCharacters = getPrefReplaceSpecialCharacters();
			return true;
		}
		return false;
	}

    public String getPrefMachineType()
    {
        return PreferenceManager.getDefaultSharedPreferences(this).getString(
                PREF_MACHINE_TYPE,
                getResources().getStringArray(R.array.pref_alias_machine_type)[0]);
    }

	public void setPrefMachineType(String pref)
	{
		PreferenceManager.getDefaultSharedPreferences(this).edit()
				.putString(PREF_MACHINE_TYPE, pref).apply();
	}

	public boolean prefMachineTypeChanged()
	{
		if(this.previousPrefMachineType == null || !this.previousPrefMachineType.equals(getPrefMachineType()))
		{
			this.previousPrefMachineType = this.getPrefMachineType();
			return true;
		}
		return false;
	}

	public String getPrefSavedEnigmaState()
	{
		return PreferenceManager.getDefaultSharedPreferences(this)
				.getString(PREF_SAVED_ENIGMA_STATE, "-1");
	}

	/**
	 * @param state HEX
	 */
	public void setPrefSavedEnigmaState(String state)
	{
		PreferenceManager.getDefaultSharedPreferences(this).edit()
				.putString(PREF_SAVED_ENIGMA_STATE, state).apply();
	}

	public boolean prefSavedEnigmaStateChanged()
	{
		if(this.previousPrefSavedEnigmaState == null || !this.previousPrefSavedEnigmaState
				.equals(getPrefSavedEnigmaState()))
		{
			this.previousPrefSavedEnigmaState = this.getPrefSavedEnigmaState();
			return true;
		}
		return false;
	}

	public String getPrefMessageFormatting()
	{
		return PreferenceManager.getDefaultSharedPreferences(this)
				.getString(SettingsActivity.PREF_MESSAGE_FORMATTING, getResources().
				getStringArray(R.array.pref_alias_message_formatting)[0]);
	}

	public void setPrefMessageFormatting(String format)
	{
		PreferenceManager.getDefaultSharedPreferences(this).edit()
				.putString(PREF_MESSAGE_FORMATTING, format).apply();
	}

	public boolean prefMessageFormattingChanged()
	{
		if(this.previousPrefMessageFormatting == null || !this.previousPrefMessageFormatting
				.equals(getPrefMessageFormatting()))
		{
			this.previousPrefMessageFormatting = this.getPrefMessageFormatting();
			return true;
		}
		return false;
	}

	public static class SettingsSingleton extends SettingsActivity
	{
		private static SettingsActivity instance;
		private SettingsSingleton()
		{
			super();
		}

		public static SettingsActivity getInstance()
		{
			if(instance == null) instance = new SettingsActivity();
			return instance;
		}
	}
}
