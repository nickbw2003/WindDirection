package com.nickbw2003.winddirection.tasks;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class DownloadStringTask extends AsyncTask<String, Integer, String> {

	@Override
	protected String doInBackground(String... params) {
		String result = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		try {
			HttpGet httpGet = new HttpGet(params[0]);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			result = httpClient.execute(httpGet, responseHandler);
		} 
		catch(Exception ex) { 
			Log.d("WindDirection", ex.getMessage());
		}
		
		return result;
	}

}
