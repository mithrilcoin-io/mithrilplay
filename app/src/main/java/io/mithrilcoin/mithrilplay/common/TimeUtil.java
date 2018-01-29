package io.mithrilcoin.mithrilplay.common;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {

	public static String getTime(long sTime){
		String time = "";
		int hours   = (int) ((sTime / (1000*60*60)) % 24);  //시
		int minutes = (int) ((sTime / (1000*60)) % 60);     //분
		int seconds = (int) (sTime / 1000) % 60 ;           //초

		if(hours != 0){
			time += hours + "시간 ";
		}
		if(minutes != 0){
			time += minutes + "분 ";
		}
		if(seconds != 0){
			time += seconds + "초";
		}
        Log.d("mithril", "시간 :" + time);

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

	// Local Time -> UTC/GMT Time
	public static long convertLocalTimeToUTC(long pv_localDateTime){
		long lv_UTCTime = pv_localDateTime;

		TimeZone z = TimeZone.getDefault();
		//int offset = z.getRawOffset(); // The offset not includes daylight savings time
		int offset = z.getOffset(pv_localDateTime); // The offset includes daylight savings time
		lv_UTCTime = pv_localDateTime - offset;
		return lv_UTCTime;
	}

	// UTC/GMT Time -> Local Time
	public static long convertUTCToLocalTime(long pv_UTCDateTime){
		long lv_localDateTime = pv_UTCDateTime;

		TimeZone z = TimeZone.getDefault();
		//int offset = z.getRawOffset(); // The offset not includes daylight savings time
		int offset = z.getOffset(pv_UTCDateTime); // The offset includes daylight savings time

		lv_localDateTime = pv_UTCDateTime + offset;

		return lv_localDateTime;
	}


}
