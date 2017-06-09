package com.smk17.mysmarthome.fragment;

import com.smk17.mysmarthome.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentAlarmControl extends MianFragment {

//	private ArrayList<DeviceSensor> list ;
//	private FragmentItemAdapter adapter;
//	private ListView deviceList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_alarmcontrol, container, false);  		 
		return view;
	}
}
