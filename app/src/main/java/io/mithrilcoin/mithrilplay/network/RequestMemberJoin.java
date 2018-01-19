package io.mithrilcoin.mithrilplay.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import io.mithrilcoin.mithrilplay.common.ServerConstant;
import io.mithrilcoin.mithrilplay.network.vo.MemberJoinRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberJoinResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


// 회원가입
public class RequestMemberJoin {

	public Context context;
	private MemberJoinRequest mMemberJoinRequest;

	public RequestMemberJoin(Context context, MemberJoinRequest memberJoinRequest){
		this.context = context;
		this.mMemberJoinRequest = memberJoinRequest;
	}

	public void post(final ApiMemberJoinResultListener listener){

		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(ServerConstant.getHostUrl())
				.addConverterFactory(GsonConverterFactory.create())
				.client(createOkHttpClient())
				.build();

		InterfaceRestful service = retrofit.create(InterfaceRestful.class);
		Call<MemberJoinResponse> call = service.setMemberJoin(mMemberJoinRequest);
		call.enqueue(new Callback<MemberJoinResponse>() {

			@Override
			public void onResponse(Call<MemberJoinResponse> call, Response<MemberJoinResponse> response) {

				if (!response.isSuccessful()) {
					return;
				}
				MemberJoinResponse resultChargeCall = response.body();
				listener.onSuccess(resultChargeCall);
			}

			@Override
			public void onFailure(Call<MemberJoinResponse> call, Throwable t) {
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

	public interface ApiMemberJoinResultListener {
		void onSuccess(MemberJoinResponse item);
		void onFail();
	}


}
