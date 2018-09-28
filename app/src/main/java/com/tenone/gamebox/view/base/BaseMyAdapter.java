package com.tenone.gamebox.view.base;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public abstract class BaseMyAdapter<T> extends BaseAdapter {
    protected List<T> mList = new ArrayList<T>();
    public Context context;
    public LayoutInflater inflater;
    Map<String, Integer> numMap = null;

    public List<T> getListData() {
        return mList;
    }

    public void updateData(List<T> newData) {
        mList.clear();
        mList.addAll(newData);
        notifyDataSetChanged();
    }

    public Map<String, Integer> getNumMap() {
        return numMap;
    }

    public void setNumMap(Map<String, Integer> numMap) {
        this.numMap = numMap;
    }

    public BaseMyAdapter(Context context) {
        // TODO Auto-generated constructor stub
        super();
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void appendToList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.addAll(list);
        notifyDataSetChanged();
        Log.e("appendToList", list.size() + "");
    }

    public void appendToTopList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.addAll(0, list);
        notifyDataSetChanged();
    }

    public void appendToTop(T object) {
        if (object == null) {
            return;
        }
        mList.add(0, object);
        notifyDataSetChanged();
    }
    public void appendToLast(T object) {
        if (object == null) {
            return;
        }
        mList.add(object);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        if (position > mList.size() - 1) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (position == getCount() - 1) {
            onReachBottom();
        }
        return getExView(position, convertView, parent);
    }

    protected abstract View getExView(int position, View convertView,
                                      ViewGroup parent);

    protected abstract void onReachBottom();

}
