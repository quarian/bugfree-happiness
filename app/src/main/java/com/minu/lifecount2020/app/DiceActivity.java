package com.minu.lifecount2020.app;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class DiceActivity extends SensorActivity {

    private ImageButton mCloseButton;
    private RelativeLayout mBackgroundLayout;
    private TextView mRedDiceTextView;
    private TextView mBlueDiceTextView;
    private Runnable mStepper;
    private Handler mHandler;
    private long mInterval;
    private int mSteps;
    private Random mGenerator;
    private boolean mRolling;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);

        bindElements();

        initElements();

        if (savedInstanceState == null) {
            mBackgroundColor =
                    (BackgroundColor) getIntent().getSerializableExtra(Constants.BACKGROUND_WHITE);
            throwDice();
        } else {
            mBackgroundColor =
                    (BackgroundColor) savedInstanceState.getSerializable(Constants.BACKGROUND_WHITE);
            mRedDiceTextView.setText(savedInstanceState.getString(Constants.RED_DICE));
            mBlueDiceTextView.setText(savedInstanceState.getString(Constants.BLUE_DICE));
        }

        setBackgroundColor();

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(Constants.BACKGROUND_WHITE, mBackgroundColor);
        savedInstanceState.putString(Constants.RED_DICE, mRedDiceTextView.getText().toString());
        savedInstanceState.putString(Constants.BLUE_DICE, mBlueDiceTextView.getText().toString());
    }

    private void setBackgroundColor() {
        if (BackgroundColor.GREY == mBackgroundColor)
            mBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.backgound_dark));
        else if (BackgroundColor.BLACK == mBackgroundColor)
            mBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.backgound_black));
        else
            mBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.background_light));
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
        instantiateSteps();
        updateInterval();
        mRolling = false;

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.activity_slide_in_top, R.anim.activity_slide_out_bottom);
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
                    if (mRolling && mSteps > 0)
                        mHandler.postDelayed(mStepper, mInterval);
                    else {
                        instantiateSteps();
                        mRolling = false;
                    }
                }
            }
        };
    }

    private void instantiateSteps() {
        mSteps = mGenerator.nextInt(Constants.STEP_VARIATION) + Constants.BASE_STEPS;
    }

    private void showStep() {
        mSteps -= 1;
        if (mSteps > 0) {
            int redNumber = mGenerator.nextInt(20) + 1;
            int blueNumber = mGenerator.nextInt(20) + 1;
            mRedDiceTextView.setText(Integer.toString(redNumber));
            mBlueDiceTextView.setText(Integer.toString(blueNumber));
            updateInterval();
        }
    }

    private void updateInterval() {
        mInterval = Constants.ROLL_BASE_TIME - (long) Math.pow(mSteps, 2);
    }

    private void throwDice() {
        if (!mRolling) {
            mRolling = true;
            mStepper.run();
        }
    }

    protected void checkShake(float x, float y, float z) {
        float acceleration = (float) Math.sqrt((double) x*x + y*y + z*z);
        if (Math.abs(acceleration - mGravity) > Constants.THROW_ACCELERATION)
            throwDice();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_slide_in_top, R.anim.activity_slide_out_bottom);
    }
}
