package com.smk17.android.tools;

import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.yh.materialdesign.dialog.MaterialDialog;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * �Ի��򹤾���
 * @author ������
 * @version 1.0
 */
public class ToolAlert {

	private static MaterialDialog mProgressDialog;
	
	/**
	 * ��ʾProgressDialog
	 * @param context ������
	 * @param message ��Ϣ
	 */
	public static void loading(String message){
		
		loading(MyApplication.gainContext(),message,false);
	}
	
	/**
	 * ��ʾProgressDialog
	 * @param context ������
	 * @param message ��Ϣ
	 */
	public static void loading( String message,final ILoadingOnKeyListener listener){
		
		loading(MyApplication.gainContext(),message,false,listener);
	}
	
	/**
	 * ��ʾProgressDialog
	 * @param context ������
	 * @param message ��Ϣ
	 */
	public static void loading(Context context, String message){
		
		loading(context,message,false);
	}
	
	/**
	 * ��ʾProgressDialog
	 * @param context ������
	 * @param message ��Ϣ
	 */
	public static void loading(Context context, String message,final ILoadingOnKeyListener listener){
		
		loading(context,message,false,listener);
	}
	
	/**
	 * ��ʾProgressDialog
	 * @param context ������
	 * @param message ��Ϣ
	 * @param cancelable �Ƿ����ȡ��
	 */
	public static void loading(Context context, String message,boolean cancelable){
		
		if (mProgressDialog == null) {
			mProgressDialog = new MaterialDialog(context).setCanceledOnTouchOutside(cancelable)
					.setCancelable(cancelable).setTitle(message);
		}
		if(mProgressDialog.isShow()){mProgressDialog.dismiss();}
		mProgressDialog.show();
	}
	
	/**
	 * ��ʾProgressDialog
	 * @param context ������
	 * @param message ��Ϣ
	 */
	public static void loading(Context context, String message,boolean cancelable ,final ILoadingOnKeyListener listener){
		
		if(mProgressDialog == null){
			mProgressDialog = new MaterialDialog(context).setCanceledOnTouchOutside(cancelable)
					.setCancelable(cancelable).setTitle(message);
		}
		
		if(mProgressDialog.isShow()){mProgressDialog.dismiss();}
		
		if(null != listener)
		{
			mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
	            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
	            	listener.onKey(dialog, keyCode, event);
	                return false;
	            }
	        });
		}else{
			mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
	            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
	                if (keyCode == KeyEvent.KEYCODE_BACK) {
	                	mProgressDialog.dismiss();
	                }
	                return false;
	            }
	        });
		}
		
		mProgressDialog.show();
	}
	
	/**
	 * �жϼ��ضԻ����Ƿ����ڼ���
	 * @return �Ƿ�
	 */
	public static boolean isLoading(){
		
		if(null != mProgressDialog){
			return mProgressDialog.isShow();
		}else{
			return false;
		}
	}
	
	/**
	 * �ر�ProgressDialog
	 */
	public static void closeLoading(){
		if(mProgressDialog != null){
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
	
	/**
	 * ����ProgressDialog������Ϣ
	 * @param message ��Ϣ
	 */
	public static void updateProgressText(String message){
		if(mProgressDialog == null ) return ;
		
		if(mProgressDialog.isShow()){
			mProgressDialog.setMessage(message);
		}
	}
	
    /**
     * �����Ի���
     * @param title �Ի������
     * @param msg  �Ի�������
     * @param okBtnListenner ȷ����ť����¼�
     * @param cancelBtnListenner ȡ����ť����¼�
     */
    public static AlertDialog dialog(Context context,String msg) {
    	return dialog(context,"",msg);
    }
    
    /**
     * �����Ի���
     * @param title �Ի������
     * @param msg  �Ի�������
     * @param okBtnListenner ȷ����ť����¼�
     * @param cancelBtnListenner ȡ����ť����¼�
     */
    public static AlertDialog dialog(Context context,String title,String msg) {
    	return dialog(context,title,msg,null);
    }
    
    /**
     * �����Ի���
     * @param title �Ի������
     * @param msg  �Ի�������
     * @param okBtnListenner ȷ����ť����¼�
     * @param cancelBtnListenner ȡ����ť����¼�
     */
    public static AlertDialog dialog(Context context,String title,String msg,OnClickListener okBtnListenner) {
    	return dialog(context,title,msg,okBtnListenner,null);
    }
    
    /**
     * �����Ի���
     * @param title �Ի������
     * @param msg  �Ի�������
     * @param okBtnListenner ȷ����ť����¼�
     * @param cancelBtnListenner ȡ����ť����¼�
     */
    public static AlertDialog dialog(Context context,String title,String msg,OnClickListener okBtnListenner,OnClickListener cancelBtnListenner) {
    	return dialog(context,null,title,msg,okBtnListenner,cancelBtnListenner);
    }
    
    /**
     * �����Ի���
     * @param title �Ի������
     * @param msg  �Ի�������
     * @param okBtnListenner ȷ����ť����¼�
     * @param cancelBtnListenner ȡ����ť����¼�
     */
    public static AlertDialog dialog(Context context,Drawable icon,String title,String msg) {
    	return dialog(context,icon,title,msg,null);
    }
    
    /**
     * �����Ի���
     * @param title �Ի������
     * @param msg  �Ի�������
     * @param okBtnListenner ȷ����ť����¼�
     * @param cancelBtnListenner ȡ����ť����¼�
     */
    public static AlertDialog dialog(Context context,Drawable icon,String title,String msg,OnClickListener okBtnListenner) {
    	return dialog(context,icon,title,msg,okBtnListenner,null);
    }
	
    /**
     * �����Ի���
     * @param icon  ����ͼ��
     * @param title �Ի������
     * @param msg  �Ի�������
     * @param okBtnListenner ȷ����ť����¼�
     * @param cancelBtnListenner ȡ����ť����¼�
     */
    public static AlertDialog dialog(Context context,Drawable icon,String title,String msg, OnClickListener okBtnListenner,OnClickListener cancelBtnListenner) {
        Builder dialogBuilder = new AlertDialog.Builder(context);
        if(null != icon){
        	dialogBuilder.setIcon(icon);
        }
        if(ToolString.isNoBlankAndNoNull(title)){
            dialogBuilder.setTitle(title);
        }
        dialogBuilder.setMessage(msg);
        if(null != okBtnListenner){
        	dialogBuilder.setPositiveButton(android.R.string.ok, okBtnListenner);
        }
        if(null != cancelBtnListenner){
        	dialogBuilder.setNegativeButton(android.R.string.cancel, cancelBtnListenner);
        }
        dialogBuilder.create();
        return dialogBuilder.show();
    }
    
    /**
     * ����һ���Զ��岼�ֶԻ���
     * @param context ������
     * @param view �Զ��岼��View
     */
	public static AlertDialog dialog(Context context,View view) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(view);
		return builder.show();
	}
	
    /**
     * ����һ���Զ��岼�ֶԻ���
     * @param context ������
     * @param resId �Զ��岼��View��Ӧ��layout id
     */
	public static AlertDialog dialog(Context context,int resId) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(resId, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(view);
		return builder.show();
	}
    
    /**
     * �����϶̵�Toast��Ϣ
     * @param msg ��Ϣ����
     */
    public static void toastShort(String msg) {
    	toastShort(MyApplication.gainContext(), msg);
    }
    
    /**
     * �����϶̵�Toast��Ϣ
     * @param msg ��Ϣ����
     */
    public static void toastShort(Context context,String msg) {
    	toast(MyApplication.gainContext(), msg, Toast.LENGTH_SHORT);
    }

    /**
     * �����ϳ���Toast��Ϣ
     * @param msg ��Ϣ����
     */
    public static void toastLong(String msg) {
    	toastLong(MyApplication.gainContext(), msg);
    }
    
    /**
     * �����ϳ���Toast��Ϣ
     * @param msg ��Ϣ����
     */
    public static void toastLong(Context context,String msg) {
    	toast(MyApplication.gainContext(), msg, Toast.LENGTH_LONG);
    }
    
    /**
     * �����Զ���ʱ����Toast��Ϣ
     * @param msg ��Ϣ����
     */
    public static void toast(String msg,int duration) {
    	toast(MyApplication.gainContext(), msg, duration);
    }
    
    /**
     * ����Ĭ��ʱ����Toast��Ϣ
     * @param msg ��Ϣ����
     */
    public static void toast(String msg) {
    	toast(MyApplication.gainContext(), msg, -1);
    }
    
    /**
     * �����Զ���ʱ����Toast��Ϣ
     * @param msg ��Ϣ����
     */
    @SuppressLint("InflateParams")
	public static void toast(Context context,String msg,int duration) {
        View layout = LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView text = (TextView) layout.findViewById(R.id.tvTextToast);
        text.setText(msg);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL| Gravity.BOTTOM, 0, 0);
//        toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 100);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
        
//        SnackBar sbar = new SnackBar((Activity) context, msg);
//        switch (duration) {
//		case Toast.LENGTH_SHORT:
//			sbar.setDismissTimer(2*1000);
//			break;
//		case Toast.LENGTH_LONG:
//			sbar.setDismissTimer(4*1000);
//			break;
//		default:
//			break;
//		}        
//        sbar.show();
    }
    
    /**
     * ����Pop����
     * @param context ��������������
     * @param anchor ����pop����Ŀؼ�
     * @param viewId pop���ڽ���layout
     * @param xoff ����Xƫ����
     * @param yoff ����Yƫ����
     */
    public static PopupWindow popwindow(Context context,View anchor,int viewId,int xoff,int yoff){
        ViewGroup menuView = (ViewGroup) LayoutInflater.from(context).inflate(viewId, null);
        PopupWindow pw = new PopupWindow(menuView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setTouchable(true);
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(anchor, xoff, yoff);
        pw.update();
        return pw;
    }
    
    /**
     * ����Pop����
     * @param anchor ����pop����Ŀؼ�
     * @param popView pop���ڽ���
     * @param xoff ����Xƫ����
     * @param yoff ����Yƫ����
     */
    public static PopupWindow popwindow(View anchor,View popView,int xoff,int yoff){
        PopupWindow pw = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(anchor, xoff, yoff);
        pw.update();
        return pw;
    }
    
    /**
     * ����Pop���ڣ��������Ƿ��������ط��رմ��ڣ�
     * @param context ��������������
     * @param anchor ����pop����Ŀؼ�
     * @param viewId pop���ڽ���layout
     * @param xoff ����Xƫ����
     * @param yoff ����Yƫ����
     * @param outSideTouchable ��������ط��Ƿ�رմ���
     */
    public static PopupWindow popwindow(Context context,View anchor,int viewId,int xoff,int yoff,boolean outSideTouchable){
        ViewGroup menuView = (ViewGroup) LayoutInflater.from(context).inflate(viewId, null);
        PopupWindow pw = new PopupWindow(menuView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setTouchable(outSideTouchable);
        pw.setFocusable(outSideTouchable);
        pw.setOutsideTouchable(outSideTouchable);
        pw.showAsDropDown(anchor, xoff, yoff);
        pw.update();
        return pw;
    }
    
    /**
     * ����Pop���ڣ��������Ƿ��������ط��رմ��ڣ�
     * @param anchor ����pop����Ŀؼ�
     * @param popView pop���ڽ���
     * @param xoff ����Xƫ����
     * @param yoff ����Yƫ����
     * @param outSideTouchable ��������ط��Ƿ�رմ���
     */
    public static PopupWindow popwindow(View anchor,View popView,int xoff,int yoff,boolean outSideTouchable){
        PopupWindow pw = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(outSideTouchable);
        pw.showAsDropDown(anchor, xoff, yoff);
        pw.update();
        
        return pw;
    }
    
    /**
     * ָ�����굯��Pop����
     * @param pw pop���ڶ���
     * @param anchor ����pop����Ŀؼ�
     * @param popView pop���ڽ���
     * @param x ����X
     * @param y ����Y
     * @param outSideTouchable ��������ط��Ƿ�رմ���
     */
    public static PopupWindow popwindowLoction(PopupWindow pw,View anchor,View popView,int x,int y){
    	if(pw == null){
    		pw = new PopupWindow(popView,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);
    		pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    		pw.setOutsideTouchable(false);
    	}
    	
    	if (pw.isShowing()) {
    		pw.update(x,y,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		} else {
			pw.showAtLocation(anchor,Gravity.NO_GRAVITY, x,y);
		}	
        
        return pw;
    }
    
    /**
     * ��״̬������һ��֪ͨ��Ϣ
     * @param mContext ������
     * @param message ��ϢBean
     */
    @SuppressWarnings("deprecation")
	public static void notification(Context mContext,NotificationMessage message){
        
    	//��Ϣ������
    	NotificationManager mNoticeManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        //����Notification
        Notification notice = new Notification();
        notice.icon = message.getIconResId();
        notice.tickerText = message.getStatusBarText();
        notice.when = System.currentTimeMillis();
        /**
         *  notification.defaults = Notification.DEFAULT_SOUND; // ����ϵͳ�Դ�����
			notification.defaults = Notification.DEFAULT_VIBRATE;// ����Ĭ���� 
			notification.defaults = Notification.DEFAULT_ALL; // ���������� 
			notification.defaults = Notification.DEFAULT_ALL; // �����е��������ó�Ĭ��
         */
        notice.defaults = Notification.DEFAULT_SOUND;//֪ͨʱ������Ĭ������
        /**
         *  notification.flags = Notification.FLAG_NO_CLEAR; // ��������ťʱ�ͻ������Ϣ֪ͨ,���ǵ��֪ͨ����֪ͨʱ������ʧ  
			notification.flags = Notification.FLAG_ONGOING_EVENT; // ��������ť���������Ϣ֪ͨ,����������ʾ����������  
			notification.flags |= Notification.FLAG_AUTO_CANCEL; // ��������ť����֪ͨ����Զ���ʧ  
			notification.flags |= Notification.FLAG_INSISTENT; // һֱ���У���������һֱ���ţ�֪���û���Ӧ
         */
        notice.flags |= Notification.FLAG_AUTO_CANCEL; //֪ͨ������
        
        //����֪ͨ��ʾ�Ĳ��� 
        Intent mIntent = new Intent(mContext, message.getForwardComponent());
        mIntent.setAction(ToolString.gainUUID());
        mIntent.putExtras(message.getExtras());
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        //�Զ�����PendingIntent��Extra����
        PendingIntent pIntent = PendingIntent.getActivity(mContext,0,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        notice.setLatestEventInfo(mContext, message.getMsgTitle(), message.getMsgContent(),pIntent);
        
        //����֪ͨ
        mNoticeManager.notify(message.getId(), notice);
    }
    
    
    /**
     * �����Զ��岼��֪ͨ��Ϣ
     * @param mContext ������
     * @param message  ��ϢBean
     */
    public static void notificationCustomView(Context mContext,NotificationMessage message){
    	
    	//��Ϣ������
    	NotificationManager mNoticeManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        //����Notification
    	Notification mNotify = new Notification();  
        mNotify.icon = message.getIconResId();  
        mNotify.tickerText = message.getStatusBarText();  
        mNotify.when = System.currentTimeMillis();  
        mNotify.flags |= Notification.FLAG_AUTO_CANCEL; //֪ͨ������
        mNotify.contentView = message.getmRemoteViews();
        
        //����֪ͨ��ʾ�Ĳ��� 
        Intent mIntent = new Intent(mContext, message.getForwardComponent());
        mIntent.setAction(ToolString.gainUUID());
        mIntent.putExtras(message.getExtras());
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext,0,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mNotify.contentIntent = contentIntent;
        
        //����֪ͨ
        mNoticeManager.notify(message.getId(), mNotify);  
    }
    
    /**
     * Loading�����Ի���
     */
    public interface ILoadingOnKeyListener{
    	 public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event);
    }
}
