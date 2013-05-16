package com.shelflifeapp.database;

import com.shelflife.android.models.Food;
import com.shelflifeapp.views.FoodListItem;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;

public class FoodCursorAdapter extends CursorAdapter {

	/**
	 * Parameterized constructor that takes in the application Context in which
	 * it is being used and the Collection of Joke objects to which it is bound.
	 * 
	 * @param context
	 *            The application Context in which this JokeListAdapter is being
	 *            used.
	 * 
	 * @param jokeCursor
	 *            A Database Cursor containing a result set of Jokes which
	 *            should be bound to JokeViews.
	 *            
	 * @param flags
	 * 			  A list of flags that decide this adapter's behavior.
	 */
	public FoodCursorAdapter(Context context, Cursor jokeCursor, int flags) {
		super(context, jokeCursor, flags);
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		int id = arg2.getInt(FoodTable.FOOD_COL_ID);
		String name  = arg2.getString(FoodTable.FOOD_COL_NAME);
		Food food = new Food(name, id);
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		int id = arg1.getInt(FoodTable.FOOD_COL_ID);
		String name  = arg1.getString(FoodTable.FOOD_COL_NAME);
		Food food = new Food(name, id);
		FoodListItem foodView = new FoodListItem(arg0, food);
		return foodView;
	}
}
