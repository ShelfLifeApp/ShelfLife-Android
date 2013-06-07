package com.shelflifeapp.android;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.shelflifeapp.database.FoodTable;
import com.shelflifeapp.database.MyFoodTable;
import com.shelflifeapp.models.Category;
import com.shelflifeapp.models.ExpirationData;
import com.shelflifeapp.models.MyFood;

public class MainActivity extends SherlockFragmentActivity implements CategoryFragment.CategoryFragmentSelectedListener{

	private final String TAG = "MAIN_ACTIVITY";

	private Context mContext = MainActivity.this;

	private Menu m_vwMenu;

	private SearchView mSearchView;

	private final int SCAN_BARCODE = 0;
	private final int RESULT_SETTINGS = 1;
	private AlarmBroadcastReceiver alarm;

	private ActionBar actionBar;
	
	private Fragment databaseFragment;
    private Fragment myFoodFragment;
    private Fragment categoryFragment;
    
    private ActionBar.Tab databaseTab;
    private ActionBar.Tab myFoodTab;
    
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {   	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
        
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        databaseTab = actionBar.newTab().setText("All Foods");
        myFoodTab = actionBar.newTab().setText("My Food");
              
        databaseFragment = new DatabaseFragment();
        myFoodFragment = new MyFoodFragment();
        categoryFragment = new CategoryFragment();
        
        databaseTab.setTabListener(new DatabaseTabListener(categoryFragment));
        myFoodTab.setTabListener(new MyFoodTabListener(myFoodFragment));
        
        actionBar.addTab(databaseTab);
        actionBar.addTab(myFoodTab);  
        
        // Alarm Stuff
        alarm = new AlarmBroadcastReceiver();
        Context context = this.getApplicationContext();
        if(alarm != null){
        	alarm.SetAlarm(context);
        }else{
        	Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        	Log.d("mgrap", "alarm is null");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
		MenuInflater inflater = this.getSupportMenuInflater();
		inflater.inflate(R.menu.menu_actionbar, menu);
		this.m_vwMenu = menu;
		this.mSearchView = (SearchView) menu.findItem(R.id.menu_ab_search).getActionView();
	    this.mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

		    @Override
		    public boolean onQueryTextSubmit(String query) {
		        // collapse the view ?
				Intent searchResults = new Intent(mContext, SearchResultsActivity.class);
				searchResults.putExtra(SearchResultsActivity.SEARCH_STRING, query);
				startActivity(searchResults);
		        menu.findItem(R.id.menu_ab_search).collapseActionView();
		        
		        return true;
		    }

		    @Override
		    public boolean onQueryTextChange(String newText) {
		        // search goes here !!
		        // listAdapter.getFilter().filter(query);
		        return true;
		    }

	    });
		return true;
    }
    
    /**
     * Called when an item in the Action Bar Sherlock Menu is pressed.
     */
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_ab_barcode:
	        	Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	        	intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", 
	        			"QR_CODE_MODE");
	        	startActivityForResult(intent, SCAN_BARCODE);
	            return true;
	        case R.id.menu_ab_search:	        		           
	        	return true;	      
	        case R.id.menu_ab_settings:
	        	Intent i = new Intent(MainActivity.this, UserPreferences.class);
	        	startActivityForResult(i, RESULT_SETTINGS);
	        	return true;
	        case android.R.id.home:
	            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

	            // Replace whatever is in the fragment_container view with this fragment,
	            // and add the transaction to the back stack so the user can navigate back
	            transaction.replace(R.id.fragment_container, categoryFragment);
	            transaction.addToBackStack(null);

	            // Commit the transaction
	            transaction.commit();
	            actionBar.setDisplayHomeAsUpEnabled(false);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    
    /**
     * Deals with the UPC code found by bar code scanner. 
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) 
		{
			case SCAN_BARCODE:
			{
				if (resultCode == Activity.RESULT_OK)
				{
		            String contents = data.getStringExtra("SCAN_RESULT");
		            String format = data.getStringExtra("SCAN_RESULT_FORMAT");

					Intent searchResults = new Intent(mContext, SearchResultsActivity.class);
					searchResults.putExtra(SearchResultsActivity.SEARCH_UPC, contents);
					startActivity(searchResults);
				}
				break;
			}
			case RESULT_SETTINGS:
			{
				// something with settigns
				break;
			}
		}
	}
    
    private class DatabaseTabListener implements ActionBar.TabListener 
    {
    	public Fragment fragment;
    	 
    	public DatabaseTabListener(Fragment fragment) 
    	{
    		this.fragment = fragment;
    	}
    	 
    	@Override
    	public void onTabReselected(Tab tab, FragmentTransaction ft) 
    	{
    	}
    	 
    	@Override
    	public void onTabSelected(Tab tab, FragmentTransaction ft) 
    	{
    		if (tab == databaseTab)
    		{
    			actionBar.setDisplayHomeAsUpEnabled(false);
    		}
    		else
    		{
    			actionBar.setDisplayHomeAsUpEnabled(true);	
    		}
    		ft.replace(R.id.fragment_container, fragment);
    	}
    	 
    	@Override
    	public void onTabUnselected(Tab tab, FragmentTransaction ft) 
    	{
    		//if (tab == databaseTab || tab == myFoodTab)
    		//{
    			actionBar.setDisplayHomeAsUpEnabled(false);
    		//}
    		
    		
    		ft.remove(fragment);
    	}  	 
    }    

    private class MyFoodTabListener implements ActionBar.TabListener 
    {
    	public Fragment fragment;
    	 
    	public MyFoodTabListener(Fragment fragment) 
    	{
    		this.fragment = fragment;
    	}
    	 
    	@Override
    	public void onTabReselected(Tab tab, FragmentTransaction ft) 
    	{
    	}
    	 
    	@Override
    	public void onTabSelected(Tab tab, FragmentTransaction ft) 
    	{
    		actionBar.setDisplayHomeAsUpEnabled(false);
    		ft.replace(R.id.fragment_container, fragment);
    	}
    	 
    	@Override
    	public void onTabUnselected(Tab tab, FragmentTransaction ft) 
    	{
   
    		ft.remove(fragment);
    	}  	 
    }

	@Override
	public void onCategorySelected(int category) 
	{
		actionBar.setDisplayHomeAsUpEnabled(true);
		Log.d("SPOCK", "onCategorySelected: " + category);
        DatabaseFragment dbFrag = new DatabaseFragment();
        Bundle args = new Bundle();
        args.putInt(DatabaseFragment.CATEGORY_KEY, category);
        dbFrag.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, dbFrag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
	} 
}