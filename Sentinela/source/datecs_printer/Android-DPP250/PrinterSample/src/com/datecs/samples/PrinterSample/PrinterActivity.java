package com.datecs.samples.PrinterSample;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.datecs.android.bluetooth.BluetoothConnector;
import com.datecs.api.card.FinancialCard;
import com.datecs.api.printer.Printer;
import com.datecs.api.printer.PrinterInformation;
import com.datecs.api.printer.ProtocolAdapter;
import com.datecs.api.printer.ProtocolAdapter.Channel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class PrinterActivity extends Activity {
	public static final String CONNECTION_STRING = "connection_string";
	
	private final Handler mHandler = new Handler();
	
	private final Thread mConnectThread = new Thread() {
		@Override
		public void run() {
		    String connectionString = getIntent().getStringExtra(CONNECTION_STRING);
		    
		    showProgress(R.string.connecting);
		    
            if (connectionString.startsWith("bth://")) {
                String address = connectionString.substring(6);
                connectBth(address);
            } else {
                throw new IllegalArgumentException("Unsupported connection string");
            }
            
            dismissProgress();
		}	
		
		void connectBth(String address) {
		    setPrinterInfo(R.drawable.help, address);            
		    
		    try {
	            mBthConnector = BluetoothConnector.getConnector(PrinterActivity.this);
	            mBthConnector.connect(address);
	            mPrinter = getPrinter(
	                    mBthConnector.getInputStream(), 
	                    mBthConnector.getOutputStream());	            
	        } catch (IOException e) {
	            error(R.drawable.bluetooth, e.getMessage());	            
	            return;
	        }	        	        
	        
	        mPrinterInfo = getPrinterInfo();	        
		}
		
		Printer getPrinter(InputStream in, OutputStream out) throws IOException {
	        ProtocolAdapter adapter = new ProtocolAdapter(in, out);
	        Printer printer = null;

	        if (adapter.isProtocolEnabled()) {
	            Channel channel = adapter.getChannel(ProtocolAdapter.CHANNEL_PRINTER);
	            InputStream newIn = channel.getInputStream();
	            OutputStream newOut = channel.getOutputStream();
	            printer = new Printer(newIn, newOut);
	        } else {
	            printer = new Printer(in, out);
	        }

	        return printer;
	    }
		
		PrinterInformation getPrinterInfo() {
		    PrinterInformation pi = null;
		    
		    try {
		        pi = mPrinter.getInformation();
                setPrinterInfo(R.drawable.printer, pi.getName());                
            } catch (IOException e) {
                e.printStackTrace();                                
            }
            
            return pi;
		}
	};
	
	private BluetoothConnector mBthConnector;
	private Printer mPrinter;
	private PrinterInformation mPrinterInfo;
	private ProgressDialog mProgressDialog;
        	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.printer);
        
        findViewById(R.id.print_self_test).setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
			    new Thread() {                  
                    @Override
                    public void run() {
                        showProgress(R.string.printing_self_test);
                        doPrintSelfTest();
                        dismissProgress();
                    }
                }.start();   				
			}        	
        });
        
        findViewById(R.id.print_text).setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
			    new Thread() {                  
                    @Override
                    public void run() {
                        showProgress(R.string.printing_text);
                        doPrintText();
                        dismissProgress();
                    }
                }.start();								
			}        	
        });
        
        findViewById(R.id.print_image).setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
			    new Thread() {                  
                    @Override
                    public void run() {
                        showProgress(R.string.printing_image);
                        doPrintImage(); 
                        dismissProgress();
                    }
                }.start();     							
			}        	
        });    
        
        findViewById(R.id.print_page).setOnClickListener(new OnClickListener() {
            // @Override
            public void onClick(View v) {
                new Thread() {                  
                    @Override
                    public void run() {
                        showProgress(R.string.printing_page);
                        doPrintPage();
                        dismissProgress();
                    }
                }.start();                                 
            }           
        });    
        
        findViewById(R.id.print_barcode).setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
			    new Thread() {                  
                    @Override
                    public void run() {
                        showProgress(R.string.printing_barcode);
                        doPrintBarcode(); 
                        dismissProgress();
                    }
                }.start(); 				
			}        	
        });   
                        
        findViewById(R.id.read_card).setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
				new Thread() {					
					@Override
					public void run() {
						showProgress(R.string.reading_card);
						doReadMagstripe();
						dismissProgress();
					}
				}.start();					
			}
        });
        
        findViewById(R.id.read_barcode).setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
				new Thread() {					
					@Override
					public void run() {
						showProgress(R.string.read_barcode);
						doReadBarcode();
						dismissProgress();
					}
				}.start();					
			}        	
        });                
    }

    @Override
	protected void onStart() {
        super.onStart();
        mConnectThread.start();        
	}	

	@Override
    protected void onStop() {
	    super.onStop();
	    
	    if (mBthConnector != null) {
	        try {
                mBthConnector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }	        
	    }		
    }   
		
    private void showProgress(final String text) {
        mHandler.post(new Runnable() {
           // @Override
            public void run() {
                mProgressDialog = ProgressDialog.show(
                        PrinterActivity.this,
                        getString(R.string.please_wait), 
                        text,
                        true);                            
            }           
        });             
    }
    
	private void showProgress(int resId) {
	    showProgress(getString(resId));
    }
        
    private void dismissProgress() {
        mHandler.post(new Runnable() {
         //   @Override
            public void run() {                
                mProgressDialog.dismiss();             
            }           
        });     
    }            
    
	private void doPrintSelfTest() {
		try {			
			mPrinter.printSelfTest();						
    	} catch (IOException e) {
            error(R.drawable.selftest, getString(R.string.failed_print_self_test) + ". " + 
                    e.getMessage());
    	}
	}
	
	private void doPrintText() {
		StringBuffer sb = new StringBuffer();
		sb.append("{reset}{center}{w}{h}RECEIPT");
        sb.append("{br}");
        sb.append("{br}");
        sb.append("{reset}1. {b}First item{br}");
        sb.append("{reset}{right}{h}$0.50 A{br}");
        sb.append("{reset}2. {u}Second item{br}");
        sb.append("{reset}{right}{h}$1.00 B{br}");
        sb.append("{reset}3. {i}Third item{br}");
        sb.append("{reset}{right}{h}$1.50 C{br}");
        sb.append("{br}");
        sb.append("{reset}{right}{w}{h}TOTAL: {/w}$3.00  {br}");            
        sb.append("{br}");
        sb.append("{reset}{center}{s}Thank You!{br}");
                            
    	try {    		
    		mPrinter.reset();  
    		mPrinter.printTaggedText(sb.toString());    		
    		mPrinter.feedPaper(110);
    	} catch (IOException e) {
    	    error(R.drawable.text, getString(R.string.failed_print_text) + ". " + 
                    e.getMessage());    		
    	}
	}
	
	private void doPrintImage() {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample);
		final int width = bitmap.getWidth();
		final int height = bitmap.getHeight();
		final int[] argb = new int[width * height];		
		bitmap.getPixels(argb, 0, width, 0, 0, width, height);				
				
		try {
		    mPrinter.reset();		     
    		mPrinter.printImage(argb, width, height, Printer.ALIGN_LEFT, true);
    		mPrinter.feedPaper(110);		    
        } catch (IOException e) {
            error(R.drawable.image, getString(R.string.failed_print_image) + ". " + 
                    e.getMessage());
        }
	}
	
	private void doPrintPage() {
	    if (mPrinterInfo == null || !mPrinterInfo.isPageModeSupported()) {
	        dialog(R.drawable.page, 
	                getString(R.string.warning), 
	                getString(R.string.unsupport_page_mode));
	        return;
	    }
	            
        try {
            mPrinter.reset();            
            mPrinter.selectPageMode();   
            
            mPrinter.setPageRegion(0, 0, 160, 320, Printer.PAGE_LEFT);            
            mPrinter.setPageXY(0, 4);            
            mPrinter.printTaggedText("{reset}{center}{b}PARAGRAPH I{br}");
            mPrinter.drawPageRectangle(0, 0, 160, 32, Printer.FILL_INVERTED);            
            mPrinter.setPageXY(0, 34);
            mPrinter.printTaggedText("{reset}Text printed from left to right" +
                    ", feed to bottom. Starting point in left top corner of the page.{br}");
            mPrinter.drawPageFrame(0, 0, 160, 320, Printer.FILL_BLACK, 1);
            
            mPrinter.setPageRegion(160, 0, 160, 320, Printer.PAGE_TOP);            
            mPrinter.setPageXY(0, 4);            
            mPrinter.printTaggedText("{reset}{center}{b}PARAGRAPH II{br}");
            mPrinter.drawPageRectangle(160 - 32, 0, 32, 320, Printer.FILL_INVERTED);            
            mPrinter.setPageXY(0, 34);
            mPrinter.printTaggedText("{reset}Text printed from top to bottom" +
                    ", feed to left. Starting point in right top corner of the page.{br}");
            mPrinter.drawPageFrame(0, 0, 160, 320, Printer.FILL_BLACK, 1);
            
            mPrinter.setPageRegion(160, 320, 160, 320, Printer.PAGE_RIGHT);            
            mPrinter.setPageXY(0, 4);            
            mPrinter.printTaggedText("{reset}{center}{b}PARAGRAPH III{br}");
            mPrinter.drawPageRectangle(0, 320 - 32, 160, 32, Printer.FILL_INVERTED);            
            mPrinter.setPageXY(0, 34);
            mPrinter.printTaggedText("{reset}Text printed from right to left" +
                    ", feed to top. Starting point in right bottom corner of the page.{br}");
            mPrinter.drawPageFrame(0, 0, 160, 320, Printer.FILL_BLACK, 1);
            
            mPrinter.setPageRegion(0, 320, 160, 320, Printer.PAGE_BOTTOM);            
            mPrinter.setPageXY(0, 4);            
            mPrinter.printTaggedText("{reset}{center}{b}PARAGRAPH IV{br}");
            mPrinter.drawPageRectangle(0, 0, 32, 320, Printer.FILL_INVERTED);            
            mPrinter.setPageXY(0, 34);
            mPrinter.printTaggedText("{reset}Text printed from bottom to top" +
                    ", feed to right. Starting point in left bottom corner of the page.{br}");
            mPrinter.drawPageFrame(0, 0, 160, 320, Printer.FILL_BLACK, 1);
            
            mPrinter.printPage();
            mPrinter.selectStandardMode();
            mPrinter.feedPaper(110);          
        } catch (IOException e) {
            error(R.drawable.page, getString(R.string.failed_print_page) + ". " + 
                    e.getMessage());            
        }
    }
	
	private void doPrintBarcode() {
		try {    		
			mPrinter.reset();
						
			mPrinter.setBarcode(Printer.ALIGN_CENTER, false, 2, Printer.HRI_BELOW, 100);
			mPrinter.printBarcode(Printer.BARCODE_EAN13, "123456789012");
			mPrinter.feedPaper(38);
			
			mPrinter.setBarcode(Printer.ALIGN_CENTER, false, 2, Printer.HRI_BOTH, 100);
			mPrinter.printBarcode(Printer.BARCODE_CODE128, "ABCDEF123456");
			mPrinter.feedPaper(38);
						
			mPrinter.setBarcode(Printer.ALIGN_CENTER, false, 2, Printer.HRI_NONE, 100);
			mPrinter.printBarcode(Printer.BARCODE_PDF417, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
			mPrinter.feedPaper(38);
			
			mPrinter.setBarcode(Printer.ALIGN_CENTER, false, 2, Printer.HRI_NONE, 100);
			mPrinter.printQRCode(4, 3, "http://www.datecs.bg");
			mPrinter.feedPaper(38);
			
			mPrinter.feedPaper(110);
    	} catch (IOException e) {
    	    error(R.drawable.barcode, getString(R.string.failed_print_barcode) + ". " +
                    e.getMessage());    	    
    	}
	}

	private void doReadMagstripe() {
		String[] tracks = null;
		FinancialCard card = null;		
		
    	try {    	    
    	    if (mPrinterInfo != null && mPrinterInfo.getName().startsWith("CMP-10")) {
    	        // The printer CMP-10 can read only two tracks at once.
    	        tracks = mPrinter.readCard(true, true, false, 15000);
    	    } else {
    	        tracks = mPrinter.readCard(true, true, true, 15000);
    	    }
    	} catch (IOException e) { 
    		error(R.drawable.card, getString(R.string.read_card) + ". " + 
                    e.getMessage());
    	}    	
    	
    	if (tracks != null) {
    		StringBuffer msg = new StringBuffer();
    		 
	    	if (tracks[0] == null && tracks[1] == null && tracks[2] == null) {
	    		msg.append(getString(R.string.no_card_read));
	    	} else {
	    		if (tracks[0] != null) {
	    			card = new FinancialCard(tracks[0]);
	    		} else if (tracks[1] != null) {
	    			card = new FinancialCard(tracks[1]);
	    		}
	    		
	    		if (card != null) {
	    			msg.append(getString(R.string.card_no) + ": " + card.getNumber());
	    			msg.append("\n");
	    			msg.append(getString(R.string.holder) + ": " + card.getName());
	    			msg.append("\n");
	    			msg.append(getString(R.string.exp_date) + ": " + String.format("%02d/%02d", 
	    					card.getExpiryMonth(), card.getExpiryYear()));
	    			msg.append("\n");	    			
	    		}
	    		
	    		if (tracks[0] != null) {
	    			msg.append("\n");
	    			msg.append(tracks[0]);
	    			
	    		}
	    		if (tracks[1] != null) {
	    			msg.append("\n");
	    			msg.append(tracks[1]);	    			
	    		}	    		
	    		if (tracks[2] != null) {
	    			msg.append("\n");
	    			msg.append(tracks[2]);
	    		}	    		
	    	}
	    	
	    	dialog(R.drawable.card, 
	    	        getString(R.string.card_info), 
	    	        msg.toString());
    	}
	}	 
	
	private void doReadBarcode() {
		String barcode = null;
		
    	try {
    		barcode = mPrinter.readBarcode(10000);
    	} catch (IOException e) {     		
    		error(R.drawable.readbarcode, getString(R.string.read_barcode) + ". " +
                    e.getMessage());
    	}    	
    	
    	if (barcode != null) {    		
	    	dialog(R.drawable.readbarcode, getString(R.string.barcode), barcode);
    	}
	}	     
	
	private void dialog(final int id, final String title, final String msg) {
        mHandler.post(new Runnable() {
          //  @Override
            public void run() {
                AlertDialog dlg = new AlertDialog.Builder(PrinterActivity.this)
                .setTitle(title)
                .setMessage(msg)               
                .create();  
                dlg.setIcon(id);
                dlg.show();             
            }           
        });             
    }
    
    private void error(final int resIconId, final String message) {
        mHandler.post(new Runnable() {
         //   @Override
            public void run() {             
                AlertDialog dlg = new AlertDialog.Builder(PrinterActivity.this)
                .setTitle("Error")
                .setMessage(message)               
                .create();
                dlg.setIcon(resIconId);
                dlg.setOnDismissListener(new OnDismissListener() {
                 //   @Override
                    public void onDismiss(DialogInterface dialog) {
                        PrinterActivity.this.finish();
                    }                   
                });
                dlg.show();             
            }           
        });             
    }
            
    private void setPrinterInfo(final int resIconId, final String text) {
        mHandler.post(new Runnable() {          
         //   @Override
            public void run() {
                ((ImageView)findViewById(R.id.icon)).setImageResource(resIconId);
                ((TextView)findViewById(R.id.name)).setText(text);
            }
        });
    }      
    
}
