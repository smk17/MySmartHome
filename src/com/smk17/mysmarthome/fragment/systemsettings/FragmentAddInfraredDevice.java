package com.smk17.mysmarthome.fragment.systemsettings;

import com.smk17.mysmarthome.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentAddInfraredDevice extends SystemFragment {
	
	public static final FragmentAddInfraredDevice newInstance(){
		FragmentAddInfraredDevice fragment = new FragmentAddInfraredDevice();
		return fragment;		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_addinfrareddevice, container, false);  		 
		return view;
	}
}
