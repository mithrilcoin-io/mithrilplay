package io.mithrilcoin.mithrilplay.common;


import android.app.Activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.mithrilcoin.mithrilplay.R;

public class TimeUtil {

	public static String getTime(Activity activity, long sTime){
		String time = "";
		int hours   = (int) ((sTime / (1000*60*60)) % 24);
		int minutes = (int) ((sTime / (1000*60)) % 60);
		int seconds = (int) (sTime / 1000) % 60 ;

		if(hours != 0){
			time += hours + activity.getString(R.string.hours) + " ";
		}
		if(minutes != 0){
			time += minutes + activity.getString(R.string.minutes) + " ";
		}
		if(seconds != 0){
			time += seconds + activity.getString(R.string.seconds) + " ";
		}
        Log.d("mithril", "time :" + time);

		return time;
	}

	public static String getTodayString(){
		String time = "";
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date currentTime = new Date();
		time = mSimpleDateFormat.format ( currentTime );
		return time;
	}

	public static String getTodayString(long aTime){
		String time = "";
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		time = mSimpleDateFormat.format (aTime);
		return time;
	}

	public static String getTodayTime(){
		String time = "";
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시", Locale.KOREA);
		Date currentTime = new Date();
		time = mSimpleDateFormat.format ( currentTime );
		return time;
	}

	public static String getTodayUtcTime() {
		String todayTime;
//        Calendar calendar = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//        todayTime = dateFormat.format(calendar.getTimeInMillis());
		todayTime = calendar.getTimeInMillis() + "";
		Log.d("mithril", "todayTime:"+ todayTime);
		return todayTime;
	}

}
