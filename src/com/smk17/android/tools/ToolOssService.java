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
        // ����Log
        OSSLog.enableLog();

        ossService = OSSServiceProvider.getService();

        ossService.setApplicationContext(context);
        ossService.setGlobalDefaultHostId("oss-cn-qingdao.aliyuncs.com"); // ����region host �� endpoint
        ossService.setGlobalDefaultACL(AccessControlList.PRIVATE); // Ĭ��Ϊprivate        
        ossService.setAuthenticationType(AuthenticationType.ORIGIN_AKSK); // ���ü�ǩ����ΪԭʼAK/SK��ǩ
        ossService.setGlobalDefaultTokenGenerator(new TokenGenerator() { // ����ȫ��Ĭ�ϼ�ǩ��
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
//        ossService.setAuthenticationType(AuthenticationType.FEDERATION_TOKEN); // ���ü�ǩ��ʽΪSTS FederToken��Ȩ��ʽ
//        ossService.setGlobalDefaultStsTokenGetter(new StsTokenGetter() {
//
//			@Override
//			public OSSFederationToken getFederationToken() {
//				// �������д���Ĵ��룬ʵ�ֻ�ȡһ���µ�STS Token
//	            // һ������£����TokenӦ����ͨ����������ȥ������ҵ���������ȡ
//	            // ע�⣬���ص�OSSFederationToken���������Ч���ĸ��ֶΣ�tempAK/tempSK/securityToken/expiration
//	            // expirationֵΪToken��ʧЧʱ�䣬��ʽΪUNIX Epochʱ�䣬����Э������ʱ1970��1��1��0ʱ0��0����TokenʧЧʱ�̵�������
//				return null;
//			}
//            
//        });
        ossService.setCustomStandardTimeWithEpochSec(System.currentTimeMillis() / 1000);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectTimeout(30 * 1000); // ����ȫ���������ӳ�ʱʱ�䣬Ĭ��30s
        conf.setSocketTimeout(30 * 1000); // ����ȫ��socket��ʱʱ�䣬Ĭ��30s
        conf.setMaxConcurrentTaskNum(5); // �滻��������������ӿڣ�����ȫ����󲢷���������Ĭ��Ϊ6
        conf.setIsSecurityTunnelRequired(false); // �Ƿ�ʹ��https��Ĭ��Ϊfalse
        ossService.setClientConfiguration(conf);
        sampleBucket = ossService.getOssBucket("sengmitnick");//http://oss.sengmitnick.com/
        sampleBucket.setBucketACL(AccessControlList.PRIVATE); // ָ����Bucket�ķ���Ȩ��
        sampleBucket.setBucketHostId("oss-cn-qingdao.aliyuncs.com"); // ָ����Bucket�����������ĵ��������Ѿ���Bucket��CNAME����
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
	
	// �ϵ�����
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
					ossFile.setUploadFilePath(temp.getPath()+filename, contentType);// ָ����Ҫ�ϴ����ļ���·�������ļ��������ͣ�����ļ������ڽ��׳��쳣
					ossFile.enableUploadCheckMd5sum(); // �����ϴ�md5У��
					ossFile.upload(); // �ϴ�ʧ�ܽ����׳��쳣
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
