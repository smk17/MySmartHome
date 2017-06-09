package com.smk17.mysmarthome;

public class Constant {
	/**
	 * 线程运行时间间隔，毫秒级
	 */
	public static final int TASKTIME = 2000; 
	
	/**
	 * 主控和手机的通信类型
	 */
	public static final String socketType = "UDP";
	
	/**
	 * 主控与手机数据传输使用的字符集名称
	 */
	public static final String CharsetName = "gb2312";
	
	public static final String DB_NAME = "mysmarthome.db";
	public static final int DB_VERSION = 1;
	
	/**
	 * 主控发出的wifiSSid的前缀
	 */
	public static final String WIFINamePrefix = "mysmarthome";
	
	/**
	 * 应用更新下载的apk文件保存时用的名字
	 */
	public static final String UPDATE_SAVENAME = "mysmarthome-%s.apk";
	
	/**
	 * 应用风格-材料设计风格
	 */
	public static final int APPStyle_Material = 0xC0;
	/**
	 * 应用风格-苹果IOS设计风格
	 */
	public static final int APPStyle_IOS = 0xC1;
	/**
	 * 应用风格-win8的Metro设计风格
	 */
	public static final int APPStyle_Metro = 0xC2;
	/**
	 * 应用风格-工厂设计风格
	 */
	public static final int APPStyle_Factory = 0xC3;
	
	
	/**
	 *  填写从短信SDK应用后台注册得到的APPKEY
	 **/
	public static final String APPKEY = "98d6b24ebbe5";
	/**
	 * 填写从短信SDK应用后台注册得到的APPSECRET
	 */
	public static final String APPSECRET = "04719df9b011679a2812d2c719c7f44e";
	
	public static final int ID_MAINDEVICE_ALL = -1;
	public static final String MAINDEVICE_ALL = "全部主控";
	public static final int ID_ZONE_ALL = -1;
	public static final String ZONE_ALL = "全部区域";
	public static final int ID_CATEGORY_ALL = -1;
	public static final String CATEGORY_ALL = "全部类型";
	
	/**
	 * 热点模式下，连接主控的端口
	 */
	public static final Integer 		SERVER_PORT 		= 8888;
	/**
	 * 热点模式下，连接主控的服务器IP
	 */
	public static final String 		SERVER_IP 			= "192.168.5.254";
//	public static final String 		SERVER_IP 					= "192.168.43.174"; 
	public static final String 		SERVER_CONN 			= "conn";
	
	public static final String 		INFO_USERLOGIN 	= "loginvalue";
	public static final String 		INFO_THEME 			= "settheme";
	public static final String 		THEME_ID 					= "theme";
	
	/**
	 * 内网模式，即局域网，和主控在同一wifi下，可以连接云端
	 */
	public static final int Intranet_Mode  = 0x111;
	/**
	 * 外网模式，连接云端
	 */
	public static final int Network_Mode = 0x112;
	/**
	 * 热点模式，以主控为服务器
	 */
	public static final int  HotSpot_Mode  = 0x113;
	
	/**
	 * 系统设置页面标题
	 */
	public static final int[] SystemSettingsTitleNames  =  new  int[] {R.string.add_terminal_device,R.string.add_main_device,
		R.string.learn_device,R.string.add_infrared_device,R.string.terminal_device_management,R.string.main_device_management,
		R.string.zone_management,R.string.scene_management,R.string.camera_management,R.string.infrared_device_management,
		R.string.about};
	
	public static final int[] UserSettingsTitleNames  =  new  int[] {R.string.user_rename,R.string.user_binding,
		R.string.user_login,R.string.user_jurisdiction,R.string.theme,R.string.advanced_settings,R.string.user_quit};
	
	/**
	 * 背景色大全
	 */
	public static final String[] SelectorBackground = new  String[] {"#008b8b","#D2E7F5","#FFDEAD","#FFD700","#FFC0CB","#FFA07A",
		"#FF8C00","#FF6347","#FF00FF","#FF0000","#0770B1","#7A71BD","#F08080","#EE82EE","#DEB887","#70BB46","#F9C066",
		"#DDA0DD","#DCDCDC","#DB7093","#DAA520","#DA70D6","#D3D3D3","#D2691E","#CD853F","#CD5C5C","#C71585",
		"#C0C0C0","#B8860B","#B22222","#B0E0E6","#B0C4DE",
		"#AFEEEE","#ADFF2F","#ADD8E6","#9400D3","#9370DB","#8B008B","#8B0000","#8A2BE2","#808080","#7FFF00","#7B68EE",
		"#708090","#6A5ACD","#48D1CC","#32CD32","#228B22","#20B2AA","#00FF7F","#00FA9A","#00CED1","#008B8B","#006400"};
	/**
	 * 情景模式图标选择
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
	 * 设备类型图标组
	 */
	public static final int[] DeviceCategoryIcon= new  int[] {R.drawable.ic_add_white_48dp,R.drawable.ic_router,
		R.drawable.ic_strips,R.drawable.ic_intelligent,R.drawable.ic_lightbulb_outline_white_48dp,R.drawable.ic_switch};
	
	public static final int[] BindingIcon= new  int[] {R.drawable.qq,R.drawable.sina_weibo};
	
	//Handler消息whatID
	/**
	 * 热点模式下，接受信息的线程
	 */
	public static final int REVToHotSpotNet_ID =0xA11;
	/**
	 * 热点模式下发送信息到主控
	 */
	public static final int SendMsgToHotSpotNet_ID =0xA12;
	/**
	 * 下拉刷新
	 */
	public static final int Refreshable_ID =0xA13;
	/**
	 * 用户信息
	 */
	public static final int User_ID = 0xA14;
	/**
	 * 主控匹配模式状态
	 */
	public static final int MATCHDEVICE_ID  = 0xA0;
	/**
	 * 设备类型
	 */
	public static final int DEVICECATEGORY_ID = 0xA1;
	/**
	 * 设备基本信息
	 */
	public static final int DEVICESENSOR_ID = 0xA2;
	/**
	 * 主控
	 */
	public static final int MAINDEVICE_ID = 0xA21;
	public static final int Jurisdiction_ID = 0xA22;
	/**
	 * 区域信息
	 */
	public static final int DEVICEZONE_ID = 0xA3;
	/**
	 * 设备详细信息
	 */
	public static final int DEVICEPROPERTY_ID = 0xA4;
	/**
	 * 情景模式信息
	 */
	public static final int SCENE_ID = 0xA5;
	/**
	 * 设备重命名
	 */
	public static final int RENAMEDEVICE_ID = 0xA6;
	/**
	 * 匹配设备个数
	 */
	public static final int MainDeviceNumRows_ID = 0xA7;
	/**
	 * 登录云端
	 */
	public static final int LOGIN_SERVER_ID = 0x0A;
	/**
	 * 本地登录
	 */
	public static final int LOGIN_Local_ID = 0xA14;
	/**
	 * 更新删除后的成功与否信息
	 */
	public static final int STATUS_ID = 0xA8;
	/**
	 * 错误信息
	 */
	public static final int ERROR_ID = 0xA9;
	
	//服务器端
	public static final String URL_Domain_Name = "http://120.27.35.109/mysmarthome/";
//	private static final String URL_Domain_Name = "http://sma.sengmitnick.com/mysmarthome/";
	/**
	 * ak参数，连接云权限密钥，必需。
	 */
	public static final String REQUEST_AK="b0aaf366866c11e483d200163e016fe1";
	/**
	 * 1.登录，连接云，需要参数：username,password,ak
	 */
	public static final String URL_LOGIN_SERVER= URL_Domain_Name +  "login.php";
	/**
	 * 2.获取设备类型信息，需要参数：ak
	 */
    public static final String URL_GET_DEVICECATEGORY = URL_Domain_Name + "getDeviceCategory.php";
    /**
     * 3.获取设备基本及详情信息，需要参数：userid,ak
     */
    public static final String URL_GET_DEVICESENSOR = URL_Domain_Name + "getDeviceSensor.php";
    /**
     * 4.获取区域信息，需要参数：id,ak
     */
    public static final String URL_GET_DEVICEZONE = URL_Domain_Name + "getDeviceZone.php";
    /**
     * 5.获取情景模式信息，需要参数：id,ak
     */
    public static final String URL_GET_SCENE = URL_Domain_Name + "getScene.php";
    /**
     * 6.更新设备详情信息，相关类：DeviceProperty，需要参数：value,id,sensor_id,ak
     */
    public static final String URL_UPDATE_DEVICEPROPERTY = URL_Domain_Name + "updateDeviceProperty.php";
    /**
     * 7.更新情景模式按钮状态，相关类：DeviceSensor，需要参数：value,id,ak
     */
	public static final String URL_UPDATE_SCENE = URL_Domain_Name + "updateScene.php";
	/**
     * 8.更新终端基本信息，需要参数：name,zoneid,id,ak
     */
	public static final String URL_UPDATE_TERMINALDEVICE  = URL_Domain_Name + "updateDevice.php";
	/**
     * 9.与主控设备取消绑定，需要参数：userid,id,ak
     */
	public static final String URL_DELETE_MAINDEVICE =URL_Domain_Name + "deleteMainDevice.php";
	/**
     * 10.发送命令告诉主控删除某终端，需要参数：id,ak
     */
	public static final String URL_DELETE_TERMINALDEVICE =URL_Domain_Name + "deleteTerminalDevice.php";
	/**
	 * 11.发送命令告诉主控进入或取消匹配模式，需要参数：value,id,ak
	 */
	public static final String URL_UPDATE_MATCHMAINDEVICE =URL_Domain_Name + "matchMainDevice.php";
	/**
	 * 12.获取主控匹配模式的状态，需要参数：id,ak
	 */
	public static final String URL_GET_MATCHMAINDEVICE =URL_Domain_Name + "getMatchMainDevice.php";
	/**
	 * 13.关联终端设备状态为0，需要参数：id,ak
	 */
	public static final String URL_UPDATE_DEVICETAG =URL_Domain_Name + "updateDeviceTag.php";
	/**
	 * 14.获取要添加的主控信息，需要参数：uuid,ak
	 */
	public static final String URL_GET_NEWMAINDEVICE =URL_Domain_Name + "getNewMainDevice.php";
	/**
	 * 15.把主控和用户关联，使用户可以控制它，需要参数：sensorid,userid,ak
	 */
	public static final String URL_ADD_NEWMAINDEVICE =URL_Domain_Name + "addMainDevice.php";
	/**
	 * 16.若要添加的主控不存在云则插入，需要参数：uuid,ak
	 */
	public static final String URL_INSERT_NEWMAINDEVICE =URL_Domain_Name + "insertMainDevice.php";
	/**
	 * 17.重命名区域名字，需要参数：id,name,ak
	 */
	public static final String URL_UPDATE_ZONENAME =URL_Domain_Name + "updateZone.php";
	/**
	 * 18.新增区域名字，需要参数：sensorid,name,ak
	 */
	public static final String URL_ADD_ZONENAME =URL_Domain_Name + "insertZone.php";
	/**
	 * 18.删除区域，需要参数：id,zondid,ak
	 */
	public static final String URL_DELETE_ZONE =URL_Domain_Name + "deleteZone.php";
	 /**
     * 19.获取当前账号下所有区域信息，需要参数：userid,ak
     */
    public static final String URL_GET_ALLZONE = URL_Domain_Name + "getAllDeviceZone.php";
    /**
     * 20.获取当前账号下所有情景模式信息，需要参数：userid,ak
     */
    public static final String URL_GET_ALLSCENE = URL_Domain_Name + "getAllScene.php";
    /**
     * 21.获取单一情景模式信息，需要参数：id,ak
     */
    public static final String URL_GET_ONESCENE = URL_Domain_Name + "getOneScene.php";
    /**
     * 22.更新单一情景模式信息，需要参数：scene,ak
     */
    public static final String URL_UPDATE_ONESCENE = URL_Domain_Name + "updateOneScene.php";
    /**
     * 23.删除单一情景模式信息，需要参数：id,ak
     */
    public static final String URL_DELETE_SCENE = URL_Domain_Name + "deleteScene.php";
    /**
     * 24.插入单一情景模式信息，需要参数：scene,ak
     */
    public static final String URL_INSERT_SCENE = URL_Domain_Name + "insertScene.php";
    /**
     * 25.注册新用户，需要参数：phone,password,ak
     */
    public static final String URL_REGISTER_SERVER = URL_Domain_Name + "register.php";
    /**
     * 26.获取当前用户的信息，需要参数：userid,ak
     */
    public static final String URL_GET_USERINFO = URL_Domain_Name + "userinfo.php";
    /**
     * 27.获取最新版本信息
     */
    public static final String URL_VERJSON_SERVER = URL_Domain_Name + "ver.json";
    /**
     * 28.判断该手机号是否已注册，需要参数：userphone,ak
     */
    public static final String URL_EXISTS_USERPHONE = URL_Domain_Name + "existsPhoneNumber.php";
    /**
     * 29.重置用户密码，需要参数：phone,password,ak
     */
    public static final String URL_RESET_PASSWORD = URL_Domain_Name + "resetPassword.php";
    /**
     * 30.获取权限列表，需要参数：user_id,sensor_id,ak
     */
    public static final String URL_GET_JURISDICTION = URL_Domain_Name + "getJurisdiction.php";
    /**
     * 31.新加一个权限用户，需要参数：user_id,sensor_id,name,phone,ak
     */
    public static final String URL_INSERT_JURISDICTION = URL_Domain_Name + "insertJurisdiction.php";
    /**
     * 32.删除一个权限用户，需要参数：ID,ak
     */
    public static final String URL_DELETE_JURISDICTION = URL_Domain_Name + "deleteJurisdiction.php";
    /**
     * 30.获取最近登录设备列表，需要参数：user_id
     */
    public static final String URL_GET_LOGIN = URL_Domain_Name + "getLogin.php";
    /**
     * 32.删除一个登录设备，需要参数：ID
     */
    public static final String URL_DELETE_LOGIN = URL_Domain_Name + "deleteLogin.php";
    /**
     * 33.添加一个登录设备，需要参数：user_id,name
     */
    public static final String URL_INSERT_LOGIN = URL_Domain_Name + "insertLogin.php";
    /**
     * 34.更新用户信息，需要参数：user_id,ak,name or phone or email or avatar
     */
	public static final String URL_UPDATE_USERINFO = URL_Domain_Name + "updateUserinfo.php";
	/**
	 * OSS SDK中加签类型为原始AK/SK加签时请求服务器获取TOKEN ，需要参数content
	 */
	public static final String URL_GET_AKSK_TOKEN = URL_Domain_Name + "getAKSKToken.php";
}
