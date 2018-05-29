package com.example.lu.thebarbershop.Entity;

import java.io.Serializable;

/**
 * Created by sweet on 2018/5/29.
 */

public class HairStyleDetail implements Serializable{
    private int hairstyle_detail_id;
    private String hairstyle_detail_picture;

    public int getHairstyle_detail_id() {
        return hairstyle_detail_id;
    }

    public void setHairstyle_detail_id(int hairstyle_detail_id) {
        this.hairstyle_detail_id = hairstyle_detail_id;
    }

    public String getHairstyle_detail_picture() {
        return hairstyle_detail_picture;
    }

    public void setHairstyle_detail_picture(String hairstyle_detail_picture) {
        this.hairstyle_detail_picture = hairstyle_detail_picture;
    }
}
