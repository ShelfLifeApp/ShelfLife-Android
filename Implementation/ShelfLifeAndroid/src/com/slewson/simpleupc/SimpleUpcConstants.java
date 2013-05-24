package com.slewson.simpleupc;

public class SimpleUpcConstants 
{
	public static final String POST_URL = "http://api.simpleupc.com/v1.php";
	public static final String API_KEY = "etxXV93BmMwtwlAHTRjbKaOOaF0T2Y";
	
	/* Methods */
	public static final String FETCH_PRODUCT_BY_UPC = "FetchProductByUPC";
	
	/* Params */
	public static final String RESPONSE_FORMAT = "JSON";
	
	/* JSON Request Keys */
	public static final String KEY_AUTH = "auth";
	public static final String KEY_METHOD = "method";
	public static final String KEY_PARAMS = "params";
	public static final String KEY_RETURN_FORMAT = "returnFormat";
	
	/* JSON Response Keys */
	public static final String KEY_SUCCESS = "success";
	public static final String KEY_USED_EXTERNAL = "usedExternal";
	public static final String KEY_RESULT = "result";
	public static final String KEY_BRAND = "brand";
	public static final String KEY_MANUFACTURER = "manufacturer";
	public static final String KEY_CONTAINER = "container";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_SIZE = "size";
	public static final String KEY_CATEGORY = "category";
	public static final String KEY_UNITS = "units";
	public static final String KEY_UPC = "upc";
	public static final String KEY_PRODUCT_HAS_IMAGE = "ProductHasImage";
	public static final String KEY_PRODUCT_HAS_NUTRITION_FACTS = "ProductHasNutritionFacts";
}
