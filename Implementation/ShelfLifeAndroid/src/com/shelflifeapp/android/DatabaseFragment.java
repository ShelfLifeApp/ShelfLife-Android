package com.shelflifeapp.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
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

import com.shelflifeapp.android.models.Food;
import com.shelflifeapp.database.FoodCursorAdapter;
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
	
	  @Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);

	    mContext = this.getActivity();
	    
	    this.getListView().setDividerHeight(0);
	    this.getListView().setVerticalScrollBarEnabled(false);
	    this.getListView().addHeaderView(new ShelfLifeListViewHeader(mContext, 
	    		"All Foods", "Browse Database"));
	    mContext = getActivity();
	    
	    this.m_foodAdapter = new FoodCursorAdapter(mContext, null, 0);
	    displayListView();
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
					FoodTable.FOOD_KEY_NAME};
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
		
		private void displayListView(){
			String[] columns = new String[] {
				FoodTable.FOOD_KEY_ID,
				FoodTable.FOOD_KEY_NAME
			};	
		}
}
