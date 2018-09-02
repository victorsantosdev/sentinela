package app.victor.sentinela.printer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.datecs.api.BuildInfo;
import com.datecs.api.printer.Printer;
import com.datecs.api.printer.PrinterInformation;
import com.datecs.api.printer.ProtocolAdapter;

import app.victor.sentinela.R;
import app.victor.sentinela.printer.BluetoothDeviceActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/* Datecs DPP-350 Printer */
public class BluetoothPrinterActivity extends Activity {
    // Debug
    private static final String LOG_TAG = "BluetoothPrinter";
    private static final boolean DEBUG = true;

    // Request to get the bluetooth device
    private static final int REQUEST_GET_DEVICE = 0;

    // The listener for all printer events
    private final ProtocolAdapter.ChannelListener mChannelListener = new ProtocolAdapter.ChannelListener() {

        @Override
        public void onReadEncryptedCard() {
            //toast(getString(R.string.msg_read_encrypted_card));
            toast("tentativa de ler cartão criptografado");
        }

        @Override
        public void onReadCard() {
            //readMagstripe();
            toast("tentativa de ler cartão magnético");

        }

        @Override
        public void onReadBarcode() {
            //readBarcode(0);
            toast("tentativa de ler código de barras");
        }

        @Override
        public void onPaperReady(boolean state) {
            if (state) {
                toast("Paper ready!");
                //toast(getString(R.string.msg_paper_ready));
            } else {
                toast("No paper on printer!");
                //toast(getString(R.string.msg_no_paper));
            }
        }

        @Override
        public void onOverHeated(boolean state) {
            if (state) {
                toast("Printer overheated! Stop printing for awhile!");
                //toast(getString(R.string.msg_overheated));
            }
        }

        @Override
        public void onLowBattery(boolean state) {
            if (state) {
                toast("Printer on low battery!");
                //toast(getString(R.string.msg_low_battery));
            }
        }
    };

    // Member variables
    private Printer mPrinter;
    private ProtocolAdapter mProtocolAdapter;
    private PrinterInformation mPrinterInfo;
    private BluetoothSocket mBluetoothSocket;
    private boolean mRestart;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.printerview_layout);
        setTitle(getString(R.string.app_name) + " SDK " + BuildInfo.VERSION);

//        findViewById(R.id.print_self_test).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                printSelfTest();
//            }
//        });
//
//        findViewById(R.id.print_text).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                printText();
//            }
//        });
//
//        findViewById(R.id.print_image).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                printImage();
//            }
//        });
//
//        findViewById(R.id.print_page).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                printPage();
//            }
//        });

//        findViewById(R.id.print_barcode).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                printBarcode();
//            }
//        });
//
//        findViewById(R.id.read_card).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                readMagstripe();
//            }
//        });
//
//        findViewById(R.id.read_barcode).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Read barcode waiting 10 seconds
//                readBarcode(10);
//            }
//        });

        mRestart = true; // ????
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
            if (resultCode == BluetoothDeviceActivity.RESULT_OK) {
                String address = data.getStringExtra(BluetoothDeviceActivity.EXTRA_DEVICE_ADDRESS);
                // address = "192.168.11.136:9100";
                if (BluetoothAdapter.checkBluetoothAddress(address)) {
                    establishBluetoothConnection(address);
                } else {
                    toast("deu zica na conexão bluetooth");
                    //establishNetworkConnection(address);
                }
            } else if (resultCode == RESULT_CANCELED) {

            } else {
                finish();
            }
        }
    }

    private void toast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void dialog(final int iconResId, final String title, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(BluetoothPrinterActivity.this);
                builder.setIcon(iconResId);
                builder.setTitle(title);
                builder.setMessage(msg);

                AlertDialog dlg = builder.create();
                dlg.show();
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
                final ProgressDialog dialog = new ProgressDialog(BluetoothPrinterActivity.this);
                dialog.setTitle("Please wait...");
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

    protected void initPrinter(InputStream inputStream, OutputStream outputStream) throws IOException {
        mProtocolAdapter = new ProtocolAdapter(inputStream, outputStream);

        if (mProtocolAdapter.isProtocolEnabled()) {
            final ProtocolAdapter.Channel channel = mProtocolAdapter.getChannel(ProtocolAdapter.CHANNEL_PRINTER);
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
            mPrinter = new Printer(channel.getInputStream(), channel.getOutputStream());
        } else {
            mPrinter = new Printer(mProtocolAdapter.getRawInputStream(), mProtocolAdapter.getRawOutputStream());
        }

        mPrinterInfo = mPrinter.getInformation();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ImageView) findViewById(R.id.icon)).setImageResource(R.drawable.icon);
                ((TextView) findViewById(R.id.name)).setText(mPrinterInfo.getName());
            }
        });
    }

    public synchronized void waitForConnection() {
        closeActiveConnection();

        // Show dialog to select a Bluetooth device.
        startActivityForResult(new Intent(this, BluetoothDeviceActivity.class), REQUEST_GET_DEVICE);
    }

    private void establishBluetoothConnection(final String address) {
        /*fecha pra não utilizar conexão por rede junto de uma conexão bluetooth*/
        //        closePrinterServer();

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
                    if (DEBUG)
                        Log.d(LOG_TAG, "Connect to " + device.getName());
                    mBluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
                    mBluetoothSocket.connect();
                    in = mBluetoothSocket.getInputStream();
                    out = mBluetoothSocket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                    error("Failed in bluetooth connection" + e.getMessage(), mRestart);
                    return;
                }

                try {
                    initPrinter(in, out);
                } catch (IOException e) {
                    e.printStackTrace();
                    error("Failed to initialize printer" + e.getMessage(), mRestart);
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
            if (DEBUG)
                Log.d(LOG_TAG, "Close Blutooth socket");
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void closePrinterConnection() {
        if (mPrinter != null) {
            mPrinter.release();
        }

        if (mProtocolAdapter != null) {
            mProtocolAdapter.release();
        }
    }

    private synchronized void closeActiveConnection() {
        closePrinterConnection();
        closeBlutoothConnection();
    }

    private void printSelfTest() {
        doJob(new Runnable() {
            @Override
            public void run() {
                try {
                    if (DEBUG)
                        Log.d(LOG_TAG, "Print Self Test");
                    mPrinter.printSelfTest();
                    mPrinter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    error(getString(R.string.msg_failed_to_print_self_test) + ". " + e.getMessage(), mRestart);
                }
            }
        }, R.string.msg_printing_self_test);
    }

    private void printText() {
        doJob(new Runnable() {
            @Override
            public void run() {
                StringBuffer sb = new StringBuffer();
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

                try {
                    if (DEBUG)
                        Log.d(LOG_TAG, "Print Text");
                    mPrinter.reset();
                    mPrinter.printTaggedText(sb.toString());
                    mPrinter.feedPaper(110);
                    mPrinter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    error(getString(R.string.msg_failed_to_print_text) + ". " + e.getMessage(), mRestart);
                }
            }
        }, R.string.msg_printing_text);
    }

    private void printImage() {
        doJob(new Runnable() {
            @Override
            public void run() {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample);
                final int width = bitmap.getWidth();
                final int height = bitmap.getHeight();
                final int[] argb = new int[width * height];
                bitmap.getPixels(argb, 0, width, 0, 0, width, height);
                bitmap.recycle();

                try {
                    if (DEBUG)
                        Log.d(LOG_TAG, "Print Image");
                    mPrinter.reset();
                    mPrinter.printImage(argb, width, height, Printer.ALIGN_CENTER, true);
                    mPrinter.feedPaper(110);
                    mPrinter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    error(getString(R.string.msg_failed_to_print_image) + ". " + e.getMessage(), mRestart);
                }
            }
        }, R.string.msg_printing_image);
    }

    private void printPage() {
        doJob(new Runnable() {
            @Override
            public void run() {
                if (mPrinterInfo == null || !mPrinterInfo.isPageModeSupported()) {
                    dialog(R.drawable.page, getString(R.string.title_warning), getString(R.string.msg_unsupport_page_mode));
                    return;
                }

                try {
                    if (DEBUG)
                        Log.d(LOG_TAG, "Print Page");
                    mPrinter.reset();
                    mPrinter.selectPageMode();

                    mPrinter.setPageRegion(0, 0, 160, 320, Printer.PAGE_LEFT);
                    mPrinter.setPageXY(0, 4);
                    mPrinter.printTaggedText("{reset}{center}{b}PARAGRAPH I{br}");
                    mPrinter.drawPageRectangle(0, 0, 160, 32, Printer.FILL_INVERTED);
                    mPrinter.setPageXY(0, 34);
                    mPrinter.printTaggedText("{reset}Text printed from left to right" + ", feed to bottom. Starting point in left top corner of the page.{br}");
                    mPrinter.drawPageFrame(0, 0, 160, 320, Printer.FILL_BLACK, 1);

                    mPrinter.setPageRegion(160, 0, 160, 320, Printer.PAGE_TOP);
                    mPrinter.setPageXY(0, 4);
                    mPrinter.printTaggedText("{reset}{center}{b}PARAGRAPH II{br}");
                    mPrinter.drawPageRectangle(160 - 32, 0, 32, 320, Printer.FILL_INVERTED);
                    mPrinter.setPageXY(0, 34);
                    mPrinter.printTaggedText("{reset}Text printed from top to bottom" + ", feed to left. Starting point in right top corner of the page.{br}");
                    mPrinter.drawPageFrame(0, 0, 160, 320, Printer.FILL_BLACK, 1);

                    mPrinter.setPageRegion(160, 320, 160, 320, Printer.PAGE_RIGHT);
                    mPrinter.setPageXY(0, 4);
                    mPrinter.printTaggedText("{reset}{center}{b}PARAGRAPH III{br}");
                    mPrinter.drawPageRectangle(0, 320 - 32, 160, 32, Printer.FILL_INVERTED);
                    mPrinter.setPageXY(0, 34);
                    mPrinter.printTaggedText("{reset}Text printed from right to left" + ", feed to top. Starting point in right bottom corner of the page.{br}");
                    mPrinter.drawPageFrame(0, 0, 160, 320, Printer.FILL_BLACK, 1);

                    mPrinter.setPageRegion(0, 320, 160, 320, Printer.PAGE_BOTTOM);
                    mPrinter.setPageXY(0, 4);
                    mPrinter.printTaggedText("{reset}{center}{b}PARAGRAPH IV{br}");
                    mPrinter.drawPageRectangle(0, 0, 32, 320, Printer.FILL_INVERTED);
                    mPrinter.setPageXY(0, 34);
                    mPrinter.printTaggedText("{reset}Text printed from bottom to top" + ", feed to right. Starting point in left bottom corner of the page.{br}");
                    mPrinter.drawPageFrame(0, 0, 160, 320, Printer.FILL_BLACK, 1);

                    mPrinter.printPage();
                    mPrinter.selectStandardMode();
                    mPrinter.feedPaper(110);
                    mPrinter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    error(getString(R.string.msg_failed_to_print_page) + ". " + e.getMessage(), mRestart);
                }
            }
        }, R.string.msg_printing_page);
    }
}
