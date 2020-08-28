package com.freshroot.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartResponse {

    @SerializedName("cart_id")
    @Expose
    private String cart_id;

    @SerializedName("productId")
    @Expose
    private String productId;

    @SerializedName("productName")
    @Expose
    private String productName;

    @SerializedName("productQuantity")
    @Expose
    private String productQuantity;

    @SerializedName("product_delivery_charge")
    @Expose
    private String product_delivery_charge;

    @SerializedName("productUnit")
    @Expose
    private String productUnit;

    @SerializedName("minPurchaseQty")
    @Expose
    private String minPurchaseQty;

    @SerializedName("productMrp")
    @Expose
    private String productMrp;

    @SerializedName("productSellPrice")
    @Expose
    private String productSellPrice;


    @SerializedName("productPhoto")
    @Expose
    private String productPhoto;

    @SerializedName("status")
    @Expose
    private String status;


    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getMinPurchaseQty() {
        return minPurchaseQty;
    }

    public void setMinPurchaseQty(String minPurchaseQty) {
        this.minPurchaseQty = minPurchaseQty;
    }

    public String getProductMrp() {
        return productMrp;
    }

    public void setProductMrp(String productMrp) {
        this.productMrp = productMrp;
    }

    public String getProductSellPrice() {
        return productSellPrice;
    }

    public void setProductSellPrice(String productSellPrice) {
        this.productSellPrice = productSellPrice;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProduct_delivery_charge() {
        return product_delivery_charge;
    }

    public void setProduct_delivery_charge(String product_delivery_charge) {
        this.product_delivery_charge = product_delivery_charge;
    }
}
