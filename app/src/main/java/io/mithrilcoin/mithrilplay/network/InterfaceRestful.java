package io.mithrilcoin.mithrilplay.network;

import java.util.List;

import io.mithrilcoin.mithrilplay.common.ServerConstant;
import io.mithrilcoin.mithrilplay.network.vo.AppBody;
import io.mithrilcoin.mithrilplay.network.vo.AppGameBody;
import io.mithrilcoin.mithrilplay.network.vo.AppGameListResponse;
import io.mithrilcoin.mithrilplay.network.vo.AppGamePackageListResponse;
import io.mithrilcoin.mithrilplay.network.vo.AppGamedataRewardResponse;
import io.mithrilcoin.mithrilplay.network.vo.AppRequest;
import io.mithrilcoin.mithrilplay.network.vo.GameRewardTotalListResponse;
import io.mithrilcoin.mithrilplay.network.vo.LoginRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberJoinRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberResponse;
import io.mithrilcoin.mithrilplay.network.vo.MemberUpdateRequest;
import io.mithrilcoin.mithrilplay.network.vo.MemberUpdateResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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

    // 로그인 사용자 정보 조회
    @Headers("Content-Type:application/json")
    @GET(ServerConstant.APP_USER_INFO)
    Call<MemberResponse> getUserInfo(@Path("id") String id);

    // 사용자 추가 정보 업데이트
    @Headers("Content-Type:application/json")
    @POST(ServerConstant.APP_MEMBER_UPDATE)
    Call<MemberUpdateResponse> setMenberDetailUpdate(@Path("id") String id, @Body MemberUpdateRequest memberUpdateRequest);

    // 게임데이터 전송(서버에 설치앱리스트를 보내고 게임정보만 내려받음)
    @Headers("Content-Type:application/json")
    @POST(ServerConstant.APP_GAMEDATA_INSERT)
    Call<AppGameListResponse> getGameApp(@Path("id") String id, @Body List<AppRequest> appRequests);

    // APP 필터링 (게임 APP) _ not use
    @Headers("Content-Type:application/json")
    @POST(ServerConstant.APP_GAMEDATA_PACKAGE)
    Call<AppGamePackageListResponse> getGameAppPackage(@Path("id") String id, @Body List<AppBody> appRequests);

    // 데이터 리워드 요청
    @Headers("Content-Type:application/json")
    @POST(ServerConstant.APP_GAMEDATA_REWARD_ORDER)
    Call<AppGamedataRewardResponse> getGamedataReward(@Path("id") String id, @Body AppGameBody appGameBody);

    // 전체 게임 데이터 조회
    @Headers("Content-Type:application/json")
    @GET(ServerConstant.APP_GAME_REWARD_ALL_LIST_GET)
    Call<GameRewardTotalListResponse> getGameRewardAllListGet(@Path("id") String id);

}
