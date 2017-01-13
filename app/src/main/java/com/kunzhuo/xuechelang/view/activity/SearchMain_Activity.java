package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.CoachList_Bean;
import com.kunzhuo.xuechelang.model.Video_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.adapter.CaochList_Adapter;
import com.kunzhuo.xuechelang.view.adapter.VideoSecond_Adapter2;
import com.kunzhuo.xuechelang.widget.MyListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/31 0031.
 */
public class SearchMain_Activity extends BaseActivity {


    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_notListtxt)
    TextView idNoTxt;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.id_SarchNameShip)
    TextView idSarchNameShip;
    @BindView(R.id.first_shiplist)
    MyListView firstShiplist;
    @BindView(R.id.id_SarchNameCoach)
    TextView idSarchNameCoach;
    @BindView(R.id.first_coachlist)
    MyListView firstCoachlist;
    @BindView(R.id.id_forShiplayout)
    LinearLayout idForShiplayout;
    @BindView(R.id.id_forCoachlayout)
    LinearLayout idForCoachlayout;

    private Context context;
    private String searchTxt;
    private VideoSecond_Adapter2 adapter;
    private CaochList_Adapter adapterCoach;
    private ProgressDialog dialog;
    private InputMethodManager inputMethodManager;
    private int noMsgState1 = 0;
    private int noMsgState2 = 0;

    @Override
    protected int setLayoutId() {
        return R.layout.search_layout;
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

        dialog = new ProgressDialog(context);
        dialog.setMessage("正在加载, 请稍后...");
        dialog.show();

        searchView.setQueryHint("找视频,找教练");

        searchView.setIconified(false);

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        itemTitlemsg.setText("搜索");
        searchTxt = getIntent().getStringExtra("searchTxt");

        idSarchNameShip.setText(searchTxt);
        idSarchNameCoach.setText(searchTxt);

        adapter = new VideoSecond_Adapter2(context, null);
        firstShiplist.setAdapter(adapter);

        firstShiplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Video_Bean bean = (Video_Bean) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(context, PlayVideo_Activity.class);
                intent.putExtra("Vid", bean.getVid());
                intent.putExtra("Type", 2);
                startActivity(intent);
            }
        });

        adapterCoach = new CaochList_Adapter(context, null);
        firstCoachlist.setAdapter(adapterCoach);

        firstCoachlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CoachList_Bean bean = (CoachList_Bean) adapterView.getItemAtPosition(i);

                Intent intenSx = new Intent(context, CoachVideo_Activity.class);

                intenSx.putExtra("Uid", bean.getUID());
                startActivity(intenSx);
            }
        });

        Resquest.getCoach_Titlte(handlerCoach, searchTxt, 1, 100);
        Resquest.getVideo_Title(handlerShip, searchTxt, 1, 100);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                idSarchNameShip.setText(query);
                idSarchNameCoach.setText(query);

                Resquest.getCoach_Titlte(handlerCoach, query, 1, 100);
                Resquest.getVideo_Title(handlerShip, query, 1, 100);

                dialog.show();

                hideSoftInput();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        hideSoftInput();

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

    private void hideSoftInput() {
        if (inputMethodManager != null) {
            View v = SearchMain_Activity.this.getCurrentFocus();
            if (v == null) {
                return;
            }

            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            searchView.clearFocus();
        }
    }

    Handler handlerShip = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {

                            idForShiplayout.setVisibility(View.VISIBLE);

                            Type type = new TypeToken<List<Video_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<Video_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            adapter.setData(mList);
                            adapter.notifyDataSetChanged();

                            noMsgState2 = 0;

                        }
                        if (Code.equals("8")) {

                            idForShiplayout.setVisibility(View.GONE);

                            noMsgState2 = 1;

//                            if (noMsgState1 == 1 && noMsgState2 == 1) {
//                                idNoTxt.setVisibility(View.VISIBLE);
//
//                            }

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

    Handler handlerCoach = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {
                            idForCoachlayout.setVisibility(View.VISIBLE);

                            Type type = new TypeToken<List<CoachList_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<CoachList_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            adapterCoach.setData(mList);
                            adapterCoach.notifyDataSetChanged();

                            noMsgState1 = 0;
                        }
                        if (Code.equals("8")) {

                            idForCoachlayout.setVisibility(View.GONE);

                            noMsgState1 = 1;
//                            if (noMsgState1 == 1 && noMsgState2 == 1) {
//                                idNoTxt.setVisibility(View.VISIBLE);
//
//                            }

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
}
