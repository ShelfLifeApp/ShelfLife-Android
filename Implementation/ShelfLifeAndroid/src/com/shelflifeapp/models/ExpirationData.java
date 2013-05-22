package com.shelflifeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ExpirationData implements Parcelable 
{
	private final int NO_DATA = -1;
	
	private int shelfOpened = NO_DATA;
	private int shelfUnopened = NO_DATA;
	
	private int fridgeOpened = NO_DATA;
	private int fridgeUnopened = NO_DATA;
	
	private int freezerOpened = NO_DATA;
	private int freezerUnopened = NO_DATA;
	
	public ExpirationData(int shelfOpened, int shelfUnopened, int fridgeOpened, int fridgeUnopened, int freezerOpened, int freezerUnopened)
	{
		this.setShelfOpened(shelfOpened);
		this.setShelfUnopened(shelfUnopened);
		
		this.setFridgeOpened(fridgeOpened);
		this.setFridgeUnopened(fridgeUnopened);
		
		this.setFreezerOpened(freezerOpened);
		this.setFreezerUnopened(freezerUnopened);
	}

	public int getShelfOpened() {
		return shelfOpened;
	}

	public void setShelfOpened(int shelfOpened) {
		this.shelfOpened = shelfOpened;
	}

	public int getShelfUnopened() {
		return shelfUnopened;
	}

	public void setShelfUnopened(int shelfUnopened) {
		this.shelfUnopened = shelfUnopened;
	}

	public int getFridgeOpened() {
		return fridgeOpened;
	}

	public void setFridgeOpened(int fridgeOpened) {
		this.fridgeOpened = fridgeOpened;
	}

	public int getFridgeUnopened() {
		return fridgeUnopened;
	}

	public void setFridgeUnopened(int fridgeUnopened) {
		this.fridgeUnopened = fridgeUnopened;
	}

	public int getFreezerOpened() {
		return freezerOpened;
	}

	public void setFreezerOpened(int freezerOpened) {
		this.freezerOpened = freezerOpened;
	}

	public int getFreezerUnopened() {
		return freezerUnopened;
	}

	public void setFreezerUnopened(int freezerUnopened) {
		this.freezerUnopened = freezerUnopened;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(shelfUnopened);
		dest.writeInt(shelfOpened);
		dest.writeInt(fridgeUnopened);
		dest.writeInt(fridgeOpened);
		dest.writeInt(freezerUnopened);
		dest.writeInt(freezerOpened);
	}
	
	public static final Parcelable.Creator<ExpirationData> CREATOR = new Parcelable.Creator<ExpirationData>() {
        public ExpirationData createFromParcel(Parcel in) {
            return new ExpirationData(in);
        }

        public ExpirationData[] newArray(int size) {
            return new ExpirationData[size];
        }
    };
    
    private ExpirationData(Parcel in) {
        shelfUnopened = in.readInt();
        shelfOpened = in.readInt();
        fridgeUnopened = in.readInt();
        fridgeOpened = in.readInt();
        freezerUnopened = in.readInt();
        freezerOpened = in.readInt();
    }
}