package com.smk17.mysmarthome.adapter;

import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.Utils.BitmapUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class HorizontalListViewAdapter extends BaseAdapter{
	private int[] mIconIDs = null;
	private String[] mIconColors = null;
	private Context mContext;
	private LayoutInflater mInflater;
	Bitmap iconBitmap;
	private int selectIndex = -1;

	public HorizontalListViewAdapter(Context context, int[] ids){
		this.mContext = context;
		this.mIconIDs = ids;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	
	public HorizontalListViewAdapter(Context context, String[] Colors){
		this.mContext = context;
		this.mIconColors = Colors;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	
	@Override
	public int getCount() {
		if(mIconIDs != null){
			return mIconIDs.length;
		}
		if(mIconColors != null){
			return mIconColors.length;
		}
		return 0;
	}
	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.material_horizontal_list_item, parent,false);
			holder.mImage=(ImageView)convertView.findViewById(R.id.img_list_item);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		if(position == selectIndex){
			convertView.setSelected(true);
		}else{
			convertView.setSelected(false);
		}
		if(mIconIDs != null){
			iconBitmap = getPropThumnail(mIconIDs[position]);
			holder.mImage.setImageBitmap(iconBitmap);
		}
		
		if(mIconColors != null){
			iconBitmap = getPropThumnail(mIconColors[position]);
			holder.mImage.setImageBitmap(iconBitmap);
		}		

		return convertView;
	}
	
	@SuppressWarnings("deprecation")
	private Bitmap getPropThumnail(int id){
		Drawable d = mContext.getResources().getDrawable(id);
		Bitmap b = BitmapUtil.drawableToBitmap(d);
//		Bitmap bb = BitmapUtil.getRoundedCornerBitmap(b, 100);
		int w = mContext.getResources().getDimensionPixelOffset(R.dimen.DIMEN_48PX);
		int h = mContext.getResources().getDimensionPixelSize(R.dimen.DIMEN_48PX);
		
		Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(b, w, h);
		
		return thumBitmap;
	}
	private Bitmap getPropThumnail(String color){
		 ColorDrawable d = new ColorDrawable(Color.parseColor(color));		
//		Bitmap bb = BitmapUtil.getRoundedCornerBitmap(b, 100);
		int w = mContext.getResources().getDimensionPixelOffset(R.dimen.DIMEN_48PX);
		int h = mContext.getResources().getDimensionPixelSize(R.dimen.DIMEN_48PX);
		Bitmap b = BitmapUtil.drawableToBitmap(w,h,d);
		Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(b, w, h);
		
		return thumBitmap;
	}

	private static class ViewHolder {
		private ImageView mImage;
	}
	
	public void setSelectIndex(int i){
		selectIndex = i;
	}
}