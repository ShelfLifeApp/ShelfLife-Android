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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.shelflifeapp.views.FoodListItem;
import com.shelflifeapp.views.ShelfLifeListViewHeader;
import com.slewson.simpleupc.SimpleUpcApi;
import com.slewson.simpleupc.SimpleUpcApi.SimpleUpcApiListener;
import com.slewson.simpleupc.SimpleUpcResponse;

public class SearchResultsActivity extends SherlockFragmentActivity implements SimpleUpcApiListener, LoaderCallbacks<Cursor>, OnItemClickListener
{	
	private Context mContext = SearchResultsActivity.this;
	
	private Menu m_vwMenu;
	
	private SearchView mSearchView;
	
	private static final int LOADER_ID = 1;
	
	private final int SCAN_BARCODE = 0;
	
	private ListView listView;
	
	public final static String SEARCH_UPC = "SEARCH_UPC";
	public final static String SEARCH_STRING = "SEARCH_STRING";
	
	private String searchItem[];
	
	private FoodCursorAdapter m_foodAdapter;
	
	private FoodDatabaseHelper myDbHelper;
	
	private ShelfLifeListViewHeader mHeader;
	
	private String HEADER_TITLE = "Search For Food";
	private String HEADER_SUBTITLE_SEARCHING = "Searching...";
	private String HEADER_SUBTITLE_RESULT = "Results for ";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    { 
    	super.onCreate(savedInstanceState);       
        setContentView(R.layout.searchresults);
        
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
 
        listView = (ListView) findViewById(R.id.search_list_view);
        listView.setBackgroundColor(getResources().getColor(R.color.activity_background));
        listView.setDividerHeight(0);
        mHeader = new ShelfLifeListViewHeader(mContext, HEADER_TITLE, HEADER_SUBTITLE_SEARCHING);
        listView.addHeaderView(mHeader);
        listView.setOnItemClickListener(this);
        
	    setUpDatabase();
	    
	    searchForItem();
    }
    
    private String formatSearchString(String searchString)
    {
    	String toBeCapped = "";
    	if (!"".equals(searchString))
    	{
    		String[] tokens = searchString.split("\\s");
    		toBeCapped = "";

    		for(int i = 0; i < tokens.length; i++)
    		{
    			char capLetter = Character.toUpperCase(tokens[i].charAt(0));
    			toBeCapped +=  " " + capLetter + tokens[i].substring(1, tokens[i].length());
    		}
    	}
    	
    	return toBeCapped.trim();
    }
    
    private String[] getQueries(String search)
    {
    	String[] temp = formatSearchString(search).split("\\s");
    	for (int i = 0; i < temp.length; i++)
    		temp[i] = "%" + temp[i] + "%";
    	
    	return temp;
    }
    
    private void searchForItem()
    {
        Bundle data = this.getIntent().getExtras();
        
        String upc = data.getString(SEARCH_UPC);
        if (upc != null)
        {
	    	SimpleUpcApi simpleUpcApi = new SimpleUpcApi(this);
	    	simpleUpcApi.fetchProductByUpc(upc);
        }
        
        String searchString = data.getString(SEARCH_STRING);
        if (searchString != null)
        {
        	setUpLoader(searchString);
        }	
    }
    
    private void setUpDatabase()
    {
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
    }
    
    private void setUpLoader(String searchText)
    {
    	searchItem = getQueries(searchText);
    	mHeader.setTitles(HEADER_TITLE, HEADER_SUBTITLE_RESULT + searchText);
	    this.m_foodAdapter = new FoodCursorAdapter(mContext, null, 0);
	    getSupportLoaderManager().initLoader(LOADER_ID, null, this);
	    listView.setAdapter(this.m_foodAdapter);
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
    	//Toast.makeText(SearchResultsActivity.this, "MainONResult", Toast.LENGTH_LONG).show();
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
		
		String selection = "name LIKE ? ";
		String temp = searchItem[0];
		for(int i = 1; i < searchItem.length; i++){
			selection += " OR name LIKE ? ";
			temp += " " + searchItem[i];
		}
		String hardcode = "name LIKE '%Beef%' OR name LIKE '%Apple%'";
		
		Log.d("mgrap", temp);
		Log.d("mgrap", "Selection: " + selection);
		String uriText = "content://com.shelflifeapp.android.provider/food_table/byname/";
		Uri uri = Uri.parse(uriText);
		return new CursorLoader(mContext, uri, projection, selection, searchItem, 
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
		if (response.isSuccess())
		{
			setUpLoader(response.getCategory());
		}
		else
		{
			Toast.makeText(this, "Unable to look up UPC", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int position, long id) 
	{
		if (position != 0)
		{
			Intent i = new Intent(mContext, FoodDetails.class);
			i.putExtra("food", ((FoodListItem) v).getFood());
			startActivity(i);
		}
		
	}
}
