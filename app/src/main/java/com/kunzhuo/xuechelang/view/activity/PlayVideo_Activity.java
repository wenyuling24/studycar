package com.kunzhuo.xuechelang.view.activity;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.http.Logg;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.Comment_Bean;
import com.kunzhuo.xuechelang.model.PlayVideo_Bean;
import com.kunzhuo.xuechelang.model.Video_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.LogUtils;
import com.kunzhuo.xuechelang.utils.NormalUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.utils.bitmap.BitmapUtil;
import com.kunzhuo.xuechelang.video.VideoViewActivity;
import com.kunzhuo.xuechelang.view.adapter.Comment_Adapter;
import com.kunzhuo.xuechelang.view.adapter.VideoSecond_Adapter1;
import com.kunzhuo.xuechelang.widget.MyGridView;
import com.kunzhuo.xuechelang.widget.MyListView;
import com.kunzhuo.xuechelang.widget.PullDownElasticImp;
import com.kunzhuo.xuechelang.widget.PullDownScrollView;
import com.kunzhuo.xuechelang.widget.emouse.EmoteInputView;
import com.kunzhuo.xuechelang.widget.emouse.EmoticonsEditText;
import com.kunzhuo.xuechelang.wxapi.ContantsUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by Administrator on 2016/9/26 0026.
 */
public class PlayVideo_Activity extends BaseActivity implements PullDownScrollView.RefreshListener {

    private static final String tag = "PlayVideo_Activity.class";

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.surface_view)
    VideoView mVideoView;
    @BindView(R.id.id_playZanImg)
    ImageView idPlayZanImg;
    @BindView(R.id.id_playZanTxt)
    TextView idPlayZanTxt;
    @BindView(R.id.id_playZanlayout)
    RelativeLayout idPlayZanlayout;
    @BindView(R.id.id_playNumTxt)
    TextView idPlayNumTxt;
    @BindView(R.id.play_gridVideoView)
    MyGridView playGridVideoView;
    @BindView(R.id.refresh_root)
    PullDownScrollView refreshRoot;
    @BindView(R.id.id_playVideoname)
    TextView idPlayVideoname;
    @BindView(R.id.id_shoucangLayout)
    RelativeLayout idShoucangLayout;
    @BindView(R.id.id_shoucangImg)
    ImageView idShoucangImg;
    @BindView(R.id.id_shoucangTxt)
    TextView idShoucangTxt;
    @BindView(R.id.play_videoCommentlistview)
    MyListView playVideoCommentlistview;
    @BindView(R.id.id_comPublish)
    ImageView idComPublish;
    @BindView(R.id.id_seeAllCommentLayout)
    RelativeLayout idSeeAllCommentLayout;
    @BindView(R.id.surface_img)
    ImageView surfaceImg;
    @BindView(R.id.surface_PlayFrame)
    FrameLayout surfacePlayFrame;
    @BindView(R.id.surface_PlayFrameImg)
    ImageView surfacePlayFrameImg;
    @BindView(R.id.loading_image)
    ImageView mLoadingImg;
    @BindView(R.id.loading_LinearLayout)
    LinearLayout mLoadingLayout;
    @BindView(R.id.surface_Title)
    FrameLayout surfaceTitle;
    @BindView(R.id.id_whilteLayout)
    View idWhilteLayout;
    @BindView(R.id.id_WrapLayout)
    RelativeLayout idWrapLayout;
    @BindView(R.id.emo_btn)
    ImageView emoBtn;
    @BindView(R.id.emo_btn2)
    ImageView emoBtn2;
    @BindView(R.id.id_comEdit)
    EmoticonsEditText mEetTextDitorEditer;
    @BindView(R.id.chat_emoview)
    EmoteInputView mInputView;
    @BindView(R.id.id_rightLayoutShare)
    RelativeLayout idRightLayoutShare;

    private Context context;
    private String Uid = "";
    private String Vid = "";
    private int Type = 1;
    private SharedPreferences sharedTime;
    private ProgressDialog dialog;
    private boolean zanflag = false; // 是否点赞标志
    private boolean cangflag = false; // 是否收藏标志
    private PlayVideo_Bean playBean;
    private VideoSecond_Adapter1 video_Adapter1;
    private Comment_Adapter com_Adapter;
    private int CommentCount = 0;
    private ObjectAnimator mOjectAnimator;
    /**
     * 当前进度
     */
    private Long currentPosition = (long) 0;

    /**
     * setting
     */
    private boolean needResume;

    private String trueAddress = "";
    private String secondAddress = "";

    // 微信用
    private IWXAPI wxApi;


    @Override
    protected int setLayoutId() {
        return R.layout.play_layout;
    }

    @Override
    protected void setDefaultViews() {
        initViews();
        regsterBoast();
        setWeixin();
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void updateViews() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!LibsChecker.checkVitamioLibs(this))
            return;

    }


    private void initViews() {
        context = this;
        dialog = new ProgressDialog(context);
        dialog.setMessage("正在加载, 请稍后....");

        dialog.show();

        itemTitlemsg.setText("视频详情");
        idRightLayoutShare.setVisibility(View.VISIBLE);

        Uid = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.USER_ID);
        Vid = getIntent().getStringExtra("Vid");
        Type = getIntent().getIntExtra("Type", 1);

        // 每次打开， 从文件拿到跟新时间
        sharedTime = getSharedPreferences("updateTimeInfo",
                Context.MODE_WORLD_WRITEABLE);
        refreshRoot.setRefreshListener(this);
        refreshRoot.setPullDownElastic(new PullDownElasticImp(
                context));
        String lastUpdateTimeFromFile = NormalUtils
                .getLastUpdateTime(sharedTime.getString("blog_update_time",
                        "首次更新"));
        refreshRoot.finishRefresh(lastUpdateTimeFromFile);

        video_Adapter1 = new VideoSecond_Adapter1(context, null);
        playGridVideoView.setAdapter(video_Adapter1);

        com_Adapter = new Comment_Adapter(context, null);
        playVideoCommentlistview.setAdapter(com_Adapter);

        playGridVideoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Video_Bean bean = (Video_Bean) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(context, PlayVideo_Activity.class);
                intent.putExtra("Vid", bean.getVid());
                intent.putExtra("Type", Type);
                startActivity(intent);
                finish();
            }
        });

        mInputView.setEditText(mEetTextDitorEditer);
    }

    private void httpRepost() {

        Resquest.setVide_Edit_Playnumber(handler2, Vid);
        Resquest.getVideoList(handler7, Type, 5, 3);
        Resquest.getCommentList(handler8, Vid, 1, 5);

    }

    @OnClick({R.id.item_back, R.id.id_playZanlayout,
            R.id.id_shoucangLayout, R.id.id_comPublish,
            R.id.id_seeAllCommentLayout, R.id.surface_PlayFrameImg,
            R.id.emo_btn, R.id.emo_btn2, R.id.id_rightLayoutShare})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.id_playZanlayout: //点赞

                if (zanflag) { // 已经点赞
                    Resquest.deleteThingDelete(handler3, playBean.getZid(), Vid);
                } else {
                    Resquest.zanThingAdd(handler4, Vid, Uid);
                }

                break;
            case R.id.id_shoucangLayout: // 收藏

                if (cangflag) {

                    Resquest.deleteCollectionDelete(handler5, playBean.getCid());

                } else {

                    if (Uid.equals("")) {
                        ToastUtil.show(context, " 无法收藏, 您还未登录");
                    } else
                        Resquest.collectCollectionAdd(handler6, Vid, Uid);
                }

                break;
            case R.id.id_comPublish:

                if (Uid.equals("")) {

                    ToastUtil.show(context, "无法评论, 您还未登录");

                } else {
                    String content = mEetTextDitorEditer.getText().toString();

                    Resquest.addCommentAdd(handler9, Vid, Uid, content);
                }

                break;
            case R.id.id_seeAllCommentLayout:

                Resquest.getCommentList(handler8, Vid, 1, CommentCount);

                break;
            case R.id.surface_PlayFrameImg:

                // 开播按钮
                preparePlayVideo();

                break;
            case R.id.emo_btn: // 显示表情
                emoBtn2.setVisibility(View.VISIBLE);
                emoBtn.setVisibility(View.GONE);
                mEetTextDitorEditer.requestFocus();
                hideKeyBoard();
                mInputView.setVisibility(View.VISIBLE);

                break;
            case R.id.emo_btn2: // 显示输入法

                emoBtn2.setVisibility(View.GONE);
                emoBtn.setVisibility(View.VISIBLE);

                if (mInputView.isShown()) {
                    mInputView.setVisibility(View.GONE);
                    showKeyBoard();
                } else {
                    showKeyBoard();
                }

                break;
            case R.id.id_rightLayoutShare:

                String url = "http://xueche555.com/Video/VideoShow.aspx?t=" + playBean.getVid();
                String title1 = "学车郎～学车帮您忙!";
                String title2 = playBean.getVtitle();
                String imageUrl = playBean.getHttpImg() + playBean.getVid();

                showSharedPop(url, title1, title2, imageUrl);

                break;
        }
    }

    // 监听返回键
    @Override
    public void onBackPressed() {
        if (mInputView.isShown()) {
            emoBtn2.setVisibility(View.GONE);
            emoBtn.setVisibility(View.VISIBLE);
            mInputView.setVisibility(View.GONE);
        } else if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
            emoBtn2.setVisibility(View.VISIBLE);
            emoBtn.setVisibility(View.GONE);
            hideKeyBoard();
        } else {
            finish();
        }
    }


    @Override
    public void onRefresh(PullDownScrollView view) {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                String curTime = NormalUtils.getCurTime();
                setUpdateTime(curTime);
                String updateTime = NormalUtils.getLastUpdateTime(curTime);
                refreshRoot.finishRefresh("上次刷新时间: " + updateTime);
                httpRepost();
            }
        }, 2000);

    }

    /**
     * 在文件中写入刷新时间
     */
    public void setUpdateTime(String curTime) {
        SharedPreferences.Editor editor = sharedTime.edit();
        editor.putString("blog_update_time", curTime);
        editor.commit();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        if (mVideoView != null) {
            mVideoView.stopPlayback();
            mVideoView = null;
        }
    }


    /**
     * 注册信息1
     */
    public void regsterBoast() {

        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);

    }


    /**
     * 展示是否有网络
     *
     * @author Administrator
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Logg.d(tag, "网络状态已经改变");
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    String name = info.getTypeName();
                    System.out.println("getAcivity当前网络名称：" + name);
                    // doSomething()

                    httpRepost();

                } else {
                    System.out.println("getAcivity没有可用网络");
                    ToastUtil.show(context, "网络异常");

                }
            }
        }
    };


    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {


                            Type type = new TypeToken<List<PlayVideo_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<PlayVideo_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            String Advertisement = jsonObject.getString("Advertisement");

                            setPlayVideo_Bean(mList.get(0), Advertisement);

                        }
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
                            Resquest.getVideoDetails(handler, Vid, Uid);

                        }
                    } catch (JSONException e) {
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

    Handler handler3 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        String Code = jsonObject.getString("Code");

                        if (Code.equals("0")) {
                            // 取消点赞
                            idPlayZanImg.setImageResource(R.drawable.icon_zanbefore);
                            zanflag = false;
                            playBean.setZid("");

                            int Count = jsonObject.getInt("Count");
                            idPlayZanTxt.setText(Count + "");

                        }
                    } catch (JSONException e) {
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

    Handler handler4 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        String Code = jsonObject.getString("Code");

                        if (Code.equals("0")) {
                            // 点赞成功
                            idPlayZanImg.setImageResource(R.drawable.icon_zanafter);
                            zanflag = true;

                            String Msg = jsonObject.getString("Msg");

                            playBean.setZid(Msg);

                            int Count = jsonObject.getInt("Count");
                            idPlayZanTxt.setText(Count + "");

                        }
                    } catch (JSONException e) {
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

    Handler handler5 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        String Code = jsonObject.getString("Code");

                        if (Code.equals("0")) {
                            // 取消收藏
                            idShoucangImg.setImageResource(R.drawable.icon_stargrey);
                            cangflag = false;
                            idShoucangTxt.setText("未收藏");
                            playBean.setCid("");
                        }
                    } catch (JSONException e) {
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

    Handler handler6 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        String Code = jsonObject.getString("Code");

                        if (Code.equals("0")) {
                            // 收藏成功
                            idShoucangImg.setImageResource(R.drawable.icon_statyellow);
                            cangflag = true;
                            idShoucangTxt.setText("已收藏");

                            String Msg = jsonObject.getString("Msg");

                            playBean.setCid(Msg);
                        }
                    } catch (JSONException e) {
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


    Handler handler7 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        Type type = new TypeToken<List<Video_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<Video_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        video_Adapter1.setData(mList);
                        video_Adapter1.notifyDataSetChanged();


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

    Handler handler8 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {

                        CommentCount = jsonObject.getInt("CommentCount");

                        Type type = new TypeToken<List<Comment_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<Comment_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        com_Adapter.setData(mList);
                        com_Adapter.notifyDataSetChanged();


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

    Handler handler9 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        String Code = jsonObject.getString("Code");

                        if (Code.equals("0")) {
                            ToastUtil.show(context, " 评论成功");
                            mEetTextDitorEditer.setText("");

                            Resquest.getCommentList(handler8, Vid, 1, 5);
                        }
                    } catch (JSONException e) {
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

    private void setPlayVideo_Bean(PlayVideo_Bean bean, String Advertisement) throws UnsupportedEncodingException {

        dialog.cancel();

        playBean = bean;

        idPlayVideoname.setText(bean.getVtitle());
        idPlayNumTxt.setText(bean.getPlaynumber() + "");
        idPlayZanTxt.setText(bean.getThing() + "");

        ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getVpid(), surfaceImg);

        if (bean.getZid().equals("0")) {
            zanflag = false;

            idPlayZanImg.setBackgroundResource(R.drawable.icon_zanbefore);

        } else {
            zanflag = true;

            idPlayZanImg.setBackgroundResource(R.drawable.icon_zanafter);
        }

        if (bean.getCid().equals("0")) {
            cangflag = false;
            idShoucangImg.setImageResource(R.drawable.icon_stargrey);
            idShoucangTxt.setText("未收藏");

        } else {
            cangflag = true;
            idShoucangImg.setImageResource(R.drawable.icon_statyellow);
            idShoucangTxt.setText("已收藏");
        }

        String ceshiaddrwss = "http://123.57.41.216:412/";

        String beforeAddredd = URLEncoder.encode(bean.getVsrc(), "UTF-8").toLowerCase();

//        String beforeAddredd = "%e9%87%8d%e5%ba%86%e5%ad%a6%e8%bd%a6%e9%83%8e%2f%e8%b6%a3%e5%91%b3%e5%ad%a6%e8%bd%a6%2f201609026015056022%e6%b8%a3%e7%94%b7%e5%8f%ab%e4%bd%a0%e7%8e%a9%e6%ae%8b%e5%a5%b3%e5%8f%8b%e6%a0%87%e6%b8%85.mp4";

        trueAddress = ceshiaddrwss + beforeAddredd;

        String secondTrueAddress = URLEncoder.encode(Advertisement, "UTF-8").toLowerCase();

        System.out.println(trueAddress + " 真是播放地址1");
        secondAddress = ceshiaddrwss + secondTrueAddress;

        System.out.println(secondAddress + " 真是播放地址2");

        mVideoView.setVisibility(View.VISIBLE);
        mVideoView.setVideoPath(secondAddress);

        MediaController mc = new MediaController(context);
        mc.setVisibility(View.GONE);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        int h = (int) (screenWidth / 1.7);

//        int w = DensityUtil.dip2px(context, screenWidth);
//        int h = DensityUtil.dip2px(context, screenHeight / 3);

//        ToastUtil.show(context, screenWidth + " 宽度 " + h + " 高度");

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(screenWidth, h);
        lp.gravity = Gravity.CENTER;
        idWrapLayout.setLayoutParams(lp);


        mVideoView.setMediaController(mc);
        mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_ORIGIN, 0);
        mVideoView.getHolder().setFixedSize(360, 240);  //设置分辨率
//        mVideoView.requestFocus();

        preparePlayVideo();

        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent appIntent = new Intent(context, VideoViewActivity.class);
                appIntent.putExtra(VideoViewActivity.VIDEO_PATH, trueAddress);
                appIntent.putExtra(VideoViewActivity.VIDEO_PATH2, secondAddress);
                startActivity(appIntent);
                return false;
            }
        });

    }

    @NonNull
    private void startLoadingAnimator() {
        if (mOjectAnimator == null) {
            mOjectAnimator = ObjectAnimator.ofFloat(mLoadingImg, "rotation", 0f, 360f);
        }
        mLoadingLayout.setVisibility(View.VISIBLE);

        mOjectAnimator.setDuration(1000);
        mOjectAnimator.setRepeatCount(-1);
        mOjectAnimator.start();

        surfaceTitle.setVisibility(View.VISIBLE);
    }

    private void stopLoadingAnimator() {
        surfaceTitle.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mOjectAnimator.cancel();
    }


    private void preparePlayVideo() {

        surfacePlayFrame.setVisibility(View.GONE);
        surfaceImg.setVisibility(View.GONE);

        startLoadingAnimator();

//        ToastUtil.show(context, trueAddress + " 播放地址1");

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // TODO Auto-generated method stub
                stopLoadingAnimator();

                idWhilteLayout.setVisibility(View.VISIBLE);

                if (currentPosition > 0) {
                    mVideoView.seekTo(currentPosition);
                } else {
                    mediaPlayer.setPlaybackSpeed(1.0f);
                }
                startPlay();
            }
        });

        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
                switch (arg1) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        //开始缓存，暂停播放
                        LogUtils.i(LogUtils.LOG_TAG, "开始缓存");
                        startLoadingAnimator();
                        if (mVideoView.isPlaying()) {
                            stopPlay();
                            needResume = true;
                        }
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        //缓存完成，继续播放
                        stopLoadingAnimator();
                        if (needResume) startPlay();
                        LogUtils.i(LogUtils.LOG_TAG, "缓存完成");
                        break;
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        //显示 下载速度
                        LogUtils.i("download rate:" + arg2);
                        break;
                }
                return true;
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                ToastUtil.show(context, " 播放结束");
                mVideoView.setVideoPath(trueAddress);
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
//                LogUtils.i(LogUtils.LOG_TAG, "what=" + what);
                return false;
            }
        });
        mVideoView.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {

            }
        });
        mVideoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                LogUtils.i(LogUtils.LOG_TAG, "percent" + percent);
            }
        });

        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH); // 高画质
        // VIDEOQUALITY_LOW
        // 流畅
        // VIDEOQUALITY_MEDIUM
        // 普通
    }

    private void startPlay() {
        mVideoView.start();
    }

    private void stopPlay() {
        mVideoView.pause();
    }


    public void onPause() {
        super.onPause();
        currentPosition = mVideoView.getCurrentPosition();
        mVideoView.pause();

    }

    /**
     * 显示键盘
     */
    protected void showKeyBoard() {
        if (mInputView.isShown()) {
            mInputView.setVisibility(View.GONE);
        }
        mEetTextDitorEditer.requestFocus();
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .showSoftInput(mEetTextDitorEditer, 0);
    }

    /**
     * 隐藏键盘
     */
    protected void hideKeyBoard() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(this.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    PopupWindow popupWindow;

    private void showSharedPop(final String url, final String title1, final String title2, final String imageUrl) {

        View view = LayoutInflater.from(context).inflate(
                R.layout.shared_layout, null);

        popupWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout weiChat = (LinearLayout) view.findViewById(R.id.id_weichat);
        LinearLayout friendsCycle = (LinearLayout) view.findViewById(R.id.id_friendslayout);
        LinearLayout qq = (LinearLayout) view.findViewById(R.id.id_qqlayout);
        LinearLayout sina = (LinearLayout) view.findViewById(R.id.id_sinalayout);

        view.findViewById(R.id.dis_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setTouchable(true);// 可点击
        popupWindow.setOutsideTouchable(true);// 设置外部可点击,点击取消
        popupWindow.setFocusable(true);// 设置可聚焦

        weiChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                wechatShare(0, url, title1, title2, imageUrl);

                popupWindow.dismiss();

            }
        });


        friendsCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                wechatShare(1, url, title1, title2, imageUrl);

                popupWindow.dismiss();

            }
        });


        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // popupWindow.dismiss();
                return true;
            }
        });

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 返回false,事件才能下发
                return false;
            }
        });

        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        ColorDrawable dw = new ColorDrawable(0xb0000000);

        popupWindow.setBackgroundDrawable(dw);

        popupWindow.showAtLocation(itemBack, Gravity.BOTTOM, 0, 0);

    }

    public void setWeixin() {

        // 实例化
        wxApi = WXAPIFactory.createWXAPI(getApplicationContext(),
                ContantsUtils.WX_APP_ID);
        wxApi.registerApp(ContantsUtils.WX_APP_ID);

    }

    /**
     * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
     *
     * @param flag (0:分享到微信好友，1：分享到微信朋友圈)
     */
    public void wechatShare(int flag, String url, String shopName, String title, String imageUrl) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shopName;
        msg.description = title;

        // 这里替换一张自己工程里的图片资源
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_1);
//        // 这里替换一张自己工程里的图片资源
//        Bitmap photo = BitmapUtil.loadRmoteImage(imageUrl);
//        Bitmap bitmap = BitmapUtil.getBitmap(photo, 200, 200);

        msg.setThumbImage(bitmap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession
                : SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }

}
