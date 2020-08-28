package com.freshroot.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderResponse {

    @SerializedName("final_order_pk")
    @Expose
    private String final_order_pk;
    @SerializedName("order_number")
    @Expose
    private String order_number;
    @SerializedName("grand_amount")
    @Expose
    private String grand_amount;
    @SerializedName("billing_address")
    @Expose
    private String billing_address;
    @SerializedName("order_date")
    @Expose
    private String order_date;
    @SerializedName("order_time")
    @Expose
    private String order_time;
    @SerializedName("order_status")
    @Expose
    private String order_status;
    @SerializedName("final_order_product_pk")
    @Expose
    private String final_order_product_pk;
    @SerializedName("product_id_fk")
    @Expose
    private String product_id_fk;
    @SerializedName("product_price")
    @Expose
    private String product_price;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("total_price")
    @Expose
    private String total_price;
    @SerializedName("delivery_boy_id_fk")
    @Expose
    private String delivery_boy_id_fk;
    @SerializedName("final_order_status")
    @Expose
    private String final_order_status;


    public String getFinal_order_pk() {
        return final_order_pk;
    }

    public void setFinal_order_pk(String final_order_pk) {
        this.final_order_pk = final_order_pk;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getGrand_amount() {
        return grand_amount;
    }

    public void setGrand_amount(String grand_amount) {
        this.grand_amount = grand_amount;
    }

    public String getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(String billing_address) {
        this.billing_address = billing_address;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getFinal_order_product_pk() {
        return final_order_product_pk;
    }

    public void setFinal_order_product_pk(String final_order_product_pk) {
        this.final_order_product_pk = final_order_product_pk;
    }

    public String getProduct_id_fk() {
        return product_id_fk;
    }

    public void setProduct_id_fk(String product_id_fk) {
        this.product_id_fk = product_id_fk;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getDelivery_boy_id_fk() {
        return delivery_boy_id_fk;
    }

    public void setDelivery_boy_id_fk(String delivery_boy_id_fk) {
        this.delivery_boy_id_fk = delivery_boy_id_fk;
    }

    public String getFinal_order_status() {
        return final_order_status;
    }

    public void setFinal_order_status(String final_order_status) {
        this.final_order_status = final_order_status;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }
}
