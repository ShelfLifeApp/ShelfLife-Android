package com.shelflifeapp.models;

import java.util.Calendar;
import java.util.Date;

import android.graphics.drawable.Drawable;

public class MyFood extends Food
{
	public static final String SHELF_UNOPENED = "Shelf, Unopened";
	public static final String SHELF_OPENED = "Shelf, Opened";
	public static final String FRIDGE_UNOPENED = "Fridge, Unopened";
	public static final String FRIDGE_OPENED = "Fridge, Opened";
	public static final String FREEZER_UNOPENED = "Freezer, Unopened";
	public static final String FREEZER_OPENED = "Freezer, Opened";
	
	private Date purchaseDate;
	private Date openDate;
	private int quantity;
	private String notes;
	private Drawable picture;
	private String state;
	
	public MyFood(int id, String name, Category category, 
			ExpirationData expirationData, String tips, String state, Date purchaseDate,
			Date openDate, int quantity, String notes, Drawable picture)
	{
		super(id, name, category, expirationData, tips);
		this.setState(state);
		this.setPurchaseDate(purchaseDate);
		this.setOpenDate(openDate);
		this.setQuantity(quantity);
		this.setNotes(notes);
		this.setPicture(picture);
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

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Drawable getPicture() {
		return picture;
	}

	public void setPicture(Drawable picture) {
		this.picture = picture;
	}

	// TODO: Appropriate Calculations and String Conversion.
	public String getExpirationDaysLeft()
	{
		int initialDays;
		int daysUsed;
		if(state == SHELF_UNOPENED){
			initialDays = this.getExpirationData().getShelfUnopened();
		}else if(state == SHELF_OPENED){
			initialDays = this.getExpirationData().getShelfOpened();
		}else if(state == FRIDGE_UNOPENED){
			initialDays = this.getExpirationData().getFridgeUnopened();
		}else if(state == FRIDGE_OPENED){
			initialDays = this.getExpirationData().getFridgeOpened();
		}else if(state == FREEZER_UNOPENED){
			initialDays = this.getExpirationData().getFreezerUnopened();
		}else if(state == FREEZER_OPENED){
			initialDays = this.getExpirationData().getFreezerOpened();
		}else{
			return "unknown";
		}
		
		//return initialDays - daysUsed;
		return "a million days";
	}
	
	
}
