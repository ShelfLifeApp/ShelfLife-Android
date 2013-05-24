package com.slewson.simpleupc;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;

public class SimpleUpcApi 
{
	private SimpleUpcApiListener mCallback = null;
	private SimpleUpcPost simpleUpcPost = null;
	
	public SimpleUpcApi(SimpleUpcApiListener callback)
	{
		mCallback = callback;
		simpleUpcPost = new SimpleUpcPost();
	}
	
	public void fetchProductByUpc(String upc)
	{
		simpleUpcPost.execute(new FetchProductByUpcCall(upc));
	}
	
	public interface SimpleUpcApiListener
	{
		public void onFetchProductByUpc(SimpleUpcResponse response);
	}
	
    private class SimpleUpcPost extends AsyncTask<SimpleUpcApiCall, Void, SimpleUpcResponse>
    {	
		@Override
		protected SimpleUpcResponse doInBackground(SimpleUpcApiCall... calls) 
		{
			SimpleUpcApiCall call = calls[0];
			
			try
			{
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
				HttpConnectionParams.setSoTimeout(httpParams, 10000);
				HttpClient client = new DefaultHttpClient(httpParams);
				
				HttpPost post = new HttpPost(SimpleUpcConstants.POST_URL);
				StringEntity entity = new StringEntity(call.getRequestBody());
				post.setEntity(entity);
				
			    ResponseHandler<String> responseHandler = new BasicResponseHandler();
			    String response = client.execute(post, responseHandler);
			    
		    	return new SimpleUpcResponse(call.getMethod(), response);
			}
			catch (Exception e)
			{
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(SimpleUpcResponse response)
		{
			if (SimpleUpcConstants.FETCH_PRODUCT_BY_UPC.equals(response.getMethod()))
			{
				mCallback.onFetchProductByUpc(response);
			}
		}
    }
}
