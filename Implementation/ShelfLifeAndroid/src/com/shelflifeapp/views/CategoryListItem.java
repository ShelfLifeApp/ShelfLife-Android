package com.shelflifeapp.views;

import com.shelflifeapp.models.Category;

import com.shelflifeapp.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CategoryListItem extends LinearLayout{
	private TextView m_vwCategory;
	private ImageView m_vwIcon;
	private ImageView m_vwIndicator;
	
	private int ICON_INDICATOR = R.drawable.icon_indicator;
	
	private Category m_category;
	
	public CategoryListItem(Context context, Category category)
	{
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.listitem_food, this, true);
		
		m_vwCategory = (TextView)findViewById(R.id.listitem_food_name);
		m_vwIcon = (ImageView) findViewById(R.id.listitem_food_icon);
		m_vwIndicator = (ImageView) findViewById(R.id.listitem_indicator);
		
		setCategory(category);
	}
	
	public void setCategory(Category category)
	{
		m_category = category;
		displayCategory(m_category);
	}
	
	public Category getCategory(){
		return m_category;
	}
	
	private void displayCategory(Category category)
	{
		if (category != null)
		{
			m_vwCategory.setText(category.getName());
			m_vwIcon.setImageResource(category.getId());
			m_vwIndicator.setImageResource(ICON_INDICATOR);
		}
	}
	
	public int getCategoryId()
	{
		return m_category.getId();
	}
}
