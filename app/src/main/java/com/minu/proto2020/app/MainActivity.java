package com.minu.proto2020.app;

import android.app.Activity;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private LinearLayout mLifeLinearLayoutOne;
    private LinearLayout mLifeLinearLayoutTwo;
    private LinearLayout mPoisonLinearLayoutOne;
    private LinearLayout mPoisonLinearLayoutTwo;
    private TextView mLifePickerOne;
    private TextView mLifePickerTwo;
    private TextView mPoisonPickerOne;
    private TextView mPoisonPickerTwo;

    private LinearLayout mWrapper;

    private TextView mTempUpdateTextView;

    private boolean mPoisonShowing;
    private ImageButton mPoisonButton;

    private String[] mOptions;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private boolean mSpun;
    private boolean mSideSwipe;

    private float mPickerY;
    private float mPickerX;
    private float mPickerLastX;
    private boolean mUpdating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemUI();

        mLifeLinearLayoutOne = (LinearLayout) findViewById(R.id.first_life_picker_layout);
        mLifeLinearLayoutTwo = (LinearLayout) findViewById(R.id.second_life_picker_layout);

        mPoisonLinearLayoutOne = (LinearLayout) findViewById(R.id.first_poison_picker_layout);
        mPoisonLinearLayoutTwo = (LinearLayout) findViewById(R.id.second_poison_picker_layout);

        mLifePickerOne = (TextView) findViewById(R.id.life_picker_1);
        mLifePickerTwo = (TextView) findViewById(R.id.life_picker_2);

        mPoisonPickerOne = (TextView) findViewById(R.id.poison_picker_1);
        mPoisonPickerTwo = (TextView) findViewById(R.id.poison_picker_2);

        mPoisonButton = (ImageButton) findViewById(R.id.poison_button);

        mWrapper = (LinearLayout) findViewById(R.id.wrapper);

        mTempUpdateTextView = (TextView) findViewById(R.id.update);

        mOptions = new String[1];
        mOptions[0] = "New duel";

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,  mOptions));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        setLayoutTouchListener(mLifeLinearLayoutOne, mLifePickerOne, false);
        setLayoutTouchListener(mLifeLinearLayoutTwo, mLifePickerTwo, false);
        setLayoutTouchListener(mPoisonLinearLayoutOne, mPoisonPickerOne, true);
        setLayoutTouchListener(mPoisonLinearLayoutTwo, mPoisonPickerTwo, true);
        setTextViewOnTouchListener(mLifePickerOne, false);
        setTextViewOnTouchListener(mLifePickerTwo, false);
        setTextViewOnTouchListener(mPoisonPickerOne, true);
        setTextViewOnTouchListener(mPoisonPickerTwo, true);

        mPoisonLinearLayoutOne.setVisibility(View.GONE);
        mPoisonLinearLayoutTwo.setVisibility(View.GONE);

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
            mPoisonLinearLayoutOne.setVisibility(View.GONE);
            mPoisonLinearLayoutTwo.setVisibility(View.GONE);
            mPoisonShowing = false;
        } else {
            mPoisonLinearLayoutOne.setVisibility(View.VISIBLE);
            mPoisonLinearLayoutTwo.setVisibility(View.VISIBLE);
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

    private void setTextViewOnTouchListener(final TextView picker, final boolean poison) {
        picker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                float y = motionEvent.getY();
                float x = motionEvent.getX();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mPickerY = motionEvent.getY();
                        mPickerX = motionEvent.getX();
                        mPickerLastX = motionEvent.getX();
                        System.out.println("On picker touch, y: " + motionEvent.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        System.out.println("On picker touch movement, y: " + motionEvent.getY());
                        if (!mSideSwipe && Math.abs(y - mPickerY) > 50.0) {
                            mSpun = true;
                            System.out.println("Changing picker value");
                            if (y > mPickerY)
                                changePickerValue(picker, false);
                            else
                                changePickerValue(picker, true);
                            mPickerY = y;
                        }
                        sideSwipe(x);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!mSpun && !mSideSwipe)
                            changePickerValue(picker, poison);
                        System.out.println("Action up");
                        mSpun = false;
                        mTempUpdateTextView.setText("NOT UPDATING");
                        if (mUpdating)
                            resetDuel();
                        mWrapper.scrollTo(0, 0);
                        mUpdating = false;
                        mSideSwipe = false;
                        break;
                    default:
                        System.out.println("Default picker touchevent");
                        break;
                }
                return true;
            }
        });
    }

    private void setLayoutTouchListener(final LinearLayout layout, final TextView picker, final boolean poison) {
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                float y = motionEvent.getY();
                float x = motionEvent.getX();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mPickerX = motionEvent.getX();
                        mPickerLastX = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        sideSwipe(x);
                        break;
                    case MotionEvent.ACTION_UP:
                        mTempUpdateTextView.setText("NOT UPDATING");
                        if (mUpdating)
                            resetDuel();
                        else {
                            int[] coordinates = {0, 0};
                            System.out.println("Layout touch, coordinates and y: " + coordinates + " " + y);
                            if (y > (coordinates[1] + layout.getHeight()) / 2)
                                changePickerValue(picker, false);
                            else
                                changePickerValue(picker, true);
                            System.out.println("On layout touch, y: " + motionEvent.getY());
                        }
                        mWrapper.scrollTo(0, 0);
                        mUpdating = false;
                        mSideSwipe = false;
                        break;
                    default:
                        System.out.println("Default layout touchevent");
                        break;
                }
                return true;
            }
        });
    }

    private void sideSwipe(float x) {
        if (!mSpun && Math.abs(x - mPickerLastX) > 20.0) {
            mSideSwipe = true;
            System.out.println((int) (x - mPickerLastX));
            if (!mUpdating)
                mWrapper.scrollBy((int) -(x - mPickerLastX) / 3, 0);
            System.out.println("Side swiping");
            if (Math.abs(x - mPickerX) > 300.0) {
                mTempUpdateTextView.setText("UPDATING");
                mUpdating = true;
            } else {
                mTempUpdateTextView.setText("NOT UPDATING");
                mUpdating = false;
            }
            mPickerLastX = x;
        }
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
