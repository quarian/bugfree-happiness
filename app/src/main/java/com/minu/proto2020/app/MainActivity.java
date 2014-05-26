package com.minu.proto2020.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private TextView mPickerOne;
    private TextView mPickerTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPickerOne = (TextView) findViewById(R.id.picker_1);
        mPickerTwo = (TextView) findViewById(R.id.picker_2);

        mPickerOne.setText("20");
        mPickerTwo.setText("20");

        mPickerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView picker = (TextView) view.findViewById(R.id.picker_1);
                int lifeTotal = Integer.parseInt(picker.getText().toString());
                lifeTotal--;
                picker.setText(Integer.toString(lifeTotal));
            }
        });

        mPickerOne.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println("Picker on long click");
                return false;
            }
        });

        mPickerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView picker = (TextView) view.findViewById(R.id.picker_2);
                int lifeTotal = Integer.parseInt(picker.getText().toString());
                lifeTotal--;
                picker.setText(Integer.toString(lifeTotal));
            }
        });

        mPickerTwo.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                return false;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
