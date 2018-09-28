/**
 * Project Name:GameBox
 * File Name:RegisterAble.java
 * Package Name:com.tenone.gamebox.mode.able
 * Date:2017-3-15下午5:40:50
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.mode.view;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * 注册 ClassName:RegisterAble <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-15 下午5:40:50 <br/>
 *
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface RegisterView {

    /* 标题 */
    TitleBarView getTitleBarView();

    /* 账号 */
    CustomizeEditText getAccountView();

    /* 密码 */
    CustomizeEditText getPwdView();

    /* 密码 */
    CustomizeEditText getPwdView2();

    /* 手机号 */
    CustomizeEditText getPhoneView();

    /* 验证码 */
    CustomizeEditText getCodeView();

    /* 发送验证码 */
    TextView getSendCodeView();

    /* 注册 */
    Button getRegisterView();

    /*协议*/
    CheckBox getCheckBox();


    LinearLayout getLayout1();

    LinearLayout getLayout2();
}
