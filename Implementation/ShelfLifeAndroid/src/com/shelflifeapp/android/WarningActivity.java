package com.shelflifeapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class WarningActivity extends SherlockActivity 
{

	private Button agree;
	private Button disagree;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warning_activity);
        
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        agree = (Button) findViewById(R.id.warning_button_agree);
        disagree = (Button) findViewById(R.id.warning_button_disagree);
        
        agree.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View v)
        	{
        		agree();
        	}
        });
        
        disagree.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View v)
        	{
        		disagree();
        	}
        });

    }
	
	private void agree()
	{
		Intent returnIntent = new Intent();
		returnIntent.putExtra(FoodDetails.KEY_DATA_AGREE, true);
		setResult(RESULT_CANCELED, returnIntent);        
		finish();
	}
	
	private void disagree()
	{
		Intent returnIntent = new Intent();
		returnIntent.putExtra(FoodDetails.KEY_DATA_AGREE, false);
		setResult(RESULT_OK, returnIntent);        
		finish();
	}
    
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
		return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) 
    {
	    switch (item.getItemId()) 
	    {	
	    	case android.R.id.home:
	    		disagree();
	    		return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}