package io.mithrilcoin.mithrilplay.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import io.mithrilcoin.mithrilplay.common.ServerConstant;
import io.mithrilcoin.mithrilplay.network.vo.AppBody;
import io.mithrilcoin.mithrilplay.network.vo.AppGamePackageListResponse;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 *  APP 필터링 (게임APP만)
 */
public class RequestGamePackageList extends RequestCommon {

	public Context context;
	private String mId;
	private List<AppBody> appBodies;

	public RequestGamePackageList(Context context, String id, List<AppBody> appList){
		this.context = context;
		this.mId = id;
		this.appBodies = appList;
	}

	public void post(final ApiGamePackageListListener listener){

		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(ServerConstant.getHostUrl())
				.addConverterFactory(GsonConverterFactory.create())
				.client(createOkHttpClient())
				.build();

		InterfaceRestful service = retrofit.create(InterfaceRestful.class);
		Call<AppGamePackageListResponse> call = service.getGameAppPackage(mId, appBodies);
		call.enqueue(new Callback<AppGamePackageListResponse>() {

			@Override
			public void onResponse(Call<AppGamePackageListResponse> call, Response<AppGamePackageListResponse> response) {

				if (!response.isSuccessful()) {
					return;
				}
				AppGamePackageListResponse appRequests = response.body();
				listener.onSuccess(appRequests);
			}

			@Override
			public void onFailure(Call<AppGamePackageListResponse> call, Throwable t) {
				Log.v("mithril", "onFailure");

				Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
				listener.onFail();
			}
		});
	}

	public interface ApiGamePackageListListener {
		void onSuccess(AppGamePackageListResponse item);
		void onFail();
	}


}
