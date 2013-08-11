package com.nickbw2003.winddirection.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class SquareLayout extends LinearLayout 
{
	public SquareLayout(Context context, AttributeSet attrs) 
	{
		super(context, attrs);		
	}
	
	public SquareLayout(Context context) 
	{
		super(context);		
	}
	
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
    {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec); 
    }
}