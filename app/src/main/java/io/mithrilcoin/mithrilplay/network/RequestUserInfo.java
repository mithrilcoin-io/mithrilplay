package io.mithrilcoin.mithrilplay.network;

import android.content.Context;
import android.widget.Toast;

import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.network.vo.MemberResponse;
import io.mithrilcoin.mithrilplay.view.ActivityBase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *	View login user information
 */
public class RequestUserInfo extends RequestCommon {

	public Context context;
	public String mId;

	public RequestUserInfo(Context context, String id){
		this.context = context;
		this.mId = id;
	}

	public void post(final ApiGetUserinfoListener listener){

		InterfaceRestful service = retrofit.create(InterfaceRestful.class);
		Call<MemberResponse> call = service.getUserInfo(mId);
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


	public interface ApiGetUserinfoListener {
		void onSuccess(MemberResponse item);
		void onFail();
	}


}
