package com.shelflifeapp.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.shelflifeapp.android.models.Category;
import com.shelflifeapp.android.models.ExpirationData;
import com.shelflifeapp.android.models.Food;
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
	
	private FoodDatabaseHelper myDbHelper = new FoodDatabaseHelper(this.mContext, null, 
			null, 1);
	
	  @Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);

	    mContext = this.getActivity();
	    
	    this.getListView().setDividerHeight(0);
	    this.getListView().setVerticalScrollBarEnabled(false);
	    this.getListView().setScrollingCacheEnabled(true);
	    this.getListView().addHeaderView(new ShelfLifeListViewHeader(mContext, 
	    		"All Foods", "Browse Database"));

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
	    
	    Cursor foodCursor = myDbHelper.fetchAllFood();
	    this.m_foodAdapter = new FoodCursorAdapter(mContext, foodCursor, 0);
	    getLoaderManager().initLoader(LOADER_ID, null, this);
	    this.getListView().setAdapter(this.m_foodAdapter);
	    
	  }

	  @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
	    // Do something with the data
	  }

	  @Override
		public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
			String[] projection = {FoodTable.FOOD_KEY_ID, 
					FoodTable.FOOD_KEY_NAME,
					FoodTable.FOOD_KEY_CATEGORY,
					FoodTable.FOOD_KEY_SHELF_U,
					FoodTable.FOOD_KEY_SHELF_O,
					FoodTable.FOOD_KEY_FRIDGE_U,
					FoodTable.FOOD_KEY_FRIDGE_O,
					FoodTable.FOOD_KEY_FREEZER_U,
					FoodTable.FOOD_KEY_FREEZER_O,
					FoodTable.FOOD_KEY_TIPS};
			Uri uri = Uri.parse("content://com.shelflifeapp.android.provider/food_table/food/3");
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
		
		/*@Override
	    protected void onDestroy() {
	        super.onDestroy();
	        if (myDbHelper != null) {
	        	myDbHelper.close();
	        }
	    }*/
		
		public class FoodListAdapter extends BaseAdapter{

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				// TODO Auto-generated method stub
				return null;
			}
			
		}
}
