package com.shelflifeapp.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.shelflifeapp.models.Food;
import com.shelflifeapp.views.FoodListItem;
import com.shelflifeapp.views.ShelfLifeListViewHeader;
import com.slewson.simpleupc.SimpleUpcApi;
import com.slewson.simpleupc.SimpleUpcApi.SimpleUpcApiListener;
import com.slewson.simpleupc.SimpleUpcResponse;

public class SearchResultsActivity extends SherlockActivity implements SimpleUpcApiListener
{	
	private Context mContext = SearchResultsActivity.this;
	
	private Menu m_vwMenu;
	
	private SearchView mSearchView;
	
	private final int SCAN_BARCODE = 0;
	
	private ListView listView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    { 
    	super.onCreate(savedInstanceState);       
        setContentView(R.layout.searchresults);
        
        Bundle data = this.getIntent().getExtras();
        String result = data.getString("BARCODE");
        Toast.makeText(SearchResultsActivity.this, "Barcode: " + result, Toast.LENGTH_LONG).show();
        
        listView = (ListView) findViewById(R.id.search_list_view);
        listView.setDividerHeight(0);
        listView.addHeaderView(new ShelfLifeListViewHeader(mContext, "Search Results", result));
        
	    ArrayList<Food> foodList = new ArrayList<Food>();
	    foodList.add(new Food("Apple", R.drawable.icon_fruit));
	    foodList.add(new Food("Orange", R.drawable.icon_fruit));
	    foodList.add(new Food("Pear", R.drawable.icon_fruit));
	    foodList.add(new Food("Grape", R.drawable.icon_fruit));
	    foodList.add(new Food("Tangerine", R.drawable.icon_fruit));
	    foodList.add(new Food("Raisin", R.drawable.icon_fruit));
	    foodList.add(new Food("Blueberry", R.drawable.icon_fruit));
	    foodList.add(new Food("Raspberry", R.drawable.icon_fruit));
	    foodList.add(new Food("Banana", R.drawable.icon_fruit));
	    listView.setAdapter(new ShelfLifeSearchResultsAdapter(mContext, foodList));

        SimpleUpcApi simpleUpcApi = new SimpleUpcApi(this);
        simpleUpcApi.fetchProductByUpc(result);
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
    
	  public class ShelfLifeSearchResultsAdapter extends BaseAdapter
	  {
		  private List<Food> foodList;
		  private Context mContext;

	     public ShelfLifeSearchResultsAdapter(Context context, List<Food> foodList) 
		 {		
	 		this.foodList = foodList; 
	 		this.mContext = context;
  		 }

	     @Override
	     public View getView(int position, View convertView, ViewGroup parent) 
	     {
	    	 FoodListItem rowView = (FoodListItem)convertView;
	    	 Food food = foodList.get(position);

	    	 if (rowView == null)
	    	 {
	    		 rowView = new FoodListItem(mContext, foodList.get(position));
	    	 }
	    	 else
	    	 {
	    		 rowView = (FoodListItem) convertView;
	    	 }

	    	 rowView.setFood(food);

	    	 return rowView;
	     }

		@Override
		public int getCount() {
			return foodList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return foodList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	  }

	@Override
	public void onFetchProductByUpc(SimpleUpcResponse response) {
		Toast.makeText(SearchResultsActivity.this, "SimpleUPC Response: " + response.getCategory(), Toast.LENGTH_LONG).show();
		
	}
}
