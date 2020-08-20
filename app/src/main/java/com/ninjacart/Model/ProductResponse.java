package com.ninjacart.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductResponse {

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productStock")
    @Expose
    private String productStock;
    @SerializedName("productSellPrice")
    @Expose
    private String productSellPrice;
    @SerializedName("productMrp")
    @Expose
    private String productMrp;
    @SerializedName("productDesc")
    @Expose
    private String productDesc;
    @SerializedName("productUnit")
    @Expose
    private String productUnit;
    @SerializedName("minPurchaseQty")
    @Expose
    private String minPurchaseQty;
    @SerializedName("productPhoto")
    @Expose
    private String productPhoto;
    @SerializedName("cartId")
    @Expose
    private String cartId;
    @SerializedName("qty")
    @Expose
    private String qty;


    //All getter and Setter...

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductStock() {
        return productStock;
    }

    public void setProductStock(String productStock) {
        this.productStock = productStock;
    }

    public String getProductSellPrice() {
        return productSellPrice;
    }

    public void setProductSellPrice(String productSellPrice) {
        this.productSellPrice = productSellPrice;
    }

    public String getProductMrp() {
        return productMrp;
    }

    public void setProductMrp(String productMrp) {
        this.productMrp = productMrp;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
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

    public String getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
