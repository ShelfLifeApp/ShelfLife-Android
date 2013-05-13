package com.slewson.simpleupc;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class SimpleUPCLoader extends AsyncTaskLoader<SimpleUPCResponse>
{
	private static final String TAG = "SimpleUPCLoader";
	
    private HTTPVerb     mVerb;
    private Uri          mAction;
    private Bundle       mParams;
    private SimpleUPCResponse mResponse;
    
    private long mLastLoad;

    private static final long STALE_DELTA = 600000;
    
    public enum HTTPVerb {
        GET,
        POST,
        PUT,
        DELETE
    }
    
    public SimpleUPCLoader(Context context)
    {
    	super(context);
    }
    
    public SimpleUPCLoader(Context context, HTTPVerb verb, Uri action)
    {
    	super(context);
    	mVerb = verb;
    	mAction = action;
    }
    
    public SimpleUPCLoader(Context context, HTTPVerb verb, Uri action, Bundle params)
    {
    	super(context);
    	
    	mVerb = verb;
    	mAction = action;
    	mParams = params;
    }
    

    @Override
    public SimpleUPCResponse loadInBackground()
    {
    	if (mAction == null)
    		return new SimpleUPCResponse();
    	
    	HttpRequestBase request = getHttpRequestBase(mVerb);
    	SimpleUPCResponse restResponse = null;
    	
    	if (request != null)
    	{
    		try
    		{
    			HttpClient client = new DefaultHttpClient();
        		HttpResponse response = client.execute(request);
        		HttpEntity responseEntity = response.getEntity();
                StatusLine responseStatus = response.getStatusLine();
                int statusCode = responseStatus != null ? responseStatus.getStatusCode() : 0;
                
                // Here we create our response and send it back to the LoaderCallbacks<RESTResponse> implementation.
                restResponse = new SimpleUPCResponse(responseEntity != null ? EntityUtils.toString(responseEntity) : null, statusCode);
    		}
    		catch (Exception e)
    		{
    			restResponse = new SimpleUPCResponse();
    		}
    	}
		return restResponse;
    }
    
    @Override
    public void deliverResult(SimpleUPCResponse data) {
        // Here we cache our response.
    	mResponse = data;
        super.deliverResult(data);
    }
    
    @Override
    protected void onStartLoading() {
        if (mResponse != null) {
            // We have a cached result, so we can just
            // return right away.
            super.deliverResult(mResponse);
        }
        
        // If our response is null or we have hung onto it for a long time,
        // then we perform a force load.
        if (mResponse == null || System.currentTimeMillis() - mLastLoad >= STALE_DELTA) forceLoad();
        mLastLoad = System.currentTimeMillis();
    }
    
    @Override
    protected void onStopLoading() {
        // This prevents the AsyncTask backing this
        // loader from completing if it is currently running.
        cancelLoad();
    }
    
    @Override
    protected void onReset() {
        super.onReset();
        
        // Stop the Loader if it is currently running.
        onStopLoading();
        
        // Get rid of our cache if it exists.
        mResponse = null;
        
        // Reset our stale timer.
        mLastLoad = 0;
    }
    
    private HttpRequestBase getHttpRequestBase(HTTPVerb mVerb)
    {
    	HttpRequestBase request = null;
    	
       	try
    	{

    		switch(mVerb)
    		{
    			case GET:
    			{
    				request = new HttpGet();
    				attachUriWithQuery(request, mAction, mParams);
    				break;
    			}
    			case DELETE:
    			{
    				request = new HttpDelete();
                    attachUriWithQuery(request, mAction, mParams);
                    break;
    			}
    			case POST:
    			{
    				request = new HttpPost();
                    request.setURI(new URI(mAction.toString()));
                    HttpPost postRequest = (HttpPost) request;
                    
                    if (mParams != null) {
                        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramsToList(mParams));
                        postRequest.setEntity(formEntity);
                    }
                    break;
    			}
    			case PUT:
    			{
                    request = new HttpPut();
                    request.setURI(new URI(mAction.toString()));
                    
                    // Attach form entity if necessary.
                    HttpPut putRequest = (HttpPut) request;
                    
                    if (mParams != null) 
                    {
                        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramsToList(mParams));
                        putRequest.setEntity(formEntity);
                    }
                
                    break;
    			}
    		}
    	}
    	catch (Exception e)
    	{
    		request = null;
    	}
    	
    	return request;
    }
    
    private static void attachUriWithQuery(HttpRequestBase request, Uri uri, Bundle params) {
        try {
            if (params == null) {
                // No params were given or they have already been
                // attached to the Uri.
                request.setURI(new URI(uri.toString()));
            }
            else {
                Uri.Builder uriBuilder = uri.buildUpon();
                
                // Loop through our params and append them to the Uri.
                for (BasicNameValuePair param : paramsToList(params)) {
                    uriBuilder.appendQueryParameter(param.getName(), param.getValue());
                }
                
                uri = uriBuilder.build();
                request.setURI(new URI(uri.toString()));
            }
        }
        catch (URISyntaxException e) {
            Log.e(TAG, "URI syntax was incorrect: "+ uri.toString());
        }
    }
    
    private static String verbToString(HTTPVerb verb) {
        switch (verb) {
            case GET:
                return "GET";
                
            case POST:
                return "POST";
                
            case PUT:
                return "PUT";
                
            case DELETE:
                return "DELETE";
        }
        
        return "";
    }
    
    private static List<BasicNameValuePair> paramsToList(Bundle params) {
        ArrayList<BasicNameValuePair> formList = new ArrayList<BasicNameValuePair>(params.size());
        
        for (String key : params.keySet()) {
            Object value = params.get(key);
            
            // We can only put Strings in a form entity, so we call the toString()
            // method to enforce. We also probably don't need to check for null here
            // but we do anyway because Bundle.get() can return null.
            if (value != null) formList.add(new BasicNameValuePair(key, value.toString()));
        }
        
        return formList;
    }
}
