package com.nickbw2003.winddirection.helpers;

import com.google.gson.Gson;
import com.nickbw2003.winddirection.data.WeatherInfo;
import com.nickbw2003.winddirection.tasks.DownloadStringTask;

import android.location.Address;
import android.util.Log;

public class WeatherInfoHelper
{
	private Address _address;
	private String _baseServiceUrl;
	private WeatherInfoListener _listener;
	
	public WeatherInfoHelper(String baseServiceUrl, Address address) {
		init(baseServiceUrl, address);
	}
	
	private void init(String baseServiceUrl, Address address) {
		_baseServiceUrl = baseServiceUrl;
		_address = address;
	}
	
	public void registerListener(WeatherInfoListener listener) {
		_listener = listener;
		
		DownloadStringTask downloadStringTask = new DownloadStringTask() {
			@Override
			protected void onPostExecute(String result) {
				onDownloadJsonFinished(result);
			}
		};
		downloadStringTask.execute(getUrl());
	}
	
	public void unregisterListener() {
		_listener = null;
	}
	
	private void onDownloadJsonFinished(String jsonString) {
		WeatherInfo weatherInfo = null;
		Gson gson = new Gson();
		
		if (jsonString != "") {
			try {
				weatherInfo = gson.fromJson(jsonString, WeatherInfo.class);
			}
			catch (Exception ex) {
				Log.d("WindDirection", ex.getMessage());
			}
		}
		
		if (_listener != null) {
			_listener.onWeatherInfoUpdate(weatherInfo);
		}
	}
	
	private String getUrl() {
		String url = "";
		
		if (_address != null && _baseServiceUrl != null) {
			String locality = _address.getLocality();
			String countryCode = _address.getCountryCode();
						
			if (locality != null && countryCode != null) {
				url = _baseServiceUrl + "/" + locality + "," + countryCode + "?format=json";
			}
		}
		
		return url;
	}
}
