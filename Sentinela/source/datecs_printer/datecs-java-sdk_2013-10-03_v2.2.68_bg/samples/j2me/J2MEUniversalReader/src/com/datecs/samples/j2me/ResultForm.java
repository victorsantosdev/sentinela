package com.datecs.samples.j2me;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Ticker;

/**
 * This class subclasses the original Form class and provide
 * additional abstract methods to operates with it.
 */
public abstract class ResultForm extends Form implements ExecuteMethods {

    public ResultForm(String title) {
        super(title);       
        new Thread() {
            public void run() {
                ((ExecuteMethods)ResultForm.this).start();
            }
        }.start();
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
}
