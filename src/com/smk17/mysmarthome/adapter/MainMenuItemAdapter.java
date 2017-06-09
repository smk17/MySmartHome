package com.smk17.mysmarthome.adapter;

import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.yh.materialdesign.views.LayoutRipple;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenuItemAdapter extends BaseAdapter {
	private  String[] menuListArray = null;
	private LayoutInflater mInflater;
	private FMenuItemClickListener mFMenuItemClickListener=null;
//	private Context context;
	
	public MainMenuItemAdapter(String[] array,Context context ,FMenuItemClickListener fMenuItemClickListener)
	{
//		this.context = context;
		this.menuListArray = array;
		this.mInflater = LayoutInflater.from(context); 
		this.mFMenuItemClickListener = fMenuItemClickListener;
	}

	public interface FMenuItemClickListener  
    {  
        void onMenuItemClick(Integer position);  
    }  
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return menuListArray.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.material_item_main_menu, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.layoutRipple = (LayoutRipple)convertView.findViewById(R.id.itemMenuButtons);
			viewHolder.menuname = (TextView)convertView.findViewById(R.id.tv_menu_name);
			viewHolder.avatar = (ImageView)convertView.findViewById(R.id.iv_menu_avatar);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.menuname.setText(menuListArray[position].toString());
		Integer NetMode = MyApplication.getNetMode();
		switch (position) {
		case 0://情景控制
			viewHolder.avatar.setImageResource(R.drawable.ic_default_white_36dp);
			if(NetMode.equals(Constant.Intranet_Mode)) hideView(convertView);
			break;
		case 1://主控列表
			viewHolder.avatar.setImageResource(R.drawable.ic_format_list_bulleted_white_36dp);
			if(MyApplication.getMainDeviceNumRows() == 1){
				hideView(convertView);
			}
			break;
		case 2://区域控制
			viewHolder.avatar.setImageResource(R.drawable.ic_home_white_36dp);
			break;
		case 3://报警控制
			viewHolder.avatar.setImageResource(R.drawable.ic_security_white_36dp);
			hideView(convertView);
			break;
		case 4://环境联动
			viewHolder.avatar.setImageResource(R.drawable.ic_toys_white_36dp);
			hideView(convertView);
			break;
		case 5://账号设置
			viewHolder.avatar.setImageResource(R.drawable.ic_supervisor_account_white_36dp);
			break;
		case 6://系统设置
			viewHolder.avatar.setImageResource(R.drawable.ic_settings_white_36dp);
			break;
		case 7://刷新数据
			viewHolder.avatar.setImageResource(R.drawable.ic_autorenew_white_36dp);
//			hideView(convertView);
			break;
		default:
			viewHolder.avatar.setImageResource(R.drawable.ic_default_white_36dp);
			break;
		}
		setOriginRiple(viewHolder.layoutRipple);
		viewHolder.layoutRipple.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(mFMenuItemClickListener != null){
					mFMenuItemClickListener.onMenuItemClick(position);		
				}
			}
		});
		return convertView;
	}
	
	private void hideView(View convertView){
		convertView.setVisibility(View.GONE);
		LayoutParams lp = convertView.getLayoutParams();
		lp.height = 1;
		convertView.setLayoutParams(lp);
		convertView.invalidate();
	}
	
	private void setOriginRiple(final LayoutRipple layoutRipple){
    	
    	layoutRipple.post(new Runnable() {
			
			@Override
			public void run() {
		    	layoutRipple.setRippleColor(Color.parseColor("#009f9f"));
		    	
		    	layoutRipple.setRippleSpeed(70);
			}
		});
    	
    }
	
	public final class ViewHolder{
		LayoutRipple layoutRipple;
		ImageView avatar;
		TextView menuname;
	}

}
