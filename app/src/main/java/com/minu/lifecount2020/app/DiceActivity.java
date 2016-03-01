package com.minu.lifecount2020.app;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class DiceActivity extends Activity {

    private boolean mWhiteBackground;
    private ImageButton mCloseButton;
    private RelativeLayout mBackgroundLayout;
    private TextView mRedDiceTextView;
    private TextView mBlueDiceTextView;
    private Runnable mStepper;
    private Handler mHandler;
    private long mInterval;
    private int mSteps;
    private Random mGenerator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);

        bindElements();

        initElements();

        if (savedInstanceState == null)
            mWhiteBackground = getIntent().getBooleanExtra(Constants.BACKGROUND_WHITE, true);
        else
            mWhiteBackground = savedInstanceState.getBoolean(Constants.BACKGROUND_WHITE);

        setBackgroundColor();

        throwDice();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(Constants.BACKGROUND_WHITE, mWhiteBackground);
    }

    private void setBackgroundColor() {
        if (!mWhiteBackground)
            mBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.backgound_dark));
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();
    }


    private void bindElements() {
        mCloseButton = (ImageButton) findViewById(R.id.dice_close_button);
        mBackgroundLayout = (RelativeLayout) findViewById(R.id.dice_container_container);
        mRedDiceTextView = (TextView) findViewById(R.id.dice_red);
        mBlueDiceTextView = (TextView) findViewById(R.id.dice_blue);
    }

    private void initElements() {
        mGenerator = new Random(System.currentTimeMillis());
        mSteps = mGenerator.nextInt(10) + 16;
        mInterval = 400 - (long) Math.pow(mSteps, 2);

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mHandler = new Handler();
        mStepper = new Runnable() {
            @Override
            public void run() {
                try {
                    showStep();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (mSteps > 0)
                        mHandler.postDelayed(mStepper, mInterval);
                    else
                        mSteps = mGenerator.nextInt(10) + 16;
                }
            }
        };
    }

    private void showStep() {
        mSteps -= 1;
        if (mSteps > 0) {
            int redNumber = mGenerator.nextInt(20) + 1;
            int blueNumber = mGenerator.nextInt(20) + 1;
            mRedDiceTextView.setText(Integer.toString(redNumber));
            mBlueDiceTextView.setText(Integer.toString(blueNumber));
            mInterval = 400 - (long) Math.pow(mSteps, 2);
        }
    }

    private void throwDice() {
        mStepper.run();
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        if (getActionBar() != null)
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
