package com.freshroot.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllList {

    @SerializedName("product")
    List<ProductResponse> productResponseList;

    @SerializedName("cartResponse")
    List<CartResponse> cartResponseList;

    @SerializedName("orderResponse")
    List<OrderResponse> orderResponseList;

    @SerializedName("profileResponse")
    List<ProfileResponse> profileResponseList;

    @SerializedName("orderDetailsResponse")
    List<OrderDetailsResponse> orderDetailsResponseList;




    public List<ProductResponse> getProductResponseList() {
        return productResponseList;
    }

    public void setProductResponseList(List<ProductResponse> productResponseList) {
        this.productResponseList = productResponseList;
    }

    public List<CartResponse> getCartResponseList() {
        return cartResponseList;
    }

    public void setCartResponseList(List<CartResponse> cartResponseList) {
        this.cartResponseList = cartResponseList;
    }

    public List<OrderResponse> getOrderResponseList() {
        return orderResponseList;
    }

    public void setOrderResponseList(List<OrderResponse> orderResponseList) {
        this.orderResponseList = orderResponseList;
    }

    public List<ProfileResponse> getProfileResponseList() {
        return profileResponseList;
    }

    public void setProfileResponseList(List<ProfileResponse> profileResponseList) {
        this.profileResponseList = profileResponseList;
    }

    public List<OrderDetailsResponse> getOrderDetailsResponseList() {
        return orderDetailsResponseList;
    }

    public void setOrderDetailsResponseList(List<OrderDetailsResponse> orderDetailsResponseList) {
        this.orderDetailsResponseList = orderDetailsResponseList;
    }
}
