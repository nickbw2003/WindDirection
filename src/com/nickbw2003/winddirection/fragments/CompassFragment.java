package com.nickbw2003.winddirection.fragments;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nickbw2003.winddirection.R;
import com.nickbw2003.winddirection.SettingsActivity;
import com.nickbw2003.winddirection.data.WeatherInfo;
import com.nickbw2003.winddirection.helpers.CompassHelper;
import com.nickbw2003.winddirection.helpers.CompassListener;
import com.nickbw2003.winddirection.helpers.LocationHelper;
import com.nickbw2003.winddirection.helpers.LocationHelper.LocationResult;
import com.nickbw2003.winddirection.helpers.WeatherInfoHelper;
import com.nickbw2003.winddirection.helpers.WeatherInfoListener;
import com.nickbw2003.winddirection.tasks.LocationGeocoderTask;
import com.nickbw2003.winddirection.tasks.StringGeocoderTask;
import com.nickbw2003.winddirection.views.CompassView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CompassFragment extends SherlockFragment implements CompassListener, WeatherInfoListener {
	private CompassHelper _compassHelper;
	private Address _address;
	
	private WeatherInfoHelper _weatherInfoHelper;
	
	private MenuItem _menuReload;
	private MenuItem _menuReloading;
	
	private TextView _titleLabel;
	private CompassView _compassView;
	
	private SharedPreferences _preferences;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		_compassHelper = new CompassHelper(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_detail, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());		
		_compassView = (CompassView)view.findViewById(R.id.compassView);
		
		setRefreshButtonOnClick(view);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.activity_main, menu);
		
		_menuReload = (MenuItem)menu.findItem(R.id.menu_reload);
		_menuReloading = (MenuItem)menu.findItem(R.id.menu_reloading);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		if (item.getItemId() == R.id.menu_reload)
		{			
			loadData();
		}
		
		return true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		_compassHelper.registerListener(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		_compassHelper.unregisterListener();
	}

	@Override
	public void onAzimutUpdate(float azimut) {
		if (_titleLabel != null) {
			_titleLabel.setText(String.valueOf(azimut));
		}
		
		if (_compassView != null) {
			_compassView.updateAzimut(azimut);
		}
	}
	
	@Override
	public void onWeatherInfoUpdate(WeatherInfo weatherInfo) {
		TextView lblCurrentWindDirection = (TextView)getActivity().findViewById(R.id.lblCurrentWindDirection);
		TextView lblCurrentWindSpeed = (TextView)getActivity().findViewById(R.id.lblCurrentWindSpeed);
		CompassView compassView = (CompassView)getActivity().findViewById(R.id.compassView);
		
		if (weatherInfo != null) {						
			lblCurrentWindDirection.setText(getLocalizedWindDirection(weatherInfo.getWindDirectionString()));
			lblCurrentWindSpeed.setText(String.format("%s km/h", weatherInfo.getWindSpeed()));
			compassView.updateWindDirection(weatherInfo.getWindDirection());
		}
		else {
			lblCurrentWindDirection.setText(getString(R.string.noData));
			lblCurrentWindSpeed.setText(getString(R.string.noData));
		}
		
		setReloadingState(false);
	}
	
	private void onLocationUpdate(Address address) {
		TextView lblCurrentLocation = (TextView)getActivity().findViewById(R.id.lblCurrentLocation);
		
		if (address != null) {
			_address = address;							
			lblCurrentLocation.setText(LocationHelper.getLocationName(_address, true));
			
			_weatherInfoHelper = new WeatherInfoHelper(getString(R.string.webServiceBaseUrl), _address);
			_weatherInfoHelper.registerListener(this);
		}
		else {
			lblCurrentLocation.setText(getString(R.string.noLocation));
			setReloadingState(false);
		}
	}
	
	private void loadData() {
		setReloadingState(true);
		
		if (_preferences.getBoolean(SettingsActivity.KEY_PREF_AUTOLOCATION, true))
		{
			getLocationByGpsOrNetwork();
		}
		else
		{
			getLocationByPreferences();			
		}
	}	
	
	private void setReloadingState(boolean reloading) {
		LinearLayout llContent = (LinearLayout)getActivity().findViewById(R.id.llContent);
		RelativeLayout rlSplashScreen = (RelativeLayout)getActivity().findViewById(R.id.rlSplashScreen);
		ImageButton btnRefresh = (ImageButton)getActivity().findViewById(R.id.btnRefresh);
		ProgressBar pbRefreshing = (ProgressBar)getActivity().findViewById(R.id.pbRefreshing);
		
		_menuReload.setVisible(!reloading);
		_menuReloading.setVisible(reloading);
				
		if (reloading) {
			btnRefresh.setVisibility(View.INVISIBLE);
			llContent.setVisibility(View.INVISIBLE);
			
			pbRefreshing.setVisibility(View.VISIBLE);
			rlSplashScreen.setVisibility(View.VISIBLE);
						
			_menuReloading.setActionView(R.layout.menu_progress);
		}
		else {
			pbRefreshing.setVisibility(View.INVISIBLE);
			rlSplashScreen.setVisibility(View.GONE);
			
			btnRefresh.setVisibility(View.VISIBLE);
			llContent.setVisibility(View.VISIBLE);
									
			_menuReload.setIcon(R.drawable.ic_action_refresh);
			_menuReloading.setActionView(null);
			
			View control = null;			
			for (int i = 0; i < llContent.getChildCount(); i++)
			{
				control = llContent.getChildAt(i);
				control.setVisibility(View.VISIBLE);
			}
		}	
	}
	
	private void getLocationByGpsOrNetwork() {
		LocationResult locationResult = new LocationResult() {
			@Override
			public void gotLocation(Location location) {
				LocationGeocoderTask locationGeocoderTask = new LocationGeocoderTask(getActivity()) {
					@Override
					protected void onPostExecute(Address result) {		
						onLocationUpdate(result);
					}
				};
				locationGeocoderTask.execute(location);
			}
		};
		
		LocationHelper locationHelper = new LocationHelper(getActivity(), locationResult);
		locationHelper.getCurrentLocation();
	}
	
	private void getLocationByPreferences() {
		String locationString = _preferences.getString(SettingsActivity.KEY_PREF_LOCATION_NAME, "-");
		
		StringGeocoderTask stringGeocoderTask = new StringGeocoderTask(getActivity()) {
			protected void onPostExecute(Address result) {
				TextView lblCurrentLocation = (TextView)getActivity().findViewById(R.id.lblCurrentLocation);
				
				if (result != null) {		
					_address = result;
					
					Editor editor = _preferences.edit();					
					editor.putString(SettingsActivity.KEY_PREF_LOCATION_NAME, LocationHelper.getLocationName(_address, false));
					editor.commit();
											
					onLocationUpdate(result);
				}
				else {
					lblCurrentLocation.setText(getString(R.string.noLocation));
					setReloadingState(false);
				}
			}
		};
		stringGeocoderTask.execute(locationString);
	}

	private void setRefreshButtonOnClick(View view)
	{
		ImageButton btnRefresh = (ImageButton)view.findViewById(R.id.btnRefresh);
        
		btnRefresh.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) 
            {   
            	loadData();
            }            
        });
	}
	
	private String getLocalizedWindDirection(String shortWindDirectionString)
	{		
		int resId = getResources().getIdentifier("windDirection" + shortWindDirectionString, "string", getActivity().getPackageName());
		return getString(resId);
	}
}