package com.smk17.mysmarthome.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.smk17.mysmarthome.domain.DeviceZone;
import com.smk17.mysmarthome.fragment.FragmentZoneControlPage;
import com.yh.materialwidgetlibrary.TabIndicator;

/**
 * Created by IntelliJ IDEA. User: keith. Date: 14-9-24. Time: 16:10.
 */
public class ZoneControlPagerAdapter extends FragmentPagerAdapter implements
		TabIndicator.TabResourceProvider {

	private ArrayList<Fragment> mFragments =new ArrayList<Fragment>();
	private ArrayList<DeviceZone> list = new ArrayList<DeviceZone>()  ;
//	protected static final String[] CONTENT = new String[] { "客厅",
//			"玄关", "就餐厅", "主人房" };

	
	public ZoneControlPagerAdapter(FragmentManager fragmentManager,ArrayList<DeviceZone> list) {
		super(fragmentManager);	
		this.list.addAll(list);
		for (int i = 0; i< list.size();i++){			
			mFragments.add(FragmentZoneControlPage.newInstance(list.get(i).getId()));
		}
		notifyDataSetChanged();		
	}
		
	public void addFragment(DeviceZone deviceZone){
		this.list.add(deviceZone);		
		mFragments.add(FragmentZoneControlPage.newInstance(deviceZone.getId()));
	}

	@Override
	public Fragment getItem(int position) {
//		notifyDataSetChanged();
		return mFragments.get(position);
	}

	@Override
	public String getText(int position) {
//		notifyDataSetChanged();
		return list.get(position % list.size()).getName();
//		return ZoneControlPagerAdapter.CONTENT[position % CONTENT.length];
	}

	@Override
	public int getCount() {
		return list.size();
	}
}
