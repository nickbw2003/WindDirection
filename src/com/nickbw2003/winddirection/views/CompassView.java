package com.nickbw2003.winddirection.views;

import com.nickbw2003.winddirection.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class CompassView extends View {
	private Paint _paint;
	private Float _azimut;
	private Bitmap _compassBitmap;
	
	private Double _windDirection;
	private boolean _firstDraw;
	
	public CompassView(Context context) {
		super(context);
		init();
	}
	
	public CompassView(Context context, AttributeSet attributes) {
		super(context, attributes);
		init();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		if (_compassBitmap == null) {
			_compassBitmap = createScaledBitmap(w, h);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
	  if (_compassBitmap != null && _azimut != null) {
		  int width = getWidth();
		  int height = getHeight();
		  int centerX = width / 2;
		  int centerY = height / 2;
		  		  
		  canvas.save();		  
		  canvas.rotate(-_azimut * 360 / (2 * (float)Math.PI), centerX, centerY);		  
		  canvas.drawBitmap(_compassBitmap, 0, 0, _paint);
		  
		  if (_windDirection != null) {
			  float radiusCompass;
			  if(centerX > centerY)
			  {
				  radiusCompass = (float) (centerY * 0.9);
			  }
			  else
			  {
				  radiusCompass = (float) (centerX * 0.9);
			  }
			
			  if(!_firstDraw)
			  {
				  canvas.drawLine(centerX, centerY, (float)(centerX + radiusCompass * Math.sin((double)(_windDirection) * 3.14/180)), (float)(centerY - radiusCompass * Math.cos((double)(_windDirection) * 3.14/180)), _paint);			
			  }
		  }
	  }
	}
	
	private void init() {
		_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		_paint.setStyle(Paint.Style.STROKE);
		_paint.setStrokeWidth(5);
		_paint.setColor(Color.RED);
		_paint.setTextSize(30);

		_firstDraw = true;
		_windDirection = null;
	}
	
	private Bitmap createScaledBitmap(int width, int height) {
		Drawable compass = getResources().getDrawable(R.drawable.compass);
		
		if (compass != null && compass instanceof BitmapDrawable && ((BitmapDrawable)compass).getBitmap() != null) {
			return Bitmap.createScaledBitmap(((BitmapDrawable)compass).getBitmap(), width, height, true);
		}
		
		return null;
	}
	
	public void updateAzimut(float azimut) {
		_azimut = azimut;
		invalidate();
	}
	
	public void updateWindDirection(Double windDirection) {
		_firstDraw = false;
		_windDirection = windDirection;
		invalidate();
	}
}