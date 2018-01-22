package io.mithrilcoin.mithrilplay.network;

import io.mithrilcoin.mithrilplay.common.ServerConstant;
import io.mithrilcoin.mithrilplay.network.vo.LoginRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberResponse;
import io.mithrilcoin.mithrilplay.network.vo.MemberJoinRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InterfaceRestful {

    // 회원가입
    @Headers("Content-Type:application/json")
    @POST(ServerConstant.APP_MEMBER_JOIN)
    Call<MemberResponse> setMemberJoin(@Body MemberJoinRequest memberJoinRequest);

    // 로그인
    @Headers("Content-Type:application/json")
    @POST(ServerConstant.APP_LOGIN)
    Call<MemberResponse> setLogin(@Body LoginRequest loginRequest);

    // 로그아웃
    @Headers("Content-Type:application/json")
    @POST(ServerConstant.APP_LOGOUT)
    Call<MemberResponse> setLogout(@Path("id") String id);

    // 회원인증 이메일 발송요청
    @Headers("Content-Type:application/json")
    @POST(ServerConstant.APP_SEND_EMAIL_ORDER)
    Call<MemberResponse> setEmailAuth(@Path("id") String id);

}
