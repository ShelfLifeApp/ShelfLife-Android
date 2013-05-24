package com.shelflifeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.shelflifeapp.models.Category;
import com.shelflifeapp.models.ExpirationData;

public class Food implements Parcelable
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(id);
		arg0.writeString(name);
		arg0.writeParcelable(category, 0);
		arg0.writeParcelable(expirationData, 0);
		arg0.writeString(tips);		
	}
	
	public static final Parcelable.Creator<Food> CREATOR = new Parcelable.Creator<Food>() {
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        public Food[] newArray(int size) {
            return new Food[size];
        }
    };
    
    public Food(Parcel in) {
        id = in.readInt();
        name = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
        expirationData = in.readParcelable(ExpirationData.class.getClassLoader());
        tips = in.readString();
    }
}