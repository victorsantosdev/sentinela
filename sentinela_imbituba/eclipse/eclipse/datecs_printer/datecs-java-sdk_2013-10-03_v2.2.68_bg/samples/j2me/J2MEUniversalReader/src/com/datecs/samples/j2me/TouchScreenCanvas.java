package com.datecs.samples.j2me;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Ticker;

/**
 * This class implement a graphics surface that converts a touch 
 * events into lines.  
 */
public abstract class TouchScreenCanvas extends Canvas implements ExecuteMethods { 
    private static final int TS_WIDTH = 256;
    private static final int TS_HEIGHT = 192;
    
    // A buffer image that contains graphics. 
    private final Image mImage = Image.createImage(TS_WIDTH, TS_HEIGHT);
       
    public TouchScreenCanvas(String title) {
        super();
        setTitle(title);
        new Thread() {
            public void run() {
                ((ExecuteMethods)TouchScreenCanvas.this).start();
            }
        }.start();
        clear();
    }
    
    /**
     * Set current status of long time execution.
     * 
     * @param statusText the status text.
     */
    public void setStatus(String statusText) {
        if (statusText == null) {
            setTicker(null);
        } else {
            setTicker(new Ticker(statusText));
        }
    }    
    
    public void draw(int fromX, int fromY, int toX, int toY) {
        Graphics g = mImage.getGraphics();
        g.setColor(0x00, 0x00, 0x00);
        g.drawLine(fromX, fromY, toX, toY);
        repaint();
    }
    
    public void clear() {
        Graphics g = mImage.getGraphics();
        g.setColor(0xff, 0xff, 0xff);
        g.fillRect(0, 0, mImage.getWidth(), mImage.getHeight());
        repaint();
    }
    
    protected void paint(Graphics g) {        
        g.drawImage(mImage, (getWidth() - mImage.getWidth()) / 2, 
                (getHeight() - mImage.getHeight()) / 2, Graphics.TOP|Graphics.LEFT);
    }
};        
