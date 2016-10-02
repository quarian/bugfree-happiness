package com.minu.lifecount2020.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends SensorActivity {

    private LinearLayout mLifeLinearLayoutOne;
    private LinearLayout mLifeLinearLayoutTwo;
    private LinearLayout mPoisonLinearLayoutOne;
    private LinearLayout mPoisonLinearLayoutTwo;
    private LinearLayout mEnergyLinerLayoutOne;
    private LinearLayout mEnergyLinerLayoutTwo;
    private TextView mLifePickerOne;
    private TextView mLifePickerTwo;
    private TextView mPoisonPickerOne;
    private TextView mPoisonPickerTwo;
    private TextView mEnergyPickerOne;
    private TextView mEnergyPickerTwo;

    private ImageButton mSettingsButton;
    private ImageButton mHistoryButton;

    private String mWhiteBackgroundColor;
    private String mDarkGreyBackgroundColor;
    private String mBlackBackgroundColor;

    private LinearLayout mWrapper;

    private TextView mLeftUpdateTextView;
    private TextView mRighyUpdateTextView;

    private boolean mPoisonShowing;
    private boolean mEnergyShowing;

    private ArrayList<String> mOptions;

    private DrawerLayout mSettingsDrawerLayout;
    private ListView mSettingsDrawerList;
    private RelativeLayout mSettingsDrawer;

    private ListView mHistoryDrawerList;

    private int mScreenHeight;
    private int mScreenWidth;

    private boolean mSpun;
    private boolean mSideSwipe;

    private float mPickerY;
    private float mPickerX;
    private float mPickerLastX;
    private boolean mUpdating;

    private int mStartingLife;

    private ArrayList<String> mHistory;

    private String mShowPoison;
    private String mHidePoison;

    private String mPullToRefresh;
    private String mReleaseToRefresh;

    private String mPoisonOption = mShowPoison;
    private int mPoisonOptionIndex;

    private float mCurrentRotation;

    private CountDownTimer mRoundTimer;
    private TextView mRoundTimerTextView;
    private boolean mTimerShowing;
    private boolean mTimerRunning;
    private long mSavedRoundTime;
    private int mRoundTime;
    private String mEnergyOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isTaskRoot()) {
            // Android launch bug
            finish();
            return;
        }

        hideSystemUI();

        bindElements();

        initElements();

        if (savedInstanceState != null) {
            //System.out.println("Restoring state");
            setLifeTotals(savedInstanceState.getString(Constants.PICKER_ONE_LIFE),
                    savedInstanceState.getString(Constants.PICKER_ONE_POISON),
                    savedInstanceState.getString(Constants.PICKER_TWO_LIFE),
                    savedInstanceState.getString(Constants.PICKER_TWO_POISON),
                    savedInstanceState.getString(Constants.PICKER_ONE_ENERGY),
                    savedInstanceState.getString(Constants.PICKER_TWO_ENERGY));

            mHistory = savedInstanceState.getStringArrayList(Constants.HISTORY);
            ((HistoryListAdapter) mHistoryDrawerList.getAdapter()).notifyDataSetChanged();
            mPoisonShowing = savedInstanceState.getBoolean(Constants.POISON);
            mEnergyShowing = savedInstanceState.getBoolean(Constants.ENERGY);
            mBackgroundColor =
                    (BackgroundColor) savedInstanceState.getSerializable(Constants.BACKGROUND_WHITE);
            mStartingLife = savedInstanceState.getInt(Constants.STARTING_LIFE);

        } else {
            restoreFromPreferences();
        }
    }

    private void restoreSettings() {
        mPoisonOption = mShowPoison;
        if (mPoisonShowing) {
            mPoisonShowing = !mPoisonShowing;
            displayPoison();
        }
        if (mEnergyShowing) {
            mEnergyShowing = !mEnergyShowing;
            displayEnergy();
        }

        if (mTimerShowing) {
            mTimerShowing = !mTimerShowing;
            toggleTimer();
        }

        if (BackgroundColor.GREY == mBackgroundColor)
            mSettingsDrawerLayout.setBackgroundColor(Color.parseColor(mDarkGreyBackgroundColor));
        else if (BackgroundColor.BLACK == mBackgroundColor)
            mSettingsDrawerLayout.setBackgroundColor(Color.parseColor(mBlackBackgroundColor));
        //System.out.println(mPoisonShowing + " " + mStartingLife + " " + mWhiteBackground);
        ((SettingsListAdapter)mSettingsDrawerList.getAdapter())
                .setSettings(mPoisonShowing, mEnergyShowing, mStartingLife, mBackgroundColor, mRoundTime, mTimerShowing);

    }

    private void restoreFromPreferences() {
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        String lifeOne = settings.getString(Constants.PICKER_ONE_LIFE, Constants.STARTING_LIFE);
        String lifeTwo = settings.getString(Constants.PICKER_TWO_LIFE, Constants.STARTING_LIFE);
        String poisonOne = settings.getString(Constants.PICKER_ONE_POISON, Constants.STARTING_POISON);
        String poisonTwo = settings.getString(Constants.PICKER_TWO_POISON, Constants.STARTING_POISON);
        String energyOne = settings.getString(Constants.PICKER_ONE_ENERGY, Constants.STARTING_ENERGY);
        String energyTwo = settings.getString(Constants.PICKER_TWO_ENERGY, Constants.STARTING_ENERGY);
        mBackgroundColor = BackgroundColor.values()[settings.getInt(Constants.BACKGROUND_WHITE, 0)];
        mLifePickerOne.setText(lifeOne);
        mLifePickerTwo.setText(lifeTwo);
        mPoisonPickerOne.setText(poisonOne);
        mPoisonPickerTwo.setText(poisonTwo);
        mEnergyPickerOne.setText(energyOne);
        mEnergyPickerTwo.setText(energyTwo);
        mPoisonShowing = settings.getBoolean(Constants.POISON, false);
        mEnergyShowing = settings.getBoolean(Constants.ENERGY, false);
        mStartingLife = settings.getInt(Constants.STARTING_LIFE,
                Integer.parseInt(Constants.STARTING_LIFE));
        mRoundTime = settings.getInt(Constants.ROUND_TIME, 50);
        mSavedRoundTime =
                settings.getLong(Constants.REMAINING_ROUND_TIME, Constants.BASE_ROUND_TIME_IN_MS);
        mTimerShowing = settings.getBoolean(Constants.ROUND_TIMER_SHOWING, false);
        resetTimer(true);
        String historyAsString = settings.getString(Constants.HISTORY, null);
        List<String> tempList;
        if (historyAsString != null)
            tempList = Arrays.asList(
                historyAsString.substring(1, historyAsString.length() - 1).split((", ")));
        else
            tempList = new ArrayList<String>();
        mHistory = new ArrayList<String>(tempList);
        restoreSettings();
    }


    @Override
    protected void onStop(){
        super.onStop();

        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.PICKER_ONE_LIFE, mLifePickerOne.getText().toString());
        editor.putString(Constants.PICKER_TWO_LIFE, mLifePickerTwo.getText().toString());
        editor.putString(Constants.PICKER_ONE_POISON, mPoisonPickerOne.getText().toString());
        editor.putString(Constants.PICKER_TWO_POISON, mPoisonPickerTwo.getText().toString());
        editor.putString(Constants.PICKER_ONE_ENERGY, mEnergyPickerOne.getText().toString());
        editor.putString(Constants.PICKER_TWO_ENERGY, mEnergyPickerTwo.getText().toString());
        editor.putInt(Constants.BACKGROUND_WHITE, mBackgroundColor.ordinal());
        editor.putBoolean(Constants.POISON, mPoisonShowing);
        editor.putBoolean(Constants.ENERGY, mEnergyShowing);
        editor.putInt(Constants.STARTING_LIFE, mStartingLife);
        editor.putString(Constants.HISTORY, mHistory.toString());
        editor.putInt(Constants.ROUND_TIME, mRoundTime);
        editor.putLong(Constants.REMAINING_ROUND_TIME, mSavedRoundTime);
        editor.putBoolean(Constants.ROUND_TIMER_SHOWING, mTimerShowing);
        editor.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //System.out.println("Saving state");
        savedInstanceState.putString(Constants.PICKER_ONE_LIFE, mLifePickerOne.getText().toString());
        savedInstanceState.putString(Constants.PICKER_ONE_POISON, mPoisonPickerOne.getText().toString());
        savedInstanceState.putString(Constants.PICKER_TWO_LIFE, mLifePickerTwo.getText().toString());
        savedInstanceState.putString(Constants.PICKER_TWO_POISON, mPoisonPickerTwo.getText().toString());
        savedInstanceState.putString(Constants.PICKER_ONE_ENERGY, mEnergyPickerOne.getText().toString());
        savedInstanceState.putString(Constants.PICKER_TWO_ENERGY, mEnergyPickerTwo.getText().toString());

        savedInstanceState.putSerializable(Constants.BACKGROUND_WHITE, mBackgroundColor);
        int startingLife;
        if (findViewById(R.id.starting_life_picker) == null)
            startingLife = Integer.parseInt(Constants.STARTING_LIFE);
        else {
            int index = ((NumberPicker) findViewById(R.id.starting_life_picker)).getValue();
            String[] values = ((NumberPicker) findViewById(R.id.starting_life_picker))
                    .getDisplayedValues();
            //System.out.println(values);
            startingLife = Integer.parseInt(values[index]);
        }
        savedInstanceState.putInt(Constants.STARTING_LIFE, startingLife);
        savedInstanceState.putBoolean(Constants.POISON, mPoisonShowing);
        savedInstanceState.putBoolean(Constants.ENERGY, mEnergyShowing);

        savedInstanceState.putStringArrayList(Constants.HISTORY, mHistory);

        savedInstanceState.putInt(Constants.ROUND_TIME, mRoundTime);
        savedInstanceState.putLong(Constants.REMAINING_ROUND_TIME, mSavedRoundTime);
        savedInstanceState.putBoolean(Constants.ROUND_TIMER_SHOWING, mTimerShowing);

        //System.out.println(savedInstanceState);

        super.onSaveInstanceState(savedInstanceState);
    }

    private void bindElements() {
        mLifeLinearLayoutOne = (LinearLayout) findViewById(R.id.first_life_picker_layout);
        mLifeLinearLayoutTwo = (LinearLayout) findViewById(R.id.second_life_picker_layout);

        mPoisonLinearLayoutOne = (LinearLayout) findViewById(R.id.first_poison_picker_layout);
        mPoisonLinearLayoutTwo = (LinearLayout) findViewById(R.id.second_poison_picker_layout);

        mEnergyLinerLayoutOne = (LinearLayout) findViewById(R.id.first_energy_picker_layout);
        mEnergyLinerLayoutTwo = (LinearLayout) findViewById(R.id.second_energy_picker_layout);

        mLifePickerOne = (TextView) findViewById(R.id.life_picker_1);
        mLifePickerTwo = (TextView) findViewById(R.id.life_picker_2);

        mPoisonPickerOne = (TextView) findViewById(R.id.poison_picker_1);
        mPoisonPickerTwo = (TextView) findViewById(R.id.poison_picker_2);

        mEnergyPickerOne = (TextView) findViewById(R.id.energy_picker_1);
        mEnergyPickerTwo = (TextView) findViewById(R.id.energy_picker_2);

        setInitialColors();

        mWrapper = (LinearLayout) findViewById(R.id.wrapper);

        mLeftUpdateTextView = (TextView) findViewById(R.id.update);
        mRighyUpdateTextView = (TextView) findViewById(R.id.update_2);

        mSettingsButton = (ImageButton) findViewById(R.id.settings_button);
        mHistoryButton = (ImageButton) findViewById(R.id.history_button);

        mOptions = new ArrayList<String>();
        mHistory = new ArrayList<String>();

        mSettingsDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mSettingsDrawer = (RelativeLayout) findViewById(R.id.settings_drawer);

        mSettingsDrawerList = (ListView) findViewById(R.id.left_drawer);
        mHistoryDrawerList = (ListView) findViewById(R.id.right_drawer);
    }

    private void instantiateArrayLists() {
        mOptions.add(getString(R.string.new_duel));
        mOptions.add(getString(R.string.starting_life_total));
        mOptions.add(mPoisonOption);
        mOptions.add(getString(R.string.energy));
        mOptions.add(getString(R.string.color_scheme));
        mOptions.add(getString(R.string.throw_dice));
        mOptions.add(getString(R.string.round_timer));
    }

    private void setInitialColors() {
        int red = Color.parseColor(getString(R.string.color_red));
        int blue = Color.parseColor(getString(R.string.color_blue));
        mLifePickerOne.setTextColor(red);
        mPoisonPickerOne.setTextColor(red);
        mEnergyPickerOne.setTextColor(red);
        mLifePickerTwo.setTextColor(blue);
        mPoisonPickerTwo.setTextColor(blue);
        mEnergyPickerTwo.setTextColor(blue);
        Drawable arrowLeft = getResources().getDrawable(R.drawable.left_arrow);
        Drawable arrowRight = getResources().getDrawable(R.drawable.right_arrow);
        Drawable energyIconLeft = getResources().getDrawable(R.drawable.energy_icon_left);
        Drawable energyIconRight = getResources().getDrawable(R.drawable.energy_icon_right);
        if (arrowLeft != null)
            arrowLeft.setColorFilter(Color.parseColor(getString(R.string.color_text)),
                    PorterDuff.Mode.SRC_ATOP);
        if (arrowRight != null)
            arrowRight.setColorFilter(Color.parseColor(getString(R.string.color_text)),
                    PorterDuff.Mode.SRC_ATOP);
        if (energyIconLeft != null)
            energyIconLeft.setColorFilter(red,
                    PorterDuff.Mode.SRC_ATOP);
        if (energyIconRight != null)
            energyIconRight.setColorFilter(blue,
                    PorterDuff.Mode.SRC_ATOP);

        ((ImageView)findViewById(R.id.update_arrow_left)).setImageDrawable(arrowLeft);
        ((ImageView)findViewById(R.id.update_arrow_right)).setImageDrawable(arrowRight);

        ((ImageView)findViewById(R.id.energy_icon_one)).setImageDrawable(energyIconLeft);
        ((ImageView)findViewById(R.id.energy_icon_two)).setImageDrawable(energyIconRight);
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
        ((HistoryListAdapter)mHistoryDrawerList.getAdapter()).clear();
        ((HistoryListAdapter)mHistoryDrawerList.getAdapter()).addAll(mHistory);
    }

    private void showHistory() {
        ((HistoryListAdapter) mHistoryDrawerList.getAdapter()).notifyDataSetChanged();
    }

    private long parseTimeStamp(String historyEntry) {
        String timeString = historyEntry.split(" ")[6];
        return Long.parseLong(timeString);
    }

    private boolean isHistoryEntryRead(String historyEntry) {
        //System.out.println(mHistory);
        String read = historyEntry.split(" ")[6];
        return read.compareTo(Constants.READ) == 0;
    }

    private String markedHistoryEntryRead(String historyEntry) {
        String[] split = historyEntry.split(" ");
        split[6] = Constants.READ;
        return split[0] + " " + split[1] + " " + split[2] + " " + split[3] + " " + split[4] + " " + split[5] + " " + split[6];
    }

    public void setmStartingLife(int startingLife) {
        mStartingLife = startingLife;
    }

    private void initElements() {

        mSettingsDrawerList.setAdapter(new SettingsListAdapter(this, mOptions));

        mHistoryDrawerList.setAdapter(new HistoryListAdapter(this, mHistory));

        mSettingsDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        setLayoutTouchListener(mLifeLinearLayoutOne, mLifePickerOne);
        setLayoutTouchListener(mLifeLinearLayoutTwo, mLifePickerTwo);
        setLayoutTouchListener(mPoisonLinearLayoutOne, mPoisonPickerOne);
        setLayoutTouchListener(mPoisonLinearLayoutTwo, mPoisonPickerTwo);
        setLayoutTouchListener(mEnergyLinerLayoutOne, mEnergyPickerOne);
        setLayoutTouchListener(mEnergyLinerLayoutTwo, mEnergyPickerTwo);
        setTextViewOnTouchListener(mLifePickerOne, false);
        setTextViewOnTouchListener(mLifePickerTwo, false);
        setTextViewOnTouchListener(mPoisonPickerOne, true);
        setTextViewOnTouchListener(mPoisonPickerTwo, true);
        setTextViewOnTouchListener(mEnergyPickerOne, false);
        setTextViewOnTouchListener(mEnergyPickerTwo, false);

        mPoisonLinearLayoutOne.setVisibility(View.GONE);
        mPoisonLinearLayoutTwo.setVisibility(View.GONE);

        mEnergyLinerLayoutOne.setVisibility(View.GONE);
        mEnergyLinerLayoutTwo.setVisibility(View.GONE);

        instantiateArrayLists();

        mStartingLife = 20;
        mPoisonOptionIndex = 2;

        mRoundTime = 50;

        mCurrentRotation = 0.0f;
        mBackgroundColor = BackgroundColor.WHITE;

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
                    //System.out.println("Should show history");
                    //System.out.println(mHistory);
                    collapseHistory();
                    showHistory();
                    mHistoryDrawerList.post(new Runnable() {
                        @Override
                        public void run() {
                            mHistoryDrawerList.
                                    setSelection(mHistoryDrawerList.getAdapter().getCount());
                        }
                    });
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

        mShowPoison = getString(R.string.poison);
        mHidePoison = getString(R.string.poison);
        mEnergyOption = getString(R.string.energy);

        mPullToRefresh = getString(R.string.pull_to_restart);
        mReleaseToRefresh = getString(R.string.pull_to_cancel);

        mWhiteBackgroundColor = getString(R.string.color_background_white);
        mDarkGreyBackgroundColor = getString(R.string.color_background_black);
        mBlackBackgroundColor = getString(R.string.color_black);

        mPoisonOption = mShowPoison;

        Display display = getWindowManager().getDefaultDisplay();
        mScreenHeight = display.getWidth();
        mScreenWidth = display.getHeight();

        ((TextView) findViewById(R.id.twitter_link))
                .setMovementMethod(LinkMovementMethod.getInstance());

        mSettingsDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
        mSettingsDrawerLayout.setKeepScreenOn(true);

       instantiateRoundTimer();

    }

    private void instantiateRoundTimer() {
        mSavedRoundTime = Constants.BASE_ROUND_TIME_IN_MS;
        mRoundTimer = getNewTimer(Constants.BASE_ROUND_TIME_IN_MS);

        mRoundTimerTextView = (TextView) findViewById(R.id.round_timer);

        mRoundTimerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning)
                    mRoundTimer.cancel();
                else {
                    mRoundTimer = getNewTimer(mSavedRoundTime);
                    mRoundTimer.start();
                }
                mTimerRunning = !mTimerRunning;
            }
        });

        setTimerAnimations(true);

        mRoundTimerTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                resetTimer(false);
                return true;
            }
        });

        //mRoundTimer.start();
        //mTimerRunning = true;
    }

    private void setTimerAnimations(boolean on) {
        if (on) {
            mRoundTimerTextView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            scaleTextView((TextView) view, true);
                            break;
                        case MotionEvent.ACTION_UP:
                            scaleTextView((TextView) view, false);
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        } else {
            mRoundTimerTextView.clearAnimation();
        }
    }

    private void resetTimer(boolean restart) {
        mRoundTimer.cancel();
        mRoundTimer = getNewTimer(Constants.BASE_ROUND_TIME_IN_MS);
        if (!restart)
            mSavedRoundTime = minutesToMilliseconds(mRoundTime);
        mTimerRunning = false;
        mRoundTimerTextView.setText(getMinutes(mSavedRoundTime));
    }

    private long minutesToMilliseconds(int minutes) {
        return minutes * 60 * 1000;
    }

    private CountDownTimer getNewTimer(long startingTime) {
        return new CountDownTimer(startingTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mRoundTimerTextView.setText(getMinutes(millisUntilFinished));
                mSavedRoundTime = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                mRoundTimerTextView.setText("TIME");
            }
        };
    }

    private String getMinutes(long millisUntilFinished) {
        long remainingSeconds = millisUntilFinished / 1000;
        long minutes = remainingSeconds / 60;
        long seconds = remainingSeconds % 60;
        String result;
        if (seconds < 10)
            result = Long.toString(minutes) + ":0" + Long.toString(seconds);
        else
            result = Long.toString(minutes) + ":" + Long.toString(seconds);
        return result;
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

    private void displayEnergy() {
        if (mEnergyShowing) {
            mEnergyLinerLayoutOne.setVisibility(View.GONE);
            mEnergyLinerLayoutTwo.setVisibility(View.GONE);
            mEnergyShowing = false;
        } else {
            mEnergyLinerLayoutOne.setVisibility(View.VISIBLE);
            mEnergyLinerLayoutTwo.setVisibility(View.VISIBLE);
            mEnergyShowing = true;
        }

        ((SettingsListAdapter)mSettingsDrawerList.getAdapter()).notifyDataSetChanged();

    }


    protected void checkShake(float x, float y, float z) {
        float acceleration = (float) Math.sqrt((double) x*x + y*y + z*z);
        if (Math.abs(acceleration - mGravity) > Constants.THROW_ACCELERATION)
            startDiceThrowActivity(mBackgroundColor);
    }

    public void setTime(int i) {
        mRoundTime = i;
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
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    mSettingsDrawerLayout.closeDrawer(mSettingsDrawer);
                    startDiceThrowActivity(mBackgroundColor);
                    break;
                case 6:
                    break;
                default:
                    mSettingsDrawerLayout.closeDrawer(mSettingsDrawer);
                    break;
            }
        }
    }

    private void startDiceThrowActivity(BackgroundColor backgroundColor) {
        Intent i = new Intent(this, DiceActivity.class);
        i.putExtra(Constants.BACKGROUND_WHITE, backgroundColor);
        startActivity(i);
        overridePendingTransition(R.anim.activity_slide_in_bottom, R.anim.activity_slide_out_top);
    }

    public void toggleBackground() {
        BackgroundColor targetBackground =
                ((SettingsListAdapter)
                        mSettingsDrawerList.getAdapter()).getBackground();
        if (BackgroundColor.GREY == targetBackground) {
            mSettingsDrawerLayout.setBackgroundColor(Color.parseColor(mDarkGreyBackgroundColor));
            mBackgroundColor = BackgroundColor.GREY;
        } else if (BackgroundColor.BLACK == targetBackground) {
            mSettingsDrawerLayout.setBackgroundColor(Color.parseColor(mBlackBackgroundColor));
            mBackgroundColor = BackgroundColor.BLACK;
        } else {
            mSettingsDrawerLayout.setBackgroundColor(Color.parseColor(mWhiteBackgroundColor));
            mBackgroundColor = BackgroundColor.WHITE;
        }
    }

    public void togglePoison() {
        String showPoison =
                ((TextView) findViewById(R.id.poison_toggle)).getText().toString();
        if (showPoison.equals("on") && !mPoisonShowing ||
                showPoison.equals("off") && mPoisonShowing) {
            displayPoison();
        }
    }

    public void toggleEnergy() {
        String showEnergy =
                ((TextView) findViewById(R.id.energy_toggle)).getText().toString();
        if (showEnergy.equals("on") && !mEnergyShowing ||
                showEnergy.equals("off") && mEnergyShowing) {
            displayEnergy();
        }
    }

    public void toggleTimer() {
        if (mTimerShowing) {
            setTimerAnimations(false);
            mRoundTimerTextView.setVisibility(View.GONE);
            mRoundTimer.cancel();
        } else {
            setTimerAnimations(true);
            mRoundTimerTextView.setVisibility(View.VISIBLE);
            if (mRoundTimer == null)
                mRoundTimer = getNewTimer(mSavedRoundTime);
            mRoundTimerTextView.setText(getMinutes(mSavedRoundTime));
        }
        mTimerShowing = !mTimerShowing;
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
                //System.out.println("Default picker touchevent");
                break;
        }
    }

    private void handlePickerTouchRelease(TextView picker, boolean poison) {
        if (!mSpun && !mSideSwipe) {
            changePickerValue(picker, poison);
            scaleTextView(picker, Constants.SCALE_DOWN);
        }
        //System.out.println("Action up");
        if (mSpun || mSideSwipe)
            scaleTextView(picker, Constants.SCALE_DOWN);
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
                //System.out.println("Default layout touchevent");
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
            scaleTextView(picker, Constants.SCALE_DOWN);
        mWrapper.scrollTo(0, 0);
        mUpdating = false;
        mSideSwipe = false;
    }

    private void recordTouchStart(MotionEvent motionEvent, TextView picker) {
        mPickerY = motionEvent.getY();
        mPickerX = motionEvent.getX();
        mPickerLastX = motionEvent.getX();
        scaleTextView(picker, Constants.SCALE_UP);
    }

    private void sideSwipe(float x) {
        if (!mSpun && Math.abs(x - mPickerLastX) > mScreenWidth / 40)
            mSideSwipe = true;
        if (mSideSwipe) {
            //System.out.println((int) (x - mPickerLastX));
            if (!mUpdating)
                mWrapper.scrollBy((int) -(x - mPickerLastX) / 2, 0);
            //System.out.println("Side swiping");
            if (Math.abs(x - mPickerX) > mScreenWidth / 3.5f) {
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
            //System.out.println("Changing picker value");
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
            scaleTextView(picker, Constants.SCALE_DOWN);
        } else {
            int[] coordinates = {0, 0};
            //System.out.println("Layout touch, coordinates and y: " + coordinates + " " + y);
            scaleTextView(picker, Constants.SCALE_DOWN);
            if (y > (coordinates[1] + layout.getHeight()) / 2)
                changePickerValue(picker, false);
            else
                changePickerValue(picker, true);
        }
    }

    private void changePickerValue(TextView picker, boolean add) {
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
                && getPickerValue(picker) == Constants.LETHAL_LIFE) ||
                ((picker.equals(mPoisonPickerOne) || picker.equals(mPoisonPickerTwo))
                        && getPickerValue(picker) == Constants.LETHAL_POISON);
    }

    @Override
    public void onResume() {
        super.onResume();
        //System.out.println("RESUMING");
        mSettingsDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
        mOptions.set(mPoisonOptionIndex, mPoisonOption);
        restoreSettings();
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
        if (findViewById(R.id.starting_life_picker) != null) {
            int index = ((NumberPicker) findViewById(R.id.starting_life_picker)).getValue();
            String[] values = ((NumberPicker) findViewById(R.id.starting_life_picker))
                    .getDisplayedValues();
            startingLife = values[index];
        }
        else
            startingLife = Constants.STARTING_LIFE;
        mLifePickerOne.setText(startingLife);
        mLifePickerTwo.setText(startingLife);
        mPoisonPickerOne.setText(Constants.STARTING_POISON);
        mPoisonPickerTwo.setText(Constants.STARTING_POISON);
        mEnergyPickerOne.setText(Constants.STARTING_ENERGY);
        mEnergyPickerTwo.setText(Constants.STARTING_ENERGY);
        mHistory.clear();
        ((HistoryListAdapter) mHistoryDrawerList.getAdapter()).clear();
        mOptions.clear();
        instantiateArrayLists();
        addToHistory(getTotals());
        ((SettingsListAdapter)mSettingsDrawerList.getAdapter()).notifyDataSetChanged();
        ((HistoryListAdapter) mHistoryDrawerList.getAdapter()).notifyDataSetChanged();
    }

    public void addToHistory(String[] totals) {
        String timeStamp = Long.toString(System.currentTimeMillis());
        //System.out.println("Adding to history " + totals[0] + " " + totals[1] + " "
        //        + totals[2] + " " + totals[3] + " " + timeStamp);
        mHistory.add(totals[0] + " " + totals[1] + " "
                + totals[2] + " " + totals[3] + " " + totals[4] + " " + totals[5] + " " + timeStamp);
    }

    public String[] getTotals() {
        return new String[]{mLifePickerOne.getText().toString(), mLifePickerTwo.getText().toString(),
                           mPoisonPickerOne.getText().toString(), mPoisonPickerTwo.getText().toString(),
                           mEnergyPickerOne.getText().toString(), mEnergyPickerTwo.getText().toString()};
    }


    private void setLifeTotals(String lifePickerOne, String poisonPickerOne,
                               String lifePickerTwo, String poisonPickerTwo,
                               String energyPickerOne, String energyPickerTwo) {
        mLifePickerOne.setText(lifePickerOne);
        mLifePickerTwo.setText(lifePickerTwo);
        mPoisonPickerOne.setText(poisonPickerOne);
        mPoisonPickerTwo.setText(poisonPickerTwo);
        mEnergyPickerOne.setText(energyPickerOne);
        mEnergyPickerTwo.setText(energyPickerTwo);
    }

    private void spinResetArrows() {
        ImageView leftArrow = (ImageView) findViewById(R.id.update_arrow_left);
        ImageView rightArrow = (ImageView) findViewById(R.id.update_arrow_right);
        RotateAnimation r = new RotateAnimation(mCurrentRotation, mCurrentRotation + 180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setDuration((long) 300);
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
}
