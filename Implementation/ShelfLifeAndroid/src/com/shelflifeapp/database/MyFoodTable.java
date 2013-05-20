package com.shelflifeapp.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MyFoodTable {
	/** Joke table in the database. */
	public static final String DATABASE_TABLE_MYFOOD = "myfood_table";
	
	/** Joke table column names and IDs for database access. */
	public static final String FOOD_KEY_ID = "_id";
	public static final int FOOD_COL_ID = 0;
	
	public static final String FOOD_KEY_FOODID = "foodId";
	public static final int FOOD_COL_FOODID = FOOD_COL_ID + 1;
	
	public static final String FOOD_KEY_PURCHASED = "purchaseDate";
	public static final int FOOD_COL_PURCHASED = FOOD_COL_ID + 2;
	
	public static final String FOOD_KEY_OPENED = "openDate";
	public static final int FOOD_COL_OPENED = FOOD_COL_ID + 3;
	
	public static final String FOOD_KEY_QUANTITY = "quantity";
	public static final int FOOD_COL_QUANTITY = FOOD_COL_ID + 4;
	
	public static final String FOOD_KEY_PICTURE = "picture";
	public static final int FOOD_COL_PICTURE = FOOD_COL_ID + 5;
	
	public static final String FOOD_KEY_NOTES = "notes";
	public static final int FOOD_COL_NOTES = FOOD_COL_ID + 6;
	
	/** SQLite database creation statement. Auto-increments IDs of inserted 
	 * jokes. Joke IDs are set after insertion into the database. */
	public static final String DATABASE_CREATE = "create table " + 
			DATABASE_TABLE_MYFOOD + " (" + 
			FOOD_KEY_ID + " integer primary key autoincrement, " + 
			FOOD_KEY_FOODID	+ " integer not null, " +
			FOOD_KEY_PURCHASED + " text, " + 
			FOOD_KEY_OPENED + " text, " +
			FOOD_KEY_QUANTITY + " integer, " +
			FOOD_KEY_PICTURE + "blob, " +
			FOOD_KEY_NOTES + " text);";
	
	/** SQLite database table removal statement. Only used if upgrading 
	 * database. */
	public static final String DATABASE_DROP = "drop table if exists " + 
			DATABASE_TABLE_MYFOOD;
	
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
		Log.w(MyFoodTable.class.getName(), "updating database...");
		database.execSQL(DATABASE_DROP);
		onCreate(database);
	}
}
