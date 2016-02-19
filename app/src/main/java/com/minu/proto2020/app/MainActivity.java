package com.minu.proto2020.app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final boolean SCALE_UP = true;
    private static final boolean SCALE_DOWN = false;
    private static final int LETHAL_LIFE = 0;
    private static final int LETHAL_POISON = 10;
    private LinearLayout mLifeLinearLayoutOne;
    private LinearLayout mLifeLinearLayoutTwo;
    private LinearLayout mPoisonLinearLayoutOne;
    private LinearLayout mPoisonLinearLayoutTwo;
    private TextView mLifePickerOne;
    private TextView mLifePickerTwo;
    private TextView mPoisonPickerOne;
    private TextView mPoisonPickerTwo;

    private ImageButton mSettingsButton;
    private ImageButton mHistoryButton;

    static final String STARTING_LIFE = "20";
    static final String STARTING_POISON = "0";

    static final String PICKER_ONE_LIFE = "PICKER_ONE_LIFE";
    static final String PICKER_TWO_LIFE = "PICKER_ONE_POISON";
    static final String PICKER_ONE_POISON = "PICKER_TWO_LIFE";
    static final String PICKER_TWO_POISON = "PICKER_TWO_POISON";

    static final String HISTORY = "HISTORY";

    private String mWhiteBackgroundColor = "#f5f5f5";
    private String mBlackBackgroundColor = "#333231";

    private LinearLayout mWrapper;

    private TextView mLeftUpdateTextView;
    private TextView mRighyUpdateTextView;

    private boolean mPoisonShowing;

    private ArrayList<String> mOptions;

    private DrawerLayout mSettingsDrawerLayout;
    private ListView mSettingsDrawerList;
    private RelativeLayout mSettingsDrawer;

    private DrawerLayout mHistoryDrawerLayout;
    private ListView mHistoryDrawerList;

    private int mScreenHeight;
    private int mScreenWidth;

    private boolean mSpun;
    private boolean mSideSwipe;

    private float mPickerY;
    private float mPickerX;
    private float mPickerLastX;
    private boolean mUpdating;

    private ArrayList<String> mHistory;
    final private int mHistoryStart = 2;

    private String mShowPoison = "Poison";
    private String mHidePoison = "Poison";

    private String mPullToRefresh = "Pull to Restart";
    private String mReleaseToRefresh = "Pull to Cancel";

    private String mPoisonOption = mShowPoison;
    private int mPoisonOptionIndex = 2;

    private float mCurrentRotation = 0.0f;
    private boolean mWhiteBackground = true;

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
                    ((ArrayAdapter<String>) mHistoryDrawerList.getAdapter()).notifyDataSetChanged();
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

        setInitialColors();

        mWrapper = (LinearLayout) findViewById(R.id.wrapper);

        mLeftUpdateTextView = (TextView) findViewById(R.id.update);
        mRighyUpdateTextView = (TextView) findViewById(R.id.update_2);

        mSettingsButton = (ImageButton) findViewById(R.id.settings_button);
        mHistoryButton = (ImageButton) findViewById(R.id.history_button);

        mOptions = new ArrayList<String>();
        mHistory = new ArrayList<String>();


        mSettingsDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mHistoryDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mSettingsDrawer = (RelativeLayout) findViewById(R.id.settings_drawer);

        mSettingsDrawerList = (ListView) findViewById(R.id.left_drawer);
        mHistoryDrawerList = (ListView) findViewById(R.id.right_drawer);
    }

    private void instantiateArrayLists() {
        mOptions.add("New duel");
        mOptions.add("Starting life total");
        mOptions.add(mPoisonOption);
        mOptions.add("Color scheme");
    }

    private void setInitialColors() {
        int red = Color.parseColor("#e3aaaa");
        int blue = Color.parseColor("#9bb8d5");
        mLifePickerOne.setTextColor(red);
        mPoisonPickerOne.setTextColor(red);
        mLifePickerTwo.setTextColor(blue);
        mPoisonPickerTwo.setTextColor(blue);
        Drawable poisonIconOne = getResources().getDrawable(R.drawable.poison_symbol_1);
        Drawable poisonIconTwo = getResources().getDrawable(R.drawable.poison_symbol_2);
        poisonIconOne.setColorFilter(red, PorterDuff.Mode.SRC_ATOP);
        poisonIconTwo.setColorFilter(blue, PorterDuff.Mode.SRC_ATOP);
        ((ImageView)findViewById(R.id.poison_symbol_1)).setImageDrawable(poisonIconOne);
        ((ImageView)findViewById(R.id.poison_symbol_2)).setImageDrawable(poisonIconTwo);
    }

    private void collapseHistory() {
        long currentTime;
        long nextTime;
        for (int i = 0; i + 1 < mHistory.size(); i++) {
            if (!isHistoryEntryRead(mHistory.get(i))) {
                currentTime = parseTimeStamp(mHistory.get(i));
                nextTime = parseTimeStamp(mHistory.get(i + 1));
                if (nextTime - currentTime < 2000) {
                    mHistory.remove(i);
                    i--;
                }
            }
        }
        for (int i = 0; i < mHistory.size(); i++)
            mHistory.set(i, markedHistoryEntryRead(mHistory.get(i)));
        ((ArrayAdapter)mHistoryDrawerList.getAdapter()).clear();
        ((ArrayAdapter)mHistoryDrawerList.getAdapter()).addAll(mHistory);
    }

    private void showHistory() {
        ((ArrayAdapter<String>) mHistoryDrawerList.getAdapter()).notifyDataSetChanged();
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

    private void initElements() {

        mSettingsDrawerList.setAdapter(new SettingsListAdapter(this, mOptions));

        mHistoryDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mHistory));

        mSettingsDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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

        instantiateArrayLists();

        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingsDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingsDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        mSettingsDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (drawerView.equals(mHistoryDrawerList)) {
                    System.out.println("Should show history");
                    System.out.println(mHistory);
                    collapseHistory();
                    showHistory();
                    mSettingsDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mSettingsDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        mScreenHeight = display.getWidth();
        mScreenWidth = display.getHeight();

        mSettingsDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
        mSettingsDrawerLayout.setKeepScreenOn(true);
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

        ((SettingsListAdapter)mSettingsDrawerList.getAdapter()).notifyDataSetChanged();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    resetDuel();
                    mSettingsDrawerLayout.closeDrawer(mSettingsDrawer);
                    break;
                case 1:
                    break;
                case 2:
                    String showPoison =
                            ((TextView) findViewById(R.id.poison_toggle)).getText().toString();
                    if (showPoison.equals("on") && !mPoisonShowing ||
                            showPoison.equals("off") && mPoisonShowing) {
                        displayPoison();
                    }
                    break;
                case 3:
                    String tag = findViewById(R.id.background_preview).getTag().toString();
                    if (tag.equals("dark"))
                        mSettingsDrawerLayout.setBackgroundColor(Color.parseColor(mBlackBackgroundColor));
                    else
                        mSettingsDrawerLayout.setBackgroundColor(Color.parseColor(mWhiteBackgroundColor));
                    mWhiteBackground = !mWhiteBackground;
                    //mSettingsDrawerLayout.closeDrawer(mSettingsDrawerList);
                    break;
                default:
                    mSettingsDrawerLayout.closeDrawer(mSettingsDrawer);
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
                recordTouchStart(motionEvent, picker);
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
        if (mSpun || mSideSwipe)
            scaleTextView(picker, SCALE_DOWN);
        mSpun = false;
        setUpdateTextViewTexts(mPullToRefresh);
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
                recordTouchStart(motionEvent, picker);
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
        setUpdateTextViewTexts(mPullToRefresh);
        if (mUpdating)
            resetDuel();
        else if (!mSideSwipe)
            peripheralTouch(y, picker, layout);
        else
            scaleTextView(picker, SCALE_DOWN);
        mWrapper.scrollTo(0, 0);
        mUpdating = false;
        mSideSwipe = false;
    }

    private void recordTouchStart(MotionEvent motionEvent, TextView picker) {
        mPickerY = motionEvent.getY();
        mPickerX = motionEvent.getX();
        mPickerLastX = motionEvent.getX();
        scaleTextView(picker, SCALE_UP);
    }

    private void sideSwipe(float x) {
        if (!mSpun && Math.abs(x - mPickerLastX) > mScreenWidth / 40)
            mSideSwipe = true;
        if (mSideSwipe) {
            System.out.println((int) (x - mPickerLastX));
            if (!mUpdating)
                mWrapper.scrollBy((int) -(x - mPickerLastX) / 2, 0);
            System.out.println("Side swiping");
            if (Math.abs(x - mPickerX) > mScreenWidth / 4) {
                setUpdateTextViewTexts(mReleaseToRefresh);
                mUpdating = true;
            } else {
                setUpdateTextViewTexts(mPullToRefresh);
                mUpdating = false;
            }
            mPickerLastX = x;
        }
    }

    private void verticalSwipe(float y, TextView picker) {
        if (!mSideSwipe && Math.abs(y - mPickerY) > mScreenHeight / 35) {
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
        if (checkLethal(picker)) {
            shakeLayout();
        } else {
            int lifeTotal = getPickerValue(picker);
            if (add)
                lifeTotal++;
            else
                lifeTotal--;
            picker.setText(Integer.toString(lifeTotal));
            addToHistory(getTotals());
            if (checkLethal(picker)) {
                shakeLayout();
            }
        }
    }

    private void shakeLayout() {
        AnimationSet animations = new AnimationSet(false);
        Animation shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake_main_layout);
        Animation unShakeAnimation = AnimationUtils.loadAnimation(this, R.anim.unshake_main_layout);
        animations.addAnimation(shakeAnimation);
        animations.addAnimation(unShakeAnimation);
        findViewById(R.id.left_update).startAnimation(animations);
    }

    private int getPickerValue(TextView picker) {
        return Integer.parseInt(picker.getText().toString());
    }

    private boolean checkLethal(TextView picker) {
        return ((picker.equals(mLifePickerOne) || picker.equals(mLifePickerTwo))
                && getPickerValue(picker) == LETHAL_LIFE) ||
                ((picker.equals(mPoisonPickerOne) || picker.equals(mPoisonPickerTwo))
                        && getPickerValue(picker) == LETHAL_POISON);
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
        String startingLife;
        if (findViewById(R.id.starting_life_picker) != null)
            startingLife =Integer.toString(
                    ((NumberPicker) findViewById(R.id.starting_life_picker)).getValue());
        else
        startingLife = STARTING_LIFE;
        mLifePickerOne.setText(startingLife);
        mLifePickerTwo.setText(startingLife);
        mPoisonPickerOne.setText(STARTING_POISON);
        mPoisonPickerTwo.setText(STARTING_POISON);
        mHistory.clear();
        mOptions.clear();
        instantiateArrayLists();
        addToHistory(getTotals());
        ((SettingsListAdapter)mSettingsDrawerList.getAdapter()).notifyDataSetChanged();
        ((ArrayAdapter<String>) mHistoryDrawerList.getAdapter()).notifyDataSetChanged();
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
        r.setDuration((long) 700);
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
