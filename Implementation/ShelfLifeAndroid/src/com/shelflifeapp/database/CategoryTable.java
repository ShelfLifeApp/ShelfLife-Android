package com.shelflifeapp.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CategoryTable {
	/** Joke table in the database. */
	public static final String DATABASE_TABLE_CATEGORY = "category_table";
	
	/** Joke table column names and IDs for database access. */
	public static final String FOOD_KEY_ID = "_id";
	public static final int FOOD_COL_ID = 0;
	
	public static final String FOOD_KEY_NAME = "name";
	public static final int FOOD_COL_NAME = FOOD_COL_ID + 1;
	
	public static final String FOOD_KEY_ICON = "icon";
	public static final int FOOD_COL_CATEGORY = FOOD_COL_ID + 2;

	
	/** SQLite database creation statement. Auto-increments IDs of inserted 
	 * jokes. Joke IDs are set after insertion into the database. */
	public static final String DATABASE_CREATE = "create table " + 
			DATABASE_TABLE_CATEGORY + " (" + 
			FOOD_KEY_ID + " integer primary key autoincrement, " + 
			FOOD_KEY_NAME	+ " text, " +
			FOOD_KEY_ICON + " blob);";
	
	/** SQLite database table removal statement. Only used if upgrading 
	 * database. */
	public static final String DATABASE_DROP = "drop table if exists " + 
			DATABASE_TABLE_CATEGORY;
	
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
		Log.w(CategoryTable.class.getName(), "updating database...");
		database.execSQL(DATABASE_DROP);
		onCreate(database);
	}
}
