package com.slewson.simpleupc;

public abstract class SimpleUpcApiCall 
{
	public String method;
	
	public SimpleUpcApiCall(String method)
	{
		this.method = method;
	}
	
	public abstract String getRequestBody();
	
	public String getMethod()
	{
		return this.method;
	}
}
