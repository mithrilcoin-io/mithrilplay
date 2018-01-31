package io.mithrilcoin.mithrilplay.common;

public class ServerConstant {

    //  host mode
    public static final int REAL_SERVER_MODE                = 0;
    public static final int DEV_SERVER_MODE                 = REAL_SERVER_MODE + 1;

    //  Set HostUrl
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

    // API Address
    public static final String APP_MEMBER_JOIN  = "/member/signup";            // Sign Up
    public static final String APP_LOGIN        = "/member/signin/";           // Login
    public static final String APP_LOGOUT       = "/member/signout/{id}";          // Logout
    public static final String APP_SEND_EMAIL_ORDER       = "/member/sendmail/auth/{id}";         // Request to send member authentication e-mail
    public static final String APP_USER_INFO       = "/member/select/userInfo/{id}";         // View login user information
    public static final String APP_MEMBER_UPDATE   = "/member/update/memberDetail/{id}";         // Update additional user information

    public static final String APP_GAMEDATA_INSERT        = "/gamedata/insert/{id}";                     // Game data transfer
    public static final String APP_GAMEDATA_PACKAGE       = "/gamedata/validate/{id}";                   // APP filtering (bring games only)
    public static final String APP_GAMEDATA_REWARD_ORDER       = "/gamedata/insert/reward/{id}";         // Game Reward Request
    public static final String APP_GAME_REWARD_ALL_LIST_GET      = "/gamedata/select/nopage/{id}";       // View all game data

}
