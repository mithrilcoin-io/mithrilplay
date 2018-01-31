package io.mithrilcoin.mithrilplay.network;

import android.content.Context;
import android.widget.Toast;

import io.mithrilcoin.mithrilplay.common.ServerConstant;
import io.mithrilcoin.mithrilplay.network.vo.LoginRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberResponse;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Login
 */
public class RequestLogin extends RequestCommon {

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
		Call<MemberResponse> call = service.setLogin(mLoginRequest);
		call.enqueue(new Callback<MemberResponse>() {

			@Override
			public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {

				if (!response.isSuccessful()) {
					return;
				}
				MemberResponse loginResponse = response.body();
				listener.onSuccess(loginResponse);
			}

			@Override
			public void onFailure(Call<MemberResponse> call, Throwable t) {
				Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
				listener.onFail();
			}
		});
	}

	public interface ApiLoginResultListener {
		void onSuccess(MemberResponse item);
		void onFail();
	}


}
