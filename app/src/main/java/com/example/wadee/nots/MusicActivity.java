package com.example.wadee.nots;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MusicActivity extends Activity {

    private String TAG = "GradPro";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_activity);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_listview,Config.songs);

        ListView listView = (ListView) findViewById(R.id.music_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String s =
                Toast.makeText(MusicActivity.this, "Some clicks, shut up " + id, Toast.LENGTH_LONG).show();

                //send music command to arduino with( id + 1 )
            }
        });
    }

}
