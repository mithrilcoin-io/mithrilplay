package io.mithrilcoin.mithrilplay.network;

import android.content.Context;
import android.widget.Toast;

import io.mithrilcoin.mithrilplay.common.Constant;
import io.mithrilcoin.mithrilplay.network.vo.MemberUpdateRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberUpdateResponse;
import io.mithrilcoin.mithrilplay.view.ActivityBase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Update additional user information
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

		InterfaceRestful service = retrofit.create(InterfaceRestful.class);
		Call<MemberUpdateResponse> call = service.setMenberDetailUpdate(mId, memberUpdateRequest);
		call.enqueue(new Callback<MemberUpdateResponse>() {

			@Override
			public void onResponse(Call<MemberUpdateResponse> call, Response<MemberUpdateResponse> response) {

				if(response.code() == Constant.OTHER_LOGIN){
					ActivityBase.instance.logoutInlogin();
					return;
				}

				if (!response.isSuccessful()) {
					return;
				}
				MemberUpdateResponse memberUpdateResponse = response.body();
				listener.onSuccess(memberUpdateResponse);
			}

			@Override
			public void onFailure(Call<MemberUpdateResponse> call, Throwable t) {
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
