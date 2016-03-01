package com.minu.lifecount2020.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageButton;

public class DiceActivity extends Activity {

    private boolean mWhiteBackground;
    private ImageButton mCloseButton;
    private DrawerLayout mBackgroundLayout;

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
        mBackgroundLayout = (DrawerLayout) findViewById(R.id.dice_drawer_layout);
    }

    private void initElements() {
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
