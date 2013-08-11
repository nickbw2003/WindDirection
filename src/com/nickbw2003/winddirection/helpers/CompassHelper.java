package com.nickbw2003.winddirection.helpers;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class CompassHelper implements SensorEventListener {
	private Activity _context;
	
	private SensorManager _sensorManager;
	private Sensor _accelerometer;
	private Sensor _magnetometer;
	
	private CompassListener _listener;
	
	private float[] _gravity;
	private float[] _geomagnetic;
	
	public CompassHelper(Activity context) {
		init(context);
	}
	
	private void init(Activity context) {
		_context = context;
		
		_sensorManager = (SensorManager)_context.getSystemService(Activity.SENSOR_SERVICE);
		_accelerometer = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		_magnetometer = _sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}
	
	public void registerListener(CompassListener listener) {
		_sensorManager.registerListener(this, _accelerometer, SensorManager.SENSOR_DELAY_UI);
		_sensorManager.registerListener(this, _magnetometer, SensorManager.SENSOR_DELAY_UI);
		
		_listener = listener;
	}
	
	public void unregisterListener() {
		_sensorManager.unregisterListener(this);
		
		_listener = null;
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) { }

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			_gravity = event.values;
		}
		else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			_geomagnetic = event.values;
		}
		
		if (_gravity != null && _geomagnetic != null) {
			float[] R = new float[9];
			float[] I = new float[9];
			
			boolean success = SensorManager.getRotationMatrix(R, I, _gravity, _geomagnetic);
			
			if (success) {
				float[] orientation = new float[3];
				
				SensorManager.getOrientation(R, orientation);
								
				if (_listener != null) {
					_listener.onAzimutUpdate(orientation[0]);
				}
			}
		}
	}
}
