package com.shelflifeapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.shelflifeapp.models.Food;
import com.shelflifeapp.models.MyFood;

public class MyFoodDetails extends SherlockActivity 
{
	private MyFood m_myfood;
	private TextView myFoodName;
	private TextView purchased;
	private TextView opened;
	private TextView quantity;
	private TextView notes;
	private TextView daysLeft;
	private TextView shelfOpened;
	private TextView shelfUnopened;
	private TextView fridgeOpened;
	private TextView fridgeUnopened;
	private TextView freezerOpened;
	private TextView freezerUnopened;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfood_details);
        
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        Bundle foodBundle = this.getIntent().getExtras();
	    if(foodBundle == null){
			Log.d("shelflife", "Bundle is null");
		}else{
			m_myfood = foodBundle.getParcelable("myfood");
			if(m_myfood == null){
				Log.d("shelflife", "food is null");
			}			
		}
        
	    myFoodName = (TextView) findViewById(R.id.myfood_name_text);
	    shelfOpened = (TextView) findViewById(R.id.row2_shelf_opened);
	    shelfUnopened = (TextView) findViewById(R.id.row2_shelf_unopened);
	    fridgeOpened = (TextView) findViewById(R.id.row3_fridge_opened);
	    fridgeUnopened = (TextView) findViewById(R.id.row3_fridge_unopened);
	    freezerOpened = (TextView) findViewById(R.id.row4_freezer_opened);
	    freezerUnopened = (TextView) findViewById(R.id.row4_freezer_unopened);
	    purchased = (TextView) findViewById(R.id.myfood_purchased_text);
	    opened = (TextView) findViewById(R.id.myfood_opened_text);
	    quantity = (TextView) findViewById(R.id.myfood_quantity_text);
	    notes = (TextView) findViewById(R.id.myfood_notes_text);
	    daysLeft = (TextView) findViewById(R.id.myfood_days_left_num);
	    
	    if(m_myfood != null){
	    	myFoodName.setText(m_myfood.getName());
	    	shelfOpened.setText("" + m_myfood.getExpirationData().getShelfOpened());
		    shelfUnopened.setText("" + m_myfood.getExpirationData().getShelfUnopened());
		    fridgeOpened.setText("" + m_myfood.getExpirationData().getFridgeOpened());
		    fridgeUnopened.setText("" + m_myfood.getExpirationData().getFridgeUnopened());
		    freezerOpened.setText("" + m_myfood.getExpirationData().getFreezerOpened());
		    freezerUnopened.setText("" + m_myfood.getExpirationData().getFreezerUnopened());
		    purchased.setText("" + m_myfood.getPurchaseDate().MONTH + "/" + 
		    		m_myfood.getPurchaseDate().DAY_OF_MONTH + "/" + 
		    		m_myfood.getPurchaseDate().YEAR);
		    opened.setText("" + m_myfood.getOpenDate().MONTH + "/" + 
		    		m_myfood.getOpenDate().DAY_OF_MONTH + "/" + 
		    		m_myfood.getOpenDate().YEAR);
		    quantity.setText("" + m_myfood.getQuantity());
		    notes.setText("" + m_myfood.getNotes());
		    if(!"unknown".equals(m_myfood.getExpirationDaysLeft())){
		    	daysLeft.setText("" + m_myfood.getExpirationDaysLeft());
		    }else{
		    	daysLeft.setText("");
		    }
	    }
	    else{
	    	Toast.makeText(this, "My Food is null", Toast.LENGTH_LONG).show();
	    }
    }
    
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
		MenuInflater inflater = this.getSupportMenuInflater();
		inflater.inflate(R.menu.myfood_actionbar, menu);
		return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) 
	    {
	        case R.id.menu_edit:
	        	Toast.makeText(this, "Edit Selected", Toast.LENGTH_LONG).show();
	            return true;	
	    	case android.R.id.home:
	    		finish();
	    		return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}