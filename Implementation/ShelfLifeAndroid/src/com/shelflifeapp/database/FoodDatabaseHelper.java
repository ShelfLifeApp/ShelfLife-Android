package com.shelflifeapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class FoodDatabaseHelper extends SQLiteOpenHelper {
	/** The name of the database. */
	public static final String DATABASE_NAME = "fooddatabase.db";
	
	/** The starting database version. */
	public static final int DATABASE_VERSION = 1;
	
	/**
	 * Create a helper object to create, open, and/or manage a database.
	 * 
	 * @param context
	 * 					The application context.
	 * @param name
	 * 					The name of the database.
	 * @param factory
	 * 					Factory used to create a cursor. Set to null for default behavior.
	 * @param version
	 * 					The starting database version.
	 */
	public FoodDatabaseHelper(Context context, String name,
		CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		FoodTable.onCreate(arg0);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		FoodTable.onUpgrade(db, oldVersion, newVersion);
	}
}
