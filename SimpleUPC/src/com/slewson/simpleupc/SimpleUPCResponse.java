package com.slewson.simpleupc;

public class SimpleUPCResponse 
{
	private String mData;
	private int mCode;
	
	public SimpleUPCResponse()
	{
	}
	
	public SimpleUPCResponse(String data, int code)
	{
		mData = data;
		mCode = code;
	}
	
	public String getData()
	{
		return mData;
	}
	
	public int getCode()
	{
		return mCode;
	}
}
