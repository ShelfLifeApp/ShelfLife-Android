package com.shelflifeapp.android;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

public class FoodDetails extends SherlockActivity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_details);
    }
}
