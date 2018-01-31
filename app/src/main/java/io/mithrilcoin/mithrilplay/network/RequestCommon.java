package io.mithrilcoin.mithrilplay.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class RequestCommon {

	// 통신과정 로그확인
	protected OkHttpClient createOkHttpClient() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		builder.addInterceptor(interceptor);
//		builder.connectTimeout(30, TimeUnit.SECONDS);
//		builder.readTimeout(30, TimeUnit.SECONDS);
		return builder.build();
	}

}
