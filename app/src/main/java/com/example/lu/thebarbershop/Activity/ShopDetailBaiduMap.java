package com.example.lu.thebarbershop.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.lu.thebarbershop.Entity.UserShopDetail;
import com.example.lu.thebarbershop.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ShopDetailBaiduMap extends AppCompatActivity {
    private static final String SHAI = "3D:1E:F5:28:A9:19:EB:C8:8B:03:5B:7D:3E:65:CF:98:99:CD:11:4E";
    private static final String AK = "6kHItlMROUNkqnD2HYeTu8bOqYghG80c";
    private static final String PACKAGE = "com.example.lu.thebarbershop";

    private MapView mMapView;
    //返回按钮
    private Button BaiDuMapBack;
    //店铺名称
    private TextView shopname;
    private TextView shopaddress;
    //店铺地址
    private LocationClient locationClient;
    private BaiduMap mapControl;
    //店铺详情对象
    private UserShopDetail userShopDetail;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //获取消息内容 mes.arg1 msg.arg2 msg.gatDate();
                case 1:
                    Bundle b = msg.getData();
                     double lat1 = b.getDouble("lat");
                     double lng1 = b.getDouble("lng");
                    LatLng ll = new LatLng(lat1,lng1);
                    MapStatusUpdate statusUpdate = MapStatusUpdateFactory.newLatLngZoom(ll,16);
                    //移到地图中心
                    mapControl.animateMapStatus(statusUpdate);
                    //构建Marker图标
                    LatLng point =new LatLng(lat1, lng1);
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.mipmap.baidumap_location);
                    //构建MarkerOption，用于在地图上添加Marker
                    OverlayOptions option = new MarkerOptions()
                            .position(point)
                            .icon(bitmap);
                    //在地图上添加Marker，并显示
                    mapControl.addOverlay(option);


                   /* //定义文字所显示的坐标点
                    LatLng llText = new LatLng(lat1, lng1);
                    //构建文字Option对象，用于在地图上添加文字
                    OverlayOptions textOption = new TextOptions()
                            .bgColor(0xAAFFFF00)
                            .fontSize(35)
                            .fontColor(0xFFFF00FF)
                            .text(userShopDetail.getShopName())
                            .rotate(0)
                            .position(llText);
                    //在地图上添加该文字对象并显示
                    mapControl.addOverlay(textOption);*/
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_shop_detail_baidu_map);
        // 获取地图控件引用
        mMapView = (MapView) findViewById(R.id.shop_detail_map);
        shopname =findViewById(R.id.baidu_map_shopname);
        shopaddress = findViewById(R.id.baidu_map_address);
        //获取intent传递过来的店铺详情对象
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userShopDetail = (UserShopDetail) bundle.getSerializable("userShopDetail");
        //s设置店铺名称、店铺地址
        shopaddress.setText(userShopDetail.getShopAddress());
        shopname.setText(userShopDetail.getShopName());

        //获取地图控制器的对象
        mapControl = mMapView.getMap();

        //设置图层定位方式
        mapControl.setMyLocationEnabled(true);
        //1.初始化定位客户端
        locationClient = new LocationClient(getApplicationContext());
        //开启定位服务
        locationClient.start();
        //2.设置定位客户端参数
        LocationClientOption option = new LocationClientOption();
        //给参数配置属性
        //打开GPS定位
        option.setOpenGps(true);
        //设置允许获取地址信息
        option.setIsNeedAddress(true);
        //设置允许获取定位的周边POI数据
        option.setIsNeedLocationPoiList(false);
        //设置循环定位点信息的时间间隔
        option.setScanSpan(5000);
        //设置采用的经纬坐标，采用百度坐标系
        option.setCoorType("bd09ll");
        //设置定位模式
        option.setLocationMode(
                LocationClientOption.LocationMode.Hight_Accuracy
        );
        //给定位客户端应用定位参数
        locationClient.setLocOption(option);
        //通过地址获取经纬度坐标
        getLngAndLat(userShopDetail.getShopAddress());
        //获取返回控件
        BaiDuMapBack= findViewById(R.id.baidu_map_back);
        BaiDuMapBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
    //通过地址获取地址的经纬度坐标并返回经纬度坐标
    public  void getLngAndLat(final String address) {
        final String url = "http://api.map.baidu.com/geocoder/v2/?address=" + address + "&output=json&ak=" + AK + "&mcode=" + SHAI + ";" + PACKAGE;
        new Thread() {
            @Override
            public void run() {
                StringBuffer json = new StringBuffer();
                try {
                    URL urlObj = new URL(url);
                    URLConnection conn = urlObj.openConnection();
                    conn.connect();
                    InputStream in = conn.getInputStream();
                    InputStreamReader inReader = new InputStreamReader(in, "utf-8");
                    BufferedReader reader = new BufferedReader(inReader);
                    String inputLine = null;
                    while ((inputLine = reader.readLine()) != null) {
                        json.append(inputLine);
                    }
                    in.close();
                    JSONObject obj = null;//JSONObject.fromObject(json);
                    try {
                        obj = new JSONObject(json.toString());
                        if (obj.get("status").toString().equals("0")) {
                            double lng = obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
                            double lat = obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
                            LatLng latLng = new LatLng(lat,lng);

                            Message message = Message.obtain();
                            //包装Message对象，本质就是想Message对象中添加数据
                            //Message 对象有三个属相（int） : arg1,arg2,what
                            //Message 的方法：setData(Bundlle对象)，getDate()
                            message.obj= latLng;
                            Bundle b = new Bundle();
                            b.putDouble("lng",lng);
                            b.putDouble("lat",lat);
                            message.what = 1;
                            message.setData(b);
                            //利用Handler对象发送消息
                           handler.sendMessage(message);
                            Log.i("lhy", address + "经度：" + lng + "--- 纬度：" + lat);
                        } else {
                            System.out.println("未找到相匹配的经纬度！");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                }
            }
        }.start();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
        //关闭图层定位
        mapControl.setMyLocationEnabled(false);
        //关闭定位服务
        locationClient.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapControl.setMyLocationEnabled(true);
        //开启定位服务
        if (!locationClient.isStarted()) {
            locationClient.start();
        }
    }
}
