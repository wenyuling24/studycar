package com.kunzhuo.xuechelang.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.CoachSingle_Bean;
import com.kunzhuo.xuechelang.model.Video_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.adapter.VideoSecond_Adapter2;
import com.kunzhuo.xuechelang.widget.CustomImageView;
import com.kunzhuo.xuechelang.widget.MyListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/24 0024.
 */
public class CoachVideo_Activity extends BaseActivity {


    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_coachvideolistview)
    MyListView idCoachvideolistview;
    @BindView(R.id.id_coachImg)
    CustomImageView idCoachImg;
    @BindView(R.id.id_coachName)
    TextView idCoachName;
    @BindView(R.id.id_coachPhone)
    TextView idCoachPhone;
    @BindView(R.id.id_coachCall)
    ImageView idCoachCall;
    @BindView(R.id.id_coachYear)
    TextView idCoachYear;
    @BindView(R.id.id_coachArea)
    TextView idCoachArea;
    @BindView(R.id.id_coachvideotxt)
    TextView idCoachvideotxt;
    @BindView(R.id.id_MoreCoachBtn)
    RelativeLayout idMoreCoachBtn;

    private Context context;
    private VideoSecond_Adapter2 adapter;
    private String Uid;

    @Override
    protected int setLayoutId() {
        return R.layout.coachvideo_layout;
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
        itemTitlemsg.setText("教练详情");

        Uid = getIntent().getStringExtra("Uid");

        adapter = new VideoSecond_Adapter2(context, null);
        idCoachvideolistview.setAdapter(adapter);

        idCoachvideolistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Video_Bean bean = (Video_Bean) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(context, PlayVideo_Activity.class);
                intent.putExtra("Vid", bean.getVid());
                intent.putExtra("Type", 2);
                startActivity(intent);
            }
        });

        Resquest.getCoachSingle(handler, Uid);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Resquest.getCoachVideoList(handler2, Uid);

    }

    @OnClick({R.id.item_back, R.id.id_MoreCoachBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.id_MoreCoachBtn:
                Intent intent = new Intent(context, CoachList_Activity.class);
                startActivity(intent);
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
                        Type type = new TypeToken<List<CoachSingle_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<CoachSingle_Bean> mList = mapper.transformCollection(jsonObject, "List");

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

                            idCoachvideotxt.setVisibility(View.GONE);
                            idCoachvideolistview.setVisibility(View.VISIBLE);

                            Type type = new TypeToken<List<Video_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<Video_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            adapter.setData(mList);
                            adapter.notifyDataSetChanged();

                        }
                        if (Code.equals("8")) {

                            idCoachvideotxt.setVisibility(View.VISIBLE);
                            idCoachvideolistview.setVisibility(View.GONE);
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

    private void setCoachMsg(final CoachSingle_Bean bean) {

        idCoachName.setText(bean.getUname());
        itemTitlemsg.setText(bean.getUname() + "的视频");

        if (!bean.getPic().equals("")) {

            if (bean.getPic().indexOf("http://wx.qlogo.cn/mmopen") != -1) {

                ImageLoader.getInstance().displayImage(bean.getPic(), idCoachImg);

            } else
                ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getPic(), idCoachImg);
        }


        if (bean.getSeniority().equals("")) {

            idCoachYear.setVisibility(View.INVISIBLE);

        } else {
            idCoachYear.setText("教龄: " + bean.getSeniority() + "年");
        }

        idCoachArea.setText(bean.getPname());
        idCoachPhone.setText(bean.getUaccount());

        idCoachCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + bean.getUaccount()));
                context.startActivity(intentCall);
            }
        });
    }

}
