package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.Brand_Bean;
import com.kunzhuo.xuechelang.model.RegionList_Bean;
import com.kunzhuo.xuechelang.model.UserdCar_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.adapter.UserdCar_Adapter;
import com.kunzhuo.xuechelang.widget.MyListView;
import com.kunzhuo.xuechelang.widget.SlidingMenu;
import com.kunzhuo.xuechelang.widget.spinner.CustomerSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/8 0008.
 */
public class CarList_Activity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_rightLayout)
    RelativeLayout idRightLayout;
    @BindView(R.id.id_carlisttxt)
    TextView idCarlisttxt;
    @BindView(R.id.first_carlistview)
    MyListView firstCarlistview;
    @BindView(R.id.spinnerBrand)
    CustomerSpinner spinnerBrand;
    @BindView(R.id.spinnerYear)
    CustomerSpinner spinnerYear;
    @BindView(R.id.id_coachrightRelease)
    TextView idCoachrightRelease;
    @BindView(R.id.id_coachrightSure)
    TextView idCoachrightSure;
    @BindView(R.id.id_menu)
    SlidingMenu idMenu;

    private Context context;
    private UserdCar_Adapter adapter;
    private ProgressDialog dialog;

    String[] carTypeList = new String[]{"不限", "微型车", "小型车", "紧凑型", "中型车", "中大型",
            "豪华车", "跑车", "SUV", "MPV", "CROSS", "皮卡", "客车", "货车", "面包车", "微卡"};

    private ArrayList<Brand_Bean> areaList_Str = new ArrayList<>(); // 品牌列表
    private ArrayList<String> areaList_Str2 = new ArrayList<>(); // 品牌列表Str
    private ArrayList<String> yearList_Str = new ArrayList<>(); // 车型列表Str

    private ArrayAdapter<String> adapter_Area;
    private ArrayAdapter<String> adapter_Year;
    private String area_Str = "";
    private String year_Str = "";


    @Override
    protected int setLayoutId() {
        return R.layout.carlist_layout;
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
        itemTitlemsg.setText("学车郎—新手淘车");
        adapter = new UserdCar_Adapter(context, null);
        firstCarlistview.setAdapter(adapter);

        firstCarlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                UserdCar_Bean bean = (UserdCar_Bean) adapterView.getItemAtPosition(i);

                Intent intenSx = new Intent(context, CarDesc_Activity.class);

                intenSx.putExtra("Car_UsedID", bean.getId());

                startActivity(intenSx);


            }
        });
        getCarList();

        idRightLayout.setVisibility(View.VISIBLE);
        dialog = new ProgressDialog(context);
        dialog.setMessage("正在加载,请稍后...");

        adapter_Area = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, areaList_Str2);

        adapter_Year = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, yearList_Str);

        spinnerBrand.setList(areaList_Str2);
        spinnerBrand.setAdapter(adapter_Area);
        spinnerBrand.setOnItemSelectedListener(new AreaSpinnerSelectedListener());

        spinnerYear.setList(yearList_Str);
        spinnerYear.setAdapter(adapter_Year);
        spinnerYear.setOnItemSelectedListener(new YearSpinnerSelectedListener());

        Resquest.getParameterUsed(handler);

    }


    @Override
    protected void onResume() {
        super.onResume();


        dialog.show();

        Resquest.getUsedCarList(handler2, "不限", "不限", 80, 1);

    }

    private void getCarList() {

        for (int i = 0; i < carTypeList.length; i++) {

            yearList_Str.add(carTypeList[i]);
        }
    }

    @OnClick({R.id.item_back, R.id.id_rightLayout, R.id.id_coachrightRelease, R.id.id_coachrightSure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.id_rightLayout:
                idMenu.toggle();
                break;
            case R.id.id_coachrightRelease:
                area_Str = "不限";
                year_Str = "不限";
                spinnerBrand.setText(area_Str);
                spinnerYear.setText(year_Str);
                break;
            case R.id.id_coachrightSure:

                Resquest.getUsedCarList(handler2, area_Str, year_Str, 40, 1);

                dialog.show();
                idMenu.toggle();

                break;
        }
    }

    /**
     * 地区
     *
     * @author waaa
     */
    class AreaSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {

            String area_Str2 = areaList_Str2.get(position);
            area_Str = areaList_Str.get(position).getPid();
            spinnerBrand.setText(area_Str2);

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /**
     * 教龄
     *
     * @author waaa
     */
    class YearSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {

            year_Str = yearList_Str.get(position);
            spinnerYear.setText(year_Str);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
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
                            Type type = new TypeToken<List<Brand_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<Brand_Bean> mList = mapper.transformCollection(jsonObject, "List");
                            ArrayList<String> tempList = new ArrayList<>(); // 地区列表

                            Brand_Bean bean = new Brand_Bean("不限", "不限");

                            mList.add(0, bean);

                            for (int i = 0; i < mList.size(); i++) {

                                tempList.add(mList.get(i).getPname());
                            }

                            areaList_Str2.clear();
                            areaList_Str2.addAll(tempList);
                            areaList_Str.clear();
                            areaList_Str.addAll(mList);

                            adapter_Area.notifyDataSetChanged();

                            mList.clear();
                            tempList.clear();
                        }
                        if (Code.equals("8")) {
                            areaList_Str.clear();
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
                            idCarlisttxt.setVisibility(View.GONE);
                            firstCarlistview.setVisibility(View.VISIBLE);

                            Type type = new TypeToken<List<UserdCar_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<UserdCar_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            adapter.setData(mList);
                            adapter.notifyDataSetChanged();
                        }
                        if (Code.equals("8")) {
                            idCarlisttxt.setVisibility(View.VISIBLE);
                            firstCarlistview.setVisibility(View.GONE);
                            areaList_Str.clear();

                        }

                        dialog.cancel();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case Resquest.FAILED_MSG:

                    dialog.cancel();
                    ToastUtil.show(context, "网络异常");
                    break;
            }

            return true;
        }
    });
}
