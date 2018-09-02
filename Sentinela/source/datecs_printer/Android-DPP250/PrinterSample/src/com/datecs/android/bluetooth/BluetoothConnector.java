package com.datecs.android.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public abstract class BluetoothConnector {
	
	/**
	 * This interface defines callback to the discovery procedure. 
	 */
	public interface OnDiscoveryListener {
		public void onDiscoveryStarted();
		public void onDiscoveryFinished();
		public void onDiscoveryError(String error);
		public void onDeviceFound(String name, String address);
	}
	
	/** Holds when to use NKD components or native API. */  
	static boolean sExpectNDK = false;
	
	/** Initialize static data. */ 
	static {
		try { 
			Class.forName("android.bluetooth.BluetoothAdapter"); 
		} catch (ClassNotFoundException e) {
			sExpectNDK = true;
		}
	}
	
	public static BluetoothConnector getConnector(Context context) throws IOException {		
		if (sExpectNDK) {
			return new NDKConnector();			
		} else {
			return new APIConnector(context);
		}		
	}
	
	public abstract void startDiscovery(OnDiscoveryListener listener) throws IOException;		
	public abstract void connect(String address) throws IOException;
	public abstract void close() throws IOException;
	public abstract InputStream getInputStream()throws IOException ;
	public abstract OutputStream getOutputStream() throws IOException ;		
}

final class APIConnector extends BluetoothConnector  {
	
	private BluetoothAdapter mAdapter;
	private BluetoothSocket mSocket;
	private Context mContext;
	
	private class DiscoveryReceiver extends BroadcastReceiver {
		public OnDiscoveryListener mListener;
		
		public DiscoveryReceiver(OnDiscoveryListener listener) {			
			mListener = listener;
		}
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
				mListener.onDiscoveryStarted();					
			} else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
				mListener.onDiscoveryFinished();
				mContext.unregisterReceiver(this);
			} else if (action.equals(BluetoothDevice.ACTION_FOUND)) {
				BluetoothDevice device;
				String name, address;
				
				device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				name = device.getName();
				address = device.getAddress();
				mListener.onDeviceFound(name, address);				
			}
		}				
	}
	
	public APIConnector(Context context) throws IOException {
		super();
		
		mContext = context;
		
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mAdapter == null) {
			throw new IOException("Bluetooth is not supported on this hardware platform.");
		}				
	}
	
	public void startDiscovery(OnDiscoveryListener listener) throws IOException {
		DiscoveryReceiver receiver;		
		
		if (listener == null) {
			throw new NullPointerException("The listener is a null");
		}	
				
		if (mAdapter.isEnabled() == false) {
			throw new IOException("Bluetooth is not enabled");
		}

		receiver = new DiscoveryReceiver(listener);
		mContext.registerReceiver(receiver, 
				new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
		mContext.registerReceiver(receiver, 
				new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
		mContext.registerReceiver(receiver, 
				new IntentFilter(BluetoothDevice.ACTION_FOUND));	
				
		if (mAdapter.startDiscovery() == false) {
			mContext.unregisterReceiver(receiver);			
		}
	}
	
	@Override
	public void close() throws IOException {
		if (mSocket != null) {
			mSocket.close();
			mSocket = null;
		}
	}

	@Override
	public void connect(String address) throws IOException {
		BluetoothDevice device = mAdapter.getRemoteDevice(address);		
		//UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
		
		mAdapter.cancelDiscovery();
		//mSocket = device.createRfcommSocketToServiceRecord(uuid);
        try {
            Method m = device.getClass().getMethod("createRfcommSocket",
                    new Class[] { int.class });
            mSocket = (BluetoothSocket)m.invoke(device, 1);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        mSocket.connect();				
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if (mSocket == null) {
			throw new IOException("The stream is not connected");
		}
		return mSocket.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		if (mSocket == null) {
			throw new IOException("The stream is not connected");
		}
		return mSocket.getOutputStream();
	}		
}

final class NDKConnector extends BluetoothConnector {
	private RFComm mRFComm;
		
	class DiscoveryRunnable implements Runnable {
		private OnDiscoveryListener mListener;	
		
		public DiscoveryRunnable(OnDiscoveryListener listener) {
			mListener = listener;
		}
		
		// @Override
		public void run() {
			List<RFComm> devices;
			String name, address;
			
			mListener.onDiscoveryStarted();
			
			try {
				devices = RFComm.scan();
			} catch (IOException e) {				
				e.printStackTrace();
				mListener.onDiscoveryError(e.getMessage());
				devices = null;
			}
			
			if (devices != null) {
				for (RFComm device : devices) {				
					try {
						name = device.getName();
					} catch (IOException e) {
						name = null;
					}
					
					address = device.getAddress();
					mListener.onDeviceFound(name, address);
				}
			}
			
			mListener.onDiscoveryFinished();
		}
	}; 
	
	@Override
	public void close() throws IOException {
		if (mRFComm != null) {
			mRFComm.close();
			mRFComm = null;
		}		
	}

	@Override
	public void connect(String address) throws IOException {
		mRFComm = new RFComm(address);
		mRFComm.connect();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if (mRFComm == null) {
			throw new IOException("The stream is not connected");
		}
		return mRFComm.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		if (mRFComm == null) {
			throw new IOException("The stream is not connected");
		}
		return mRFComm.getOutputStream();
	}

	@Override
	public void startDiscovery(OnDiscoveryListener listener) throws IOException {
		if (listener == null) {
			throw new NullPointerException("The listener is a null");
		}	
				
		new Thread(new DiscoveryRunnable(listener)).start();	
	}
}
