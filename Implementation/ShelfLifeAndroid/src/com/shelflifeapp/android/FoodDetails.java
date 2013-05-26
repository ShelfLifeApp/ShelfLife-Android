package com.shelflifeapp.android;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.slewson.simpleupc.SimpleUpcApi;
import com.slewson.simpleupc.SimpleUpcResponse;

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
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_details);
        
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
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
