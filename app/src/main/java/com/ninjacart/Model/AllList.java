package com.ninjacart.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllList {

    @SerializedName("")
    List<ProductResponse> productResponseList;



    public List<ProductResponse> getProductResponseList() {
        return productResponseList;
    }

    public void setProductResponseList(List<ProductResponse> productResponseList) {
        this.productResponseList = productResponseList;
    }
}
