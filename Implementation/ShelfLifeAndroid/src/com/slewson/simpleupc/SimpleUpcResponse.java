package com.slewson.simpleupc;

import org.json.JSONException;
import org.json.JSONObject;

public class SimpleUpcResponse 
{
	private String json = null;
	private String method = null;
	
	private boolean success = false;
	private boolean usedExternal = false;
	private String brand = null;
	private String manufacturer = null;
	private String container = null;
	private String description = null;
	private int size = 0;
	private String category = null;
	private String units = null;
	private String upc = null;
	private boolean productHasImage = false;
	private boolean productHasNutritionFacts = false;
	
	public SimpleUpcResponse(String method, String json)
	{
		this.json = json;
		this.method = method;
		initializeResponse(json);
	}
	
	private void initializeResponse(String json)
	{
		if ("".equals(json) || json == null)
			return;
		
		try 
		{
			JSONObject response = new JSONObject(json);
			JSONObject results = response.getJSONObject(SimpleUpcConstants.KEY_RESULT);
			
			if (response.has(SimpleUpcConstants.KEY_SUCCESS))
				success = response.getBoolean(SimpleUpcConstants.KEY_SUCCESS);
			
			if (response.has(SimpleUpcConstants.KEY_USED_EXTERNAL))
				usedExternal = response.getBoolean(SimpleUpcConstants.KEY_USED_EXTERNAL);
			
			if (results.has(SimpleUpcConstants.KEY_BRAND))
				brand = results.getString(SimpleUpcConstants.KEY_BRAND);
			
			if (results.has(SimpleUpcConstants.KEY_MANUFACTURER))
				manufacturer = results.getString(SimpleUpcConstants.KEY_MANUFACTURER);
			
			if (results.has(SimpleUpcConstants.KEY_CONTAINER))
				container = results.getString(SimpleUpcConstants.KEY_CONTAINER);
			
			if (results.has(SimpleUpcConstants.KEY_DESCRIPTION))
				description = results.getString(SimpleUpcConstants.KEY_DESCRIPTION);
			
			if (results.has(SimpleUpcConstants.KEY_SIZE))
				size = results.getInt(SimpleUpcConstants.KEY_SIZE);
			
			if (results.has(SimpleUpcConstants.KEY_CATEGORY))
				category = results.getString(SimpleUpcConstants.KEY_CATEGORY);
			
			if (results.has(SimpleUpcConstants.KEY_UNITS))
				units = results.getString(SimpleUpcConstants.KEY_UNITS);
			
			if (results.has(SimpleUpcConstants.KEY_UPC))
				upc = results.getString(SimpleUpcConstants.KEY_UPC);
			
			if (results.has(SimpleUpcConstants.KEY_PRODUCT_HAS_IMAGE))
				productHasImage = results.getBoolean(SimpleUpcConstants.KEY_PRODUCT_HAS_IMAGE);
			
			if (results.has(SimpleUpcConstants.KEY_PRODUCT_HAS_NUTRITION_FACTS))
				productHasNutritionFacts = results.getBoolean(SimpleUpcConstants.KEY_PRODUCT_HAS_NUTRITION_FACTS);
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}
	
	public String getJson() 
	{
		return json;
	}

	public boolean isSuccess() 
	{
		return success;
	}

	public boolean isUsedExternal() 
	{
		return usedExternal;
	}

	public String getBrand() 
	{
		return brand;
	}

	public String getManufacturer() 
	{
		return manufacturer;
	}

	public String getContainer() 
	{
		return container;
	}

	public String getDescription() 
	{
		return description;
	}

	public int getSize() 
	{
		return size;
	}

	public String getCategory() 
	{
		return category;
	}

	public String getUnits() 
	{
		return units;
	}

	public String getUpc() 
	{
		return upc;
	}

	public boolean isProductHasImage() 
	{
		return productHasImage;
	}

	public boolean isProductHasNutritionFacts() 
	{
		return productHasNutritionFacts;
	}
	
	public String getMethod() 
	{
		return method;
	}
}