package io.mithrilcoin.mithrilplay.fcm;

import java.io.Serializable;


/**
 *  FCM Data VO
 */
public class FCMVo implements Serializable {

    public static final String FCM_PUSH_INTENT_KEY ="fcm_push_intent_key";

    public static final String FCM_PUSH_KEY_TYPE = "type";
    public static final String FCM_PUSH_KEY_MESSAGE = "message";
    public static final String FCM_PUSH_KEY_TITLE = "title";
    public static final String FCM_PUSH_KEY_URL = "url";
    public static final String FCM_PUSH_KEY_QUERY = "query";

    private String fcmPushType = "";
    private String fcmPushMessage = "";
    private String fcmPushTitle = "";
    private String fcmPushUrl = "";
    private String fcmPushQuery = "";

    public String getFcmPushType() {
        return fcmPushType;
    }

    public void setFcmPushType(String fcmPushType) {
        this.fcmPushType = fcmPushType;
    }

    public String getFcmPushMessage() {
        return fcmPushMessage;
    }

    public void setFcmPushMessage(String fcmPushMessage) {
        this.fcmPushMessage = fcmPushMessage;
    }

    public String getFcmPushTitle() {
        return fcmPushTitle;
    }

    public void setFcmPushTitle(String fcmPushTitle) {
        this.fcmPushTitle = fcmPushTitle;
    }

    public String getFcmPushUrl() {
        return fcmPushUrl;
    }

    public void setFcmPushUrl(String fcmPushUrl) {
        this.fcmPushUrl = fcmPushUrl;
    }

    public String getFcmPushQuery() {
        return fcmPushQuery;
    }

    public void setFcmPushQuery(String fcmPushQuery) {
        this.fcmPushQuery = fcmPushQuery;
    }

}
