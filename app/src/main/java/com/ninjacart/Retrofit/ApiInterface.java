package com.ninjacart.Retrofit;

import com.ninjacart.Model.AllList;
import com.ninjacart.Model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("/androidApp/Login.php")
    Call<LoginResponse> Login(@Field("mobile") String mobile);


    @GET("/androidApp/productList.php")
    Call<AllList> getProductList(@Query("userId") String userId);


    @FormUrlEncoded
    @POST("/androidApp/addToCart.php")
    Call<LoginResponse> addToCart(@Field("productId") String productId,
                                  @Field("userId") String userId,
                                  @Field("quantity") String quantity);


    @FormUrlEncoded
    @POST("/androidApp/deleteCart.php")
    Call<LoginResponse> deleteCart(@Field("productId") String productId,
                                   @Field("userId") String userId);


    @GET("/androidApp/viewCart.php")
    Call<AllList> getCartList(@Query("userId") String userId);


    @FormUrlEncoded
    @POST("/androidApp/FinalOrder.php")
    Call<LoginResponse> placeOrder(@Field("userId") String userId,
                                   @Field("subAmount") String subAmount);


    @GET("/androidApp/getOrderList.php")
    Call<AllList> getOrderList(@Query("userId") String userId);

    @FormUrlEncoded
    @POST("/androidApp/updateProfile.php")
    Call<LoginResponse> updateProfile(@Field("userId") String userId,
                                      @Field("customerName") String customerName,
                                      @Field("customerMobile") String customerMobile,
                                      @Field("customerAddress") String customerAddress);


}
