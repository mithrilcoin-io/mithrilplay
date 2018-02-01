package io.mithrilcoin.mithrilplay.network;

import io.mithrilcoin.mithrilplay.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestCommon {

	Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(BuildConfig.HOST_SERVER)
			.addConverterFactory(GsonConverterFactory.create())
			.client(createOkHttpClient())
			.build();

	// Check network log
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
