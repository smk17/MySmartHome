package com.smk17.mysmarthome.fragment.usersettings;

import com.smk17.mysmarthome.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentAdvancedSettings extends UserFragment {
	
	public static final FragmentAdvancedSettings newInstance(){
		FragmentAdvancedSettings fragment = new FragmentAdvancedSettings();
		return fragment;		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_advancedsettings, container, false);  		 
		return view;
	}
}
