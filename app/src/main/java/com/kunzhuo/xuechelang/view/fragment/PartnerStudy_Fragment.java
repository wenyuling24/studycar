package com.kunzhuo.xuechelang.view.fragment;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.MapList_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.activity.CoachDetails_Activity;
import com.kunzhuo.xuechelang.view.activity.CoachShows_Activity;
import com.kunzhuo.xuechelang.view.activity.UpCoachMsg_Activity;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/10 0010.
 */
public class PartnerStudy_Fragment extends BaseFragment {
    private static final String tag = "PartnerStudy_Fragment";

    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.id_rightFrame)
    FrameLayout idRightFrame;
    @BindView(R.id.id_bottomFrame)
    FrameLayout idBottomFrame;
    @BindView(R.id.id_marker_info)
    RelativeLayout mMarkerInfoLy;
    @BindView(R.id.id_mapBtnmade)
    Button idMapBtnmade;
    @BindView(R.id.id_mapBtnshowCoach)
    Button idMapBtnshowCoach;

    private BaiduMap mBaiduMap;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    private boolean isFirstLoc = true; // 是否首次定位
    private double latitude = 0;
    private double longitude = 0;
    String Province;
    String City;
    String Area;
    String address;

    // 初始化全局 bitmap 信息，不用时及时 recycle
    private BitmapDescriptor mIconMaker;

    @Override
    protected int setLayoutId() {
        return R.layout.map_layout;
    }

    @Override
    protected void setDefaultViews() {

        initViews();
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void upDetoryViews() {

    }

    private void initViews() {

        mIconMaker = BitmapDescriptorFactory.fromResource(R.drawable.icon_marker);
        mBaiduMap = mMapView.getMap();
        mCurrentMode = LocationMode.NORMAL;
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型 设置返回定位结果是百度经纬度 默认gcj02
        option.setScanSpan(1000);// 设置发起定位请求的时间间隔 单位ms
        option.setIsNeedAddress(true);// 设置定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 设置定位结果包含手机机头 的方向

        mLocClient.setLocOption(option);
        mLocClient.start();

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                //获得marker中的数据
                MapList_Bean info = (MapList_Bean) marker.getExtraInfo().get("info");

                InfoWindow mInfoWindow;
                //生成一个TextView用户在地图中显示InfoWindow
                TextView location = new TextView(getActivity());
                location.setBackgroundResource(R.drawable.icon_dialogaddress);
                location.setPadding(30, 20, 30, 50);
                location.setText(info.getName());
                //将marker所在的经纬度的信息转化成屏幕上的坐标
                final LatLng ll = marker.getPosition();
                Point p = mBaiduMap.getProjection().toScreenLocation(ll);
                Log.e(tag, "--!" + p.x + " , " + p.y);
                p.y -= 47;
                LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
                //为弹出的InfoWindow添加点击事件

                mInfoWindow = new InfoWindow(location, llInfo, -47);

                //显示InfoWindow
                mBaiduMap.showInfoWindow(mInfoWindow);
                //设置详细信息布局为可见
                mMarkerInfoLy.setVisibility(View.VISIBLE);
                idBottomFrame.setVisibility(View.GONE);
                //根据商家信息为详细信息布局设置信息
                popupInfo(mMarkerInfoLy, info);
                return true;
            }
        });

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {

            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                return false;
            }

            @Override
            public void onMapClick(LatLng arg0) {
                mMarkerInfoLy.setVisibility(View.GONE);
                idBottomFrame.setVisibility(View.VISIBLE);
                mBaiduMap.hideInfoWindow();

            }
        });
    }


    @OnClick({R.id.id_mapBtnshowCoach, R.id.id_mapBtnmade})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_mapBtnshowCoach:
                Intent intent = new Intent(getActivity(), CoachShows_Activity.class);
                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitude);
                startActivity(intent);
                break;
            case R.id.id_mapBtnmade:

                Intent intent2 = new Intent(getActivity(), UpCoachMsg_Activity.class);
                intent2.putExtra("LocationType", 1);
                intent2.putExtra("longitude", longitude);
                intent2.putExtra("latitude", latitude);
                intent2.putExtra("Province", Province);
                intent2.putExtra("City", City);
                intent2.putExtra("Area", Area);
                intent2.putExtra("address", address);

                startActivity(intent2);

                break;

        }
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(3000)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Province = location.getProvince();
                City = location.getCity();
                Area = location.getDistrict();
                address = location.getAddrStr();

                Resquest.getMapList(handler, longitude, latitude);

                Log.i(tag, "Province" + Province + "City" + City + "Area" + Area + "address:"
                        + address + " latitude:" + latitude
                        + " longitude:" + longitude + "---");

                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(14.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }

            if (mLocClient.isStarted()) {
                // 获得位置之后停止定位
                mLocClient.stop();
            }

        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        super.onDestroy();
        mIconMaker.recycle();

        if (mLocClient != null) {
            mLocClient.unRegisterLocationListener(myListener);
        }

//        mMapView.onDestroy();
//        mMapView = null;

    }

    private void toCoachDetails(MapList_Bean bean) {
        Intent intent = new Intent(getActivity(), CoachDetails_Activity.class);
        intent.putExtra("ID", bean.getID());
        intent.putExtra("Name", bean.getName());
        startActivity(intent);
    }


    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        Type type = new TypeToken<List<MapList_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<MapList_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        addInfosOverlay(mList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(getActivity(), "网络异常");
                    break;
            }

            return true;
        }
    });


    private void addInfosOverlay(List<MapList_Bean> infos) {

        mBaiduMap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        for (MapList_Bean info : infos) {
            // 位置
            latLng = new LatLng(info.getY(), info.getX());
            // 图标
            overlayOptions = new MarkerOptions().position(latLng)
                    .icon(mIconMaker).zIndex(5);
            marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
        }
//        // 将地图移到到最后一个经纬度位置
//        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
//        mBaiduMap.setMapStatus(u);

    }


    /**
     * 复用弹出面板mMarkerLy的控件
     */
    private class ViewHolder {
        ImageView infoImg; // 头像
        TextView infoName; // 名称
        TextView infoPhone; // 电话
        TextView infoAddress; // 地址
    }


    /**
     * 根据info为布局上的控件设置信息
     *
     * @param info
     */
    protected void popupInfo(RelativeLayout mMarkerLy, final MapList_Bean info) {
        ViewHolder viewHolder = null;
        if (mMarkerLy.getTag() == null) {
            viewHolder = new ViewHolder();
            viewHolder.infoImg = (ImageView) mMarkerLy
                    .findViewById(R.id.coach_logo);
            viewHolder.infoName = (TextView) mMarkerLy
                    .findViewById(R.id.coach_name);
            viewHolder.infoPhone = (TextView) mMarkerLy.findViewById(R.id.coach_phone);
            viewHolder.infoAddress = (TextView) mMarkerLy.findViewById(R.id.coach_address);

            mMarkerLy.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) mMarkerLy.getTag();

        if (!info.getHead_portrait().equals("")) {
            ImageLoader.getInstance().displayImage(info.getHttpImg() + info.getHead_portrait(), viewHolder.infoImg);

        }
        viewHolder.infoName.setText(info.getName());
        viewHolder.infoPhone.setText(info.getTelephone());
        viewHolder.infoAddress.setText(info.getSite_address());

        mMarkerLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toCoachDetails(info);
            }
        });

    }


}
