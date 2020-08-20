package com.ninjacart.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllList {

    @SerializedName("product")
    List<ProductResponse> productResponseList;

    @SerializedName("cartResponse")
    List<CartResponse> cartResponseList;

    @SerializedName("orderResponse")
    List<OrderResponse> orderResponseList;


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
}
