package io.mithrilcoin.mithrilplay.network;

import android.content.Context;
import android.widget.Toast;

import io.mithrilcoin.mithrilplay.common.ServerConstant;
import io.mithrilcoin.mithrilplay.network.vo.MemberJoinRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberResponse;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Sign Up
 */
public class RequestMemberJoin extends RequestCommon {

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
		Call<MemberResponse> call = service.setMemberJoin(mMemberJoinRequest);
		call.enqueue(new Callback<MemberResponse>() {

			@Override
			public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {

				if (!response.isSuccessful()) {
					return;
				}
				MemberResponse memberJoinResponse = response.body();
				listener.onSuccess(memberJoinResponse);
			}

			@Override
			public void onFailure(Call<MemberResponse> call, Throwable t) {
				Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
				listener.onFail();
			}
		});
	}

	public interface ApiMemberJoinResultListener {
		void onSuccess(MemberResponse item);
		void onFail();
	}


}
