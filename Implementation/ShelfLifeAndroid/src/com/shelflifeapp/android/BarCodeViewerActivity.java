package com.shelflifeapp.android;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public class BarCodeViewerActivity extends SherlockActivity {
	Bundle bundleObject;
	String upcNum;
	TextView upcView;
	private static final String TAG = "BarCodeViewerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		bundleObject = this.getIntent().getExtras();
		upcView = (TextView) findViewById(R.id.textView1);
		if(bundleObject == null){
			Log.d(TAG, "Bundle is null");
		}else{
			upcNum = bundleObject.getString("upc");
			if(upcNum == null){
				Log.d(TAG, "Greeting is null");
			}else{
				upcView.setText("Barcode: " + upcNum);
			}			
		}
		setContentView(R.layout.barcode_view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = this.getSupportMenuInflater();
		inflater.inflate(R.menu.menu_actionbar, menu);
		return true;
	}
}
