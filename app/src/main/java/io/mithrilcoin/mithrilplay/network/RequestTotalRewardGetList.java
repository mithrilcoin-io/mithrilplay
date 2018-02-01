package io.mithrilcoin.mithrilplay.network;

import android.content.Context;
import android.widget.Toast;

import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.network.vo.GameRewardTotalListResponse;
import io.mithrilcoin.mithrilplay.view.ActivityBase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *	View full list received by Reward
 */
public class RequestTotalRewardGetList extends RequestCommon {

	public Context context;
	public String mId;

	public RequestTotalRewardGetList(Context context, String id){
		this.context = context;
		this.mId = id;
	}

	public void post(final ApiGameRewardTotalListener listener){

		InterfaceRestful service = retrofit.create(InterfaceRestful.class);
		Call<GameRewardTotalListResponse> call = service.getGameRewardAllListGet(mId);
		call.enqueue(new Callback<GameRewardTotalListResponse>() {

			@Override
			public void onResponse(Call<GameRewardTotalListResponse> call, Response<GameRewardTotalListResponse> response) {

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
