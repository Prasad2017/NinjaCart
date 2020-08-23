package com.ninjacart.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailsResponse {

    @SerializedName("final_order_product_pk")
    @Expose
    private String finalOrderProductPk;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("final_order_status")
    @Expose
    private String finalOrderStatus;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productPhoto")
    @Expose
    private String productPhoto;
    @SerializedName("order_number")
    @Expose
    private String orderNumber;
    @SerializedName("grand_amount")
    @Expose
    private String grandAmount;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;


    public String getFinalOrderProductPk() {
        return finalOrderProductPk;
    }

    public void setFinalOrderProductPk(String finalOrderProductPk) {
        this.finalOrderProductPk = finalOrderProductPk;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getFinalOrderStatus() {
        return finalOrderStatus;
    }

    public void setFinalOrderStatus(String finalOrderStatus) {
        this.finalOrderStatus = finalOrderStatus;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getGrandAmount() {
        return grandAmount;
    }

    public void setGrandAmount(String grandAmount) {
        this.grandAmount = grandAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
