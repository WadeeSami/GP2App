package com.example.wadee.nots;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BluetoothService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.wadee.nots.action.FOO";
    private static final String ACTION_BAZ = "com.example.wadee.nots.action.BAZ";
    private String TAG = "GradProBluetoothService";
    private ConnectThread mConnectThread;
    private Button sendBtn, writeWordBtn;
    private ConnectedThread dataSendingThread;
    private BluetoothDevice mDevice = null;
    private InputStream mmInStream = null;
    private OutputStream mmOutStream = null;
    private boolean b = false;
    private BluetoothSocket mmSocket;
    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.wadee.nots.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.wadee.nots.extra.PARAM2";

    public BluetoothService() {
        super("BluetoothService");
    }
    private NotificationManager nNM = null;

    @Override
    public void onCreate() {
        super.onCreate();
//        nNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Log.i(TAG, "Starting connection");
        this.getAvailableDevicesAndStartConnection();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }


    private void sendDataWithBluetooth() {
        Log.i(TAG, "Send !!!");
        if (b) {
            this.dataSendingThread.write("bhjjbh".getBytes());
        } else {
            this.dataSendingThread.write("w".getBytes());
        }
        b = !b;
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
//                tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.i(TAG, "IO Exeption in ConnectThread constructor");
            }
            mmSocket = tmp;
        }

        public void run() {

            MainActivity.mBluetoothAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                try {
                    connectException.printStackTrace();
                    mmSocket.close();
                    Log.i(TAG, "Socket Closed");
                } catch (IOException closeException) {
                    closeException.printStackTrace();
                }
                return;
            }

            //succeeded
            dataSendingThread = new ConnectedThread(mmSocket);
            Log.i(TAG, "Connection thread working !!!!! after init the trans, thread");


        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }


    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            Log.i(TAG, "The constructor of the transmission thread");
        }

        public void run() {

//            //send data using bluetooth
//            this.write("Hello".getBytes());
//            Log.i(TAG, "Send !!! from the worker thread");
//            byte[] buffer = new byte[1024];
//            int begin = 0;
//            int bytes = 0;
//            while (true) {
//                try {
//                    bytes += mmInStream.read(buffer, bytes, buffer.length - bytes);
//                    for(int i = begin; i < bytes; i++) {
//                        if(buffer[i] == "#".getBytes()[0]) {
//                            mHandler.obtainMessage(1, begin, i, buffer).sendToTarget();
//                            begin = i + 1;
//                            if(i == bytes - 1) {
//                                bytes = 0;
//                                begin = 0;
//                            }
//                        }
//                    }
//                } catch (IOException e) {
//                    break;
//                }
//            }
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
                Log.i(TAG, "Inside the wriee method");

            } catch (IOException e) {
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private void getAvailableDevicesAndStartConnection() {

        Set<BluetoothDevice> bondedDevices = MainActivity.mBluetoothAdapter.getBondedDevices();
        if (!bondedDevices.isEmpty()) {
            Log.i(TAG, "There are some bonded devices " + bondedDevices.size());
        }
        Set<BluetoothDevice> pairedDevices = MainActivity.mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().toString().equals(Config.CHOSEN_DEVICE)) {
                    Log.i(TAG, device.getName().toString());
                    this.mDevice = device;
                }
            }
        }


        if (mDevice != null) {

            this.connect();
            Log.i(TAG, "Connect method started");
            this.write("whatsapp".getBytes());
        } else {
            //there's no available devices
        }

    }


    private void connect() {


        final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        BluetoothSocket tmp = null;
        Log.i(TAG, "Connecction worked");
        try {
            tmp = this.mDevice.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.i(TAG, "IO Exeption in ConnectThread constructor");
        }

        mmSocket = tmp;
        Log.i(TAG, "Temp device    " + mDevice.getAddress());
        MainActivity.mBluetoothAdapter.cancelDiscovery();

        try {
            mmSocket.connect();
        } catch (IOException connectException) {
            try {
                connectException.printStackTrace();
                mmSocket.close();
                Log.i(TAG, "Socket Closed");
            } catch (IOException closeException) {
                closeException.printStackTrace();
            }


            return;
        }

        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = mmSocket.getInputStream();
            tmpOut = mmSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
        Log.i(TAG, "The output streams have been assigned");

        //succeeded
//        dataSendingThread = new ConnectedThread(mmSocket);
//        Log.i(TAG, "Connection thread working !!!!! after init the trans, thread");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "OnStartCommand method");
        if (intent != null) {
            Log.i(TAG, "handling the intent in the service");
            if(intent.getStringExtra(Config.NOTIFICATION_EXTRA) != null) {
                this.write("whatsapp\n".getBytes());
            }
        }else {
            Log.i(TAG, "Intent is nul ??? , WTF");
        }
        StartForground();
        return START_STICKY;

    }

    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
            Log.i(TAG, "Inside the write method");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancel() {
        try {
            this.mmSocket.close();
        } catch (IOException e) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed !!!!");
    }

    private void StartForground() {
        Notification notification = new NotificationCompat.Builder(this)
                .setOngoing(false)
                .setSmallIcon(android.R.color.transparent)

                //.setSmallIcon(R.drawable.picture)
                .build();
        startForeground(101, notification);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(TAG, "The onstart method");
    }
}

