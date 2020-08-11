package com.ninjacart.Retrofit;

import com.ninjacart.Model.AllList;
import com.ninjacart.Model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("/androidApp/Transporter/Login.php")
    Call<LoginResponse> Login(@Field("mobile") String mobile);


    @GET("")
    Call<AllList> getProductList(String userId);

}
