package com.shelflife.android.models;

import java.util.Calendar;
import java.util.Date;

import android.graphics.drawable.Drawable;

public class MyFood extends Food
{
	private Date purchaseDate;
	private Date openDate;
	private int quantity;
	private String notes;
	private Drawable picture;
	private State state;
	
	public enum State {
		SHELF_UNOPENED, SHELF_OPENED,
		FRIDGE_UNOPENED, FRIDGE_OPENED,
		FREEZER_UNOPENED, FREEZER_OPENED
	}
	
	public MyFood(int id, String name, Category category, 
			ExpirationData expirationData, String tips, State state, Date purchaseDate)
	{
		super(id, name, category, expirationData, tips);
		this.setState(state);
		this.setPurchaseDate(purchaseDate);
		this.setOpenDate(openDate);
		this.setQuantity(quantity);
		this.setNotes(notes);
		this.setPicture(picture);
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
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
		if(state == State.SHELF_UNOPENED){
			initialDays = this.getExpirationData().getShelfUnopened();
		}else if(state == State.SHELF_OPENED){
			initialDays = this.getExpirationData().getShelfOpened();
		}else if(state == State.FRIDGE_UNOPENED){
			initialDays = this.getExpirationData().getFridgeUnopened();
		}else if(state == State.FRIDGE_OPENED){
			initialDays = this.getExpirationData().getFridgeOpened();
		}else if(state == State.FREEZER_UNOPENED){
			initialDays = this.getExpirationData().getFreezerUnopened();
		}else if(state == State.FREEZER_OPENED){
			initialDays = this.getExpirationData().getFreezerOpened();
		}
		
		//return initialDays - daysUsed;
		return "3 days";
	}
	
	
}
