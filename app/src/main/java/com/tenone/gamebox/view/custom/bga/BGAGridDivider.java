/**
 * Copyright 2016 bingoogolapple
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

import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * ����:���� �ʼ�:bingoogolapple@gmail.com
 * ����ʱ��:17/1/9 ����11:12
 * ����:
 */
public class BGAGridDivider extends RecyclerView.ItemDecoration {
    private int mSpace;

    private BGAGridDivider(int space) {
        mSpace = space;
    }

    /**
     * ���ü����Դ id
     *
     * @param resId
     * @return
     */
    public static BGAGridDivider newInstanceWithSpaceRes(@DimenRes int resId) {
        return new BGAGridDivider(BGABaseAdapterUtil.getDimensionPixelOffset(resId));
    }

    /**
     * ���ü��
     *
     * @param spaceDp ��λΪ dp
     * @return
     */
    public static BGAGridDivider newInstanceWithSpaceDp(int spaceDp) {
        return new BGAGridDivider(BGABaseAdapterUtil.dp2px(spaceDp));
    }

    /**
     * ���ü��
     *
     * @param spacePx ��λΪ px
     * @return
     */
    public static BGAGridDivider newInstanceWithSpacePx(int spacePx) {
        return new BGAGridDivider(spacePx);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.top = mSpace;
        outRect.bottom = mSpace;
    }
}