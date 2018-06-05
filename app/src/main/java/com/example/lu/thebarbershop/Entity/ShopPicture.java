package com.example.lu.thebarbershop.Entity;

import java.io.Serializable;

/**
 * Created by sweet on 2018/5/29.
 */

public class ShopPicture implements Serializable{
    private int shoppicture_id;
    private String shoppicture_picture;

    public int getShoppicture_id() {
        return shoppicture_id;
    }

    public void setShoppicture_id(int shoppicture_id) {
        this.shoppicture_id = shoppicture_id;
    }

    public String getShoppicture_picture() {
        return shoppicture_picture;
    }

    public void setShoppicture_picture(String shoppicture_picture) {
        this.shoppicture_picture = shoppicture_picture;
    }
}
