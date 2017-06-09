package com.smk17.mysmarthome.fragment.systemsettings;

import com.smk17.mysmarthome.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentAddTerminalDevice extends SystemFragment {
	
	public static final FragmentAddTerminalDevice newInstance(){
		FragmentAddTerminalDevice fragment = new FragmentAddTerminalDevice();
		return fragment;		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_addterminaldevice, container, false);  		 
		return view;
	}
}
