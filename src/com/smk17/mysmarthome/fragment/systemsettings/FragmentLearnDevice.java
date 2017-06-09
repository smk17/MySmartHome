package com.smk17.mysmarthome.fragment.systemsettings;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.Utils.BitmapUtil;
import com.smk17.mysmarthome.adapter.FragmentItemAdapter;
import com.smk17.mysmarthome.cloud.ExecToCloudSql;
import com.smk17.mysmarthome.cloud.ParseDevice;
import com.smk17.mysmarthome.db.ExecSql;
import com.smk17.mysmarthome.db.HotSpotMode;
import com.smk17.mysmarthome.domain.DeviceSensor;
import com.yh.materialdesign.dialog.MaterialDialog;
import com.yh.materialdesign.views.DropdownButton;
import com.yh.materialdesign.views.DropdownItemObject;
import com.yh.materialdesign.views.DropdownListView;
import com.yh.materialdesign.views.LayoutRipple;
import com.yh.materialdesign.views.RippleBackground;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
 
public class FragmentLearnDevice extends SystemFragment {
	
	private int foundDeviceTag = 0;
	private int foundDeviceNum = 0;
	private boolean isStart = false;
	private ImageView button;
	private String SensorUuid = null;
	private RippleBackground rippleBackground;
	private TextView mTvTips;
	private RelativeLayout mRlResult;
	private LinearLayout mLlContent;
	private ListView deviceList;
	private View mask;
	private DropdownButton chooseMainDevice;
	private DropdownListView dropdownMainDevice;
	private Animation dropdown_in, dropdown_out, dropdown_mask_out;
	private static int MainDeviceSelectedId = 0;
	private LayoutRipple saveButtons;
	private FragmentItemAdapter adapter;
	private ArrayList<DeviceSensor> dList = null; 
	private ArrayList<DeviceSensor> MainList = new ArrayList<DeviceSensor>();
	private DropdownButtonsController dropdownButtonsController = new DropdownButtonsController();
	private ImageView[] foundDevices = null;
	private Timer timer = new Timer();
	private MyHandler handler = new MyHandler(this);
	
	public static final FragmentLearnDevice newInstance(){
		FragmentLearnDevice fragment = new FragmentLearnDevice();
		return fragment;		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_learndevice, container, false);
		rippleBackground=(RippleBackground)view.findViewById(R.id.rippleBackground);
		mLlContent = (LinearLayout)view.findViewById(R.id.content);
		button=(ImageView)view.findViewById(R.id.centerImage);
		mRlResult = (RelativeLayout)view.findViewById(R.id.result);
		deviceList = (ListView)view.findViewById(R.id.device_list);
		saveButtons = (LayoutRipple)view.findViewById(R.id.saveButtons);
		mask = view.findViewById(R.id.mask);
        chooseMainDevice = (DropdownButton) view.findViewById(R.id.chooseMainDevice);		
        dropdownMainDevice = (DropdownListView) view.findViewById(R.id.dropdownMainDevice);
        
        dropdown_in = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_in);
        dropdown_out = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_out);
        dropdown_mask_out = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_mask_out);
		MainDeviceSelectedId = MyApplication.getMainDeviceId();		
        mTvTips = (TextView)view.findViewById(R.id.tv_tips);
        mTvTips.setText("点击进入匹配模式");
        initFoundDevice();
        dList = new ArrayList<DeviceSensor>();
        adapter = new FragmentItemAdapter(dList, getActivity(), FragmentItemAdapter.TYPE_MacthDevice);
        deviceList.setAdapter(adapter);
        saveButtons.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (MyApplication.getNetMode()) {
				case Constant.Network_Mode:
					//TODO 外网模式下的初始化
					ExecToCloudSql.matchDeviceRunnable(MainDeviceSelectedId, "0");
					break;
				case Constant.Intranet_Mode:
					//TODO 内网模式下的初始化
					break;
				case Constant.HotSpot_Mode:
					//TODO 热点模式下的初始化
					ExecSql.updateDevice(dList,false);
					break;
				default:
					break;
				}
				dList.clear();
				adapter.notifyDataSetChanged();
				mRlResult.setVisibility(View.GONE);
				chooseMainDevice.setEnabled(true);
				mLlContent.setVisibility(View.VISIBLE);
			}
		});
        switch (MyApplication.getNetMode()) {
		case Constant.Network_Mode:
			//TODO 外网模式下的初始化
			button.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View view) {
		            	if(isStart){
		            		//取消匹配
		            		mTvTips.setText("点击进入匹配模式");
		            		chooseMainDevice.setEnabled(true);
		            		macthOk();
		            	}else{
		            		//开始匹配
		            		mTvTips.setText("开始匹配...");
		            		foundDeviceNum = 0;
		            		dList.clear();
		            		adapter.notifyDataSetChanged();
		            		chooseMainDevice.setEnabled(false);
		            		rippleBackground.startRippleAnimation();
		            		ExecToCloudSql.matchDeviceRunnable(MainDeviceSelectedId, "1");
		            		isStart = !isStart;
		            	}
		            	
		            }
		        });     
			ExecToCloudSql.getMainDeviceRunnable(handler);
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					ExecToCloudSql.matchDeviceRunnable(handler, MainDeviceSelectedId);
				}
			}, 500, Constant.TASKTIME);
			break;
		case Constant.Intranet_Mode:
			//TODO 内网模式下的初始化
			break;
		case Constant.HotSpot_Mode:
			//TODO 热点模式下的初始化
			button.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	            	if(isStart){
	            		//取消匹配
	            		mTvTips.setText("点击进入匹配模式");
	            		chooseMainDevice.setEnabled(true);
	            		HotSpotMode.CloseRevTcpCli();
	            		macthOk();
	            	}else{
	            		//开始匹配
	            		mTvTips.setText("开始匹配...");
	            		foundDeviceNum = 0;
	            		dList.clear();
	            		adapter.notifyDataSetChanged();
	            		chooseMainDevice.setEnabled(false);
	            		rippleBackground.startRippleAnimation();
	            		HotSpotMode.matchRevTcpCli(handler);
	            		isStart = !isStart;
	            	}
	            	
	            }
	        });
			
			break;
		default:
			break;
		}        
		return view;
	}
	
	@Override
	public void onDestroy() {
		HotSpotMode.CloseRevTcpCli();
		timer.cancel();
		super.onDestroy();
	}
	
	@SuppressLint("NewApi")
	private void foundDevice(){
		AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList=new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundDevices[foundDeviceTag], "ScaleX", 0f, 1.2f, 1f);
		animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundDevices[foundDeviceTag], "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        foundDevices[foundDeviceTag].setVisibility(View.VISIBLE);
        animatorSet.start();		
    }
	
	private void initFoundDevice(){

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(BitmapUtil.dip(getActivity(),64),BitmapUtil.dip(getActivity(),64));
		
		ImageView foundDevice1=new ImageView(getActivity());
		foundDevice1.setImageResource(R.drawable.phone2);
		foundDevice1.setVisibility(View.INVISIBLE);
		params = new RelativeLayout.LayoutParams(BitmapUtil.dip(getActivity(),64),BitmapUtil.dip(getActivity(),64));
		params.addRule(RelativeLayout.ABOVE, button.getId());
		params.addRule(RelativeLayout.LEFT_OF, button.getId());
		params.setMargins(0, 0, BitmapUtil.dip(getActivity(),6), BitmapUtil.dip(getActivity(),32));
		foundDevice1.setLayoutParams(params);
		rippleBackground.addView(foundDevice1);
		
		ImageView foundDevice2=new ImageView(getActivity());
		foundDevice2.setImageResource(R.drawable.phone2);
		foundDevice2.setVisibility(View.INVISIBLE);
		params = new RelativeLayout.LayoutParams(BitmapUtil.dip(getActivity(),64),BitmapUtil.dip(getActivity(),64));
		params.addRule(RelativeLayout.ABOVE, button.getId());
		params.addRule(RelativeLayout.RIGHT_OF, button.getId());
		params.setMargins(BitmapUtil.dip(getActivity(),6), 0, 0, BitmapUtil.dip(getActivity(),32));
		foundDevice2.setLayoutParams(params);
		rippleBackground.addView(foundDevice2);
		
		ImageView foundDevice3=new ImageView(getActivity());
		foundDevice3.setImageResource(R.drawable.phone2);
		params = new RelativeLayout.LayoutParams(BitmapUtil.dip(getActivity(),64),BitmapUtil.dip(getActivity(),64));
		foundDevice3.setVisibility(View.INVISIBLE);
		params.addRule(RelativeLayout.BELOW, button.getId());
		params.addRule(RelativeLayout.LEFT_OF, button.getId());
		params.setMargins(0, BitmapUtil.dip(getActivity(),32), BitmapUtil.dip(getActivity(),6), 0);
		foundDevice3.setLayoutParams(params);
		rippleBackground.addView(foundDevice3);
		
		ImageView foundDevice4=new ImageView(getActivity());
		foundDevice4.setImageResource(R.drawable.phone2);
		params = new RelativeLayout.LayoutParams(BitmapUtil.dip(getActivity(),64),BitmapUtil.dip(getActivity(),64));
		foundDevice4.setVisibility(View.INVISIBLE);
		params.addRule(RelativeLayout.BELOW, button.getId());
		params.addRule(RelativeLayout.RIGHT_OF, button.getId());
		params.setMargins(BitmapUtil.dip(getActivity(),6), BitmapUtil.dip(getActivity(),32), 0, 0);
		foundDevice4.setLayoutParams(params);
		rippleBackground.addView(foundDevice4);
		
		foundDevices = new ImageView[]{foundDevice1,foundDevice2,foundDevice3,foundDevice4};
	}
	
	private void macthOk(){
		rippleBackground.stopRippleAnimation();
		if(foundDeviceNum>0){
			mRlResult.setVisibility(View.VISIBLE);
			mLlContent.setVisibility(View.GONE);
		}
		foundDeviceTag = 0;
		foundDeviceNum=0;							
		for(int i = 0; i < foundDevices.length;i++){
			foundDevices[i].setVisibility(View.GONE);
		}
		ExecToCloudSql.matchDeviceRunnable(MainDeviceSelectedId, "0");
		isStart = !isStart;				
	}
	
	private void matchToOne(ArrayList<DeviceSensor> tmpList){
		if(tmpList.size() != 0){
			String uid = tmpList.get(0).getUid();
			String uuid = uid.substring(0, uid.length()-2);
			if(dList.size() == 0){
				if(isStart){
	        		if( foundDeviceTag < foundDevices.length){
	        			foundDevice();
	        			foundDeviceTag++;
	        		}                        		
	        	}
				foundDeviceNum++;
				mTvTips.setText("成功匹配"+foundDeviceNum+"个");
				dList.clear();
				for (DeviceSensor deviceSensor : tmpList) {
					dList.add(deviceSensor);
					adapter.notifyDataSetChanged();
				}
			}else {									
				Log.d("MyHandler", "new uuid is" + uuid);
				if( SensorUuid != null && !SensorUuid.equals(uuid)){//不相等，新设备
					if(isStart){
		        		if( foundDeviceTag < foundDevices.length){
		        			foundDevice();
		        			foundDeviceTag++;
		        		}                        		
		        	}
					foundDeviceNum++;
					mTvTips.setText("成功匹配"+foundDeviceNum+"个");
					for (DeviceSensor deviceSensor : tmpList) {
						dList.add(deviceSensor);
						adapter.notifyDataSetChanged();
					}
				}								
			}
			SensorUuid = uuid;
		}
	}
	
	private void refresh(){
		mTvTips.setText("匹配完成，再次点击进入匹配模式");	
		if(foundDeviceNum==0){
			final MaterialDialog alert = new MaterialDialog(getActivity());
			String msg = "匹配已经完成，但是主控并没发现新的控制器。\n请确保有新的控制器并已经上电。";
			alert.setTitle("匹配完成").setMessage(msg).setCanceledOnTouchOutside(true)
							.setPositiveButton("好的", new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									alert.dismiss();													
								}
							}).show();
		}
		macthOk();
	}
	
	private void refresh(int match_status,ArrayList<DeviceSensor> tmpList){
		Log.e("refresh", "go in.");
		switch (match_status) {
		case 2:		//匹配出一个		
			Log.e("refresh--match_status", match_status+"");			
			if(tmpList.size() != 0){
				String uid = tmpList.get(0).getUid();
				String uuid = uid.substring(0, uid.length()-2);
				Log.e("refresh--dList.size()", dList.size()+"");	
				if(dList.size() == 0){
					Log.e("refresh--dList.size()", dList.size()+"");	
					if(isStart){
		        		if( foundDeviceTag < foundDevices.length){
		        			foundDevice();
		        			foundDeviceTag++;
		        		}                        		
		        	}
					foundDeviceNum++;
					mTvTips.setText("成功匹配"+foundDeviceNum+"个");
					dList.clear();
					dList.addAll(tmpList);
					adapter.notifyDataSetChanged();
					ExecToCloudSql.UpdateDeviceTagRunnable(tmpList);
					ExecToCloudSql.matchDeviceRunnable(MainDeviceSelectedId, "1");
				}else {									
					Log.d("MyHandler", "new uuid is" + uuid);
					if( SensorUuid != null && !SensorUuid.equals(uuid)){//不相等，新设备
						if(isStart){
			        		if( foundDeviceTag < foundDevices.length){
			        			foundDevice();
			        			foundDeviceTag++;
			        		}                        		
			        	}
						foundDeviceNum++;
						mTvTips.setText("成功匹配"+foundDeviceNum+"个");
						dList.addAll(tmpList);
						adapter.notifyDataSetChanged();
						ExecToCloudSql.UpdateDeviceTagRunnable(tmpList);
						ExecToCloudSql.matchDeviceRunnable(MainDeviceSelectedId, "1");
					}								
				}
				SensorUuid = uuid;
			}
			break;
		case 4:		//匹配完成	
			mTvTips.setText("匹配完成，再次点击进入匹配模式");	
			if(foundDeviceNum==0){
				final MaterialDialog alert = new MaterialDialog(getActivity());
				String msg1 = "匹配已经完成，但是主控并没发现新的控制器。\n请确保有新的控制器并已经上电。";
				alert.setTitle("匹配完成").setMessage(msg1).setCanceledOnTouchOutside(true)
								.setPositiveButton("好的", new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										alert.dismiss();													
									}
								}).show();
			}
			macthOk();
			break;

		default:
			break;
		}
	}

	private static class MyHandler extends Handler { 
		//TODO MyHandler类定义
		WeakReference<FragmentLearnDevice> mActivity;
		
		public MyHandler(FragmentLearnDevice aLearnDevice){
			mActivity = new WeakReference<FragmentLearnDevice>(aLearnDevice);
		}
				
		@Override
		public void handleMessage(Message msg) {			
			final FragmentLearnDevice theActivity = mActivity.get();
			if(msg.obj!=null){
				ArrayList<DeviceSensor> tmpList = new ArrayList<DeviceSensor>();
				switch (msg.what) {
				case Constant.MAINDEVICE_ID:
					try {
						tmpList = ParseDevice.parseMainDevice(msg.obj.toString());
						theActivity.MainList.clear();
						theActivity.MainList.addAll(tmpList);
						theActivity.dropdownButtonsController.init();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case Constant.MATCHDEVICE_ID:
					try {						
						JSONObject Object =new JSONObject(msg.obj.toString());						
						int status = Object.getInt("status");	
						int match_status = Integer.parseInt(Object.getString("match_status")) ;
						Log.e("MATCHDEVICE_ID", "status:"+status+";match_status:"+match_status);
						if(status>0 && (match_status == 2 || match_status == 4)){	
							tmpList = ParseDevice.parseMatchDevice(msg.obj.toString());
							Log.e("MATCHDEVICE_ID--if 0", msg.obj.toString());
							theActivity.refresh(match_status, tmpList);
						}						
					} catch (JSONException e) {
						Log.d("JSONException", msg.obj.toString());
					}
					break;
				case Constant.REVToHotSpotNet_ID:
					try {
						JSONObject Object =new JSONObject(msg.obj.toString());
	        			String type = Object.getString("type");
	        			String status = Object.getString("status");
	        			if(type.equals("match")){
	        				String uid = Object.getString("uuid");
	        				tmpList = ExecSql.getContainsDevice(uid);
	        				if(status.equals("failed")){
	        					theActivity.matchToOne( tmpList);
	        				}else if(status.equals("succeeded")){
	        					theActivity.refresh();
	        				}
	        			}
					} catch (JSONException e) {
						Log.d("JSONException", msg.obj.toString());
					}					
					break;
				default:
					break;
				}
				
			}		
		}
	};
	
	private class DropdownButtonsController implements DropdownListView.Container {

		private DropdownListView currentDropdownList;
        private List<DropdownItemObject> datasetMainDevice = new ArrayList<>();
		@Override
		public void show(DropdownListView listView) {
			// TODO Auto-generated method stub
			 if (currentDropdownList != null) {
	                currentDropdownList.clearAnimation();
	                currentDropdownList.startAnimation(dropdown_out);
	                currentDropdownList.setVisibility(View.GONE);
	                currentDropdownList.setBottonChecked(false);
	            }
	            currentDropdownList = listView;
	            mask.clearAnimation();
	            mask.setVisibility(View.VISIBLE);
	            currentDropdownList.clearAnimation();
	            currentDropdownList.startAnimation(dropdown_in);
	            currentDropdownList.setVisibility(View.VISIBLE);
	            currentDropdownList.setBottonChecked(true);
		}

		@Override
		public void hide() {
			// TODO Auto-generated method stub
			 if (currentDropdownList != null) {
	                currentDropdownList.clearAnimation();
	                currentDropdownList.startAnimation(dropdown_out);
	                currentDropdownList.setBottonChecked(false);
	                mask.clearAnimation();
	                mask.startAnimation(dropdown_mask_out);
	            }
	            currentDropdownList = null;
		}

		@Override
		public void onSelectionChanged(DropdownListView view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.dropdownMainDevice:
				MainDeviceSelectedId = dropdownMainDevice.getSelectedItemObject().id;
				break;			
			default:
				break;
			}			
		}
		void reset() {
            chooseMainDevice.setChecked(false);

            dropdownMainDevice.setVisibility(View.GONE);
            mask.setVisibility(View.GONE);

            dropdownMainDevice.clearAnimation();
            mask.clearAnimation();
        }
		
		 void init() {
			 reset();
			 int i = 0;
			 int len = MainList.size();
			 datasetMainDevice.clear();
			 if(len>0){
				 for(;i<len;i++){
					 datasetMainDevice.add(new DropdownItemObject(MainList.get(i).getName(), MainList.get(i).getId(), null));
				 }
				 dropdownMainDevice.bind(datasetMainDevice, chooseMainDevice, this, MainDeviceSelectedId);
			 }
			 dropdown_mask_out.setAnimationListener(new Animation.AnimationListener() {
	                @Override
	                public void onAnimationStart(Animation animation) {

	                }

	                @Override
	                public void onAnimationEnd(Animation animation) {
	                    if (currentDropdownList == null) {
	                        reset();
	                    }
	                }

	                @Override
	                public void onAnimationRepeat(Animation animation) {

	                }
	            });
		 }
		
	}
	
}
