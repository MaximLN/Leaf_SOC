package com.example.leaf_soc;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * This is the main Activity that displays the current chat session.
 */
public class MainActivity extends Activity {
    boolean getname = false, getprotocol = false, commandmode = false, getecho = false, initialized = false, setprotocol = false;
    String devicename, deviceprotocol;
    // Debugging
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    private ArrayList<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothService mChatService = null;
    String[] initializeCommands;
    String[] initializeCommandsClimate;
    int whichCommand = 0;

    double TEMP = 0.0;
    String TEMP_LOW_PRECISION = "0.0";
    double SOH = 0.0;
    String SOH_LOW_PRECISION = "0.0";
    double SOC = 0.0;
    static int DISPLAY_WIDTH = 1;
    static int DISPLAY_HEIGHT = 1;

    static public TextView textViewTempAvg,textViewSoc,textViewSoh,textViewClimatePower;
    static public ProgressBar progressBarSoc,progressBarTempAvg,progressBarSoh,progressBarClimatePower;
    static public TextView textViewTempPercent,textViewSocPercent,textViewSohPercent,textViewClimatePowerPercent;
    static public TextView textViewSohDescription,textViewTempAvgDescription,textViewClimatePowerDescription,textViewSocDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        DISPLAY_WIDTH = size.x;
        DISPLAY_HEIGHT = size.y;

        progressBarSoc = findViewById(R.id.progressBarSoc);
        progressBarSoh = findViewById(R.id.progressBarSoh);
        progressBarTempAvg = findViewById(R.id.progressBarTempAvg);
        progressBarClimatePower = findViewById(R.id.progressBarClimatePower);
        textViewTempAvg =  findViewById(R.id.textViewTempAvg);
        textViewSoc =  findViewById(R.id.textViewSoc);
        textViewSoh =  findViewById(R.id.textViewSoh);
        textViewClimatePower =  findViewById(R.id.textViewClimatePower);
        textViewTempPercent =  findViewById(R.id.textViewTempPercent);
        textViewSohPercent =  findViewById(R.id.textViewSohPercent);
        textViewClimatePowerPercent =  findViewById(R.id.textViewClimatePowerPercent);
        textViewSohDescription  =  findViewById(R.id.textViewSohDescription);
        textViewTempAvgDescription  =  findViewById(R.id.textViewTempAvgDescription);
        textViewClimatePowerDescription  =  findViewById(R.id.textViewClimatePowerDescription);
        textViewSocPercent  =  findViewById(R.id.textViewSocPercent);
        textViewSocDescription  =  findViewById(R.id.textViewSocDescription);

        DisplayBuilder.setSize();

        //Widget update
        Intent intent = new Intent(this, LeafWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), LeafWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBlack)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorBlack)); //status bar or the time bar at the top
        }

        initializeCommands = new String[]{"ATZ", "ATSP6", "ATH1", "ATD1", "AT SH 79B", "AT FC SH 79B", "AT FC SD 30 00 20", "AT FC SM 1", "2101"};
        initializeCommandsClimate = new String[]{"ATZ", "ATSP6", "ATH1","ATL1", "ATS1", "ATAL", "ATDP", "ATCM 7FF", "ATCF 5C5", "ATMA"};
        whichCommand = 0;

// Start comments for Testing without BT -------------------------------------------
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (mChatService != null) {
            if (mChatService.getState() == BluetoothService.STATE_NONE) {
                mChatService.start();
            }
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            if (mChatService == null) setupChat();
        }
// End comments for Testing without BT-----------------------------------------------
    }
    private void setupChat() {
        Log.d(TAG, "setupChat()");
        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayList<>();
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothService(this, mHandler);
        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
        Log.d(TAG, "mChatService Initialize Ok");
        connectDevice();}

    public synchronized void onPause() {
        super.onPause();
        if (D) Log.e(TAG, "- ON PAUSE -");}
    public void onStop() {super.onStop();}
    public void onDestroy (){super.onDestroy();}
    /**
     * Sends a message.
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, "not_connected", Toast.LENGTH_LONG).show();
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            message = message + "\r";
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);}
    }
    boolean tryconnect = false;
    // The Handler that gets information back from the BluetoothChatService
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "mHandler Run ");
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Log.i(TAG, "connected_to, " + mConnectedDeviceName);
                            MainActivity.this.sendMessage("ATZ");
                            mConversationArrayAdapter.clear();
                            Log.i(TAG, "DISCONNECT");
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.i(TAG, "title_connecting");
                            tryconnect = true;
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            tryconnect = false;
                            Log.i(TAG, "title_not_connected");
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Command:  " + writeMessage.trim());
                    Log.i(TAG, "mHandler MESSAGE_WRITE");
                    break;
                case MESSAGE_READ:
                    compileMessage(msg.obj.toString());
                    Log.i(TAG, "mHandler MESSAGE_READ");
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void compileMessage(String msg) {
        msg = msg.replace("null", "");
        msg = msg.substring(0, msg.length() - 2);
        msg = msg.replaceAll("\n", "");
        msg = msg.replaceAll("\r", "");
        if (!initialized) {
            if (msg.contains("ELM327")) {
                Log.i(TAG, msg);
                msg = msg.replaceAll("ATZ", "");
                msg = msg.replaceAll("ATI", "");
                devicename = msg;
                getname = true;
                Log.i(TAG, devicename);}
            if (msg.contains("ATDP")) {
                deviceprotocol = msg.replace("ATDP", "");
                getprotocol = true;
                Log.i(TAG, "if msg.contains ATDP " + devicename + " " + deviceprotocol);}
            String send = initializeCommands[whichCommand];
            MainActivity.this.sendMessage(send);
            Log.i(TAG, "Initialize message №" + whichCommand + " Message: " + msg + " Send: " + send);
            if (whichCommand == initializeCommands.length - 1) {
                whichCommand = 0;
                initialized = true;
                Log.i(TAG, "initializeCommands All Ok");}
            else {whichCommand++;}
        } else {
            Log.i(TAG, "MSG: " + msg);
            if (msg.contains(ParseSohAndSoc.START_MSG_SOH)){
                SOH = ParseSohAndSoc.parseSoh(msg);
                progressBarSoh.setProgress((int) SOH);
                SOH_LOW_PRECISION = String.format(Locale.US,"%.1f", SOH);
                textViewSoh.setText(" "+SOH_LOW_PRECISION+" ");
                SOC = ParseSohAndSoc.parseSoc(msg);
                progressBarSoc.setProgress((int) SOC);
                textViewSoc.setText(" "+(int) SOC+" ");
                LeafWidget.widgetSocText = (int) SOC + " %";
                sendMessage("2104");}
            else if (msg.contains(ParseTemperature.START_MSG_TEMP)) {
                TEMP = ParseTemperature.parseTemp(msg);
                TEMP_LOW_PRECISION = String.format(Locale.US,"%.1f",TEMP);
                progressBarTempAvg.setProgress((int) TEMP);
                textViewTempAvg.setText(" "+TEMP_LOW_PRECISION+" ");
                LeafWidget.widgetTempText = TEMP_LOW_PRECISION + " C°";
                sendMessage("ATMA");}
            else if (msg.contains(ParseClimatePower.START_MSG_CLIMATE)) {
                ParseClimatePower.climatePower(msg);
                sendMessage("2101");}
            else {sendMessage("ATMA");}
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {getname = false;}
                break;
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    if (mChatService == null) setupChat();
                } else {
                    Toast.makeText(this, "BT not enabled", Toast.LENGTH_SHORT).show();
                    if (mChatService == null) setupChat();}
        }
    }

    private void connectDevice() {
        // Get the device MAC address
        String address = "AA:BB:CC:11:22:33";
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        getname = false;
        getprotocol = false;
        commandmode = false;
        getecho = false;
        initialized = false;
        setprotocol = false;
        whichCommand = 0;
        Log.d(TAG, "BluetoothDevice device Ok");
        mChatService.connect(device);
    }
}