package com.shelflifeapp.database;

import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.shelflifeapp.models.Category;
import com.shelflifeapp.models.ExpirationData;
import com.shelflifeapp.models.MyFood;
import com.shelflifeapp.views.MyFoodListItem;

public class MyFoodCursorAdapter extends CursorAdapter{

	public MyFoodCursorAdapter(Context context, Cursor foodCursor, int flags) {
		super(context, foodCursor, flags);
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		//int id = arg2.getInt(FoodTable.FOOD_COL_ID); 
		String name = arg2.getString(FoodTable.FOOD_COL_NAME);
		int catId = arg2.getInt(FoodTable.FOOD_COL_CATEGORY);
		int shelf_u = arg2.getInt(FoodTable.FOOD_COL_SHELF_U);
		int shelf_o = arg2.getInt(FoodTable.FOOD_COL_SHELF_O);
		int fridge_u = arg2.getInt(FoodTable.FOOD_COL_FRIDGE_U);
		int fridge_o = arg2.getInt(FoodTable.FOOD_COL_FRIDGE_O);
		int freezer_u = arg2.getInt(FoodTable.FOOD_COL_FREEZER_U);
		int freezer_o = arg2.getInt(FoodTable.FOOD_COL_FREEZER_O);
		String tips = arg2.getString(FoodTable.FOOD_COL_TIPS);
		int id = arg2.getInt(MyFoodTable.FOOD_COL_ID + FoodTable.FOOD_COL_TIPS + 1);
		int foodid  = arg2.getInt(MyFoodTable.FOOD_COL_FOODID + FoodTable.FOOD_COL_TIPS + 1);
		String purchased  = arg2.getString(MyFoodTable.FOOD_COL_PURCHASED + FoodTable.FOOD_COL_TIPS + 1);
		String opened  = arg2.getString(MyFoodTable.FOOD_COL_OPENED + FoodTable.FOOD_COL_TIPS + 1);
		int quantity  = arg2.getInt(MyFoodTable.FOOD_COL_QUANTITY + FoodTable.FOOD_COL_TIPS + 1);
		String notes  = arg2.getString(MyFoodTable.FOOD_COL_NOTES + FoodTable.FOOD_COL_TIPS + 1);
		
		MyFood food = new MyFood(id, name, new Category(), 
				new ExpirationData(1, 1, 3, 4, 5, 6), 
				tips, MyFood.SHELF_UNOPENED, MyFood.convertStringToDate(purchased), MyFood.convertStringToDate(opened), quantity, notes, null);		
		MyFoodListItem listItem = (MyFoodListItem) arg0;
		listItem.setMyFood(food);		
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		//int id = arg1.getInt(FoodTable.FOOD_COL_ID); 
		String name = arg1.getString(FoodTable.FOOD_COL_NAME);
		int catId = arg1.getInt(FoodTable.FOOD_COL_CATEGORY);
		int shelf_u = arg1.getInt(FoodTable.FOOD_COL_SHELF_U);
		int shelf_o = arg1.getInt(FoodTable.FOOD_COL_SHELF_O);
		int fridge_u = arg1.getInt(FoodTable.FOOD_COL_FRIDGE_U);
		int fridge_o = arg1.getInt(FoodTable.FOOD_COL_FRIDGE_O);
		int freezer_u = arg1.getInt(FoodTable.FOOD_COL_FREEZER_U);
		int freezer_o = arg1.getInt(FoodTable.FOOD_COL_FREEZER_O);
		String tips = arg1.getString(FoodTable.FOOD_COL_TIPS);
		int id = arg1.getInt(MyFoodTable.FOOD_COL_ID + FoodTable.FOOD_COL_TIPS + 1);
		int foodid  = arg1.getInt(MyFoodTable.FOOD_COL_FOODID + FoodTable.FOOD_COL_TIPS + 1);
		String purchased  = arg1.getString(MyFoodTable.FOOD_COL_PURCHASED + FoodTable.FOOD_COL_TIPS + 1);
		String opened  = arg1.getString(MyFoodTable.FOOD_COL_OPENED + FoodTable.FOOD_COL_TIPS + 1);
		int quantity  = arg1.getInt(MyFoodTable.FOOD_COL_QUANTITY + FoodTable.FOOD_COL_TIPS + 1);
		String notes  = arg1.getString(MyFoodTable.FOOD_COL_NOTES + FoodTable.FOOD_COL_TIPS + 1);

		Log.d("mgrap", "id: " + id);
		Log.d("mgrap", "foodid: " + foodid);
		Log.d("mgrap", "purchased: " + purchased);
		Log.d("mgrap", "opened: " + opened);
		Log.d("mgrap", "quantity: " + quantity);
		Log.d("mgrap", "notes: " + notes);
		
		MyFood food = new MyFood(id, name, new Category(), 
				new ExpirationData(1, 2, 3, 4, 5, 6), 
				tips, MyFood.SHELF_UNOPENED, MyFood.convertStringToDate(purchased), MyFood.convertStringToDate(opened), quantity, notes, null);
		MyFoodListItem foodView = new MyFoodListItem(arg0, food);
		return foodView;
	}
}
