package com.datecs.samples.UniversalReader;

import java.io.IOException;

import com.datecs.api.smartcard.AnswerToReset;
import com.datecs.api.universalreader.UniversalReader;
import com.datecs.api.universalreader.UniversalReaderException;
import com.datecs.api.universalreader.UniversalReader.SmartCardReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SmartCardActivity extends Activity {
	
    private class SmartCardThread extends Thread {
		private volatile boolean mRun = true;
						
		@Override
		public void run() {
		    SmartCardReader smartcard = getUniversalReader().getSmartCardReader();
		    
			try {
			   while (mRun) {							
					boolean cardPresent;
															
					cardPresent = false;
					setCaption(true);
					while (mRun && cardPresent == false) {
						if (mRun) {
							cardPresent = smartcard.isCardPresent();
						}
						
						if (mRun) {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {									
								e.printStackTrace();
							}
						}
					}
					
					if (mRun) {
						AnswerToReset atr;
						smartcard.setPower(true);
						atr = smartcard.reset();
						smartcard.setPower(false);
						setAnswerToReset(atr);
						setCaption(false);
					}
										
					while (mRun && cardPresent) {
						if (mRun) {
							cardPresent = smartcard.isCardPresent();
						}
						
						if (mRun) {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {									
								e.printStackTrace();
							}
						}
					}
					
					setAnswerToReset(null);					
				}
			} catch (IOException e) {						
				e.printStackTrace();	
				cancelActivity(e.getMessage(), 0);		
			} catch (UniversalReaderException e) {						
				e.printStackTrace();			
				cancelActivity(e.getMessage(), e.getErrorCode());		
			}			
		}
		
		public void finish() {
			mRun = false;
		}
	}
	
	private Handler mHandler;
	private SmartCardThread mThread;	
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartcard);
        setResult(RESULT_OK);
        
        mHandler = new Handler();
    }
    
    @Override
	protected void onResume() {
    	if (getUniversalReader() != null) {
    		mThread = new SmartCardThread();
    		mThread.start();
    	} else {
    		finish();
    	}
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

	private UniversalReader getUniversalReader() {
		return UniversalReaderActivity.sUniversalReader;
	}
    
    private void setCaption(final boolean put) {
    	final TextView tvCaption = (TextView)findViewById(R.id.caption);
    	final LinearLayout llData = (LinearLayout)findViewById(R.id.section_data);
    	
    	mHandler.post(new Runnable() {			
			@Override
			public void run() {
				if (put) {
					llData.setVisibility(View.INVISIBLE);
					tvCaption.setText(R.string.put_card);					
				} else {					
					llData.setVisibility(View.VISIBLE);
					tvCaption.setText(R.string.remove_card);
				}
			}
		});
    }
    
    private void setAnswerToReset(final AnswerToReset atr) {
    	final TextView tvData = (TextView)findViewById(R.id.data);
    	
		mHandler.post(new Runnable() {			
			@Override
			public void run() {
				if (atr != null) {
					StringBuffer sb = new StringBuffer();
					for (Byte b : atr.getData()) {
						sb.append(String.format("%02X ", (int)b & 0xff));
					}
					tvData.setText(sb.toString());
				} else {
					tvData.setText("");
				}
			}
		});
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
