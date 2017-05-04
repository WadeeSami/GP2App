package com.example.wadee.nots;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.margaritov.preference.colorpicker.ColorPickerDialog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity {
    private String TAG = "GradPro";
    private TextView txtView;
    private NotificationReceiver nReceiver;
    private BluetoothAdapter mBluetoothAdapter;
    private ConnectThread mConnectThread;
    private Button sendBtn;
    private ConnectedThread dataSendingThread;
    private BluetoothDevice mDevice = null;
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        txtView = (TextView) findViewById(R.id.textView);
        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.wadee.nots.NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(nReceiver,filter);

        this.initiateBluetoothConnection();



        //initialize the connection thread
        mConnectThread = new ConnectThread(mDevice);
        mConnectThread.start();

        ///
        sendBtn = (Button)findViewById(R.id.sendTextBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Sending with Bt", Toast.LENGTH_LONG).show();
                Log.i(TAG, "Send !!!");
                MainActivity.this.dataSendingThread.write("bhjjbh".getBytes());
            }
        });

        int color  = Color.parseColor("#005500");
        ColorPickerDialog dialog = new ColorPickerDialog(this,color);
        dialog.setTitle("Some Title");
        dialog.show();
        dialog.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                Toast.makeText(MainActivity.this, "The new Color is " + color, Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }





    class NotificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra("notification_event") + "\n" ;//+ txtView.getText();
            //send data here to arduino
//            txtView.setText(temp);
        }
    }


    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private  final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }
        public void run() {
            mBluetoothAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
                MainActivity.this.dataSendingThread = new ConnectedThread(mmSocket);
                Log.i(TAG, "Connection thread working");
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
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
            } catch (IOException e) { }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {

            //send data using bluetooth
            this.write("Hello".getBytes());
            Log.i(TAG, "Send !!! from the worker thread");
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

            } catch (IOException e) { }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private void sendCommandUsingBluetooth(String data){
        this.mConnectThread.start();
    }

    private void initiateBluetoothConnection(){
        //handle bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(this, "Sorry, Your Wretched device does not support Bluetooth", Toast.LENGTH_LONG).show();
        }
        //check if the bluetooth is not enabled and show some UI to open Bluetooth
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if(device.getName().toString().equals("Wadee’s MacBook Pro")){
                    Log.i(TAG, device.getName().toString());
                    this.mDevice = device;
                }
            }
        }

    }
}
