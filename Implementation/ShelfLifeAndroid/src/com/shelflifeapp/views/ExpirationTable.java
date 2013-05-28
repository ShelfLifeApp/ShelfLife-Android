package com.shelflifeapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.shelflifeapp.android.R;
import com.shelflifeapp.models.ExpirationData;
import com.shelflifeapp.models.Food;

public class ExpirationTable extends LinearLayout
{
	private TextView m_vwShelfOpened;
	private TextView m_vwShelfUnopened;
	
	private TextView m_vwFridgeOpened;
	private TextView m_vwFridgeUnopened;
	
	private TextView m_vwFreezerOpened;
	private TextView m_vwFreezerUnopened;
	
	private Context mContext;
	private ExpirationData mExpirationData;
	
	public ExpirationTable(Context context)
	{
		super(context);
		
		mContext = context;
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.expiration_table, this, true);
		
		m_vwShelfOpened = (TextView) findViewById(R.id.row2_shelf_opened);
		m_vwShelfUnopened = (TextView) findViewById(R.id.row2_shelf_unopened);
		
		m_vwFridgeOpened = (TextView) findViewById(R.id.row3_fridge_opened);
		m_vwFridgeUnopened = (TextView) findViewById(R.id.row3_fridge_unopened);
		
		m_vwFreezerOpened = (TextView) findViewById(R.id.row4_freezer_opened);
		m_vwFreezerUnopened = (TextView) findViewById(R.id.row4_freezer_unopened);
	}
	
	public ExpirationTable(Context context, AttributeSet attrSet)
	{
		super(context, attrSet);
		
		mContext = context;
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.expiration_table, this, true);
		
		m_vwShelfOpened = (TextView) findViewById(R.id.row2_shelf_opened);
		m_vwShelfUnopened = (TextView) findViewById(R.id.row2_shelf_unopened);
		
		m_vwFridgeOpened = (TextView) findViewById(R.id.row3_fridge_opened);
		m_vwFridgeUnopened = (TextView) findViewById(R.id.row3_fridge_unopened);
		
		m_vwFreezerOpened = (TextView) findViewById(R.id.row4_freezer_opened);
		m_vwFreezerUnopened = (TextView) findViewById(R.id.row4_freezer_unopened);
	}

	public void setFood(Food food)
	{
		setExpirationData(food.getExpirationData());
	}
	
	public void setExpirationData(ExpirationData expirationData)
	{
		mExpirationData = expirationData;
		displayExpirationData(expirationData);
	}
	
	private void displayExpirationData(ExpirationData expirationData)
	{
		m_vwShelfOpened.setText(formatData(expirationData.getShelfOpened()));
		m_vwShelfUnopened.setText(formatData(expirationData.getShelfUnopened()));
		
		m_vwFridgeOpened.setText(formatData(expirationData.getFridgeOpened()));
		m_vwFridgeUnopened.setText(formatData(expirationData.getFridgeUnopened()));
		
		m_vwFreezerOpened.setText(formatData(expirationData.getFreezerOpened()));
		m_vwFreezerUnopened.setText(formatData(expirationData.getFreezerUnopened()));
	}
	
	private String formatData(int days)
	{
		if (days == -1)
			return "-";
		else if (days == 0)
			return "<1 day";
		else if (days == 1)
			return "1 day";
		else 
			return days + " days";
	}
	
}
