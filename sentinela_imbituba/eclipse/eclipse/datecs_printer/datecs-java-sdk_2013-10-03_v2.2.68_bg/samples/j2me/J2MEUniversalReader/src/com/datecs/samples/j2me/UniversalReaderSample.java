package com.datecs.samples.j2me;

import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.RemoteDevice;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.Ticker;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.datecs.api.smartcard.AnswerToReset;
import com.datecs.api.universalreader.TouchEvent;
import com.datecs.api.universalreader.UniversalReader;
import com.datecs.api.universalreader.UniversalReaderException;
import com.datecs.api.universalreader.UniversalReader.TouchScreen;

public class UniversalReaderSample extends MIDlet implements CommandListener
    , ItemCommandListener {
    private static final Command CMD_EXIT = new Command("Exit", Command.EXIT, 1);
    private static final Command CMD_SCAN = new Command("Scan", Command.ITEM, 1);
    private static final Command CMD_SELECT = new Command("Select", Command.ITEM, 1);
    private static final Command CMD_BARCODE = new Command("Barcode", Command.ITEM, 1);
    private static final Command CMD_SMARTCARD = new Command("Smart Card", Command.ITEM, 1);
    private static final Command CMD_MIFARE = new Command("Mifare", Command.ITEM, 1);
    private static final Command CMD_TOUCHSCREEN = new Command("Touch Screen", Command.ITEM, 1); 
    
    // Object used for waiting
    private Object mLock = new Object();
    
    private Display mDisplay;
    private Form mForm;
    private TextField mAddressField;  
    private Vector mDevices;
    
    /**
     * Appends a new button like Item to given Form.
     * 
     * @param f the form.
     * @param c the default command.
     */
    public void appendButtonItem(Form f, Command c) {
        StringItem item = new StringItem("", c.getLabel(), Item.BUTTON);
        item.setDefaultCommand(c);
        item.setItemCommandListener(this);
        item.setLayout(Item.LAYOUT_2);
        item.setPreferredSize(f.getWidth() / 2 - 5, -1);
        f.append(item);
    }
    
    protected void startApp() throws MIDletStateChangeException {
        mDisplay = Display.getDisplay(this);
        
        mForm = new Form("UniversalReader sample application");
        mForm.addCommand(CMD_EXIT);
        mForm.setCommandListener(this);
        mDisplay.setCurrent(mForm);
        
        mAddressField = new TextField("Device: ", "000190E61C68", 17, TextField.ANY);
        mAddressField.setDefaultCommand(CMD_SCAN);
        mAddressField.setItemCommandListener(this);
        mForm.append(mAddressField);        
                
        appendButtonItem(mForm, CMD_BARCODE);          
        appendButtonItem(mForm, CMD_SMARTCARD);
        appendButtonItem(mForm, CMD_MIFARE);
        appendButtonItem(mForm, CMD_TOUCHSCREEN);                    
    }

    public void pauseApp() {
        // Called when MIDlet enter the Paused state.
    }

    public void destroyApp(boolean unconditional) {
        // Called when MIDlet terminate and enter the Destroyed state.
    }

    /**
     * Implements a command action listener.
     */
    public void commandAction(Command c, Displayable d) {
        if (c == CMD_EXIT) {
            if (d == mForm) {
                destroyApp(true);                
                notifyDestroyed();
            } else {
                ExecuteMethods em = (ExecuteMethods)d;
                em.stop();
                mDisplay.setCurrent(mForm);
            }
        }
    }
    
    /**
     * Implements an item command action listener.
     */
    public void commandAction(Command c, Item item) {
        if (c == CMD_SCAN) {
            scan();
        } else if (c == CMD_BARCODE) {
            readBarcode();
        } else if (c == CMD_SMARTCARD) {
            resetSmartCard();
        } else if (c == CMD_MIFARE) {
            readMifareCard();
        } else if (c == CMD_TOUCHSCREEN) {
            drawTouchScreen();
        }
    }
    
    /**
     * Show error alert.
     * 
     * @param text the text.
     */
    private void error(String text) {
        AlertType.ERROR.playSound(Display.getDisplay(this));
        AlertType.INFO.playSound(Display.getDisplay(this));
        Alert alert = new Alert("error", text, null, AlertType.ERROR);
        alert.setTimeout(Alert.FOREVER);
        mDisplay.setCurrent(alert, mForm);
    }
    
    /**
     * Scan for Bluetooth printers in range.
     */
    private void scan() {
        final List list = new List("Select a device", List.IMPLICIT);                      
        list.setTicker(new Ticker("Scanning for devices..."));                
        mDisplay.setCurrent(list);
        
        new Thread() {                
            public void run() {
                int deviceCount;
                
                mDevices = new BluetoothDeviceDiscovery().discovery();
                deviceCount = mDevices.size();
                for (int i = 0; i < deviceCount; i++) {
                    RemoteDevice remoteDevice = (RemoteDevice)mDevices.elementAt(i);
                    String address = remoteDevice.getBluetoothAddress();
                    String name;
                    
                    try {
                        name = remoteDevice.getFriendlyName(true);
                    } catch (IOException e) {
                        name = "[UNKNOWN DEVICE]";
                        e.printStackTrace();
                    }
                    
                    list.append(address + " " + name, null);                
                }
                list.setTicker(null);                                    
                list.setCommandListener(new CommandListener() {                    
                    public void commandAction(Command c, Displayable d) {
                        if (c == CMD_EXIT) {
                            mDisplay.setCurrent(mForm);
                        } else if (c == CMD_SELECT) {
                            List list = (List)d;
                            int selectedIndex = list.getSelectedIndex();
                            RemoteDevice remoteDevice = (RemoteDevice)mDevices.elementAt(selectedIndex);
                            mAddressField.setString(remoteDevice.getBluetoothAddress());                        
                            mDisplay.setCurrent(mForm);
                        }
                    }
                });
                list.addCommand(CMD_EXIT);
                list.setSelectCommand(CMD_SELECT);
            }
        }.start();
    }
    
    /**
     * Returns an instance of PrinterConnector class.
     * 
     * @return an instance of PrinterConnector class.
     */
    private UniversalReaderConnector getConnector() {
        char[] buf;
        String address;
        UniversalReaderConnector urc;
        
        buf = new char[mAddressField.size()];
        mAddressField.getChars(buf);
        address = new String(buf);
        
        urc = new UniversalReaderConnector(address);
        return urc;        
    }
    
    private void readBarcode() {
        ResultForm form = new ResultForm("Read barcode") { 
            private volatile boolean mRun = true;
            
            public void stop() {
                mRun = false;
            }  
            
            public void start() {
                UniversalReaderConnector urc = getConnector();
                UniversalReader universalReader;
                UniversalReader.BarcodeReader barcodeReader;               
                 
                synchronized(mLock) {
                    try {  
                        setStatus("Connecting...");
                        urc.open();
                        universalReader = new UniversalReader(
                                urc.getInputStream(), urc.getOutputStream());
                        barcodeReader = universalReader.getBarcodeReader();
                        
                        setStatus("Press device button to read barcode ...");
                        while (mRun) {
                            boolean pressed = false;
                                                            
                            if (mRun) {
                                pressed = universalReader.isButtonPressed();                            
                            }
                                
                            if (mRun) {
                                if (pressed) {
                                    String data;                            
                                    try {
                                        data = barcodeReader.read();
                                        append("#" + data.substring(1) + "\n");
                                    } catch (UniversalReaderException e) {
                                        e.printStackTrace();
                                        if (e.getErrorCode() != 
                                            UniversalReader.ERROR_BARCODE_NODATA) {
                                            throw e;
                                        }
                                    }                           
                                } else {
                                    try {
                                        Thread.sleep(40);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {                      
                        e.printStackTrace();
                        error(e.getMessage());
                    } catch (UniversalReaderException e) {                      
                        e.printStackTrace();
                        error(e.getMessage());
                    } finally {
                        try {
                            urc.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setStatus(null);
                    } 
                }
            }            
        };
        form.setCommandListener(this);
        form.addCommand(CMD_EXIT);        
        mDisplay.setCurrent(form);        
    }
    
    private void resetSmartCard() {
        ResultForm form = new ResultForm("Reset Smart Card") { 
            private volatile boolean mRun = true;
            
            private void setAnswerToReset(AnswerToReset atr) {
                byte[] b = atr.getData();
                
                if (b != null) {
                    StringBuffer sb = new StringBuffer(b.length * 3);
                    sb.append("Smart Card Reset Bytes:\n");
                    for (int i = 0; i < b.length; i++) {
                        sb.append(Integer.toHexString(((int)b[i] & 0xff) + 0x100).substring(1));
                        sb.append(' ');
                    }
                    append(sb.toString());
                }
            }
            
            public void stop() {
                mRun = false;
            }  
            
            public void start() {
                UniversalReaderConnector urc = getConnector();
                UniversalReader universalReader;
                UniversalReader.SmartCardReader scReader;               
                 
                synchronized(mLock) {
                    try {    
                        setStatus("Connecting...");
                        urc.open();
                        universalReader = new UniversalReader(urc.getInputStream(), urc.getOutputStream());
                        scReader = universalReader.getSmartCardReader();
                                                
                        while (mRun) {                          
                            boolean cardPresent = false;
                              
                            deleteAll();
                            setStatus("Put smart card into reader...");
                            while (mRun && cardPresent == false) {
                                if (mRun) {
                                    cardPresent = scReader.isCardPresent();
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
                                scReader.setPower(true);
                                atr = scReader.reset();
                                scReader.setPower(false);
                                setAnswerToReset(atr);
                                setStatus("Remove smart card from reader...");
                            }
                                                
                            while (mRun && cardPresent) {
                                if (mRun) {
                                    cardPresent = scReader.isCardPresent();
                                }
                                
                                if (mRun) {
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {                                  
                                        e.printStackTrace();
                                    }
                                }
                            }                            
                        }
                    } catch (IOException e) {                      
                        e.printStackTrace();
                        error(e.getMessage());
                    } catch (UniversalReaderException e) {                      
                        e.printStackTrace();
                        error(e.getMessage());
                    } finally {
                        try {
                            urc.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setStatus(null);
                    }
                }
            }
        };
        form.setCommandListener(this);
        form.addCommand(CMD_EXIT);        
        mDisplay.setCurrent(form);        
    }
    
    private void readMifareCard() {
        ResultForm form = new ResultForm("Read Mifare card") { 
            private volatile boolean mRun = true;
                            
            public void stop() {
                mRun = false;
            }  
                        
            public void start() {
                UniversalReaderConnector urc = getConnector();
                UniversalReader universalReader;
                UniversalReader.MifareReader mifareReader;               
                int cardSerial = 0;
                
                final int BLOCKS = 4;
                final int SECTORS = 4;                
                final byte[] KEY = {(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff};
                final int KEY_A = 0x60;
                
                synchronized(mLock) {
                    try {
                        setStatus("Connecting...");
                        urc.open();
                        universalReader = new UniversalReader(urc.getInputStream(), urc.getOutputStream());                        
                        mifareReader = universalReader.getMifareReader();
                                         
                        setStatus("Put Mifare card on reader...");
                        
                        mifareReader.setPower(true);
                        mifareReader.config();                        
                        while (mRun) {
                            if (mRun) {
                                if (mRun) {
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException e) {                              
                                        e.printStackTrace();
                                    }
                                }
                                
                                if (mRun) {
                                    try {
                                        mifareReader.request(false);
                                        cardSerial = mifareReader.anticollision();
                                        mifareReader.select(cardSerial);  
                                        append("Card found: #" + Integer.toHexString(cardSerial) + "\n");
                                    } catch (UniversalReaderException e) {
                                        if (!e.getMessage().startsWith("Mifare")) {
                                            throw e;
                                        }
                                        continue;
                                    }
                                }
                                
                                try {
                                    for (int sector = 0; sector < SECTORS && mRun; sector++) {
                                        mifareReader.authenticate(cardSerial, sector * BLOCKS, KEY_A, KEY);
                                        for (int block = 0; block < BLOCKS && mRun; block++) {
                                            int record = sector * BLOCKS + block;
                                            byte[] data = mifareReader.read(record);
                                            StringBuffer sb = new StringBuffer(data.length * 3);
                                            for (int i = 0; i < data.length; i++) {
                                                sb.append(Integer.toHexString(((int)data[i] & 0xff) + 0x100).substring(1));
                                                sb.append(' ');
                                            }
                                            append("Sector " + sector + ",block " + block + ", data: ");
                                            append(sb.toString());
                                            append("\n");
                                        }
                                    }
                                } catch (UniversalReaderException e) {
                                    if (!e.getMessage().startsWith("Mifare")) {
                                        throw e;
                                    }                                        
                                }                                                                    
                                  
                                if (mRun) {
                                    mifareReader.halt();
                                }                                
                            }                                                        
                        }
                        
                        mifareReader.setPower(false);             
                        
                    } catch (IOException e) {                       
                        e.printStackTrace();
                        error(e.getMessage());                              
                    } catch (UniversalReaderException e) {                      
                        e.printStackTrace();
                        error(e.getMessage());
                    } finally {
                        try {
                            urc.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setStatus(null);
                    } 
                }
            }
        };
        form.setCommandListener(this);
        form.addCommand(CMD_EXIT);        
        mDisplay.setCurrent(form);                
    }
    
    private void drawTouchScreen() {
        TouchScreenCanvas canvas = new TouchScreenCanvas("Draw from touch screen") {
            private volatile boolean mRun = true;
            private TouchEvent mSavedEvent;
            
            public void stop() {
                mRun = false;
            }  
            
            private void append(TouchEvent[] events) {
                for (int i = 0; i < events.length; i++) {
                    append(events[i]);
                }
            }
            
            private void append(TouchEvent event) {                    
                if (event.getAction() == TouchEvent.ACTION_DOWN) {
                    if (mSavedEvent != null) {                            
                        int fromX =  mSavedEvent.getX();
                        int fromY =  mSavedEvent.getY();
                        int toX =  event.getX();
                        int toY =  event.getY();
                        draw(fromX, fromY, toX, toY);                            
                    }
                    
                    mSavedEvent = event;
                } else {
                    mSavedEvent = null;
                }           
            }
            
            public void start() {
                UniversalReaderConnector urc = getConnector();
                UniversalReader universalReader;
                TouchScreen touchScreen;
                TouchEvent[] events;
                boolean pressed;
                
                synchronized (mLock) {                                       
                    try {
                        setStatus("Connecting...");
                        urc.open();
                        universalReader = new UniversalReader(
                                urc.getInputStream(), urc.getOutputStream());                        
                        touchScreen = universalReader.getTouchScreen();
                      
                        setStatus("Draw on touch screen...");
                        while (mRun) {
                            if (mRun) {
                                events = touchScreen.getEvents();                       
                                if (events != null) {
                                    append(events);
                                }
                            }
                            
                            if (mRun) {
                                pressed = universalReader.isButtonPressed(false);
                                if (pressed) {
                                    clear();                        
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
                        error(e.getMessage());                              
                    } catch (UniversalReaderException e) {                      
                        e.printStackTrace();
                        error(e.getMessage());
                    } finally {
                        try {
                            urc.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setStatus(null);
                    }
                }
            }
        };
        canvas.setCommandListener(this);   
        canvas.addCommand(CMD_EXIT);
        mDisplay.setCurrent(canvas);                
    }
}
