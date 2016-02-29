package com.dreamfactory.bluetooth.view.adapter.base;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 作者：Kurtis on 2016/2/23 21:41
 * Eevery one should have a dream, what if one day it comes true!
 */
public class ViewHolder {

    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    public ViewHolder(ViewGroup parent, int position, int layoutResId , LayoutInflater mInflater) {
        mConvertView = mInflater.inflate(layoutResId, parent, false);
        this.mPosition = position;
        mViews = new SparseArray<>();
        mConvertView.setTag(this);
    }

    public static ViewHolder get(View convertView, ViewGroup parent, int position, int layoutResId , LayoutInflater mInflater) {
        if (null == convertView) {
            return new ViewHolder(parent, position, layoutResId, mInflater);
        } else {
            return (ViewHolder) convertView.getTag();
        }
    }

    public <T extends View> T getView(int resId) {
        View view = mViews.get(resId);
        if (null == view) {
            view = mConvertView.findViewById(resId);
            mViews.put(resId, view);
        }
        return (T)view;
    }

    public ViewHolder setText(int id, String res) {
        TextView view = getView(id);
        if (null != view) {
            view.setText(res);
        }
        return this;
    }

    public View getConvertView() {
        return mConvertView;
    }
}
