package com.shelflifeapp.android;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
 
public class UserPreferences extends SherlockPreferenceActivity {
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        addPreferencesFromResource(R.layout.user_preferences);
 
    }
    
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
		MenuInflater inflater = this.getSupportMenuInflater();
		inflater.inflate(R.menu.pref_actionbar, menu);
		return true;
    }

    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) 
	    {
	    	case android.R.id.home:
	    		finish();
	    		return true;
	    	case R.id.pref_menu_done:
	    		finish();
	    		return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}