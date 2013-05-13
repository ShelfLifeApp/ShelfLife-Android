package com.shelflifeapp.database;

import com.shelflife.android.models.Food;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;

public class FoodCursorAdapter extends CursorAdapter {
	/** The OnJokeChangeListener that should be connected to each of the
	 * JokeViews created/managed by this Adapter. */
	//private OnJokeChangeListener m_listener;

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

	/**
	 * Mutator method for changing the OnJokeChangeListener.
	 * 
	 * @param listener
	 *            The OnJokeChangeListener that will be notified when the
	 *            internal state of any Joke contained in one of this Adapters
	 *            JokeViews is changed.
	 */
	/*public void setOnJokeChangeListener(OnJokeChangeListener mListener) {
		this.m_listener = mListener;
	}*/

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		int id = arg2.getInt(FoodTable.FOOD_COL_ID);
		String category = arg2.getString(FoodTable.FOOD_COL_CAT);
		String name  = arg2.getString(FoodTable.FOOD_COL_NAME);
		int days = arg2.getInt(FoodTable.FOOD_COL_DAYS);
		Food food = new Food(name, id);
		/*((JokeView) arg0).setOnJokeChangeListener(null);
		((JokeView) arg0).setJoke(joke);
		((JokeView) arg0).setOnJokeChangeListener(this.m_listener);	*/	
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		int id = arg1.getInt(FoodTable.FOOD_COL_ID);
		String category = arg1.getString(FoodTable.FOOD_COL_CAT);
		String name  = arg1.getString(FoodTable.FOOD_COL_NAME);
		int days = arg1.getInt(FoodTable.FOOD_COL_DAYS);
		Food food = new Food(name, id);
		return null;
		/*JokeView jokeView = new JokeView(arg0, joke);
		jokeView.setOnJokeChangeListener(this.m_listener);
		return jokeView;*/
	}
}
