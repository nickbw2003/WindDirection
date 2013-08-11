package com.nickbw2003.winddirection.tasks;

import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

public class StringGeocoderTask extends AsyncTask<String, Integer, Address>
{
	private Context _appContext;
	
	public StringGeocoderTask(Context appContext)
	{
		_appContext = appContext;
	}
	
	@Override
	protected Address doInBackground(String... params) 
	{
		String locationString = params[0];
		Address address = null;
		
		if (locationString != null && locationString.length() > 0)
		{
			Geocoder geocoder = new Geocoder(_appContext);
			List<Address> addresses = null;
			
			try
			{
				addresses = geocoder.getFromLocationName(locationString, 1);
			}
			catch(Exception ex)
			{}
			
			if (addresses != null && addresses.size() > 0 && addresses.get(0) != null)
			{			
				address = addresses.get(0);
			}
		}
		
		return address;
	}
}
