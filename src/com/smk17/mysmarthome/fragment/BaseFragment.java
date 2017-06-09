package com.smk17.mysmarthome.fragment;

import java.lang.reflect.Field;

import com.smk17.mysmarthome.Constant;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
	protected int themeId = 1;
	 /**
     * 此函数需要重构它已适配当前页面并在onCreate中调用
     * @param id 已使用的主题id
     */
    protected void setTheme(){
    	if(getActivity() != null){
    		SharedPreferences sp = getActivity().getSharedPreferences(Constant.INFO_THEME ,Activity.MODE_PRIVATE);
    		themeId = sp.getInt(Constant.THEME_ID, 1);        	
    	}    	
    	switch (themeId) {
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;

		default:
			break;
		}
    }
	
	@Override
	public void onDetach() {
		super.onDetach();
		try {
    	    Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
    	    childFragmentManager.setAccessible(true);
    	    childFragmentManager.set(this, null);

    	} catch (NoSuchFieldException e) {
    	    throw new RuntimeException(e);
    	} catch (IllegalAccessException e) {
    	    throw new RuntimeException(e);
    	}
	}
}
