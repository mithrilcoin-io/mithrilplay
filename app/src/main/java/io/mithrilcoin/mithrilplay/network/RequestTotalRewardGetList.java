package io.mithrilcoin.mithrilplay.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.ServerConstant;
import io.mithrilcoin.mithrilplay.network.vo.GameRewardTotalListResponse;
import io.mithrilcoin.mithrilplay.view.ActivityBase;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 *	전체 게임 데이터 조회(no paging)
 */
public class RequestTotalRewardGetList extends RequestCommon {

	public Context context;
	public String mId;

	public RequestTotalRewardGetList(Context context, String id){
		this.context = context;
		this.mId = id;
	}

	public void post(final ApiGameRewardTotalListener listener){

		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(ServerConstant.getHostUrl())
				.addConverterFactory(GsonConverterFactory.create())
				.client(createOkHttpClient())
				.build();

		InterfaceRestful service = retrofit.create(InterfaceRestful.class);
		Call<GameRewardTotalListResponse> call = service.getGameRewardAllListGet(mId);
		call.enqueue(new Callback<GameRewardTotalListResponse>() {

			@Override
			public void onResponse(Call<GameRewardTotalListResponse> call, Response<GameRewardTotalListResponse> response) {

				Log.d("mithril", "response.code() =" +  response.code() );
				if(response.code() == Constant.OTHER_LOGIN){
					ActivityBase.instance.logoutInlogin();
					return;
				}

				if (!response.isSuccessful()) {
					return;
				}
				GameRewardTotalListResponse gameRewardTotalListResponse = response.body();
				listener.onSuccess(gameRewardTotalListResponse);
			}

			@Override
			public void onFailure(Call<GameRewardTotalListResponse> call, Throwable t) {
				Log.v("mithril", "onFailure");

				Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
				listener.onFail();
			}
		});
	}


	public interface ApiGameRewardTotalListener {
		void onSuccess(GameRewardTotalListResponse item);
		void onFail();
	}


}
