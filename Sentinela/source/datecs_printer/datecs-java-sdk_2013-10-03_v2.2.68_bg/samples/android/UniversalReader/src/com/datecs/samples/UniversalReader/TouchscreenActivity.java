package com.datecs.samples.UniversalReader;

import java.io.IOException;

import com.datecs.api.universalreader.TouchEvent;
import com.datecs.api.universalreader.UniversalReader;
import com.datecs.api.universalreader.UniversalReaderException;
import com.datecs.api.universalreader.UniversalReader.TouchScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.PorterDuff;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TouchscreenActivity extends Activity {	
	private class TouchscreenThread extends Thread {
		private volatile boolean mRun = true;
						
		@Override
		public void run() {
			boolean pressed = false;
			TouchEvent[] events = null;
			TouchScreen touchscreen = getUniversalReader().getTouchScreen();
			
			try {				
				while (mRun) {									
					
					if (mRun) {
						events = touchscreen.getEvents();						
						if (events != null) {
							appendCoordinates(events);
						}
					}
					
					if (mRun) {
						pressed = getUniversalReader().isButtonPressed(false);
						if (pressed) {
							clearCoordinates();							
						}
					}
										
					if (mRun) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (IOException e) {						
				e.printStackTrace();			
				cancelActivity(e.getMessage(), 0);				
				return;
			} catch (UniversalReaderException e) {						
				e.printStackTrace();
				cancelActivity(e.getMessage(), e.getErrorCode());
			}			
		}
		
		public void finish() {
			mRun = false;
		}
	}
	
	private TouchscreenView mTouchscreenView;
	private TouchscreenThread mThread;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touchscreen);
        setResult(RESULT_OK);
        
        mTouchscreenView = new TouchscreenView(this);
        
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LinearLayout llMain = (LinearLayout)findViewById(R.id.linearlayout_main);
        llMain.addView(mTouchscreenView, params);                
    }
    
    @Override
	protected void onResume() {
    	mThread = new TouchscreenThread();
    	mThread.start();
		super.onResume();
	}

	@Override
	protected void onPause() {
		mThread.finish();
		try {
			mThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, R.string.clear);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == Menu.FIRST) {
			clearCoordinates();
			return true;
		}
		return false;
	}
	
	private UniversalReader getUniversalReader() {
		return UniversalReaderActivity.sUniversalReader;
	}
	
	private void appendCoordinates(TouchEvent[] events) {
		for (TouchEvent event : events) {
			mTouchscreenView.append(event);
		}
    }	
	
	private void clearCoordinates() {		
		mTouchscreenView.clear();		
    }	
	
	private void cancelActivity(String message, int errorCode) {
        Intent data = new Intent();
        toast(message + ": " + errorCode);
        setResult(UniversalReaderActivity.RESULT_ERROR, data);
        finish();
    }
    
    private void toast(final String text) {
        runOnUiThread(new Runnable() {            
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

final class TouchscreenView extends View {
	private static final int TS_WIDTH = 256;
	private static final int TS_HEIGHT = 192;		
	
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private TouchEvent mSavedEvent;
	private Paint mPaint;
	
	public TouchscreenView(Context context) {
		super(context);	
		
		mBitmap = Bitmap.createBitmap(TS_WIDTH, TS_HEIGHT, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mSavedEvent = null;
		mPaint = new Paint();
		
		clear();		
	}	
	
	@Override
	protected void onDraw(Canvas canvas) {
		float x, y;
		x = (getWidth() - TS_WIDTH) / 2; 
		y = (getHeight() - TS_HEIGHT) / 2;
		
		synchronized (mBitmap) {
			canvas.drawBitmap(mBitmap, x, y, mPaint);
		}		
	}			
	
	public void clear() {		
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.WHITE);
		paint.setAlpha(getResources().getColor(R.color.translucent_color) >> 24);
		paint.setStrokeWidth(1);
		paint.setPathEffect(new DashPathEffect(new float[]{ 5.0f, 5.0f }, 0));
		
		synchronized (mBitmap) {
			mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
			mCanvas.drawRect(0, 0, TS_WIDTH - 1, TS_HEIGHT - 1, paint);
		}
		
		postInvalidate();
	}
	
	public void append(TouchEvent event) {
		if (event.getAction() == TouchEvent.ACTION_DOWN) {
			if (mSavedEvent != null) {
				Paint paint = new Paint();
				paint.setStyle(Paint.Style.STROKE);
				paint.setColor(Color.WHITE);
				paint.setAlpha(getResources().getColor(R.color.translucent_color) >> 24);
				paint.setStrokeWidth(1);
				
				int fromX =  mSavedEvent.getX();
				int fromY =  mSavedEvent.getY();
				int toX =  event.getX();
				int toY =  event.getY();
				
				synchronized (mBitmap) {
					mCanvas.drawLine(fromX, fromY, toX, toY, paint);
				}
				
				postInvalidate();
			}
			
			mSavedEvent = event;
		} else {
			mSavedEvent = null;
		}					
	}	
}