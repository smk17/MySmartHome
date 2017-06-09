package com.smk17.android.tools;

import com.smk17.mysmarthome.MyApplication;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
/**
 * ���Ͷ�����֤�빤����
 * @author ������
 * @version 1.0
 *
 */
public class ToolSMS {
	
	public static String CHINA = "86";
	private static IValidateSMSCode mIValidateSMSCode;
	private static Handler mSMSHandle = new MySMSHandler();
	private static Context context = MyApplication.gainContext();
	
	/**
	 * ��ʼ��ShareSDK���Ͷ�����֤��ʵ��
	 * @param appkey
	 * @param appSecrect
	 */
	public static void initSDK(String appkey, String appSecrect){
		// ��ʼ������SDK
		SMSSDK.initSDK(context, appkey, appSecrect);
		//ע��ص������ӿ�
		SMSSDK.registerEventHandler(new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				mSMSHandle.sendMessage(msg);
			}
		});
	}
	
	/**
	 * �����ȡ������֤��
	 * @param phone �ֻ���
	 */
	public static void getVerificationCode(String phone){
		SMSSDK.getVerificationCode(CHINA, phone);
	}
	
	/**
	 * �ύ������֤�룬У���Ƿ���ȷ
	 * @param phone �ֻ���
	 * @param validateCode �ֻ�������֤��
	 */
	public static void submitVerificationCode(String phone, String validateCode,IValidateSMSCode callback){
		mIValidateSMSCode = callback;
		SMSSDK.submitVerificationCode(CHINA, phone, validateCode);
	}
	
	/**
	 * �ͷ���Դ
	 */
	public static void release(){
		// ���ٻص������ӿ�
		SMSSDK.unregisterAllEventHandler();
	}
	
    /**
     * ��Ϣ����Handle
     */
	private static class MySMSHandler extends Handler{
    	@Override
		public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		
    		int event = msg.arg1;
    		int result = msg.arg2;
    		Object data = msg.obj;
			
			if (result == SMSSDK.RESULT_COMPLETE) {
				//�ύ��֤��ɹ�
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					//��֤�ɹ��ص�
					if(null != mIValidateSMSCode){
						mIValidateSMSCode.onSucced();
					}
				} 
			} else {
				Throwable exption = ((Throwable) data);
				//��֤�ɹ��ص�
				if(null != mIValidateSMSCode){
					mIValidateSMSCode.onFailed(exption);
				}
			}
		}
    }
	
    /**
     * �ύ������֤��ص��ӿ�
     */
    public interface IValidateSMSCode{
    	void onSucced();
    	void onFailed(Throwable e);
    }
}
