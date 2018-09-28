/**
 * Copyright 2015 bingoogolapple
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tenone.gamebox.view.custom.bga;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tenone.gamebox.mode.listener.BGAOnItemChildClickListener;
import com.tenone.gamebox.mode.mode.BGAOnRVItemClickListener;
import com.tenone.gamebox.mode.mode.BGAOnRVItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildCheckedChangeListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildLongClickListener;


/**
 * @param <M> �������������
 */
public abstract class BGARecyclerViewAdapter<M> extends RecyclerView.Adapter<BGARecyclerViewHolder> {
    protected final int mItemLayoutId;
    protected Context mContext;
    protected List<M> mData;
    protected BGAOnItemChildClickListener mOnItemChildClickListener;
    protected BGAOnItemChildLongClickListener mOnItemChildLongClickListener;
    protected BGAOnItemChildCheckedChangeListener mOnItemChildCheckedChangeListener;
    protected BGAOnRVItemClickListener mOnRVItemClickListener;
    protected BGAOnRVItemLongClickListener mOnRVItemLongClickListener;

    protected RecyclerView mRecyclerView;

    public BGARecyclerViewAdapter(RecyclerView recyclerView, int itemLayoutId) {
        mRecyclerView = recyclerView;
        mContext = mRecyclerView.getContext();
        mItemLayoutId = itemLayoutId;
        mData = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public BGARecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BGARecyclerViewHolder viewHolder = new BGARecyclerViewHolder(mRecyclerView, LayoutInflater.from(mContext).inflate(mItemLayoutId, parent, false), mOnRVItemClickListener, mOnRVItemLongClickListener);
        viewHolder.getViewHolderHelper().setOnItemChildClickListener(mOnItemChildClickListener);
        viewHolder.getViewHolderHelper().setOnItemChildLongClickListener(mOnItemChildLongClickListener);
        viewHolder.getViewHolderHelper().setOnItemChildCheckedChangeListener(mOnItemChildCheckedChangeListener);
        setItemChildListener(viewHolder.getViewHolderHelper());
        return viewHolder;
    }

    /**
     * Ϊitem�ĺ��ӽڵ����ü�������������ÿһ�������б�ҪΪitem���ӿؼ�����¼���������������������˿�ʵ�֣���Ҫ�����¼�������ʱ��д�÷�������
     *
     * @param viewHolderHelper
     */
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
    }

    @Override
    public void onBindViewHolder(BGARecyclerViewHolder viewHolder, int position) {
        fillData(viewHolder.getViewHolderHelper(), position, getItem(position));
    }

    /**
     * ���item����
     *
     * @param viewHolderHelper
     * @param position
     * @param model
     */
    protected abstract void fillData(BGAViewHolderHelper viewHolderHelper, int position, M model);

    /**
     * ����item�ĵ���¼�������
     *
     * @param onRVItemClickListener
     */
    public void setOnRVItemClickListener(BGAOnRVItemClickListener onRVItemClickListener) {
        mOnRVItemClickListener = onRVItemClickListener;
    }

    /**
     * ����item�ĳ����¼�������
     *
     * @param onRVItemLongClickListener
     */
    public void setOnRVItemLongClickListener(BGAOnRVItemLongClickListener onRVItemLongClickListener) {
        mOnRVItemLongClickListener = onRVItemLongClickListener;
    }

    /**
     * ����item�е��ӿؼ�����¼�������
     *
     * @param onItemChildClickListener
     */
    public void setOnItemChildClickListener(BGAOnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    /**
     * ����item�е��ӿؼ������¼�������
     *
     * @param onItemChildLongClickListener
     */
    public void setOnItemChildLongClickListener(BGAOnItemChildLongClickListener onItemChildLongClickListener) {
        mOnItemChildLongClickListener = onItemChildLongClickListener;
    }

    /**
     * ����item�ӿؼ�ѡ��״̬�仯�¼�������
     *
     * @param onItemChildCheckedChangeListener
     */
    public void setOnItemChildCheckedChangeListener(BGAOnItemChildCheckedChangeListener onItemChildCheckedChangeListener) {
        mOnItemChildCheckedChangeListener = onItemChildCheckedChangeListener;
    }

    public M getItem(int position) {
        return mData.get(position);
    }

    /**
     * ��ȡ���ݼ���
     *
     * @return
     */
    public List<M> getData() {
        return mData;
    }

    /**
     * �ڼ���ͷ������µ����ݼ��ϣ������ӷ�������ȡ���µ����ݼ��ϣ���������΢���������µļ���΢�����ݣ�
     *
     * @param data
     */
    public void addNewData(List<M> data) {
        if (data != null) {
            mData.addAll(0, data);
            notifyItemRangeInserted(0, data.size());
        }
    }

    /**
     * �ڼ���β����Ӹ������ݼ��ϣ������ӷ�������ȡ��������ݼ��ϣ���������΢���б��������ظ���ʱ�䷢����΢�����ݣ�
     *
     * @param data
     */
    public void addMoreData(List<M> data) {
        if (data != null) {
            mData.addAll(mData.size(), data);
            notifyItemRangeInserted(mData.size(), data.size());
        }
    }

    /**
     * ����ȫ�µ����ݼ��ϣ��������null������������б���һ�δӷ������������ݣ���������ˢ�µ�ǰ�������ݱ�
     *
     * @param data
     */
    public void setData(List<M> data) {
        if (data != null) {
            mData = data;
        } else {
            mData.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * ��������б�
     */
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    /**
     * ɾ��ָ������������Ŀ
     *
     * @param position
     */
    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * ɾ��ָ��������Ŀ
     *
     * @param model
     */
    public void removeItem(M model) {
        removeItem(mData.indexOf(model));
    }

    /**
     * ��ָ��λ�����������Ŀ
     *
     * @param position
     * @param model
     */
    public void addItem(int position, M model) {
        mData.add(position, model);
        notifyItemInserted(position);
    }

    /**
     * �ڼ���ͷ�����������Ŀ
     *
     * @param model
     */
    public void addFirstItem(M model) {
        addItem(0, model);
    }

    /**
     * �ڼ���ĩβ���������Ŀ
     *
     * @param model
     */
    public void addLastItem(M model) {
        addItem(mData.size(), model);
    }

    /**
     * �滻ָ��������������Ŀ
     *
     * @param location
     * @param newModel
     */
    public void setItem(int location, M newModel) {
        mData.set(location, newModel);
        notifyItemChanged(location);
    }

    /**
     * �滻ָ��������Ŀ
     *
     * @param oldModel
     * @param newModel
     */
    public void setItem(M oldModel, M newModel) {
        setItem(mData.indexOf(oldModel), newModel);
    }

    /**
     * �ƶ�������Ŀ��λ��
     *
     * @param fromPosition
     * @param toPosition
     */
    public void moveItem(int fromPosition, int toPosition) {
        mData.add(toPosition, mData.remove(fromPosition));
        notifyItemMoved(fromPosition, toPosition);
    }
}