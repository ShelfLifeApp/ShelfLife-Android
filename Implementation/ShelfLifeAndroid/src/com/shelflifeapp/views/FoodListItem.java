package com.shelflifeapp.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shelflifeapp.android.R;
import com.shelflifeapp.android.models.Food;

public class FoodListItem extends LinearLayout
{
	private TextView m_vwFood;
	private ImageView m_vwIcon;
	private ImageView m_vwIndicator;
	
	private int ICON_INDICATOR = R.drawable.icon_indicator;
	
	private Food m_food;
	
	public FoodListItem(Context context, Food food)
	{
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.listitem_food, this, true);
		
		m_vwFood = (TextView)findViewById(R.id.listitem_food_name);
		m_vwIcon = (ImageView) findViewById(R.id.listitem_food_icon);
		m_vwIndicator = (ImageView) findViewById(R.id.listitem_indicator);
		
		setFood(food);
	}
	
	public void setFood(Food food)
	{
		m_food = food;
		displayFood(m_food);
	}
	
	private void displayFood(Food food)
	{
		if (food != null)
		{
			m_vwFood.setText(food.getName());
			m_vwIcon.setImageResource(food.getId());
			m_vwIndicator.setImageResource(ICON_INDICATOR);
		}
	}
	
}
