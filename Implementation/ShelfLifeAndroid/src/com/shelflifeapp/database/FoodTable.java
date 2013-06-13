package com.shelflifeapp.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FoodTable {
	/** Joke table in the database. */
	public static final String DATABASE_TABLE_FOOD = "food_table";
	
	/** Joke table column names and IDs for database access. */
	public static final String FOOD_KEY_ID = "_id";
	public static final int FOOD_COL_ID = 0;
	
	public static final String FOOD_KEY_NAME = "name";
	public static final int FOOD_COL_NAME = FOOD_COL_ID + 1;
	
	public static final String FOOD_KEY_CATEGORY = "catId";
	public static final int FOOD_COL_CATEGORY = FOOD_COL_ID + 2;
	
	public static final String FOOD_KEY_SHELF_U = "shelf_u";
	public static final int FOOD_COL_SHELF_U = FOOD_COL_ID + 3;
	
	public static final String FOOD_KEY_SHELF_O = "shelf_o";
	public static final int FOOD_COL_SHELF_O = FOOD_COL_ID + 4;
	
	public static final String FOOD_KEY_FRIDGE_U = "fridge_u";
	public static final int FOOD_COL_FRIDGE_U = FOOD_COL_ID + 5;
	
	public static final String FOOD_KEY_FRIDGE_O = "fridge_o";
	public static final int FOOD_COL_FRIDGE_O = FOOD_COL_ID + 6;
	
	public static final String FOOD_KEY_FREEZER_U = "freezer_u";
	public static final int FOOD_COL_FREEZER_U = FOOD_COL_ID + 7;
	
	public static final String FOOD_KEY_FREEZER_O = "freezer_o";
	public static final int FOOD_COL_FREEZER_O = FOOD_COL_ID + 8;
	
	public static final String FOOD_KEY_TIPS = "tips";
	public static final int FOOD_COL_TIPS = FOOD_COL_ID + 9;
	
	/** SQLite database creation statement. Auto-increments IDs of inserted 
	 * jokes. Joke IDs are set after insertion into the database. */
	public static final String DATABASE_CREATE = "create table " + 
			DATABASE_TABLE_FOOD + " (" + 
			FOOD_KEY_ID + " integer primary key autoincrement, " + 
			FOOD_KEY_NAME	+ " text, " +
			FOOD_KEY_CATEGORY + " integer, " + 
			FOOD_KEY_SHELF_U + " integer, " +
			FOOD_KEY_SHELF_O + " integer, " +
			FOOD_KEY_FRIDGE_U + " integer, " +
			FOOD_KEY_FRIDGE_O + " integer, " +
			FOOD_KEY_FREEZER_U + " integer, " +
			FOOD_KEY_FREEZER_O + " integer, " +
			FOOD_KEY_TIPS + " text);";
	
	/** SQLite database table removal statement. Only used if upgrading 
	 * database. */
	public static final String DATABASE_DROP = "drop table if exists " + 
			DATABASE_TABLE_FOOD;
	
	/**
	 * Initializes the database.
	 * 
	 * @param database
	 * 				The database to initialize.	
	 */
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	/**
	 * Upgrades the database to a new version.
	 * 
	 * @param database
	 * 					The database to upgrade.
	 * @param oldVersion
	 * 					The old version of the database.
	 * @param newVersion
	 * 					The new version of the database.
	 */
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, 
			int newVersion)
	{
		Log.w(FoodTable.class.getName(), "updating database...");
		database.execSQL(DATABASE_DROP);
		onCreate(database);
	}
}
