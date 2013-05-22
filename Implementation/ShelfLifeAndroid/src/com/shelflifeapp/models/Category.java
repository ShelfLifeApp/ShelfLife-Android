package com.shelflifeapp.models;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable{
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		//dest.writeParcelable(icon, 0);
		// write Drawable ???
	}
	
	public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
    
    private Category(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }
}
