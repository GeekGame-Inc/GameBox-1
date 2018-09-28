package com.tenone.gamebox.mode.biz;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.tenone.gamebox.mode.able.MyMessageAble;
import com.tenone.gamebox.view.activity.NotificationDetailsActivity;
import com.tenone.gamebox.view.fragment.CommentReplyFragment;
import com.tenone.gamebox.view.fragment.MessageListFragment;

import java.util.ArrayList;
import java.util.List;

public class MyMessageBiz implements MyMessageAble {

	@Override
	public List<Fragment> getFragments() {
		List<Fragment> fragments = new ArrayList<Fragment>();
        MessageListFragment fragment2 = new MessageListFragment();
        NotificationDetailsActivity fragment1 = new NotificationDetailsActivity();
        CommentReplyFragment commentReplyFragment = new CommentReplyFragment();
        fragments.add(fragment2);
        fragments.add(commentReplyFragment);
		fragments.add(fragment1);
		return fragments;
	}

	@Override
	public List<String> getTitles(Context cxt, int rId) {
		List<String> titles = new ArrayList<String>();
		String[] array = cxt.getResources().getStringArray(rId);
		if (array != null) {
			for (String string : array) {
				titles.add(string);
			}
		}
		return titles;
	}
}
