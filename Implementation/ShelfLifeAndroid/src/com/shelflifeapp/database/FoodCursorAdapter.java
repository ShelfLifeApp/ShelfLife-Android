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

public class FoodCursorAdapter extends CursorAdapter {

	public FoodCursorAdapter(Context context, Cursor foodCursor, int flags) {
		super(context, foodCursor, flags);
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		int id = arg2.getInt(FoodTable.FOOD_COL_ID);
		String name  = arg2.getString(FoodTable.FOOD_COL_NAME);
		int category = arg2.getInt(FoodTable.FOOD_COL_CATEGORY);
		int shelf_u = arg2.getInt(FoodTable.FOOD_COL_SHELF_U);
		int shelf_o = arg2.getInt(FoodTable.FOOD_COL_SHELF_O);
		int fridge_u = arg2.getInt(FoodTable.FOOD_COL_FRIDGE_U);
		int fridge_o = arg2.getInt(FoodTable.FOOD_COL_FRIDGE_O);
		int freezer_u = arg2.getInt(FoodTable.FOOD_COL_FREEZER_U);
		int freezer_o = arg2.getInt(FoodTable.FOOD_COL_FREEZER_O);
		String tips = arg2.getString(FoodTable.FOOD_COL_TIPS);

		Food food = new Food(id, name, new Category(category), 
				new ExpirationData(shelf_o, shelf_u, fridge_o, fridge_u, 
						freezer_o, freezer_u), tips);		
		FoodListItem listItem = (FoodListItem) arg0;
		listItem.setFood(food);		
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		int id = arg1.getInt(FoodTable.FOOD_COL_ID);
		String name  = arg1.getString(FoodTable.FOOD_COL_NAME);
		int category = arg1.getInt(FoodTable.FOOD_COL_CATEGORY);
		int shelf_u = arg1.getInt(FoodTable.FOOD_COL_SHELF_U);
		int shelf_o = arg1.getInt(FoodTable.FOOD_COL_SHELF_O);
		int fridge_u = arg1.getInt(FoodTable.FOOD_COL_FRIDGE_U);
		int fridge_o = arg1.getInt(FoodTable.FOOD_COL_FRIDGE_O);
		int freezer_u = arg1.getInt(FoodTable.FOOD_COL_FREEZER_U);
		int freezer_o = arg1.getInt(FoodTable.FOOD_COL_FREEZER_O);
		String tips = arg1.getString(FoodTable.FOOD_COL_TIPS);
		
		Food food = new Food(id, name, new Category(category), 
				new ExpirationData(shelf_o, shelf_u, fridge_o, fridge_u, 
						freezer_o, freezer_u), tips);	
		FoodListItem foodView = new FoodListItem(arg0, food);
		return foodView;
	}
	

}
