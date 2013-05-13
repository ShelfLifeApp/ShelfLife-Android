package com.shelflifeapp.android;

import jim.h.common.android.lib.zxing.integrator.IntentIntegrator;
import jim.h.common.android.lib.zxing.integrator.IntentResult;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.shelflifeapp.database.FoodTable;

public class MainActivity extends SherlockFragmentActivity implements LoaderCallbacks<Cursor> {

	private final String TAG = "MAIN_ACTIVITY";
	private Context mContext = MainActivity.this;
	
	private Menu m_vwMenu;
	
	/** The ID of the CursorLoader to be initialized in the LoaderManager and used to load a Cursor. */
	private static final int LOADER_ID = 1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.Tab databaseTab = actionBar.newTab().setText("Database");
        ActionBar.Tab databaseTab2 = actionBar.newTab().setText("Database2");
        ActionBar.Tab myFoodTab = actionBar.newTab().setText("My Food");
        
        Fragment databaseFragment = new DatabaseFragment();
        Fragment databaseFragment2 = new DatabaseFragment();
        //Fragment myFoodFragment = new MyFoodFragment();
        
        databaseTab.setTabListener(new MyTabsListener(databaseFragment));
        databaseTab2.setTabListener(new MyTabsListener(databaseFragment2));

        
        actionBar.addTab(databaseTab);
        actionBar.addTab(databaseTab2);
        
        //Database Stuff
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
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
	        	intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
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
					IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
					if (scanResult != null) {
						String upc = scanResult.getContents();
						Toast.makeText(mContext, upc, Toast.LENGTH_LONG).show();
			 
						//put whatever you want to do with the code here
						Intent intentVar;						
						intentVar = new Intent(this, BarCodeViewerActivity.class);
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
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = {FoodTable.FOOD_KEY_ID, 
				FoodTable.FOOD_KEY_CAT, FoodTable.FOOD_KEY_NAME, FoodTable.FOOD_KEY_DAYS};
		Uri uri = Uri.parse("content://edu.calpoly.android.lab4.contentprovider/joke_table/filters/" /*+ getFilterString()*/);
		return new CursorLoader(this, uri, projection, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
}
