package com.example.lu.thebarbershop.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by sweet on 2018/5/15.
 */

public class UserShopDetail implements Serializable {
    private int shopId;
    private String shopPicture;
    private String shopName;
    private String shopAddress;
    private String shopPhone;
    private String shopIntroduce;//店铺简介
    private Set<ShopPicture> ShopPictureSet = new HashSet<>();
    private List<HairStyleDetail> hairStyleDetails = new ArrayList<HairStyleDetail>();
    /*private List<Baber> baberSet = new ArrayList<Baber>();*/

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopPicture() {
        return shopPicture;
    }

    public void setShopPicture(String shopPicture) {
        this.shopPicture = shopPicture;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public String getShopIntroduce() {
        return shopIntroduce;
    }

    public void setShopIntroduce(String shopIntroduce) {
        this.shopIntroduce = shopIntroduce;
    }

    public Set<ShopPicture> getShopPictureSet() {
        return ShopPictureSet;
    }

    public void setShopPictureSet(Set<ShopPicture> shopPictureSet) {
        ShopPictureSet = shopPictureSet;
    }

    public List<HairStyleDetail> getHairStyleDetails() {
        return hairStyleDetails;
    }

    public void setHairStyleDetails(List<HairStyleDetail> hairStyleDetails) {
        this.hairStyleDetails = hairStyleDetails;
    }


}
