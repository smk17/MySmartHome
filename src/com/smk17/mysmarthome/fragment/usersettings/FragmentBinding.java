package com.smk17.mysmarthome.fragment.usersettings;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.adapter.FragmentItemAdapter;
import com.smk17.mysmarthome.adapter.FragmentItemAdapter.FIABtnClickListener;
import com.smk17.mysmarthome.domain.DeviceSensor;

public class FragmentBinding extends UserFragment{
	private ArrayList<DeviceSensor> dList =  new ArrayList<DeviceSensor>();
	private ListView deviceList;	
	private FragmentItemAdapter adapter;
	
	public static final FragmentBinding newInstance(){
		FragmentBinding fragment = new FragmentBinding();
		return fragment;		
	}	
	
	private FIABtnClickListener fBtnClickListener = new FIABtnClickListener() {
		
		@Override
		public void onFEditBtnClick(DeviceSensor device) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onFDeleteBtnClick(DeviceSensor device) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onFClick(Integer position) {
			// TODO Auto-generated method stub
			
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_binding, container, false);  	
		deviceList = (ListView)view.findViewById(R.id.device_list);
		dList = new ArrayList<DeviceSensor>();
		dList.add(new DeviceSensor(1, 0, 0, 0, "QQ", getString(R.string.no_binding), 1, null));
		dList.add(new DeviceSensor(2, 0, 1, 0, getString(R.string.SinaWeibo), getString(R.string.no_binding), 1, null));
		adapter = new FragmentItemAdapter(dList, getActivity(),FragmentItemAdapter.TYPE_Binding ,fBtnClickListener);		 
		deviceList.setAdapter(adapter);		
		return view;
	}
}
