package de.vanitasvitae.enigmandroid;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

/**
 * Class that represents the settings activity.
 * Use the singleton to get an instance of this class to get preferences
 * Copyright (C) 2015  Paul Schaub
 * <p>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * @author vanitasvitae
 */
public class SettingsActivity extends PreferenceActivity
{
	public static final String PREF_NUMERIC_LANGUAGE = "prefNumericLanguage";
	public static final String PREF_MACHINE_TYPE = "prefMachineType";
	public static final String PREF_MESSAGE_FORMATTING = "prefMessageFormatting";
	public static final String PREF_REPLACE_SPECIAL_CHARACTERS = "prefReplaceSpecialCharacters";
	public static final String PREF_SAVED_ENIGMA_STATE = "prefSavedEnigmaState";
	public static final String PREF_VERSION_NUMBER = "prefVersionNumber";

	private String previousPrefNumericLanguage;
	private String previousPrefMachineType;
	private String previousPrefMessageFormatting;
	private boolean previousPrefReplaceSpecialCharacters;
	private String previousPrefSavedEnigmaState;

	private SharedPreferences prefs;
	private Resources res;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//noinspection deprecation
		addPreferencesFromResource(R.xml.pref_page);
	}

	public void setSharedPreferences(SharedPreferences prefs)
	{
		this.prefs = prefs;
	}

	public void setResources(Resources res)
	{
		this.res = res;
	}

	private boolean isFullyInitialized()
	{
		if (prefs == null)
		{
			Log.e(MainActivity.APP_ID,
				  "SharedPreferences not initialized via setSharedPreferences!");
			return false;
		}
		if (res == null)
		{
			Log.e(MainActivity.APP_ID, "Resources not initialized via setResources!");
			return false;
		}
		return true;
	}

	public String getPrefNumericLanguage()
	{
		if (isFullyInitialized())
			return prefs.getString(
					PREF_NUMERIC_LANGUAGE,
					res.getStringArray(R.array.pref_alias_message_formatting)[0]);
		else return null;
	}

	public void setPrefNumericLanguage(String lang)
	{
		if (isFullyInitialized())
			prefs.edit().putString(PREF_NUMERIC_LANGUAGE, lang).apply();
	}

	public boolean prefNumericLanguageChanged()
	{
		if (this.previousPrefNumericLanguage == null || !this.previousPrefNumericLanguage.equals(
				getPrefNumericLanguage()))
		{
			this.previousPrefNumericLanguage = this.getPrefNumericLanguage();
			Log.d(MainActivity.APP_ID, PREF_NUMERIC_LANGUAGE+" changed!");
			return true;
		}
		return false;
	}

	/**
	 * Return whether special characters will be replaced.
	 * If the SettingsActivity is not fully initialized return false and ignore preference.
	 * @return boolean
	 */
	public boolean getPrefReplaceSpecialCharacters()
	{
		return isFullyInitialized() && prefs.getBoolean(PREF_REPLACE_SPECIAL_CHARACTERS, true);
	}

	public void setPrefReplaceSpecialCharacters(boolean replace)
	{
		if (isFullyInitialized())
			prefs.edit().putBoolean(PREF_REPLACE_SPECIAL_CHARACTERS, replace).apply();
	}

	public boolean prefReplaceSpecialCharactersChanged()
	{
		boolean changed = previousPrefReplaceSpecialCharacters != getPrefReplaceSpecialCharacters();
		if (changed)
		{
			previousPrefReplaceSpecialCharacters = getPrefReplaceSpecialCharacters();
			Log.d(MainActivity.APP_ID, PREF_REPLACE_SPECIAL_CHARACTERS+" changed!");
			return true;
		}
		return false;
	}

	public String getPrefMachineType()
	{
		if (isFullyInitialized())
			return prefs.getString(PREF_MACHINE_TYPE,
								   res.getStringArray(R.array.pref_alias_machine_type)[0]);
		else return null;
	}

	public void setPrefMachineType(String pref)
	{
		if (isFullyInitialized())
			prefs.edit().putString(PREF_MACHINE_TYPE, pref).apply();
	}

	public boolean prefMachineTypeChanged()
	{
		if (this.previousPrefMachineType == null || !this.previousPrefMachineType.equals(
				getPrefMachineType()))
		{
			this.previousPrefMachineType = this.getPrefMachineType();
			Log.d(MainActivity.APP_ID, PREF_MACHINE_TYPE+" changed!");
			return true;
		}
		return false;
	}

	public String getPrefSavedEnigmaState()
	{
		if (isFullyInitialized())
			return prefs.getString(PREF_SAVED_ENIGMA_STATE, "-1");
		else return null;
	}

	/**
	 * @param state HEX
	 */
	public void setPrefSavedEnigmaState(String state)
	{
		if (isFullyInitialized())
			prefs.edit().putString(PREF_SAVED_ENIGMA_STATE, state).apply();
	}

	public boolean prefSavedEnigmaStateChanged()
	{
		if (this.previousPrefSavedEnigmaState == null || !this.previousPrefSavedEnigmaState
				.equals(getPrefSavedEnigmaState()))
		{
			this.previousPrefSavedEnigmaState = this.getPrefSavedEnigmaState();
			Log.d(MainActivity.APP_ID, PREF_SAVED_ENIGMA_STATE+" changed!");
			return true;
		}
		return false;
	}

	public String getPrefMessageFormatting()
	{
		if (isFullyInitialized())
			return prefs.getString(SettingsActivity.PREF_MESSAGE_FORMATTING,
								   res.getStringArray(R.array.pref_alias_message_formatting)[0]);
		else return null;
	}

	public void setPrefMessageFormatting(String format)
	{
		if (isFullyInitialized())
			prefs.edit().putString(PREF_MESSAGE_FORMATTING, format).apply();
	}

	public boolean prefMessageFormattingChanged()
	{
		if (this.previousPrefMessageFormatting == null || !this.previousPrefMessageFormatting
				.equals(getPrefMessageFormatting()))
		{
			this.previousPrefMessageFormatting = this.getPrefMessageFormatting();
			Log.d(MainActivity.APP_ID, PREF_MESSAGE_FORMATTING+" changed!");
			return true;
		}
		return false;
	}

	public int getVersionNumber()
	{
		if (isFullyInitialized())
			return prefs.getInt(PREF_VERSION_NUMBER, -1);
		else return -1;
	}

	public void setVersionNumber(int v)
	{
		if (isFullyInitialized())
			prefs.edit().putInt(PREF_VERSION_NUMBER, v).apply();
	}

	public static class SettingsSingleton extends SettingsActivity
	{
		private static SettingsActivity instance;

		private SettingsSingleton()
		{
			super();
		}

		public static SettingsActivity getInstance(SharedPreferences prefs, Resources res)
		{
			instance = new SettingsActivity();
			instance.setSharedPreferences(prefs);
			instance.setResources(res);
			return instance;
		}

		public static SettingsActivity getInstance()
		{
			if (instance == null)
			{
				instance = new SettingsActivity();
				Log.d(MainActivity.APP_ID, "Created new SettingsActivity!");
			}
			return instance;
		}
	}
}