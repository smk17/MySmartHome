package com.smk17.mysmarthome.fragment;

import java.lang.reflect.Field;

import com.smk17.mysmarthome.Constant;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
	protected int themeId = 1;
	 /**
     * �˺�����Ҫ�ع��������䵱ǰҳ�沢��onCreate�е���
     * @param id ��ʹ�õ�����id
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
