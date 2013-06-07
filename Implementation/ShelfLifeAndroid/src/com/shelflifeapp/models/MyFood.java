package com.shelflifeapp.models;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class MyFood extends Food
{
	public static final String SHELF_UNOPENED = "Shelf, Unopened";
	public static final String SHELF_OPENED = "Shelf, Opened";
	public static final String FRIDGE_UNOPENED = "Fridge, Unopened";
	public static final String FRIDGE_OPENED = "Fridge, Opened";
	public static final String FREEZER_UNOPENED = "Freezer, Unopened";
	public static final String FREEZER_OPENED = "Freezer, Opened";
	
	public static final String LOC_NONE = "none";
	public static final String LOC_FREEZER = "Freezer";
	public static final String LOC_FRIDGE = "Fridge";
	public static final String LOC_SHELF = "Shelf";
	
	public static final String STATE_OPENED = "Opened";
	public static final String STATE_UNOPENED = "Unopened";
	
	private Calendar purchaseDate;
	private Calendar openDate;
	private int quantity;
	private String notes;
	private Bitmap picture;
	private String state;
	private String state_opened = STATE_UNOPENED;
	private String loc = LOC_SHELF;
	
	public MyFood(int id, 
			String name, 
			Category category, 
			ExpirationData expirationData, 
			String tips, 
			String loc, 
			String state_op, 
			Calendar purchaseDate,
			Calendar openDate,
			int quantity,
			String notes,
			Bitmap picture)
	{
		super(id, name, category, expirationData, tips);
		this.setState_opened(state_op);
		this.setLoc(loc);
		this.setStateFromStateLoc(state_op, loc);
		this.setPurchaseDate(purchaseDate);
		this.setOpenDate(openDate);
		this.setQuantity(quantity);
		this.setNotes(notes);
		this.setPicture(picture);
	}
	
	public MyFood(int id, String name, Category category, 
			ExpirationData expirationData, String tips, String state, 
			Calendar purchaseDate, Calendar openDate, int quantity, 
			String notes, Bitmap picture)
	{
		super(id, name, category, expirationData, tips);
		this.setState(state);
		this.setPurchaseDate(purchaseDate);
		this.setOpenDate(openDate);
		this.setQuantity(quantity);
		this.setNotes(notes);
		this.setPicture(picture);
		setLocState(state);
	}
		
	private void setLocState(String state)
	{
		if (state == null)
			return;
		
		if (state.equals(SHELF_OPENED))
		{
			loc = LOC_SHELF;
			state_opened = STATE_OPENED;
		}
		else if (state.equals(SHELF_UNOPENED))
		{
			loc = LOC_SHELF;
			state_opened = STATE_UNOPENED;
		}
		else if (state.equals(FRIDGE_OPENED))
		{
			loc = LOC_FRIDGE;
			state_opened = STATE_OPENED;
		}
		else if (state.equals(FRIDGE_UNOPENED))
		{
			loc = LOC_FRIDGE;
			state_opened = STATE_UNOPENED;
		}
		else if (state.equals(FREEZER_OPENED))
		{
			loc = LOC_FREEZER;
			state_opened = STATE_OPENED;
		}
		else if (state.equals(FREEZER_UNOPENED))
		{
			loc = LOC_FREEZER;
			state_opened = STATE_UNOPENED;
		}
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
		setLocState(state);
	}
	
	public void setStateFromStateLoc(String state_op, String loc)
	{
		state = (loc + ", " + state_op);
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

	public Bitmap getPicture() {
		return picture;
	}

	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}

	public int getExpirationDaysLeft()
	{
		int initialDays;
		int openDays;
		int unopenedDaysLeft;
		int daysLeft;
		Calendar currentDate = Calendar.getInstance();
		
		Log.d("MY_FOOD", "State: " + state + ", PurchaseDate: " + (purchaseDate == null));
		
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
	
	public byte[] setPictureToByteArray(Bitmap photo){
		if(photo != null){
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
	        byte[] byteArray = stream.toByteArray();
	        return byteArray;
		}
		return null;
	}
	
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		super.writeToParcel(arg0, arg1);
		byte[] byte_array = setPictureToByteArray(this.picture);
		int length = 0;
		if(byte_array != null){
			length = byte_array.length;
		}
		Log.d("mgrap", "Byte Array: " + byte_array);
		arg0.writeSerializable(purchaseDate);
		arg0.writeSerializable(openDate);
		arg0.writeString(state);
		arg0.writeString(loc);
		arg0.writeString(state_opened);
		arg0.writeInt(quantity);
		arg0.writeString(notes);
		arg0.writeInt(length);
		arg0.writeByteArray(byte_array);
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
        int length = 0;
        
        purchaseDate = (Calendar) in.readSerializable();
        openDate = (Calendar) in.readSerializable();
        state = in.readString();
        loc = in.readString();
        state_opened = in.readString();
        quantity = in.readInt();
        notes = in.readString();
        length = in.readInt();
        if(length > 0){
	        byte[] _byte = new byte[length];
	        in.readByteArray(_byte);
	        if(_byte != null){
	        	picture = BitmapFactory.decodeByteArray(_byte, 0, length);
	        }
        }
        
        
    }

	public void setState_opened(String state_opened) {
		this.state_opened = state_opened;
		setStateFromStateLoc(state_opened, loc);
	}

	public String getState_opened() {
		return state_opened;
	}

	public void setLoc(String loc) {
		this.loc = loc;
		setStateFromStateLoc(state_opened, loc);
	}

	public String getLoc() {
		return loc;
	}
	
	public static MyFood getEmptyFoodFromMyFood(MyFood m)
	{
		return new MyFood(m.getId(), 
				m.getName(), 
				m.getCategory(), 
				m.getExpirationData(), 
				m.getTips(),
				LOC_NONE, STATE_UNOPENED, Calendar.getInstance(), null, 0, "", null);
	}
}