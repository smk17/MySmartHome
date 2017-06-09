package com.smk17.mysmarthome.fragment.usersettings;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.smk17.android.tools.ToolAlert;
import com.smk17.android.tools.ToolString;
import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.adapter.FragmentItemAdapter;
import com.smk17.mysmarthome.adapter.FragmentItemAdapter.FIABtnClickListener;
import com.smk17.mysmarthome.cloud.ExecToCloudSql;
import com.smk17.mysmarthome.cloud.ParseDevice;
import com.smk17.mysmarthome.domain.DeviceSensor;
import com.yh.materialdesign.dialog.MaterialDialog;
import com.yh.materialdesign.edittext.MaterialEditText;
import com.yh.materialdesign.views.ButtonFlat;
import com.yh.materialdesign.views.DropdownButton;
import com.yh.materialdesign.views.DropdownItemObject;
import com.yh.materialdesign.views.DropdownListView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class FragmentJurisdiction extends UserFragment{
	
	private ArrayList<DeviceSensor> dList =  new ArrayList<DeviceSensor>();
	private ArrayList<DeviceSensor> dAllList = new ArrayList<DeviceSensor>();
	private ArrayList<DeviceSensor> MainList = new ArrayList<DeviceSensor>();
	private ListView deviceList;	
	private RelativeLayout mRlLoad;
	private SwipeRefreshLayout deviceRefreshable;
	private boolean IsRefreshable = false;
	private FragmentItemAdapter adapter;
	private MaterialEditText mMetName;
	private MaterialEditText mMetPhone;
	private MaterialDialog mMaterialDialog;
	private View mask;
	private DropdownButton chooseMainDevice;
	private DropdownListView dropdownMainDevice;
	private Animation dropdown_in, dropdown_out, dropdown_mask_out;
	private int MainDeviceSelectedId;
	private MyHandler handler = new MyHandler(this);
	private DropdownButtonsController dropdownButtonsController = new DropdownButtonsController();
	
	private FIABtnClickListener fBtnClickListener = new FIABtnClickListener() {
		
		@Override
		public void onFEditBtnClick(DeviceSensor device) {			
		}
		
		@Override
		public void onFDeleteBtnClick(final DeviceSensor device) {
			final MaterialDialog alert = new MaterialDialog(getActivity());
			String msg = "确定要取消该联系人对"+"的权限吗？\n取消后该联系人将无法再控制它。";
			alert.setTitle("删除").setMessage(msg).setCanceledOnTouchOutside(true)
							.setPositiveButton("确定", new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									ExecToCloudSql.deleteJurisdictionRunnable(handler, device.getId());
									alert.dismiss();													
								}
							}).setNegativeButton("取消", new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									alert.dismiss();	
								}
							}).show();
		}
		
		@Override
		public void onFClick(Integer position) {
			AddJurisdiction();
		}
	};
	
	public static final FragmentJurisdiction newInstance(){
		FragmentJurisdiction fragment = new FragmentJurisdiction();
		return fragment;		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_jurisdiction, container, false);  	
		deviceList = (ListView)view.findViewById(R.id.device_list);
		deviceRefreshable = (SwipeRefreshLayout)view.findViewById(R.id.device_refreshable);
		deviceRefreshable.setColorSchemeResources(R.color.title_background);
		mask = view.findViewById(R.id.mask);
		mRlLoad = (RelativeLayout)view.findViewById(R.id.device_load);
        chooseMainDevice = (DropdownButton) view.findViewById(R.id.chooseMainDevice);		
        dropdownMainDevice = (DropdownListView) view.findViewById(R.id.dropdownMainDevice);
        
        dropdown_in = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_in);
        dropdown_out = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_out);
        dropdown_mask_out = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_mask_out);
        dList = new ArrayList<DeviceSensor>();
		adapter = new FragmentItemAdapter(dList, getActivity(),FragmentItemAdapter.TYPE_Jurisdiction ,fBtnClickListener);		 
		deviceList.setAdapter(adapter);		
		MainDeviceSelectedId = MyApplication.getMainDeviceId();
		deviceRefreshable.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				new Thread(){
					@Override
					public void run() {
						IsRefreshable = true;
						try {
							Thread.sleep(Constant.TASKTIME);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}						
						ExecToCloudSql.getJurisdictionRunnable(handler,MainDeviceSelectedId);
						
					}
				}.start();
			}
		});
		mMaterialDialog = new MaterialDialog(getActivity()).setIsShowIMM(true);
		View v = LayoutInflater.from(getActivity()).inflate(R.layout.material_dialog_add_jurisdiction, container,false);
		mMetName = (MaterialEditText)v.findViewById(R.id.met_jurisdiction_name);
		mMetPhone = (MaterialEditText)v.findViewById(R.id.met_jurisdiction_phone);
		((ButtonFlat)v.findViewById(R.id.btn_selector_jurisdiction)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); 		 
				startActivityForResult(intent, 1); 				
			}
		});
		((Button)v.findViewById(R.id.btn_save)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = mMetName.getText().toString();
				String phone = mMetPhone.getText().toString();
				if(ToolString.isNoBlankAndNoNull(name)&& ToolString.isNoBlankAndNoNull(phone)){
					dList.add(new DeviceSensor(0, 0, 0, 0, name, phone, 0, null));
		            adapter.notifyDataSetChanged();
		            ExecToCloudSql.insertJurisdictionRunnable(handler, name, phone, MainDeviceSelectedId);
		            mMaterialDialog.dismiss();
				}else{
					ToolAlert.toastShort(getString(R.string.jurisdiction_null));
				}
			}
		});
		((Button)v.findViewById(R.id.btn_cancel)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMaterialDialog.dismiss();
			}
		});
		mMaterialDialog.setView(v).setCanceledOnTouchOutside(false);
		ExecToCloudSql.getMainDeviceRunnable(handler);		
		return view;
	}
	
	private void AddJurisdiction() {		
		mMaterialDialog.show();			
	}
	
	@Override
	public void onUserAction() {
		AddJurisdiction();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {	
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("onActivityResult", "requestCode="+requestCode);
		switch (requestCode) {
		case 1:			
			if(getActivity() != null && data != null){
				Uri contactData = data.getData(); 
		        Cursor c = getActivity().managedQuery(contactData, null, null, null, null); //	 
		        c.moveToFirst(); 
		        getContactPhone(c);
			}			
			break;

		default:
			break;
		}		
	}

	//获取联系人电话 
	private void getContactPhone(Cursor cursor) 
	{	 
	    int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
	    int phoneNum = cursor.getInt(phoneColumn);  
	    int nameFieldColumnIndex = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
        final String contact = cursor.getString(nameFieldColumnIndex);
        
        final ArrayList<String> arrayList = new ArrayList<String>();
	    //System.out.print(phoneNum); 
	    if (phoneNum > 0) 
	    {
	    	// 获得联系人的ID号 
	        int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID); 
	        String contactId = cursor.getString(idColumn); 
	        // 获得联系人的电话号码的cursor; 
	        Cursor phones = getActivity().getContentResolver().query(
		            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
		            null, 
		            ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = " + contactId,  
		            null, null); 
	      //int phoneCount = phones.getCount(); 
	      //allPhoneNum = new ArrayList<String>(phoneCount); 
	      if (phones.moveToFirst()) 
          {
	    	  // 遍历所有的电话号码 
              for (;!phones.isAfterLast();phones.moveToNext()) 
              {                                             
                  int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER); 
                  String phoneNumber = phones.getString(index); 
                  arrayList.add(phoneNumber);                  
                  Log.e("", "选择的联系人名字："+ contact + "；号码：" + phoneNumber);
              }	                    
              if (!phones.isClosed()) 
              { 
                     phones.close(); 
              }
          }
	      if(arrayList.size() > 1){
	    	  ListView listView = new ListView(getActivity());
	    	  @SuppressWarnings("deprecation")
	    	  Drawable drawable = getResources().getDrawable(R.drawable.settings_selector); 
	    	  listView.setSelector(drawable);
	    	  listView.setDividerHeight(0);
	    	  listView.setVerticalScrollBarEnabled(true);
	    	  listView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item2,arrayList));
	    	  final MaterialDialog alert = new MaterialDialog(getActivity()).setTitle("选择要添加的号码").setContentView(listView);
	    	  listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						alert.dismiss();
						mMetName.setText(contact);
				    	mMetPhone.setText(arrayList.get(arg2));
					}
	    	  });
	    	  alert.setCancelable(true).setCanceledOnTouchOutside(true).show();
			
	      }else if(arrayList.size() == 1){
	    	  mMetName.setText(contact);
	    	  mMetPhone.setText(arrayList.get(0));
	      }
	      
	    }else{
	    	
	    }
	} 
	
	public void ControllerInit(ArrayList<DeviceSensor> tList) {
		MainList.clear();
		MainList.addAll(tList);
	}
	
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
			dList.clear();
			for (DeviceSensor deviceSensor : dAllList) {
				if(MainDeviceSelectedId == Constant.ID_MAINDEVICE_ALL || MainDeviceSelectedId == deviceSensor.getDeviceId())
				dList.add(deviceSensor);
				adapter.notifyDataSetChanged();
			}
			if(dList.size() <=0 ){
				dList.add(new DeviceSensor(null, 0, 0, 0, "暂无联系人", "点击添加新的联系人", 0, null));
				adapter.notifyDataSetChanged();
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

	private static class MyHandler extends Handler {
		WeakReference<FragmentJurisdiction> mFragment;
		public MyHandler(FragmentJurisdiction fragment){
			mFragment = new WeakReference<FragmentJurisdiction>(fragment);
		}
		@Override
		public void handleMessage(Message msg) {
			FragmentJurisdiction theFragment = mFragment.get();
			if(msg.obj !=null){
				ArrayList<DeviceSensor> tList = null;
				switch (msg.what) {		
				case Constant.MAINDEVICE_ID:					
					try {
						tList = ParseDevice.parseMainDevice(msg.obj.toString());
						theFragment.ControllerInit(tList);
						ExecToCloudSql.getJurisdictionRunnable(this,0);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case Constant.Jurisdiction_ID:
					try {
						Log.d("MyHandler", msg.obj.toString());
						tList= ParseDevice.parseJurisdiction(msg.obj.toString());
						theFragment.dList.clear();
						theFragment.dAllList.clear();
						if(tList.size()>0){
							theFragment.dAllList.addAll(tList);
							for (DeviceSensor deviceSensor : tList) {
								if(theFragment.MainDeviceSelectedId == Constant.ID_MAINDEVICE_ALL || 
										theFragment.MainDeviceSelectedId == deviceSensor.getDeviceId()){
									theFragment.dList.add(deviceSensor);
									theFragment.adapter.notifyDataSetChanged();
								}									
							}
							if(theFragment.dList.size()<=0){
								theFragment.dList.add(new DeviceSensor(null, 0, 0, 0, "暂无联系人", "点击添加新的联系人", 0, null));
								theFragment.adapter.notifyDataSetChanged();
							}
						}else{
							theFragment.dList.add(new DeviceSensor(null, 0, 0, 0, "暂无联系人", "点击添加新的联系人", 0, null));
							theFragment.adapter.notifyDataSetChanged();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					theFragment.dropdownButtonsController.init();
					theFragment.mRlLoad.setVisibility(View.GONE);
					theFragment.deviceRefreshable.setVisibility(View.VISIBLE);
					if(theFragment.IsRefreshable){
						msg = new Message();	
						msg.what = Constant.Refreshable_ID;
						msg.obj = 1;
						theFragment.IsRefreshable = false;
						this.sendMessage(msg);
					}
					break;
				case Constant.Refreshable_ID:
					theFragment.deviceRefreshable.setRefreshing(false);
					theFragment.IsRefreshable = false;
					break;
				case Constant.STATUS_ID:
					ExecToCloudSql.getJurisdictionRunnable(this,0);
					break;
				default:
					break;
				}
			}			
			super.handleMessage(msg);
		}	
		
	}
}
