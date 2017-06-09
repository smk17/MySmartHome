package com.smk17.mysmarthome.db;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.smk17.android.tools.ToolString;
import com.smk17.mysmarthome.Constant;

public class HotSpotMode {
	private static volatile boolean isRun = false;  
	private static volatile boolean isQuerySql = false;
	
	public static void RevTcpCli(){
		isRun = true;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO 子线程接收信息并发送到主线程
				try {
	    			String data = "";//tcpService
	        		byte[] rBuffer = new byte[1024];
	        		Socket tcpCli = new Socket(Constant.SERVER_IP, Constant.SERVER_PORT);
					InputStream inputStream = tcpCli.getInputStream();
	        		DataInputStream input = new DataInputStream(inputStream);
					DataOutputStream dos=new DataOutputStream(tcpCli.getOutputStream());
//		    		rev_sb = new StringBuilder();
		    		dos.write(Constant.SERVER_CONN.getBytes(Constant.CharsetName));
		    		dos.flush();
		    		
		    		while (isRun){
	            		int length = input.read(rBuffer);
	            		data = new String(rBuffer, 0, length, Constant.CharsetName);
	            		dos.write(data.getBytes(Constant.CharsetName));
	            		dos.flush();
	            		Log.e("Tcp Demo", "message From Server:" + data);  
//	            		dos.writeUTF(data);			
	            		if(ToolString.isNoBlankAndNoNull(data) ){
	            			isQuerySql = true;
	            			ExecSql.getDevice(data);
	            		}
	                    Thread.sleep(1000);
		    		}
		    		dos.write("close".getBytes(Constant.CharsetName));
		    		dos.close();
		    		tcpCli.close();
				} catch (Exception e) {
					e.printStackTrace();
				}	 
			}
		}).start();
	}
	
	public static void matchRevTcpCli(final Handler handler ){
		isRun = false;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
	    			String data = "";//tcpService
	        		byte[] rBuffer = new byte[1024];
	        		int failed_count = 1;
	        		Message msg = new Message();	
	        		Socket tcpCli = new Socket(Constant.SERVER_IP, Constant.SERVER_PORT);
					InputStream inputStream = tcpCli.getInputStream();
	        		DataInputStream input = new DataInputStream(inputStream);
					DataOutputStream dos=new DataOutputStream(tcpCli.getOutputStream());
//		    		rev_sb = new StringBuilder();
		    		dos.write(ToolString.hexStr2Bytes("EFEE"));
		    		dos.flush();
		    		isRun = true;
		    		msg.what = Constant.REVToHotSpotNet_ID;
		    		while (isRun){
	            		int length = input.read(rBuffer);
	            		data = new String(rBuffer, 0, length, Constant.CharsetName);	            		
	            		Log.e("Tcp Demo", "message From Server:" + data);  
//	            		dos.writeUTF(data);			
	            		if(ToolString.isNoBlankAndNoNull(data) ){
	            			JSONObject Object =new JSONObject(data);
	            			String type = Object.getString("type");
	            			String status = Object.getString("status");
	            			switch (type) {
							case "match":
								if(status.equals("failed")){
									if(failed_count == 2){
										failed_count = 1;
										msg.obj  = data;
				            			handler.sendMessage(msg);
									}else{
										failed_count++;
										dos.write(ToolString.hexStr2Bytes("EFEE"));
							    		dos.flush();
									}
									
								}else if(status.equals("succeeded")){
									dos.write(ToolString.hexStr2Bytes("EFEE"));
						    		dos.flush();
						    		msg.obj  = data;
			            			handler.sendMessage(msg);
								}
								break;
							case "recall":
								if(status.equals("failed")){
									
								}else if(status.equals("succeeded")){
									msg.obj  = data;
			            			handler.sendMessage(msg);
								}
								break;
							case "gotomatch":
								if(status.equals("succeeded")){
									
								}
								break;
							default:
								break;
							}	            			
	            		}
	                    Thread.sleep(1000);
		    		}
		    		dos.write("close".getBytes(Constant.CharsetName));
		    		dos.close();
		    		tcpCli.close();
				} catch (Exception e) {
					e.printStackTrace();
				}	 
			}
		}).start();
	}
	
	public static void CloseRevTcpCli(){
		isRun = false;
	}
	
	public static boolean getIsQuerySql(){
		return isQuerySql;
	}
	public static void setIsQuerySql(boolean b){
		isQuerySql = b;
	}
}
