package com.smk17.mysmarthome.fragment;

import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentSystemSettings extends MianFragment{

	private int[] id = { R.id.item_add_terminal_device, R.id.item_add_main_device, R.id.item_learn_device, 
			R.id.item_add_infrared_device, R.id.item_terminal_device_management , 
			R.id.item_main_device_management , R.id.item_zone_management, R.id.item_scene_management, 
			R.id.item_camera_management , R.id.item_infrared_device_management, R.id.item_about  };
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_systemsettings, container, false);  
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			SystemBarTintManager tintManager = new SystemBarTintManager(getActivity()); 
//			SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();  
//			view.setPadding(0,0, config.getPixelInsetRight(), config.getPixelInsetBottom()); 
//		} 
//		RelativeLayout rl = (RelativeLayout)view.findViewById(R.id.rl_item_settings);
		final SharedPreferences sp = getActivity().getSharedPreferences(Constant.INFO_THEME ,Activity.MODE_PRIVATE);
		final int theme_id = sp.getInt(Constant.THEME_ID, 1);
		int TextColor = getActivity().getResources().getColor(R.color.white);
		switch (theme_id) {
		case 1:
			TextColor = getActivity().getResources().getColor(R.color.black);
			break;
		case 2:case 3:case 4:case 5:case 6:
			TextColor = getActivity().getResources().getColor(R.color.white);
			break;
		default:
			break;
		}
		for (int i = 0; i < id.length; i++) {
			View v = view.findViewById(id[i]);
			TextView tv = (TextView) v.findViewById(R.id.tv_item_set_name);			
            tv.setText(Constant.SystemSettingsTitleNames[i]);
            tv.setTextColor(TextColor);
		}
		return view;
	}	
}
