package com.shelflifeapp.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shelflifeapp.android.R;

public class ShelfLifeListViewHeader extends RelativeLayout 
{
	private TextView m_vwTitle;
	private TextView m_vwSubtitle;
	
	private String mTitle;
	private String mSubtitle;
	
	public ShelfLifeListViewHeader(Context context, String title, String subtitle)
	{
		super(context);
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.listview_header, this, true);
		
		m_vwTitle = (TextView)findViewById(R.id.listview_header_title);
		m_vwSubtitle = (TextView)findViewById(R.id.listview_header_subtitle);
		
		setTitles(title, subtitle);
	}
	
	public void setTitles(String title, String subtitle)
	{
		mTitle = title;
		mSubtitle = subtitle;
		displayTitles(mTitle, mSubtitle);
	}
	
	private void displayTitles(String title, String subtitle)
	{
		if (mTitle != null && mSubtitle != null)
		{
			m_vwTitle.setText(title);
			m_vwSubtitle.setText(subtitle);
		}
	}
}
