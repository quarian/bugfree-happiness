package com.minu.proto2020.app;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by mnur on 19/07/14.
 */
public class PickerAdapter extends BaseAdapter {

    private ArrayList<Picker> mPickers;
    private Context mContext;

    public PickerAdapter(Context context, ArrayList<Picker> pickers) {
        mPickers = pickers;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mPickers.size();
    }

    @Override
    public Picker getItem(int i) {
        return mPickers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.picker_layout, null);
        Picker picker = getItem(i) == null ? new Picker() : getItem(i);
        picker.setUpFromPickerView(view);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        picker.reset();
        return view;
    }
}
