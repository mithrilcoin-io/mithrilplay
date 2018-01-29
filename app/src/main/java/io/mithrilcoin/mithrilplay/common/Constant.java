package io.mithrilcoin.mithrilplay.common;

public class Constant {

	public static final int NOTIFICATION_INDEX = 836809014;
	public static final int ANDROID_NOUGHT = 24;

	public static final int OTHER_LOGIN		= 409;	// 다른 기기에서 로그인

	public static final String USER_STATUS_NOT_AUTH		= "M001001";	// 미인증 상태
	public static final String USER_STATUS_AUTH_ON		= "M001002";	// 정상 (인증)
	public static final String USER_AUTH_PLUS_PROFILE	= "M001003";	// 추가정보 입력 완료

	public static final String TAG_AUTH_EMAIL_ID = "auth_email_id"; 	// 아이디 (로그인이나 회원가입시 서버에서 내려주는 ID)

	public static final int REQUEST_CODE_INTRO_PERMISSION = 1000;		// 인트로에서 권한허용 이동시
	public static final int REQUEST_PACKAGE_USAGE_STATS = 1001;		// 다른 앱정보 가져오기 위해 권한 허용 요청
	public static final int REQUEST_JOIN_MOREINFO = 1002;		// 회원가입 => 추가정보 입력
	public static final int REQUEST_SETTING_MOREINFO = 1003;		// setting => 추가정보 입력
	public static final int REQUEST_INTRO_EMAILAUTH = 1004;		// intro => 이메일 인증

}
