package com.kunzhuo.xuechelang.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.TryTolearn_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.adapter.MyBaseAdapter;
import com.kunzhuo.xuechelang.widget.MyListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/5 0005.
 */
public class TryTolearn_Activity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_coachlisttxt)
    TextView idCoachlisttxt;
    @BindView(R.id.first_trystudylistview)
    MyListView firstTrystudylistview;

    private Context context;
    private String Uid = "";
    private TryTolearn_Adapter adapter;

    @Override
    protected int setLayoutId() {
        return R.layout.trystudy_layout;
    }

    @Override
    protected void setDefaultViews() {

        iniViews();
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void updateViews() {

    }

    private void iniViews() {

        context = this;
        itemTitlemsg.setText("试学申请");

        Uid = getIntent().getStringExtra("Uid");

        adapter = new TryTolearn_Adapter(context, null);
        firstTrystudylistview.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();

        Resquest.getTryTolearn_ListDB(handlerList, Uid);
    }


    Handler handlerList = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {
                            idCoachlisttxt.setVisibility(View.GONE);
                            firstTrystudylistview.setVisibility(View.VISIBLE);

                            Type type = new TypeToken<List<TryTolearn_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<TryTolearn_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            adapter.setData(mList);
                            adapter.notifyDataSetChanged();
                        }
                        if (Code.equals("8")) {
                            idCoachlisttxt.setVisibility(View.VISIBLE);
                            firstTrystudylistview.setVisibility(View.GONE);
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


    class TryTolearn_Adapter extends MyBaseAdapter<TryTolearn_Bean> {


        private Holder mHolder;
        private Context context;

        public TryTolearn_Adapter(Context mContext, List<TryTolearn_Bean> mList) {
            super(mContext, mList);
            this.context = mContext;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.trystudy_item, parent,
                        false);
                mHolder = new Holder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (Holder) convertView.getTag();
            }

            final TryTolearn_Bean bean = mList.get(position);

            mHolder.idTryStudyName.setText("姓名: " + bean.getName());
            mHolder.idTryStudyPhone.setText("电话: " + bean.getPhone());
            mHolder.idTryStudyAddress.setText("地址: " + bean.getDetailed());


            if (bean.getTryState().equals("2") || bean.getTryState().equals("4")) {

                mHolder.idTryStudyBtn1.setVisibility(View.VISIBLE);
                mHolder.idTryStudyRelayout.setVisibility(View.GONE);

                mHolder.idTryStudyBtn1.setText("接收学员");

                mHolder.idTryStudyBtn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Resquest.editTryTolearn_EditState(handler, bean.getID(), 5);
                    }
                });
            }

            if (bean.getTryState().equals("5")) {
                mHolder.idTryStudyBtn1.setText("完   成");

                mHolder.idTryStudyBtn1.setVisibility(View.VISIBLE);
                mHolder.idTryStudyRelayout.setVisibility(View.GONE);

                mHolder.idTryStudyBtn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Resquest.editTryTolearn_EditState(handler2, bean.getID(), 6);
                    }
                });
            }
            if (bean.getTryState().equals("6")) {

                mHolder.idTryStudyBtn1.setVisibility(View.GONE);
                mHolder.idTryStudyRelayout.setVisibility(View.VISIBLE);

            }


            return convertView;
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
                                ToastUtil.show(context, "操作成功");
                                mHolder.idTryStudyBtn1.setText("完   成");

                                adapter.notifyDataSetChanged();
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

                                ToastUtil.show(context, "操作成功");

                                mHolder.idTryStudyBtn1.setVisibility(View.GONE);
                                mHolder.idTryStudyRelayout.setVisibility(View.VISIBLE);

                                adapter.notifyDataSetChanged();

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


        class Holder {

            @BindView(R.id.id_tryStudyName)
            TextView idTryStudyName;
            @BindView(R.id.id_tryStudyPhone)
            TextView idTryStudyPhone;
            @BindView(R.id.id_tryStudyAddress)
            TextView idTryStudyAddress;
            @BindView(R.id.id_tryStudyBtn1)
            TextView idTryStudyBtn1;
            @BindView(R.id.id_tryStudyRelayout)
            RelativeLayout idTryStudyRelayout;

            public Holder(View v) {
                ButterKnife.bind(this, v);
            }
        }


    }
}
