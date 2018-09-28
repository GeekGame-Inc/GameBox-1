/**
 * Project Name:GameBox
 * File Name:TextUtils.java
 * Package Name:com.tenone.gamebox.view.utils
 * Date:2017-3-14����3:50:10
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

/**
 * ClassName:TextUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-14 ����3:50:10 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
public class CharSequenceUtils {
    public static SpannableStringBuilder builderTextColor(String text,
                                                          int color, int start, int end) {
        ForegroundColorSpan fgcspan = new ForegroundColorSpan( color );
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder( text );
        ssbuilder.setSpan( fgcspan, start, end, 0 );
        return ssbuilder;
    }

    public static SpannableStringBuilder StringInterceptionChangeRed(
            String string, String string2, String string3, int color1,
            int color2) {
        int fstart = string.indexOf( string2 );
        int fend = fstart + string2.length();
        SpannableStringBuilder style = new SpannableStringBuilder( string );
        if (!"".equals( string3 ) && string3 != null) {
            int bstart = string.indexOf( string3 );
            int bend = bstart + string3.length();
            style.setSpan( new ForegroundColorSpan( color1 ), bstart, bend,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE );
        }
        style.setSpan( new ForegroundColorSpan( color2 ), fstart, fend,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE );
        return style;
    }

    @SuppressWarnings("deprecation")
    public static boolean CopyToClipboard(Context cxt, String text) {
        try {
            if (!TextUtils.isEmpty( text )) {
                // ��API11��ʼandroid�Ƽ�ʹ��android.content.ClipboardManager
                // Ϊ�˼��ݵͰ汾��������ʹ�þɰ��android.text.ClipboardManager����Ȼ��ʾdeprecated������Ӱ��ʹ�á�
                ClipboardManager cm = (ClipboardManager) cxt
                        .getSystemService( Context.CLIPBOARD_SERVICE );
                // ���ı����ݷŵ�ϵͳ�������
                cm.setText( text );
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    public static String paste(Context context) {
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService( Context.CLIPBOARD_SERVICE );
        return cmb.getText().toString().trim();
    }


    public static String getVisibilyPhone(String phoneNumber) {
        if (phoneNumber.length() < 7) {
            return phoneNumber;
        }
        String returnPhone = phoneNumber.substring( 0, 3 ) + "****"
                + phoneNumber.substring( 7, phoneNumber.length() );
        return returnPhone;
    }


    public static String getVisibilyText(String text) {
        String t = "*******";
        if (TextUtils.isEmpty( text )) {
            return t;
        }
        if (text.length() > 2) {
            String s = text.substring( 0, 1 );
            String s1 = text.substring( text.length() - 1, text.length() );
            return s + t + s1;
        } else {
            return text + t;
        }
    }
}
