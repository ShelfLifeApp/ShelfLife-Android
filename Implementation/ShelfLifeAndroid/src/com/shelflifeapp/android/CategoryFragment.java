package com.shelflifeapp.android;

import java.io.IOException;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.Toast;

import com.shelflifeapp.database.CategoryCursorAdapter;
import com.shelflifeapp.database.CategoryTable;
import com.shelflifeapp.database.FoodDatabaseHelper;
import com.shelflifeapp.views.CategoryListItem;
import com.shelflifeapp.views.ShelfLifeListViewHeader;

public class CategoryFragment extends ListFragment implements LoaderCallbacks<Cursor>{
private Context mContext;
	
	/** 
	 * The ID of the CursorLoader to be initialized in the LoaderManager 
	 * and used to load a Cursor. 
	 */
	private static final int LOADER_ID = 1;
	
	private CategoryCursorAdapter m_catAdapter;
	
	private FoodDatabaseHelper myDbHelper; 
	
	private CategoryFragmentSelectedListener mCallback = null;
	
	  @Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    mContext = this.getActivity();
	    this.getListView().setBackgroundColor(getResources().getColor(R.color.activity_background));

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

	    this.m_catAdapter = new CategoryCursorAdapter(mContext, null, 0);
	    getLoaderManager().initLoader(LOADER_ID, null, this);
	    this.getListView().setAdapter(this.m_catAdapter);	    
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
		  
		  int category = ((CategoryListItem) v).getCategoryId();
		  //Log.d("SPOCK", "Callback with Category: " + category);
		  mCallback.onCategorySelected(category);
	  }

	  @Override
		public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
			String[] projection = {CategoryTable.FOOD_KEY_ID, 
					CategoryTable.FOOD_KEY_NAME,
					CategoryTable.FOOD_KEY_ICON};
			Uri uri = Uri.parse("content://com.shelflifeapp.android.provider/category_table/all");
			return new CursorLoader(mContext, uri, projection, null, null, 
					null);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
			this.m_catAdapter.swapCursor(arg1);
			setListShown(true);		
		}

		@Override
		public void onLoaderReset(Loader<Cursor> arg0) {
			this.m_catAdapter.swapCursor(null);
		}
		
		@Override
		public void onDestroy() {
	        super.onDestroy();
	        if (myDbHelper != null) {
	        	myDbHelper.close();
	        }
	    }
		
		@Override
		public void onAttach(Activity activity) {
		    super.onAttach(activity);

		    // This makes sure that the container activity has implemented
		    // the callback interface. If not, it throws an exception
		    try 
		    {
		        mCallback = (CategoryFragmentSelectedListener) activity;
		    } 
		    catch (ClassCastException e) 
		    {
		        throw new ClassCastException(activity.toString()
		                + " must implement CategoryFragmentSelectedListener");
		    }
		}
		
		public interface CategoryFragmentSelectedListener
		{
			public void onCategorySelected(int category);
		}
}
