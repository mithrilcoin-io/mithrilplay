package io.mithrilcoin.mithrilplay.common;

public class Constant {

	public static final int OTHER_LOGIN		= 409;			// Sign in from another device
	public static final String SUCCESS		= "SUCCESS";	// network succeed

	public static final String USER_STATUS_NOT_AUTH		= "M001001";	// Unauthorized
	public static final String USER_STATUS_AUTH_ON		= "M001002";	// Email verification completed
	public static final String USER_AUTH_PLUS_PROFILE	= "M001003";	// Complete more info

	public static final String REWARD_TYPE_INFO_ADD	= "T003002";	// Additional information input reward
	public static final String REWARD_TYPE_GAME		= "T003003";	// game working reward

	public static final String TAG_AUTH_EMAIL_ID = "auth_email_id"; 	// ID (ID that the server gives at login or membership)

	public static final int REQUEST_CODE_INTRO_USE_INFO = 1000;		// When the Intro requests permission to access usage information
	public static final int REQUEST_PACKAGE_USAGE_STATS = 1001;		// Request permission to fetch other app information
	public static final int REQUEST_JOIN_MOREINFO = 1002;			// SignUp => MoreInfo
	public static final int REQUEST_SETTING_MOREINFO = 1003;		// Setting => MoreInfo
	public static final int REQUEST_INTRO_EMAILAUTH = 1004;			// Intro => VerifyEmail
	public static final int REQUEST_CODE_HOME_USE_INFO = 1005;		// When the Home requests permission to access usage information

}
