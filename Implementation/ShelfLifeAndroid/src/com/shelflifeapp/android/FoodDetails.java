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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.shelflifeapp.database.MyFoodTable;
import com.shelflifeapp.models.Category;
import com.shelflifeapp.models.ExpirationData;
import com.shelflifeapp.models.Food;

public class FoodDetails extends SherlockActivity 
{
	Button addButton; 
	private Food m_food;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        m_food = new Food(2, "Banana", new Category(), new ExpirationData(1, 2, 3, 4, 5, 6), "enjoy!");
        setContentView(R.layout.food_details);
        addButton = (Button) findViewById(R.id.add_food_button);
        addButton.setOnClickListener(new Button.OnClickListener() {  

			@Override
			public void onClick(View arg0) {
				Uri uri = Uri.parse("content://com.shelflifeapp.android.provider/myfood_table/insert/" + 0);
				ContentValues cv = new ContentValues();
				cv.put(MyFoodTable.FOOD_KEY_FOODID, m_food.getId());
				Uri idUri = getContentResolver().insert(uri, cv);
				//joke.setID(Long.parseLong(idUri.getLastPathSegment())); // ?????
				//fillData();
				Toast.makeText(FoodDetails.this, m_food.getName() + " Inserted", Toast.LENGTH_LONG).show();
			}
        });
        SimpleUpcPost s = new SimpleUpcPost();
        s.execute("");
    }
    
    private class SimpleUpcPost extends AsyncTask<String, Void, String>
    {
    	private String getPostInfo()
    	{
    		JSONObject postObject = new JSONObject();
    		JSONObject paramsObject = new JSONObject();

    		try 
    		{
        		paramsObject.put("upc", "073731001059");
        		
        		postObject.put("auth", "etxXV93BmMwtwlAHTRjbKaOOaF0T2Y");
				postObject.put("method", "FetchProductByUPC");
				postObject.put("params", paramsObject);
				postObject.put("returnFormat", "JSON");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d("SPOCK", postObject.toString());
			return postObject.toString();
    		
    	}
    	
    	
		@Override
		protected String doInBackground(String... arg0) 
		{
			try
			{
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
				HttpConnectionParams.setSoTimeout(httpParams, 10000);
				HttpClient client = new DefaultHttpClient(httpParams);
				
				HttpPost post = new HttpPost("http://api.simpleupc.com/v1.php");
				StringEntity se = new StringEntity(getPostInfo());
				
				post.setEntity(se);
				//post.setHeader("Accept", "application/json");
			    //post.setHeader("Content-type", "application/json");
			    
			    ResponseHandler<String> responseHandler = new BasicResponseHandler();
			    String response = client.execute(post, responseHandler);
			    return response;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return "uh-oh";
			}
		}
		
		@Override
		protected void onPostExecute(String arg)
		{
			TextView tv = (TextView) findViewById(R.id.food_details_simpleupc);
			tv.setText(arg);
			//Toast.makeText(FoodDetails.this, arg, Toast.LENGTH_LONG).show();
		}
    	
    }
}
