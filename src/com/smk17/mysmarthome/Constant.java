package com.smk17.mysmarthome;

public class Constant {
	/**
	 * �߳�����ʱ���������뼶
	 */
	public static final int TASKTIME = 2000; 
	
	/**
	 * ���غ��ֻ���ͨ������
	 */
	public static final String socketType = "UDP";
	
	/**
	 * �������ֻ����ݴ���ʹ�õ��ַ�������
	 */
	public static final String CharsetName = "gb2312";
	
	public static final String DB_NAME = "mysmarthome.db";
	public static final int DB_VERSION = 1;
	
	/**
	 * ���ط�����wifiSSid��ǰ׺
	 */
	public static final String WIFINamePrefix = "mysmarthome";
	
	/**
	 * Ӧ�ø������ص�apk�ļ�����ʱ�õ�����
	 */
	public static final String UPDATE_SAVENAME = "mysmarthome-%s.apk";
	
	/**
	 * Ӧ�÷��-������Ʒ��
	 */
	public static final int APPStyle_Material = 0xC0;
	/**
	 * Ӧ�÷��-ƻ��IOS��Ʒ��
	 */
	public static final int APPStyle_IOS = 0xC1;
	/**
	 * Ӧ�÷��-win8��Metro��Ʒ��
	 */
	public static final int APPStyle_Metro = 0xC2;
	/**
	 * Ӧ�÷��-������Ʒ��
	 */
	public static final int APPStyle_Factory = 0xC3;
	
	
	/**
	 *  ��д�Ӷ���SDKӦ�ú�̨ע��õ���APPKEY
	 **/
	public static final String APPKEY = "98d6b24ebbe5";
	/**
	 * ��д�Ӷ���SDKӦ�ú�̨ע��õ���APPSECRET
	 */
	public static final String APPSECRET = "04719df9b011679a2812d2c719c7f44e";
	
	public static final int ID_MAINDEVICE_ALL = -1;
	public static final String MAINDEVICE_ALL = "ȫ������";
	public static final int ID_ZONE_ALL = -1;
	public static final String ZONE_ALL = "ȫ������";
	public static final int ID_CATEGORY_ALL = -1;
	public static final String CATEGORY_ALL = "ȫ������";
	
	/**
	 * �ȵ�ģʽ�£��������صĶ˿�
	 */
	public static final Integer 		SERVER_PORT 		= 8888;
	/**
	 * �ȵ�ģʽ�£��������صķ�����IP
	 */
	public static final String 		SERVER_IP 			= "192.168.5.254";
//	public static final String 		SERVER_IP 					= "192.168.43.174"; 
	public static final String 		SERVER_CONN 			= "conn";
	
	public static final String 		INFO_USERLOGIN 	= "loginvalue";
	public static final String 		INFO_THEME 			= "settheme";
	public static final String 		THEME_ID 					= "theme";
	
	/**
	 * ����ģʽ��������������������ͬһwifi�£����������ƶ�
	 */
	public static final int Intranet_Mode  = 0x111;
	/**
	 * ����ģʽ�������ƶ�
	 */
	public static final int Network_Mode = 0x112;
	/**
	 * �ȵ�ģʽ��������Ϊ������
	 */
	public static final int  HotSpot_Mode  = 0x113;
	
	/**
	 * ϵͳ����ҳ�����
	 */
	public static final int[] SystemSettingsTitleNames  =  new  int[] {R.string.add_terminal_device,R.string.add_main_device,
		R.string.learn_device,R.string.add_infrared_device,R.string.terminal_device_management,R.string.main_device_management,
		R.string.zone_management,R.string.scene_management,R.string.camera_management,R.string.infrared_device_management,
		R.string.about};
	
	public static final int[] UserSettingsTitleNames  =  new  int[] {R.string.user_rename,R.string.user_binding,
		R.string.user_login,R.string.user_jurisdiction,R.string.theme,R.string.advanced_settings,R.string.user_quit};
	
	/**
	 * ����ɫ��ȫ
	 */
	public static final String[] SelectorBackground = new  String[] {"#008b8b","#D2E7F5","#FFDEAD","#FFD700","#FFC0CB","#FFA07A",
		"#FF8C00","#FF6347","#FF00FF","#FF0000","#0770B1","#7A71BD","#F08080","#EE82EE","#DEB887","#70BB46","#F9C066",
		"#DDA0DD","#DCDCDC","#DB7093","#DAA520","#DA70D6","#D3D3D3","#D2691E","#CD853F","#CD5C5C","#C71585",
		"#C0C0C0","#B8860B","#B22222","#B0E0E6","#B0C4DE",
		"#AFEEEE","#ADFF2F","#ADD8E6","#9400D3","#9370DB","#8B008B","#8B0000","#8A2BE2","#808080","#7FFF00","#7B68EE",
		"#708090","#6A5ACD","#48D1CC","#32CD32","#228B22","#20B2AA","#00FF7F","#00FA9A","#00CED1","#008B8B","#006400"};
	/**
	 * �龰ģʽͼ��ѡ��
	 */
	public static final int[] SelectorIcon = new  int[] {R.drawable.ic_audiotrack_white_48dp,R.drawable.ic_add_white_48dp,
		R.drawable.ic_book_open_white_48dp,R.drawable.ic_hotel_white_48dp,R.drawable.ic_lightbulb_white_48dp,
		R.drawable.ic_seat,R.drawable.ic_sofa,R.drawable.ic_radiator,R.drawable.ic_mirror,R.drawable.ic_air_conditioning,
		R.drawable.ic_bell,R.drawable.ic_table_lamp,R.drawable.ic_chair,R.drawable.ic_desk,R.drawable.ic_high_seat,
		R.drawable.ic_laptop,R.drawable.ic_bedside_lamp,R.drawable.ic_tea,R.drawable.ic_kitchen,R.drawable.ic_sound,
		R.drawable.ic_potted,R.drawable.ic_game,R.drawable.ic_more_sofa,R.drawable.ic_news,R.drawable.ic_meeting,
		R.drawable.ic_bookshelf,R.drawable.ic_dressing_table,R.drawable.ic_landline,R.drawable.ic_hanger,
		R.drawable.ic_television};
	/**
	 * �豸����ͼ����
	 */
	public static final int[] DeviceCategoryIcon= new  int[] {R.drawable.ic_add_white_48dp,R.drawable.ic_router,
		R.drawable.ic_strips,R.drawable.ic_intelligent,R.drawable.ic_lightbulb_outline_white_48dp,R.drawable.ic_switch};
	
	public static final int[] BindingIcon= new  int[] {R.drawable.qq,R.drawable.sina_weibo};
	
	//Handler��ϢwhatID
	/**
	 * �ȵ�ģʽ�£�������Ϣ���߳�
	 */
	public static final int REVToHotSpotNet_ID =0xA11;
	/**
	 * �ȵ�ģʽ�·�����Ϣ������
	 */
	public static final int SendMsgToHotSpotNet_ID =0xA12;
	/**
	 * ����ˢ��
	 */
	public static final int Refreshable_ID =0xA13;
	/**
	 * �û���Ϣ
	 */
	public static final int User_ID = 0xA14;
	/**
	 * ����ƥ��ģʽ״̬
	 */
	public static final int MATCHDEVICE_ID  = 0xA0;
	/**
	 * �豸����
	 */
	public static final int DEVICECATEGORY_ID = 0xA1;
	/**
	 * �豸������Ϣ
	 */
	public static final int DEVICESENSOR_ID = 0xA2;
	/**
	 * ����
	 */
	public static final int MAINDEVICE_ID = 0xA21;
	public static final int Jurisdiction_ID = 0xA22;
	/**
	 * ������Ϣ
	 */
	public static final int DEVICEZONE_ID = 0xA3;
	/**
	 * �豸��ϸ��Ϣ
	 */
	public static final int DEVICEPROPERTY_ID = 0xA4;
	/**
	 * �龰ģʽ��Ϣ
	 */
	public static final int SCENE_ID = 0xA5;
	/**
	 * �豸������
	 */
	public static final int RENAMEDEVICE_ID = 0xA6;
	/**
	 * ƥ���豸����
	 */
	public static final int MainDeviceNumRows_ID = 0xA7;
	/**
	 * ��¼�ƶ�
	 */
	public static final int LOGIN_SERVER_ID = 0x0A;
	/**
	 * ���ص�¼
	 */
	public static final int LOGIN_Local_ID = 0xA14;
	/**
	 * ����ɾ����ĳɹ������Ϣ
	 */
	public static final int STATUS_ID = 0xA8;
	/**
	 * ������Ϣ
	 */
	public static final int ERROR_ID = 0xA9;
	
	//��������
	public static final String URL_Domain_Name = "http://120.27.35.109/mysmarthome/";
//	private static final String URL_Domain_Name = "http://sma.sengmitnick.com/mysmarthome/";
	/**
	 * ak������������Ȩ����Կ�����衣
	 */
	public static final String REQUEST_AK="b0aaf366866c11e483d200163e016fe1";
	/**
	 * 1.��¼�������ƣ���Ҫ������username,password,ak
	 */
	public static final String URL_LOGIN_SERVER= URL_Domain_Name +  "login.php";
	/**
	 * 2.��ȡ�豸������Ϣ����Ҫ������ak
	 */
    public static final String URL_GET_DEVICECATEGORY = URL_Domain_Name + "getDeviceCategory.php";
    /**
     * 3.��ȡ�豸������������Ϣ����Ҫ������userid,ak
     */
    public static final String URL_GET_DEVICESENSOR = URL_Domain_Name + "getDeviceSensor.php";
    /**
     * 4.��ȡ������Ϣ����Ҫ������id,ak
     */
    public static final String URL_GET_DEVICEZONE = URL_Domain_Name + "getDeviceZone.php";
    /**
     * 5.��ȡ�龰ģʽ��Ϣ����Ҫ������id,ak
     */
    public static final String URL_GET_SCENE = URL_Domain_Name + "getScene.php";
    /**
     * 6.�����豸������Ϣ������ࣺDeviceProperty����Ҫ������value,id,sensor_id,ak
     */
    public static final String URL_UPDATE_DEVICEPROPERTY = URL_Domain_Name + "updateDeviceProperty.php";
    /**
     * 7.�����龰ģʽ��ť״̬������ࣺDeviceSensor����Ҫ������value,id,ak
     */
	public static final String URL_UPDATE_SCENE = URL_Domain_Name + "updateScene.php";
	/**
     * 8.�����ն˻�����Ϣ����Ҫ������name,zoneid,id,ak
     */
	public static final String URL_UPDATE_TERMINALDEVICE  = URL_Domain_Name + "updateDevice.php";
	/**
     * 9.�������豸ȡ���󶨣���Ҫ������userid,id,ak
     */
	public static final String URL_DELETE_MAINDEVICE =URL_Domain_Name + "deleteMainDevice.php";
	/**
     * 10.���������������ɾ��ĳ�նˣ���Ҫ������id,ak
     */
	public static final String URL_DELETE_TERMINALDEVICE =URL_Domain_Name + "deleteTerminalDevice.php";
	/**
	 * 11.��������������ؽ����ȡ��ƥ��ģʽ����Ҫ������value,id,ak
	 */
	public static final String URL_UPDATE_MATCHMAINDEVICE =URL_Domain_Name + "matchMainDevice.php";
	/**
	 * 12.��ȡ����ƥ��ģʽ��״̬����Ҫ������id,ak
	 */
	public static final String URL_GET_MATCHMAINDEVICE =URL_Domain_Name + "getMatchMainDevice.php";
	/**
	 * 13.�����ն��豸״̬Ϊ0����Ҫ������id,ak
	 */
	public static final String URL_UPDATE_DEVICETAG =URL_Domain_Name + "updateDeviceTag.php";
	/**
	 * 14.��ȡҪ��ӵ�������Ϣ����Ҫ������uuid,ak
	 */
	public static final String URL_GET_NEWMAINDEVICE =URL_Domain_Name + "getNewMainDevice.php";
	/**
	 * 15.�����غ��û�������ʹ�û����Կ���������Ҫ������sensorid,userid,ak
	 */
	public static final String URL_ADD_NEWMAINDEVICE =URL_Domain_Name + "addMainDevice.php";
	/**
	 * 16.��Ҫ��ӵ����ز�����������룬��Ҫ������uuid,ak
	 */
	public static final String URL_INSERT_NEWMAINDEVICE =URL_Domain_Name + "insertMainDevice.php";
	/**
	 * 17.�������������֣���Ҫ������id,name,ak
	 */
	public static final String URL_UPDATE_ZONENAME =URL_Domain_Name + "updateZone.php";
	/**
	 * 18.�����������֣���Ҫ������sensorid,name,ak
	 */
	public static final String URL_ADD_ZONENAME =URL_Domain_Name + "insertZone.php";
	/**
	 * 18.ɾ��������Ҫ������id,zondid,ak
	 */
	public static final String URL_DELETE_ZONE =URL_Domain_Name + "deleteZone.php";
	 /**
     * 19.��ȡ��ǰ�˺�������������Ϣ����Ҫ������userid,ak
     */
    public static final String URL_GET_ALLZONE = URL_Domain_Name + "getAllDeviceZone.php";
    /**
     * 20.��ȡ��ǰ�˺��������龰ģʽ��Ϣ����Ҫ������userid,ak
     */
    public static final String URL_GET_ALLSCENE = URL_Domain_Name + "getAllScene.php";
    /**
     * 21.��ȡ��һ�龰ģʽ��Ϣ����Ҫ������id,ak
     */
    public static final String URL_GET_ONESCENE = URL_Domain_Name + "getOneScene.php";
    /**
     * 22.���µ�һ�龰ģʽ��Ϣ����Ҫ������scene,ak
     */
    public static final String URL_UPDATE_ONESCENE = URL_Domain_Name + "updateOneScene.php";
    /**
     * 23.ɾ����һ�龰ģʽ��Ϣ����Ҫ������id,ak
     */
    public static final String URL_DELETE_SCENE = URL_Domain_Name + "deleteScene.php";
    /**
     * 24.���뵥һ�龰ģʽ��Ϣ����Ҫ������scene,ak
     */
    public static final String URL_INSERT_SCENE = URL_Domain_Name + "insertScene.php";
    /**
     * 25.ע�����û�����Ҫ������phone,password,ak
     */
    public static final String URL_REGISTER_SERVER = URL_Domain_Name + "register.php";
    /**
     * 26.��ȡ��ǰ�û�����Ϣ����Ҫ������userid,ak
     */
    public static final String URL_GET_USERINFO = URL_Domain_Name + "userinfo.php";
    /**
     * 27.��ȡ���°汾��Ϣ
     */
    public static final String URL_VERJSON_SERVER = URL_Domain_Name + "ver.json";
    /**
     * 28.�жϸ��ֻ����Ƿ���ע�ᣬ��Ҫ������userphone,ak
     */
    public static final String URL_EXISTS_USERPHONE = URL_Domain_Name + "existsPhoneNumber.php";
    /**
     * 29.�����û����룬��Ҫ������phone,password,ak
     */
    public static final String URL_RESET_PASSWORD = URL_Domain_Name + "resetPassword.php";
    /**
     * 30.��ȡȨ���б���Ҫ������user_id,sensor_id,ak
     */
    public static final String URL_GET_JURISDICTION = URL_Domain_Name + "getJurisdiction.php";
    /**
     * 31.�¼�һ��Ȩ���û�����Ҫ������user_id,sensor_id,name,phone,ak
     */
    public static final String URL_INSERT_JURISDICTION = URL_Domain_Name + "insertJurisdiction.php";
    /**
     * 32.ɾ��һ��Ȩ���û�����Ҫ������ID,ak
     */
    public static final String URL_DELETE_JURISDICTION = URL_Domain_Name + "deleteJurisdiction.php";
    /**
     * 30.��ȡ�����¼�豸�б���Ҫ������user_id
     */
    public static final String URL_GET_LOGIN = URL_Domain_Name + "getLogin.php";
    /**
     * 32.ɾ��һ����¼�豸����Ҫ������ID
     */
    public static final String URL_DELETE_LOGIN = URL_Domain_Name + "deleteLogin.php";
    /**
     * 33.���һ����¼�豸����Ҫ������user_id,name
     */
    public static final String URL_INSERT_LOGIN = URL_Domain_Name + "insertLogin.php";
    /**
     * 34.�����û���Ϣ����Ҫ������user_id,ak,name or phone or email or avatar
     */
	public static final String URL_UPDATE_USERINFO = URL_Domain_Name + "updateUserinfo.php";
	/**
	 * OSS SDK�м�ǩ����ΪԭʼAK/SK��ǩʱ�����������ȡTOKEN ����Ҫ����content
	 */
	public static final String URL_GET_AKSK_TOKEN = URL_Domain_Name + "getAKSKToken.php";
}
