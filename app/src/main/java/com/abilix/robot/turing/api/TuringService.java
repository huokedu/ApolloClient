package com.abilix.robot.turing.api;

import com.abilix.robot.turing.entity.TuringResultInfo;
import com.abilix.robot.turing.entity.VoiceResultInfo;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Tony 2016-08-10
 */

public interface TuringService {

    @Headers({"url_name:turing"})
    @POST("openapi/api")
    Call<TuringResultInfo> search(@Query("key") String type, @Query("info") String info, @Query("userid") String userid);

    @Headers({"url_name:server"})
    @POST("ApolloServer")
    Call<VoiceResultInfo> send(@Query("info") String info, @Query("userid") String userid);

    @Headers({"url_name:server"})
    @POST("KnowledgeServer")
    Call<VoiceResultInfo> sendServer(@Query("info") String info, @Query("userid") String userid);


}
