package com.minu.proto2020.app;

import android.app.Activity;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity {

    private TextView mLifePickerOne;
    private TextView mLifePickerTwo;
    private TextView mPoisonPickerOne;
    private TextView mPoisonPickerTwo;

    private boolean mPoisonShowing;
    private ImageButton mPoisonButton;

    private String[] mOptions;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private boolean mSpun;

    private float mPickerY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemUI();

        mLifePickerOne = (TextView) findViewById(R.id.life_picker_1);
        mLifePickerTwo = (TextView) findViewById(R.id.life_picker_2);

        mPoisonPickerOne = (TextView) findViewById(R.id.poison_picker_1);
        mPoisonPickerTwo = (TextView) findViewById(R.id.poison_picker_2);

        mPoisonButton = (ImageButton) findViewById(R.id.poison_button);

        mOptions = new String[1];
        mOptions[0] = "New duel";

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,  mOptions));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        setOnTouchListener(mLifePickerOne, false);
        setOnTouchListener(mLifePickerTwo, false);
        setOnTouchListener(mPoisonPickerOne, true);
        setOnTouchListener(mPoisonPickerTwo, true);

        mPoisonPickerOne.setVisibility(View.GONE);
        mPoisonPickerTwo.setVisibility(View.GONE);

        mPoisonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("PoisonButton pushed");
                displayPoison();
            }
        });

        mDrawerLayout.setKeepScreenOn(true);
        resetDuel();

    }

    private void displayPoison() {
        if (mPoisonShowing) {
            mPoisonPickerOne.setVisibility(View.GONE);
            mPoisonPickerTwo.setVisibility(View.GONE);
            mPoisonShowing = false;
        } else {
            mPoisonPickerOne.setVisibility(View.VISIBLE);
            mPoisonPickerTwo.setVisibility(View.VISIBLE);
            mPoisonShowing = true;
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    resetDuel();
                    mDrawerLayout.closeDrawer(mDrawerList);
                    break;
                case 1:
                    break;
                default:
                    mDrawerLayout.closeDrawer(mDrawerList);
                    break;
            }
        }
    }

    private void setOnTouchListener(final TextView picker, final boolean poison) {
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
                            mSpun = true;
                            System.out.println("Changing value");
                            if (y > mPickerY)
                                changePickerValue(picker, false);
                            else
                                changePickerValue(picker, true);
                            mPickerY = y;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!mSpun)
                            changePickerValue(picker, poison);
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
    public void onResume() {
        super.onResume();
        hideSystemUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.reset) {
            resetDuel();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void resetDuel() {
        mLifePickerOne.setText("20");
        mLifePickerTwo.setText("20");
        mPoisonPickerOne.setText("0");
        mPoisonPickerTwo.setText("0");
    }


    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        getActionBar().hide();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            // Not KitKat, do something else
        }
    }

}
