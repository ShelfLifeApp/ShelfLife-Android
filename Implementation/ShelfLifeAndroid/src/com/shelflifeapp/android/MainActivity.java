package com.shelflifeapp.android;

import java.io.IOException;

import jim.h.common.android.lib.zxing.integrator.IntentIntegrator;
import jim.h.common.android.lib.zxing.integrator.IntentResult;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.shelflifeapp.database.FoodCursorAdapter;
import com.shelflifeapp.database.FoodDatabaseHelper;
import com.shelflifeapp.database.FoodTable;

public class MainActivity extends SherlockFragmentActivity {

	private final String TAG = "MAIN_ACTIVITY";
	
	private Context mContext = MainActivity.this;
	
	private Menu m_vwMenu;
	
	private FoodDatabaseHelper myDbHelper = new FoodDatabaseHelper(null, null, 
			null, 1);
	
	/** The ID of the CursorLoader to be initialized in the LoaderManager and 
	 * used to load a Cursor. */
	private static final int LOADER_ID = 1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.Tab databaseTab = actionBar.newTab().setText("All Foods");
        ActionBar.Tab databaseTab2 = actionBar.newTab().setText("My Food");
        ActionBar.Tab myFoodTab = actionBar.newTab().setText("My Food");
        
        Fragment databaseFragment = new DatabaseFragment();
        Fragment myFoodFragment = new MyFoodFragment();
        //Fragment myFoodFragment = new MyFoodFragment();
        
        databaseTab.setTabListener(new MyTabsListener(databaseFragment));
        databaseTab2.setTabListener(new MyTabsListener(myFoodFragment));
   
        actionBar.addTab(databaseTab);
        actionBar.addTab(databaseTab2);
         
        try {         
        	myDbHelper.createDataBase();         
        } catch (IOException ioe) {
        	throw new Error("Unable to create database");        
        }
         
        try {        
        	myDbHelper.openDataBase();        
        }catch(SQLException sqle){       
        	throw sqle;        
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getSupportMenuInflater();
		inflater.inflate(R.menu.menu_actionbar, menu);
		this.m_vwMenu = menu;
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
	        	startActivityForResult(intent, 0);
	            return true;
	        case R.id.menu_ab_search:	        		           
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
		switch(requestCode) {
			case IntentIntegrator.REQUEST_CODE: {
				if (resultCode != RESULT_CANCELED) {
					IntentResult scanResult = 
						IntentIntegrator.parseActivityResult(requestCode, 
								resultCode, data);
					if (scanResult != null) {
						String upc = scanResult.getContents();
						Toast.makeText(mContext, upc, Toast.LENGTH_LONG).show();
			 
						//put whatever you want to do with the code here
						Intent intentVar;						
						intentVar = new Intent(this, 
								BarCodeViewerActivity.class);
						intentVar.putExtra("upc", upc);
						this.startActivity(intentVar);			 
					}
				}
				break;
			}	
		}
	}
    
    private class MyTabsListener implements ActionBar.TabListener 
    {
    	public Fragment fragment;
    	 
    	public MyTabsListener(Fragment fragment) {
    	this.fragment = fragment;
    	}
    	 
    	@Override
    	public void onTabReselected(Tab tab, FragmentTransaction ft) {
    	}
    	 
    	@Override
    	public void onTabSelected(Tab tab, FragmentTransaction ft) {
    		ft.replace(R.id.fragment_container, fragment);
    	}
    	 
    	@Override
    	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    		ft.remove(fragment);
    	}  	 
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myDbHelper != null) {
        	myDbHelper.close();
        }
    }    
}
