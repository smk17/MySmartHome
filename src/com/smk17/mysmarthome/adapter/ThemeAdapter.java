package com.smk17.mysmarthome.adapter;

import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ThemeAdapter extends BaseAdapter{	
	private int[] list = null; 
	private LayoutInflater mInflater;
	private Context context;
	private BtnClickListener mBtnClickListener;
	
	public ThemeAdapter(int[] list, Context context,BtnClickListener BtnClickListener){
		this.context = context;
		this.mInflater = LayoutInflater.from(context); 
		this.list = list;
		this.mBtnClickListener = BtnClickListener;
	}

	public interface BtnClickListener  
    {
		void onBtnClick(Integer id);
    }  
	
	@Override
	public int getCount() {
		return list.length;
	}

	@Override
	public Object getItem(int arg0) {
		return list[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {			
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.material_item_theme, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.ivImage = (ImageView)convertView.findViewById(R.id.iv_theme);
			viewHolder.tvName = (TextView)convertView.findViewById(R.id.tv_theme_name);
			viewHolder.btnSetting = (RelativeLayout)convertView.findViewById(R.id.btn_setting);
			viewHolder.ivBtn = (ImageView)convertView.findViewById(R.id.iv_btn_ok);
			viewHolder.tvBtnName = (TextView)convertView.findViewById(R.id.tv_btn_ok);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		final SharedPreferences sp = context.getSharedPreferences(Constant.INFO_THEME ,Activity.MODE_PRIVATE);
		final int theme_id = sp.getInt(Constant.THEME_ID, 1);
		if(list[position] == theme_id){
			viewHolder.ivBtn.setVisibility(View.VISIBLE);
			viewHolder.tvBtnName.setText(R.string.theme_applying);
			viewHolder.tvBtnName.setTextColor(context.getResources().getColor(R.color.white));
			viewHolder.btnSetting.setBackgroundResource(R.drawable.content_check_bg);
		}else{
			viewHolder.ivBtn.setVisibility(View.GONE);
			viewHolder.tvBtnName.setText(R.string.theme_apply);
			viewHolder.tvBtnName.setTextColor(context.getResources().getColor(R.color.menu_background));
			viewHolder.btnSetting.setBackgroundResource(R.drawable.content_bg);
		}
		switch (list[position]) {
		case 1:
			viewHolder.ivImage.setImageResource(R.drawable.theme_default);
			viewHolder.tvName.setText(R.string.theme_default);			
			break;
		case 2:
			viewHolder.ivImage.setImageResource(R.drawable.theme_nighttime);
			viewHolder.tvName.setText(R.string.theme_nighttime);			
			break;
		case 3:
			viewHolder.ivImage.setImageResource(R.drawable.theme_spring);
			viewHolder.tvName.setText(R.string.theme_spring);
			break;
		case 4:
			viewHolder.ivImage.setImageResource(R.drawable.theme_summer);
			viewHolder.tvName.setText(R.string.theme_summer);
			break;
		case 5:
			viewHolder.ivImage.setImageResource(R.drawable.theme_autumn);
			viewHolder.tvName.setText(R.string.theme_autumn);
			break;
		case 6:
			viewHolder.ivImage.setImageResource(R.drawable.theme_winter);
			viewHolder.tvName.setText(R.string.theme_winter);
			break;
		default:
			break;
		}
		switch (theme_id) {
		case 1:
			viewHolder.tvName.setTextColor(context.getResources().getColor(R.color.black));
			break;
		case 2:case 3:case 4:case 5:case 6:
			viewHolder.tvName.setTextColor(context.getResources().getColor(R.color.white));
			break;
		
		default:
			break;
		}
		viewHolder.btnSetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(theme_id != list[position]){
					sp.edit().putInt(Constant.THEME_ID ,list[position]).commit();
					notifyDataSetChanged();
					mBtnClickListener.onBtnClick(list[position]);
				}				
			}
		});
		return convertView;
	}
	
	public static final class ViewHolder{
		ImageView ivImage;
		TextView tvName;
		RelativeLayout btnSetting;
		ImageView ivBtn;
		TextView tvBtnName;
	}

}
