package com.shelflifeapp.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

public class MyFood extends Food
{
	public static final String SHELF_UNOPENED = "Shelf, Unopened";
	public static final String SHELF_OPENED = "Shelf, Opened";
	public static final String FRIDGE_UNOPENED = "Fridge, Unopened";
	public static final String FRIDGE_OPENED = "Fridge, Opened";
	public static final String FREEZER_UNOPENED = "Freezer, Unopened";
	public static final String FREEZER_OPENED = "Freezer, Opened";
	
	private Calendar purchaseDate;
	private Calendar openDate;
	private int quantity;
	private String notes;
	private Drawable picture;
	private String state;
	
	public MyFood(int id, String name, Category category, 
			ExpirationData expirationData, String tips, String state, 
			Calendar purchaseDate, Calendar openDate, int quantity, 
			String notes, Drawable picture)
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

	public Calendar getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Calendar purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Calendar getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Calendar openDate) {
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

	public int getExpirationDaysLeft()
	{
		int initialDays;
		int openDays;
		int unopenedDaysLeft;
		int daysLeft;
		Calendar currentDate = Calendar.getInstance(); 
		Calendar baseDate;
		
		if(state == null || purchaseDate == null){
			return -1;
		}
		
		if(state.equals(SHELF_UNOPENED)){
			initialDays = this.getExpirationData().getShelfUnopened();
			daysLeft = initialDays - diff(currentDate, purchaseDate);
		}else if(state.equals(SHELF_OPENED)){
			initialDays = this.getExpirationData().getShelfUnopened();
			openDays = this.getExpirationData().getShelfOpened();			
			unopenedDaysLeft = initialDays - diff(openDate, purchaseDate);
			if(unopenedDaysLeft < openDays){
				openDays = unopenedDaysLeft;
			}
			daysLeft = openDays - diff(currentDate, openDate);
		}else if(state.equals(FRIDGE_UNOPENED)){
			initialDays = this.getExpirationData().getFridgeUnopened();
			daysLeft = initialDays - diff(currentDate, purchaseDate);
		}else if(state.equals(FRIDGE_OPENED)){
			initialDays = this.getExpirationData().getFridgeUnopened();
			openDays = this.getExpirationData().getFridgeOpened();			
			unopenedDaysLeft = initialDays - diff(openDate, purchaseDate);
			if(unopenedDaysLeft < openDays){
				openDays = unopenedDaysLeft;
			}
			daysLeft = openDays - diff(currentDate, openDate);
		}else if(state.equals(FREEZER_UNOPENED)){
			initialDays = this.getExpirationData().getFreezerUnopened();
			daysLeft = initialDays - diff(currentDate, purchaseDate);
		}else if(state.equals(FREEZER_OPENED)){
			initialDays = this.getExpirationData().getFreezerUnopened();
			openDays = this.getExpirationData().getFreezerOpened();			
			unopenedDaysLeft = initialDays - diff(openDate, purchaseDate);
			if(unopenedDaysLeft < openDays){
				openDays = unopenedDaysLeft;
			}
			daysLeft = openDays - diff(currentDate, openDate);
		}else{
			return -1;
		}
		
		return daysLeft;	
	}
	
	public static Calendar convertStringToDate(String dateStr){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try {
			cal.setTime(sdf.parse(dateStr));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		} catch (NullPointerException e){
			return null;
		}
		return cal;
	}
	
	public static int julianDay(int year, int month, int day) {
		  int a = (14 - month) / 12;
		  int y = year + 4800 - a;
		  int m = month + 12 * a - 3;
		  int jdn = day + (153 * m + 2)/5 + 365*y + y/4 - y/100 + y/400 - 32045;
		  return jdn;
	}
	
	public static int julianDay(Calendar cal) {
		return julianDay(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH)); 
	}

	public static int diff(Calendar cal1, Calendar cal2) {
		  return julianDay(cal1) - julianDay(cal2);
		}
	
	public static int diff(int y1, int m1, int d1, int y2, int m2, int d2) {
	  return julianDay(y1, m1, d1) - julianDay(y2, m2, d2);
	}
	
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		super.writeToParcel(arg0, arg1);
		arg0.writeSerializable(purchaseDate);
		arg0.writeSerializable(openDate);
		arg0.writeString(state);
		arg0.writeInt(quantity);
		arg0.writeString(notes);		
	}
	
	public static final Parcelable.Creator<MyFood> CREATOR = 
			new Parcelable.Creator<MyFood>() {
        public MyFood createFromParcel(Parcel in) {
            return new MyFood(in);
        }

        public MyFood[] newArray(int size) {
            return new MyFood[size];
        }
    };
    
    public MyFood(Parcel in) {
        super(in);
        purchaseDate = (Calendar) in.readSerializable();
        openDate = (Calendar) in.readSerializable();
        state = in.readString();
        quantity = in.readInt();
        notes = in.readString();
    }
	
}
