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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.ActionMode.Callback;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.shelflifeapp.database.FoodDatabaseHelper;
import com.shelflifeapp.database.FoodTable;
import com.shelflifeapp.database.MyFoodCursorAdapter;
import com.shelflifeapp.database.MyFoodTable;
import com.shelflifeapp.models.MyFood;
import com.shelflifeapp.views.MyFoodListItem;
import com.shelflifeapp.views.ShelfLifeListViewHeader;

public class MyFoodFragment extends SherlockListFragment 
		implements LoaderCallbacks<Cursor> {
	private Context mContext;
	
	/** Used to handle Contextual Action Mode when long-clicking on a single Food. */
	private Callback mActionModeCallback;
	private ActionMode mActionMode;
	
	/** The Joke that is currently focused after long-clicking. */
	private int selected_position;
	
	/** 
	 * The ID of the CursorLoader to be initialized in the LoaderManager 
	 * and used to load a Cursor. 
	 */
	private static final int LOADER_ID = 1;
	
	private MyFoodCursorAdapter m_myfoodAdapter;
	
	private FoodDatabaseHelper myDbHelper;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    mContext = this.getActivity();
	    this.getListView().setBackgroundColor(getResources().getColor(R.color.activity_background));
	    
	    this.getListView().setDividerHeight(0);
	    this.getListView().setVerticalScrollBarEnabled(false);
	    if (savedInstanceState == null)
	    	this.getListView().addHeaderView(new ShelfLifeListViewHeader(mContext, "My Foods", "Items you have"));
	    myDbHelper = new FoodDatabaseHelper(this.mContext, null, 
				null, 9);
	    
	    this.getListView().setOnItemLongClickListener (new OnItemLongClickListener() {
			
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if (mActionMode != null) {
		            return false;
		        }
		        mActionMode = getSherlockActivity().startActionMode(mActionModeCallback);
		        selected_position = position;
		        return true;
		    }
		});
	    
	    mActionModeCallback = new Callback() {			

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				MenuInflater inflater = mode.getMenuInflater();
		        inflater.inflate(R.menu.context_action_menu, menu);
		        return true;
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
	            case R.id.menu_remove:
	            	removeFood(((MyFoodListItem) MyFoodFragment.this.getListView().getChildAt(selected_position)).getMyFood());
	            	mode.finish();
	                return true;
	                
	            default:
	                return false;
				}
			}
			
			public void onDestroyActionMode(ActionMode mode) {
		        mActionMode = null;
		    }

		};
	    
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
	    
	    Cursor myfoodCursor = myDbHelper.fetchAllMyFood();
	    this.m_myfoodAdapter = new MyFoodCursorAdapter(mContext, myfoodCursor, 0);
	    getLoaderManager().initLoader(LOADER_ID, null, this);
	    this.getListView().setAdapter(this.m_myfoodAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent i = new Intent(mContext, MyFoodDetails.class);
		startActivity(i);
	
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = {FoodTable.DATABASE_TABLE_FOOD + "." 
				+ FoodTable.FOOD_KEY_ID, 
				FoodTable.FOOD_KEY_NAME,
				FoodTable.FOOD_KEY_CATEGORY,
				FoodTable.FOOD_KEY_SHELF_U,
				FoodTable.FOOD_KEY_SHELF_O,
				FoodTable.FOOD_KEY_FRIDGE_U,
				FoodTable.FOOD_KEY_FRIDGE_O,
				FoodTable.FOOD_KEY_FREEZER_U,
				FoodTable.FOOD_KEY_FREEZER_O,
				FoodTable.FOOD_KEY_TIPS,
				MyFoodTable.DATABASE_TABLE_MYFOOD + "." + MyFoodTable.FOOD_KEY_ID,
				MyFoodTable.FOOD_KEY_FOODID,
				MyFoodTable.FOOD_KEY_PURCHASED,
				MyFoodTable.FOOD_KEY_OPENED,
				MyFoodTable.FOOD_KEY_QUANTITY,
				MyFoodTable.FOOD_KEY_PICTURE,
				MyFoodTable.FOOD_KEY_NOTES};
		Uri uri = Uri.parse("content://com.shelflifeapp.android.provider/myfood_table/all");
		return new CursorLoader(mContext, uri, projection, null, null, 
				null);
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		this.m_myfoodAdapter.swapCursor(arg1);
		setListShown(true);	
		
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		this.m_myfoodAdapter.swapCursor(null);
		
	}
	
	@Override
	public void onDestroy() {
        super.onDestroy();
        if (myDbHelper != null) {
        	myDbHelper.close();
        }
    }
	
	protected void removeFood(MyFood myfood)
	{
		Uri uri = Uri.parse("content://com.shelflifeapp.android.provider/myfood_table/delete/" + myfood.getId());
		this.getSherlockActivity().getContentResolver().delete(uri, null, null);
		fillData();
	}
	
	protected void fillData(){
		this.getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
		this.getListView().setAdapter(m_myfoodAdapter);
	}
}
