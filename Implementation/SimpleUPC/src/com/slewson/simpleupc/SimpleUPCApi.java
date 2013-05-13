package com.slewson.simpleupc;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.widget.Toast;

public class SimpleUPCApi implements LoaderCallbacks<SimpleUPCResponse>
{
	private Uri uri;
	private Bundle params;
	private Bundle args;
	private Context mContext;
	
	private SimpleUPCLoader mLoader;
	
    private static final String ARGS_URI    = "com.slewson.simpleupc.ARGS_URI";
    private static final String ARGS_PARAMS = "com.slewson.simpleupc.ARGS_PARAMS";
	
	public SimpleUPCApi(Context context)
	{
        params = new Bundle();
        params.putString("q", "android");
        
        mContext = context;
        
	}

	@Override
	public Loader<SimpleUPCResponse> onCreateLoader(int id, Bundle args) 
	{
		if (args != null && args.containsKey(ARGS_URI) && args.containsKey(ARGS_PARAMS))
		{
			 Uri action = args.getParcelable(ARGS_URI);
	         Bundle params = args.getParcelable(ARGS_PARAMS);
	            
	         return new SimpleUPCLoader(mContext, SimpleUPCLoader.HTTPVerb.GET, action, params);
		}
		
		return null;
	}

	@Override
	public void onLoadFinished(Loader<SimpleUPCResponse> args, SimpleUPCResponse response) {
        int    code = response.getCode();
        String xml = response.getData();
        
        // Check to see if we got an HTTP 200 code and have some data.
        if (code == 200 && !xml.equals("")) 
        {
        
        }
        else 
        {
        }
	}

	@Override
	public void onLoaderReset(Loader<SimpleUPCResponse> arg0) {
		// TODO Auto-generated method stub
		
	}
}
