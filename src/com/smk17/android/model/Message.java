package com.smk17.android.model;

import java.io.Serializable;

/**
 * ��Ϣʵ��Bean
 * @author ������
 * @version 1.0
 *
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 7491152915368949244L;
	
	/**
	 * ��ϢID
	 */
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
