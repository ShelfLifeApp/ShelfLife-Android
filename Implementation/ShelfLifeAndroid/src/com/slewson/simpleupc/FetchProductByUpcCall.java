package com.slewson.simpleupc;

import org.json.JSONException;
import org.json.JSONObject;

public class FetchProductByUpcCall extends SimpleUpcApiCall
{
	String upc = "";
	String requestBody = null;
	
	public FetchProductByUpcCall(String upc)
	{
		super(SimpleUpcConstants.FETCH_PRODUCT_BY_UPC);
		this.upc = upc;
		requestBody = generateRequestBody();
	}
	
	private String generateRequestBody()
	{
		JSONObject postObject = new JSONObject();
		JSONObject paramsObject = new JSONObject();

		try 
		{
    		paramsObject.put(SimpleUpcConstants.KEY_UPC, upc);
    		postObject.put(SimpleUpcConstants.KEY_AUTH, SimpleUpcConstants.API_KEY);
			postObject.put(SimpleUpcConstants.KEY_METHOD, SimpleUpcConstants.FETCH_PRODUCT_BY_UPC);
			postObject.put(SimpleUpcConstants.KEY_PARAMS, paramsObject);
			postObject.put(SimpleUpcConstants.KEY_RETURN_FORMAT, SimpleUpcConstants.RESPONSE_FORMAT);
			
		} 
		catch (JSONException e) 
		{
			return null;
		}
		
		return postObject.toString();
	}

	@Override
	public String getRequestBody() 
	{
		return requestBody;
	}
}