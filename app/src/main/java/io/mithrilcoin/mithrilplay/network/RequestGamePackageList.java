package io.mithrilcoin.mithrilplay.network;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.network.vo.AppBody;
import io.mithrilcoin.mithrilplay.network.vo.AppGamePackageListResponse;
import io.mithrilcoin.mithrilplay.view.ActivityBase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *  APP filtering (game APP)
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

		InterfaceRestful service = retrofit.create(InterfaceRestful.class);
		Call<AppGamePackageListResponse> call = service.getGameAppPackage(mId, appBodies);
		call.enqueue(new Callback<AppGamePackageListResponse>() {

			@Override
			public void onResponse(Call<AppGamePackageListResponse> call, Response<AppGamePackageListResponse> response) {

				if(response.code() == Constant.OTHER_LOGIN){
					ActivityBase.instance.logoutInlogin();
					return;
				}

				if (!response.isSuccessful()) {
					return;
				}
				AppGamePackageListResponse appRequests = response.body();
				listener.onSuccess(appRequests);
			}

			@Override
			public void onFailure(Call<AppGamePackageListResponse> call, Throwable t) {
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
