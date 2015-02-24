package de.vanitasvitae.enigmandroid;

import android.os.Bundle;
import android.preference.PreferenceActivity;

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
