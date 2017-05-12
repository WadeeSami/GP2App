package com.example.wadee.nots;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class NotificationActivity extends Activity {
    private String TAG = "GradPro";
    private NotificationReceiver nReceiver;
    private TextView notificationsTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_activity);

        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.wadee.nots.NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(nReceiver, filter);
        notificationsTextView = (TextView)findViewById(R.id.notification_text_view);
        NotificationActivity.this.sendNotification("whatsapp");
    }



    class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra("notification_event") + "\n";//+ txtView.getText();
            notificationsTextView.setText(temp);

            //send data here to arduino
//            NotificationActivity.this.sendNotification("whatsapp");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }

    private void sendNotification(String notificationType){

        Intent bluetoothServiceIntent = new Intent(this, BluetoothService.class);
        bluetoothServiceIntent.putExtra(Config.NOTIFICATION_EXTRA, notificationType);
        startService(bluetoothServiceIntent);
        Log.i(TAG, "Not ??????? ");
    }

}
