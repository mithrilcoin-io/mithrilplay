package io.mithrilcoin.mithrilplay.network;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.network.vo.AppGameListResponse;
import io.mithrilcoin.mithrilplay.network.vo.AppRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberResponse;
import io.mithrilcoin.mithrilplay.network.vo.QueryDataRequest;
import io.mithrilcoin.mithrilplay.view.ActivityBase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *  Database Query Data Send
 */
public class RequestSendQueryData extends RequestCommon {

	public Context context;
	private String mId;
	private QueryDataRequest appRequests;

	public RequestSendQueryData(Context context, String id, QueryDataRequest appList){
		this.context = context;
		this.mId = id;
		this.appRequests = appList;
	}

	public void post(final ApiQueryDataListener listener){

		InterfaceRestful service = retrofit.create(InterfaceRestful.class);
		Call<MemberResponse> call = service.sendQueryData(mId, appRequests);
		call.enqueue(new Callback<MemberResponse>() {

			@Override
			public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {

				if(response.code() == Constant.OTHER_LOGIN){
					ActivityBase.instance.logoutInlogin();
					return;
				}

				if (!response.isSuccessful()) {
					return;
				}
				MemberResponse queryDataResponse = response.body();
				listener.onSuccess(queryDataResponse);
			}

			@Override
			public void onFailure(Call<MemberResponse> call, Throwable t) {
				Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
				listener.onFail();
			}
		});
	}

	public interface ApiQueryDataListener {
		void onSuccess(MemberResponse item);
		void onFail();
	}


}
