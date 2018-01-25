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
    public static final String APP_USER_INFO       = "/member/select/userInfo/{id}";         // 로그인 사용자 정보 조회
    public static final String APP_MEMBER_UPDATE   = "/member/update/memberDetail/{id}";         // 사용자 추가 정보 업데이트

    public static final String APP_GAMEDATA_INSERT        = "/gamedata/insert/{id}";         // 게임데이터 전송
    public static final String APP_GAMEDATA_PACKAGE       = "/gamedata/validate/{id}";         // APP 필터링(게임만 가져옴)
    public static final String APP_GAMEDATA_REWARD_ORDER       = "/gamedata/insert/reward/{id}";         // 데이터 리워드 요청
    public static final String APP_GAME_REWARD_ALL_LIST_GET      = "/gamedata/select/nopage/{id}";         // 전체 게임 데이터 조회



}
