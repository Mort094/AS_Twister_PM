package com.example.twister_pm;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AnboService {
    @GET("Messages")
    Call<List<Message>> getMessage();

    @GET("Messages/{messageId}/Comments")
    Call<List<Comment>> getComment(@Path("messageId") int messageId);

    @POST("Messages/{messageId}/Comments")
    Call<Comment> saveComment(@Path("messageId") int messageId, @Body Comment comment);
}
