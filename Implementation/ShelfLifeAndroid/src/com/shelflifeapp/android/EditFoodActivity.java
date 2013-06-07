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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.shelflifeapp.database.MyFoodTable;
import com.shelflifeapp.models.MyFood;

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
	
	private Context mContext;
	private int operation;
	
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
        //Log.d("EDIT", "Before Loc: " + m_myFood.getLoc());
        //Log.d("EDIT", "Before State op: " + m_myFood.getState_opened());
        //Log.d("EDIT", "Before State: " + m_myFood.getState());
        
        if (m_myFood.getPurchaseDate() == null)
        {
        	m_myFood.setPurchaseDate(Calendar.getInstance());
        }
        
        operation = foodBundle.getInt("operation");
        m_myFood.setId(foodBundle.getInt("foodid"));
        
        initializeViews();

        updateDisplay(m_myFood);
        
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == PICTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK)
		{
			Bitmap picture = (Bitmap)data.getExtras().get("data");
			m_myFood.setPicture(picture);
			updateDisplay();
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
		intializeListeners();
	}
	
	private void intializeListeners()
	{
		conditions_loc_rgroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup rg, int checkId) {
				switch(checkId)
				{
					case R.id.edit_loc_shelf:
						m_myFood.setLoc(MyFood.LOC_SHELF);
						return;
						
					case R.id.edit_loc_fridge:
						m_myFood.setLoc(MyFood.LOC_FRIDGE);
						return;
					case R.id.edit_loc_freezer:
						m_myFood.setLoc(MyFood.LOC_FREEZER);
						return;
				}
				return;
			}
		});
		
		conditions_opened_rgroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				switch (checkedId)
				{
					case R.id.edit_open_yes:
						m_myFood.setState_opened(MyFood.STATE_OPENED);
						if (m_myFood.getOpenDate() == null)
							details_openDate_entry.setText("Set open date");
						return;
					case R.id.edit_open_no:
						m_myFood.setState_opened(MyFood.STATE_UNOPENED);
						m_myFood.setOpenDate(null);
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
						m_myFood.setPurchaseDate(c);
						updateDisplay();
					}
				};
				
				if (m_myFood.getPurchaseDate() != null)
					chooseDate(m_myFood.getPurchaseDate(), purDate_listener);
				else
					chooseDate(Calendar.getInstance(), purDate_listener);
			}	
		});
		
		details_openDate_entry.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				if (m_myFood.getState_opened().equals(MyFood.STATE_OPENED))
				{
					DatePickerDialog.OnDateSetListener openDate_listener = new DatePickerDialog.OnDateSetListener() 
					{
						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
						{
							Calendar c = Calendar.getInstance();
							c.set(year, monthOfYear, dayOfMonth);
							m_myFood.setOpenDate(c);
							updateDisplay();
						}
					};
					
					if (m_myFood.getOpenDate() != null)
						chooseDate(m_myFood.getOpenDate(), openDate_listener);
					else 
						chooseDate(Calendar.getInstance(), openDate_listener);
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
	
	private void updateDisplay()
	{
		updateDisplay(m_myFood);
	}
	
	private void updateDisplay(MyFood mf)
	{
		details_name_entry.setText(mf.getName());
		details_quantity_entry.setText(mf.getQuantity() + "");
	
		if (mf.getOpenDate() != null)
			details_openDate_entry.setText(formatCalendarString(mf.getOpenDate()));
		
		if (mf.getPurchaseDate() != null)
			details_purDate_entry.setText(formatCalendarString(mf.getPurchaseDate()));
		else
			details_purDate_entry.setText(formatCalendarString(Calendar.getInstance()));
	
		
		String loc = mf.getLoc();
		//Log.d("EDIT", "Location: " + loc);
		if (loc.equals(MyFood.LOC_SHELF))
			conditions_loc_rgroup.check(R.id.edit_loc_shelf);
		else if (loc.equals(MyFood.LOC_FRIDGE))
			conditions_loc_rgroup.check(R.id.edit_loc_fridge);
		else if (loc.equals(MyFood.LOC_FREEZER))
			conditions_loc_rgroup.check(R.id.edit_loc_freezer);
		
		String state = mf.getState_opened();
		//Log.d("EDIT", "State: " + state);
		if (state.equals(MyFood.STATE_OPENED))
			conditions_opened_rgroup.check(R.id.edit_open_yes);
		else if (state.equals(MyFood.STATE_UNOPENED))
			conditions_opened_rgroup.check(R.id.edit_open_no);
		
		
		if (mf.getPicture() != null)
			details_photo.setImageBitmap(mf.getPicture());
	}
	
	private String formatCalendarString(Calendar c)
	{
		SimpleDateFormat format = new SimpleDateFormat("EEE\', \'MMM\' \'d\', \'yyyy ");
		return format.format(c.getTime());
	}
	
	private boolean fillInMyFood()
	{
		if (m_myFood.getState_opened().equals(MyFood.STATE_OPENED) && m_myFood.getOpenDate() == null)
		{	
			Toast.makeText(mContext, "Open Date Invalid", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if (m_myFood.getPurchaseDate() == null)
		{
			Toast.makeText(mContext, "Purchase Date Invalid", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		String name = details_name_entry.getText().toString();
		if (!"".equals(name))
		{
			m_myFood.setName(name);
		}
		else
		{
			Toast.makeText(mContext, "Invalid Name", Toast.LENGTH_LONG).show();
			return false;
		}
		
		try
		{
			int quantity = Integer.parseInt(details_quantity_entry.getText().toString());
			if (quantity >= 0)
			{
				m_myFood.setQuantity(quantity);
			}
			else
			{
				Toast.makeText(mContext, "Invalid Quantity 1", Toast.LENGTH_LONG).show();					
				return false;
			}	
		}
		catch (Exception e)
		{
			Toast.makeText(mContext, "Invalid Quantity 2", Toast.LENGTH_LONG).show();	
			return false;
		}
		
		String notes = details_notes_entry.getText().toString();
		m_myFood.setNotes(notes);
		
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
}