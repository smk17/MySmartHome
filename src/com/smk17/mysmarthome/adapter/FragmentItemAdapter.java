package com.smk17.mysmarthome.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.Utils.BitmapUtil;
import com.smk17.mysmarthome.Utils.HttpUtils;
import com.smk17.mysmarthome.activity.SystemSettingsActivity;
import com.smk17.mysmarthome.cloud.ExecToCloudSql;
import com.smk17.mysmarthome.db.ExecSql;
import com.smk17.mysmarthome.domain.DeviceCategory;
import com.smk17.mysmarthome.domain.DeviceSensor;
import com.yh.materialdesign.dialog.MaterialDialog;
import com.yh.materialdesign.edittext.MaterialEditText;
import com.yh.materialdesign.views.ButtonFloatSmall;
import com.yh.materialdesign.views.CheckBox;
import com.yh.materialdesign.views.LayoutRipple;
import com.yh.materialdesign.views.SlideSwitch;
import com.yh.materialdesign.views.SlideSwitch.SlideListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentItemAdapter extends BaseAdapter {
	private ArrayList<DeviceSensor> list = null;
	private LayoutInflater mInflater;
	private int mType;
//	private boolean isPlay = false;
	private Context context;
	private FIABtnClickListener mFBtnClickListener=null;
	/**
	 * CheckBox �Ƿ�ѡ��Ĵ洢����,key �� position , value �Ǹ�position�Ƿ�ѡ��
	 */
//	@SuppressLint("UseSparseArrays")
//	private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();
	
	//[start]�豸�б��������ȫ�ֳ���
	
	/**
	 * ������Ʋ�����ʽ
	 */
	public static final int TYPE_ZoneControl					= 11;	
	/**
	 * ƥ���ն˵Ĳ�����ʽ
	 */
	public static final int TYPE_MacthDevice 					= 12;
	/**
	 * ��������������ʽ
	 */
	public static final int TYPE_EnvironmentLinkage		= 13;
	/**
	 * �����豸�б�
	 */
	public static final int TYPE_MainDeviceList				= 14;
	/**
	 * �����豸����
	 */
	public static final int TYPE_MainDevice						= 15;
	/**
	 * �ն��豸������
	 */
	public static final int TYPE_TerminalDevice				= 16;
	/**
	 * �༭�������龰ģʽʱ���ն��б�
	 */
	public static final int TYPE_SelectionDevice 				= 17;
	/**
	 * �û�Ȩ�޹���
	 */
	public static final int TYPE_Jurisdiction 					= 18;
	/**
	 * �����¼�豸
	 */
	public static final int TYPE_Login			 					= 19;
	/**
	 * �󶨹���
	 */
	public static final int TYPE_Binding			 				= 20;
	
	//[end]
	
	public FragmentItemAdapter(ArrayList<DeviceSensor> list ,Context context,int type ){
		this.context = context;
		this.mInflater = LayoutInflater.from(context); 
		this.list = list;
		this.mType = type;				
	}
	public FragmentItemAdapter(ArrayList<DeviceSensor> list ,Context context,int type ,
			final FIABtnClickListener fBtnClickListener){
		this.context = context;
		this.mInflater = LayoutInflater.from(context); 
		this.list = list;
		this.mType = type;			
		this.mFBtnClickListener=fBtnClickListener;		
		
	}
	
	/** 
     * ���ð�ť����Ļص� 
     * 
     */  
	public interface FIABtnClickListener  
    {
		void onFClick(Integer position);
        void onFDeleteBtnClick(DeviceSensor device);  
        void onFEditBtnClick(DeviceSensor device);
    }  
	

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {			
		ViewHolder viewHolder = null;
		DeviceSensor device = list.get(position);
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.material_item_fragment, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.rlItemFragment = (RelativeLayout)convertView.findViewById(R.id.rl_item_fragment);
			viewHolder.tvName = (TextView)convertView.findViewById(R.id.tv_name);
			viewHolder.tvReason = (TextView)convertView.findViewById(R.id.tv_reason);
			viewHolder.btnfsAvatar = (ButtonFloatSmall)convertView.findViewById(R.id.btnfs_avatar);
			viewHolder.mdSwitch = (SlideSwitch)convertView.findViewById(R.id.md_switch);
			viewHolder.mdlrPlay = (LayoutRipple)convertView.findViewById(R.id.mdlr_play);
			viewHolder.ivPlay = (ImageView)convertView.findViewById(R.id.iv_play);
			viewHolder.ivDelete = (ImageView)convertView.findViewById(R.id.iv_delete);
			viewHolder.ivEdit = (ImageView)convertView.findViewById(R.id.iv_edit);
			viewHolder.ckSelector = (CheckBox)convertView.findViewById(R.id.cb_selector);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
//		viewHolder.btnfsAvatar.setIsAnime(false);
		initView(viewHolder,device,position,parent);		
//		Log.e("FragmentItemAdapter",  "device id :"+device.getId());
		return convertView;
	}
	/**
	 * ��ʼ����ͼ
	 * @param viewHolder
	 * @param device	�ն��豸����
	 */
	private void initView(final ViewHolder viewHolder,final DeviceSensor device,int position, ViewGroup parent){
		//TODO  ��ʼ����ͼ
		ImageView icon = viewHolder.btnfsAvatar.getIcon();		
		//initViewToDeviceType(viewHolder,device);		
		icon.setBackgroundResource(Constant.DeviceCategoryIcon[device.getCategoryId()]);			
		viewHolder.btnfsAvatar.setIcon(icon);
		viewHolder.btnfsAvatar.setEnabledAnime(false);
		viewHolder.btnfsAvatar.setBackgroundColor(Color.parseColor( Constant.SelectorBackground[device.getBackground()]));
		viewHolder.tvName.setText(device.getName());
		switch (mType) {		
		case TYPE_MainDeviceList:
			initViewMainDeviceList(viewHolder,device,position);
			break;		
		case TYPE_EnvironmentLinkage:
			initViewToEnvironmentLinkage(viewHolder,device);
			break;		
		case TYPE_ZoneControl:
			initViewToZoneControl(viewHolder,device);
			break;
		case TYPE_MainDevice:
			initViewMainDevice(viewHolder,device,position,parent);
			break;
		case TYPE_TerminalDevice:
			initViewTerminalDevice(viewHolder,device,position);
			break;
		case TYPE_MacthDevice:
			initViewMacthDevice(viewHolder,device,position);
			break;
		case TYPE_SelectionDevice:
			initViewSelectionDevice(viewHolder,device,position);
			break;
		case TYPE_Jurisdiction:
			initViewJurisdiction(viewHolder,device,position);
			break;
		case TYPE_Login:
			initViewLogin(viewHolder,device,position);
			break;
		case TYPE_Binding:
			initViewBinding(viewHolder,device,position);
			break;
		default:		
			break;
		}
		final SharedPreferences sp = context.getSharedPreferences(Constant.INFO_THEME ,Activity.MODE_PRIVATE);
		final int theme_id = sp.getInt(Constant.THEME_ID, 1);
		switch (theme_id) {
		case 1:
			viewHolder.tvName.setTextColor(context.getResources().getColor(R.color.black));
			viewHolder.tvReason.setTextColor(context.getResources().getColor(R.color.black));
			break;
		case 2:case 3:case 4:case 5:case 6:
			viewHolder.tvName.setTextColor(context.getResources().getColor(R.color.white));
			viewHolder.tvReason.setTextColor(context.getResources().getColor(R.color.white));
			break;
		default:
			break;
		}
		
	}
	
	private void initViewBinding(ViewHolder viewHolder, DeviceSensor device,
			int position) {
		// TODO ��ʼ���󶨹���
		viewHolder.ckSelector.setVisibility(View.GONE);
		viewHolder.ivDelete.setVisibility(View.GONE);
		viewHolder.ivEdit.setVisibility(View.GONE);
		viewHolder.ivPlay.setVisibility(View.GONE);
		viewHolder.mdlrPlay.setVisibility(View.GONE);
		viewHolder.mdSwitch.setVisibility(View.GONE);
		viewHolder.btnfsAvatar.setVisibility(View.VISIBLE);
		viewHolder.tvReason.setVisibility(View.VISIBLE);
		viewHolder.tvName.setVisibility(View.VISIBLE);
		viewHolder.tvName.setText(device.getName());
		viewHolder.tvReason.setText(device.getUid());
		ImageView icon = viewHolder.btnfsAvatar.getIcon();
		icon.setBackgroundResource(Constant.BindingIcon[device.getCategoryId()]);			
		viewHolder.btnfsAvatar.setIcon(icon);
		viewHolder.btnfsAvatar.setEnabledAnime(false);
		viewHolder.btnfsAvatar.setBackgroundColor(Color.parseColor( "#00000000"));
	}
	private void initViewLogin(ViewHolder viewHolder, DeviceSensor device,
			int position) {
		// TODO ��ʼ�������¼�豸
		viewHolder.ckSelector.setVisibility(View.GONE);
		viewHolder.ivDelete.setVisibility(View.GONE);
		viewHolder.ivEdit.setVisibility(View.GONE);
		viewHolder.ivPlay.setVisibility(View.GONE);
		viewHolder.mdlrPlay.setVisibility(View.GONE);
		viewHolder.mdSwitch.setVisibility(View.GONE);
		viewHolder.btnfsAvatar.setVisibility(View.GONE);
		viewHolder.tvReason.setVisibility(View.VISIBLE);
		viewHolder.tvName.setVisibility(View.VISIBLE);
		viewHolder.tvName.setText(device.getName());
		viewHolder.tvReason.setText(device.getUid());		
	}
	private void initViewJurisdiction(final ViewHolder viewHolder,
			final DeviceSensor device, final int position) {
		// TODO ��ʼ��Ȩ�޹���
		viewHolder.ckSelector.setVisibility(View.GONE);
		viewHolder.ivDelete.setVisibility(View.VISIBLE);
		viewHolder.ivEdit.setVisibility(View.GONE);
		viewHolder.ivPlay.setVisibility(View.GONE);
		viewHolder.mdlrPlay.setVisibility(View.GONE);
		viewHolder.mdSwitch.setVisibility(View.GONE);
		viewHolder.btnfsAvatar.setVisibility(View.GONE);
		viewHolder.tvReason.setVisibility(View.VISIBLE);
		viewHolder.tvName.setVisibility(View.VISIBLE);
		viewHolder.tvName.setText(device.getName());
		viewHolder.tvReason.setText("�ֻ��ţ�"+device.getUid());
		viewHolder.rlItemFragment.setOnClickListener(null);
		if(device.getId() == null){
			viewHolder.ivDelete.setVisibility(View.GONE);
			viewHolder.tvReason.setText(device.getUid());
			viewHolder.rlItemFragment.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(mFBtnClickListener !=null)
						mFBtnClickListener.onFClick(position);
				}
			});
		}		
		viewHolder.ivDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mFBtnClickListener !=null)
					mFBtnClickListener.onFDeleteBtnClick(device);				
			}
		});
	}
	private void initViewSelectionDevice(final ViewHolder viewHolder,
			final DeviceSensor device, final int position) {
		// TODO ��ʼ���༭���������龰ģʽ����		
		viewHolder.ckSelector.setVisibility(View.GONE);
		viewHolder.ivDelete.setVisibility(View.GONE);
		viewHolder.ivEdit.setVisibility(View.GONE);
		viewHolder.ivPlay.setVisibility(View.GONE);
		viewHolder.mdlrPlay.setVisibility(View.GONE);
		viewHolder.mdSwitch.setVisibility(View.GONE);
		if(device.getSelectMode()){
			viewHolder.ckSelector.setVisibility(View.VISIBLE);
			RelativeLayout.LayoutParams params = 
				    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				        RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
			params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
			params.rightMargin = BitmapUtil.dip(context,12);		
			viewHolder.ckSelector.setLayoutParams(params);
			
			params =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				        RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			params.leftMargin = BitmapUtil.dip(context,12);		
			viewHolder.tvName.setLayoutParams(params);
			viewHolder.tvName.setTextSize(18);
			if(device.getCheck()){
				viewHolder.ckSelector.setChecked(true);
			}else{
				viewHolder.ckSelector.setChecked(false);
			}
			
			viewHolder.ckSelector.setOncheckListener(new CheckBox.OnCheckListener() {
				
				@Override
				public void onCheck(boolean check) {
					device.setCheck(check);
					list.get(position).setCheck(check);
				}
			});
			viewHolder.rlItemFragment.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					viewHolder.ckSelector.setChecked(!viewHolder.ckSelector.isCheck());
					device.setCheck(viewHolder.ckSelector.isCheck());
					list.get(position).setCheck(viewHolder.ckSelector.isCheck());
				}
			});
		}else{			
			initViewToSelectionDeviceDeviceType(viewHolder,device,position);
			viewHolder.ckSelector.setVisibility(View.VISIBLE);
			RelativeLayout.LayoutParams params = 
				    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				        RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			viewHolder.ckSelector.setLayoutParams(params);
			
			params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				        RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			params.addRule(RelativeLayout.RIGHT_OF, viewHolder.ckSelector.getId());
			params.leftMargin = BitmapUtil.dip(context,12);		
			viewHolder.tvName.setLayoutParams(params);			
			if(device.getCheck()){
				viewHolder.ckSelector.setChecked(true);
			}else{
				viewHolder.ckSelector.setChecked(false);
			}
			
			viewHolder.ckSelector.setOncheckListener(new CheckBox.OnCheckListener() {
				
				@Override
				public void onCheck(boolean check) {
					device.setCheck(check);
					list.get(position).setCheck(check);
				}
			});
		}
	}
	
	private void initViewToSelectionDeviceDeviceType(final ViewHolder viewHolder,
			final DeviceSensor device, final int position) {
		switch (device.getCategoryId()) {
		case DeviceCategory.TYPE_Strips:
			viewHolder.ckSelector.setVisibility(View.GONE);
			viewHolder.ivDelete.setVisibility(View.GONE);
			viewHolder.ivEdit.setVisibility(View.GONE);
			viewHolder.ivPlay.setVisibility(View.GONE);
			viewHolder.mdlrPlay.setVisibility(View.GONE);
			viewHolder.mdSwitch.setVisibility(View.GONE);
			viewHolder.tvName.setVisibility(View.VISIBLE);
			viewHolder.rlItemFragment.setOnClickListener(null);
			break;
		case DeviceCategory.TYPE_Socket:
			viewHolder.ckSelector.setVisibility(View.GONE);
			viewHolder.ivDelete.setVisibility(View.GONE);
			viewHolder.ivEdit.setVisibility(View.GONE);
			viewHolder.ivPlay.setVisibility(View.GONE);
			viewHolder.mdlrPlay.setVisibility(View.GONE);
			viewHolder.mdSwitch.setVisibility(View.GONE);
			viewHolder.tvName.setVisibility(View.VISIBLE);		
			viewHolder.rlItemFragment.setOnClickListener(null);
			break;
		case DeviceCategory.TYPE_DimmableLights:
			viewHolder.ckSelector.setVisibility(View.GONE);
			viewHolder.ivDelete.setVisibility(View.GONE);
			viewHolder.ivEdit.setVisibility(View.GONE);
			viewHolder.ivPlay.setVisibility(View.GONE);
			viewHolder.mdlrPlay.setVisibility(View.GONE);
			viewHolder.mdSwitch.setVisibility(View.GONE);
			viewHolder.tvName.setVisibility(View.VISIBLE);
			break;
		case DeviceCategory.TYPE_Switch:			
			viewHolder.ckSelector.setVisibility(View.GONE);
			viewHolder.ivDelete.setVisibility(View.GONE);
			viewHolder.ivEdit.setVisibility(View.GONE);
			viewHolder.ivPlay.setVisibility(View.GONE);
			viewHolder.mdlrPlay.setVisibility(View.GONE);
			viewHolder.mdSwitch.setVisibility(View.VISIBLE);
			viewHolder.tvName.setVisibility(View.VISIBLE);
			initViewToShowSwitch(viewHolder,device);	
			viewHolder.mdSwitch.setSlideListener(new SlideListener() {
				
				@Override
				public void onCheck(boolean check) {
					if(check){
						for (int i=0;i<device.getpList().size();i++){
							if(device.getpList().get(i).getName().equals("����")){
								device.getpList().get(i).setValue("1");
								list.get(position).getpList().get(i).setValue("1");
								viewHolder.tvReason.setText(device.pListToString());
							}
						}
					}else{
						for (int i=0;i<device.getpList().size();i++){
							if(device.getpList().get(i).getName().equals("����")){
								device.getpList().get(i).setValue("0");
								list.get(position).getpList().get(i).setValue("0");
								viewHolder.tvReason.setText(device.pListToString());
							}
						}
					}
				}
			});
			break;
		case 0:
			break;
		default:
			break;
		}
		
	}
	
	
	private void initViewMacthDevice(final ViewHolder viewHolder,final DeviceSensor device,final int position){
		//TODO  ��ʼ��ѧϰ�ն˲�����ʽ����ͼ
		RelativeLayout.LayoutParams params = 
			    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
			        RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		params.leftMargin = BitmapUtil.dip(context,12);		
		viewHolder.ivEdit.setVisibility(View.VISIBLE);
		viewHolder.tvName.setLayoutParams(params);
		params = (RelativeLayout.LayoutParams)viewHolder.ivDelete.getLayoutParams();
		params.removeRule(RelativeLayout.LEFT_OF);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		viewHolder.ivEdit.setLayoutParams(params);
		viewHolder.tvName.setTextSize(18);
		viewHolder.tvName.setText(device.getName());
		viewHolder.ivEdit.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("InflateParams")
			@Override
			public void onClick(View v) {
				final MaterialDialog mMaterialDialog = new MaterialDialog(context);
				View view = LayoutInflater.from(context).inflate(R.layout.material_dialog_update, null);
				final MaterialEditText metDeviveName = (MaterialEditText)view.findViewById(R.id.met_device_name);
				metDeviveName.setText(device.getName());
				((Button)view.findViewById(R.id.btn_save)).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						device.setName(metDeviveName.getText().toString());
						if(mFBtnClickListener !=null)
							mFBtnClickListener.onFEditBtnClick(device);
					}
				});
				((Button)view.findViewById(R.id.btn_cancel)).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mMaterialDialog.dismiss();						
					}
				});
				mMaterialDialog.setView(view).setCanceledOnTouchOutside(true).show();				
			}
		});

	}
	/**
	 * ����ѡ���б�
	 * @param viewHolder
	 * @param device
	 * @param position
	 */	
	private void initViewMainDeviceList(final ViewHolder viewHolder,final DeviceSensor device,final int position){
		//TODO  ��ʼ������ѡ���б�����ʽ����ͼ
		RelativeLayout.LayoutParams params = 
			    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
			        RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.RIGHT_OF,viewHolder.btnfsAvatar.getId());
		params.leftMargin = BitmapUtil.dip(context,12);
		viewHolder.tvName.setLayoutParams(params);
		viewHolder.btnfsAvatar.setVisibility(View.VISIBLE);
		viewHolder.tvName.setTextSize(18);
		viewHolder.tvName.setText(device.getName());
		viewHolder.rlItemFragment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyApplication.setMainDeviceId(device.getId());
				Log.d("MainDeviceId", "MainDeviceId:"+device.getId());
				if(mFBtnClickListener !=null)
					mFBtnClickListener.onFClick(device.getId());
			}
		});
	}
	
	/**
	 * ��ʼ�������豸��������ʽ����ͼ
	 * @param viewHolder
	 * @param device	
	 */
	private void initViewMainDevice(final ViewHolder viewHolder,final DeviceSensor device,final int position,final ViewGroup parent){
		//TODO  ��ʼ�����ع�������ʽ����ͼ
		RelativeLayout.LayoutParams params = 
			    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
			        RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		params.leftMargin = BitmapUtil.dip(context,12);
		viewHolder.tvName.setLayoutParams(params);
		viewHolder.tvName.setTextSize(18);
		viewHolder.tvName.setText(device.getName());
		viewHolder.ivEdit.setVisibility(View.VISIBLE);
		if(!MyApplication.getNetMode().equals(Constant.HotSpot_Mode)){
			viewHolder.ivDelete.setVisibility(View.VISIBLE);
		}			
		else{
			 params =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
					        RelativeLayout.LayoutParams.WRAP_CONTENT);
			 params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			 params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
			 params.rightMargin = BitmapUtil.dip(context,12);
			 viewHolder.ivEdit.setLayoutParams(params);
		}				
		viewHolder.ivEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final MaterialDialog mMaterialDialog = new MaterialDialog(context);
				View view = LayoutInflater.from(context).inflate(R.layout.material_dialog_update, parent,false);
				final MaterialEditText metDeviveName = (MaterialEditText)view.findViewById(R.id.met_device_name);
				metDeviveName.setText(device.getName());
				((Button)view.findViewById(R.id.btn_save)).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						device.setName(metDeviveName.getText().toString());
						if(mFBtnClickListener !=null)
							mFBtnClickListener.onFEditBtnClick(device);
						mMaterialDialog.dismiss();
					}
				});
				((Button)view.findViewById(R.id.btn_cancel)).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mMaterialDialog.dismiss();
					}
				});
				mMaterialDialog.setView(view).setIsShowIMM(true).setCanceledOnTouchOutside(true).show();	
			}
		});
		viewHolder.ivDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final MaterialDialog alert = new MaterialDialog(context);
				String msg = "ȷ��Ҫ������Ӹ�������\n���������ؽ�����ƥ��ģʽ��";
				msg += "��Ҳ���������������";
				alert.setTitle("�������").setMessage(msg).setCanceledOnTouchOutside(true)
								.setPositiveButton(R.string.ok, new OnClickListener() {
									
									@Override
									public void onClick(View v) {
//										list.remove(position);
//										notifyDataSetChanged();
										if(mFBtnClickListener !=null)
											mFBtnClickListener.onFDeleteBtnClick(device);
										alert.dismiss();													
									}
								}).setNegativeButton(R.string.cancel_name, new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										alert.dismiss();	
									}
								}).setOnDismissListener(new OnDismissListener() {
									
									@Override
									public void onDismiss(DialogInterface dialog) {
										notifyDataSetChanged();
									}
								}).show();
			}
		});		

	}
	
	/**
	 * ��ʼ���ն��豸��������ʽ����ͼ
	 * @param viewHolder
	 * @param device	
	 */
	private void initViewTerminalDevice(final ViewHolder viewHolder,final DeviceSensor device,final int position){
		//TODO  ��ʼ���ն˹�������ʽ����ͼ
		
		if(device.getId() == 0){
//			viewHolder.tvReason.setVisibility(View.VISIBLE);
			viewHolder.tvName.setText(device.getName()+"("+ device.getUid()  +")");
			viewHolder.ivEdit.setVisibility(View.GONE);
			viewHolder.ivDelete.setVisibility(View.GONE);
			viewHolder.rlItemFragment.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO ����µ��ն�
					final MaterialDialog alert = new MaterialDialog(context);
					String msg = "����Ҫ����µ��ն���\n����ն�ֻ�������ؽ���ƥ��ģʽ����������Ҫ��ӵ��ն�ͨ�硣\n"
							+ "׼���ú�����ȷ��������ƥ����������ӡ�";
					alert.setTitle("���").setMessage(msg).setCanceledOnTouchOutside(true)
									.setPositiveButton("ȷ��", new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											Intent intent = new Intent();
											intent.putExtra("MainDeviceId", device.getDeviceId());
											intent.setClass(context, SystemSettingsActivity.class);
											intent.putExtra("Page",SystemSettingsActivity.Page_LearnDevice);
											context.startActivity(intent);
											((Activity)context).overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);
											alert.dismiss();	
										}
									}).setNegativeButton("ȡ��", new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											alert.dismiss();	
										}
									}).setOnDismissListener(new OnDismissListener() {
										
										@Override
										public void onDismiss(DialogInterface dialog) {
											notifyDataSetChanged();
										}
									}).show();
				}
			});
		}else{
			RelativeLayout.LayoutParams params = 
				    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				        RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			params.leftMargin = BitmapUtil.dip(context,12);		
			viewHolder.tvName.setLayoutParams(params);
			viewHolder.tvName.setTextSize(18);
			viewHolder.rlItemFragment.setOnClickListener(null);
			viewHolder.tvName.setText(device.getName());
			if(device.getSelectMode()){
				viewHolder.ivEdit.setVisibility(View.GONE);
				viewHolder.ivDelete.setVisibility(View.GONE);
				viewHolder.ckSelector.setVisibility(View.VISIBLE);
				if(device.getCheck()){
					viewHolder.ckSelector.setChecked(true);
				}else{
					viewHolder.ckSelector.setChecked(false);
				}
				
				viewHolder.ckSelector.setOncheckListener(new CheckBox.OnCheckListener() {
					
					@Override
					public void onCheck(boolean check) {
						device.setCheck(check);
						list.get(position).setCheck(check);
					}
				});
				viewHolder.rlItemFragment.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						viewHolder.ckSelector.setChecked(!viewHolder.ckSelector.isCheck());
						device.setCheck(viewHolder.ckSelector.isCheck());
						list.get(position).setCheck(viewHolder.ckSelector.isCheck());
					}
				});
			}else{
				viewHolder.ivEdit.setVisibility(View.VISIBLE);
				viewHolder.ivDelete.setVisibility(View.VISIBLE);
				viewHolder.ivEdit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(mFBtnClickListener !=null)
							mFBtnClickListener.onFEditBtnClick(device);
						
					}
				});
				viewHolder.ivDelete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						final MaterialDialog alert = new MaterialDialog(context);
						String msg = "ȷ��Ҫ������Ӹ��ն���\n�������ն˽�����ƥ��ģʽ��";
						msg += "��Ҳ�������������ؽ���ƥ��ģʽ�������";
						alert.setTitle("�������").setMessage(msg).setCanceledOnTouchOutside(true)
										.setPositiveButton("ȷ��", new OnClickListener() {
											
											@Override
											public void onClick(View v) {												
												if(mFBtnClickListener !=null)
													mFBtnClickListener.onFDeleteBtnClick(device);
												alert.dismiss();	
											}
										}).setNegativeButton("ȡ��", new OnClickListener() {
											
											@Override
											public void onClick(View v) {
												alert.dismiss();	
											}
										}).setOnDismissListener(new OnDismissListener() {
											
											@Override
											public void onDismiss(DialogInterface dialog) {
												notifyDataSetChanged();
											}
										}).show();
					}
				});	
			}
			viewHolder.rlItemFragment.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					if(!device.getSelectMode()){
						int len = list.size();
						for(int i = 0; i <len;i++){
							list.get(i).setSelectMode(true);
							notifyDataSetChanged();
						}
						mFBtnClickListener.onFClick(position);
					}				
					return true;
				}
			});
		}
		
		

	}
	/**
	 * ��ʼ����������������ʽ����ͼ
	 * @param viewHolder
	 * @param device	�ն��豸����
	 */
	private void initViewToEnvironmentLinkage(final ViewHolder viewHolder,final DeviceSensor device){
		//TODO  ��ʼ����������������ʽ����ͼ
		viewHolder.btnfsAvatar.setVisibility(View.VISIBLE);
		viewHolder.tvReason.setVisibility(View.VISIBLE);
		viewHolder.tvReason.setText(device.pListToString());
	}
	
	
	/**
	 * ��ʼ��������Ʋ�����ʽ����ͼ
	 * @param viewHolder
	 * @param device	�ն��豸����
	 */
	private void initViewToZoneControl(final ViewHolder viewHolder,final DeviceSensor device){
		//TODO  ��ʼ��������Ʋ�����ʽ����ͼ
		RelativeLayout.LayoutParams params = 
			    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
			        RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.RIGHT_OF, viewHolder.btnfsAvatar.getId());
		params.leftMargin = BitmapUtil.dip(context,12) ;
		params.topMargin = BitmapUtil.dip(context,12);
		viewHolder.tvName.setLayoutParams(params);
		viewHolder.btnfsAvatar.setVisibility(View.VISIBLE);
		viewHolder.tvReason.setVisibility(View.VISIBLE);
//		viewHolder.tvName.setTextSize(dip(context,16));
//		viewHolder.tvReason.setTextSize(dip(context,14));
		viewHolder.tvReason.setText(device.pListToString());
		initViewToZoneControlDeviceType(viewHolder,device);
	}
	
	/**
	 * ��ʼ��������Ʋ�����ʽ����ͼ���ٸ����ն����ͽ������Ӧ�ĳ�ʼ��
	 * @param viewHolder
	 * @param device	�ն��豸����
	 */
	private void initViewToZoneControlDeviceType(final ViewHolder viewHolder,final DeviceSensor device){
		//TODO  ��ʼ��������Ʋ�����ʽ����ͼ���ٸ����ն����ͽ������Ӧ�ĳ�ʼ��		
		switch (device.getCategoryId()) {
		case DeviceCategory.TYPE_Strips:
			viewHolder.ckSelector.setVisibility(View.GONE);
			viewHolder.ivDelete.setVisibility(View.GONE);
			viewHolder.ivEdit.setVisibility(View.GONE);
			viewHolder.ivPlay.setVisibility(View.GONE);
			viewHolder.mdlrPlay.setVisibility(View.GONE);
			viewHolder.mdSwitch.setVisibility(View.GONE);
			viewHolder.tvName.setVisibility(View.VISIBLE);
			viewHolder.rlItemFragment.setOnClickListener(null);
			break;
		case DeviceCategory.TYPE_Socket:
			viewHolder.ckSelector.setVisibility(View.GONE);
			viewHolder.ivDelete.setVisibility(View.GONE);
			viewHolder.ivEdit.setVisibility(View.GONE);
			viewHolder.ivPlay.setVisibility(View.GONE);
			viewHolder.mdlrPlay.setVisibility(View.GONE);
			viewHolder.mdSwitch.setVisibility(View.GONE);
			viewHolder.tvName.setVisibility(View.VISIBLE);		
			viewHolder.rlItemFragment.setOnClickListener(null);
			break;
		case DeviceCategory.TYPE_DimmableLights:
			viewHolder.ckSelector.setVisibility(View.GONE);
			viewHolder.ivDelete.setVisibility(View.GONE);
			viewHolder.ivEdit.setVisibility(View.GONE);
			viewHolder.ivPlay.setVisibility(View.GONE);
			viewHolder.mdlrPlay.setVisibility(View.GONE);
			viewHolder.mdSwitch.setVisibility(View.VISIBLE);
			viewHolder.tvName.setVisibility(View.VISIBLE);
			initViewToShowSwitch(viewHolder,device);		
			viewHolder.mdSwitch.setSlideListener(null);
			break;
		case DeviceCategory.TYPE_Switch:			
			viewHolder.ckSelector.setVisibility(View.GONE);
			viewHolder.ivDelete.setVisibility(View.GONE);
			viewHolder.ivEdit.setVisibility(View.GONE);
			viewHolder.ivPlay.setVisibility(View.GONE);
			viewHolder.mdlrPlay.setVisibility(View.GONE);
			viewHolder.mdSwitch.setVisibility(View.VISIBLE);
			viewHolder.tvName.setVisibility(View.VISIBLE);
			initViewToShowSwitch(viewHolder,device);				
			switch (MyApplication.getNetMode()) {
			case Constant.Network_Mode:
				//TODO ����ģʽ�µĳ�ʼ��		
				viewHolder.rlItemFragment.setOnClickListener(new OnClickListener() {
					
					private void Check(boolean check,boolean millis){
						if(check){
							for (int i=0;i<device.getpList().size();i++){
								if(device.getpList().get(i).getName().equals("����")){
									device.getpList().get(i).setValue("1");
									viewHolder.tvReason.setText(device.pListToString());	
									if(millis){
										//if(device.getIsRefreshable())
											ExecToCloudSql.UpdateDevicePropertyRunnable(device.getpList().get(i));
										//else device.setIsRefreshable(true);
									}
								}
							}
						}else{
							for (int i=0;i<device.getpList().size();i++){
								if(device.getpList().get(i).getName().equals("����")){
									device.getpList().get(i).setValue("0");
									viewHolder.tvReason.setText(device.pListToString());
									if(millis){
										//if(device.getIsRefreshable())
											ExecToCloudSql.UpdateDevicePropertyRunnable(device.getpList().get(i));
										//else device.setIsRefreshable(true);
									}
								}
							}
						}
					}
					
					@Override
					public void onClick(View v) {
						if(viewHolder.mdSwitch.isCheck()){
							viewHolder.mdSwitch.setState(false);
						}else{
							viewHolder.mdSwitch.setState(true);
						}
						
						long newTimeMillis = System.currentTimeMillis();
						//Log.e("mdSwitch", "newTimeMillis="+newTimeMillis+";timeMillis="+device.getTimeMillis());
						if(newTimeMillis - device.getTimeMillis() > 1000 || device.count > 5){
							Check(viewHolder.mdSwitch.isCheck(),true);
							if(device.count > 5){
								device.count = 0;
							}else{
								device.count += 1;
							}
						}else{
							Check(viewHolder.mdSwitch.isCheck(),false);
						}
						device.setTimeMillis(newTimeMillis);						
					}
				});
				break;
			case Constant.Intranet_Mode:
				//TODO ����ģʽ�µĳ�ʼ��
				viewHolder.rlItemFragment.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(viewHolder.mdSwitch.isCheck()){
							viewHolder.mdSwitch.setState(false);
						}else{
							viewHolder.mdSwitch.setState(true);
						}
						
					}
				});
				viewHolder.mdSwitch.setSlideListener(new SlideListener() {
					
					@Override
					public void onCheck(boolean check) {
						if(check){
							for (int i=0;i<device.getpList().size();i++){
								if(device.getpList().get(i).getName().equals("����")){
									device.getpList().get(i).setValue("1");
									viewHolder.tvReason.setText(device.pListToString());								
								}
							}
						}else{
							for (int i=0;i<device.getpList().size();i++){
								if(device.getpList().get(i).getName().equals("����")){
									device.getpList().get(i).setValue("0");
									viewHolder.tvReason.setText(device.pListToString());
								}
							}
						}
					}
				});
				break;
			case Constant.HotSpot_Mode:
				//TODO �ȵ�ģʽ�µĳ�ʼ��
				viewHolder.mdSwitch.setSlideListener(new SlideListener() {
					
					@Override
					public void onCheck(boolean check) {
						if(check){
							device.setAllPropertyValue(1);							
						}else{
							device.setAllPropertyValue(0);
						}
						viewHolder.tvReason.setText(device.pListToString());
						try {
							ExecSql.updateDevice(device);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				break;
			default:
				break;
			}
			
			break;
		case 0:
			viewHolder.rlItemFragment.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO ����µ��ն�
					final MaterialDialog alert = new MaterialDialog(context);
					String msg = "����Ҫ����µ��ն���\n����ն�ֻ�������ؽ���ƥ��ģʽ����������Ҫ��ӵ��ն�ͨ�硣\n"
							+ "׼���ú�����ȷ��������ƥ����������ӡ�";
					alert.setTitle("���").setMessage(msg).setCanceledOnTouchOutside(true)
									.setPositiveButton("ȷ��", new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											Intent intent = new Intent();
											intent.putExtra("MainDeviceId", device.getDeviceId());
											intent.setClass(context, SystemSettingsActivity.class);
											intent.putExtra("Page",SystemSettingsActivity.Page_LearnDevice);
											context.startActivity(intent);
											((Activity)context).overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);
											alert.dismiss();	
										}
									}).setNegativeButton("ȡ��", new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											alert.dismiss();	
										}
									}).setOnDismissListener(new OnDismissListener() {
										
										@Override
										public void onDismiss(DialogInterface dialog) {
											notifyDataSetChanged();
										}
									}).show();
				}
			});
			break;
		default:
			viewHolder.mdSwitch.setVisibility(View.VISIBLE);
			initViewToShowPlay(viewHolder);
			break;
		}
	}
	private void  initViewToShowSwitch(final ViewHolder viewHolder,final DeviceSensor device){
		int i = 0;
		for (i=0;i<device.getpList().size();i++){
			if(device.getpList().get(i).getName().equals("����")){
				if(device.getpList().get(i).getValue().equals("1")){
					viewHolder.mdSwitch.setState(true);
				}else if(device.getpList().get(i).getValue().equals("0")){
					viewHolder.mdSwitch.setState(false);
				}
			}
		}	
	}
	
	/**
	 * �����ն����ͽ������Ӧ�ĳ�ʼ��
	 * @param viewHolder
	 * @param device	�ն��豸����
	 */
//	private void initViewToDeviceType(final ViewHolder viewHolder,final DeviceSensor device){
//		//TODO  �����ն����ͽ������Ӧ�ĳ�ʼ��
//		ImageView icon = null;
//		switch (device.getCategoryId()) {
//		case DeviceCategory.TYPE_Master:
//			icon = viewHolder.btnfsAvatar.getIcon();
//			icon.setBackgroundResource(R.drawable.ic_router);			
//			viewHolder.btnfsAvatar.setIcon(icon);
//			viewHolder.btnfsAvatar.setEnabledAnime(false);
//			viewHolder.btnfsAvatar.setBackgroundColor(context.getResources().getColor(R.color.router_background));
//			break;
//		case DeviceCategory.TYPE_TempHumi:
//			icon = viewHolder.btnfsAvatar.getIcon();
//			icon.setBackgroundResource(R.drawable.ic_temphumi);
//			viewHolder.btnfsAvatar.setIcon(icon);
//			viewHolder.btnfsAvatar.setBackgroundColor(Color.parseColor("#B3001E"));
//			viewHolder.btnfsAvatar.setEnabledAnime(false);
//			break;
//		case DeviceCategory.TYPE_PM2_5:
//			icon = viewHolder.btnfsAvatar.getIcon();
//			icon.setBackgroundColor(Color.parseColor("#00000000"));
//			viewHolder.btnfsAvatar.setIcon(icon);
//			viewHolder.btnfsAvatar.setText("PM");
//			viewHolder.btnfsAvatar.setTextColor(Color.parseColor("#FFFFFF"));
//			viewHolder.btnfsAvatar.setBackgroundColor(Color.parseColor("#77D577"));
//			viewHolder.btnfsAvatar.setEnabledAnime(false);
//			break;
//		case DeviceCategory.TYPE_PIR:
//			icon = viewHolder.btnfsAvatar.getIcon();
//			icon.setBackgroundResource(R.drawable.ic_pir);
//			viewHolder.btnfsAvatar.setIcon(icon);
//			viewHolder.btnfsAvatar.setBackgroundColor(Color.parseColor("#BB5994"));
//			viewHolder.btnfsAvatar.setEnabledAnime(false);
//			break;
//		case DeviceCategory.TYPE_Switch:
//			icon = viewHolder.btnfsAvatar.getIcon();
//			icon.setBackgroundResource(R.drawable.ic_switch);			
//			viewHolder.btnfsAvatar.setIcon(icon);
//			viewHolder.btnfsAvatar.setBackgroundColor(Color.parseColor("#0770B1"));
//			viewHolder.btnfsAvatar.setEnabledAnime(false);
//			break;
//		case 0:
//			icon = viewHolder.btnfsAvatar.getIcon();
//			icon.setBackgroundResource(R.drawable.ic_add_white_48dp);			
//			viewHolder.btnfsAvatar.setIcon(icon);
//			viewHolder.btnfsAvatar.setBackgroundColor(context.getResources().getColor(R.color.orangered));			
//			break;
//		default:
//			
//			break;
//		}
//	}
	/**
	 * ������ͣ����
	 * @param viewHolder
	 * @param device	�ն��豸����
	 */
	private void initViewToShowPlay(final ViewHolder viewHolder){
		//TODO  ������ͣ����
		viewHolder.mdlrPlay.setVisibility(View.VISIBLE);
		viewHolder.mdlrPlay.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				viewHolder.tvReason.setText("״̬����ͣ");
			}
		});
	}
	
	public static final class ViewHolder{
		RelativeLayout rlItemFragment;
		ButtonFloatSmall btnfsAvatar;
		TextView tvName;
		TextView tvReason;
		SlideSwitch mdSwitch;
		LayoutRipple mdlrPlay;
		ImageView ivPlay;
		ImageView ivDelete;
		ImageView ivEdit;
		CheckBox ckSelector;
	}

	
	public static int adjustFontSize(Context context,int pxValue) {  
		//��ȡ��ǰ�豸����Ļ��С
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		//�������㿪��ʱ�趨����Ļ��С���ݺ��(��������㿪��ʱ������Ļ��С��480*800)
		int screenWidth = displayMetrics.widthPixels;
		int screenHeight = displayMetrics.heightPixels;
		float ratioWidth = (float)screenWidth / 480;
		float ratioHeight = (float)screenHeight / 800;
		        
		float RATIO = Math.min(ratioWidth, ratioHeight);
//		if (ratioWidth != ratioHeight) {
//		    if (RATIO == ratioWidth) {
//		    OFFSET_TOP = Math.round((screenHeight - 800 * RATIO) / 2);
//		    }else {
//		    OFFSET_LEFT = Math.round((screenWidth - 480 * RATIO) / 2);
//		    }
//		}

		//������һ�������������С�ݺ����ȷ������Ĵ�С(�ٶ���480*800��Ļ�������С�趨Ϊ35)

//		int TEXT_SIZE = Math.round(pxValue * RATIO);

		//������һ������������С���趨Ӧ�ó���������Ĵ�С
        return (int)(pxValue * RATIO); //����̫СҲ���ÿ���  
	} 	

}
