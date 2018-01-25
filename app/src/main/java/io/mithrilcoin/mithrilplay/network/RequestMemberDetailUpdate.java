package io.mithrilcoin.mithrilplay.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import io.mithrilcoin.mithrilplay.common.ServerConstant;
import io.mithrilcoin.mithrilplay.network.vo.MemberUpdateRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberUpdateResponse;
import io.mithrilcoin.mithrilplay.network.vo.MemberResponse;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 사용자 추가 정보 업데이트
 */
public class RequestMemberDetailUpdate extends RequestCommon {

	public Context context;
	private String mId;
	private MemberUpdateRequest memberUpdateRequest;

	public RequestMemberDetailUpdate(Context context, String id, MemberUpdateRequest memberDetailRequest){
		this.context = context;
		this.mId = id;
		this.memberUpdateRequest = memberDetailRequest;
	}

	public void post(final ApiMemberDetailUpdateListener listener){

		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(ServerConstant.getHostUrl())
				.addConverterFactory(GsonConverterFactory.create())
				.client(createOkHttpClient())
				.build();

		InterfaceRestful service = retrofit.create(InterfaceRestful.class);
		Call<MemberUpdateResponse> call = service.setMenberDetailUpdate(mId, memberUpdateRequest);
		call.enqueue(new Callback<MemberUpdateResponse>() {

			@Override
			public void onResponse(Call<MemberUpdateResponse> call, Response<MemberUpdateResponse> response) {

				if (!response.isSuccessful()) {
					return;
				}
				MemberUpdateResponse memberUpdateResponse = response.body();
				listener.onSuccess(memberUpdateResponse);
			}

			@Override
			public void onFailure(Call<MemberUpdateResponse> call, Throwable t) {
				Log.v("mithril", "onFailure");

				Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
				listener.onFail();
			}
		});
	}

	public interface ApiMemberDetailUpdateListener {
		void onSuccess(MemberUpdateResponse item);
		void onFail();
	}


}