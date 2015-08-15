package de.vanitasvitae.enigmandroid;

import android.os.Bundle;
import android.preference.PreferenceActivity;

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
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //noinspection deprecation
        addPreferencesFromResource(R.xml.pref_page);
    }
}
