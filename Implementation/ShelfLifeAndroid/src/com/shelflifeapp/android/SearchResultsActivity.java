package com.shelflifeapp.android;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.shelflifeapp.database.FoodCursorAdapter;
import com.shelflifeapp.database.FoodDatabaseHelper;
import com.shelflifeapp.database.FoodTable;
import com.shelflifeapp.views.ShelfLifeListViewHeader;
import com.slewson.simpleupc.SimpleUpcApi;
import com.slewson.simpleupc.SimpleUpcApi.SimpleUpcApiListener;
import com.slewson.simpleupc.SimpleUpcResponse;

public class SearchResultsActivity extends SherlockFragmentActivity implements SimpleUpcApiListener, LoaderCallbacks<Cursor>
{	
	private Context mContext = SearchResultsActivity.this;
	
	private Menu m_vwMenu;
	
	private SearchView mSearchView;
	
	private static final int LOADER_ID = 1;
	
	private final int SCAN_BARCODE = 0;
	
	private ListView listView;
	
	public final static String SEARCH_UPC = "SEARCH_UPC";
	public final static String SEARCH_STRING = "SEARCH_STRING";
	
	private FoodCursorAdapter m_foodAdapter;
	
	private FoodDatabaseHelper myDbHelper;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    { 
    	super.onCreate(savedInstanceState);       
        setContentView(R.layout.searchresults);
        
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        Bundle data = this.getIntent().getExtras();
        
        String upc = data.getString(SEARCH_UPC);
        if (upc != null)
        	Toast.makeText(SearchResultsActivity.this, "UPC: " + upc, Toast.LENGTH_LONG).show();
        
        String search = data.getString(SEARCH_STRING);
        if (search != null)
        	Toast.makeText(SearchResultsActivity.this, "Search: " + search, Toast.LENGTH_LONG).show();       
        
        listView = (ListView) findViewById(R.id.search_list_view);
        listView.setDividerHeight(0);
        listView.addHeaderView(new ShelfLifeListViewHeader(mContext, "Search Results", "SEARCH GOES HERE"));
        
	    myDbHelper = new FoodDatabaseHelper(this.mContext, null, null, 9);
        
	    try 
	    {         
        	myDbHelper.createDataBase(); 
        	myDbHelper.openDataBase();  
        } 
	    catch (IOException ioe) 
	    {
        	throw new Error("Unable to create database");        
        }
        catch(SQLException sqle)
        {       
        	throw sqle;        
        }
	    
        /* Right now, its just hard coded to search for all the foods that have the name
         * equal to "Apple".  Its giving some errors about the columns or something.  
         * 
         * Also, in onLoadFinished, what does setListShown() due?
         * 
         * Maybe something to do with 
         * Uri uri = Uri.parse("content://com.shelflifeapp.android.provider/food_table/");
         * 
         * I put the WHERE clause is in the fetchFoodByStringQuery(String query) in the helper
         * but is it supposed to go there???  Makes more sense that it should be in the helper
         */
        
	    Cursor foodCursor = myDbHelper.fetchFoodByStringQuery("Apple");
	    this.m_foodAdapter = new FoodCursorAdapter(mContext, foodCursor, 0);
	    getSupportLoaderManager().initLoader(LOADER_ID, null, this);
	    listView.setAdapter(this.m_foodAdapter);	

	    if (upc != null)
	    {
	    	SimpleUpcApi simpleUpcApi = new SimpleUpcApi(this);
	    	simpleUpcApi.fetchProductByUpc(upc);
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
	public boolean onOptionsItemSelected(MenuItem item) 
    {
	    switch (item.getItemId()) 
	    {
	    	case android.R.id.home:
	    		finish();
	    		return true;
	        case R.id.menu_ab_barcode:
	        	Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	        	intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", 
	        			"QR_CODE_MODE");
	        	startActivityForResult(intent, SCAN_BARCODE);
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
    	Toast.makeText(SearchResultsActivity.this, "MainONResult", Toast.LENGTH_LONG).show();
		switch(requestCode) 
		{
			case SCAN_BARCODE:
			{
				Toast.makeText(mContext, "SCAN_BARCODE", Toast.LENGTH_LONG).show();
			}
		}
	}
    
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = {FoodTable.DATABASE_TABLE_FOOD + "." + FoodTable.FOOD_KEY_ID, 
				FoodTable.DATABASE_TABLE_FOOD + "." + FoodTable.FOOD_KEY_NAME,
				FoodTable.FOOD_KEY_CATEGORY,
				FoodTable.FOOD_KEY_SHELF_U,
				FoodTable.FOOD_KEY_SHELF_O,
				FoodTable.FOOD_KEY_FRIDGE_U,
				FoodTable.FOOD_KEY_FRIDGE_O,
				FoodTable.FOOD_KEY_FREEZER_U,
				FoodTable.FOOD_KEY_FREEZER_O,
				FoodTable.FOOD_KEY_TIPS};
		Uri uri = Uri.parse("content://com.shelflifeapp.android.provider/food_table/");
		return new CursorLoader(mContext, uri, projection, null, null, 
				null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) 
	{
		this.m_foodAdapter.swapCursor(arg1);
		//setListShown(true);	What is this for?	
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) 
	{
		this.m_foodAdapter.swapCursor(null);
	}
	
	@Override
	public void onDestroy() 
	{
        super.onDestroy();
        if (myDbHelper != null) 
        {
        	myDbHelper.close();
        }
    }
    
	@Override
	public void onFetchProductByUpc(SimpleUpcResponse response) 
	{
		Toast.makeText(SearchResultsActivity.this, "SimpleUPC Response: " + response.getCategory(), Toast.LENGTH_LONG).show();
	}
}
