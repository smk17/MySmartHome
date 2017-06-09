package com.smk17.mysmarthome.fragment.usersettings;

import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.adapter.ThemeAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

public class FragmentTheme extends UserFragment {
	private GridView mGvList;
	private ThemeAdapter adapter;
	private LinearLayout mLlContainer;
	private BtnClickListener mBtnClickListener;
	
	public static final FragmentTheme newInstance(BtnClickListener BtnClickListener){
		FragmentTheme fragment = new FragmentTheme(BtnClickListener);
		return fragment;		
	}
	
	private FragmentTheme(BtnClickListener BtnClickListener){
		this.mBtnClickListener = BtnClickListener;
	}
	
	public interface BtnClickListener  
    {
		void onBtnClick(Integer id);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_theme, container, false);
		mGvList = (GridView)view.findViewById(R.id.gv_theme_list);
		mLlContainer = (LinearLayout)view.findViewById(R.id.content);
		adapter = new ThemeAdapter(new int[] {1,2,3,4,5,6}, getActivity(),new ThemeAdapter.BtnClickListener() {
			
			@Override
			public void onBtnClick(Integer id) {				
				switch (id) {
				case 1:
					mLlContainer.setBackgroundResource(android.R.color.transparent);
					break;
				case 2:
					mLlContainer.setBackgroundResource(android.R.color.transparent);
					break;
				case 3:
					mLlContainer.setBackgroundResource(android.R.color.transparent);
					break;
				case 4:
					mLlContainer.setBackgroundResource(android.R.color.transparent);
					break;
				case 5:
					mLlContainer.setBackgroundResource(android.R.color.transparent);
					break;
				case 6:
					mLlContainer.setBackgroundResource(android.R.color.transparent);
					break;

				default:
					break;
				}
				mBtnClickListener.onBtnClick(id);
			}
		});
		mGvList.setAdapter(adapter);
		setTheme();
		return view;
	}

	@Override
	protected void setTheme() {
		super.setTheme();
		switch (themeId) {
		case 1:
			mLlContainer.setBackgroundResource(R.color.content_background);
			break;
		case 2:
			mLlContainer.setBackgroundResource(android.R.color.transparent);
			break;
		case 3:
			mLlContainer.setBackgroundResource(android.R.color.transparent);
			break;
		case 4:
			mLlContainer.setBackgroundResource(android.R.color.transparent);
			break;
		case 5:
			mLlContainer.setBackgroundResource(android.R.color.transparent);
			break;
		case 6:
			mLlContainer.setBackgroundResource(android.R.color.transparent);
			break;

		default:
			break;
		}
	}
	
	
}
