package com.shelflifeapp.android;

import java.io.IOException;
import java.util.List;

import jim.h.common.android.lib.zxing.integrator.IntentIntegrator;
import jim.h.common.android.lib.zxing.integrator.IntentResult;
import android.app.SearchManager;
import android.app.SearchableInfo;
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
import com.actionbarsherlock.widget.SearchView;
import com.shelflifeapp.database.FoodCursorAdapter;
import com.shelflifeapp.database.FoodDatabaseHelper;
import com.shelflifeapp.database.FoodTable;

public class MainActivity extends SherlockFragmentActivity{

	private final String TAG = "MAIN_ACTIVITY";
	
	private Context mContext = MainActivity.this;
	
	private Menu m_vwMenu;
	
	private SearchView mSearchView;
	
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
						/*Intent intentVar;						
						intentVar = new Intent(this, 
								BarCodeViewerActivity.class);
						intentVar.putExtra("upc", upc);
						this.startActivity(intentVar);*/
						this.getIntent().putExtra("upc", upc);
						this.startActivity(this.getIntent());
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
    
        
}
