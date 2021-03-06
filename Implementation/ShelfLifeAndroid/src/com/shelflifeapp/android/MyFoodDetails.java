package com.shelflifeapp.android;

import java.text.SimpleDateFormat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.shelflifeapp.views.ExpirationTable;

public class MyFoodDetails extends SherlockActivity 
{
	private static final String DATE_PURCHASED = "Purchased: ";
	private static final String DATE_OPENED = "Opened: ";
	private static final String LOCATION = "Location: ";
	private static final String QUANTITY = "Quantity: ";
	private static final String SHELF = "Shelf";
	private static final String FRIDGE = "Fridge";
	private static final String FREEZER = "Freezer";
	
	private MyFood m_myfood;
	private TextView myFoodName;
	private TextView purchased;
	private TextView opened;
	private TextView location;
	private TextView quantity;
	private TextView notes;
	private TextView daysLeft;
	private TextView daysText;
	private TextView shelfOpened;
	private TextView shelfUnopened;
	private TextView fridgeOpened;
	private TextView fridgeUnopened;
	private TextView freezerOpened;
	private TextView freezerUnopened;
	private ImageView picture;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfood_details);
        
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        Bundle foodBundle = this.getIntent().getExtras();
	    if(foodBundle == null){
			//Log.d("shelflife", "Bundle is null");
		}else{
			m_myfood = foodBundle.getParcelable("myfood");
			if(m_myfood == null){
				//Log.d("shelflife", "food is null");
			}			
		}
	    
	    //Log.d("EDIT", "DETAILS BEFORE: " + m_myfood.getLoc());
	    //Log.d("EDIT", "DETAILS BEFORE op: " + m_myfood.getState_opened());
	    //Log.d("EDIT", "DETAILS BEFORE: " + m_myfood.getState());
        
	    ExpirationTable expirationTable = (ExpirationTable) findViewById(R.id.ExpirationTable);
	    myFoodName = (TextView) findViewById(R.id.myfood_name_text);
	    shelfOpened = (TextView) findViewById(R.id.row2_shelf_opened);
	    shelfUnopened = (TextView) findViewById(R.id.row2_shelf_unopened);
	    fridgeOpened = (TextView) findViewById(R.id.row3_fridge_opened);
	    fridgeUnopened = (TextView) findViewById(R.id.row3_fridge_unopened);
	    freezerOpened = (TextView) findViewById(R.id.row4_freezer_opened);
	    freezerUnopened = (TextView) findViewById(R.id.row4_freezer_unopened);
	    purchased = (TextView) findViewById(R.id.myfood_purchased_text);
	    opened = (TextView) findViewById(R.id.myfood_opened_text);
	    location = (TextView) findViewById(R.id.myfood_location_text);
	    quantity = (TextView) findViewById(R.id.myfood_quantity_text);
	    notes = (TextView) findViewById(R.id.myfood_notes_text);
	    daysLeft = (TextView) findViewById(R.id.myfood_days_left_num);
	    daysText = (TextView) findViewById(R.id.myfood_days_left_text);
	    picture = (ImageView) findViewById(R.id.myfood_details_plate_food);
	    
	    if(m_myfood != null){
	    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	    	sdf.setLenient(false);
	    	
	    	myFoodName.setText(m_myfood.getName());
	    	expirationTable.setExpirationData(m_myfood.getExpirationData());

		    purchased.setText(DATE_PURCHASED + sdf.format(m_myfood.getPurchaseDate().getTime()));
		    
		    if(m_myfood.getOpenDate() != null){
		    	opened.setText(DATE_OPENED + sdf.format(m_myfood.getOpenDate().getTime()));
		    }else{
		    	opened.setText(DATE_OPENED + "No");
		    }
		    
		    if(MyFood.SHELF_UNOPENED.equals(m_myfood.getState()) || 
		    		MyFood.SHELF_OPENED.equals(m_myfood.getState())){
		    	location.setText(LOCATION + SHELF);
		    }else if(MyFood.FRIDGE_UNOPENED.equals(m_myfood.getState()) || 
		    		MyFood.FRIDGE_OPENED.equals(m_myfood.getState())){
		    	location.setText(LOCATION + FRIDGE);
		    }else if(MyFood.FREEZER_UNOPENED.equals(m_myfood.getState()) || 
		    		MyFood.FREEZER_OPENED.equals(m_myfood.getState())){
		    	location.setText(LOCATION + FREEZER);
		    }
		    quantity.setText(QUANTITY + m_myfood.getQuantity());
		    
		    if(m_myfood.getNotes() != null && !"".equals(m_myfood.getNotes())){
		    	notes.setText(m_myfood.getNotes());
		    }
		    
		    if(m_myfood.getExpirationDaysLeft() < 0){
		    	daysLeft.setTextColor(getResources().getColor(R.color.listitem_myfood_text_expire_color));
		    	daysLeft.setText("Expired");
		    	daysLeft.setTextSize(20);
		    	daysText.setVisibility(View.GONE);		    	
		    }else if(m_myfood.getExpirationDaysLeft() == 1){
		    	daysLeft.setText("" + m_myfood.getExpirationDaysLeft());
		    	daysText.setText("day");
		    	daysText.setVisibility(View.VISIBLE);
		    }else{
		    	daysLeft.setText("" + m_myfood.getExpirationDaysLeft());
		    	daysText.setText("days");
		    	daysText.setVisibility(View.VISIBLE);
		    }
	    }
	    else{
	    	Toast.makeText(this, "My Food is null", Toast.LENGTH_LONG).show();
	    }
	    if(m_myfood.getPicture() != null){
	    	Bitmap pictureBitmap = m_myfood.getPicture();
	    	Bitmap circleBitmap = Bitmap.createBitmap(pictureBitmap.getWidth(),
	    			pictureBitmap.getHeight(), Bitmap.Config.ARGB_8888);
	    	BitmapShader shader = new BitmapShader (pictureBitmap,  TileMode.CLAMP, TileMode.CLAMP);
	    	Paint paint = new Paint();
	    	        paint.setShader(shader);
	        Canvas c = new Canvas(circleBitmap);
	        c.drawCircle(pictureBitmap.getWidth()/2, pictureBitmap.getHeight()/2, pictureBitmap.getWidth()/2, paint);
	    	picture.setImageBitmap(circleBitmap);
	    }
	    else
	    {
	    	picture.setImageResource(m_myfood.getCategory().getPlateIconId());
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
	    switch (item.getItemId()) 
	    {
	        case R.id.menu_edit:
	        	Intent i = new Intent(MyFoodDetails.this, EditFoodActivity.class);
				i.putExtra("myfood", m_myfood);
				//Log.d("EDIT", "Details Loc: " + m_myfood.getLoc());
				//Log.d("EDIT", "Details State op: " + m_myfood.getState_opened());
				//Log.d("EDIT", "Details State: " + m_myfood.getState());
				i.putExtra("operation", EditFoodActivity.OP_EDIT_FOOD);
				i.putExtra("foodid", m_myfood.getId());
				finish();
				
				startActivity(i);
	            return true;	
	        
	    	case android.R.id.home:
	        	Intent h = new Intent(this, MainActivity.class);
	        	h.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	    		finish();
	    		startActivity(h);

	    		return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}