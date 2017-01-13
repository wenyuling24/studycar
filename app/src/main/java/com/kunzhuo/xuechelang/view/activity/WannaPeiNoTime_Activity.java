package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.AndroidApplication;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.MyPeiPrice_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DateUtils;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.utils.ToolsUtils;
import com.kunzhuo.xuechelang.view.adapter.MyPeiPrice_Adapter;
import com.kunzhuo.xuechelang.widget.MyGridView;
import com.kunzhuo.xuechelang.widget.dialog.DateTimePickDialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/1 0001.
 * 无陪练时间
 */
public class WannaPeiNoTime_Activity extends BaseActivity {

    private static final String tag = "WannaPeiNoTime_Activity";

    @BindView(R.id.id_peiBtn)
    TextView idPeiBtn;
    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.second_peiPriceGrid)
    MyGridView secondPeiPriceGrid;
    @BindView(R.id.id_peiPriceMsg)
    TextView idPeiPriceMsg;
    @BindView(R.id.id_coachEditName)
    EditText idCoachEditName;
    @BindView(R.id.id_coachEditPhone)
    EditText idCoachEditPhone;
    @BindView(R.id.id_coachEditTime)
    TextView idCoachEditTime;
    @BindView(R.id.id_coachEditAddressPei)
    EditText idCoachEditAddressPei;
    @BindView(R.id.id_coachEditAddress)
    EditText idCoachEditAddress;
    @BindView(R.id.id_coachEdiJiaNum)
    EditText idCoachEdiJiaNum;
    @BindView(R.id.id_afterPrice)
    TextView idAfterPrice;
    @BindView(R.id.id_getPeiAddressBtn)
    TextView idGetPeiAddressBtn;
    @BindView(R.id.id_coachAddressBtn)
    TextView idCoachAddressBtn;

    private Context context;
    private ProgressDialog dialog;
    private MyPeiPrice_Adapter adapter;
    private String PriceType;
    private String ID;
    private String Uid;


    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationConfiguration.LocationMode mCurrentMode;

    private UUID guid;
    private int LocationType = 1;
    private double longitude;
    private double latitude;
    private String Province;
    private String City;
    private String Area;
    private String address;

    // 用来保存那些被选中的
    boolean[] checksKe; // = new boolean[3];

    private String initStartDateTime; // = DateUtils.getNowtime(); // 初始化开始时间

    @Override
    protected int setLayoutId() {
        return R.layout.mypeilian_layout2;
    }

    @Override
    protected void setDefaultViews() {

        initViews();
        initLocation();
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void updateViews() {

    }

    private void initViews() {

        context = this;
        AndroidApplication.activityList2.add(WannaPeiNoTime_Activity.this);
        itemTitlemsg.setText("学车郎—我要陪练");
        guid = UUID.randomUUID();
        PriceType = getIntent().getStringExtra("PriceType");
        ID = getIntent().getStringExtra("ID");
        Uid = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.USER_ID);

        idCoachEditPhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        adapter = new MyPeiPrice_Adapter(context, null);
        secondPeiPriceGrid.setAdapter(adapter);

        secondPeiPriceGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                MyPeiPrice_Bean bean = (MyPeiPrice_Bean) adapterView.getItemAtPosition(i);

                idPeiPriceMsg.setText(bean.getPexplain());
                idAfterPrice.setText(bean.getPmoney());
                TextView typeKe1 = (TextView) view.findViewById(R.id.type_Ke1);

                typeKe1.setBackgroundResource(R.drawable.btn_selected);

                for (int k = 0; k < adapter.getCount(); k++) {

                    View tempView = secondPeiPriceGrid.getChildAt(k);

                    TextView typeKe = (TextView) tempView.findViewById(R.id.type_Ke1);

                    if (k == i) {
                        checksKe[k] = true;

                    } else {
                        checksKe[k] = false;
                        typeKe.setBackgroundResource(R.drawable.btn_chooseorange);
                    }
                }

            }
        });

        initStartDateTime = DateUtils.getNowtime(); // 初始化开始时间

        idCoachEditTime.setText(initStartDateTime);

        idCoachEditTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        WannaPeiNoTime_Activity.this, "");

                dateTimePicKDialog.dateTimePicKDialog(idCoachEditTime);

            }
        });


        dialog = new ProgressDialog(context);
        dialog.setMessage("正在加载, 请稍后...");
        dialog.show();


        Resquest.getUnitPriceList(handler, PriceType);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.id_peiBtn, R.id.item_back, R.id.id_getPeiAddressBtn, R.id.id_coachAddressBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_peiBtn:

                if (Uid.equals("")) {

                    ToastUtil.show(context, "您还未登录, 无法购买, 请先返回登录");

                } else {
                    String OrdAddress = idCoachEditAddressPei.getText().toString();
                    String mg_address = idCoachEditAddress.getText().toString();
                    String licenseH = idCoachEdiJiaNum.getText().toString();
                    String Telephone = idCoachEditPhone.getText().toString();
                    String Name = idCoachEditName.getText().toString();
                    String LearnTime = idCoachEditTime.getText().toString();
                    String orderMoney = idAfterPrice.getText().toString();

                    if (!Name.equals("")) {

                        if (!Telephone.equals("") && ToolsUtils.isMobileNO(Telephone)) {

                            if (!licenseH.equals("")) {

                                if (!orderMoney.equals("")) {

                                    Resquest.addSp_ordered_Add(handler2, guid.toString(), OrdAddress, mg_address,
                                            licenseH, Telephone, Name, longitude, latitude, ID, LearnTime, Uid);

                                } else {
                                    ToastUtil.show(context, "请选择正确的陪练类型");
                                }

                            } else {
                                ToastUtil.show(context, "请输入正确的驾照号码");
                            }

                        } else {
                            ToastUtil.show(context, "请输入正确的手机号");
                        }

                    } else {
                        ToastUtil.show(context, "请输入联系人姓名");
                    }
                }

                break;
            case R.id.item_back:
                finish();
                AndroidApplication.activityList2.clear();
                break;
            case R.id.id_getPeiAddressBtn:

                idCoachEditAddressPei.setText(address.replace("中国", ""));
                break;
            case R.id.id_coachAddressBtn:

                if (address != null) {
                    idCoachEditAddress.setText(address.replace("中国", ""));
                    idCoachEditAddressPei.setText(address.replace("中国", ""));
                }


                break;
        }
    }

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {

                            Type type = new TypeToken<List<MyPeiPrice_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<MyPeiPrice_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            adapter.setData(mList);
                            adapter.notifyDataSetChanged();

                            checksKe = new boolean[mList.size()];
                        }
                        if (Code.equals("8")) {


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.cancel();
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(context, "网络异常");
                    dialog.cancel();
                    break;
            }

            return true;
        }
    });


    Handler handler2 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {

                            ToastUtil.show(context, "操作成功");

                        }
                        if (Code.equals("8")) {


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.cancel();
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(context, "网络异常");
                    dialog.cancel();
                    break;
            }

            return true;
        }
    });


    private void initLocation() {

        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);// 设置定位结果包含地址信息
        mLocClient.setLocOption(option);
        mLocClient.start();
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(3000)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

            if (isFirstLoc) {
                isFirstLoc = false;
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Province = location.getProvince();
                City = location.getCity();
                Area = location.getDistrict();
                address = location.getAddrStr();

                Log.i(tag, "Province" + Province + "City" + City + "Area" + Area + "address:"
                        + address + " latitude:" + latitude
                        + " longitude:" + longitude + "---");

                if (address != null) {
                    idCoachEditAddress.setText(address.replace("中国", ""));
                    idCoachEditAddressPei.setText(address.replace("中国", ""));
                }


//                LatLng ll = new LatLng(location.getLatitude(),
//                        location.getLongitude());
//                MapStatus.Builder builder = new MapStatus.Builder();
//                builder.target(ll).zoom(14.0f);
            }

            if (mLocClient.isStarted()) {
                // 获得位置之后停止定位
                mLocClient.stop();
            }

        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

}
