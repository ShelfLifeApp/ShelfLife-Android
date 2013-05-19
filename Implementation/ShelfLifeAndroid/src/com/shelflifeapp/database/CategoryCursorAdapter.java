package com.shelflifeapp.database;

import com.shelflifeapp.models.Category;
import com.shelflifeapp.models.ExpirationData;
import com.shelflifeapp.models.Food;
import com.shelflifeapp.views.FoodListItem;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;

public class CategoryCursorAdapter extends CursorAdapter {

	public CategoryCursorAdapter(Context context, Cursor foodCursor, int flags) {
		super(context, foodCursor, flags);
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		int id = arg2.getInt(CategoryTable.FOOD_COL_ID);
		String name  = arg2.getString(CategoryTable.FOOD_COL_NAME);

		Food food = new Food(name, id);		
		FoodListItem listItem = (FoodListItem) arg0;
		listItem.setFood(food);		
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		int id = arg1.getInt(CategoryTable.FOOD_COL_ID);
		String name  = arg1.getString(CategoryTable.FOOD_COL_NAME);

		Food food = new Food(name, id);
		FoodListItem foodView = new FoodListItem(arg0, food);
		return foodView;
	}
	

}