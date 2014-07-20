package com.minu.proto2020.app;

import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mnur on 19/07/14.
 */
public class Picker {

    private LinearLayout mPickerLayout;
    private LinearLayout mLifeLinearLayout;
    private LinearLayout mPoisonLinearLayout;
    private TextView mLifeTextView;
    private TextView mPoisonTextView;
    private float mPickerY;
    private boolean mSpun;
    private boolean mPoisonShowing;


    private void setTextViewOnTouchListener(final TextView picker, final boolean poison) {
        picker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                float y = motionEvent.getY();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mPickerY = motionEvent.getY();
                        System.out.println("On picker touch, y: " + motionEvent.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        System.out.println("On picker touch movement, y: " + motionEvent.getY());
                        if (Math.abs(y - mPickerY) > 50.0) {
                            mSpun = true;
                            System.out.println("Changing picker value");
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
                        System.out.println("Default picker touchevent");
                        mPickerY = motionEvent.getY();
                        break;
                }
                return true;
            }
        });
    }

    private void setLinearLayoutTouchListener(final LinearLayout layout, final TextView picker, final boolean poison) {
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                float y = motionEvent.getY();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        int[] coordinates = {0, 0};
                        if (y > (coordinates[1] + layout.getHeight()) / 2)
                            changePickerValue(picker, false);
                        else
                            changePickerValue(picker, true);
                        System.out.println("On layout touch, y: " + motionEvent.getY());
                        break;
                    default:
                        System.out.println("Default layout touchevent");
                        break;
                }
                return true;
            }
        });
    }

    public void setUpFromPickerView(View v) {
        setPickerLayout((LinearLayout) v.findViewById(R.id.picker_layout));
        setLifeLinearLayout((LinearLayout) v.findViewById(R.id.life_picker_layout));
        setPoisonLinearLayout((LinearLayout) v.findViewById(R.id.poison_picker_layout));
        setLifeTextView((TextView) v.findViewById(R.id.life_picker));
        setPoisonTextView((TextView) v.findViewById(R.id.poison_picker));

        setTextViewOnTouchListener(mLifeTextView, false);
        setTextViewOnTouchListener(mPoisonTextView, true);
        setLinearLayoutTouchListener(mLifeLinearLayout, mLifeTextView, false);
        setLinearLayoutTouchListener(mPoisonLinearLayout, mPoisonTextView, true);
    }

    private void changePickerValue(TextView picker, boolean add) {
        int lifeTotal = Integer.parseInt(picker.getText().toString());
        if (add)
            lifeTotal++;
        else
            lifeTotal--;
        picker.setText(Integer.toString(lifeTotal));
    }


    public void displayPoison() {
        if (mPoisonShowing)
            mPoisonLinearLayout.setVisibility(View.GONE);
        else
            mPoisonLinearLayout.setVisibility(View.VISIBLE);
        mPoisonShowing = !mPoisonShowing;
    }

    public void reset() {
        mLifeTextView.setText("20");
        mPoisonTextView.setText("0");
    }

    public void setPickerLayout(LinearLayout mPickerLayout) {
        this.mPickerLayout = mPickerLayout;
    }

    public void setLifeTextView(TextView mLifeTextView) {
        this.mLifeTextView = mLifeTextView;
    }

    public void setPoisonTextView(TextView mPoisonTextView) {
        this.mPoisonTextView = mPoisonTextView;
    }

    public void setPoisonLinearLayout(LinearLayout mPoisonLinearLayout) {
        this.mPoisonLinearLayout = mPoisonLinearLayout;
        this.mPoisonLinearLayout.setVisibility(View.GONE);
    }

    public void setLifeLinearLayout(LinearLayout mLifeLinearLayout) {
        this.mLifeLinearLayout = mLifeLinearLayout;
    }

    public LinearLayout getPickerLayout() {
        return mPickerLayout;
    }
}
