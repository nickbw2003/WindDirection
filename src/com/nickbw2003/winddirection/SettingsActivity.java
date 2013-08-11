package com.nickbw2003.winddirection;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

import android.os.Bundle;

public class SettingsActivity extends SherlockPreferenceActivity {
	public static final String KEY_PREF_AUTOLOCATION = "pref_autolocation";
	public static final String KEY_PREF_LOCATION_NAME = "pref_location_name";
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   
	   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setHomeButtonEnabled(true);
	   
	   addPreferencesFromResource(R.xml.preferences);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
        	finish();
        }
        
        return super.onOptionsItemSelected(item);
    }
}
