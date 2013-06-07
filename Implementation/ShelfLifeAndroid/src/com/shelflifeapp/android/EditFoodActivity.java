package com.shelflifeapp.android;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.ActionMode.Callback;
import com.shelflifeapp.database.MyFoodTable;
import com.shelflifeapp.models.MyFood;
import com.shelflifeapp.views.MyFoodListItem;

public class EditFoodActivity extends SherlockActivity  
{
	/* Location Keys */
	private final static int LOC_SHELF = 0;
	private final static int LOC_FRIDGE = 1;
	private final static int LOC_FREEZER = 2;

	/* Action Keys */
	public final static int OP_ADD_FOOD = 3;
	public final static int OP_EDIT_FOOD = 4;
	
	/* Activity Result Codes */
	private static final int PICTURE_REQUEST_CODE = 2;

	/* Activity Header Views */
	private TextView header_title;
	private TextView header_subtitle;
	
	/* Food Conditions */
	private RadioGroup conditions_loc_rgroup;
	private RadioGroup conditions_opened_rgroup;
	
	/* Food Details */
	private ImageView details_photo;
	private EditText details_name_entry;
	private EditText details_purDate_entry;
	private EditText details_quantity_entry;
	private EditText details_openDate_entry;
	private EditText details_notes_entry;
	
	private MyFood m_myFood;
	private Bitmap picture;
	Calendar purchaseDate = Calendar.getInstance();
	Calendar openDate = Calendar.getInstance();
	
	private Context mContext;
	private int operation;
	
	private Callback mActionModeCallback;
	private ActionMode mActionMode;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_food);
        mContext = EditFoodActivity.this;
        
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
                
        Bundle foodBundle = getIntent().getExtras();
        m_myFood = foodBundle.getParcelable("myfood");
        
        operation = foodBundle.getInt("operation");
        m_myFood.setId(foodBundle.getInt("foodid"));
        
        initializeViews();
        
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == PICTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK)
		{
			picture = (Bitmap)data.getExtras().get("data");
			details_photo.setImageBitmap(picture);
		}
	}
	
	
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) 
    {
		MenuInflater inflater = this.getSupportMenuInflater();
		inflater.inflate(R.menu.editmyfood_actionbar, menu);
		return true;
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) 
    {
	    switch (item.getItemId()) 
	    {
	        case R.id.menu_done:
	        {
	        	if(fillInMyFood() == true)
	        	{
	        		saveToDatabase(m_myFood);
	        	}
	        	return true;
	        }
	        case R.id.menu_take_picture:
	        {
				Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(camera, PICTURE_REQUEST_CODE);
				return true;
	        }
	        case R.id.menu_remove:
	        {
				removePicture();
				return true;
	        }
	    	case android.R.id.home:
	    	{
	    		finish();
	    		return true;
	    	}
	    }
	    
	    return super.onOptionsItemSelected(item);
    }
    
	private void initializeViews()
	{
		header_title = (TextView) findViewById(R.id.edit_header_title);
		header_subtitle = (TextView) findViewById(R.id.edit_header_subtitle);
		
		conditions_loc_rgroup = (RadioGroup) findViewById(R.id.edit_loc_group);
		conditions_opened_rgroup = (RadioGroup) findViewById(R.id.edit_open_group);
		
		details_name_entry = (EditText) findViewById(R.id.edit_table_name_entry);
		details_purDate_entry = (EditText) findViewById(R.id.edit_table_purchased_entry);
		details_quantity_entry = (EditText) findViewById(R.id.edit_table_quantity_entry);
		details_openDate_entry = (EditText) findViewById(R.id.edit_table_opened_entry);
		details_notes_entry = (EditText) findViewById(R.id.edit_notes_entry);
		details_photo = (ImageView) findViewById(R.id.edit_picture);
		
		if(m_myFood.getName() != null){
			details_name_entry.setText(m_myFood.getName());
		}
		if(m_myFood.getPurchaseDate() != null){
			purchaseDate = m_myFood.getPurchaseDate();
			details_purDate_entry.setText(formatCalendarString(purchaseDate));
		}else{
			details_purDate_entry.setText(formatCalendarString(purchaseDate));
		}
		if(m_myFood.getOpenDate() != null){
			conditions_opened_rgroup.check(R.id.edit_open_yes);
			openDate = m_myFood.getOpenDate();
			details_openDate_entry.setText(formatCalendarString(openDate));
		}else{
			conditions_opened_rgroup.check(R.id.edit_open_no);
			details_openDate_entry.setText("Unopened");
		}
		if(m_myFood.getState() != null){
			String[] states = this.getResources().getStringArray(R.array.state_list);
			if(MyFood.SHELF_UNOPENED.equals(m_myFood.getState()) ||
				MyFood.SHELF_OPENED.equals(m_myFood.getState())){
				conditions_loc_rgroup.check(R.id.edit_loc_shelf);
			}else if(MyFood.FRIDGE_UNOPENED.equals(m_myFood.getState()) ||
				MyFood.FRIDGE_OPENED.equals(m_myFood.getState())){
				conditions_loc_rgroup.check(R.id.edit_loc_fridge);
			}else if(MyFood.FREEZER_UNOPENED.equals(m_myFood.getState()) ||
				MyFood.FREEZER_OPENED.equals(m_myFood.getState())){
				conditions_loc_rgroup.check(R.id.edit_loc_freezer);
			}
		}else{
			conditions_loc_rgroup.check(R.id.edit_loc_shelf);
		}
		details_quantity_entry.setText("" + m_myFood.getQuantity());
		if(m_myFood.getNotes() != null){
			details_notes_entry.setText(m_myFood.getNotes());
		}

		if(m_myFood.getPicture() != null){
			details_photo.setImageBitmap(m_myFood.getPicture());
		}
		intializeListeners();
	}
	
	private void intializeListeners()
	{		
		conditions_opened_rgroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				switch (checkedId)
				{
					case R.id.edit_open_yes:
						details_openDate_entry.setText(formatCalendarString(openDate));
						return;
					case R.id.edit_open_no:
						details_openDate_entry.setText("Unopened");
						return;
				}
			}	
		});
		
		details_purDate_entry.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				DatePickerDialog.OnDateSetListener purDate_listener = new DatePickerDialog.OnDateSetListener() 
				{
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
					{
						Calendar c = Calendar.getInstance();
						c.set(year, monthOfYear, dayOfMonth);
						details_purDate_entry.setText(formatCalendarString(c));
						purchaseDate = c;
					}
				};
				
				chooseDate(purchaseDate, purDate_listener);
			}	
		});
		
		details_openDate_entry.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				if (conditions_opened_rgroup.getCheckedRadioButtonId() == R.id.edit_open_yes)
				{
					DatePickerDialog.OnDateSetListener openDate_listener = new DatePickerDialog.OnDateSetListener() 
					{
						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
						{
							Calendar c = Calendar.getInstance();
							c.set(year, monthOfYear, dayOfMonth);
							details_openDate_entry.setText(formatCalendarString(c));
							openDate = c;
						}
					};
					
					chooseDate(openDate, openDate_listener);
				}
			}	
		});
		
		details_photo.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(camera, PICTURE_REQUEST_CODE);
			}	
		});
	}
	
	private void chooseDate(Calendar date, DatePickerDialog.OnDateSetListener listener)
	{
		new DatePickerDialog(EditFoodActivity.this, listener,
				date.get(Calendar.YEAR),
				date.get(Calendar.MONTH),
				date.get(Calendar.DAY_OF_MONTH)).show();
	}

	private String formatCalendarString(Calendar c)
	{
		SimpleDateFormat format = new SimpleDateFormat("EEE\', \'MMM\' \'d\', \'yyyy ");
		return format.format(c.getTime());
	}
	
	private boolean fillInMyFood()
	{		
		if(details_name_entry.getText() != null && !"".equals(details_name_entry.getText().toString())){
			m_myFood.setName(details_name_entry.getText().toString());
			}else{
				Toast.makeText(this, "Food name must be specified.",
		        Toast.LENGTH_LONG).show();
		        return false;
			}
			if(purchaseDate.compareTo(Calendar.getInstance()) > 0){
				Toast.makeText(this, "Purchase date is later than current date.",
				Toast.LENGTH_LONG).show();
				return false;
			}
			m_myFood.setPurchaseDate(purchaseDate);
			if(conditions_opened_rgroup.getCheckedRadioButtonId() == R.id.edit_open_yes){
				if(openDate == null){
					Toast.makeText(this, "Open date needs to be set.",
					Toast.LENGTH_LONG).show();
					return false;
				}
				
				if(purchaseDate.compareTo(openDate) > 0){
					Toast.makeText(this, "Open date must be on or after purchase date.",
					Toast.LENGTH_LONG).show();
					return false;
				}

				if(openDate.compareTo(Calendar.getInstance()) > 0){
					Toast.makeText(this, "Open date is later than current date.",
					Toast.LENGTH_LONG).show();
					return false;
				}	
				m_myFood.setOpenDate(openDate);

				if(conditions_loc_rgroup.getCheckedRadioButtonId() == R.id.edit_loc_shelf){
					m_myFood.setState(MyFood.SHELF_OPENED);
				}else if(conditions_loc_rgroup.getCheckedRadioButtonId() == R.id.edit_loc_fridge){
					m_myFood.setState(MyFood.FRIDGE_OPENED);
				}else if(conditions_loc_rgroup.getCheckedRadioButtonId() == R.id.edit_loc_freezer){
					m_myFood.setState(MyFood.FREEZER_OPENED);
			}
			}else{
				m_myFood.setOpenDate(null);
				if(conditions_loc_rgroup.getCheckedRadioButtonId() == R.id.edit_loc_shelf){
					m_myFood.setState(MyFood.SHELF_UNOPENED);
				}else if(conditions_loc_rgroup.getCheckedRadioButtonId() == R.id.edit_loc_fridge){
					m_myFood.setState(MyFood.FRIDGE_UNOPENED);
				}else if(conditions_loc_rgroup.getCheckedRadioButtonId() == R.id.edit_loc_freezer){
					m_myFood.setState(MyFood.FREEZER_UNOPENED);
				}
			}

			if(details_quantity_entry.getText() != null){
				m_myFood.setQuantity(Integer.parseInt(details_quantity_entry.getText().toString()));
			}else{
				Toast.makeText(this, "Quantity must be specified.",
			         Toast.LENGTH_LONG).show();
			         return false;
			}
			if(details_notes_entry.getText() != null){
				m_myFood.setNotes(details_notes_entry.getText().toString());
			}else{
				m_myFood.setNotes(null);
			}
			m_myFood.setPicture(picture);
			return true;
	}
	
	private void saveToDatabase(MyFood m_myfood)
	{
		SimpleDateFormat sdf = (new SimpleDateFormat("yyyy-MM-dd"));
    	sdf.setLenient(false);
		ContentValues cv = new ContentValues();
		cv.put(MyFoodTable.FOOD_KEY_NAME, m_myfood.getName());
		cv.put(MyFoodTable.FOOD_KEY_PURCHASED, sdf.format(m_myfood.getPurchaseDate().getTime()));
		cv.put(MyFoodTable.FOOD_KEY_STATE, m_myfood.getState());
		cv.put(MyFoodTable.FOOD_KEY_QUANTITY, m_myfood.getQuantity());
		cv.put(MyFoodTable.FOOD_KEY_NOTES, m_myfood.getNotes());
		
		if(m_myfood.getPicture() != null)
		{
			cv.put(MyFoodTable.FOOD_KEY_PICTURE, m_myfood.setPictureToByteArray(m_myfood.getPicture()));
		}
		else
		{
			cv.putNull(MyFoodTable.FOOD_KEY_PICTURE);
		}
		if(m_myfood.getOpenDate() != null)
		{
			cv.put(MyFoodTable.FOOD_KEY_OPENED, sdf.format(m_myfood.getOpenDate().getTime()));
		}

		
		if(operation == OP_ADD_FOOD)
		{   
			cv.put(MyFoodTable.FOOD_KEY_FOODID, m_myfood.getId());
        	Uri uri = Uri.parse("content://com.shelflifeapp.android.provider/myfood_table/insert/" + 0);
			Uri idUri = getContentResolver().insert(uri, cv);
			String id = idUri.getLastPathSegment();
			m_myfood.setId(Integer.parseInt(id));
    	}
		else if (operation == OP_EDIT_FOOD)
		{
    		Uri uri = Uri.parse("content://com.shelflifeapp.android.provider/myfood_table/edit/" + m_myfood.getId());
			getContentResolver().update(uri, cv, null, null);
			Toast.makeText(this, m_myfood.getName() + " Edited", Toast.LENGTH_LONG).show();
    	}

    	Intent i = new Intent(this, MyFoodDetails.class);
    	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		i.putExtra("myfood", m_myfood);
		finish();
		startActivity(i);
	}
	
	private void removePicture(){
		picture = null;
		this.details_photo.setImageResource(R.drawable.empty_pic_selector);
	} 
}