package com.example.myinvoice;

import android.graphics.Bitmap;

public class Invoice {
    private String title,date,invoiceType,shopName,location,comment,invoiceID;
    private Bitmap image;

    public Invoice(String title, String date, String invoiceType, String shopName, String location, String comment, Bitmap image) {
        this.title = title;
        this.date = date;
        this.invoiceType = invoiceType;
        this.shopName = shopName;
        this.location = location;
        this.comment = comment;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    @Override
    public String toString(){
        return title+"\n"+date+"\n"+shopName;
    }
}
