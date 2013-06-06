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
import android.util.Log;

public class FoodContentProvider extends ContentProvider {

	private FoodDatabaseHelper database;
	
	private static final int FOOD_ALL = 1;
	private static final int MYFOOD_ALL = 2;
	private static final int CATEGORY_ALL = 3;
	private static final int FOOD_BYCAT = 4;
	private static final int MYFOOD_BYCAT = 5;
	private static final int MYFOOD_DELETE = 6;
	private static final int MYFOOD_INSERT = 7;
	private static final int MYFOOD_EDIT = 8;
	private static final int FOOD_BY_KEYWORD = 9;
	private static final int MYFOOD_DELETE_ALL = 10;
	
	private static final String AUTHORITY = "com.shelflifeapp.android.provider";
	
	private static final String FOOD_TABLE = "food_table";
	private static final String MYFOOD_TABLE = "myfood_table";
	private static final String CATEGORY_TABLE = "category_table";
	
	public static final Uri CONTENT_URI_FOOD = Uri.parse("content://" + AUTHORITY + 
			"/" + FOOD_TABLE);
	
	public static final Uri CONTENT_URI_MYFOOD = Uri.parse("content://" + AUTHORITY + 
			"/" + MYFOOD_TABLE);
	
	public static final Uri CONTENT_URI_CATEGORY = Uri.parse("content://" + AUTHORITY + 
			"/" + CATEGORY_TABLE);
	
	private static final UriMatcher sURIMatcher = 
		new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, FOOD_TABLE + "/all", FOOD_ALL);
		sURIMatcher.addURI(AUTHORITY, MYFOOD_TABLE + "/all", MYFOOD_ALL);
		sURIMatcher.addURI(AUTHORITY, CATEGORY_TABLE + "/all", CATEGORY_ALL);
		sURIMatcher.addURI(AUTHORITY, FOOD_TABLE + "/bycat/#", FOOD_BYCAT);
		sURIMatcher.addURI(AUTHORITY, MYFOOD_TABLE + "/bycat/#", MYFOOD_BYCAT);
		sURIMatcher.addURI(AUTHORITY, MYFOOD_TABLE + "/delete/#", MYFOOD_DELETE);
		sURIMatcher.addURI(AUTHORITY, MYFOOD_TABLE + "/delete_all", MYFOOD_DELETE_ALL);
		sURIMatcher.addURI(AUTHORITY, MYFOOD_TABLE + "/insert/#", MYFOOD_INSERT);
		sURIMatcher.addURI(AUTHORITY, MYFOOD_TABLE + "/edit/#", MYFOOD_EDIT);
		sURIMatcher.addURI(AUTHORITY, FOOD_TABLE + "/by_keyword", FOOD_BY_KEYWORD);
	}
	
	@Override
	public boolean onCreate() {
		this.database = new FoodDatabaseHelper(this.getContext(), 
				FoodDatabaseHelper.DATABASE_NAME, null, 
				FoodDatabaseHelper.DATABASE_VERSION);
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		
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
				checkCategoryColumns(projection);
				queryBuilder.setTables(CategoryTable.DATABASE_TABLE_CATEGORY);
				orderBy = CategoryTable.FOOD_KEY_NAME + " ASC";
				selection = null;
				break;
			case MYFOOD_ALL:
				checkMyFoodColumns(projection);
				queryBuilder.setTables(MyFoodTable.DATABASE_TABLE_MYFOOD + ", " 
						+ FoodTable.DATABASE_TABLE_FOOD);
				orderBy = MyFoodTable.DATABASE_TABLE_MYFOOD + "." + MyFoodTable.FOOD_KEY_NAME + " ASC";
				queryBuilder.appendWhere(FoodTable.DATABASE_TABLE_FOOD + "." 
						+ FoodTable.FOOD_KEY_ID + "=" 
						+ MyFoodTable.DATABASE_TABLE_MYFOOD + "." 
						+ MyFoodTable.FOOD_KEY_FOODID);
				break;
			case FOOD_BYCAT:
				checkFoodColumns(projection);
				queryBuilder.setTables(FoodTable.DATABASE_TABLE_FOOD + ", " 
						+ CategoryTable.DATABASE_TABLE_CATEGORY);
				orderBy = FoodTable.DATABASE_TABLE_FOOD + "." 
						+ FoodTable.FOOD_KEY_NAME + " ASC";
				String catId = uri.getLastPathSegment();
				if(catId != null){
					queryBuilder.appendWhere(FoodTable.FOOD_KEY_CATEGORY + "=" 
							+ catId + " and " + FoodTable.FOOD_KEY_CATEGORY + "=" 
							+ CategoryTable.DATABASE_TABLE_CATEGORY + "." + CategoryTable.FOOD_KEY_ID);
				}else{
					queryBuilder.appendWhere(FoodTable.FOOD_KEY_CATEGORY + "=" + CategoryTable.FOOD_KEY_ID);
				}
				break;
			case FOOD_BY_KEYWORD:
				checkFoodColumns(projection);
				queryBuilder.setTables(FoodTable.DATABASE_TABLE_FOOD);
				orderBy = FoodTable.FOOD_KEY_NAME + " ASC";
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		SQLiteDatabase db = this.database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, 
				null, null, orderBy);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}
	
	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase sqlDB = this.database.getWritableDatabase();
		long id = 0;
		int uriType = sURIMatcher.match(uri);
		
		switch(uriType)	{
		case MYFOOD_INSERT:
			id = sqlDB.insert(MyFoodTable.DATABASE_TABLE_MYFOOD, null, values);
			break;			
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		
		return Uri.parse(MYFOOD_TABLE + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase sqlDB = this.database.getWritableDatabase();
		int rowsDeleted = 0;
		/*if(sURIMatcher.match(uri) == MYFOOD_DELETE){
			String id = uri.getLastPathSegment();
			rowsDeleted = sqlDB.delete(MyFoodTable.DATABASE_TABLE_MYFOOD, 
					MyFoodTable.FOOD_KEY_ID + "=" + id, null);
			if(rowsDeleted > 0){
				getContext().getContentResolver().notifyChange(uri, null);
			}
		}*/		
		int uriType = sURIMatcher.match(uri);
		String orderBy = null;
		String where = null;
		switch(uriType) {
			case MYFOOD_DELETE:
				String id = uri.getLastPathSegment();
				where = MyFoodTable.FOOD_KEY_ID + "=" + id;
				break;
			case MYFOOD_DELETE_ALL:
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		rowsDeleted = sqlDB.delete(MyFoodTable.DATABASE_TABLE_MYFOOD, 
				where, null);
		if(rowsDeleted > 0){
			getContext().getContentResolver().notifyChange(uri, null);
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
		if(sURIMatcher.match(uri) == MYFOOD_EDIT){
			String id = uri.getLastPathSegment();			
			rowsUpdated = sqlDB.update(MyFoodTable.DATABASE_TABLE_MYFOOD, values, 
					MyFoodTable.FOOD_KEY_ID + "=" + id, null);
			if(rowsUpdated > 0){
				getContext().getContentResolver().notifyChange(uri, null);
			}
		}
		return rowsUpdated;
	}

	private void checkFoodColumns(String[] projection)
	{
		String[] available = { FoodTable.FOOD_KEY_ID, FoodTable.FOOD_KEY_NAME, 
				FoodTable.FOOD_KEY_CATEGORY, FoodTable.FOOD_KEY_SHELF_U, 
				FoodTable.FOOD_KEY_SHELF_O, FoodTable.FOOD_KEY_FRIDGE_U,
				FoodTable.FOOD_KEY_FRIDGE_O, FoodTable.FOOD_KEY_FREEZER_U,
				FoodTable.FOOD_KEY_FREEZER_O, FoodTable.FOOD_KEY_TIPS,
				FoodTable.DATABASE_TABLE_FOOD + "." + FoodTable.FOOD_KEY_ID, 
				FoodTable.DATABASE_TABLE_FOOD + "." + FoodTable.FOOD_KEY_NAME};
		
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
	
	private void checkMyFoodColumns(String[] projection)
	{
		String[] available = { FoodTable.DATABASE_TABLE_FOOD + "." 
				+ FoodTable.FOOD_KEY_ID, FoodTable.DATABASE_TABLE_FOOD + "." 
				+ FoodTable.FOOD_KEY_NAME, 
				FoodTable.FOOD_KEY_CATEGORY, FoodTable.FOOD_KEY_SHELF_U, 
				FoodTable.FOOD_KEY_SHELF_O, FoodTable.FOOD_KEY_FRIDGE_U,
				FoodTable.FOOD_KEY_FRIDGE_O, FoodTable.FOOD_KEY_FREEZER_U,
				FoodTable.FOOD_KEY_FREEZER_O, FoodTable.FOOD_KEY_TIPS,
				MyFoodTable.DATABASE_TABLE_MYFOOD + "." + MyFoodTable.FOOD_KEY_ID,
				MyFoodTable.DATABASE_TABLE_MYFOOD + "." + MyFoodTable.FOOD_KEY_NAME,
				MyFoodTable.FOOD_KEY_FOODID, MyFoodTable.FOOD_KEY_PURCHASED, 
				MyFoodTable.FOOD_KEY_OPENED, MyFoodTable.FOOD_KEY_STATE, 
				MyFoodTable.FOOD_KEY_QUANTITY, MyFoodTable.FOOD_KEY_PICTURE, 
				MyFoodTable.FOOD_KEY_NOTES};
		
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
