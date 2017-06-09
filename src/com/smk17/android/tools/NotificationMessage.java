package com.smk17.android.tools;

import com.smk17.android.model.Message;

import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * ����Notification֪ͨʵ��
 * 
 * @author ������
 * @version 1.0
 * 
 */
public class NotificationMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 681166507845221063L;

	/**
	 * ״̬����ʾ��Ϣͼ��
	 */
	private int iconResId;

	/**
	 * ״̬����ʾ��Ϣͼ��
	 */
	private String statusBarText;

	/**
	 * ��Ϣ����
	 */
	private String msgTitle;

	/**
	 * ��Ϣ����
	 */
	private String msgContent;

	/**
	 * �����Ϣ��ת�Ľ���
	 */
	private Class<?> forwardComponent;
	
	/**
	 * �����Ϣ��ת������Я��������
	 */
	private Bundle extras;

	/**
	 * �Զ�����Ϣ֪ͨ����View
	 */
	private RemoteViews mRemoteViews;

	public int getIconResId() {
		return iconResId;
	}

	public void setIconResId(int iconResId) {
		this.iconResId = iconResId;
	}

	public String getStatusBarText() {
		return statusBarText;
	}

	public void setStatusBarText(String statusBarText) {
		this.statusBarText = statusBarText;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public Class<?> getForwardComponent() {
		return forwardComponent;
	}

	public void setForwardComponent(Class<?> forwardComponent) {
		this.forwardComponent = forwardComponent;
	}

	public Bundle getExtras() {
		return extras;
	}

	public void setExtras(Bundle extras) {
		this.extras = extras;
	}

	public RemoteViews getmRemoteViews() {
		return mRemoteViews;
	}

	public void setmRemoteViews(RemoteViews mRemoteViews) {
		this.mRemoteViews = mRemoteViews;
	}
}
