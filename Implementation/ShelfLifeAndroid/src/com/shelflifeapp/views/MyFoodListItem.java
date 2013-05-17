package com.shelflifeapp.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shelflifeapp.android.R;
import com.shelflifeapp.android.models.Food;
import com.shelflifeapp.android.models.MyFood;

public class MyFoodListItem extends LinearLayout
{
	private TextView m_vwFood;
	private TextView m_vwState;
	private TextView m_vwExpiration;
	private ImageView m_vwIcon;
	private ImageView m_vwIndicator;
	
	private int ICON_INDICATOR = R.drawable.icon_indicator;
	
	private MyFood m_myFood;
	
	public MyFoodListItem(Context context, MyFood myFood)
	{
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.listitem_myfood, this, true);
		
		m_vwFood = (TextView)findViewById(R.id.listitem_myfood_name);
		m_vwState = (TextView) findViewById(R.id.listitem_myfood_state);
		m_vwExpiration = (TextView) findViewById(R.id.listitem_myfood_expiration);
		m_vwIcon = (ImageView) findViewById(R.id.listitem_myfood_icon);
		m_vwIndicator = (ImageView) findViewById(R.id.listitem_myfood_indicator);
		
		setMyFood(m_myFood);
	}
	
	public void setMyFood(MyFood food)
	{
		m_myFood = food;
		displayFood(m_myFood);
	}
	
	private void displayFood(MyFood myFood)
	{
		if (myFood != null)
		{
			m_vwFood.setText(myFood.getName());
			m_vwIcon.setImageResource(myFood.getId());
			m_vwIndicator.setImageResource(ICON_INDICATOR);
			m_vwState.setText(myFood.getState().toString());
			m_vwExpiration.setText(myFood.getExpirationDaysLeft());
		}
	}
	
}