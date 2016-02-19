package com.minu.proto2020.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Miro on 19/2/2016.
 */
public class SettingsListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mData;
    private static LayoutInflater mLayoutInflater;

    public SettingsListAdapter(Context context, ArrayList<String> data) {
        mContext = context;
        mData = data;
        mLayoutInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null) {
            switch (position) {
                case 1:
                    vi = LayoutInflater.from(
                            new ContextThemeWrapper(mContext, R.style.NumberPickerTextColorStyle))
                            .inflate(R.layout.starting_life_option, null);
                    NumberPicker np = (NumberPicker) vi.findViewById(R.id.starting_life_picker);
                    setDividerColor(np, Color.argb(0, 0, 0, 0));
                    np.setEnabled(true);
                    np.setMinValue(0);
                    np.setMaxValue(100);
                    np.setValue(20);
                    break;
                default:
                    vi = mLayoutInflater.inflate(R.layout.settings_list_item, null);
                    break;

            }
        }
        ((TextView)vi.findViewById(R.id.settings_text)).setText(mData.get(position));
        return vi;
    }

    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}