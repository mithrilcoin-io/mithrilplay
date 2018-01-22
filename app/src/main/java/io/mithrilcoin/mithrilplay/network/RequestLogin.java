package io.mithrilcoin.mithrilplay.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import io.mithrilcoin.mithrilplay.common.ServerConstant;
import io.mithrilcoin.mithrilplay.network.vo.LoginRequest;
import io.mithrilcoin.mithrilplay.network.vo.LoginResponse;
import io.mithrilcoin.mithrilplay.network.vo.MemberJoinRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberJoinResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// 로그인
public class RequestLogin {

	public Context context;
	private LoginRequest mLoginRequest;

	public RequestLogin(Context context, LoginRequest loginRequest){
		this.context = context;
		this.mLoginRequest = loginRequest;
	}

	public void post(final ApiLoginResultListener listener){

		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(ServerConstant.getHostUrl())
				.addConverterFactory(GsonConverterFactory.create())
				.client(createOkHttpClient())
				.build();

		InterfaceRestful service = retrofit.create(InterfaceRestful.class);
		Call<LoginResponse> call = service.setLogin(mLoginRequest);
		call.enqueue(new Callback<LoginResponse>() {

			@Override
			public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

				if (!response.isSuccessful()) {
					return;
				}
				LoginResponse loginResponse = response.body();
				listener.onSuccess(loginResponse);
			}

			@Override
			public void onFailure(Call<LoginResponse> call, Throwable t) {
				Log.v("mithril", "onFailure");

				Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
				listener.onFail();
			}
		});
	}

	// 통신과정 로그확인
	private static OkHttpClient createOkHttpClient() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		builder.addInterceptor(interceptor);

		builder.connectTimeout(30, TimeUnit.SECONDS);
		builder.readTimeout(30, TimeUnit.SECONDS);

		return builder.build();
	}

	public interface ApiLoginResultListener {
		void onSuccess(LoginResponse item);
		void onFail();
	}


}
