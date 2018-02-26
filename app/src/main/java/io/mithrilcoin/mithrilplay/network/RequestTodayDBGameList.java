package io.mithrilcoin.mithrilplay.network;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.network.vo.AppGameListResponse;
import io.mithrilcoin.mithrilplay.network.vo.AppRequest;
import io.mithrilcoin.mithrilplay.network.vo.GamePlayDataRequest;
import io.mithrilcoin.mithrilplay.view.ActivityBase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *  Transfer game data (send installed app list to server and download game information only)
 */
public class RequestTodayDBGameList extends RequestCommon {

	public Context context;
	private String mId;
	private List<GamePlayDataRequest> appRequests;

	public RequestTodayDBGameList(Context context, String id, List<GamePlayDataRequest> appList){
		this.context = context;
		this.mId = id;
		this.appRequests = appList;
	}

	public void post(final ApiTodayGameListListener listener){

		InterfaceRestful service = retrofit.create(InterfaceRestful.class);
		Call<AppGameListResponse> call = service.getDBGameApp(mId, appRequests);
		call.enqueue(new Callback<AppGameListResponse>() {

			@Override
			public void onResponse(Call<AppGameListResponse> call, Response<AppGameListResponse> response) {

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
