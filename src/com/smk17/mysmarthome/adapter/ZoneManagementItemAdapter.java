package com.smk17.mysmarthome.adapter;

import java.util.ArrayList;

import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.Utils.BitmapUtil;
import com.smk17.mysmarthome.adapter.FragmentItemAdapter.ViewHolder;
import com.smk17.mysmarthome.domain.DeviceZone;
import com.yh.materialdesign.dialog.MaterialDialog;
import com.yh.materialdesign.edittext.MaterialEditText;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ZoneManagementItemAdapter extends BaseAdapter {

	private ArrayList<DeviceZone> list = null;
	private LayoutInflater mInflater;
	private ZMIABtnClickListener mFBtnClickListener = null;
	private Context context;
	
	public ZoneManagementItemAdapter(ArrayList<DeviceZone> zlist,Context context,ZMIABtnClickListener fBtnClickListener) {
		this.mInflater = LayoutInflater.from(context); 
		this.context = context;
		this.list = zlist;
		this.mFBtnClickListener = fBtnClickListener;
	}
	
	public interface ZMIABtnClickListener  
    {  
        void onFEditBtnClick(DeviceZone device);
		void onFDeleteBtnClick(DeviceZone device, int position);  
    }  
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		ViewHolder viewHolder = null;
		final DeviceZone device = list.get(position);
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.material_item_fragment, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.rlItemFragment = (RelativeLayout)convertView.findViewById(R.id.rl_item_fragment);
			viewHolder.tvName = (TextView)convertView.findViewById(R.id.tv_name);	
			viewHolder.ivDelete = (ImageView)convertView.findViewById(R.id.iv_delete);
			viewHolder.ivEdit = (ImageView)convertView.findViewById(R.id.iv_edit);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		RelativeLayout.LayoutParams params = 
			    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
			        RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		params.leftMargin = BitmapUtil.dip(context,12);
		viewHolder.ivEdit.setVisibility(View.VISIBLE);
		viewHolder.ivDelete.setVisibility(View.VISIBLE);
		viewHolder.tvName.setLayoutParams(params);
		viewHolder.tvName.setTextSize(18);
		viewHolder.tvName.setText(device.getName());
		viewHolder.ivEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final MaterialDialog mMaterialDialog = new MaterialDialog(context).setIsShowIMM(true);
				View view = LayoutInflater.from(context).inflate(R.layout.material_dialog_update, parent, false);
				final MaterialEditText metDeviveName = (MaterialEditText)view.findViewById(R.id.met_device_name);
				metDeviveName.setText(device.getName());
				((Button)view.findViewById(R.id.btn_save)).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						device.setName(metDeviveName.getText().toString());
						if(mFBtnClickListener!=null)
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
				mMaterialDialog.setView(view).setCanceledOnTouchOutside(true).show();				
			}
		});
		viewHolder.ivDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(list.size()>1){
					final MaterialDialog alert = new MaterialDialog(context);
					String msg = "确定要删除该区域吗？\n删除后该区域下的终端将转移到";
					if(position >0){
						msg += list.get(0).getName();
					}else{
						msg += list.get(1).getName();
					}							
					msg += "下，随后你可以重新编辑终端转移到你想放置的区域下。";
					alert.setTitle("删除区域").setMessage(msg).setCanceledOnTouchOutside(true)
									.setPositiveButton("确定", new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											if(mFBtnClickListener!=null){
												mFBtnClickListener.onFDeleteBtnClick(device,position);
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
				}else if(list.size() == 1){
					final MaterialDialog alert = new MaterialDialog(context);
					String msg = "检查到当前主控只有一个区域。\n每个主控最少要有一个主控，所以该区域不可删除。";
					alert.setTitle("删除区域").setMessage(msg).setCanceledOnTouchOutside(true)
									.setPositiveButton("确定", new OnClickListener() {
										
										@Override
										public void onClick(View v) {								
											alert.dismiss();													
										}
									}).show();
				}
				
			}
		});
		return convertView;
	}
}
