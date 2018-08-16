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
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.Ticker;
import javax.microedition.midlet.MIDlet;

import com.datecs.api.card.FinancialCard;
import com.datecs.api.printer.Printer;

/**
 * This class implement a MIDlet that demonstrates using of
 * Datecs's printer SDK. 
 */
public class PrinterSample extends MIDlet implements CommandListener
        , ItemCommandListener {
    private static final Command CMD_EXIT = new Command("Exit", Command.EXIT, 1);    
    private static final Command CMD_SCAN = new Command("Scan", Command.ITEM, 1);
    private static final Command CMD_SELECT = new Command("Select", Command.ITEM, 1);
    private static final Command CMD_PRINT_SELFTEST = new Command("Print Self Test", Command.ITEM, 1);
    private static final Command CMD_PRINT_TEXT = new Command("Print Text", Command.ITEM, 1);
    private static final Command CMD_PRINT_IMAGE = new Command("Print Image", Command.ITEM, 1);
    private static final Command CMD_PRINT_PAGE = new Command("Print Page", Command.ITEM, 1);
    private static final Command CMD_PRINT_BARCODE = new Command("Print Barcode", Command.ITEM, 1);
    private static final Command CMD_READ_CARD = new Command("Read Card", Command.ITEM, 1);
    private static final Command CMD_READ_BARCODE = new Command("Read Barcode", Command.ITEM, 1);   
    
    // Defines a value to feed paper after print.
    private static final int LINES = 110;            
    
    // Object used for waiting.
    private static Object mLock = new Object();
    
    // An instance associated with current display.     
    private Display mDisplay;
    
    // An instance associated with main form.
    private Form mForm;
    // An instance associated with address text field.
    private TextField mAddressField;
    
    // A vector that contains founded Bluetooth remote devices.     
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
        
    public void startApp() {
        mDisplay = Display.getDisplay(this);
        
        mForm = new Form("Printer sample application");
        mForm.addCommand(CMD_EXIT);
        mForm.setCommandListener(this);        
        mDisplay.setCurrent(mForm);
        
        mAddressField = new TextField("Device: ", "000190E61C68", 17, TextField.ANY);
        mAddressField.setDefaultCommand(CMD_SCAN);
        mAddressField.setItemCommandListener(this);
        mForm.append(mAddressField);        
               
        appendButtonItem(mForm, CMD_PRINT_SELFTEST);          
        appendButtonItem(mForm, CMD_PRINT_TEXT);
        appendButtonItem(mForm, CMD_PRINT_IMAGE);
        appendButtonItem(mForm, CMD_PRINT_PAGE);
        appendButtonItem(mForm, CMD_PRINT_BARCODE);
        appendButtonItem(mForm, CMD_READ_CARD);
        appendButtonItem(mForm, CMD_READ_BARCODE);                 
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
                mDisplay.setCurrent(mForm);
            }            
        } else if (c == CMD_SELECT) {      
            List list = (List)d;
            int selectedIndex = list.getSelectedIndex();
            RemoteDevice remoteDevice = (RemoteDevice)mDevices.elementAt(selectedIndex);
            mAddressField.setString(remoteDevice.getBluetoothAddress());
            mDisplay.setCurrent(mForm);
        } 
    }

    /**
     * Implements an item command action listener.
     */
    public void commandAction(Command c, Item item) {
        if (c == CMD_SCAN) {
            scan();
        } else if (c == CMD_PRINT_SELFTEST) {
            printSelfTest();
        } else if (c == CMD_PRINT_TEXT) {
            printText();
        } else if (c == CMD_PRINT_IMAGE) {
            printImage();
        } else if (c == CMD_PRINT_PAGE) {
            printPage();
        } else if (c == CMD_PRINT_BARCODE) {
            printBarcode();
        } else if (c == CMD_READ_CARD) {
            readCard();
        } else if (c == CMD_READ_BARCODE) {
            readBarcode();
        }
    }
    
    /**
     * Show information alert.
     * 
     * @param text the text.
     */
    private void info(String text) {
        AlertType.INFO.playSound(Display.getDisplay(this));
        Alert alert = new Alert("Info", text, null, AlertType.INFO);
        alert.setTimeout(Alert.FOREVER);
        mDisplay.setCurrent(alert, mForm);
    }

    /**
     * Show error alert.
     * 
     * @param text the text.
     */
    private void error(String text) {
        AlertType.ERROR.playSound(Display.getDisplay(this));        
        Alert alert = new Alert("Error", text, null, AlertType.ERROR);
        alert.setTimeout(Alert.FOREVER);
        mDisplay.setCurrent(alert, mForm);
    }
    
    /**
     * Scan for Bluetooth printers in range.
     */
    private void scan() {
        final List list = new List("Select a printer", List.IMPLICIT);                      
        list.setTicker(new Ticker("Scanning for printers..."));        
        list.setCommandListener(this);        
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
    private PrinterConnector getConnector() {
        char[] buf;
        String address;
        PrinterConnector pc;
        
        buf = new char[mAddressField.size()];
        mAddressField.getChars(buf);
        address = new String(buf);
        
        pc = new PrinterConnector(address);
        return pc;        
    }
    
    /**
     * Set current status of long time execution.
     * 
     * @param statusText the status text.
     */
    private void setStatus(String statusText) {
        if (statusText == null) {
            mForm.setTicker(null);
        } else {
            mForm.setTicker(new Ticker(statusText));
        }
    }
       
    private void readBarcode() {
        new Thread() {            
            public void run() {
                PrinterConnector pc = getConnector();
                String barcode = null;
                Printer printer;
                
                synchronized (mLock) {
                    try {
                        setStatus("Connecting...");
                        pc.open();                                                
                        printer = new Printer(pc.getInputStream(), pc.getOutputStream());
                        
                        setStatus("Read barcode...");
                        barcode = printer.readBarcode(10000);                    
                    } catch (IOException e) {           
                        e.printStackTrace();
                        error(e.getMessage());
                    } finally {                        
                        try {
                            pc.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }      
                        setStatus(null);
                    }                      
                }
                
                if (barcode != null) {
                    info(barcode);
                }
            }
        }.start();                
    }
   
    private void readCard() {
        new Thread() {            
            public void run() {
                PrinterConnector pc = getConnector();
                String[] tracks = null;
                FinancialCard card = null;
                Printer printer;
                
                synchronized (mLock) {
                    try {
                        setStatus("Connecting...");
                        pc.open();
                        printer = new Printer(pc.getInputStream(), pc.getOutputStream());
                        
                        setStatus("Read card...");
                        tracks = printer.readCard(true, true, false, 10000);                     
                    } catch (IOException e) {           
                        e.printStackTrace();
                        error(e.getMessage());
                    } finally {
                        try {
                            pc.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setStatus(null);
                    }                    
                }
                
                if (tracks != null) {
                    StringBuffer msg = new StringBuffer();
                     
                    if (tracks[0] == null && tracks[1] == null) {
                        msg.append("No card readed. Please try again.");
                    } else {
                        if (tracks[0] != null) {
                            card = new FinancialCard(tracks[0]);
                        } else if (tracks[1] != null) {
                            card = new FinancialCard(tracks[1]);
                        }
                        
                        if (card != null) {
                            msg.append("NUMBER: " + card.getNumber());
                            msg.append("\n");
                            msg.append("HOLDER: " + card.getName());
                            msg.append("\n");
                            msg.append("EXPDATE: " + card.getExpiryMonth() + "/" + 
                                    card.getExpiryYear());  
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
                    }
                    
                    info(msg.toString());
                }
            }
        }.start();
    }
    
    private void printBarcode() {
        new Thread() {            
            public void run() {
                PrinterConnector pc = getConnector();
                Printer printer;
                
                synchronized (mLock) {
                    mForm.setTicker(new Ticker("Print barcode..."));
                    
                    try {
                        setStatus("Connecting...");
                        pc.open();
                        printer = new Printer(pc.getInputStream(), pc.getOutputStream());
                        
                        setStatus("Print barcode...");
                        printer.reset();
                        
                        printer.setBarcode(Printer.ALIGN_CENTER, false, 2, Printer.HRI_BELOW, 100);
                        printer.printBarcode(Printer.BARCODE_EAN13, "123456789012");
                        printer.feedPaper(38);
                        
                        printer.setBarcode(Printer.ALIGN_CENTER, false, 2, Printer.HRI_BOTH, 100);
                        printer.printBarcode(Printer.BARCODE_CODE128, "ABCDEF123456");
                        printer.feedPaper(38);
                                    
                        printer.setBarcode(Printer.ALIGN_CENTER, false, 2, Printer.HRI_NONE, 100);
                        printer.printBarcode(Printer.BARCODE_PDF417, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");         
                        
                        printer.feedPaper(LINES);
                        printer.flush();
                    } catch (IOException e) {           
                        e.printStackTrace();
                        error(e.getMessage());
                    } finally {
                        try {
                            pc.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setStatus(null);
                    }                    
                }
            }
        }.start();        
    }
    
    private void printPage() {
        new Thread() {            
            public void run() {
                PrinterConnector pc = getConnector();
                Printer printer;
                
                synchronized (mLock) {
                    try {    
                        setStatus("Connecting...");
                        pc.open();
                        printer = new Printer(pc.getInputStream(), pc.getOutputStream());
                       
                        setStatus("Print page...");
                        if (printer.getInformation().isPageModeSupported() == false) {
                            throw new IOException("The printer does not supports page mode");               
                        }
                        
                        printer.reset();            
                        printer.selectPageMode();  
                        
                        printer.setPageRegion(0, 0, 160, 320, Printer.PAGE_LEFT);            
                        printer.setPageXY(0, 4);            
                        printer.printTaggedText("{reset}{center}{b}PARAGRAPH I{br}");
                        printer.drawPageRectangle(0, 0, 160, 32, Printer.FILL_INVERTED);            
                        printer.setPageXY(0, 34);
                        printer.printTaggedText("{reset}Text printed from left to right" +
                                ", feed to bottom. Starting point in left top corner of the page.{br}");
                        printer.drawPageFrame(0, 0, 160, 320, Printer.FILL_BLACK, 1);
                        
                        printer.setPageRegion(160, 0, 160, 320, Printer.PAGE_TOP);            
                        printer.setPageXY(0, 4);            
                        printer.printTaggedText("{reset}{center}{b}PARAGRAPH II{br}");
                        printer.drawPageRectangle(160 - 32, 0, 32, 320, Printer.FILL_INVERTED);            
                        printer.setPageXY(0, 34);
                        printer.printTaggedText("{reset}Text printed from top to bottom" +
                                ", feed to left. Starting point in right top corner of the page.{br}");
                        printer.drawPageFrame(0, 0, 160, 320, Printer.FILL_BLACK, 1);
                        
                        printer.setPageRegion(160, 320, 160, 320, Printer.PAGE_RIGHT);            
                        printer.setPageXY(0, 4);            
                        printer.printTaggedText("{reset}{center}{b}PARAGRAPH III{br}");
                        printer.drawPageRectangle(0, 320 - 32, 160, 32, Printer.FILL_INVERTED);            
                        printer.setPageXY(0, 34);
                        printer.printTaggedText("{reset}Text printed from right to left" +
                                ", feed to top. Starting point in right bottom corner of the page.{br}");
                        printer.drawPageFrame(0, 0, 160, 320, Printer.FILL_BLACK, 1);
                        
                        printer.setPageRegion(0, 320, 160, 320, Printer.PAGE_BOTTOM);            
                        printer.setPageXY(0, 4);            
                        printer.printTaggedText("{reset}{center}{b}PARAGRAPH IV{br}");
                        printer.drawPageRectangle(0, 0, 32, 320, Printer.FILL_INVERTED);            
                        printer.setPageXY(0, 34);
                        printer.printTaggedText("{reset}Text printed from bottom to top" +
                                ", feed to right. Starting point in left bottom corner of the page.{br}");
                        printer.drawPageFrame(0, 0, 160, 320, Printer.FILL_BLACK, 1);
                        
                        printer.printPage();
                        printer.selectStandardMode();
                        printer.feedPaper(LINES); 
                        printer.flush();
                    } catch (IOException e) {           
                        e.printStackTrace();
                        error(e.getMessage());
                    } finally {        
                        try {
                            pc.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setStatus(null);
                    }                    
                }
            }
        }.start();                
    }
    
    private void printImage() {
        new Thread() {            
            public void run() {
                PrinterConnector pc = getConnector();
                Image image;
                Printer printer;                
                
                synchronized (mLock) {
                    try {
                        int w, h;
                        int[] argb;            
                        image = Image.createImage("/sample.png");
                        w = image.getWidth();
                        h = image.getHeight();
                        argb = new int[w * h]; 
                        image.getRGB(argb, 0, w, 0, 0, w, h);  
                        
                        setStatus("Connecting...");
                        pc.open();
                        printer = new Printer(pc.getInputStream(), pc.getOutputStream());
                        
                        setStatus("Print image...");
                        printer.reset();  
                        printer.printImage(argb, w, h, Printer.ALIGN_LEFT, true);          
                        printer.feedPaper(LINES);      
                        printer.flush();
                    } catch (IOException e) {           
                        e.printStackTrace();
                        error(e.getMessage());
                    }  finally {
                        try {
                            pc.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setStatus(null);
                    }                    
                }
            }
        }.start();          
    }
   
    private void printText() {
        new Thread() {            
            public void run() {
                PrinterConnector pc = getConnector();
                StringBuffer sb = new StringBuffer();
                Printer printer;                
                
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
                
                synchronized (mLock) {
                    try {
                        setStatus("Connecting...");
                        pc.open();
                        printer = new Printer(pc.getInputStream(), pc.getOutputStream());
                        
                        setStatus("Print text...");
                        printer.reset();  
                        printer.printTaggedText(sb.toString());         
                        printer.feedPaper(LINES);
                        printer.flush();
                    } catch (IOException e) {           
                        e.printStackTrace();
                        error(e.getMessage());
                    } finally {
                        try {
                            pc.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setStatus(null);
                    }
                    
                    mForm.setTicker(null);
                }
            }
        }.start();                
    }
    
    private void printSelfTest() {
        new Thread() {            
            public void run() {
                PrinterConnector pc = getConnector();
                Printer printer;                
                
                synchronized (mLock) {
                    try {
                        setStatus("Connecting...");
                        pc.open();
                        printer = new Printer(pc.getInputStream(), pc.getOutputStream());
                        
                        setStatus("Print self test...");
                        printer.printSelfTest();
                        printer.flush();
                    } catch (IOException e) {           
                        e.printStackTrace();
                        error(e.getMessage());
                    } finally {
                        try {
                            pc.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setStatus(null);
                    }                    
                }
            }
        }.start();        
    }   
}
