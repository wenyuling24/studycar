package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.CoachShows_Bean;
import com.kunzhuo.xuechelang.model.MapCommentList_Bean;
import com.kunzhuo.xuechelang.model.MapUserPic_Bean;
import com.kunzhuo.xuechelang.model.NoviceList_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.NormalUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.adapter.MapComment_Adapter;
import com.kunzhuo.xuechelang.view.adapter.MapUserPic_Adapter;
import com.kunzhuo.xuechelang.view.adapter.MapUserPic_Adapter_2;
import com.kunzhuo.xuechelang.view.adapter.Novice_Adapter;
import com.kunzhuo.xuechelang.widget.CustomImageView;
import com.kunzhuo.xuechelang.widget.MyGridView;
import com.kunzhuo.xuechelang.widget.MyListView;
import com.kunzhuo.xuechelang.wxapi.ContantsUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/12 0012.
 */
public class CoachDetails_Activity extends BaseActivity {


    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_coachImg)
    CustomImageView idCoachImg;
    @BindView(R.id.id_coachName)
    TextView idCoachName;
    @BindView(R.id.id_coachPhone)
    TextView idCoachPhone;
    @BindView(R.id.id_coachTo)
    ImageView idCoachTo;
    @BindView(R.id.id_coachYear)
    TextView idCoachYear;
    @BindView(R.id.id_coachSx)
    LinearLayout idCoachSx;
    @BindView(R.id.id_coachSp)
    LinearLayout idCoachSp;
    @BindView(R.id.id_coachSxlx)
    LinearLayout idCoachSxlx;
    @BindView(R.id.id_coachZjlx)
    LinearLayout idCoachZjlx;
    @BindView(R.id.id_coachXqzb)
    LinearLayout idCoachXqzb;
    @BindView(R.id.id_coachYyks)
    LinearLayout idCoachYyks;
    @BindView(R.id.id_coachJkjf)
    LinearLayout idCoachJkjf;
    @BindView(R.id.id_coachWzcx)
    LinearLayout idCoachWzcx;
    @BindView(R.id.id_coachgridView1)
    MyGridView idCoachgridView1;
    @BindView(R.id.id_coachgridView2)
    MyGridView idCoachgridView2;
    @BindView(R.id.id_coachType)
    TextView idCoachType;
    @BindView(R.id.id_coachCall)
    ImageView idCoachCall;
    @BindView(R.id.id_CoachNovicelistview)
    MyListView idCoachNovicelistview;
    @BindView(R.id.id_CoachMsg)
    TextView idCoachMsg;
    @BindView(R.id.play_CoachCommentlist)
    MyListView playCoachCommentlist;
    @BindView(R.id.id_orangeLeftTitle)
    Button idOrangeLeftTitle;
    @BindView(R.id.id_orangeRightTitle)
    Button idOrangeRightTitle;
    @BindView(R.id.id_ComSeeAllbtn)
    Button idComSeeAllbtn;
    @BindView(R.id.id_ComToBtn)
    Button idComToBtn;
    @BindView(R.id.comm_btnlayout)
    LinearLayout commBtnlayout;
    @BindView(R.id.id_coachNoImg)
    TextView idCoachNoImg;
    @BindView(R.id.id_coachMoreImg)
    Button idCoachMoreImg;
    @BindView(R.id.id_Xueqian)
    TextView idXueqian;
    @BindView(R.id.id_Yuyue)
    TextView idYuyue;
    @BindView(R.id.id_Jiakao)
    TextView idJiakao;
    @BindView(R.id.id_Weizhang)
    TextView idWeizhang;
    @BindView(R.id.id_rightLayoutShare)
    RelativeLayout idRightLayoutShare;


    private Context context;
    private CoachShows_Bean coach_Bean;
    private String ID;
    private String Name;
    private String Province;
    private ProgressDialog dialog;
    private MapUserPic_Adapter adapter;
    private MapUserPic_Adapter_2 adapter2;
    private Novice_Adapter novice_Adapter;
    private MapComment_Adapter comment_Adapter;
    private int PicCount = 0; // 相片总数
    private int CommentCount = 0; //总评论条数
    private int InfoCount = 0; // 总文章条数
    // 微信用
    private IWXAPI wxApi;


    @Override
    protected int setLayoutId() {
        return R.layout.coachmsg_layout;
    }

    @Override
    protected void setDefaultViews() {
        initViews();
        setWeixin();
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void updateViews() {

    }

    private void initViews() {

        context = this;
        dialog = new ProgressDialog(context);
        ID = getIntent().getStringExtra("ID");
        Name = getIntent().getStringExtra("Name");

        itemTitlemsg.setText(Name + "的微网站");
        idRightLayoutShare.setVisibility(View.VISIBLE);

        adapter = new MapUserPic_Adapter(context, null);
        idCoachgridView1.setAdapter(adapter);

        idCoachgridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(context, GalleryActivity.class);
                intent.putExtra("ImgType", 0);
                intent.putExtra("ID", ID);
                intent.putExtra("PicCount", PicCount);
                intent.putExtra("position", i);
                startActivity(intent);
            }
        });

        adapter2 = new MapUserPic_Adapter_2(context, null);
        idCoachgridView2.setAdapter(adapter2);

        idCoachgridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(context, GalleryActivity.class);
                intent.putExtra("ImgType", 0);
                intent.putExtra("ID", ID);
                intent.putExtra("PicCount", PicCount);
                intent.putExtra("position", i);
                startActivity(intent);
            }
        });


        novice_Adapter = new Novice_Adapter(context, null);
        idCoachNovicelistview.setAdapter(novice_Adapter);

        comment_Adapter = new MapComment_Adapter(context, null);
        playCoachCommentlist.setAdapter(comment_Adapter);

        idCoachNovicelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NoviceList_Bean bean = (NoviceList_Bean) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(context, WebView_Activity.class);
                intent.putExtra("Title", bean.getItitle());
                intent.putExtra("IID", bean.getIID());
                startActivity(intent);
            }
        });

        dialog.setMessage("正在加载, 请稍后...");
        dialog.show();

        Resquest.getMapUser(handler, ID);

        Resquest.getMapUserPic(handler2, ID, 1, 1);
        Resquest.getMapUserPic(handler3, ID, 1, 5);

    }

    @Override
    protected void onResume() {
        super.onResume();
        httpRepost();
    }

    private void httpRepost() {

        Resquest.getNoviceList(handler4, 1, NormalUtils.setRandom(8), 6);
        Resquest.getMapCommentList(handler5, ID, 1, 5);
    }

    @OnClick({R.id.item_back, R.id.id_coachTo,
            R.id.id_coachSx, R.id.id_coachSp,
            R.id.id_coachSxlx, R.id.id_coachZjlx,
            R.id.id_coachXqzb, R.id.id_coachYyks,
            R.id.id_coachJkjf, R.id.id_coachWzcx,
            R.id.id_coachCall, R.id.id_orangeLeftTitle,
            R.id.id_orangeRightTitle, R.id.id_ComSeeAllbtn,
            R.id.id_ComToBtn, R.id.id_coachMoreImg,
            R.id.id_rightLayoutShare})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.id_coachTo:
                Intent intent = new Intent(context, CoachMap_Activity.class);
                intent.putExtra("Name", coach_Bean.getName());
                intent.putExtra("longitude", coach_Bean.getX());
                intent.putExtra("latitude", coach_Bean.getY());
                startActivity(intent);

                break;
            case R.id.id_coachSx: // 教练视频

                if (coach_Bean.getUid().equals("")) {
                    Intent intenSx = new Intent(context, CoachList_Activity.class);
                    startActivity(intenSx);

                } else {
                    Intent intenSx = new Intent(context, CoachVideo_Activity.class);
                    intenSx.putExtra("Uid", coach_Bean.getUid());
                    startActivity(intenSx);
                }

                break;
            case R.id.id_coachSp:

                Intent intentSx = new Intent(context, TextStudy_Activity.class);
                intentSx.putExtra("DistriCoach", coach_Bean.getUid());
                startActivity(intentSx);

                break;
            case R.id.id_coachSxlx: // 顺序联系

                Intent intent1 = new Intent(context, ProblemMain_Activity.class);
                intent1.putExtra("ChapterID", "");
                intent1.putExtra("RecType", 1);
                intent1.putExtra("Number", 1);
                intent1.putExtra("T_Subject", "");
                intent1.putExtra("ZJ_Count", 10000);
                intent1.putExtra("Type", 1);
                startActivity(intent1);

                break;
            case R.id.id_coachZjlx: // 章节信息

                Intent intent3 = new Intent(context, QuestionBank_Activity.class);
                intent3.putExtra("ChapterListType", 1);
                startActivity(intent3);

                break;
            case R.id.id_coachXqzb: // 学前准备

                if (Province.equals("四川省")) {

                    Intent intentXq = new Intent(context, WebView_Activity.class);
                    intentXq.putExtra("Title", "约考查询");
                    intentXq.putExtra("Url", "http://www.scjj.gov.cn:8635/");
                    startActivity(intentXq);

                }
                if (Province.equals("重庆市")) {

                }

                break;
            case R.id.id_coachYyks: // 预约考试


                if (Province.equals("四川省")) {

                    Intent intentYy = new Intent(context, WebView_Activity.class);
                    intentYy.putExtra("Title", "预约考试");
                    intentYy.putExtra("Url", "https://sc.122.gov.cn");
                    startActivity(intentYy);
                }
                if (Province.equals("重庆市")) {

                    Intent intentYy = new Intent(context, WebView_Activity.class);
                    intentYy.putExtra("Title", "预约考试");
                    intentYy.putExtra("Url", "https://cq.122.gov.cn/m/login");
                    startActivity(intentYy);

                }

                break;
            case R.id.id_coachJkjf: // 驾考缴费

                if (Province.equals("四川省")) {

                    Intent intentJk = new Intent(context, WebView_Activity.class);
                    intentJk.putExtra("Title", "网上车管所");
                    intentJk.putExtra("Url", "http://www.cdjg.gov.cn/");
                    startActivity(intentJk);
                }
                if (Province.equals("重庆市")) {

                    Intent intentJk = new Intent(context, WebView_Activity.class);
                    intentJk.putExtra("Title", "驾考缴费");
                    intentJk.putExtra("Url", " http://cqjj.ggjfw.com:9688/Veh/");
                    startActivity(intentJk);

                }

                break;
            case R.id.id_coachWzcx: // 违章查询

                if (Province.equals("四川省")) {

                    Intent intentWz = new Intent(context, WebView_Activity.class);
                    intentWz.putExtra("Title", "违章查询");
                    intentWz.putExtra("Url", "http://028.m.weizhangwang.com/");
                    startActivity(intentWz);

                }
                if (Province.equals("重庆市")) {

                    Intent intentWz = new Intent(context, WebView_Activity.class);
                    intentWz.putExtra("Title", "违章查询");
                    intentWz.putExtra("Url", "http://023.m.weizhangwang.com/");
                    startActivity(intentWz);

                }


                break;
            case R.id.id_coachCall:

                Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + coach_Bean.getTelephone()));
                context.startActivity(intentCall);

                break;
            case R.id.id_orangeLeftTitle:
                idCoachMsg.setVisibility(View.VISIBLE);
                playCoachCommentlist.setVisibility(View.GONE);
                commBtnlayout.setVisibility(View.GONE);

                if (coach_Bean.getIntroduction().equals("")) {
                    idCoachMsg.setText("暂无简介");
                } else {
                    idCoachMsg.setText(coach_Bean.getIntroduction());
                }

                break;
            case R.id.id_orangeRightTitle:

                commBtnlayout.setVisibility(View.VISIBLE);

                if (comment_Adapter.getCount() == 0) {
                    idCoachMsg.setVisibility(View.VISIBLE);
                    idCoachMsg.setText("暂无评论");
                    playCoachCommentlist.setVisibility(View.GONE);

                } else {
                    idCoachMsg.setVisibility(View.GONE);
                    playCoachCommentlist.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.id_ComSeeAllbtn:
                Intent intentComment = new Intent(context, ShowCommentAll_Activity.class);
                intentComment.putExtra("ID", ID);
                intentComment.putExtra("TotalID", coach_Bean.getID());
                intentComment.putExtra("CommentCount", CommentCount);
                intentComment.putExtra("Name", Name);
                startActivity(intentComment);

                break;
            case R.id.id_ComToBtn: // 去评论
                String openid = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.OPEN_ID);

                if (!openid.equals("")) {

                    Intent intent2 = new Intent(context, CoachComment_Activity.class);
                    intent2.putExtra("TotalID", coach_Bean.getID());
                    startActivity(intent2);


                } else {
                    ToastUtil.show(context, "您还未登录,无法发表评论");
                }

                break;
            case R.id.id_coachMoreImg:
                Intent intentImg = new Intent(context, ShowCoachAllImg_Activity.class);
                intentImg.putExtra("ID", ID);
                intentImg.putExtra("PicCount", PicCount);
                intentImg.putExtra("Name", Name);
                startActivity(intentImg);
                break;
            case R.id.id_rightLayoutShare:

                String url = "http://xueche555.com/Map/VCard.aspx?t=" + coach_Bean.getID();
                String title1 = "学车郎～学车帮您忙!";
                String title2 = coach_Bean.getName() + "的微网站";

                if (!coach_Bean.getHead_portrait().equals("")) {

                    if (coach_Bean.getHead_portrait().indexOf("http://wx.qlogo.cn/mmopen") != -1) {

                        showSharedPop(url, title1, title2, coach_Bean.getHead_portrait());

                    } else {
                        showSharedPop(url, title1, title2, coach_Bean.getHttpImg() + coach_Bean.getHead_portrait());
                    }


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
                            PicCount = jsonObject.getInt("PicCount");

                            Type type = new TypeToken<List<MapUserPic_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<MapUserPic_Bean> mList = mapper.transformCollection(jsonObject, "List");
                            adapter.setData(mList);
                            adapter.notifyDataSetChanged();

                            idCoachNoImg.setVisibility(View.GONE);
                            idCoachMoreImg.setVisibility(View.VISIBLE);
                        }
                        if (Code.equals("8")) {
                            idCoachNoImg.setVisibility(View.VISIBLE);
                            idCoachMoreImg.setVisibility(View.GONE);
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
                        mList.remove(0);
                        adapter2.setData(mList);
                        adapter2.notifyDataSetChanged();

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

    Handler handler4 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {

                        InfoCount = jsonObject.getInt("InfoCount");

                        Type type = new TypeToken<List<NoviceList_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<NoviceList_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        novice_Adapter.setData(mList);
                        novice_Adapter.notifyDataSetChanged();

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


    Handler handler5 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        CommentCount = jsonObject.getInt("CommCount");
                        Type type = new TypeToken<List<MapCommentList_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<MapCommentList_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        comment_Adapter.setData(mList);
                        comment_Adapter.notifyDataSetChanged();

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

    private void setCoachMsg(CoachShows_Bean bean) {

        coach_Bean = bean;

        if (!bean.getHead_portrait().equals("")) {


            if (bean.getHead_portrait().indexOf("http://wx.qlogo.cn/mmopen") != -1) {


                ImageLoader.getInstance().displayImage(bean.getHead_portrait(), idCoachImg);

            } else
                ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getHead_portrait(), idCoachImg);
        }

        idCoachName.setText(bean.getName());
        idCoachPhone.setText(bean.getTelephone());
        idCoachType.setText(bean.getCoachType());

        if (bean.getSeniority().equals("")) {

            idCoachYear.setVisibility(View.INVISIBLE);

        } else {
            idCoachYear.setText("教龄: " + bean.getSeniority() + "年");
        }

        if (bean.getIntroduction().equals("")) {
            idCoachMsg.setText("暂无简介");
        } else {
            idCoachMsg.setText(bean.getIntroduction());
        }

        idCoachMsg.setVisibility(View.VISIBLE);
        playCoachCommentlist.setVisibility(View.GONE);
        commBtnlayout.setVisibility(View.GONE);

        Province = bean.getProvince();

        if (Province.equals("四川省")) {

            idXueqian.setText("约考查询");
            idYuyue.setText("预约考试");
            idJiakao.setText("网上车管所");


        }
        if (Province.equals("重庆市")) {

            idXueqian.setText("学前准备");
            idYuyue.setText("预约考试");
            idJiakao.setText("驾考缴费");

        }

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
