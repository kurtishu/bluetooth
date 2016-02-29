package com.dreamfactory.bluetooth.view.adapter.base;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Kurtis on 2016/2/23 21:41
 * Eevery one should have a dream, what if one day it comes true!
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    private List<T> mDatas;
    private LayoutInflater inflater;
    private int layoutId;
    private Context mContext;

   public CommonAdapter(Context context, int layoutId) {
       mDatas = new ArrayList<T>();
       this.layoutId = layoutId;
       this.inflater = LayoutInflater.from(context);
       mContext = context;
   }

    public void setDatas(List<T> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(convertView, parent, position, layoutId, inflater);
        convert(viewHolder, position);
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder viewHolder, int position);

    public Context getContext() {
        return mContext;
    }
}
