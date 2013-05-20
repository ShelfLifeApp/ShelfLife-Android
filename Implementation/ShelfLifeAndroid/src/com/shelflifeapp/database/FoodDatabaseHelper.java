package com.shelflifeapp.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class FoodDatabaseHelper extends SQLiteOpenHelper {
	private static String DB_PATH = "data/data/com.shelflifeapp.android/databases/";
	
	/** The name of the database. */
	public static final String DATABASE_NAME = "food_db";
	
	private final Context myContext;
	
	/** The starting database version. */
	public static final int DATABASE_VERSION = 9;
	
	private SQLiteDatabase myDataBase;
	
	/**
	 * Create a helper object to create, open, and/or manage a database.
	 * 
	 * @param context
	 * 					The application context.
	 * @param name
	 * 					The name of the database.
	 * @param factory
	 * 					Factory used to create a cursor. Set to null for 
	 * 					default behavior.
	 * @param version
	 * 					The starting database version.
	 */
	public FoodDatabaseHelper(Context context, String name,
		CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.myContext = context;
		File f = new File(DB_PATH);
		if (!f.exists()) {
		f.mkdir();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		FoodTable.onUpgrade(db, oldVersion, newVersion);
		CategoryTable.onUpgrade(db, oldVersion, newVersion);
		MyFoodTable.onUpgrade(db, oldVersion, newVersion);
	}
	
	 /**
	  * Creates a empty database on the system and rewrites it with your 
	  * own database.
	  */
	public void createDataBase() throws IOException{
	 
		boolean dbExist = databaseExist();
	 
		if(dbExist){
			/* do nothing - database already exist */
		}else{ 
			/*By calling this method and empty database will be created into 
			 * the default system path of your application so we are gonna be 
			 * able to overwrite that database with our database.*/
			this.getReadableDatabase();	 
			try {		 
				copyDataBase();
				this.close(); 
			} catch (IOException e) {	 
				throw new Error("Error copying database");	 
			}
		}	 
	}
	
	public boolean databaseExist()
	{
	    File dbFile = new File(DB_PATH + DATABASE_NAME);
	    return dbFile.exists();
	}
	 
	/**
	  * Check if the database already exist to avoid re-copying the file each 
	  * time you open the application.
	  * 
	  * @return true if it exists, false if it doesn't
	  */
	private boolean checkDataBase(){	 
		SQLiteDatabase checkDB = null;	 
		try{
			String myPath = DB_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, 
					SQLiteDatabase.OPEN_READONLY);	 
		}catch(SQLiteException e){
			//database does't exist yet. 
		}	 
		if(checkDB != null){ 
			checkDB.close(); 
		}
	 
		return checkDB != null ? true : false;
	}
	 
	/**
	  * Copies your database from your local assets-folder to the just 
	  * created empty database in the system folder, from where it can be 
	  * accessed and handled. This is done by transfering bytestream.
	  */
	private void copyDataBase() throws IOException{	
		/* Open your local db as the input stream */
		InputStream myInput = myContext.getAssets().open(DATABASE_NAME);	
		/* Path to the just created empty db */
		String outFileName = DB_PATH + DATABASE_NAME;	 
		/*Open the empty db as the output stream */
		OutputStream myOutput = new FileOutputStream(outFileName);
	
		/*transfer bytes from the inputfile to the outputfile */
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}
		 
		/*Close the streams */
		myOutput.flush();
		myOutput.close();
		myInput.close();	 
	}
	 
	public void openDataBase() throws SQLException{	 
		/* Open the database */
		String myPath = DB_PATH + DATABASE_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, 
				SQLiteDatabase.OPEN_READONLY);	 
	}
	 
	@Override
	public synchronized void close() {	 
		if(myDataBase != null){
			myDataBase.close();
		}
		super.close();	 
	}
	
	public Cursor fetchAllFood() {		 
		Cursor mCursor = myDataBase.query(FoodTable.DATABASE_TABLE_FOOD, 
				   new String[] { FoodTable.FOOD_KEY_ID, FoodTable.FOOD_KEY_NAME },
		     null, null, null, null, null);
		 
		  if (mCursor != null) {
		   mCursor.moveToFirst();
		  }
		  return mCursor;
	}	
	
	public Cursor fetchAllCategories() {		 
		Cursor mCursor = myDataBase.query(CategoryTable.DATABASE_TABLE_CATEGORY, 
				    new String[] { CategoryTable.FOOD_KEY_ID, 
					CategoryTable.FOOD_KEY_NAME, CategoryTable.FOOD_KEY_ICON },
					null, null, null, null, null);
		 
		  if (mCursor != null) {
		   mCursor.moveToFirst();
		  }
		  return mCursor;
	}	
}
