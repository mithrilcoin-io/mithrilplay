package io.mithrilcoin.mithrilplay.common;


public class ServerConstant {

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
