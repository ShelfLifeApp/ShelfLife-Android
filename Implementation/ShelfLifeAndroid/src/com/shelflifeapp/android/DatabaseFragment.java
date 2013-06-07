package com.shelflifeapp.android;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.shelflifeapp.database.CategoryCursorAdapter;
import com.shelflifeapp.database.CategoryTable;
import com.shelflifeapp.database.FoodCursorAdapter;
import com.shelflifeapp.database.FoodDatabaseHelper;
import com.shelflifeapp.database.FoodTable;
import com.shelflifeapp.views.FoodListItem;
import com.shelflifeapp.views.ShelfLifeListViewHeader;

public class DatabaseFragment extends ListFragment 
		implements LoaderCallbacks<Cursor> 
{
	private Context mContext;
	
	/** 
	 * The ID of the CursorLoader to be initialized in the LoaderManager 
	 * and used to load a Cursor. 
	 */
	private static final int LOADER_ID = 1;
	
	private FoodCursorAdapter m_foodAdapter;
	
	private FoodDatabaseHelper myDbHelper;
	
	private int catId;
	public final static String CATEGORY_KEY = "meow";
	
	  @Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    mContext = this.getActivity();
	    this.getListView().setBackgroundColor(getResources().getColor(R.color.activity_background));
	    Bundle catIdBundle = this.getArguments();
	    if(catIdBundle == null){
			//Log.d("shelflife", "Bundle is null");
		}else{
			catId = catIdBundle.getInt(CATEGORY_KEY, -1);
			Log.d("SPOCK", "DatabaseFragment Category: " + catId);
			if(catId == -1){
				Toast.makeText(mContext, "cat", Toast.LENGTH_SHORT).show();
				Log.d("shelflife", "catId is -1");
			}			
		}
	    this.getListView().setDividerHeight(0);
	    this.getListView().setVerticalScrollBarEnabled(false);
	    this.getListView().setScrollingCacheEnabled(true);
	    this.getListView().addHeaderView(new ShelfLifeListViewHeader(mContext, 
	    		"All Foods", "Browse Database"));
	    myDbHelper = new FoodDatabaseHelper(this.mContext, null, 
				null, 9);
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

	    this.m_foodAdapter = new FoodCursorAdapter(mContext, null, 0);
	    getLoaderManager().initLoader(LOADER_ID, null, this);
	    this.getListView().setAdapter(this.m_foodAdapter);	
	    
	  }
	  
	   @Override
	   public void onActivityResult(int requestCode, int resultCode, Intent data) 
	   {
			super.onActivityResult(requestCode, resultCode, data);
	   }
	  
	  @Override
	  public void onListItemClick(ListView l, View v, int position, long id) 
	  {
		  if (position == 0)
		  {
			  return;
		  }
		  
		  Intent i = new Intent(mContext, FoodDetails.class);
		  i.putExtra("food", ((FoodListItem) v).getFood());
		  startActivity(i);
	  }

	  @Override
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
			Uri uri = Uri.parse("content://com.shelflifeapp.android.provider/food_table/bycat/" + catId);
			return new CursorLoader(mContext, uri, projection, null, null, 
					null);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
			this.m_foodAdapter.swapCursor(arg1);
			setListShown(true);		
		}

		@Override
		public void onLoaderReset(Loader<Cursor> arg0) {
			this.m_foodAdapter.swapCursor(null);
		}
		
		@Override
		public void onDestroy() {
	        super.onDestroy();
	        if (myDbHelper != null) {
	        	myDbHelper.close();
	        }
	    }
}
