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

    // Sign Up
    @Headers("Content-Type:application/json")
    @POST(ServerConstant.APP_MEMBER_JOIN)
    Call<MemberResponse> setMemberJoin(@Body MemberJoinRequest memberJoinRequest);

    // Login
    @Headers("Content-Type:application/json")
    @POST(ServerConstant.APP_LOGIN)
    Call<MemberResponse> setLogin(@Body LoginRequest loginRequest);

    // Logout
    @Headers("Content-Type:application/json")
    @POST(ServerConstant.APP_LOGOUT)
    Call<MemberResponse> setLogout(@Path("id") String id);

    // Member authentication e-mail request
    @Headers("Content-Type:application/json")
    @POST(ServerConstant.APP_SEND_EMAIL_ORDER)
    Call<MemberResponse> setEmailAuth(@Path("id") String id);

    // View login user information
    @Headers("Content-Type:application/json")
    @GET(ServerConstant.APP_USER_INFO)
    Call<MemberResponse> getUserInfo(@Path("id") String id);

    // Update additional user information
    @Headers("Content-Type:application/json")
    @POST(ServerConstant.APP_MEMBER_UPDATE)
    Call<MemberUpdateResponse> setMenberDetailUpdate(@Path("id") String id, @Body MemberUpdateRequest memberUpdateRequest);

    // Transfer game data (send installed app list to server and download game information only)
    @Headers("Content-Type:application/json")
    @POST(ServerConstant.APP_GAMEDATA_INSERT)
    Call<AppGameListResponse> getGameApp(@Path("id") String id, @Body List<AppRequest> appRequests);

    // APP filtering (game APP) _ test
    @Headers("Content-Type:application/json")
    @POST(ServerConstant.APP_GAMEDATA_PACKAGE)
    Call<AppGamePackageListResponse> getGameAppPackage(@Path("id") String id, @Body List<AppBody> appRequests);

    // Game Reward Request
    @Headers("Content-Type:application/json")
    @POST(ServerConstant.APP_GAMEDATA_REWARD_ORDER)
    Call<AppGamedataRewardResponse> getGamedataReward(@Path("id") String id, @Body AppGameBody appGameBody);

    // View full list received by Reward
    @Headers("Content-Type:application/json")
    @GET(ServerConstant.APP_GAME_REWARD_ALL_LIST_GET)
    Call<GameRewardTotalListResponse> getGameRewardAllListGet(@Path("id") String id);

}
