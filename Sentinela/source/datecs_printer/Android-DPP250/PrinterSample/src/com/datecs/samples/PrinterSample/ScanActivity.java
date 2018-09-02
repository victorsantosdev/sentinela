package com.datecs.samples.PrinterSample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.datecs.android.bluetooth.BluetoothConnector;
import com.datecs.android.bluetooth.BluetoothConnector.OnDiscoveryListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ScanActivity extends Activity implements OnDiscoveryListener {
    private static final String DATABASE_NAME = "bluetooth_devices.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DEVICES_TABLE = "bluetooth_devices";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    
    public static final String BLUETOOTH_NAME = "bluetooth_name";
    public static final String BLUETOOTH_ADDR = "bluetooth_addr";    
    
    private class Device {   
        private String mName;
        private String mAddress;
        
        public Device(String name, String address) {       
            mName = name;
            mAddress = address;
        }
        
        public String getName() {
            return mName;
        }
        
        public String getAddress() {
            return mAddress;
        }            
    }
    
    private abstract class DeviceAdapter extends BaseAdapter {
        private ArrayList<Device> mDevices = new ArrayList<Device>();
        
        public List<Device> getContainer() {
            return mDevices;
        }
        
        public void add(Device device) {
            mDevices.add(device);
        }
                
        public void clear() {
            mDevices.clear();
        }
    }
    
    private final DeviceAdapter mAdapter = new DeviceAdapter() {
     //   @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = LayoutInflater.from(ScanActivity.this);
                v = vi.inflate(R.layout.device, null);
            }
            
            Device device = getContainer().get(position);
            ((TextView)v.findViewById(R.id.name)).setText(device.getName());
            ((TextView)v.findViewById(R.id.address)).setText(device.getAddress());
            
            return v;
        }
        
     //   @Override
        public long getItemId(int location) {
            return (long)location;
        }
        
     //   @Override
        public Object getItem(int location) {
            return getContainer().get(location);
        }
        
     //   @Override
        public int getCount() {
            return getContainer().size();
        }     
    };
    
    private class DatabaseHelper extends SQLiteOpenHelper {
        
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
                
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + DEVICES_TABLE + " ("                     
                    + NAME + " text, "
                    + ADDRESS + " text);");            
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXIST " + DEVICES_TABLE);            
            onCreate(db);
        }
    }

    private final Handler mHandler = new Handler();
    
    private ListView mDeviceView;    
    private ProgressDialog mProgress;
    private BluetoothConnector mConnector;
        
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devices);
                       
        mDeviceView = (ListView)findViewById(R.id.devices);
        mDeviceView.setAdapter(mAdapter);        
        mDeviceView.setOnItemClickListener(new OnItemClickListener() {
            //@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Device device = (Device)mAdapter.getItem(position);
                Intent data = new Intent(ScanActivity.this, PrinterActivity.class);
                String connectionString = "bth://" + device.getAddress();
                data.putExtra(PrinterActivity.CONNECTION_STRING, connectionString);
                startActivityForResult(data, 0);
            }           
        });               
        
        try {
            mConnector = BluetoothConnector.getConnector(this);
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            finish();           
        }
    }        
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase dbase = dbHelper.getWritableDatabase();
        String[] columns = { NAME, ADDRESS };
        Cursor c = dbase.query(DEVICES_TABLE, columns, null, null, null, null, null);
        
        mAdapter.clear();        
        if (c != null && c.moveToFirst()) {
            int nameIndex = c.getColumnIndex(NAME);
            int addrIndex = c.getColumnIndex(ADDRESS);
            
            do {
                String name = c.getString(nameIndex);
                String addr = c.getString(addrIndex);
                Device device = new Device(name, addr);                
                mAdapter.add(device);
            }  while (c.moveToNext());            
            c.close();
            
            mAdapter.notifyDataSetChanged();            
        }
        
        dbase.close();             
    }

    @Override
    protected void onStop() {
        super.onStop();
        
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase dbase = dbHelper.getWritableDatabase();
        
        try {
            dbase.beginTransaction();
            dbase.delete(DEVICES_TABLE, null, null);
            
            int count = mAdapter.getCount();
            for (int i = 0; i < count; i++) {
                Device device = (Device)mAdapter.getItem(i);
                ContentValues cv = new ContentValues();
                cv.put(NAME, device.getName());
                cv.put(ADDRESS, device.getAddress());
                dbase.insert(DEVICES_TABLE, NAME, cv);                
            }
            dbase.setTransactionSuccessful();
        } finally {
            dbase.endTransaction();
            dbase.close();
        }        
    }    
        
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, R.string.scan_menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == Menu.FIRST) {
            startDiscovery();
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void startDiscovery() {     
        try {
            mConnector.startDiscovery(this);
        } catch (IOException e) {
            notifyUser(e.getMessage());
        }           
    }   
    
    protected void notifyUser(final String message) {        
        mHandler.post(new Runnable() {
            public void run() {             
                new AlertDialog.Builder(ScanActivity.this)
                .setTitle(R.string.scan_error)
                .setMessage(message)
                .create()
                .show();               
            }           
        }); 
    }   

    public void onDeviceFound(final String name, final String address) {  
        mHandler.post(new Runnable() {
            public void run() {
                Device device = new Device(name, address);                
                if (name == null) {
                    device = new Device(getString(R.string.scan_unknown_device), address);
                } else {
                    device = new Device(name, address);
                }
                
                mAdapter.add(device);                 
                mAdapter.notifyDataSetChanged();              
            }           
        });
    }
    
    public void onDiscoveryFinished() {
        mHandler.post(new Runnable() {
            public void run() {
                mProgress.dismiss();                
            }           
        });     
    }

    public void onDiscoveryStarted() {
        mHandler.post(new Runnable() {
            public void run() {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();  
                mProgress = ProgressDialog.show(
                        ScanActivity.this,     
                        getString(R.string.scan_title), 
                        getString(R.string.scan_text),
                        true);
            }
        });     
    }

    public void onDiscoveryError(String error) {
        notifyUser(error);
    }
}