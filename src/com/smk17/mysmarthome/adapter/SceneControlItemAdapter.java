package com.smk17.mysmarthome.adapter;

import java.util.ArrayList;

import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.Utils.BitmapUtil;
import com.smk17.mysmarthome.activity.SystemSettingsActivity;
import com.smk17.mysmarthome.adapter.FragmentItemAdapter.ViewHolder;
import com.smk17.mysmarthome.cloud.ExecToCloudSql;
import com.smk17.mysmarthome.db.ExecSql;
import com.smk17.mysmarthome.domain.DeviceScene;
import com.yh.materialdesign.dialog.MaterialDialog;
import com.yh.materialdesign.views.ButtonFloatSmall;
import com.yh.materialdesign.views.CheckBox;
import com.yh.materialdesign.views.LayoutRipple;
import com.yh.materialdesign.views.SlideSwitch;
import com.yh.materialdesign.views.SlideSwitch.SlideListener;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SceneControlItemAdapter extends BaseAdapter {

	private ArrayList<DeviceScene> list = null;
	private LayoutInflater mInflater;
	private SCMIABtnClickListener mFBtnClickListener = null;
	private Context context;
	private int mType;
	
	public static final int TYPE_SceneControl				= 22;	
	public static final int TYPE_SceneManagement			= 23;	
	
	public SceneControlItemAdapter(ArrayList<DeviceScene> list ,Context context,int type ){
		this.context = context;
		this.mInflater = LayoutInflater.from(context); 
		this.list = list;
		this.mType = type;
	}
	
	public SceneControlItemAdapter(ArrayList<DeviceScene> list ,Context context,int type, SCMIABtnClickListener fBtnClickListener){
		this.context = context;
		this.mInflater = LayoutInflater.from(context); 
		this.list = list;
		this.mType = type;
		this.mFBtnClickListener = fBtnClickListener;
	}
	
	public interface SCMIABtnClickListener  
    {
		void onFAddBtnClick(DeviceScene scene);  
		void onFDeleteBtnClick(DeviceScene scene);
		void onFEditBtnClick(DeviceScene scene);  
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
		DeviceScene scene = list.get(position);
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
		viewHolder.tvName.setText(scene.getName());
		initView(viewHolder,scene,position,parent);
		return convertView;
	}
	
	private void initView(final ViewHolder viewHolder,final DeviceScene scene,int position,ViewGroup parent){
		switch (mType) {
		case TYPE_SceneControl:
			ImageView icon = viewHolder.btnfsAvatar.getIcon();
			viewHolder.btnfsAvatar.setVisibility(View.VISIBLE);
			icon.setBackgroundResource(Constant.SelectorIcon[scene.getIcon()]);
			viewHolder.btnfsAvatar.setIcon(icon);
			viewHolder.btnfsAvatar.setBackgroundColor(Color.parseColor( Constant.SelectorBackground[scene.getBackground()]));
			if(scene.getID() == 0){				
				viewHolder.tvReason.setVisibility(View.VISIBLE);
				viewHolder.tvName.setText(scene.getName());
				viewHolder.tvReason.setText(scene.getRemarks());
				viewHolder.rlItemFragment.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 添加新的情景模式
						final MaterialDialog alert = new MaterialDialog(context);
						String msg = "你是要添加新的情景模式吗？\n请您点击【确定】进入情景模式管理界面进行添加。";
						alert.setTitle("添加").setMessage(msg).setCanceledOnTouchOutside(true)
										.setPositiveButton("确定", new OnClickListener() {
											
											@Override
											public void onClick(View v) {
												Intent intent = new Intent();
												intent.setClass(context, SystemSettingsActivity.class);
												intent.putExtra("Page",SystemSettingsActivity.Page_SceneManagement);
												context.startActivity(intent);
												alert.dismiss();	
											}
										}).setNegativeButton("取消", new OnClickListener() {
											
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
				initViewToZoneControl(viewHolder,scene,position);
			}			
			break;
		case TYPE_SceneManagement:
			viewHolder.ckSelector.setVisibility(View.GONE);
			viewHolder.ivDelete.setVisibility(View.GONE);
			viewHolder.ivEdit.setVisibility(View.GONE);
			viewHolder.ivPlay.setVisibility(View.GONE);
			viewHolder.mdlrPlay.setVisibility(View.GONE);
			viewHolder.mdSwitch.setVisibility(View.GONE);
			if(scene.getID() == 0){
				viewHolder.tvName.setText(scene.getName());
				viewHolder.tvReason.setText(scene.getRemarks());
				viewHolder.rlItemFragment.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 添加新的情景模式
						final MaterialDialog alert = new MaterialDialog(context);
						String msg = "你是要添加新的情景模式吗？\n请您点击【确定】进行添加。";
						alert.setTitle("添加").setMessage(msg).setCanceledOnTouchOutside(true)
										.setPositiveButton("确定", new OnClickListener() {
											
											@Override
											public void onClick(View v) {
												mFBtnClickListener.onFAddBtnClick(scene);
												alert.dismiss();	
											}
										}).setNegativeButton("取消", new OnClickListener() {
											
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
				viewHolder.rlItemFragment.setOnClickListener(null);
				initViewToZoneManagement(viewHolder,scene,position,parent);
			}			
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
	
	private void initViewToZoneControl(final ViewHolder viewHolder,final DeviceScene scene,int position){
		RelativeLayout.LayoutParams params = 
			    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
			        RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.RIGHT_OF, viewHolder.btnfsAvatar.getId());
		params.leftMargin = BitmapUtil.dip(context,12);
		viewHolder.tvName.setLayoutParams(params);		
		viewHolder.mdSwitch.setVisibility(View.VISIBLE);
		if(scene.getValue().equals(1)){
			viewHolder.mdSwitch.setState(true);
		}else{
			viewHolder.mdSwitch.setState(false);
		}
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
					scene.setValue(1);					
				}else{
					scene.setValue(0);					
				}
				switch (MyApplication.getNetMode()) {
				case Constant.Network_Mode:
					//TODO 外网模式下的初始化
					ExecToCloudSql.UpdateScene(String.valueOf(scene.getID()), String.valueOf(scene.getValue()));
					break;
				case Constant.Intranet_Mode:
					//TODO 内网模式下的初始化
					break;
				case Constant.HotSpot_Mode:
					//TODO 热点模式下的初始化
					ExecSql.UpdateScene(scene,true);
					break;
				default:
					break;
				}
			}
		});
		
	}
	private void initViewToZoneManagement(final ViewHolder viewHolder,final DeviceScene scene,final int position,final ViewGroup parent){
		RelativeLayout.LayoutParams params = 
			    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
			        RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.RIGHT_OF, viewHolder.btnfsAvatar.getId());
		params.leftMargin = BitmapUtil.dip(context,12);
		viewHolder.tvName.setLayoutParams(params);
		viewHolder.ivEdit.setVisibility(View.VISIBLE);
		viewHolder.ivDelete.setVisibility(View.VISIBLE);
		viewHolder.ivEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mFBtnClickListener!=null){
					mFBtnClickListener.onFEditBtnClick(scene);
				}
			}
		});
		viewHolder.ivDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final MaterialDialog alert = new MaterialDialog(context);
				String msg = "确定要删除该情景模式吗？\n删除后该情景模式后，";
				msg += "与之相关的设置及数据也将清除。";
				alert.setTitle("删除情景模式").setMessage(msg).setCanceledOnTouchOutside(true)
								.setPositiveButton("确定", new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										if(mFBtnClickListener!=null){
											mFBtnClickListener.onFDeleteBtnClick(scene);
										}
										alert.dismiss();													
									}
								}).setNegativeButton("取消", new OnClickListener() {
									
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

}
