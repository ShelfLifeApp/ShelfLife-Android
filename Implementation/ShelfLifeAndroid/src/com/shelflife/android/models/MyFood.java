package com.shelflife.android.models;

import java.util.Date;

public class MyFood extends Food
{
	private String category;
	private String state;
	private ExpirationData expirationData;
	private Date purchaseDate;
	
	public MyFood(String name, int iconId, ExpirationData expirationData, String category, String state, Date purchaseDate)
	{
		super(name, iconId);
		this.setExpirationData(expirationData);
		this.setCategory(category);
		this.setState(state);
		this.setPurchaseDate(purchaseDate);
	}
	
	public MyFood(Food food, ExpirationData expirationData, String category, String state, Date purchaseDate)
	{
		super(food.getName(), food.getIconId());
		this.setExpirationData(expirationData);
		this.setCategory(category);
		this.setState(state);
		this.setPurchaseDate(purchaseDate);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public ExpirationData getExpirationData() {
		return expirationData;
	}

	public void setExpirationData(ExpirationData expirationData) {
		this.expirationData = expirationData;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	
	public String getExpirationDaysLeft()
	{
		return "3 days";
	}
	
	
}
