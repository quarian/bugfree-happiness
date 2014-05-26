package com.minu.proto2020.app;

import android.content.ClipData;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private TextView mPickerOne;
    private TextView mPickerTwo;

    float mPickerOneY;
    float mPickerTwoY;

    private TextView debug;

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
                changePickerValue(picker, false);
            }
        });

        mPickerOne.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                boolean moved = false;
                TextView picker = (TextView) view.findViewById(R.id.picker_1);
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mPickerOneY = motionEvent.getY();
                        System.out.println("On touch, y: " + motionEvent.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        System.out.println("On touch movement, y: " + motionEvent.getY());
                        float y = motionEvent.getY();
                        if (Math.abs(y - mPickerOneY) > 50.0) {
                            if (y > mPickerOneY)
                                changePickerValue(picker, false);
                            else
                                changePickerValue(picker, true);
                            mPickerOneY = y;
                        }
                        moved = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!moved)
                            changePickerValue(picker, false);
                        System.out.println("Action up");
                        break;
                    default:
                        System.out.println("Default touchevent");
                        break;
                }
                return true;
            }
        });


        mPickerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView picker = (TextView) view.findViewById(R.id.picker_2);
                changePickerValue(picker, false);
            }
        });

        mPickerTwo.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                return false;
            }
        });

    }

    private void setOnTouchListener(TextView picker) {

    }

    private void changePickerValue(TextView picker, boolean add) {
        int lifeTotal = Integer.parseInt(picker.getText().toString());
        if (add)
            lifeTotal++;
        else
            lifeTotal--;
        picker.setText(Integer.toString(lifeTotal));
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
