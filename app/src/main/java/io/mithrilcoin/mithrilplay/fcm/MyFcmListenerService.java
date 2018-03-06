package io.mithrilcoin.mithrilplay.fcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import io.mithrilcoin.mithrilplay.IntroActivity;
import io.mithrilcoin.mithrilplay.R;
import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.Log;
import io.mithrilcoin.mithrilplay.data.AppUsageStatManager;
import io.mithrilcoin.mithrilplay.view.AlterDialogActivity;


public class MyFcmListenerService extends FirebaseMessagingService {

    private NotificationManager mNotificationManager;
    private static PowerManager.WakeLock sCpuWakeLock;

    protected final String TAG = "mithril";

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Log.e(TAG,"FCM onMessageReceived " +message.toString());

        String from = message.getFrom();
        Map<String, String> data = message.getData();
        Log.i(TAG,"FCM data.toString() =" + data.toString());
        String title = data.get("title");
        String msg = data.get("message");
        String type = data.get("type");
        String url = data.get("url");
        String query = data.get("query");
        String idx = data.get("idx");
        Log.i(TAG,"FCM from " + from);
        Log.i(TAG,"FCM title " + title);
        Log.i(TAG,"FCM msg " + msg);
        Log.i(TAG,"FCM type " + type);
        Log.i(TAG,"FCM url " + url);
        Log.i(TAG,"FCM query " + query);
        Log.i(TAG,"FCM idx " + idx);

        final FCMVo FCMVo = getData(message.getData());

        if(type.equals(Constant.PUSH_RECEIVE_TYPE_GAME)){
            // get database query data
            AppUsageStatManager.sendQueryData(FCMVo);
            return;
        }

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent;
        JSONObject object;

        try {
        
            if (isAppInForeground(this)) {

                intent = new Intent(this, AlterDialogActivity.class);
                intent.putExtra(FCMVo.FCM_PUSH_INTENT_KEY, FCMVo);
                PendingIntent contentIntent = null;
                contentIntent = PendingIntent.getActivity(this, 8888, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                contentIntent.send();

            } else {

                try {
                    NotificationCompat.Builder builder = getBuilder(FCMVo);
                    mNotificationManager.notify(Constant.NOTIFICATION_INDEX, builder.build());
                    if (sCpuWakeLock != null) {
                        return;
                    }
                    PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
                    sCpuWakeLock = pm.newWakeLock(
                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                                    PowerManager.ON_AFTER_RELEASE, "hi");

                    sCpuWakeLock.acquire();


                    if (sCpuWakeLock != null) {
                        sCpuWakeLock.release();
                        sCpuWakeLock = null;
                    }

                } catch (Exception e) {

                }
            }

        } catch (Exception e) {
            Log.d(TAG, "Exception");
            e.printStackTrace();
        }
    }

    private static String getLauncherClassName(Context context){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(context.getPackageName());

        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent,0);
        if(resolveInfoList != null && resolveInfoList.size() > 0){
            return resolveInfoList.get(0).activityInfo.name;
        }
        return "";
    }


    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    private FCMVo getData(Map<String, String> data) {
        FCMVo FCMVo = new FCMVo();

        FCMVo.setFcmPushMessage(data.get(FCMVo.FCM_PUSH_KEY_MESSAGE));
        FCMVo.setFcmPushType(data.get(FCMVo.FCM_PUSH_KEY_TYPE));
        FCMVo.setFcmPushTitle(data.get(FCMVo.FCM_PUSH_KEY_TITLE));
        FCMVo.setFcmPushUrl(data.get(FCMVo.FCM_PUSH_KEY_URL));
        FCMVo.setFcmPushQuery(data.get(FCMVo.FCM_PUSH_KEY_QUERY));
        FCMVo.setFcmPushIdx(data.get(FCMVo.FCM_PUSH_KEY_IDX));

        if (Build.VERSION.SDK_INT > 18 && Build.MODEL.contains("Nexus 5")) {
            try {
                FCMVo.setFcmPushMessage(URLDecoder.decode(FCMVo.getFcmPushMessage(), "utf-8"));
            } catch (Exception e) {
            }
        } else {
            try {
                FCMVo.setFcmPushMessage(URLDecoder.decode(FCMVo.getFcmPushMessage(), "euc-kr"));
            } catch (Exception e) {


            }
        }
        return FCMVo;
    }


    public NotificationCompat.Builder getBuilder(FCMVo FCMVo) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round, null))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(FCMVo.getFcmPushMessage()))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setDefaults(Notification.DEFAULT_SOUND )
                .setContentTitle(FCMVo.getFcmPushTitle())
                .setContentText(FCMVo.getFcmPushMessage())
                .setColor(0xff999999);


        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Notification.PRIORITY_MAX);

        Intent intent = new Intent(this, IntroActivity.class);
        intent.putExtra(FCMVo.FCM_PUSH_INTENT_KEY, FCMVo);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(IntroActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        return mBuilder;
    }

    private boolean isAppInForeground(Context context) {
        String packageName = context.getApplicationInfo().packageName;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
            if (!tasks.isEmpty()) {
                ComponentName baseActivity = tasks.get(0).topActivity;
                if (packageName.equals(baseActivity.getPackageName())) {
                    return true;
                }
            }
            return false;
        } else {
            return isForeground(context);
        }

    }
    public boolean isForeground(Context context) {
        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();
        if(tasks.isEmpty())
            return false;

        for (ActivityManager.RunningAppProcessInfo task : tasks) {
            if(context.getPackageName().equalsIgnoreCase(task.processName)){
                return task.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }
}
