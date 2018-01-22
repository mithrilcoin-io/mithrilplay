package io.mithrilcoin.mithrilplay.common;

public class ServerConstant {

    //  host 모음
    public static final int REAL_SERVER_MODE                = 0;
    public static final int DEV_SERVER_MODE                 = REAL_SERVER_MODE + 1;

    //  연결 HostUrl
    public static final int CONNECT_SERVER_MODE      = DEV_SERVER_MODE;

    private static String hostUrl = "http://dev.mithril.io";

    public static String getHostUrl(){
        if(CONNECT_SERVER_MODE == REAL_SERVER_MODE){
            hostUrl               = "http://dev.mithril.io";
        }else if(CONNECT_SERVER_MODE == DEV_SERVER_MODE){
            hostUrl               = "http://dev-play.mithrilcoin.io:80";
        }

        return hostUrl;
    }

    //--------------------------------------------------

    // API Address
    public static final String APP_MEMBER_JOIN  = "/member/signup";            // 회원가입
    public static final String APP_LOGIN        = "/member/signin/";           // 로그인
    public static final String APP_LOGOUT       = "/member/signout/{id}";          // 로그아웃
    public static final String APP_SEND_EMAIL_ORDER       = "/member/sendmail/auth/{id}";         // 회원인증 이메일 전송요청
    public static final String APP_USER_INFO       = "/member/select/userInfo";         // 로그인 사용자 정보 조회
    public static final String APP_MEMBER_UPDATE       = "/update/memberDetail/{id}";         // 사용자 추가 정보 업데이트

}
