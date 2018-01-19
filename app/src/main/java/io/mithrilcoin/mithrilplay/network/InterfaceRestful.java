package io.mithrilcoin.mithrilplay.network;

import io.mithrilcoin.mithrilplay.common.ServerConstant;
import io.mithrilcoin.mithrilplay.network.vo.LoginRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberJoinRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberJoinResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface InterfaceRestful {

    // 회원가입
    @Headers("Content-Type:application/json")
    @POST(ServerConstant.APP_MEMBER_JOIN)
    Call<MemberJoinResponse> setMemberJoin(@Body MemberJoinRequest memberJoinRequest);

    // 로그인
    @Headers("Content-Type:application/json")
    @POST(ServerConstant.APP_LOGIN)
    Call<MemberJoinResponse> setLogin(@Body LoginRequest loginRequest);

}
