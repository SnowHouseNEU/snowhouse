package com.neu.snowhouse.api;

import com.neu.snowhouse.model.PostResponseModel;
import com.neu.snowhouse.model.UserLoginRequestModel;
import com.neu.snowhouse.model.UserRegisterRequestModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface API {
    @POST("user/login")
    Call<ResponseBody> loginUser(@Body UserLoginRequestModel user);

    @POST("user/register")
    Call<ResponseBody> registerUser(@Body UserRegisterRequestModel user);

    // POST request to upload a post without image to the backend
    @Multipart
    @POST("post")
    Call<ResponseBody> uploadRawPost(@Part("post") RequestBody jsonPost);


    // POST request to upload a post with image to the backend
    @Multipart
    @POST("post/withImage")
    Call<ResponseBody> uploadPostWithImage(@Part MultipartBody.Part image, @Part("post") RequestBody jsonPost);

    // GET request to get a post by its ID
    @GET("post/{postId}")
    Call<PostResponseModel> getPostById(@Path("postId") int postId);
}
