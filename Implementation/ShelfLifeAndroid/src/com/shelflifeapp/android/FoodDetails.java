package com.shelflifeapp.android;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.shelflifeapp.database.MyFoodTable;
import com.shelflifeapp.models.Food;
import com.shelflifeapp.views.ExpirationTable;

public class FoodDetails extends SherlockActivity
{
	Button addButton; 
	private Food m_food;
	private TextView foodName;
	private TextView foodDetails;
	private TextView shelfOpened;
	private TextView shelfUnopened;
	private TextView fridgeOpened;
	private TextView fridgeUnopened;
	private TextView freezerOpened;
	private TextView freezerUnopened;
	
	private ImageView iconView;
	
	private final String PREF_KEY = "fooddetails";
	private final String PREF_KEY_AGREE = "pref_key_agree";
	public final static String KEY_DATA_AGREE = "key_data_agree";
	private final int KEY_WARNING_AGREE = 0;
	
	private boolean hasAgreed = false;
	
	private SharedPreferences prefs;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_details);
        
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        prefs = this.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        hasAgreed = prefs.getBoolean(PREF_KEY_AGREE, false);
        
        if (!hasAgreed)
        {
        	Intent agreeIntent = new Intent(this, WarningActivity.class);
        	startActivityForResult(agreeIntent, KEY_WARNING_AGREE);
        }
        	
        
        Bundle foodBundle = this.getIntent().getExtras();
	    if(foodBundle == null)
	    {
			Log.d("shelflife", "Bundle is null");
		}
	    else
	    {
			m_food = foodBundle.getParcelable("food");
			if(m_food == null)
			{
				Log.d("shelflife", "food is null");
			}			
		}
        
	    ExpirationTable expirationTable = (ExpirationTable) findViewById(R.id.ExpirationTable);
	    expirationTable.setExpirationData(m_food.getExpirationData());
	    
        addButton = (Button) findViewById(R.id.add_food_button);
        addButton.setOnClickListener(new Button.OnClickListener() 
        {  
			@Override
			public void onClick(View arg0) 
			{
				if(m_food != null)
				{
					Uri uri = Uri.parse("content://com.shelflifeapp.android.provider/myfood_table/insert/" + 0);
					ContentValues cv = new ContentValues();
					cv.put(MyFoodTable.FOOD_KEY_FOODID, m_food.getId());
					cv.put(MyFoodTable.FOOD_KEY_PURCHASED, "2013-05-21");
					Uri idUri = getContentResolver().insert(uri, cv);
					Toast.makeText(FoodDetails.this, m_food.getName() + " Inserted", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(FoodDetails.this, "Insert Failed", Toast.LENGTH_LONG).show();
				}
			}
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) 
		{
			case KEY_WARNING_AGREE:
			{
				if (resultCode == Activity.RESULT_CANCELED)
				{
					Toast.makeText(this, "CANCELLED", Toast.LENGTH_LONG).show();
					prefs.edit().putBoolean(PREF_KEY_AGREE, false).commit();
					this.finish();
				}
				else if (resultCode == Activity.RESULT_OK)
				{
					Toast.makeText(this, "OKAY", Toast.LENGTH_LONG).show();
					boolean agreed = data.getBooleanExtra(KEY_DATA_AGREE, false);
					
					if (agreed)
					{
						prefs.edit().putBoolean(PREF_KEY_AGREE, agreed).commit();
					}
					else
					{
						prefs.edit().putBoolean(PREF_KEY_AGREE, agreed).commit();
						this.finish();
					}
				}	
			}
		}
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) 
	    {
	    	case android.R.id.home:
	    		finish();
	    		return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
