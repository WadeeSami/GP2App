package com.example.wadee.nots;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.margaritov.preference.colorpicker.ColorPickerDialog;

public class WriteWordActivity extends Activity {

    private EditText screenWordEditText;
    private LinearLayout layout;
    private ColorPickerDialog dialog;
    private int chosenColor = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_word);
        layout = (LinearLayout) findViewById(R.id.activity_write_word);
        screenWordEditText = (EditText) findViewById(R.id.wordEditText);
        //initialize a dialg, but don't show
        int color = Color.parseColor("#005500");
        dialog = new ColorPickerDialog(this, color);
        dialog.setTitle("Some Title");

        dialog.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                Toast.makeText(WriteWordActivity.this, "The new Color is " + color, Toast.LENGTH_LONG).show();
                layout.setBackgroundColor(color);
                if (!screenWordEditText.getText().toString().isEmpty()) {
                    //show the color on the screen and return to the previous activity
                    WriteWordActivity.this.chosenColor = color;
                    WriteWordActivity.this.redirectToMainActivity();
                } else {
                    Toast.makeText(WriteWordActivity.this,
                            "You should tyoe a word to display on the screen",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    public void buttonClicked(View v) {
        if (v.getId() == R.id.chooseColorBtn) {
            this.dialog.show();
        }
    }

    private void redirectToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Config.CHOSEN_WORD, this.screenWordEditText.getText().toString());
        intent.putExtra(Config.CHOSEN_COLOR, this.chosenColor);
        startActivity(intent);
    }

}
