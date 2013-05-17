package com.shelflifeapp.android.models;

import com.shelflifeapp.android.models.Category;
import com.shelflifeapp.android.models.ExpirationData;

public class Food 
{
	private int id;
	private String name;	
	private Category category;
	private ExpirationData expirationData;
	String tips;
	
	public Food(String name, int id)
	{
		this.name = name;
		this.id = id;
	}
	
	public Food(int id, String name, Category cat, ExpirationData ed, String tips){
		this.id = id;
		this.name = name;
		this.category = cat;
		this.expirationData = ed;
		this.tips = tips;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public Category getCategory()
	{
		return this.category;
	}
	
	public ExpirationData getExpirationData()
	{
		return this.expirationData;
	}
	
	public String getTips()
	{
		return this.tips;
	}
	
}