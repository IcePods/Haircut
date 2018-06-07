package com.example.lu.thebarbershop.Entity;

/**
 * Created by sweet on 2018/6/7.
 */

public class UserCollections {
    private int collection_id;
    private Users user;
    private UserShopDetail userShopDetail;
    private boolean CollectionCondition;

    public int getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(int collection_id) {
        this.collection_id = collection_id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public UserShopDetail getUserShopDetail() {
        return userShopDetail;
    }

    public void setUserShopDetail(UserShopDetail userShopDetail) {
        this.userShopDetail = userShopDetail;
    }

    public boolean isCollectionCondition() {
        return CollectionCondition;
    }

    public void setCollectionCondition(boolean collectionCondition) {
        CollectionCondition = collectionCondition;
    }
}
