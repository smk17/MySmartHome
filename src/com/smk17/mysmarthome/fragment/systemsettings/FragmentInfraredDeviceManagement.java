package com.smk17.mysmarthome.fragment.systemsettings;

import com.smk17.mysmarthome.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentInfraredDeviceManagement extends SystemFragment {
	
	public static final FragmentInfraredDeviceManagement newInstance(){
		FragmentInfraredDeviceManagement fragment = new FragmentInfraredDeviceManagement();
		return fragment;		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_infrareddevicemanagement, container, false);  		 
		return view;
	}
}
