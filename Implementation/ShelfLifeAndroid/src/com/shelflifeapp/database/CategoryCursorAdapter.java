package com.shelflifeapp.database;

import com.shelflifeapp.models.Category;
import com.shelflifeapp.views.CategoryListItem;

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

		Category cat = new Category(id, name, null);
		CategoryListItem listItem = (CategoryListItem) arg0;
		listItem.setCategory(cat);		
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		int id = arg1.getInt(CategoryTable.FOOD_COL_ID);
		String name  = arg1.getString(CategoryTable.FOOD_COL_NAME);

		Category cat = new Category(id, name, null);
		CategoryListItem listItem = new CategoryListItem(arg0, cat);
		listItem.setCategory(cat);
		
		return listItem;
	}
	

}