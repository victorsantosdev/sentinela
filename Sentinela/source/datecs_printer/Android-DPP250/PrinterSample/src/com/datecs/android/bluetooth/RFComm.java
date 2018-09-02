/**
 * Copyright (C) 2009 Datecs Ltd.
 *
 * @author Miroslav Slavchev (mslavchev@datecs.bg)
 */
package com.datecs.android.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class lets you to perform fundamental Bluetooth tasks, such as initiate
 * device discovery, connect to remote device over RFCOMM and exchange data with
 * it.
 * <p>
 * This class is designed to be used on Android 1.6 and below, as the SDK's for
 * newer Android OS have Serial Bluetooth support.
 * </p>
 * <p>
 * The application that uses RFComm requires the external native library
 * "libRFComm.so" which must exists into application's directory:
 * "(ProjectRoot)/libs/armeabi/". Also, the application must grand user
 * permissions of "android.permission.BLUETOOTH" and
 * "android.permission.BLUETOOTH_ADMIN".
 * </p>
 * <p>
 * List of RFComm devices in range can be obtained with {@link #scan()} method,
 * which may act as start point of using Bluetooth connection.
 * </p>
 * <p>
 * Any instance of class is object associated with a given Bluetooth hardware
 * address.
 * </p>
 */
public final class RFComm {
    /** Invalid socket. */
    private static final int INVALID_SOCKET = -1;

    /** libRFComm.so: Get last error value. */
    private static native int DatecsRFComm_getErrno();

    /** libRFComm.so: Scan for bluetooth devices in range. */
    private static native String[] DatecsRFComm_scan() throws IOException;

    /** libRFComm.so: Get bluetooth device name. */
    private static native String DatecsRFComm_queryName(String address) throws IOException;

    /** libRFComm.so: Connect to remote bluetooth device. */
    private native int DatecsRFComm_connect(int channel, String address);

    /** libRFComm.so: Close connection with remote bluetooth device. */
    private native int DatecsRFComm_close(int sock);

    /** libRFComm.so: Read data from remote bluetooth device. */
    private native int DatecsRFComm_read(int sock, byte[] buffer, int start, int size);

    /** libRFComm.so: Read byte from remote bluetooth device. */
    private native int DatecsRFComm_readByte(int sock);

    /** libRFComm.so: Write data to remote bluetooth device. */
    private native int DatecsRFComm_write(int sock, byte[] buffer, int start, int size);

    /** libRFComm.so: Write byte to remote bluetooth device. */
    private native int DatecsRFComm_writeByte(int sock, byte b);

    /** libRFComm.so: Receive number of bytes available for reading. */
    private native int DatecsRFComm_available(int sock);

    private int mSock;
    private String mBthAddr;
   
    /** Initializes the static members */
    static {
        System.loadLibrary("RFComm");
    }

    /**
     * Enumerates remote Bluetooth devices.
     * <p>
     * This method will blocks for ~11 seconds until enumerates devices that are
     * currently in discovery state. After successful execution it returns a
     * list with <code>RFComm</code> objects, which are associated with devices
     * was found.
     * </p>
     * 
     * @return the array of <code>RFComm</code> objects associated with devices
     *         that was found.
     * 
     * @throws IOException if an error occurs.
     */
    public static List<RFComm> scan() throws IOException {
        String[] addrs = DatecsRFComm_scan();
        List<RFComm> devices = new ArrayList<RFComm>(addrs.length);        
        for (String addr : addrs) {
            devices.add(new RFComm(addr));
        }

        return devices;
    }

    /**
     * Constructs a RFComm from Bluetooth hardware address of remote device.
     * 
     * @param address the Bluetooth hardware address of remote device.
     *            <p>
     *            For example, "00:11:22:AA:BB:CC".
     */
    public RFComm(String address) {        
        mSock = INVALID_SOCKET;
        mBthAddr = address;        
    }

    /**
     * Returns the connection status.
     * <p>
     * This method returns positive when connection has been made but not
     * closed. It does not gives any guarantees that data can be successfully
     * transmit to device.
     * </p>
     * 
     * @return <code>true</code> if the object is connected; false otherwise.
     */
    public boolean isConnected() {
        return (mSock != INVALID_SOCKET);
    }

    /**
     * Returns the Bluetooth hardware address of remote device associated with
     * this object.
     * 
     * @return the Bluetooth hardware address as string.
     */
    public String getAddress() {
        return mBthAddr;
    }

    /**
     * Gets the friendly Bluetooth name of the remote device.
     * <p>
     * This operation will blocks until resolve the device name. First time it
     * may take a long time to finish.
     * </p>
     * 
     * @return the Bluetooth name.
     * 
     * @throws IOException if an error occurs.
     */
    public String getName() throws IOException {
        return DatecsRFComm_queryName(mBthAddr);        
    }

    /**
     * Attempts to connect to a remote device.
     * <p>
     * This method will block until a connection is made or the connection
     * fails. If this method returns without an exception then this object is
     * now connected. When finish using the connection it must be closed.
     * </p>
     * <p>
     * Do not use this method until perform scan procedure.
     * </p>
     * 
     * @throws IOException if an error occurs.
     */
    public void connect() throws IOException {
        if (mSock != INVALID_SOCKET) {
            return;
        }

        mSock = DatecsRFComm_connect(1, mBthAddr);
        if (mSock == INVALID_SOCKET) {
            throw new IOException("Error " + DatecsRFComm_getErrno());
        }
    }

    /**
     * Immediately close connection, and release all associated resources.
     * 
     * @throws IOException if an error occurs.
     */
    public void close() throws IOException {
        if (mSock == INVALID_SOCKET) {
            return;
        }

        if (DatecsRFComm_close(mSock) != 0) {
            throw new IOException("Error" + DatecsRFComm_getErrno());
        }

        mSock = INVALID_SOCKET;
    }

    /**
     * Helper method that check if the current object is connected.
     * 
     * @throws IOException if the current object is not connected.
     */
    private void checkStream() throws IOException {
        if (!isConnected()) {
            throw new IOException("The object is not connected");
        }
    }

    /**
     * Gets the input stream associated with this object.
     * <p>
     * The input stream will be returned even if the object is not yet
     * connected, but operations on that stream will throws IOException until
     * the associated object is connected.
     * </p>
     * 
     * @return the input stream object.
     * 
     * @throws IOException if an error occurs.
     */
    public InputStream getInputStream() {
        return new InputStream() {
            @Override
            public synchronized int available() throws IOException {
                checkStream();
                return DatecsRFComm_available(mSock);
            }

            @Override
            public synchronized int read() throws IOException {
                checkStream();
                return DatecsRFComm_readByte(mSock);
            }

            @Override
            public synchronized int read(byte[] b, int off, int len) throws IOException {
                if (b == null) {
                    throw new NullPointerException();
                }

                if (off < 0 || len < 0 || len > (b.length - off)) {
                    throw new IndexOutOfBoundsException();
                }

                checkStream();
                return DatecsRFComm_read(mSock, b, off, len);
            }

            @Override
            public synchronized int read(byte[] b) throws IOException {
                if (b == null) {
                    throw new NullPointerException();
                }

                checkStream();
                return DatecsRFComm_read(mSock, b, 0, b.length);
            }
        };
    }

    /**
     * Gets the output stream associated with this object.
     * <p>
     * The output stream will be returned even if the object is not yet
     * connected, but operations on that stream will throws IOException until
     * the associated object is connected.
     * </p>
     * 
     * @return the output stream object.
     * 
     * @throws IOException if an error occurs.
     */
    public OutputStream getOutputStream() {
        return new OutputStream() {            
            @Override
            public synchronized void write(int b) throws IOException {
                int status;

                do {
                    checkStream();
                    status = DatecsRFComm_writeByte(mSock, (byte)(b & 0xFF));
                    if (status < 0) {
                        throw new IOException("Error " + DatecsRFComm_getErrno());
                    }
                } while (status != 1);
            }

            @Override
            public synchronized void write(byte[] b, int off, int len) throws IOException {
                int status, bytes = 0;

                if (b == null) {
                    throw new NullPointerException();
                }

                if (off < 0 || len < 0 || (off + len) > b.length) {
                    throw new IndexOutOfBoundsException();
                }

                do {
                    checkStream();
                    status = DatecsRFComm_write(mSock, b, bytes + off, len - bytes);
                    if (status < 0) {
                        throw new IOException("Error " + DatecsRFComm_getErrno());
                    }
                    bytes += status;
                } while (bytes != len);
            }

            @Override
            public synchronized void write(byte[] b) throws IOException {
                int status, bytes = 0;

                if (b == null) {
                    throw new NullPointerException();
                }

                do {
                    checkStream();
                    status = DatecsRFComm_write(mSock, b, bytes, b.length - bytes);
                    if (status < 0) {
                        throw new IOException("Error " + DatecsRFComm_getErrno());
                    }
                    bytes += status;
                } while (bytes != b.length);
            }
        };
    }
}
