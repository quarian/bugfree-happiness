package com.minu.proto2020.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.NumberPicker;


public class MainActivity extends ActionBarActivity implements NumberPicker.OnScrollListener {

    private NumberPicker mPickerOne;
    private NumberPicker mPickerTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPickerOne = (NumberPicker) findViewById(R.id.picker_1);
        mPickerTwo = (NumberPicker) findViewById(R.id.picker_2);

        mPickerOne.setOnScrollListener(this);
        mPickerTwo.setOnScrollListener(this);

        mPickerOne.setMaxValue(1000);
        mPickerTwo.setMaxValue(1000);

        mPickerOne.setValue(20);
        mPickerTwo.setValue(20);

        mPickerOne.setWrapSelectorWheel(false);
        mPickerTwo.setWrapSelectorWheel(false);
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

    @Override
    public void onScrollStateChange(NumberPicker numberPicker, int i) {

    }
}
