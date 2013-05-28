package com.shelflifeapp.android;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.shelflifeapp.database.MyFoodTable;
import com.shelflifeapp.models.MyFood;
import com.shelflifeapp.views.MyFoodListItem;

public class EditFoodActivity extends SherlockActivity  {
	
	private final static int SHELF = 0;
	private final static int FRIDGE = 1;
	private final static int FREEZER = 2;
	
	public final static int ADD = 3;
	public final static int EDIT = 4;
	
	private MyFood m_myfood;
	private int foodid;
	private int operation;
	
	private EditText myFoodName;
	private Button purchasedButton;
	private RadioGroup openedBool;
	private TextView openedTitle;
	private Button openedButton;
	private Spinner state;
	private EditText quantity;
	private EditText notes;
	
	DateFormat formatDateTime = DateFormat.getDateInstance();
	Calendar purchaseDate = Calendar.getInstance();
	Calendar openDate = Calendar.getInstance();
	private TextView purchaseDateLabel;
	private TextView openDateLabel;
	
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
			m_myfood = foodBundle.getParcelable("myfood");
			if(m_myfood == null){
				Log.d("shelflife", "food is null");
			}
			foodid = foodBundle.getInt("foodid");
			if(foodid == 0){
				Log.d("shelflife", "foodid is 0");
			}
			operation = foodBundle.getInt("operation");
			if(operation != ADD && operation != EDIT){
				Log.d("shelflife", "operation error");
			}
		}
        
	    purchaseDateLabel = (TextView) findViewById(R.id.purchase_date_label);
	    openDateLabel = (TextView) findViewById(R.id.open_date_label);
        updateLabel();
	    
	    myFoodName = (EditText) findViewById(R.id.edit_name_text);
	    purchasedButton = (Button) findViewById(R.id.edit_purchase_button);
	    openedBool = (RadioGroup) findViewById(R.id.edit_opened_group);
	    openedTitle = (TextView) findViewById(R.id.edit_opened_title_text);
	    openedButton = (Button) findViewById(R.id.edit_open_button);
	    state = (Spinner) findViewById(R.id.state_spinner);
	    quantity = (EditText) findViewById(R.id.edit_quantity_text);
	    notes = (EditText) findViewById(R.id.edit_notes_text);
	    
	    purchasedButton.setOnClickListener(new Button.OnClickListener() 
        {  
			@Override
			public void onClick(View arg0) 
			{
				chooseDate(purchaseDate, p);
			}
        });
	    
	    openedButton.setOnClickListener(new Button.OnClickListener() 
        {  
			@Override
			public void onClick(View arg0) 
			{
				chooseDate(openDate, o);
			}
        });
	    
	    openedBool.setOnCheckedChangeListener(
            new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.edit_opened_true:
                    	openedTitle.setVisibility(View.VISIBLE);
                    	openedButton.setVisibility(View.VISIBLE);
                    	openDateLabel.setVisibility(View.VISIBLE);
                        break;
                    case R.id.edit_opened_false:
                    	openedTitle.setVisibility(View.GONE);
                    	openedButton.setVisibility(View.GONE);
                    	openDateLabel.setVisibility(View.GONE);
                        break;
                }
            }
        });
	    
	    if(m_myfood != null){   	
	    	if(m_myfood.getName() != null){
	    		myFoodName.setText(m_myfood.getName());
	    	}
	    	if(m_myfood.getPurchaseDate() != null){
	    		purchaseDate.set(Calendar.YEAR, m_myfood.getPurchaseDate().get(Calendar.YEAR));
				purchaseDate.set(Calendar.MONTH, m_myfood.getPurchaseDate().get(Calendar.MONTH));
				purchaseDate.set(Calendar.DAY_OF_MONTH, m_myfood.getPurchaseDate().get(Calendar.DAY_OF_MONTH));
				updateLabel();
	    	}
	    	if(m_myfood.getOpenDate() != null){
	    		openedBool.check(R.id.edit_opened_true);
	    		openedTitle.setVisibility(View.VISIBLE);
				openedButton.setVisibility(View.VISIBLE);
				openDateLabel.setVisibility(View.VISIBLE);
				openDate.set(Calendar.YEAR, m_myfood.getOpenDate().get(Calendar.YEAR));
				openDate.set(Calendar.MONTH, m_myfood.getOpenDate().get(Calendar.MONTH));
				openDate.set(Calendar.DAY_OF_MONTH, m_myfood.getOpenDate().get(Calendar.DAY_OF_MONTH));
	    				updateLabel();
	    	}else{
	    		openedBool.check(R.id.edit_opened_false);
	    		openedTitle.setVisibility(View.GONE);
	    		openedButton.setVisibility(View.GONE);
	    		openDateLabel.setVisibility(View.GONE);
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
	    		notes.setText(m_myfood.getNotes());
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
	    switch (item.getItemId()) 
	    {
	        case R.id.menu_done:
	        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	    	sdf.setLenient(false);
	        	
	        	if(myFoodName.getText() != null){
	        		m_myfood.setName(myFoodName.getText().toString());
	        	}else{
	        		Toast.makeText(this, "Food name must be specified.", 
        					Toast.LENGTH_LONG).show();
        			return false;
	        	}        	
	        	
	        	if(openedBool.getCheckedRadioButtonId() == R.id.edit_opened_true){
	        		if(purchaseDate.compareTo(openDate) > 0){
	        			Toast.makeText(this, "Open date must be on or after purchase date.", 
	        					Toast.LENGTH_LONG).show();
	        			return false;
	        		}
	        		if(purchaseDate.compareTo(Calendar.getInstance()) > 0){
	        			Toast.makeText(this, "Purchase date is later than current date.", 
	        					Toast.LENGTH_LONG).show();
	        			return false;
	        		}
	        		if(openDate.compareTo(Calendar.getInstance()) > 0){
	        			Toast.makeText(this, "Open date is later than current date.", 
	        					Toast.LENGTH_LONG).show();
	        			return false;
	        		}
	        		m_myfood.setPurchaseDate(purchaseDate);
		        	m_myfood.setOpenDate(openDate);
	        		
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
	        		Toast.makeText(this, "Quantity must be specified.", 
        					Toast.LENGTH_LONG).show();
        			return false;
	        	}
	        	if(notes.getText() != null){
	        		m_myfood.setNotes(notes.getText().toString());
	        	}else{
	        		m_myfood.setNotes(null);
	        	}
	        	
	        	/* put food in database */
	        	if(operation == ADD){    	    	
		        	Uri uri = Uri.parse("content://com.shelflifeapp.android.provider/myfood_table/insert/" + 0);
					ContentValues cv = new ContentValues();
					cv.put(MyFoodTable.FOOD_KEY_NAME, m_myfood.getName());
					cv.put(MyFoodTable.FOOD_KEY_FOODID, foodid);
					cv.put(MyFoodTable.FOOD_KEY_PURCHASED, sdf.format(m_myfood.getPurchaseDate().getTime()));
					if(m_myfood.getOpenDate() != null){
						cv.put(MyFoodTable.FOOD_KEY_OPENED, sdf.format(m_myfood.getOpenDate().getTime()));
					}
					cv.put(MyFoodTable.FOOD_KEY_STATE, m_myfood.getState());
					cv.put(MyFoodTable.FOOD_KEY_QUANTITY, m_myfood.getQuantity());
					cv.put(MyFoodTable.FOOD_KEY_NOTES, m_myfood.getNotes());
					Uri idUri = getContentResolver().insert(uri, cv);
					Toast.makeText(this, m_myfood.getName() + " Inserted", Toast.LENGTH_LONG).show();
					Log.d("id", "id: " + m_myfood.getId());
					Log.d("id", "uri: " + idUri);
	        	}else{
	        		Uri uri = Uri.parse("content://com.shelflifeapp.android.provider/myfood_table/edit/" + m_myfood.getId());
					ContentValues cv = new ContentValues();
					cv.put(MyFoodTable.FOOD_KEY_NAME, m_myfood.getName());
					cv.put(MyFoodTable.FOOD_KEY_PURCHASED, sdf.format(m_myfood.getPurchaseDate().getTime()));
					if(m_myfood.getOpenDate() != null){
						cv.put(MyFoodTable.FOOD_KEY_OPENED, sdf.format(m_myfood.getOpenDate().getTime()));
					}
					cv.put(MyFoodTable.FOOD_KEY_STATE, m_myfood.getState());
					cv.put(MyFoodTable.FOOD_KEY_QUANTITY, m_myfood.getQuantity());
					cv.put(MyFoodTable.FOOD_KEY_NOTES, m_myfood.getNotes());
					getContentResolver().update(uri, cv, null, null);
					Toast.makeText(this, m_myfood.getName() + " Edited", Toast.LENGTH_LONG).show();
	        	}
	        	
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
    
    public void chooseDate(Calendar date, DatePickerDialog.OnDateSetListener listener){
    	new DatePickerDialog(EditFoodActivity.this, listener, 
    			date.get(Calendar.YEAR),
    			date.get(Calendar.MONTH), 
    			date.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener p = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			purchaseDate.set(Calendar.YEAR,year);
			purchaseDate.set(Calendar.MONTH, monthOfYear);
			purchaseDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateLabel();
		}
	};
	
	DatePickerDialog.OnDateSetListener o = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			openDate.set(Calendar.YEAR,year);
			openDate.set(Calendar.MONTH, monthOfYear);
			openDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateLabel();
		}
	};

	private void updateLabel() {
		purchaseDateLabel.setText(formatDateTime.format(purchaseDate.getTime()));
		openDateLabel.setText(formatDateTime.format(openDate.getTime()));
	}
}
