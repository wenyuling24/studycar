package com.kunzhuo.xuechelang.view.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.BikingRouteOverlay;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.MapList_Bean;
import com.kunzhuo.xuechelang.view.adapter.RouteLineAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/13 0013.
 */
public class CoachMap_Activity extends BaseActivity {

    private static final String tag = "CoachMap_Activity";

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.mapView)
    MapView mMapView;
    @BindView(R.id.pre)
    Button mBtnPre;
    @BindView(R.id.next)
    Button mBtnNext;

    private Context context;
    private BaiduMap mBaiduMap;
    private double latitude = 0;
    private double longitude = 0;
    private String Province = "";
    private String City = "";

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    private boolean isFirstLoc = true; // 是否首次定位
    private double coach_latitude = 0;
    private double coach_longitude = 0;
    private String Name = "";
    // 初始化全局 bitmap 信息，不用时及时 recycle
    private BitmapDescriptor mIconMaker;
    private MapList_Bean mapList_bean;

    // 搜索相关
    private RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    private TransitRouteResult nowResult = null;
    private DrivingRouteResult nowResultd = null;

    // 浏览路线节点相关
    int nodeIndex = -1; // 节点索引,供浏览节点时使用
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    boolean useDefaultIcon = true;
    private TextView popupText = null; // 泡泡view

    @Override
    protected int setLayoutId() {
        return R.layout.coachmap_layout;
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

    private void initViews() {

        context = this;
        mapList_bean = new MapList_Bean();
        Name = getIntent().getStringExtra("Name");
        coach_longitude = getIntent().getDoubleExtra("longitude", 0);
        coach_latitude = getIntent().getDoubleExtra("latitude", 0);
        mapList_bean.setName(Name);
        mapList_bean.setY(coach_latitude);
        mapList_bean.setX(coach_longitude);
        itemTitlemsg.setText(Name + "的位置");
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);

        mIconMaker = BitmapDescriptorFactory.fromResource(R.drawable.icon_marker);
        mBaiduMap = mMapView.getMap();
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(context);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型 设置返回定位结果是百度经纬度 默认gcj02
        option.setScanSpan(1000);// 设置发起定位请求的时间间隔 单位ms
        option.setIsNeedAddress(true);// 设置定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 设置定位结果包含手机机头 的方向

        mLocClient.setLocOption(option);
        mLocClient.start();


        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(listener3);

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                //获得marker中的数据
                final MapList_Bean info = (MapList_Bean) marker.getExtraInfo().get("info");

                InfoWindow mInfoWindow;
                //生成一个TextView用户在地图中显示InfoWindow
//                TextView location = new TextView(context);
//                location.setBackgroundResource(R.drawable.icon_dialogaddress);
//                location.setPadding(30, 20, 30, 50);
//                location.setText(info.getName());

                View view = LayoutInflater.from(context).inflate(R.layout.map_item, null);
                view.setBackgroundResource(R.drawable.icon_dialogaddress);
                view.setPadding(30, 20, 30, 50);
                TextView txtView = (TextView) view.findViewById(R.id.id_mapitemTxt);
                txtView.setText(info.getName());

                view.findViewById(R.id.id_mapitemBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 导航
//                        PlanNode stNode = PlanNode.withCityNameAndPlaceName(Province, startAddress1);
//                        PlanNode enNode = PlanNode.withCityNameAndPlaceName(Province, stopAddress2);

                        PlanNode stNode1 = PlanNode.withLocation(new LatLng(latitude,
                                longitude));

                        PlanNode enNode1 = PlanNode.withLocation(new LatLng(info.getY(),
                                info.getX()));

                        mSearch.transitSearch((new TransitRoutePlanOption())
                                .from(stNode1).city(City).to(enNode1));

                    }
                });

                //将marker所在的经纬度的信息转化成屏幕上的坐标
                final LatLng ll = marker.getPosition();
                Point p = mBaiduMap.getProjection().toScreenLocation(ll);
                Log.e(tag, "--!" + p.x + " , " + p.y);
                p.y -= 47;
                LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
                //为弹出的InfoWindow添加点击事件
                mInfoWindow = new InfoWindow(view, llInfo, -47);
                //显示InfoWindow
                mBaiduMap.showInfoWindow(mInfoWindow);
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
                mBaiduMap.hideInfoWindow();

            }
        });
    }

    @OnClick({R.id.item_back, R.id.item_titlemsg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.item_titlemsg:
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
                    .accuracy(100)
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
                String Area = location.getDistrict();
                String address = location.getAddrStr();

                Log.i(tag, "Province" + Province + "City" + City + "Area" + Area + "address:"
                        + address + " latitude:" + latitude
                        + " longitude:" + longitude + "---");

                addInfosOverlay(mapList_bean);

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
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
        mIconMaker.recycle();
        if (mLocClient != null) {
            mLocClient.unRegisterLocationListener(myListener);
        }
        mSearch.destroy();
    }

    private void addInfosOverlay(MapList_Bean info) {

        mLocClient.stop();
        mBaiduMap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        // 位置
        latLng = new LatLng(info.getY(), info.getX());
        // 图标
        overlayOptions = new MarkerOptions().position(latLng)
                .icon(mIconMaker).zIndex(5);
        marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", info);
        marker.setExtraInfo(bundle);

//        // 将地图移到到最后一个经纬度位置
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(u);
    }

    OnGetRoutePlanResultListener listener3 = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(context, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // result.getSuggestAddrInfo()
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                nodeIndex = -1;
                mBtnPre.setVisibility(View.VISIBLE);
                mBtnNext.setVisibility(View.VISIBLE);
                route = result.getRouteLines().get(0);
                WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult result) {

            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(context, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // result.getSuggestAddrInfo()
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                nodeIndex = -1;
                mBtnPre.setVisibility(View.VISIBLE);
                mBtnNext.setVisibility(View.VISIBLE);

                if (result.getRouteLines().size() > 1) {
                    nowResult = result;

                    MyTransitDlg myTransitDlg = new MyTransitDlg(context,
                            result.getRouteLines(),
                            RouteLineAdapter.Type.TRANSIT_ROUTE);
                    myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                        public void onItemClick(int position) {
                            route = nowResult.getRouteLines().get(position);
                            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
                            mBaiduMap.setOnMarkerClickListener(overlay);
                            routeOverlay = overlay;
                            overlay.setData(nowResult.getRouteLines().get(position));
                            overlay.addToMap();
                            overlay.zoomToSpan();
                        }

                    });
                    myTransitDlg.show();


                } else if (result.getRouteLines().size() == 1) {
                    // 直接显示
                    route = result.getRouteLines().get(0);
                    TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    routeOverlay = overlay;
                    overlay.setData(result.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();

                } else {
                    Log.d("transitresult", "结果数<0");
                    return;
                }


            }
        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult result) {

            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(context, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // result.getSuggestAddrInfo()
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                nodeIndex = -1;

                if (result.getRouteLines().size() > 1) {
                    nowResultd = result;

                    MyTransitDlg myTransitDlg = new MyTransitDlg(context,
                            result.getRouteLines(),
                            RouteLineAdapter.Type.DRIVING_ROUTE);
                    myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                        public void onItemClick(int position) {
                            route = nowResultd.getRouteLines().get(position);
                            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
                            mBaiduMap.setOnMarkerClickListener(overlay);
                            routeOverlay = overlay;
                            overlay.setData(nowResultd.getRouteLines().get(position));
                            overlay.addToMap();
                            overlay.zoomToSpan();
                        }

                    });
                    myTransitDlg.show();

                } else if (result.getRouteLines().size() == 1) {
                    route = result.getRouteLines().get(0);
                    DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
                    routeOverlay = overlay;
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    overlay.setData(result.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                    mBtnPre.setVisibility(View.VISIBLE);
                    mBtnNext.setVisibility(View.VISIBLE);
                }

            }
        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            if (bikingRouteResult == null || bikingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(context, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            }
            if (bikingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // result.getSuggestAddrInfo()
                return;
            }
            if (bikingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                nodeIndex = -1;
                mBtnPre.setVisibility(View.VISIBLE);
                mBtnNext.setVisibility(View.VISIBLE);
                route = bikingRouteResult.getRouteLines().get(0);
                BikingRouteOverlay overlay = new MyBikingRouteOverlay(mBaiduMap);
                routeOverlay = overlay;
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(bikingRouteResult.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }

        }
    };

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyBikingRouteOverlay extends BikingRouteOverlay {
        public MyBikingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    // 响应DLg中的List item 点击
    interface OnItemInDlgClickListener {
        public void onItemClick(int position);
    }

    // 供路线选择的Dialog
    class MyTransitDlg extends Dialog {

        private List<? extends RouteLine> mtransitRouteLines;
        private ListView transitRouteList;
        private RouteLineAdapter mTransitAdapter;

        OnItemInDlgClickListener onItemInDlgClickListener;

        public MyTransitDlg(Context context, int theme) {
            super(context, theme);
        }

        public MyTransitDlg(Context context, List<? extends RouteLine> transitRouteLines, RouteLineAdapter.Type
                type) {
            this(context, 0);
            mtransitRouteLines = transitRouteLines;
            mTransitAdapter = new RouteLineAdapter(context, mtransitRouteLines, type);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_transit_dialog);

            transitRouteList = (ListView) findViewById(R.id.transitList);
            transitRouteList.setAdapter(mTransitAdapter);

            transitRouteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemInDlgClickListener.onItemClick(position);
                    mBtnPre.setVisibility(View.VISIBLE);
                    mBtnNext.setVisibility(View.VISIBLE);
                    dismiss();

                }
            });
        }

        public void setOnItemInDlgClickLinster(OnItemInDlgClickListener itemListener) {
            onItemInDlgClickListener = itemListener;
        }

    }

    /**
     * 发起路线规划搜索示例
     *
     * @param v
     */
    public void searchButtonProcess(View v) {
        // 重置浏览节点的路线数据
        route = null;
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        mBaiduMap.clear();
        // 设置起终点信息，对于tranist search 来说，城市名无意义

        System.out.println(latitude + "weid纬度" + longitude + "经度" + coach_latitude + "纬度2" + coach_longitude + "经度2");

        PlanNode stNode = PlanNode.withLocation(new LatLng(latitude,
                longitude));

        PlanNode enNode = PlanNode.withLocation(new LatLng(coach_latitude,
                coach_longitude));

        // 实际使用中请对起点终点城市进行正确的设定
        if (v.getId() == R.id.drive) {
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode).to(enNode));
        } else if (v.getId() == R.id.transit) {
            //获取反向地理编码结果
            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(stNode).city(City).to(enNode));
        } else if (v.getId() == R.id.walk) {
            mSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(stNode).to(enNode));
        } else if (v.getId() == R.id.bike) {
            mSearch.bikingSearch((new BikingRoutePlanOption())
                    .from(stNode).to(enNode));
        }
    }

    /**
     * 节点浏览示例
     *
     * @param v
     */
    public void nodeClick(View v) {
        if (route == null || route.getAllStep() == null) {
            return;
        }
        if (nodeIndex == -1 && v.getId() == R.id.pre) {
            return;
        }
        // 设置节点索引
        if (v.getId() == R.id.next) {
            if (nodeIndex < route.getAllStep().size() - 1) {
                nodeIndex++;
            } else {
                return;
            }
        } else if (v.getId() == R.id.pre) {
            if (nodeIndex > 0) {
                nodeIndex--;
            } else {
                return;
            }
        }
        // 获取节结果信息
        LatLng nodeLocation = null;
        String nodeTitle = null;
        Object step = route.getAllStep().get(nodeIndex);
        if (step instanceof DrivingRouteLine.DrivingStep) {
            nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrance().getLocation();
            nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
        } else if (step instanceof WalkingRouteLine.WalkingStep) {
            nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrance().getLocation();
            nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
        } else if (step instanceof TransitRouteLine.TransitStep) {
            nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrance().getLocation();
            nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
        } else if (step instanceof BikingRouteLine.BikingStep) {
            nodeLocation = ((BikingRouteLine.BikingStep) step).getEntrance().getLocation();
            nodeTitle = ((BikingRouteLine.BikingStep) step).getInstructions();
        }

        if (nodeLocation == null || nodeTitle == null) {
            return;
        }
        // 移动节点至中心
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
        // show popup
        popupText = new TextView(context);
        popupText.setBackgroundResource(R.drawable.icon_dialogaddress);
        popupText.setTextColor(0xFF000000);
        popupText.setText(nodeTitle);
        mBaiduMap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));

    }

}
