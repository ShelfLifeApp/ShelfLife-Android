package com.shelflifeapp.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.shelflifeapp.models.Category;
import com.shelflifeapp.models.ExpirationData;
import com.shelflifeapp.models.MyFood;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
	
	public Cursor fetchMyFood(){
		String[] projection = {FoodTable.DATABASE_TABLE_FOOD + "." 
				+ FoodTable.FOOD_KEY_ID, 
				FoodTable.DATABASE_TABLE_FOOD + "." 
				+ FoodTable.FOOD_KEY_NAME,
				FoodTable.FOOD_KEY_CATEGORY,
				FoodTable.FOOD_KEY_SHELF_U,
				FoodTable.FOOD_KEY_SHELF_O,
				FoodTable.FOOD_KEY_FRIDGE_U,
				FoodTable.FOOD_KEY_FRIDGE_O,
				FoodTable.FOOD_KEY_FREEZER_U,
				FoodTable.FOOD_KEY_FREEZER_O,
				FoodTable.FOOD_KEY_TIPS,
				MyFoodTable.DATABASE_TABLE_MYFOOD + "." + MyFoodTable.FOOD_KEY_ID,
				MyFoodTable.DATABASE_TABLE_MYFOOD + "." + MyFoodTable.FOOD_KEY_NAME,
				MyFoodTable.FOOD_KEY_FOODID,
				MyFoodTable.FOOD_KEY_PURCHASED,
				MyFoodTable.FOOD_KEY_OPENED,
				MyFoodTable.FOOD_KEY_STATE,
				MyFoodTable.FOOD_KEY_QUANTITY,
				MyFoodTable.FOOD_KEY_PICTURE,
				MyFoodTable.FOOD_KEY_NOTES};
		Uri uri = Uri.parse("content://com.shelflifeapp.android.provider/myfood_table/all");
		Cursor c = myContext.getContentResolver().query(uri, projection, null, null, null);
		
		return c;
	}
	
	public MyFood cursorToMyFood(Cursor c){
		int catId = c.getInt(FoodTable.FOOD_COL_CATEGORY);
		int shelf_u = c.getInt(FoodTable.FOOD_COL_SHELF_U);
		int shelf_o = c.getInt(FoodTable.FOOD_COL_SHELF_O);
		int fridge_u = c.getInt(FoodTable.FOOD_COL_FRIDGE_U);
		int fridge_o = c.getInt(FoodTable.FOOD_COL_FRIDGE_O);
		int freezer_u = c.getInt(FoodTable.FOOD_COL_FREEZER_U);
		int freezer_o = c.getInt(FoodTable.FOOD_COL_FREEZER_O);
		String tips = c.getString(FoodTable.FOOD_COL_TIPS);
		int id = c.getInt(MyFoodTable.FOOD_COL_ID + FoodTable.FOOD_COL_TIPS + 1);
		String name = c.getString(MyFoodTable.FOOD_COL_NAME + FoodTable.FOOD_COL_TIPS + 1);
		int foodid  = c.getInt(MyFoodTable.FOOD_COL_FOODID + FoodTable.FOOD_COL_TIPS + 1);
		String purchased  = c.getString(MyFoodTable.FOOD_COL_PURCHASED + FoodTable.FOOD_COL_TIPS + 1);
		String opened  = c.getString(MyFoodTable.FOOD_COL_OPENED + FoodTable.FOOD_COL_TIPS + 1);
		String state  = c.getString(MyFoodTable.FOOD_COL_STATE + FoodTable.FOOD_COL_TIPS + 1);
		int quantity  = c.getInt(MyFoodTable.FOOD_COL_QUANTITY + FoodTable.FOOD_COL_TIPS + 1);
		byte[] picture = c.getBlob(MyFoodTable.FOOD_COL_PICTURE + FoodTable.FOOD_COL_TIPS + 1);
		String notes  = c.getString(MyFoodTable.FOOD_COL_NOTES + FoodTable.FOOD_COL_TIPS + 1);
		
		Bitmap bitMap = null;		
		if(picture != null){
			bitMap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
		}
		
		MyFood food = new MyFood(id, name, new Category(), 
				new ExpirationData(shelf_o, shelf_u, fridge_o, fridge_u, 
						freezer_o, freezer_u), 
				tips, state, MyFood.convertStringToDate(purchased), 
				MyFood.convertStringToDate(opened), quantity, notes, bitMap);
		return food;
	}
}
