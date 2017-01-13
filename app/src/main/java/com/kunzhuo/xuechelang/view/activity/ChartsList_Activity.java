package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.ChartsList_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.adapter.ChartsList_Adapter;
import com.kunzhuo.xuechelang.widget.MyListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by waaa on 2016/9/20.
 * 排行榜
 */
public class ChartsList_Activity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_chartsSelfNum)
    TextView idChartsSelfNum;
    @BindView(R.id.id_chartsSelfImg)
    ImageView idChartsSelfImg;
    @BindView(R.id.id_chartsNick)
    TextView idChartsNick;
    @BindView(R.id.id_chartsPoint)
    TextView idChartsPoint;
    @BindView(R.id.id_chartsUseTime)
    TextView idChartsUseTime;
    @BindView(R.id.id_chartsDate)
    TextView idChartsDate;
    @BindView(R.id.charts_listview)
    MyListView chartsListview;
    @BindView(R.id.id_chartsTop)
    LinearLayout idChartsTop;
    @BindView(R.id.sc)
    LinearLayout sc;
    @BindView(R.id.scroll)
    ScrollView scroll;

    private Context context;
    private ChartsList_Adapter adapter;
    private String Uid = "";
    private int Type = 1;
    private ProgressDialog dialog;

    @Override
    protected int setLayoutId() {
        return R.layout.chartslist_layout;
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

        itemTitlemsg.setText("排行榜");

        dialog = new ProgressDialog(context);
        dialog.setMessage("正在加载, 请稍后...");

        Type = getIntent().getIntExtra("Type", 1);

        Uid = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.USER_ID);

        if (Uid.equals("")) {
            idChartsTop.setVisibility(View.GONE);
        } else {
            idChartsTop.setVisibility(View.VISIBLE);
        }

        adapter = new ChartsList_Adapter(context, null);
        chartsListview.setAdapter(adapter);

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Uid.equals("")) {
            idChartsTop.setVisibility(View.GONE);
        } else {
            idChartsTop.setVisibility(View.VISIBLE);
        }


        Resquest.getMnksRankingPX(handler, Uid, Type);
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

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {

                            idChartsNick.setText("您还未参加过测试");
                            java.lang.reflect.Type type = new TypeToken<List<ChartsList_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<ChartsList_Bean> mList = mapper.transformCollection(jsonObject, "List");
                            adapter.setData(mList);
                            adapter.notifyDataSetChanged();

                            for (int i = 0; i < mList.size(); i++) {

                                if (mList.get(i).getSelf() == 1) {
                                    setSelfPH(mList.get(i));
                                    break;
                                }
                            }
                        }
                        if (Code.equals("8")) {

                            idChartsNick.setText("暂无排行");

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

    private void setSelfPH(ChartsList_Bean bean) {

        idChartsSelfNum.setText(bean.getPxSort() + "");

        if (bean.getWeChat().equals("0")) {
            if (!bean.getPicSrc().equals("")) {
                ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getPicSrc(), idChartsSelfImg);
            }
        }
        if (bean.getWeChat().equals("1")) {
            if (!bean.getWx_portrait().equals("")) {
                ImageLoader.getInstance().displayImage(bean.getWx_portrait(), idChartsSelfImg);
            }
        }

        String Unickname = bean.getUnickname();
        String Uaccount = bean.getUaccount();

        if (Unickname.equals(""))

        {
            idChartsNick.setText(Uaccount.subSequence(0, 3)
                    + "****" + Uaccount.subSequence(7, 11));
        } else

        {
            idChartsNick.setText(Unickname);
        }

        idChartsPoint.setText(bean.getFraction() + " 分");
        idChartsDate.setText(bean.getRanDate());

        int Time = bean.getExamTime();

        if (Time % 60 < 10)

        { // XX:0X
            idChartsUseTime.setText(Time / 60 + "分" + "0" + Time % 60 + "秒");
        } else

        {
            idChartsUseTime.setText(Time / 60 + "分" + Time % 60 + "秒");
        }
    }

}
