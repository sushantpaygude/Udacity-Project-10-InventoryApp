package com.example.sushant.inventoryapp.Cards;

import android.net.Uri;

/**
 * Created by sushant on 10/11/16.
 */
public class InventoryInfo {
    public String productName;
    public String productPrice;
    public int productQuantity;
    public String productImage;
    public String productSupplier;
    public String productSupplierContact;

    public InventoryInfo(String productName, String productPrice, int productQuantity, String productImage, String productSupplier, String productSupplierContact) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productImage = productImage;
        this.productSupplier = productSupplier;
        this.productSupplierContact=productSupplierContact;
    }

    public InventoryInfo() {

    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductSupplier() {
        return productSupplier;
    }

    public void setProductSupplier(String productSupplier) {
        this.productSupplier = productSupplier;
    }

    public String getProductSupplierContact() {
        return productSupplierContact;
    }

    public void setProductSupplierContact(String productSupplierContact) {
        this.productSupplierContact = productSupplierContact;
    }
}
