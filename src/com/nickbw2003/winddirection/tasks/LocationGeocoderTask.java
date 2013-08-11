package com.nickbw2003.winddirection.tasks;

import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

public class LocationGeocoderTask extends AsyncTask<Location, Integer, Address> 
{
	private Context _appContext;
	
	public LocationGeocoderTask(Context appContext)
	{
		_appContext = appContext;
	}
	
	@Override
	protected Address doInBackground(Location... params) 
	{
		Location location = params[0];
		Address address = null;
		
		if (location != null)
		{
			Geocoder geocoder = new Geocoder(_appContext);
			List<Address> addresses = null;
			
			try
			{
				addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
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
