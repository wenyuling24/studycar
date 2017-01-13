package com.kunzhuo.xuechelang.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.AndroidApplication;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.CoachShows_Bean;
import com.kunzhuo.xuechelang.model.MapCommentList_Bean;
import com.kunzhuo.xuechelang.model.MapUserPic_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.network.SystemBase;
import com.kunzhuo.xuechelang.utils.NormalUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.utils.ToolsUtils;
import com.kunzhuo.xuechelang.utils.bitmap.BitmapUtil;
import com.kunzhuo.xuechelang.utils.eventutils.FirstEvent;
import com.kunzhuo.xuechelang.view.adapter.MapUserPic_Adapter_2;
import com.kunzhuo.xuechelang.widget.CustomImageView;
import com.kunzhuo.xuechelang.widget.MyGridView;
import com.kunzhuo.xuechelang.wxapi.ContantsUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class UpCoachMsg_Activity extends BaseActivity {

    private static final String tag = "UpCoachMsg_Activity";

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_coachmsgImg)
    CustomImageView idCoachmsgImg;
    @BindView(R.id.id_coachImgLayout)
    LinearLayout idCoachImgLayout;
    @BindView(R.id.id_coachEditName)
    EditText idCoachEditName;
    @BindView(R.id.id_coachEditPhone)
    EditText idCoachEditPhone;
    @BindView(R.id.spinnerType)
    Spinner spinnerType;
    @BindView(R.id.spinnerCarType)
    Spinner spinnerCarType;
    @BindView(R.id.spinnerYear)
    Spinner spinnerYear;
    @BindView(R.id.id_coachEditSchool)
    EditText idCoachEditSchool;
    @BindView(R.id.id_coachEditCom)
    EditText idCoachEditCom;
    @BindView(R.id.id_coachEditChang)
    EditText idCoachEditChang;
    @BindView(R.id.id_coachEditAddress)
    EditText idCoachEditAddress;
    @BindView(R.id.id_EditInfo)
    EditText idEditInfo;
    @BindView(R.id.id_coachImggridView)
    MyGridView idCoachImggridView;
    @BindView(R.id.id_coachEditCity)
    EditText idCoachEditCity;
    @BindView(R.id.id_coachUpBtn)
    TextView idCoachUpBtn;
    @BindView(R.id.id_rightLayoutWeichat)
    RelativeLayout idRightLayoutWeichat;

    private Context context;
    private UUID guid;
    private int LocationType = 1;
    private double longitude;
    private double latitude;
    private String Province;
    private String City;
    private String Area;
    private String address;
    private MapUserPic_Adapter_2 adapter;

    public static IWXAPI WXapi;
    private String get_access_token = "";

    private String[] infosArray = new String[]{"", "教练是以为对工作和学员极端负责的人, 教学态度好, 教学认真, 关心学员, 是一位难得的好教练",
            "教练教学态度严谨, 认真负责, 使我们掌握驾驶技能, 顺利考取驾照, 非常感谢！",
            "教练人品好, 待人谦和, 为人善良, 一视同仁对待每位学员.",
            "教练为人特别耿直, 公正, 淳朴, 对工作一丝不苟,踏踏实实, 认真负责,令人敬佩"};

    private ArrayAdapter<String> adapter_Type;
    private ArrayAdapter<String> adapter_CarType;
    private ArrayAdapter<String> adapter_Year;

    private static final String[] isModel = {"科二教练", "科三教练", "综合教练(包含科二科三)"};
    private static final String[] isModelStr = {"2", "3", "0"};

    private static final String[] isCarModel = {"C1", "C2", "综合车型(包含C1, C2)"};
    private static final String[] isCarModelStr = {"C1", "C2", "0"};

    private ArrayList<String> yearList_Str = new ArrayList<>(); // 教龄列表
    private String upType = "";
    private String upCarType = "";
    private String upYear = "";
    private String openid;
    private String headimgurl;
    private String Head_portrait;

    private ArrayList<MapUserPic_Bean> imgList = new ArrayList<>();

    private ProgressDialog dialog;
    private HttpUtils httpUtils;
    private String[] items = new String[]{"选择本地图片", "拍照"};

    /* 头像名称 */
    private static final String IMAGE_FILE_NAME = "tempUserHead.jpg";
    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int IMAGE_REQUEST_CODE2 = 3;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private String ID;
    private String Name;

    @Override
    protected int setLayoutId() {
        return R.layout.upcoach_layout;
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
        guid = UUID.randomUUID();
        itemTitlemsg.setText("填写个人信息");
        idRightLayoutWeichat.setVisibility(View.VISIBLE);
        httpUtils = new HttpUtils(30000);
        dialog = new ProgressDialog(context);
        initLocation();

        LocationType = getIntent().getIntExtra("LocationType", 1);

        if (LocationType == 1) {
            longitude = getIntent().getDoubleExtra("longitude", 0);
            latitude = getIntent().getDoubleExtra("latitude", 0);
            Province = getIntent().getStringExtra("Province");
            City = getIntent().getStringExtra("City");
            Area = getIntent().getStringExtra("Area");
            address = getIntent().getStringExtra("address");

            idCoachEditChang.setText(address.replace("中国" + City + Area, ""));
            idCoachEditAddress.setText(address.replace("中国" + City + Area, ""));
            idCoachEditCity.setText(City + Area);

        }
        if (LocationType == 0) {

            mLocClient.start();
            ID = getIntent().getStringExtra("ID");
            Name = getIntent().getStringExtra("Name");

            Resquest.getMapUser(handler, ID);

            Resquest.getMapUserPic(handler2, ID, 1, 1);

        }

        idCoachEditPhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        adapter = new MapUserPic_Adapter_2(context, imgList);
        idCoachImggridView.setAdapter(adapter);

        idCoachImggridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(context, GalleryActivity.class);
                if (LocationType == 1) {
                    intent.putExtra("ImgType", 1);
                }
                if (LocationType == 0) {
                    intent.putExtra("ImgType", 2);
                    intent.putExtra("ID", ID);
                }

                intent.putExtra("imgList", imgList);
                intent.putExtra("position", i);
                startActivity(intent);
            }
        });

        getYearList();
        getSpinner();

        WXapi = WXAPIFactory.createWXAPI(context,
                ContantsUtils.WX_APP_ID, true);
        WXapi.registerApp(ContantsUtils.WX_APP_ID);

        EventBus.getDefault().register(this);

    }

    private void getSpinner() {

        adapter_Type = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, isModel);

        adapter_CarType = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, isCarModel);

        adapter_Year = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, yearList_Str);

        adapter_Type
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_CarType
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter_Year
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerType.setAdapter(adapter_Type);
        spinnerCarType.setAdapter(adapter_CarType);
        spinnerYear.setAdapter(adapter_Year);

        spinnerType.setOnItemSelectedListener(new TypeSpinnerSelectedListener());
        spinnerCarType.setOnItemSelectedListener(new CarTypeSpinnerSelectedListener());
        spinnerYear.setOnItemSelectedListener(new YearTypeSpinnerSelectedListener());

        // 设置默认值
        spinnerType.setSelection(0, true);
        spinnerCarType.setSelection(0, true);
        spinnerYear.setSelection(0, true);

    }

    @Subscribe
    public void onEventMainThread(FirstEvent event) {

        int msg = event.getMsg();

        Message message = new Message();
        message.what = 1;
        message.obj = msg;

        handler5.sendMessage(message);

    }


    Handler handler5 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                imgList.remove((int) msg.obj);
                adapter.notifyDataSetChanged();
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        judgeWXcode();
    }

    @OnClick({R.id.item_back, R.id.id_coachImgLayout, R.id.id_TxtBtn, R.id.id_coachImgChooseBtn, R.id.id_coachUpBtn, R.id.id_rightLayoutWeichat})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.id_coachImgLayout:

                showDialog();

                break;
            case R.id.id_TxtBtn:
                idEditInfo.setText(infosArray[NormalUtils.setRandom(4)]);
                break;
            case R.id.id_coachImgChooseBtn:
                chooseImg();
                break;
            case R.id.id_coachUpBtn:


                String Name = idCoachEditName.getText().toString();
                String Phone = idCoachEditPhone.getText().toString();
                String CoachType = upType;
                String Models = upCarType;
                String Seniority = upYear;
                String Driver_school = idCoachEditSchool.getText().toString();
                String Company = idCoachEditCom.getText().toString();
                String Site_address = idCoachEditChang.getText().toString();
                String CurrentAddress = idCoachEditAddress.getText().toString();
                double x = longitude;
                double y = latitude;
                String Introduction = idEditInfo.getText().toString();

                if (openid.equals("")) {
                    ToastUtil.show(context, "请先绑定微信");

                } else {
                    if (Name.equals("")) {

                        ToastUtil.show(context, "姓名不能为空");
                    } else {
                        if (!ToolsUtils.isMobileNO(Phone)) {

                            ToastUtil.show(context, "请输入正确的手机号码");
                        } else {
                            if (Driver_school.equals("")) {

                                ToastUtil.show(context, "驾校不能为空");

                            } else {

                                if (Company.equals("")) {

                                    ToastUtil.show(context, "公司不能为空");

                                } else {

                                    if (LocationType == 1) { // 上传
                                        Resquest.addExtension_User_Add(handler1, guid.toString(), openid, Name, Head_portrait, Phone, CoachType, Models
                                                , Seniority, Driver_school, Company, Site_address, CurrentAddress, x, y, Province, City, Area, Introduction);

                                    }
                                    if (LocationType == 0) { //修改

                                        Resquest.editExtension_User_Edit(handler1, ID, openid, Name, Head_portrait, Phone, CoachType, Models
                                                , Seniority, Driver_school, Company, Site_address, CurrentAddress, x, y, Province, City, Area, Introduction, putPicSrc());

                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case R.id.id_rightLayoutWeichat:
                WXLogin();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imgList.clear();
        imgList = null;

        EventBus.getDefault().unregister(this);//反注册EventBus

        // 退出时销毁定位
        mLocClient.stop();
    }

    Handler handler1 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        String Code = jsonObject.getString("Code");

                        if (Code.equals("0")) {

                            System.out.println(putPicSrc() + " 获取的图片数据1");

                            if (!putPicSrc().equals("")) {

                                if (LocationType == 1) { // 上传
                                    Resquest.addMapUserPicAdd(handlerImg, guid.toString(), putPicSrc());
                                }
                                if (LocationType == 0) {
//                                    Resquest.addMapUserPicAdd(handlerImg, ID, putPicSrc());
                                    finish();
                                    ToastUtil.show(context, "修改成功！");
                                }

                            } else {
                                ToastUtil.show(context, "提交成功！");
                                finish();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(context, "网络异常");
                    break;
            }

            return true;
        }
    });


    Handler handlerImg = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        String Code = jsonObject.getString("Code");

                        if (Code.equals("0")) {

                            ToastUtil.show(context, "提交成功！");
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(context, "网络异常");
                    break;
            }

            return true;
        }
    });

    private void getYearList() {
        int a = 41;
        yearList_Str.add(0, "不限教龄");

        for (int i = 1; i < a; i++) {

            yearList_Str.add(i + "年");
        }
    }

    class TypeSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {

            upType = isModelStr[position];

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    class CarTypeSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {

            upCarType = isCarModelStr[position];

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    class YearTypeSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {

            String upYearStr = yearList_Str.get(position);

            if (upYearStr.equals("不限教龄")) {
                upYear = "";

            } else {
                StringBuffer sb = new StringBuffer(upYearStr);
                sb.deleteCharAt(sb.length() - 1);
                upYear = sb.toString();
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /**
     * 显示选择对话框
     */
    private void showDialog() {

        new AlertDialog.Builder(this)
                .setTitle("选择方式")
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intentFromGallery = new Intent(
                                        Intent.ACTION_PICK, null);
                                intentFromGallery
                                        .setDataAndType(
                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                "image/*");

                                startActivityForResult(intentFromGallery,
                                        IMAGE_REQUEST_CODE);
                                break;
                            case 1:
                                takePhoto();
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    private Uri imageUri;

    private void takePhoto() {
        File picture = new File(SystemBase.APP_CACHE_PATH, IMAGE_FILE_NAME);

        imageUri = Uri.fromFile(picture);

        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, CAMERA_REQUEST_CODE);
    }

    private void chooseImg() {
        Intent intentFromGallery = new Intent(
                Intent.ACTION_PICK, null);
        intentFromGallery
                .setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");

        startActivityForResult(intentFromGallery,
                IMAGE_REQUEST_CODE2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    if (data != null) {
                        imageUri = data.getData();
//                        startPhotoZoom(imageUri);
                        dialog.setMessage("正在上传头像,请稍后...");
                        dialog.show();
                        getImageToView(imageUri, 2);
                    }
                    break;
                case CAMERA_REQUEST_CODE:
                    startPhotoZoom(imageUri);
                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {

                        dialog.setMessage("正在上传头像,请稍后...");
                        dialog.show();

                        getImageToView(imageUri, 2);
                    }
                    break;
                case IMAGE_REQUEST_CODE2:
                    if (data != null) {
                        imageUri = data.getData();
//                        startPhotoZoom(imageUri);
                        dialog.setMessage("正在上传图片,请稍后...");
                        dialog.show();
                        getImageToView(imageUri, 1);
                    }
                    break;

                default:
                    break;
            }
        }
        // super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    private void getImageToView(Uri data, int Type) {

        String path = BitmapUtil.getRealFilePath(this, data);

        System.out.println(path + " 图片地址1");

        doUpLoadPic(path, Type);
    }

    /**
     * 执行上传图片的方法1
     */
    public void doUpLoadPic(String filePath, int Type) {

        RequestParams params = Resquest
                .loadPic(Type, filePath);

        httpUtils.send(HttpRequest.HttpMethod.POST, Resquest.PIC_ADDRESS, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException e, String msg) {

                        ToastUtil.show(context, "图片上传失败, 请检查网络并重新选择");

                        dialog.cancel();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        String rule = responseInfo.result;

                        System.out.println(rule + " 图片上传返回结果");


                        dialog.cancel();

                        JsonParser jsonParser = new JsonParser();
                        JsonElement jsonElement = jsonParser.parse(rule); // 将json字符串转换成JsonElement
                        JsonObject jsonObject = jsonElement.getAsJsonObject();

                        String Code = jsonObject.get("Code").getAsString();
                        int Type = jsonObject.get("Type").getAsInt();
                        String httpImg = jsonObject.get("httpImg").getAsString();
                        String srcImg = jsonObject.get("srcImg").getAsString();
                        String data = jsonObject.get("data").getAsString();

                        if (Code.equals("0")) {

                            if (Type == 2) {

                                Head_portrait = srcImg;
                            }
                            if (Type == 1) {

                                MapUserPic_Bean bean = new MapUserPic_Bean();
                                bean.setHttpImg(httpImg);
                                bean.setPicSrc(srcImg);

                                imgList.add(bean);
                                adapter.notifyDataSetChanged();

                            }
                        }

                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                        super.onLoading(total, current, isUploading);

                    }

                    @Override
                    public void onStart() {
                        super.onStart();

                    }
                });
    }


    private String putPicSrc() {

        StringBuilder basestring = new StringBuilder();

        for (int i = 0; i < imgList.size(); i++) {

            String fileCode = imgList.get(i).getPicSrc();

            if (fileCode != null) {

                basestring.append(fileCode + ",");

            }

        }

        return ToolsUtils.urlEnodeUTF8(basestring.toString());

    }

    /**
     * �微信登录�
     */
    private void WXLogin() {

        SendAuth.Req req = new SendAuth.Req();

        // 授权读取用户信息
        req.scope = "snsapi_userinfo";
        // 自定义信息
        req.state = "wechat_sdk_demo";
        // 向微信发送请求
        WXapi.sendReq(req);

    }

    public void judgeWXcode() {

        if (AndroidApplication.getResp() != null) {

            if (AndroidApplication.getResp().getType() == ConstantsAPI.COMMAND_SENDAUTH) {

                String weixinCode = ((SendAuth.Resp) AndroidApplication.getResp()).code;

                if (!weixinCode.equals("")) {
                    get_access_token = getCodeRequest(weixinCode);
                    Thread thread = new Thread(downloadRun);
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        } else {
            // 清除保存信息
//            SharedPreferences preference = getSharedPreferences(
//                    SPUtils.SP_USERINFO_NAME,
//                    getActivity().MODE_WORLD_WRITEABLE);
//            SharedPreferences.Editor editor = preference.edit();
//            editor.clear().commit();
        }
    }

    /**
     * @param code ��Ȩʱ��΢�Żص�����
     * @return URL
     */
    public static String getCodeRequest(String code) {
        String result = null;
        result = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + urlEnodeUTF8(ContantsUtils.WX_APP_ID) + "&secret="
                + urlEnodeUTF8(ContantsUtils.WX_AppSecret) + "&code="
                + urlEnodeUTF8(code) + "&grant_type=authorization_code";
        return result;
    }

    /**
     * @param access_token ��ȡaccess_tokenʱ����
     * @param openid       ��ȡaccess_tokenʱ����
     * @return URL
     */
    public static String getUserInfo(String access_token, String openid) {
        String result = null;
        result = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + urlEnodeUTF8(access_token) + "&openid="
                + urlEnodeUTF8(openid);
        return result;
    }

    public static String urlEnodeUTF8(String str) {
        String result = str;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Runnable downloadRun = new Runnable() {

        @Override
        public void run() {
            WXGetAccessToken();
        }
    };

    private void WXGetAccessToken() {

        httpUtils.send(HttpRequest.HttpMethod.POST, get_access_token,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException e, String msg) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String rule = responseInfo.result;

                        if (rule != null) {
                            JsonParser jsonParser = new JsonParser();
                            JsonElement jsonElement = jsonParser.parse(rule); // 将json字符串转换成JsonElement
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            if (jsonObject != null) {
                                String access_token = jsonObject.get("access_token").getAsString(); //(String) jsonObject.get("access_token");
                                String openid = jsonObject.get("openid").getAsString(); //(String) jsonObject.get("openid");
                                String get_user_info_url = getUserInfo(
                                        access_token, openid);
                                WXGetUserInfo(get_user_info_url);
                            }

                        }
                    }
                });

    }

    /**
     * @param get_user_info_url ����URL
     */
    private void WXGetUserInfo(String get_user_info_url) {

        httpUtils.send(HttpRequest.HttpMethod.POST, get_user_info_url,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException e, String msg) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        String rule = responseInfo.result;

                        JsonParser jsonParser = new JsonParser();
                        JsonElement jsonElement = jsonParser.parse(rule); // 将json字符串转换成JsonElement
                        JsonObject jsonObject = jsonElement.getAsJsonObject();

                        String unionid = jsonObject.get("unionid").getAsString(); //(String) jsonObject.get("unionid");
                        openid = jsonObject.get("openid").getAsString(); //(String) json1.get("openid");
                        String nickname = jsonObject.get("nickname").getAsString(); //(String) json1.get("nickname");
                        headimgurl =
                                jsonObject.get("headimgurl").getAsString(); // (String) json1.get("headimgurl");

                        Head_portrait = jsonObject.get("headimgurl").getAsString(); // (String) json1.get("headimgurl");;

                        ImageLoader.getInstance().displayImage(headimgurl, idCoachmsgImg);

                        AndroidApplication.setResp(null);

                    }
                });

    }

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

                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(14.0f);
            }

            if (mLocClient.isStarted()) {
                // 获得位置之后停止定位
                mLocClient.stop();
            }

        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        Type type = new TypeToken<List<CoachShows_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<CoachShows_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        setCoachMsg(mList.get(0));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(context, "网络异常");
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
                            int PicCount = jsonObject.getInt("PicCount");

                            Resquest.getMapUserPic(handler3, ID, 1, PicCount);

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
                    dialog.cancel();
                    ToastUtil.show(context, "网络异常");
                    break;
            }

            return true;
        }
    });

    Handler handler3 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        Type type = new TypeToken<List<MapUserPic_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<MapUserPic_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        imgList.clear();
                        imgList.addAll(mList);
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialog.cancel();
                    break;
                case Resquest.FAILED_MSG:
                    dialog.cancel();
                    ToastUtil.show(context, "网络异常");
                    break;
            }

            return true;
        }
    });

    private void setCoachMsg(CoachShows_Bean bean) {

        openid = bean.getOpenid();

        Head_portrait = bean.getHead_portrait();

        if (!bean.getHead_portrait().equals("")) {


            if (bean.getHead_portrait().indexOf("http://wx.qlogo.cn/mmopen") != -1) {

                ImageLoader.getInstance().displayImage(bean.getHead_portrait(), idCoachmsgImg);

            } else
                ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getHead_portrait(), idCoachmsgImg);
        }

        idCoachEditName.setText(bean.getName());
        idCoachEditPhone.setText(bean.getTelephone());
        idCoachEditChang.setText(bean.getSite_address());
        idCoachEditAddress.setText(bean.getCurrentAddress());
        idCoachEditCity.setText(bean.getCity());
        idCoachEditSchool.setText(bean.getDriver_school());
        idCoachEditCom.setText(bean.getCompany());
        idEditInfo.setText(bean.getIntroduction());
    }

}
