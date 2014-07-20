package com.minu.proto2020.app;

import android.app.Activity;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import it.sephiroth.android.library.widget.HListView;

public class MainActivity extends Activity {

    private ArrayList<Picker> mPickers;

    private ImageButton mPoisonButton;

    private String[] mOptions;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private HListView mListView;
    private PickerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemUI();

        mPoisonButton = (ImageButton) findViewById(R.id.poison_button);

        mOptions = new String[1];
        mOptions[0] = "New duel";

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,  mOptions));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mListView = (HListView) findViewById(R.id.list_view);


        mPickers = new ArrayList<Picker>();
        mPickers.add(new Picker());
        mPickers.add(new Picker());

        mAdapter = new PickerAdapter(this, mPickers);
        mListView.setAdapter(mAdapter);

        mPoisonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("PoisonButton pushed");
                displayPoison();
            }
        });

        mDrawerLayout.setKeepScreenOn(true);
    }

    private void displayPoison() {
        for (Picker picker : mPickers)
            picker.displayPoison();
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
        for (Picker picker : mPickers)
            picker.reset();
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
