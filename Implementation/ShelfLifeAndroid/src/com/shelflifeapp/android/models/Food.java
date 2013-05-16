package com.shelflifeapp.android.models;

public class Food 
{
	private String name;
	private int iconId;
	
	public Food(String name, int iconId)
	{
		this.name = name;
		this.iconId = iconId;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getIconId()
	{
		return this.iconId;
	}
	
}
