package com.datecs.samples.j2me;

import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

/**
 * Implements a Bluetooth discovery. 
 */
public class BluetoothDeviceDiscovery implements DiscoveryListener {    
    // Object used for waiting
    private static Object mLock = new Object();
   
    // Vector containing the devices discovered
    private static Vector mDevices = new Vector();
    
    public BluetoothDeviceDiscovery() {
    }
    
    public Vector discovery() {        
        LocalDevice localDevice;
        DiscoveryAgent agent;
        
        try {
            localDevice = LocalDevice.getLocalDevice();
            agent = localDevice.getDiscoveryAgent();
            agent.startInquiry(DiscoveryAgent.GIAC, this);
        } catch (BluetoothStateException e) {
            inquiryCompleted(DiscoveryListener.INQUIRY_ERROR);
            e.printStackTrace();
        }
        
        try {
            synchronized(mLock){
                mLock.wait();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return mDevices;
    }
    
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        System.out.println("Device discovered: " + btDevice.getBluetoothAddress());
        if(!mDevices.contains(btDevice)){
            mDevices.addElement(btDevice);
            
        }
    }

    public void inquiryCompleted(int discType) {
        synchronized(mLock){
            mLock.notify();
        }
       
        switch (discType) {
            case DiscoveryListener.INQUIRY_COMPLETED :
                System.out.println("INQUIRY_COMPLETED");
                break;
               
            case DiscoveryListener.INQUIRY_TERMINATED :
                System.out.println("INQUIRY_TERMINATED");
                break;
               
            case DiscoveryListener.INQUIRY_ERROR :
                System.out.println("INQUIRY_ERROR");
                break;
 
            default :
                System.out.println("Unknown Response Code");
                break;
        }
    }

    public void serviceSearchCompleted(int transID, int respCode) {
        
    }

    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        
    }

}
