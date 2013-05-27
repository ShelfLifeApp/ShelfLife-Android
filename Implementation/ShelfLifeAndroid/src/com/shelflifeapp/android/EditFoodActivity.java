package com.shelflifeapp.android;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.shelflifeapp.models.MyFood;

public class EditFoodActivity extends SherlockActivity  {
	
	private MyFood m_myfood;
	private EditText myFoodName;
	private DatePicker purchased;
	private DatePicker opened;
	private EditText quantity;
	private EditText notes;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_food);
        
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        Bundle foodBundle = this.getIntent().getExtras();
	    if(foodBundle == null){
			Log.d("shelflife", "Bundle is null");
		}else{
			m_myfood = foodBundle.getParcelable("food");
			if(m_myfood == null){
				Log.d("shelflife", "food is null");
			}			
		}
        
	    myFoodName = (EditText) findViewById(R.id.edit_name_text);
	    purchased = (DatePicker) findViewById(R.id.edit_purchase_date);
	    opened = (DatePicker) findViewById(R.id.edit_open_date);
	    quantity = (EditText) findViewById(R.id.edit_quantity_text);
	    //notes = (EditText) findViewById(R.id.edit_notes_text);
	    
	    if(m_myfood != null){
	    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	    	sdf.setLenient(false);
	    	Calendar cal = Calendar.getInstance();
	    	
	    	Log.d("poop", m_myfood.getName());
	    	Log.d("poop", myFoodName.toString());
	    	
	    	myFoodName.setText(m_myfood.getName());
	    }
	    else{
	    	Toast.makeText(this, "My Food is null", Toast.LENGTH_LONG).show();
	    }
    }
    
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
		MenuInflater inflater = this.getSupportMenuInflater();
		inflater.inflate(R.menu.editmyfood_actionbar, menu);
		return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) 
	    {
	        case R.id.menu_done:
	        	Toast.makeText(this, "Done Selected", Toast.LENGTH_LONG).show();
	            return true;	
	    	case android.R.id.home:
	    		finish();
	    		return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
