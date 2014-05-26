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
                int lifeTotal = Integer.parseInt(picker.getText().toString());
                lifeTotal--;
                picker.setText(Integer.toString(lifeTotal));
            }
        });

        mPickerOne.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadow = new View.DragShadowBuilder();
                        view.startDrag(data, shadow, null, 0);
                        System.out.println("On touch, y: " + motionEvent.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        System.out.println("On touch movement, y: " + motionEvent.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        System.out.println("Action up");
                        break;
                    default:
                        System.out.println("Default touchevent");
                        break;
                }
                return true;
            }
        });

        mPickerOne.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                int action = dragEvent.getAction();
                float y = dragEvent.getY();
                System.out.println("Y: " + y);
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        y = dragEvent.getY();
                        System.out.println("location Y: " + y);
                        break;
                    case DragEvent.ACTION_DROP:
                        y = dragEvent.getY();
                        System.out.println("drop Y: " + y);
                        break;
                    default:
                        y = dragEvent.getY();
                        System.out.println("default Y: " + y);
                        break;
                }
                return true;
            }
        });

        mPickerOne.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println("Picker on long click");
                return true;
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
