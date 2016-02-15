package com.minu.proto2020.app;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final boolean SCALE_UP = true;
    private static final boolean SCALE_DOWN = false;
    private LinearLayout mLifeLinearLayoutOne;
    private LinearLayout mLifeLinearLayoutTwo;
    private LinearLayout mPoisonLinearLayoutOne;
    private LinearLayout mPoisonLinearLayoutTwo;
    private TextView mLifePickerOne;
    private TextView mLifePickerTwo;
    private TextView mPoisonPickerOne;
    private TextView mPoisonPickerTwo;

    static final String STARTING_LIFE = "20";
    static final String STARTING_POISON = "0";

    static final String PICKER_ONE_LIFE = "PICKER_ONE_LIFE";
    static final String PICKER_TWO_LIFE = "PICKER_ONE_POISON";
    static final String PICKER_ONE_POISON = "PICKER_TWO_LIFE";
    static final String PICKER_TWO_POISON = "PICKER_TWO_POISON";

    static final String HISTORY = "HISTORY";

    private LinearLayout mWrapper;

    private TextView mLeftUpdateTextView;
    private TextView mRighyUpdateTextView;

    private boolean mPoisonShowing;

    private ArrayList<String> mOptions;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private boolean mSpun;
    private boolean mSideSwipe;

    private float mPickerY;
    private float mPickerX;
    private float mPickerLastX;
    private boolean mUpdating;

    private ArrayList<String> mHistory;
    final private int mHistoryStart = 2;

    private String mShowPoison = "Show Poison Counters";
    private String mHidePoison = "Hide Poison Counters";

    private String mPoisonOption = mShowPoison;
    private int mPoisonOptionIndex = 1;

    private float mCurrentRotation = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideSystemUI();

        bindElements();

        initElements();

        if (savedInstanceState != null) {
            System.out.println("Restoring state");
            setLifeTotals(savedInstanceState.getString(PICKER_ONE_LIFE),
                    savedInstanceState.getString(PICKER_TWO_POISON),
                    savedInstanceState.getString(PICKER_TWO_LIFE),
                    savedInstanceState.getString(PICKER_TWO_POISON));

                    mHistory = savedInstanceState.getStringArrayList(HISTORY);
                    mOptions.addAll(mHistoryStart, mHistory);
                    ((ArrayAdapter<String>)mDrawerList.getAdapter()).notifyDataSetChanged();
        } else {
            mHistory = new ArrayList<String>();
            resetDuel();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        System.out.println("Saving state");
        savedInstanceState.putString(PICKER_ONE_LIFE, mLifePickerOne.getText().toString());
        savedInstanceState.putString(PICKER_ONE_POISON, mPoisonPickerTwo.getText().toString());
        savedInstanceState.putString(PICKER_TWO_LIFE, mLifePickerTwo.getText().toString());
        savedInstanceState.putString(PICKER_TWO_POISON, mPoisonPickerTwo.getText().toString());

        savedInstanceState.putStringArrayList(HISTORY, mHistory);

        super.onSaveInstanceState(savedInstanceState);
    }

    private void bindElements() {
        mLifeLinearLayoutOne = (LinearLayout) findViewById(R.id.first_life_picker_layout);
        mLifeLinearLayoutTwo = (LinearLayout) findViewById(R.id.second_life_picker_layout);

        mPoisonLinearLayoutOne = (LinearLayout) findViewById(R.id.first_poison_picker_layout);
        mPoisonLinearLayoutTwo = (LinearLayout) findViewById(R.id.second_poison_picker_layout);

        mLifePickerOne = (TextView) findViewById(R.id.life_picker_1);
        mLifePickerTwo = (TextView) findViewById(R.id.life_picker_2);

        mPoisonPickerOne = (TextView) findViewById(R.id.poison_picker_1);
        mPoisonPickerTwo = (TextView) findViewById(R.id.poison_picker_2);

        mWrapper = (LinearLayout) findViewById(R.id.wrapper);

        mLeftUpdateTextView = (TextView) findViewById(R.id.update);
        mRighyUpdateTextView = (TextView) findViewById(R.id.update_2);

        mOptions = new ArrayList<String>();
        instansiateOptions();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                collapseHistory();
                showHistory();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
    }

    private void instansiateOptions() {
        mOptions.add("New Duel");
        mOptions.add(mPoisonOption);
    }

    private void collapseHistory() {
        long currentTime;
        long nextTime;
        for (int i = 0; i + 2 < mHistory.size(); i++) {
            if (!isHistoryEntryRead(mHistory.get(i))) {
                currentTime = parseTimeStamp(mHistory.get(i));
                nextTime = parseTimeStamp(mHistory.get(i + 1));
                if (nextTime - currentTime < 2000) {
                    mHistory.remove(i + 1);
                    i--;
                }
            }
        }
        for (int i = 0; i + 1 < mHistory.size(); i++) {
            mHistory.set(i, markedHistoryEntryRead(mHistory.get(i)));
        }
    }

    private void showHistory() {
        mOptions.subList(mHistoryStart, mOptions.size()).clear();
        if (mHistory.size() > 2)
            mHistory.remove(mHistory.size() - 2); // MAGIC: the CODENING
        mOptions.addAll(mHistoryStart, mHistory);
        ((ArrayAdapter<String>)mDrawerList.getAdapter()).notifyDataSetChanged();
    }

    private long parseTimeStamp(String historyEntry) {
        String timeString = historyEntry.split(" ")[4];
        return Long.parseLong(timeString);
    }

    private boolean isHistoryEntryRead(String historyEntry) {
        String read = historyEntry.split(" ")[4];
        return read.compareTo("READ") == 0;
    }

    private String markedHistoryEntryRead(String historyEntry) {
        String[] split = historyEntry.split(" ");
        split[4] = "READ";
        return split[0] + " " + split[1] + " " + split[2] + " " + split[3]+ " " + split[4];
    }

    private void initElements() { mDrawerList.setAdapter(new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, mOptions));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        setLayoutTouchListener(mLifeLinearLayoutOne, mLifePickerOne);
        setLayoutTouchListener(mLifeLinearLayoutTwo, mLifePickerTwo);
        setLayoutTouchListener(mPoisonLinearLayoutOne, mPoisonPickerOne);
        setLayoutTouchListener(mPoisonLinearLayoutTwo, mPoisonPickerTwo);
        setTextViewOnTouchListener(mLifePickerOne, false);
        setTextViewOnTouchListener(mLifePickerTwo, false);
        setTextViewOnTouchListener(mPoisonPickerOne, true);
        setTextViewOnTouchListener(mPoisonPickerTwo, true);

        mPoisonLinearLayoutOne.setVisibility(View.GONE);
        mPoisonLinearLayoutTwo.setVisibility(View.GONE);

        mDrawerLayout.setKeepScreenOn(true);
    }

    private void displayPoison() {
        if (mPoisonShowing) {
            mPoisonLinearLayoutOne.setVisibility(View.GONE);
            mPoisonLinearLayoutTwo.setVisibility(View.GONE);
            mPoisonShowing = false;
            mPoisonOption = mShowPoison;
        } else {
            mPoisonLinearLayoutOne.setVisibility(View.VISIBLE);
            mPoisonLinearLayoutTwo.setVisibility(View.VISIBLE);
            mPoisonShowing = true;
            mPoisonOption = mHidePoison;
        }
        mOptions.set(mPoisonOptionIndex, mPoisonOption);
        ((ArrayAdapter<String>)mDrawerList.getAdapter()).notifyDataSetChanged();
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
                    displayPoison();
                    mDrawerLayout.closeDrawer(mDrawerList);
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
                handlePickerTouchEvent(x, y, action, motionEvent, picker, poison);
                return true;
            }
        });
    }

    private void handlePickerTouchEvent(float x, float y, int action,
                                        MotionEvent motionEvent, TextView picker, boolean poison) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                recordTouchStart(motionEvent);
                break;
            case MotionEvent.ACTION_MOVE:
                verticalSwipe(y, picker);
                sideSwipe(x);
                break;
            case MotionEvent.ACTION_UP:
                handlePickerTouchRelease(picker, poison);
                break;
            default:
                System.out.println("Default picker touchevent");
                break;
        }
    }

    private void handlePickerTouchRelease(TextView picker, boolean poison) {
        if (!mSpun && !mSideSwipe) {
            changePickerValue(picker, poison);
            scaleTextView(picker, SCALE_DOWN);
        }
        System.out.println("Action up");
        if (mSpun)
            scaleTextView(picker, SCALE_DOWN);
        mSpun = false;
        addToHistory(getTotals());
        setUpdateTextViewTexts("NOT UPDATING");
        if (mUpdating)
            resetDuel();
        mWrapper.scrollTo(0, 0);
        mUpdating = false;
        mSideSwipe = false;
    }

    private void setUpdateTextViewTexts(String s) {
        if (mLeftUpdateTextView.getText().toString().compareTo(s) != 0) {
            mRighyUpdateTextView.setText(s);
            mLeftUpdateTextView.setText(s);
            spinResetArrows();
        }
    }

    private void setLayoutTouchListener(final LinearLayout layout, final TextView picker) {
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                float y = motionEvent.getY();
                float x = motionEvent.getX();
                handleLayoutTouchEvent(x, y, action, motionEvent, picker, layout);
                return true;
            }
        });
    }

    private void handleLayoutTouchEvent(float x, float y, int action, MotionEvent motionEvent,
                                        TextView picker, LinearLayout layout) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                recordTouchStart(motionEvent);
                break;
            case MotionEvent.ACTION_MOVE:
                verticalSwipe(y, picker);
                sideSwipe(x);
                break;
            case MotionEvent.ACTION_UP:
                handleLayoutTouchRelease(y, picker, layout);
                break;
            default:
                System.out.println("Default layout touchevent");
                break;
        }
    }

    private void handleLayoutTouchRelease(float y, TextView picker, LinearLayout layout) {
        setUpdateTextViewTexts("NOT UPDATING");
        if (mUpdating)
            resetDuel();
        else
            peripheralTouch(y, picker, layout);
        mWrapper.scrollTo(0, 0);
        mUpdating = false;
        mSideSwipe = false;
    }

    private void recordTouchStart(MotionEvent motionEvent) {
        mPickerY = motionEvent.getY();
        mPickerX = motionEvent.getX();
        mPickerLastX = motionEvent.getX();
    }

    private void sideSwipe(float x) {
        if (!mSpun && Math.abs(x - mPickerLastX) > 20.0)
            mSideSwipe = true;
        if (mSideSwipe) {
            System.out.println((int) (x - mPickerLastX));
            if (!mUpdating)
                mWrapper.scrollBy((int) -(x - mPickerLastX) / 2, 0);
            System.out.println("Side swiping");
            if (Math.abs(x - mPickerX) > 300.0) {
                setUpdateTextViewTexts("UPDATING");
                mUpdating = true;
            } else {
                setUpdateTextViewTexts("NOT UPDATING");
                mUpdating = false;
            }
            mPickerLastX = x;
        }
    }

    private void verticalSwipe(float y, TextView picker) {
        if (!mSideSwipe && Math.abs(y - mPickerY) > 50.0) {
            if (!mSpun)
                scaleTextView(picker, SCALE_UP);
            mSpun = true;
            System.out.println("Changing picker value");
            if (y > mPickerY)
                changePickerValue(picker, false);
            else
                changePickerValue(picker, true);
            mPickerY = y;
        }
    }

    private void peripheralTouch(float y, TextView picker, LinearLayout layout) {
        if (mSpun) {
            mSpun = false;
            scaleTextView(picker, SCALE_DOWN);
        } else {
            int[] coordinates = {0, 0};
            System.out.println("Layout touch, coordinates and y: " + coordinates + " " + y);
            scaleTextView(picker, SCALE_DOWN);
            if (y > (coordinates[1] + layout.getHeight()) / 2)
                changePickerValue(picker, false);
            else
                changePickerValue(picker, true);
        }
    }

    private void changePickerValue(TextView picker, boolean add) {
        int lifeTotal = Integer.parseInt(picker.getText().toString());
        if (add)
            lifeTotal++;
        else
            lifeTotal--;
        picker.setText(Integer.toString(lifeTotal));
        addToHistory(getTotals());
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
        mLifePickerOne.setText(STARTING_LIFE);
        mLifePickerTwo.setText(STARTING_LIFE);
        mPoisonPickerOne.setText(STARTING_POISON);
        mPoisonPickerTwo.setText(STARTING_POISON);
        mHistory.clear();
        mOptions.clear();
        instansiateOptions();
        addToHistory(getTotals());
        ((ArrayAdapter<String>)mDrawerList.getAdapter()).notifyDataSetChanged();
    }

    public void addToHistory(String[] totals) {
        String timeStamp = Long.toString(System.currentTimeMillis());
        System.out.println("Adding to history " + totals[0] + " " + totals[1] + " "
                + totals[2] + " " + totals[3] + " " + timeStamp);
        mHistory.add(totals[0] + " " + totals[1] + " "
                + totals[2] + " " + totals[3] + " " + timeStamp);
    }

    public String[] getTotals() {
        String[] result = {mLifePickerOne.getText().toString(), mLifePickerTwo.getText().toString(),
                           mPoisonPickerOne.getText().toString(), mPoisonPickerTwo.getText().toString()};
        return result;
    }


    private void setLifeTotals(String lifePickerOne, String poisonPickerOne,
                               String lifePickerTwo, String poisonPickerTwo) {
        mLifePickerOne.setText(lifePickerOne);
        mLifePickerTwo.setText(lifePickerTwo);
        mPoisonPickerOne.setText(poisonPickerOne);
        mPoisonPickerTwo.setText(poisonPickerTwo);
    }

    private void spinResetArrows() {
        ImageView leftArrow = (ImageView) findViewById(R.id.update_arrow_left);
        ImageView rightArrow = (ImageView) findViewById(R.id.update_arrow_right);
        RotateAnimation r = new RotateAnimation(mCurrentRotation, mCurrentRotation + 180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setDuration((long) 1000);
        r.setRepeatCount(0);
        r.setFillAfter(true);
        mCurrentRotation += 180.0f;
        leftArrow.startAnimation(r);
        rightArrow.startAnimation(r);
    }

    private void scaleTextView(TextView view, boolean scaleUp) {
        Animation scaleAnimation;
        if (scaleUp)
            scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.text_scale_up);
        else
            scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.text_scale_down);
        view.startAnimation(scaleAnimation);
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        getActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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
