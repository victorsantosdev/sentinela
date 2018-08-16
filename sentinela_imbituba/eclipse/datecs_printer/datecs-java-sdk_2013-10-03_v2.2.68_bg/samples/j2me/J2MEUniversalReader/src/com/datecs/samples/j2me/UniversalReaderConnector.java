package com.datecs.samples.j2me;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import com.datecs.api.printer.ProtocolAdapter;
import com.datecs.api.printer.ProtocolAdapter.Channel;

/**
 * This class implements a universal reader Bluetooth connector.
 */
public class UniversalReaderConnector {    
    private String mUrl;
    private StreamConnection mConnection;        
    private InputStream mBaseInputStream;
    private OutputStream mBaseOutputStream;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private boolean mOpen;
   
    /**
     * Creates a new instance of this class from a given Bluetooth address.
     * 
     * @param address the bluetooth address.
     */
    public UniversalReaderConnector(String address) {            
        mUrl = "btspp://" + address + ":1;authenticate=false;encrypt=false";          
        mOpen = false;
    }
    
    /**
     * Open the Bluetooth connection.
     * 
     * @throws IOException if an I/O error occurs.
     */
    public void open() throws IOException {
        ProtocolAdapter adapter;             
        
        mConnection = (StreamConnection) Connector.open(mUrl);
        mBaseInputStream = mConnection.openInputStream();          
        mBaseOutputStream = mConnection.openOutputStream();        
        mOpen = true;
        
        // Check if the selected Bluetooth device is not a printer that 
        // response protocol mode. In that case data streams must be 
        // obtained from a specified logical channel.
        adapter = new ProtocolAdapter(mBaseInputStream, mBaseOutputStream);
        if (adapter.isProtocolEnabled()) {
            Channel channelReader = adapter.getChannel(ProtocolAdapter.CHANNEL_UNIVERSAL_READER);
            mInputStream = channelReader.getInputStream();
            mOutputStream = channelReader.getOutputStream();
        } else {
            mInputStream = mBaseInputStream;
            mOutputStream = mBaseOutputStream;
        }
    }
    
    /**
     * Close the Bluetooth connection.
     * 
     * @throws IOException
     */
    public void close() throws IOException {        
        if (mOpen) {            
            mBaseInputStream.close();
            mBaseOutputStream.close();
            mConnection.close();                
            mOpen = false;
        }        
    }
    
    /**
     * Returns an instance of InputStream class associated with this object.
     * 
     * @return an instance of InputStream class.
     * 
     * @throws IOException if an I/O error occurs. 
     */
    public InputStream getInputStream() throws IOException {
        if (mOpen == false) {
            throw new IOException("Not connected");
        }
        return mInputStream;
    }
    
    /**
     * Returns an instance of OutputStream class associated with this object.
     * 
     * @return an instance of OutputStream class.
     * 
     * @throws IOException if an I/O error occurs.
     */
    public OutputStream getOutputStream() throws IOException {
        if (mOpen == false) {
            throw new IOException("Not connected");
        }        
        return mOutputStream;
    }
}