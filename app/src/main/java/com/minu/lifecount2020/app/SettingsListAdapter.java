package com.minu.lifecount2020.app;

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

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Miro on 19/2/2016.
 */
public class SettingsListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mData;
    private static LayoutInflater mLayoutInflater;

    private boolean mPoisonShowing;
    private boolean mEnergyShowing;
    private BackgroundColor mBackgroundColor;
    private int mStartingLife;
    private String[] mStartingLifeValues;
    private int mTime;
    private boolean mTimerShowing;
    private String[] mTimerOptions;

    public SettingsListAdapter(Context context, ArrayList<String> data) {
        mContext = context;
        mData = data;
        mLayoutInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mStartingLifeValues =
                new String[]{"10", "20", "25", "30", "40", "50", "60", "70", "80", "90", "100"};
        mTimerOptions =
                new String[]{"30", "40", "50", "60", "90", "120"};
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
        switch (position) {
            case 0:
                vi = mLayoutInflater.inflate(R.layout.settings_list_item, parent, false);
                break;
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
                vi = mLayoutInflater.inflate(R.layout.energy_option, parent, false);
                setupEnergyOption(vi);
                break;
            case 4:
                vi = mLayoutInflater.inflate(R.layout.change_background_option, parent, false);
                setupBackgroundOption(vi);
                break;
            case 5:
                vi = mLayoutInflater.inflate(R.layout.settings_list_item, parent, false);
                break;
            case 6:
                vi = LayoutInflater.from(
                        new ContextThemeWrapper(mContext, R.style.NumberPickerTextColorStyle))
                        .inflate(R.layout.timer_option, parent, false);
                setupTimerOption(vi);
                break;
            default:
                vi = mLayoutInflater.inflate(R.layout.settings_list_item, parent, false);
                break;
        }
        ((TextView)vi.findViewById(R.id.settings_text)).setText(mData.get(position));
        return vi;
    }

    private void setupTimerOption(View vi) {
        NumberPicker np = (NumberPicker) vi.findViewById(R.id.round_time_picker);
        setDividerColor(np, Color.argb(0, 0, 0, 0));
        np.setEnabled(true);
        np.setDisplayedValues(mTimerOptions);
        np.setMinValue(0);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setMaxValue(mTimerOptions.length - 1);
        TextView poisonToggle = (TextView) vi.findViewById(R.id.timer_toggle);
        setUpToggle(poisonToggle, mTimerShowing);
        setToggle(poisonToggle, new toggleTimerCommand());
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                ((MainActivity) mContext).
                        setTime(Integer.parseInt(mTimerOptions[newVal]));
                mTime = Integer.parseInt(mTimerOptions[newVal]);
            }
        });
        np.setValue(getTimeSpinnerValue());
    }

    private int getTimeSpinnerValue() {
        int result;
        switch (mTime) {
            case 30:
                result = 0;
                break;
            case 40:
                result = 1;
                break;
            case 50:
                result = 2;
                break;
            case 60:
                result = 3;
                break;
            case 90:
                result = 4;
                break;
            case 120:
                result = 5;
                break;
            default:
                result = 2;
                break;
        }
        return result;
    }

    private void setupStartingLifeOption(View vi) {
        NumberPicker np = (NumberPicker) vi.findViewById(R.id.starting_life_picker);
        setDividerColor(np, Color.argb(0, 0, 0, 0));
        np.setEnabled(true);
        np.setDisplayedValues(mStartingLifeValues);
        np.setMinValue(0);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setMaxValue(mStartingLifeValues.length - 1);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                ((MainActivity) mContext).
                        setmStartingLife(Integer.parseInt(mStartingLifeValues[newVal]));
                mStartingLife = Integer.parseInt(mStartingLifeValues[newVal]);
            }
        });
        np.setValue(getInitialSpinnerValue());
    }

    private int getInitialSpinnerValue() {
        int result;
        switch (mStartingLife) {
            case 10:
                result = 0;
                break;
            case 20:
                result = 1;
                break;
            case 25:
                result = 2;
                break;
            case 30:
                result = 3;
                break;
            case 40:
                result = 4;
                break;
            case 50:
                result = 5;
                break;
            case 60:
                result = 6;
                break;
            case 70:
                result = 7;
                break;
            case 80:
                result = 8;
                break;
            case 90:
                result = 9;
                break;
            case 100:
                result = 10;
                break;
            default:
                result = 1;
                break;
        }
        return result;
    }

    private void setupPoisonOption(View vi) {
        TextView poisonToggle = (TextView) vi.findViewById(R.id.poison_toggle);
        setUpToggle(poisonToggle, mPoisonShowing);
        setToggle(poisonToggle, new togglePoisonCommand());
    }

    private void setupEnergyOption(View vi) {
        TextView energyToggle = (TextView) vi.findViewById(R.id.energy_toggle);
        setUpToggle(energyToggle, mEnergyShowing);
        setToggle(energyToggle, new toggleEnergyCommand());
    }

    private void setUpToggle(TextView toggle, boolean on) {
        if (on) {
            toggle.setText(mContext.getString(R.string.on));
            toggle.setTextColor(
                    Color.parseColor(mContext.getString(R.string.color_blue)));
        } else {
            toggle.setText(mContext.getString(R.string.off));
            toggle.setTextColor(
                    Color.parseColor(mContext.getString(R.string.color_red)));
        }
    }

    private void setToggle(final TextView toggle, final Command command) {
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tw = ((TextView) v);
                if (!isToggleOn(toggle)) {
                    tw.setText(mContext.getString(R.string.on));
                    tw.setTextColor(
                            Color.parseColor(mContext.getString(R.string.color_blue)));
                } else {
                    tw.setText(mContext.getString(R.string.off));
                    tw.setTextColor(
                            Color.parseColor(mContext.getString(R.string.color_red)));
                }
                command.execute();
            }
        });
    }

    private boolean isToggleOn(TextView toggle) {
        return toggle.getText().equals(mContext.getString(R.string.on));
    }

    private class togglePoisonCommand implements Command {
        @Override
        public void execute() {
            mPoisonShowing = !mPoisonShowing;
            ((MainActivity) mContext).togglePoison();
        }
    }

    private class toggleEnergyCommand implements Command {

        @Override
        public void execute() {
            mEnergyShowing = !mEnergyShowing;
            ((MainActivity) mContext).toggleEnergy();
        }
    }

    private class toggleTimerCommand implements Command{
        @Override
        public void execute() {
            mTimerShowing = !mTimerShowing;
            ((MainActivity) mContext).toggleTimer();
        }
    }

    private void setupBackgroundOption(View vi) {
        final ImageView imageView = (ImageView) vi.findViewById(R.id.background_preview);
        if (BackgroundColor.GREY == mBackgroundColor)
            imageView.setImageDrawable(vi.getContext().getResources()
                    .getDrawable(R.drawable.color_scheme_dark));
        else if (BackgroundColor.WHITE == mBackgroundColor)
            imageView.setImageDrawable(vi.getContext().getResources()
                    .getDrawable(R.drawable.color_scheme_light));
        else
            imageView.setImageDrawable(vi.getContext().getResources()
                    .getDrawable(R.drawable.color_scheme_black));


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView iw = (ImageView) v;
                if (BackgroundColor.WHITE == mBackgroundColor) {
                    iw.setImageDrawable(v.getContext().getResources()
                            .getDrawable(R.drawable.color_scheme_dark));
                    mBackgroundColor = BackgroundColor.GREY;
                } else if (BackgroundColor.GREY == mBackgroundColor) {
                    iw.setImageDrawable(v.getContext().getResources()
                            .getDrawable(R.drawable.color_scheme_black  ));
                    mBackgroundColor = BackgroundColor.BLACK;
                } else {
                    iw.setImageDrawable(v.getContext().getResources()
                            .getDrawable(R.drawable.color_scheme_light));
                    mBackgroundColor = BackgroundColor.WHITE;
                }
                ((MainActivity)mContext).toggleBackground();
            }
        });
    }

    public BackgroundColor getBackground() {
        return mBackgroundColor;
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

    public void setSettings(boolean poisonShowing, boolean energyShowing, int startingLife,
                                BackgroundColor background, int time, boolean timerShowing) {
        //System.out.println("Settings settings " + poisonShowing);
        mPoisonShowing = poisonShowing;
        mEnergyShowing = energyShowing;
        mStartingLife = startingLife;
        mBackgroundColor = background;
        mTimerShowing = timerShowing;
        mTime = time;
    }
}
