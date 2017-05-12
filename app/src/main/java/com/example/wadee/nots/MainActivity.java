package com.example.wadee.nots;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private String TAG = "GradPro";
    private TextView txtView;
    public static BluetoothAdapter mBluetoothAdapter;
    //    private ConnectThread mConnectThread;
    private Button sendBtn, writeWordBtn;
    //    private ConnectedThread dataSendingThread;
    private BluetoothDevice mDevice = null;
    private boolean b = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initiateBluetoothConnection();


        sendBtn = (Button) findViewById(R.id.sendTextBtn);
        writeWordBtn = (Button) findViewById(R.id.openColorActivity);
//        sendBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.this.sendDataWithBluetooth();
//            }
//        });


    }

    public void buttonClicked(View v) {
        if (v.getId() == R.id.openColorActivity) {
            Toast.makeText(this, "Sending with Bt", Toast.LENGTH_LONG).show();
            //start a new activity
            Intent intent = new Intent(MainActivity.this, WriteWordActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.notificationModeBtn) {
            Log.i(TAG, "Start the notification mode");
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.musicModeBtn) {
            Intent intent = new Intent(MainActivity.this, MusicActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(nReceiver);
    }


    class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra("notification_event") + "\n";//+ txtView.getText();
            Log.i(TAG, "Not!!!!!");
            //send data here to arduino
//            txtView.setText(temp);
        }
    }


//    private class ConnectThread extends Thread {
//        private final BluetoothSocket mmSocket;
//        private final BluetoothDevice mmDevice;
//        private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
//
//        public ConnectThread(BluetoothDevice device) {
//            BluetoothSocket tmp = null;
//            mmDevice = device;
//            try {
//                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
////                tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
//            } catch (IOException e) {
//                Log.i(TAG, "IO Exeption in ConnectThread constructor");
//            }
//            mmSocket = tmp;
//        }
//
//        public void run() {
//
//            mBluetoothAdapter.cancelDiscovery();
//            try {
//                mmSocket.connect();
////                Log.i(TAG, "Connection thread after !!!!!");
////                Intent i = getIntent();
////                Bundle extraData = i.getExtras();
////                if (extraData != null) {
////                    String word = extraData.getString(Config.CHOSEN_WORD);
////                    int chosenColor = extraData.getInt(Config.CHOSEN_COLOR);
////                    //send data to the arduino ina  certain way
////                    Toast.makeText(MainActivity.this, "REcieved color and word", Toast.LENGTH_LONG).show();
////                    MainActivity.this.sendDataWithBluetooth();
////                }
//            } catch (IOException connectException) {
//                try {
//                    connectException.printStackTrace();
//                    mmSocket.close();
//                    Log.i(TAG, "Socket Closed");
//                } catch (IOException closeException) {
//                    closeException.printStackTrace();
//                }
//                return;
//            }
//
//            //succeeded
//            MainActivity.this.dataSendingThread = new ConnectedThread(mmSocket);
//            Log.i(TAG, "Connection thread working !!!!! after init the trans, thread");
//
//
//        }
//
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) {
//            }
//        }
//    }


//    private class ConnectedThread extends Thread {
//        private final BluetoothSocket mmSocket;
//        private final InputStream mmInStream;
//        private final OutputStream mmOutStream;
//
//        public ConnectedThread(BluetoothSocket socket) {
//            mmSocket = socket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e) {
//            }
//            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
//            Log.i(TAG, "The constructor of the transmission thread");
//        }
//
//        public void run() {
//
////            //send data using bluetooth
////            this.write("Hello".getBytes());
////            Log.i(TAG, "Send !!! from the worker thread");
////            byte[] buffer = new byte[1024];
////            int begin = 0;
////            int bytes = 0;
////            while (true) {
////                try {
////                    bytes += mmInStream.read(buffer, bytes, buffer.length - bytes);
////                    for(int i = begin; i < bytes; i++) {
////                        if(buffer[i] == "#".getBytes()[0]) {
////                            mHandler.obtainMessage(1, begin, i, buffer).sendToTarget();
////                            begin = i + 1;
////                            if(i == bytes - 1) {
////                                bytes = 0;
////                                begin = 0;
////                            }
////                        }
////                    }
////                } catch (IOException e) {
////                    break;
////                }
////            }
//        }
//
//        public void write(byte[] bytes) {
//            try {
//                mmOutStream.write(bytes);
//                Log.i(TAG, "Inside the wriee method");
//
//            } catch (IOException e) {
//            }
//        }
//
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) {
//            }
//        }
//    }

//    private void sendCommandUsingBluetooth(String data) {
//        this.mConnectThread.start();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Config.REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //the bluetooth has been enabled
                this.startBluetoothService();


            } else {

                Toast.makeText(this, "This app will not work until Bluetooth is working, please try to fix your bluetooth", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void initiateBluetoothConnection() {
        //handle bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(this, "Sorry, Your Wretched device does not support Bluetooth", Toast.LENGTH_LONG).show();
        }
        //check if the bluetooth is not enabled and show some UI to open Bluetooth
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, Config.REQUEST_ENABLE_BT);
            //the rest of the code will be executed in the onActivityResult method
        } else {
            this.startBluetoothService();
        }


    }

//    private void sendDataWithBluetooth() {
//        Toast.makeText(MainActivity.this, "Sending with Bt", Toast.LENGTH_LONG).show();
//        Log.i(TAG, "Send !!!");
//        if(b) {
//            this.dataSendingThread.write("bhjjbh".getBytes());
//        }else{
//            this.dataSendingThread.write("w".getBytes());
//        }
//        b = !b;
//    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "The onStart Method");
    }

//    private void getAvailableDevicesAndStartConnection() {
//
//        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
//        if( ! bondedDevices.isEmpty()) {
//            Log.i(TAG, "There are some bonded devices " + bondedDevices.size());
//        }
//        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//
//        if (pairedDevices.size() > 0) {
//            for (BluetoothDevice device : pairedDevices) {
//                if (device.getName().toString().equals(Config.CHOSEN_DEVICE)) {
//                Log.i(TAG, device.getName().toString());
//                this.mDevice = device;
//                }
//            }
//        }
//
//
//        if (mDevice != null) {
//            //initialize the connection thread
//            mConnectThread = new ConnectThread(mDevice);
//            mConnectThread.start();
//            Log.i(TAG, "Connection thread started");
//        }
//
//    }


    private void startBluetoothService() {
        Intent bluetoothServiceIntent = new Intent(this, BluetoothService.class);
        startService(bluetoothServiceIntent);

    }
}
