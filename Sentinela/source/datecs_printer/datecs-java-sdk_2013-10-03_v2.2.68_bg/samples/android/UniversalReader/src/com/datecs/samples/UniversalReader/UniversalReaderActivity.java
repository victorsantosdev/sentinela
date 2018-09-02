package com.datecs.samples.UniversalReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

import com.datecs.api.BuildInfo;
import com.datecs.api.printer.ProtocolAdapter;
import com.datecs.api.universalreader.UniversalReader;
import com.datecs.api.universalreader.UniversalReaderException;
import com.datecs.samples.UniversalReader.network.PrinterServer;
import com.datecs.samples.UniversalReader.network.PrinterServerListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UniversalReaderActivity extends Activity {
 // Debug
    private static final String LOG_TAG = "PrinterSample"; 
    private static final boolean DEBUG = true;
    
    // Request to get the bluetooth device
    private static final int REQUEST_GET_DEVICE = 0; 
    
    // Request to show barcode activity
    private static final int REQUEST_BARCODE = 1; 
    
    // Request to show smart card activity
    private static final int REQUEST_SMARTCARD = 2; 
    
    // Request to show mifare activity
    private static final int REQUEST_MIFARE = 3; 
    
    // Request to show mifare activity
    private static final int REQUEST_TOUCHSCREEN = 4; 
    
    // Request to get the bluetooth device
    private static final int DEFAULT_NETWORK_PORT = 9100; 
        
    // Result code for error
    public static final int RESULT_ERROR = RESULT_FIRST_USER + 0;
    
    // The listener for all printer events
    private final ProtocolAdapter.ChannelListener mChannelListener = new ProtocolAdapter.ChannelListener() {
        @Override
        public void onReadEncryptedCard() {
            // Nothing to do here
        }
        
        @Override
        public void onReadCard() {
            // Nothing to do here
        }
        
        @Override
        public void onReadBarcode() {
            // Nothing to do here
        }
        
        @Override
        public void onPaperReady(boolean state) {
            if (state) {
                toast(getString(R.string.msg_paper_ready));
            } else {
                toast(getString(R.string.msg_no_paper));
            }
        }
        
        @Override
        public void onOverHeated(boolean state) {
            if (state) {
                toast(getString(R.string.msg_overheated));
            }
        }
               
        @Override
        public void onLowBattery(boolean state) {
            if (state) {
                toast(getString(R.string.msg_low_battery));
            }
        }
    }; 
    
    // Member variables
    public static UniversalReader sUniversalReader;
    
    private ProtocolAdapter mProtocolAdapter;    
    private BluetoothSocket mBluetoothSocket;
    private PrinterServer mPrinterServer;
    private Socket mPrinterSocket;
    private boolean mRestart;    
		
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_universalreader);
        setTitle(getString(R.string.app_name) + " SDK " + BuildInfo.VERSION);
                
        findViewById(R.id.barcode).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    Intent intent = new Intent(UniversalReaderActivity.this, BarcodeActivity.class);
		        startActivityForResult(intent, REQUEST_BARCODE);				
			}        	
        });
        
        findViewById(R.id.smartcard).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    Intent intent = new Intent(UniversalReaderActivity.this, SmartCardActivity.class);
		        startActivityForResult(intent, REQUEST_SMARTCARD);		
			}        	
        });
        
        findViewById(R.id.mifare).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    Intent intent = new Intent(UniversalReaderActivity.this, MifareActivity.class);
		        startActivityForResult(intent, REQUEST_MIFARE);				
			}        	
        });    
        
        findViewById(R.id.touchscreen).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    Intent intent = new Intent(UniversalReaderActivity.this, TouchscreenActivity.class);
		        startActivityForResult(intent, REQUEST_TOUCHSCREEN);
			}        	
        });
    
        mRestart = true;
        waitForConnection();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRestart = false;               
        closeActiveConnection();
    }   
            
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GET_DEVICE) {
            if (resultCode == DeviceListActivity.RESULT_OK) {   
                String address = data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                //address = "192.168.11.136:9100";
                if (BluetoothAdapter.checkBluetoothAddress(address)) {
                    establishBluetoothConnection(address);
                } else {
                    establishNetworkConnection(address);
                }
            } else if (resultCode == RESULT_CANCELED) {
                
            } else {
                finish();
            }
        } else {
            if (resultCode == RESULT_ERROR) {
                waitForConnection();
            }
        }
    }
		
	private void toast(final String text) {
        runOnUiThread(new Runnable() {            
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();                
            }
        });
    }
       
    private void error(final String text, boolean resetConnection) {        
        if (resetConnection) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {        
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();                
                }           
            });
                
            waitForConnection();
        }
    }
    
    private void doJob(final Runnable job, final int resId) {
        // Start the job from main thread
        runOnUiThread(new Runnable() {            
            @Override
            public void run() {
                // Progress dialog available due job execution
                final ProgressDialog dialog = new ProgressDialog(UniversalReaderActivity.this);
                dialog.setTitle(getString(R.string.title_please_wait));
                dialog.setMessage(getString(resId));
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                
                Thread t = new Thread(new Runnable() {            
                    @Override
                    public void run() {                
                        try {
                            job.run();
                        } finally {
                            dialog.dismiss();
                        }
                    }
                });
                t.start();   
            }
        });                     
    }
    
    protected void initUniversalReader(InputStream inputStream, OutputStream outputStream) throws IOException {
        mProtocolAdapter = new ProtocolAdapter(inputStream, outputStream);
       
        if (mProtocolAdapter.isProtocolEnabled()) {
            final ProtocolAdapter.Channel channel = mProtocolAdapter.getChannel(ProtocolAdapter.CHANNEL_UNIVERSAL_READER);
            channel.setListener(mChannelListener);
            // Create new event pulling thread
            new Thread(new Runnable() {                
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        
                        try {
                            channel.pullEvent();
                        } catch (IOException e) {
                            e.printStackTrace();
                            error(e.getMessage(), mRestart);
                            break;
                        }
                    }
                }
            }).start();
            sUniversalReader = new UniversalReader(channel.getInputStream(), channel.getOutputStream());
        } else {
            sUniversalReader = new UniversalReader(mProtocolAdapter.getRawInputStream(), mProtocolAdapter.getRawOutputStream());
        }
        
        final String info;        
        try {
            info = sUniversalReader.getIdentification();
        } catch (UniversalReaderException e) {
            throw new IOException(e.getMessage());
        }
        
        runOnUiThread(new Runnable() {          
            @Override
            public void run() {
                ((ImageView)findViewById(R.id.icon)).setImageResource(R.drawable.icon);
                ((TextView)findViewById(R.id.name)).setText(info);
            }
        });
    }
    
	public synchronized void waitForConnection() {
        closeActiveConnection();
        
        // Show dialog to select a Bluetooth device. 
        startActivityForResult(new Intent(this, DeviceListActivity.class), REQUEST_GET_DEVICE);
        
        // Start server to listen for network connection.
        try {
            mPrinterServer = new PrinterServer(new PrinterServerListener() {                
                @Override
                public void onConnect(Socket socket) {
                    if (DEBUG) Log.d(LOG_TAG, "Accept connection from " + socket.getRemoteSocketAddress().toString());
                    
                    // Close Bluetooth selection dialog
                    finishActivity(REQUEST_GET_DEVICE);                    
                    
                    mPrinterSocket = socket;
                    try {
                        InputStream in = socket.getInputStream();
                        OutputStream out = socket.getOutputStream();
                        initUniversalReader(in, out);
                    } catch (IOException e) {   
                        e.printStackTrace();
                        error(getString(R.string.msg_failed_to_init) + ". " + e.getMessage(), mRestart);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   
    
    private void establishBluetoothConnection(final String address) {
        closePrinterServer();
        
        doJob(new Runnable() {           
            @Override
            public void run() {      
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();                
                BluetoothDevice device = adapter.getRemoteDevice(address);                    
                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                InputStream in = null;
                OutputStream out = null;
                
                adapter.cancelDiscovery();
                
                try {
                    if (DEBUG) Log.d(LOG_TAG, "Connect to " + device.getName());
                    mBluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
                    mBluetoothSocket.connect();
                    in = mBluetoothSocket.getInputStream();
                    out = mBluetoothSocket.getOutputStream();                                        
                } catch (IOException e) {    
                    e.printStackTrace();
                    error(getString(R.string.msg_failed_to_connect) + ". " +  e.getMessage(), mRestart);
                    return;
                }                                  
                
                try {
                    initUniversalReader(in, out);
                } catch (IOException e) {
                    e.printStackTrace();
                    error(getString(R.string.msg_failed_to_init) + ". " +  e.getMessage(), mRestart);
                    return;
                }
            }
        }, R.string.msg_connecting); 
    }
    
    private void establishNetworkConnection(final String address) {
        closePrinterServer();
        
        doJob(new Runnable() {           
            @Override
            public void run() {             
                Socket s = null;
                try {
                    String[] url = address.split(":");
                    int port = DEFAULT_NETWORK_PORT;
                    
                    try {
                        if (url.length > 1)  {
                            port = Integer.parseInt(url[1]);
                        }
                    } catch (NumberFormatException e) { }
                    
                    s = new Socket(url[0], port);
                    s.setKeepAlive(true);
                    s.setTcpNoDelay(true);
                } catch (UnknownHostException e) {
                    error(getString(R.string.msg_failed_to_connect) + ". " +  e.getMessage(), mRestart);
                    return;
                } catch (IOException e) {
                    error(getString(R.string.msg_failed_to_connect) + ". " +  e.getMessage(), mRestart);
                    return;
                }            
                
                InputStream in = null;
                OutputStream out = null;
                
                try {
                    if (DEBUG) Log.d(LOG_TAG, "Connect to " + address);
                    mPrinterSocket = s;                    
                    in = mPrinterSocket.getInputStream();
                    out = mPrinterSocket.getOutputStream();                                        
                } catch (IOException e) {                    
                    error(getString(R.string.msg_failed_to_connect) + ". " +  e.getMessage(), mRestart);
                    return;
                }                                  
                
                try {
                    initUniversalReader(in, out);
                } catch (IOException e) {
                    error(getString(R.string.msg_failed_to_init) + ". " +  e.getMessage(), mRestart);
                    return;
                }
            }
        }, R.string.msg_connecting); 
    }
    
    private synchronized void closeBlutoothConnection() {        
        // Close Bluetooth connection 
        BluetoothSocket s = mBluetoothSocket;
        mBluetoothSocket = null;
        if (s != null) {
            if (DEBUG) Log.d(LOG_TAG, "Close Blutooth socket");
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }        
    }
    
    private synchronized void closeNetworkConnection() {
        // Close network connection
        Socket s = mPrinterSocket;
        mPrinterSocket = null;
        if (s != null) {
            if (DEBUG) Log.d(LOG_TAG, "Close Network socket");
            try {
                s.shutdownInput();
                s.shutdownOutput();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }            
        }
    }
    
    private synchronized void closePrinterServer() {
        closeNetworkConnection();
        
        // Close network server
        PrinterServer ps = mPrinterServer;
        mPrinterServer = null;
        if (ps != null) {
            if (DEBUG) Log.d(LOG_TAG, "Close Network server");
            try {
                ps.close();
            } catch (IOException e) {                
                e.printStackTrace();
            }            
        }     
    }
    
    private synchronized void closeUniversalReaderConnection() {
        if (sUniversalReader != null) {
            sUniversalReader.release();
        }
        
        if (mProtocolAdapter != null) {
            mProtocolAdapter.release();
        }
    }
    
	private synchronized void closeActiveConnection() {
	    closeUniversalReaderConnection();
        closeBlutoothConnection();
        closeNetworkConnection();  
        closePrinterServer();
    }

}
