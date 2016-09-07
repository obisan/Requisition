package com.dubinets.requisition.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.dubinets.requisition.R;

/**
 * Created by dubinets on 05.09.2016.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }
}
