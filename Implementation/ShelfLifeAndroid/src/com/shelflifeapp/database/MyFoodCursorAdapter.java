package com.shelflifeapp.database;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.shelflifeapp.models.Food;
import com.shelflifeapp.models.MyFood;
import com.shelflifeapp.views.FoodListItem;
import com.shelflifeapp.views.MyFoodListItem;

public class MyFoodCursorAdapter extends CursorAdapter{

	public MyFoodCursorAdapter(Context context, Cursor foodCursor, int flags) {
		super(context, foodCursor, flags);
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		int id = arg2.getInt(MyFoodTable.FOOD_COL_ID);
		int foodid  = arg2.getInt(MyFoodTable.FOOD_COL_FOODID);
		String purchased  = arg2.getString(MyFoodTable.FOOD_COL_PURCHASED);
		String opened  = arg2.getString(MyFoodTable.FOOD_COL_OPENED);
		int quantity  = arg2.getInt(MyFoodTable.FOOD_COL_QUANTITY);
		String notes  = arg2.getString(MyFoodTable.FOOD_COL_NOTES);
		
		//MyFood food = new MyFood(id, foodid, purchased, opened, quantity, notes);		
		//MyFoodListItem listItem = (MyFoodListItem) arg0;
		//listItem.setFood(food);		
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
