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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.CoachFile_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.network.SystemBase;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.utils.ToolsUtils;
import com.kunzhuo.xuechelang.utils.bitmap.BitmapUtil;
import com.kunzhuo.xuechelang.utils.eventutils.FirstEvent;
import com.kunzhuo.xuechelang.view.adapter.CoachFile_Adapter;
import com.kunzhuo.xuechelang.widget.MyGridView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/5 0005.
 */
public class UpCoachImgRen_Activity extends BaseActivity {


    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_coachImgView)
    MyGridView idCoachImgView;
    @BindView(R.id.id_coachImgChooseBtn)
    TextView idCoachImgChooseBtn;
    @BindView(R.id.id_coachImgUpBtn)
    TextView idCoachImgUpBtn;
    @BindView(R.id.id_TxtTishi)
    TextView idTxtTishi;


    private Context context;
    private String Uid;
    private CoachFile_Adapter adapter;
    private ProgressDialog dialog;
    private HttpUtils httpUtils;
    private String[] items = new String[]{"选择本地图片", "拍照"};
    private ArrayList<CoachFile_Bean> imgList = new ArrayList<>();
    private int LocationType = 0;

    /* 头像名称 */
    private static final String IMAGE_FILE_NAME = "tempUserHead.jpg";
    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    @Override
    protected int setLayoutId() {
        return R.layout.upcoachimg_layout;
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
        Uid = getIntent().getStringExtra("Uid");

        httpUtils = new HttpUtils(30000);
        dialog = new ProgressDialog(context);

        adapter = new CoachFile_Adapter(context, imgList);
        idCoachImgView.setAdapter(adapter);
        LocationType = getIntent().getIntExtra("LocationType", 0); // 0 上传 1 查看

        if (LocationType == 0) {
            itemTitlemsg.setText("上传教练认证资料");
            idCoachImgChooseBtn.setVisibility(View.VISIBLE);
            idCoachImgUpBtn.setVisibility(View.VISIBLE);
            idTxtTishi.setVisibility(View.VISIBLE);

        }
        if (LocationType == 1) {
            itemTitlemsg.setText("查看教练认证资料");
            idCoachImgChooseBtn.setVisibility(View.GONE);
            idCoachImgUpBtn.setVisibility(View.GONE);
            idTxtTishi.setVisibility(View.GONE);
            dialog.show();
            Resquest.selCoachfile_Sel(handler2, Uid);
        }

        idCoachImgView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(context, GalleryActivity2.class);

                if (LocationType == 1) {
                    intent.putExtra("ImgType", 1);
                }
                if (LocationType == 0) {
                    intent.putExtra("ImgType", 2);

                }

                intent.putExtra("imgList", imgList);
                intent.putExtra("position", i);
                startActivity(intent);
            }
        });

        EventBus.getDefault().register(this);

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

    @OnClick({R.id.item_back, R.id.id_coachImgChooseBtn, R.id.id_coachImgUpBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.id_coachImgChooseBtn:
                showDialog();
                break;
            case R.id.id_coachImgUpBtn:

                if (!putPicSrc().equals("")) {

                    dialog.show();
                    Resquest.addCoachfile_Add(handler1, Uid, putPicSrc());

                } else {
                    ToastUtil.show(context, "请选择图片");
                }


                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imgList.clear();
        imgList = null;

        EventBus.getDefault().unregister(this);//反注册EventBus

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

                            ToastUtil.show(context, "验证信息提交成功,请耐心等待我们的审核");
                            finish();

                        }
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

                        Type type = new TypeToken<List<CoachFile_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<CoachFile_Bean> mList = mapper.transformCollection(jsonObject, "List");

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    if (data != null) {
                        imageUri = data.getData();
//                        startPhotoZoom(imageUri);
                        dialog.setMessage("正在上传图片,请稍后...");
                        dialog.show();
                        getImageToView(imageUri, 3);
                    }
                    break;
                case CAMERA_REQUEST_CODE:
                    startPhotoZoom(imageUri);
                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {

                        dialog.setMessage("正在上传图片,请稍后...");
                        dialog.show();

                        getImageToView(imageUri, 3);
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

                            if (Type == 3) {

                                CoachFile_Bean bean = new CoachFile_Bean();
                                bean.setHttpImg(httpImg);
                                bean.setPic(srcImg);

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

            String fileCode = imgList.get(i).getPic();

            if (fileCode != null) {

                basestring.append(fileCode + ",");

            }

        }

        return ToolsUtils.urlEnodeUTF8(basestring.toString());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
