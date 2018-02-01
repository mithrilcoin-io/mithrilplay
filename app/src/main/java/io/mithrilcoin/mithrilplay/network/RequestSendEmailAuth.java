package io.mithrilcoin.mithrilplay.network;

import android.content.Context;
import android.widget.Toast;

import io.mithrilcoin.mithrilplay.network.vo.MemberResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *	Member authentication e-mail request
 */
public class RequestSendEmailAuth extends RequestCommon {

	public Context context;
	public String mId;

	public RequestSendEmailAuth(Context context, String id){
		this.context = context;
		this.mId = id;
	}

	public void post(final ApiSendEmailAuthtListener listener){

		InterfaceRestful service = retrofit.create(InterfaceRestful.class);
		Call<MemberResponse> call = service.setEmailAuth(mId);
		call.enqueue(new Callback<MemberResponse>() {

			@Override
			public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {

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


	public interface ApiSendEmailAuthtListener {
		void onSuccess(MemberResponse item);
		void onFail();
	}


}
