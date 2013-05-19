package com.shelflifeapp.models;

import android.graphics.drawable.Drawable;

public class Category {
	private int id;
	private String name;
	private Drawable icon;
	
	public Category(){
		this.id = 0;
		this.name = "Temp";
		this.icon = null;
	}
	
	public Category(int id, String name, Drawable icon){
		this.id = id;
		this.name = name;
		this.icon = icon;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public Drawable getIcon(){
		return this.icon;
	}
}
