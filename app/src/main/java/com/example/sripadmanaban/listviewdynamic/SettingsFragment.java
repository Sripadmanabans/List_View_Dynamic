package com.example.sripadmanaban.listviewdynamic;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Setting Fragment
 * Created by Sripadmanaban on 12/15/2014.
 */
public class SettingsFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
