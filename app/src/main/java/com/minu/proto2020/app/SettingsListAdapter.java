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
import android.widget.ImageView;
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

    private boolean mPoisonShowing;
    private boolean mWhiteBackGround;
    private int mStartingLife;

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
                            .inflate(R.layout.starting_life_option, parent, false);
                    setupStartingLifeOption(vi);
                    break;
                case 2:
                    vi = mLayoutInflater.inflate(R.layout.poison_option, parent, false);
                    setupPoisonOption(vi);
                    break;
                case 3:
                    vi = mLayoutInflater.inflate(R.layout.change_background_option, parent, false);
                   setupBackgroundOption(vi);
                    break;
                default:
                    vi = mLayoutInflater.inflate(R.layout.settings_list_item, parent, false);
                    break;

            }
        }
        ((TextView)vi.findViewById(R.id.settings_text)).setText(mData.get(position));
        return vi;
    }

    private void setupStartingLifeOption(View vi) {
        NumberPicker np = (NumberPicker) vi.findViewById(R.id.starting_life_picker);
        setDividerColor(np, Color.argb(0, 0, 0, 0));
        np.setEnabled(true);
        np.setDisplayedValues(new String[]{"10", "20", "30", "40", "50", "60", "70", "80", "90", "100"});
        np.setMinValue(0);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setMaxValue(9);
        np.setValue(mStartingLife / 10 - 1);
    }

    private void setupPoisonOption(View vi) {
        TextView poisonToggle = (TextView) vi.findViewById(R.id.poison_toggle);
        if (mPoisonShowing) {
            poisonToggle.setText(mContext.getString(R.string.on));
            poisonToggle.setTextColor(
                    Color.parseColor(mContext.getString(R.string.color_blue)));
        } else {
            poisonToggle.setText(mContext.getString(R.string.off));
            poisonToggle.setTextColor(
                    Color.parseColor(mContext.getString(R.string.color_red)));
        }
        poisonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tw = ((TextView) v);
                if (!mPoisonShowing) {
                    tw.setText(mContext.getString(R.string.on));
                    tw.setTextColor(
                            Color.parseColor(mContext.getString(R.string.color_blue)));
                } else {
                    tw.setText(mContext.getString(R.string.off));
                    tw.setTextColor(
                            Color.parseColor(mContext.getString(R.string.color_red)));
                }
                mPoisonShowing = !mPoisonShowing;
                ((MainActivity) mContext).togglePoison();
            }
        });
    }

    private void setupBackgroundOption(View vi) {
        final ImageView imageView = (ImageView) vi.findViewById(R.id.background_preview);
        if (!mWhiteBackGround)
            imageView.setImageDrawable(vi.getContext().getResources()
                    .getDrawable(R.drawable.color_scheme_dark));
        else
            imageView.setImageDrawable(vi.getContext().getResources()
                    .getDrawable(R.drawable.color_scheme_light));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView iw = (ImageView) v;
                if (mWhiteBackGround)
                    iw.setImageDrawable(v.getContext().getResources()
                            .getDrawable(R.drawable.color_scheme_dark));
                else
                    iw.setImageDrawable(v.getContext().getResources()
                            .getDrawable(R.drawable.color_scheme_light));

                mWhiteBackGround = !mWhiteBackGround;
                ((MainActivity)mContext).toggleBackground();
            }
        });
    }

    public boolean getWhiteBackground() {
        return mWhiteBackGround;
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

    public void setSettings(boolean poisonShowing, int startingLife, boolean whiteBackground) {
        System.out.println("Settings settings " + poisonShowing);
        mPoisonShowing = poisonShowing;
        mStartingLife = startingLife;
        mWhiteBackGround = whiteBackground;
    }
}
