package com.minu.proto2020.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Miro on 19/2/2016.
 */
public class HistoryListAdapter extends BaseAdapter {

    private ArrayList<String> mData;
    private static LayoutInflater mLayoutInflater;

    public HistoryListAdapter(Context context, ArrayList<String> data) {
        mData = data;
        mLayoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        if (vi == null)
            vi = mLayoutInflater.inflate(R.layout.history_entry, parent, false);
        String leftLife = mData.get(position).split(" ")[0];
        String rightLife = mData.get(position).split(" ")[1];
        String leftPoison = mData.get(position).split(" ")[2];
        String rightPoison = mData.get(position).split(" ")[3];
        ((TextView) vi.findViewById(R.id.left_life)).setText(leftLife);
        ((TextView) vi.findViewById(R.id.right_life)).setText(rightLife);
        if (!leftPoison.equals("0")) {
            vi.findViewById(R.id.left_poison).setVisibility(View.VISIBLE);
            ((TextView) vi.findViewById(R.id.left_poison)).setText(leftPoison);
            vi.findViewById(R.id.poison_left_icon).setVisibility(View.VISIBLE);
        }
        if (!rightPoison.equals("0")) {
            vi.findViewById(R.id.right_poison).setVisibility(View.VISIBLE);
            ((TextView) vi.findViewById(R.id.right_poison)).setText(rightPoison);
            vi.findViewById(R.id.poison_right_icon).setVisibility(View.VISIBLE);
        }

        if (getCount() > 1 && position > 0)
            vi.findViewById(R.id.divider_pipe_above).setVisibility(View.VISIBLE);
        else
            vi.findViewById(R.id.divider_pipe_above).setVisibility(View.INVISIBLE);
        if (getCount() > 1 && position < getCount() - 1)
            vi.findViewById(R.id.divider_pipe_below).setVisibility(View.VISIBLE);
        else
            vi.findViewById(R.id.divider_pipe_below).setVisibility(View.INVISIBLE);
        return vi;
    }

    public void addAll(ArrayList<String> history) {
        mData.addAll(history);
    }

    public void clear() {
        mData.clear();
    }
}
