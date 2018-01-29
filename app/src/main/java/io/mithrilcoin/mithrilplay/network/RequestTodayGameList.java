package io.mithrilcoin.mithrilplay.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.common.ServerConstant;
import io.mithrilcoin.mithrilplay.network.vo.AppGameListResponse;
import io.mithrilcoin.mithrilplay.network.vo.AppRequest;
import io.mithrilcoin.mithrilplay.view.ActivityBase;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 *  게임리스트 가져오기
 */
public class RequestTodayGameList extends RequestCommon {

	public Context context;
	private String mId;
	private List<AppRequest> appRequests;

	public RequestTodayGameList(Context context, String id, List<AppRequest> appList){
		this.context = context;
		this.mId = id;
		this.appRequests = appList;
	}

	public void post(final ApiTodayGameListListener listener){

		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(ServerConstant.getHostUrl())
				.addConverterFactory(GsonConverterFactory.create())
				.client(createOkHttpClient())
				.build();

		InterfaceRestful service = retrofit.create(InterfaceRestful.class);
		Call<AppGameListResponse> call = service.getGameApp(mId, appRequests);
		call.enqueue(new Callback<AppGameListResponse>() {

			@Override
			public void onResponse(Call<AppGameListResponse> call, Response<AppGameListResponse> response) {

				Log.d("mithril", "response.code() =" +  response.code() );
				if(response.code() == Constant.OTHER_LOGIN){
					ActivityBase.instance.logoutInlogin();
					return;
				}

				if (!response.isSuccessful()) {
					return;
				}
				AppGameListResponse appGameListResponse = response.body();
				listener.onSuccess(appGameListResponse);
			}

			@Override
			public void onFailure(Call<AppGameListResponse> call, Throwable t) {
				Log.v("mithril", "onFailure");

				Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
				listener.onFail();
			}
		});
	}

	public interface ApiTodayGameListListener {
		void onSuccess(AppGameListResponse item);
		void onFail();
	}


}
