package com.kunzhuo.xuechelang.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.network.SystemBase;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.utils.bitmap.BitmapUtil;
import com.kunzhuo.xuechelang.widget.MyGridView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/30 0030.
 */
public class UpImage_Activity extends BaseActivity {


    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.noScrollgridview)
    MyGridView noScrollgridview;
    @BindView(R.id.icon_msg)

    ImageView iconView;

    ProgressDialog dialog;
    Context context;

    private HttpUtils httpUtils;
    private String[] items = new String[]{"选择本地图片", "拍照"};

    /* 头像名称 */
    private static final String IMAGE_FILE_NAME = "tempUserHead.jpg";

    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    @Override
    protected int setLayoutId() {
        return R.layout.upimage_layout;
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
        itemTitlemsg.setText("上传图片测试");

        httpUtils = new HttpUtils(30000);
        dialog = new ProgressDialog(context);

    }


    @OnClick({R.id.item_back, R.id.icon_msg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.icon_msg:
                showDialog();
                break;
        }
    }

    /**
     * 显示选择对话框
     */
    private void showDialog() {

        new AlertDialog.Builder(this)
                .setTitle("设置头像")
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
                        dialog.setMessage("正在上传头像,请稍后...");
                        dialog.show();

                        getImageToView(imageUri);
                    }
                    break;
                case CAMERA_REQUEST_CODE:
                    startPhotoZoom(imageUri);
                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {

                        dialog.setMessage("正在上传头像,请稍后...");
                        dialog.show();

                        getImageToView(imageUri);
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
    private void getImageToView(Uri data) {

        String path = BitmapUtil.getRealFilePath(this, data);

        System.out.println(path + " 图片地址1");

        doUpLoadPic(path);
    }

    /**
     * 执行上传图片的方法1
     */
    public void doUpLoadPic(String filePath) {

        RequestParams params = Resquest
                .loadPic(1, filePath);

        httpUtils.send(HttpRequest.HttpMethod.POST, Resquest.PIC_ADDRESS, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException e, String msg) {

                        ToastUtil.show(context, "图片上传失败, 请检查网络并重新选择");

                        dialog.cancel();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        ToastUtil.show(context, "图片上传成功！");

                        String rule = responseInfo.result;

                        System.out.println(rule + " 图片上传返回结果1");

                        dialog.cancel();

                        Gson gson = new Gson();
                        JsonParser jsonParser = new JsonParser();
                        JsonElement jsonElement = jsonParser.parse(rule); // 将json字符串转换成JsonElement
                        JsonObject jsonObject = jsonElement.getAsJsonObject();

                        String Code = jsonObject.get("Code").getAsString();

                        if (Code.equals("0")) {
                            System.out.println(rule + " 图片上传返回结果2");
                            ToastUtil.show(context, rule + " 图片上传返回结果");
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


}
