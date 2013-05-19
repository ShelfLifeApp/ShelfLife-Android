package com.shelflifeapp.database;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class FoodContentProvider extends ContentProvider {
	/** The joke database. */
	private FoodDatabaseHelper database;
	
	/** Values for the URIMatcher. */
	private static final int FOOD_ALL = 1;
	private static final int MYFOOD_ALL = 2;
	private static final int CATEGORY_ALL = 3;
	private static final int FOOD_BYCAT = 4;
	private static final int MYFOOD_BYCAT = 5;
	
	/** The authority for this content provider. */
	private static final String AUTHORITY = "com.shelflifeapp.android.provider";
	
	/** 
	 * The database table to read from and write to, and also the root path 
	 * for use in the URI matcher. This is essentially a label to a 
	 * two-dimensional array in the database filled with rows of jokes
	 * whose columns contain joke data. 
	 */
	private static final String FOOD_TABLE = "food_table";
	private static final String MYFOOD_TABLE = "myfood_table";
	private static final String CATEGORY_TABLE = "category_table";
	
	/** 
	 * This provider's content location. Used by accessing applications to
	 * interact with this provider. 
	 */
	public static final Uri CONTENT_URI_FOOD = Uri.parse("content://" + AUTHORITY + 
			"/" + FOOD_TABLE);
	
	public static final Uri CONTENT_URI_MYFOOD = Uri.parse("content://" + AUTHORITY + 
			"/" + MYFOOD_TABLE);
	
	public static final Uri CONTENT_URI_CATEGORY = Uri.parse("content://" + AUTHORITY + 
			"/" + CATEGORY_TABLE);
	
	/** 
	 * Matches content URIs requested by accessing applications with possible
	 * expected content URI formats to take specific actions in this provider. 
	 */
	private static final UriMatcher sURIMatcher = 
		new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, FOOD_TABLE + "/all", FOOD_ALL);
		sURIMatcher.addURI(AUTHORITY, MYFOOD_TABLE + "/all", MYFOOD_ALL);
		sURIMatcher.addURI(AUTHORITY, CATEGORY_TABLE + "/all", CATEGORY_ALL);
		sURIMatcher.addURI(AUTHORITY, FOOD_TABLE + "/bycat", FOOD_BYCAT);
		sURIMatcher.addURI(AUTHORITY, MYFOOD_TABLE + "/bycat", MYFOOD_BYCAT);
	}
	
	@Override
	public boolean onCreate() {
		this.database = new FoodDatabaseHelper(this.getContext(), 
				FoodDatabaseHelper.DATABASE_NAME, null, 
				FoodDatabaseHelper.DATABASE_VERSION);
		return false;
	}

	/**
	 * Fetches rows from the joke table. Given a specified URI that contains a
	 * filter, returns a list of jokes from the joke table matching that 
	 * filter in the form of a Cursor.
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		 /** Use a helper class to perform a query for us. */
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		
		/** Match the passed-in URI to an expected URI format. */
		int uriType = sURIMatcher.match(uri);
		String orderBy = null;
		switch(uriType) {
			case FOOD_ALL:
				/** Make sure the projection is proper before querying. */
				checkFoodColumns(projection);
				/** Set up helper to query our jokes table. */
				queryBuilder.setTables(FoodTable.DATABASE_TABLE_FOOD);
				orderBy = FoodTable.FOOD_KEY_NAME + " ASC";
				selection = null;
				break;
			case CATEGORY_ALL:
				/** Make sure the projection is proper before querying. */
				checkCategoryColumns(projection);
				/** Set up helper to query our jokes table. */
				queryBuilder.setTables(CategoryTable.DATABASE_TABLE_CATEGORY);
				orderBy = CategoryTable.FOOD_KEY_NAME + " ASC";
				selection = null;
				break;
			case FOOD_BYCAT:
				/** Make sure the projection is proper before querying. */
				checkFoodColumns(projection);
				/** Set up helper to query our jokes table. */
				queryBuilder.setTables(FoodTable.DATABASE_TABLE_FOOD + ", " 
						+ CategoryTable.DATABASE_TABLE_CATEGORY);
				orderBy = FoodTable.FOOD_KEY_NAME + " ASC";
				selection = FoodTable.DATABASE_TABLE_FOOD + "." + FoodTable.FOOD_KEY_CATEGORY + " = " + 
						FoodTable.DATABASE_TABLE_FOOD + "." + CategoryTable.FOOD_KEY_ID;
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		/** Perform the database query. */
		SQLiteDatabase db = this.database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection, null, 
				null, null, orderBy);
		/** Set the cursor to automatically alert listeners for content/view 
		 * refreshing. */
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}
	
	/** We don't really care about this method for this application. */
	@Override
	public String getType(Uri uri) {
		return null;
	}
	
	/**
	 * Inserts a joke into the joke table. Given a specific URI that contains a
	 * joke and the values of that joke, writes a new row in the table filled
	 * with that joke's information and gives the joke a new ID, then returns 
	 * a URI containing the ID of the inserted joke.
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		
		/** Open the database for writing. */
		SQLiteDatabase sqlDB = this.database.getWritableDatabase();
		
		/** Will contain the ID of the inserted joke. */
		long id = 0;
		
		/** Match the passed-in URI to an expected URI format. */
		int uriType = sURIMatcher.match(uri);
		
		switch(uriType)	{
		
		/** 
		 * Expects a joke ID, but we will do nothing with the passed-in ID 
		 * since the database will automatically handle ID assignment and 
		 * incrementation. IMPORTANT: joke ID cannot be set to -1 in passed-in 
		 * URI; -1 is not interpreted as a numerical value by the URIMatcher. 
		 */
		case FOOD_ALL:			
			/** Perform the database insert, placing the joke at the bottom 
			 * of the table. */
			id = sqlDB.insert(FoodTable.DATABASE_TABLE_FOOD, null, values);
			break;			
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		/** Alert any watchers of an underlying data change for content/view 
		 * refreshing. */
		getContext().getContentResolver().notifyChange(uri, null);
		
		return Uri.parse(FOOD_TABLE + "/" + id);
	}

	/**
	 * Removes a row from the joke table. Given a specific URI containing a 
	 * joke ID, removes rows in the table that match the ID and returns the 
	 * number of rows removed. Since IDs are automatically incremented on 
	 * insertion, this will only ever remove a single row from the joke table.
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase sqlDB = this.database.getWritableDatabase();
		int rowsDeleted = 0;
		if(sURIMatcher.match(uri) == FOOD_ALL){
			String id = uri.getLastPathSegment();			
			rowsDeleted = sqlDB.delete(FoodTable.DATABASE_TABLE_FOOD, 
					FoodTable.FOOD_KEY_ID + "=" + id, null);
			if(rowsDeleted > 0){
				getContext().getContentResolver().notifyChange(uri, null);
			}
		}
		return rowsDeleted;
	}

	/**
	 * Updates a row in the joke table. Given a specific URI containing a joke 
	 * ID and the new joke values, updates the values in the row with the 
	 * matching ID in the table. Since IDs are automatically incremented 
	 * on insertion, this will only ever update a single row in the joke table.
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase sqlDB = this.database.getWritableDatabase();
		int rowsUpdated = 0;
		if(sURIMatcher.match(uri) == FOOD_ALL){
			String id = uri.getLastPathSegment();			
			rowsUpdated = sqlDB.update(FoodTable.DATABASE_TABLE_FOOD, values, 
					FoodTable.FOOD_KEY_ID + "=" + id, null);
			if(rowsUpdated > 0){
				getContext().getContentResolver().notifyChange(uri, null);
			}
		}
		return rowsUpdated;
	}

	/**
	 * Verifies the correct set of columns to return data from when performing 
	 * a query.
	 * 
	 * @param projection
	 * 						The set of columns about to be queried.
	 */
	private void checkFoodColumns(String[] projection)
	{
		String[] available = { FoodTable.FOOD_KEY_ID, FoodTable.FOOD_KEY_NAME, 
				FoodTable.FOOD_KEY_CATEGORY, FoodTable.FOOD_KEY_SHELF_U, 
				FoodTable.FOOD_KEY_SHELF_O, FoodTable.FOOD_KEY_FRIDGE_U,
				FoodTable.FOOD_KEY_FRIDGE_O, FoodTable.FOOD_KEY_FREEZER_U,
				FoodTable.FOOD_KEY_FREEZER_O, FoodTable.FOOD_KEY_TIPS};
		
		if(projection != null) {
			HashSet<String> requestedColumns = 
				new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = 
				new HashSet<String>(Arrays.asList(available));
			
			if(!availableColumns.containsAll(requestedColumns))	{
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}
	
	/**
	 * Verifies the correct set of columns to return data from when performing 
	 * a query.
	 * 
	 * @param projection
	 * 						The set of columns about to be queried.
	 */
	private void checkCategoryColumns(String[] projection)
	{
		String[] available = { CategoryTable.FOOD_KEY_ID, CategoryTable.FOOD_KEY_NAME, 
				CategoryTable.FOOD_KEY_ICON};
		
		if(projection != null) {
			HashSet<String> requestedColumns = 
				new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = 
				new HashSet<String>(Arrays.asList(available));
			
			if(!availableColumns.containsAll(requestedColumns))	{
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}
}
