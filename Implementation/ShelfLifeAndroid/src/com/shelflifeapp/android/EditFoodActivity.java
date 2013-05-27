package com.shelflifeapp.android;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.shelflifeapp.models.MyFood;
import com.shelflifeapp.views.MyFoodListItem;

public class EditFoodActivity extends SherlockActivity  {
	
	private final int SHELF = 0;
	private final int FRIDGE = 1;
	private final int FREEZER = 2;
	
	private MyFood m_myfood;
	private EditText myFoodName;
	private DatePicker purchased;
	private RadioGroup openedBool;
	private TextView openedTitle;
	private DatePicker opened;
	private Spinner state;
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
	    openedBool = (RadioGroup) findViewById(R.id.edit_opened_group);
	    openedTitle = (TextView) findViewById(R.id.edit_opened_title_text);
	    opened = (DatePicker) findViewById(R.id.edit_open_date);
	    state = (Spinner) findViewById(R.id.state_spinner);
	    quantity = (EditText) findViewById(R.id.edit_quantity_text);
	    //notes = (EditText) findViewById(R.id.edit_notes_text);
	    
	    openedBool.setOnCheckedChangeListener(
            new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.edit_opened_true:
                    	openedTitle.setVisibility(View.VISIBLE);
                    	opened.setVisibility(View.VISIBLE);
                        break;
                    case R.id.edit_opened_false:
                    	openedTitle.setVisibility(View.GONE);
                    	opened.setVisibility(View.GONE);                    
                        break;
                }
            }
        });
	    
	    if(m_myfood != null){   	
	    	if(m_myfood.getName() != null){
	    		myFoodName.setText(m_myfood.getName());
	    	}
	    	if(m_myfood.getPurchaseDate() != null){
	    		purchased.updateDate(m_myfood.getPurchaseDate().get(Calendar.YEAR), 
	    				m_myfood.getPurchaseDate().get(Calendar.MONTH), 
	    				m_myfood.getPurchaseDate().get(Calendar.DAY_OF_MONTH));
	    	}
	    	if(m_myfood.getOpenDate() != null){
	    		openedBool.check(R.id.edit_opened_true);
	    		openedTitle.setVisibility(View.VISIBLE);
	    		opened.setVisibility(View.VISIBLE);
	    		opened.updateDate(m_myfood.getOpenDate().get(Calendar.YEAR), 
	    				m_myfood.getOpenDate().get(Calendar.MONTH), 
	    				m_myfood.getOpenDate().get(Calendar.DAY_OF_MONTH));
	    	}else{
	    		openedBool.check(R.id.edit_opened_false);
	    		openedTitle.setVisibility(View.GONE);
	    		opened.setVisibility(View.GONE);
	    	}
	    	if(m_myfood.getState() != null){
	    		String[] states = this.getResources().getStringArray(R.array.state_list);
	    		if(MyFood.SHELF_UNOPENED.equals(m_myfood.getState()) || 
	    				MyFood.SHELF_OPENED.equals(m_myfood.getState())){
	    			state.setSelection(SHELF);
	    		}else if(MyFood.FRIDGE_UNOPENED.equals(m_myfood.getState()) || 
	    				MyFood.FRIDGE_OPENED.equals(m_myfood.getState())){
	    			state.setSelection(FRIDGE);
	    		}else if(MyFood.FREEZER_UNOPENED.equals(m_myfood.getState()) || 
	    				MyFood.FREEZER_OPENED.equals(m_myfood.getState())){
	    			state.setSelection(FREEZER);
	    		}
	    	}
	    	quantity.setText("" + m_myfood.getQuantity());
	    	if(m_myfood.getNotes() != null){
	    		//notes.setText(m_myfood.getNotes());
	    	}
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
	        	final Calendar purchasedCal = Calendar.getInstance();
	        	int purchaseDay = purchased.getDayOfMonth();
	        	int purchaseMonth = purchased.getMonth();
	        	int purchaseYear = purchased.getYear();
	        	purchasedCal.set(purchaseYear, purchaseMonth, purchaseDay);
	        	
	        	if(myFoodName.getText() != null){
	        		m_myfood.setName(myFoodName.getText().toString());
	        	}else{
	        		m_myfood.setName(null);
	        	}
	        	m_myfood.setPurchaseDate(purchasedCal);
	        	
	        	if(openedBool.getCheckedRadioButtonId() == R.id.edit_opened_true){
	        		final Calendar openedCal = Calendar.getInstance();
		        	int openedDay = opened.getDayOfMonth();
		        	int openedMonth = opened.getMonth();
		        	int openedYear = opened.getYear();
		        	openedCal.set(openedYear, openedMonth, openedDay);
	        		m_myfood.setOpenDate(openedCal);
	        		
	        		if(state.getSelectedItemPosition() == SHELF){
	        			m_myfood.setState(MyFood.SHELF_OPENED);
	        		}else if(state.getSelectedItemPosition() == FRIDGE){
	        			m_myfood.setState(MyFood.FRIDGE_OPENED);
	        		}else if(state.getSelectedItemPosition() == FREEZER){
	        			m_myfood.setState(MyFood.FREEZER_OPENED);
	        		}
	        	}else{
	        		m_myfood.setOpenDate(null);
	        		if(state.getSelectedItemPosition() == SHELF){
	        			m_myfood.setState(MyFood.SHELF_UNOPENED);
	        		}else if(state.getSelectedItemPosition() == FRIDGE){
	        			m_myfood.setState(MyFood.FRIDGE_UNOPENED);
	        		}else if(state.getSelectedItemPosition() == FREEZER){
	        			m_myfood.setState(MyFood.FREEZER_UNOPENED);
	        		}
	        	}
	        	
	        	if(quantity.getText() != null){
	        		m_myfood.setQuantity(Integer.parseInt(quantity.getText().toString()));
	        	}else{
	        		m_myfood.setQuantity(0);
	        	}
	        	/*if(notes.getText() != null){
	        		m_myfood.setNotes(notes.getText().toString());
	        	}else{
	        		m_myfood.setNotes(null);
	        	}*/
	        	Intent i = new Intent(this, MyFoodDetails.class);
	    		i.putExtra("myfood", m_myfood);
	    		startActivity(i);
	            return true;	
	    	case android.R.id.home:
	    		finish();
	    		return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    
    
}
