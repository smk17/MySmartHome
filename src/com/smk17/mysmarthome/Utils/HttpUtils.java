package com.smk17.mysmarthome.Utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * HTTP工具类
 * 
 * @author YQY
 * 
 */

@SuppressWarnings("deprecation")
public class HttpUtils {
	static String TAG = "YQY_HttpUtils";
	
	/**
	 * 上传图片，文件
	 * 
	 * @param file
	 * @param actionUrl
	 */
	public static String uploadFile(File file, String actionUrl) {
		try {
			URL url = new URL(actionUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());

			Log.e("imageFilePath", file.getPath());
			FileInputStream fStream = new FileInputStream(file);
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			while ((length = fStream.read(buffer)) != -1) {
				ds.write(buffer, 0, length);
			}
			fStream.close();
			ds.flush();

			InputStream is = con.getInputStream();

			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			ds.close();
			return b.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 从服务器获取图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getUrlImage(String url) {
		Bitmap img = null;
		try {
			URL picurl = new URL(url);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) picurl
					.openConnection();
			conn.setConnectTimeout(6000);// 设置超时
			conn.setDoInput(true);
			conn.setUseCaches(false);// 不缓存
			conn.connect();
			InputStream is = conn.getInputStream();// 获得图片的数据流
			img = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}
	
//	public static String Get(String addrurl, Map<String, String> params) throws Exception {
//		return "";
//	}

	/**
	 * 使用post的方式登录
	 * 
	 * @param phone
	 * @param password
	 * @return
	 * @throws Exception 
	 */
	public static String Post(String addrurl, Map<String, String> params) throws Exception {
		HttpURLConnection conn = null;
		StringBuilder sb = new StringBuilder();
		//Log.e("url", addrurl);
		// 如果参数不为空
		try {
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					// Post方式提交参数的话，不能省略内容类型与长度
//					sb.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), "GBK")).append('&');
					sb.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), "UTF-8")).append('&');
					// Log.e("entry.getValue()",
					// URLEncoder.encode(entry.getValue(), "GBK"));
				}
				sb.deleteCharAt(sb.length() - 1);
				//Log.e("entry.getValue()", sb.toString());
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// 得到实体的二进制数据
		byte[] entitydata = sb.toString().getBytes();
		URL url = new URL(addrurl);

		conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(10000);
		conn.setReadTimeout(10000);
		conn.setDoOutput(true);
//		conn.setRequestProperty("contentType", "utf-8");
//		conn.setRequestProperty("contentType", "GBK"); 
		
		OutputStream out = conn.getOutputStream();
		out.write(entitydata);
		out.flush();
		out.close();

		int responseCode = conn.getResponseCode();
		if (responseCode == 200) {
			InputStream is = conn.getInputStream();
			String state = getStringFromInputStream(is);
			return state;
		} else {
			Log.e(TAG, "访问失败: " + responseCode);
		}
		if (conn != null) {
			conn.disconnect();
		}
			
		return "";
	}
	
	/**
	* 获取网址内容
	* @param url
	* @return
	* @throws Exception
	*/
	public static String getContent(String url) throws Exception{
	    StringBuilder sb = new StringBuilder();
	    
	    HttpClient client = new DefaultHttpClient();
	    HttpParams httpParams = client.getParams();
	    //设置网络超时参数
	    HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
	    HttpConnectionParams.setSoTimeout(httpParams, 5000);
	    HttpResponse response = client.execute(new HttpGet(url));
	    HttpEntity entity = response.getEntity();
	    if (entity != null) {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"), 8192);
	        
	        String line = null;
	        while ((line = reader.readLine())!= null){
	            sb.append(line + "\n");
	        }
	        reader.close();
	    }
	    return sb.toString();
	} 

	/**
	 * 根据流返回一个字符串信息
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private static String getStringFromInputStream(InputStream is)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;

		while ((len = is.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
		}
		is.close();

		String html = baos.toString(); // 把流中的数据转换成字符串, 采用的编码是: utf-8

		// String html = new String(baos.toByteArray(), "GBK");

		baos.close();
		return html;
	}
}
