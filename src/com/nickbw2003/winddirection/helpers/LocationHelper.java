package com.nickbw2003.winddirection.helpers;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationHelper {
	private Context _appContext;
	private Timer _locationTimer;
	private LocationManager _locationManager;
	private boolean _gpsProviderIsEnabled;
	private boolean _networkProviderIsEnabled;
	private LocationResult _locationResult;	
	
	public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }
	private class GetLastLocation extends TimerTask {    
    	@Override
        public void run() {
             _locationManager.removeUpdates(_locationListenerGps);
             _locationManager.removeUpdates(_locationListenerNetwork);

             Location gpsLocation = null;
             Location networkLocation = null;
                          
             if(_gpsProviderIsEnabled) {
            	 gpsLocation = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
             }
             else if(_networkProviderIsEnabled) {
            	 networkLocation = _locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
             }

             if(gpsLocation != null && networkLocation != null) {
                 if(gpsLocation.getTime() > networkLocation.getTime()) {
                	 _locationResult.gotLocation(gpsLocation);
                 }
                 else {
                	 _locationResult.gotLocation(networkLocation);
                 }
                 
                 return;
             }

             if(gpsLocation != null) {
            	 _locationResult.gotLocation(gpsLocation);
                 return;
             }
             if(networkLocation != null) {
            	 _locationResult.gotLocation(networkLocation);
                 return;
             }
             
             _locationResult.gotLocation(null);
        }
    }
	
	public LocationHelper(Context appContext, LocationResult result) {		
		_appContext = appContext;
		_locationResult = result;
	}
	
	public boolean getCurrentLocation() {		
		if (_locationManager == null) {
			_locationManager = getLocationManagerInstance();
		}	
        
        try {
        	_gpsProviderIsEnabled = _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch(Exception ex) {
        }
        
        try {
        	_networkProviderIsEnabled = _locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception ex) {        	
        }

        if (_gpsProviderIsEnabled || _networkProviderIsEnabled) {   		
			if (_gpsProviderIsEnabled) {
	    		getCurrentLocationByGps();
	    	}
	    	
			if (_networkProviderIsEnabled) {
	    		getCurrentLocationByNetwork();
	    	}
	    	        		
	    	_locationTimer = new Timer();
	        _locationTimer.schedule(new GetLastLocation(), 20000);
	        
	        return true;        	
        }
        
        return false;
    }
	
	public void cancelTimer()  { 
		_locationTimer.cancel(); 
		_locationManager.removeUpdates(_locationListenerGps); 
		_locationManager.removeUpdates(_locationListenerNetwork); 
	}
	
	public String getLocationName(double latitude, double longitude) {
		Geocoder geocoder = new Geocoder(_appContext, Locale.getDefault());
		
		try {
			List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
			
			if (addresses != null && addresses.size() > 0) {
				return addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
			}
		}
		catch (Exception ex) {
		}
		
		return "";
	}
		
	public static String getLocationName(Address address, boolean withBreaks) {
		StringBuilder strBuilder = new StringBuilder();
		
		if (address.getLocality() == null || (address.getLocality() != null && address.getLocality().trim() == "")) {			
			strBuilder.append((address.getFeatureName() == null || address.getFeatureName() == "") ? "" : address.getFeatureName() + ", " + (withBreaks ? "\r\n" : ""));
		}
		
		strBuilder.append(address.getLocality() == null || address.getLocality() == "" ? "" : address.getLocality() + ", " + (withBreaks ? "\r\n" : ""));
		strBuilder.append(address.getAdminArea() == null || address.getAdminArea() == "" ? "" : address.getAdminArea() + ", " + (withBreaks ? "\r\n" : ""));
		strBuilder.append(address.getCountryName() == null || address.getCountryName() == "" ? "" : address.getCountryName());
			
		if (strBuilder.toString().startsWith(", ") || strBuilder.toString().startsWith(", \r\n")) {
			strBuilder.replace(strBuilder.toString().indexOf(", "), strBuilder.toString().indexOf(", ") + 1, "" );
		}
		
		if (strBuilder.toString().endsWith(", ") || strBuilder.toString().endsWith(", \r\n")) {
			strBuilder.replace(strBuilder.toString().lastIndexOf(", "), strBuilder.toString().lastIndexOf(", ") + 1, "" );
		}
		
		return strBuilder.toString().trim();
	}
	
	private LocationListener _locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
        	_locationTimer.cancel();
        	_locationResult.gotLocation(location);
            _locationManager.removeUpdates(this);
            _locationManager.removeUpdates(_locationListenerNetwork);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };
	
    private LocationListener _locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
        	_locationTimer.cancel();
        	_locationResult.gotLocation(location);
            _locationManager.removeUpdates(this);
            _locationManager.removeUpdates(_locationListenerGps);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };
        
	private LocationManager getLocationManagerInstance() {
		return (LocationManager) _appContext.getSystemService(Context.LOCATION_SERVICE);
	}
	
	private void getCurrentLocationByGps() {
		if (_locationManager == null) {
			_locationManager = getLocationManagerInstance();
		}
		
		_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, _locationListenerGps);
	}
	
	private void getCurrentLocationByNetwork() {
		if (_locationManager == null) {
			_locationManager = getLocationManagerInstance();
		}
		
		_locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, _locationListenerNetwork);
	}	
}