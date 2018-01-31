package io.mithrilcoin.mithrilplay.network;

import android.content.Context;
import android.widget.Toast;

import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.ServerConstant;
import io.mithrilcoin.mithrilplay.network.vo.AppGameBody;
import io.mithrilcoin.mithrilplay.network.vo.AppGamedataRewardResponse;
import io.mithrilcoin.mithrilplay.view.ActivityBase;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 *  Game Reward Request
 */
public class RequestGameRewardOrder extends RequestCommon {

	public Context context;
	private String mId;
	private AppGameBody appGameBody;

	public RequestGameRewardOrder(Context context, String id, AppGameBody gameBody){
		this.context = context;
		this.mId = id;
		this.appGameBody = gameBody;
	}

	public void post(final ApiGameRewardOrderListener listener){

		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(ServerConstant.getHostUrl())
				.addConverterFactory(GsonConverterFactory.create())
				.client(createOkHttpClient())
				.build();

		InterfaceRestful service = retrofit.create(InterfaceRestful.class);
		Call<AppGamedataRewardResponse> call = service.getGamedataReward(mId, appGameBody);
		call.enqueue(new Callback<AppGamedataRewardResponse>() {

			@Override
			public void onResponse(Call<AppGamedataRewardResponse> call, Response<AppGamedataRewardResponse> response) {

				if(response.code() == Constant.OTHER_LOGIN){
					ActivityBase.instance.logoutInlogin();
					return;
				}

				if (!response.isSuccessful()) {
					return;
				}
				AppGamedataRewardResponse appGamedataRewardResponse = response.body();
				listener.onSuccess(appGamedataRewardResponse);
			}

			@Override
			public void onFailure(Call<AppGamedataRewardResponse> call, Throwable t) {
				Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
				listener.onFail();
			}
		});
	}

	public interface ApiGameRewardOrderListener {
		void onSuccess(AppGamedataRewardResponse item);
		void onFail();
	}


}
