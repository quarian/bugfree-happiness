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

    private boolean mSpun;

    private float mPickerY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPickerOne = (TextView) findViewById(R.id.picker_1);
        mPickerTwo = (TextView) findViewById(R.id.picker_2);

        setOnTouchListener(mPickerOne);
        setOnTouchListener(mPickerTwo);

        mPickerOne.setText("20");
        mPickerTwo.setText("20");


    }

    private void setOnTouchListener(final TextView picker) {
        picker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                float y = motionEvent.getY();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mPickerY = motionEvent.getY();
                        System.out.println("On touch, y: " + motionEvent.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        System.out.println("On touch movement, y: " + motionEvent.getY());
                        if (Math.abs(y - mPickerY) > 50.0) {
                            System.out.println("Changing value");
                            if (y > mPickerY)
                                changePickerValue(picker, false);
                            else
                                changePickerValue(picker, true);
                            mPickerY = y;
                        }
                        mSpun = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!mSpun)
                            changePickerValue(picker, false);
                        System.out.println("Action up");
                        mSpun = false;
                        break;
                    default:
                        System.out.println("Default touchevent");
                        break;
                }
                return true;
            }
        });
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
