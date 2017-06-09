package com.smk17.android.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.callback.GetFileCallback;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.AccessControlList;
import com.alibaba.sdk.android.oss.model.AuthenticationType;
import com.alibaba.sdk.android.oss.model.ClientConfiguration;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.alibaba.sdk.android.oss.model.TokenGenerator;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSFile;
import com.alibaba.sdk.android.oss.util.OSSLog;
import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.Utils.HttpUtils;

public class ToolOssService {
	private static String TAG = "ToolOssService";
	
	private static OSSService ossService;
	private static OSSBucket sampleBucket;
	
	public static void initOssService(Context context) {
        // 开启Log
        OSSLog.enableLog();

        ossService = OSSServiceProvider.getService();

        ossService.setApplicationContext(context);
        ossService.setGlobalDefaultHostId("oss-cn-qingdao.aliyuncs.com"); // 设置region host 即 endpoint
        ossService.setGlobalDefaultACL(AccessControlList.PRIVATE); // 默认为private        
        ossService.setAuthenticationType(AuthenticationType.ORIGIN_AKSK); // 设置加签类型为原始AK/SK加签
        ossService.setGlobalDefaultTokenGenerator(new TokenGenerator() { // 设置全局默认加签器
            @Override
            public String generateToken(String httpMethod, String md5, String type, String date,
                                        String ossHeaders, String resource) {

                String content = httpMethod + "\n" + md5 + "\n" + type + "\n" + date + "\n" + ossHeaders
                        + resource;
                Map<String, String> params = new HashMap<String, String>();
                params.put("content", content);
                String Token = "";
                try {
                	Token = HttpUtils.Post(Constant.URL_GET_AKSK_TOKEN, params);
                	//Log.e(TAG, "SERVER_Token="+Token+"\nLocal_Token="+OSSToolKit.generateToken(accessKey, screctKey, content));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}                
                return Token;
            }
        });
//        ossService.setAuthenticationType(AuthenticationType.FEDERATION_TOKEN); // 设置加签方式为STS FederToken鉴权方式
//        ossService.setGlobalDefaultStsTokenGetter(new StsTokenGetter() {
//
//			@Override
//			public OSSFederationToken getFederationToken() {
//				// 在这里编写您的代码，实现获取一个新的STS Token
//	            // 一般情况下，这个Token应该是通过网络请求去到您的业务服务器获取
//	            // 注意，返回的OSSFederationToken必须包含有效的四个字段：tempAK/tempSK/securityToken/expiration
//	            // expiration值为Token的失效时间，格式为UNIX Epoch时间，即从协调世界时1970年1月1日0时0分0秒起到Token失效时刻的总秒数
//				return null;
//			}
//            
//        });
        ossService.setCustomStandardTimeWithEpochSec(System.currentTimeMillis() / 1000);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectTimeout(30 * 1000); // 设置全局网络连接超时时间，默认30s
        conf.setSocketTimeout(30 * 1000); // 设置全局socket超时时间，默认30s
        conf.setMaxConcurrentTaskNum(5); // 替换设置最大连接数接口，设置全局最大并发任务数，默认为6
        conf.setIsSecurityTunnelRequired(false); // 是否使用https，默认为false
        ossService.setClientConfiguration(conf);
        sampleBucket = ossService.getOssBucket("sengmitnick");//http://oss.sengmitnick.com/
        sampleBucket.setBucketACL(AccessControlList.PRIVATE); // 指明该Bucket的访问权限
        sampleBucket.setBucketHostId("oss-cn-qingdao.aliyuncs.com"); // 指明该Bucket所在数据中心的域名或已经绑定Bucket的CNAME域名
    }
	
	public static void upload(final File file,final String filename){
		OSSFile bigfFile = ossService.getOssFile(sampleBucket, "mysmarthome/UserAvatar/" + filename);
        try {
        	Log.d(TAG,file.getAbsolutePath());
            bigfFile.setUploadFilePath(file.getAbsolutePath(), "image/jpeg");
            bigfFile.ResumableUploadInBackground(new SaveCallback() {

                @Override
                public void onSuccess(String objectKey) {
                    Log.d(TAG, "[onSuccess] - " + objectKey + " upload success!");
                }

                @Override
                public void onProgress(String objectKey, int byteCount, int totalSize) {
                    Log.d(TAG, "[onProgress] - current upload " + objectKey + " bytes: " + byteCount + " in total: " + totalSize);
                }

                @SuppressWarnings("deprecation")
				@Override
                public void onFailure(String objectKey, OSSException ossException) {
                    Log.e(TAG, "[onFailure] - upload " + objectKey + " failed!\n" + ossException.toString());
                    ossException.printStackTrace();
                    ossException.getException().printStackTrace();
                }

            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}
	
	// 断点下载
    public static void download(final File file,final String filename ,GetFileCallback getFileCallback) {
        OSSFile bigFile = ossService.getOssFile(sampleBucket, "mysmarthome/UserAvatar/" + filename);
        bigFile.ResumableDownloadToInBackground(file.getAbsolutePath(), getFileCallback);
    }
	
	public static void upload(final String filename,final String contentType){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				OSSFile ossFile = ossService.getOssFile(sampleBucket,  filename);
				File temp = MyApplication.gainContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
				try {
					ossFile.setUploadFilePath(temp.getPath()+filename, contentType);// 指明需要上传的文件的路径，和文件内容类型，如果文件不存在将抛出异常
					ossFile.enableUploadCheckMd5sum(); // 开启上传md5校验
					ossFile.upload(); // 上传失败将会抛出异常
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 				
			}
		}).start();
		
	}
	
	public static String generateToken(String accessKey, String screctKey, String content)
	{
	    String signature = null;
	    try
	    {
	        signature =getHmacSha1Signature(content, screctKey);
	        signature = signature.trim();
	    }
	    catch(Exception e)
	    {
	        OSSLog.logD(e.toString());
	    }
	    OSSLog.logD((new StringBuilder()).append("[genAuth] - signature: ").append(signature).toString());
	    return (new StringBuilder()).append("OSS ").append(accessKey).append(":").append(signature).toString();
	}
	
	public static String getHmacSha1Signature(String value, String key)
	        throws NoSuchAlgorithmException, InvalidKeyException
	    {
	        byte keyBytes[] = key.getBytes();
	        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
	        Mac mac = Mac.getInstance("HmacSHA1");
	        mac.init(signingKey);
	        byte rawHmac[] = mac.doFinal(value.getBytes());
	        return (new String(Base64.encode(rawHmac, 0))).trim();
	    }

		
}
