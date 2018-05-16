package com.example.lu.thebarbershop.MyTools;

import com.example.lu.thebarbershop.Entity.UserShopDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sweet on 2018/5/15.
 */

public class PrepareIndexShopDetail {
    public List<UserShopDetail> prepareIndexShopDetail(){
        List<UserShopDetail> list = new ArrayList<UserShopDetail>();
        UserShopDetail userShopDetail1 = new UserShopDetail();
        userShopDetail1.setShopId(1);
        userShopDetail1.setShopPicture("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526384959497&di=0ba01c4ae22ea48da90dce1cd243d6be&imgtype=0&src=http%3A%2F%2Fpic1.xtuan.com%2Fupload%2Fxiaoguotu%2F20130513%2F10253745280.jpg");
        userShopDetail1.setShopAddress("石家庄建设南大街与石家庄中国南大街交叉口南行120米路东");
        userShopDetail1.setShopName("石家庄梅聂剪发");
        userShopDetail1.setShopPhone("1577896541");
        userShopDetail1.setShopIntroduce("我们用双手成就你的梦想哈哈哈快来快活吧");
        list.add(userShopDetail1);
        UserShopDetail userShopDetail2 = new UserShopDetail();
        userShopDetail2.setShopId(2);
        userShopDetail2.setShopPicture("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526384959496&di=5b5ef1abd138c0ff0087b13cb80dc356&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F016ca059b8e5e9a8012075346b2bea.jpg%401280w_1l_2o_100sh.png");
        userShopDetail2.setShopAddress("石家庄建设南大街与石家庄中国南大街交叉口南行120米路东");
        userShopDetail2.setShopName("石家庄MIX沙龙");
        userShopDetail2.setShopPhone("1577896541");
        userShopDetail2.setShopIntroduce("我们用双手成就你的梦想哈哈哈快来快活吧");
        list.add(userShopDetail2);
        UserShopDetail userShopDetail3 = new UserShopDetail();
        userShopDetail3.setShopId(1);
        userShopDetail3.setShopPicture("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526384959496&di=90b394a6394f97aa7e4282e19f33f925&imgtype=0&src=http%3A%2F%2Fstatic.qizuang.com%2Fupload%2Feditor%2Fimage%2F20150803%2F20150803131518_22451.png");
        userShopDetail3.setShopAddress("石家庄建设南大街与石家庄中国南大街交叉口南行120米路东");
        userShopDetail3.setShopName("石家庄王大锤沙龙");
        userShopDetail3.setShopPhone("1577896541");
        userShopDetail3.setShopIntroduce("我们用双手成就你的梦想哈哈哈快来快活吧");
        list.add(userShopDetail3);
        return list;
    }
}
