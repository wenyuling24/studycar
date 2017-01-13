package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.BannerBean;
import com.kunzhuo.xuechelang.model.CarouselList_Bean;
import com.kunzhuo.xuechelang.model.CoachList_Bean;
import com.kunzhuo.xuechelang.model.RegionList_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.adapter.CaochList_Adapter;
import com.kunzhuo.xuechelang.view.fragment.ImageViewPagerFragment;
import com.kunzhuo.xuechelang.widget.MyListView;
import com.kunzhuo.xuechelang.widget.SlidingMenu;
import com.kunzhuo.xuechelang.widget.spinner.CustomerSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/25 0025.
 */
public class CoachList_Activity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_rightLayout)
    RelativeLayout idRightLayout;
    @BindView(R.id.first_coachlistview)
    MyListView firstCoachlistview;
    @BindView(R.id.spinnerSchool)
    CustomerSpinner spinnerSchool;
    @BindView(R.id.spinnerYear)
    CustomerSpinner spinnerYear;
    @BindView(R.id.id_editCoachName)
    EditText idEditCoachName;
    @BindView(R.id.id_coachrightRelease)
    TextView idCoachrightRelease;
    @BindView(R.id.id_coachrightSure)
    TextView idCoachrightSure;
    @BindView(R.id.id_menu)
    SlidingMenu idMenu;
    @BindView(R.id.id_coachlisttxt)
    TextView idCoachlisttxt;

    private CaochList_Adapter adapter;
    private ImageViewPagerFragment fragmentBanner;
    private Context context;
    private ProgressDialog dialog;

    private ArrayList<RegionList_Bean> areaList_Str = new ArrayList<>(); // 地区列表
    private ArrayList<String> areaList_Str2 = new ArrayList<>(); // 地区列表Str
    private ArrayList<String> yearList_Str = new ArrayList<>(); // 教龄列表
    private ArrayAdapter<String> adapter_Area;
    private ArrayAdapter<String> adapter_Year;
    private String area_Str = "";
    private String year_Str = "";

    String[] checkKeStr = new String[]{"2", "3", "0"};
    // 分配空间2
    CheckBox[] checkBoxsKe = new CheckBox[3];
    // 用来保存那些被选中的
    boolean[] checksKe = new boolean[3];
    List<String> boxKeList = new ArrayList<>();

    private InputMethodManager inputMethodManager;

    @Override
    protected int setLayoutId() {
        return R.layout.coachlist_layout;
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
        itemTitlemsg.setText("学车郎-教练");
        idRightLayout.setVisibility(View.VISIBLE);
        dialog = new ProgressDialog(context);
        dialog.setMessage("正在加载,请稍后...");

        adapter_Area = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, areaList_Str2);

        adapter_Year = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, yearList_Str);

        spinnerSchool.setList(areaList_Str2);
        spinnerSchool.setAdapter(adapter_Area);
        spinnerSchool.setOnItemSelectedListener(new AreaSpinnerSelectedListener());

        spinnerYear.setList(yearList_Str);
        spinnerYear.setAdapter(adapter_Year);
        spinnerYear.setOnItemSelectedListener(new YearSpinnerSelectedListener());

        adapter = new CaochList_Adapter(context, null);
        firstCoachlistview.setAdapter(adapter);

        firstCoachlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CoachList_Bean bean = (CoachList_Bean) adapterView.getItemAtPosition(i);

                Intent intenSx = new Intent(context, CoachVideo_Activity.class);

                intenSx.putExtra("Uid", bean.getUID());
                startActivity(intenSx);
            }
        });

        fragmentBanner = new ImageViewPagerFragment();

        FragmentTransaction trans2 = getSupportFragmentManager()
                .beginTransaction();
        trans2.add(R.id.fragment_banner, fragmentBanner);
        trans2.commit();

        getYearList();
        initCheckBox();

        idMenu.toggle();

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        httpRepost();
    }

    private void httpRepost() {

        dialog.show();

        Resquest.getAreaRegionList(handler);
        Resquest.getCarouselList(handlerBanner, 2);
        Resquest.getCoachList(handler2, "", "", "", "", 1, 50);

    }

    @OnClick({R.id.item_back, R.id.id_rightLayout, R.id.id_coachrightRelease, R.id.id_coachrightSure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.id_coachrightRelease:

                boxKeList.clear();
                checkBoxsKe[0].setChecked(false);
                checkBoxsKe[1].setChecked(false);
                checkBoxsKe[2].setChecked(false);
                area_Str = "不限地区";
                year_Str = "不限教龄";
                spinnerSchool.setText(area_Str);
                spinnerYear.setText(year_Str);

                break;
            case R.id.id_coachrightSure:

                String Type = "";
                String Region = "";
                String Seniority = "";
                String Other = "";

                if (boxKeList.size() == 0) {
                    Type = "";
                } else {
                    Type = boxKeList.get(0);
                }

                if (year_Str.equals("不限教龄")) {
                    Seniority = "";
                } else {

                    StringBuffer sb = new StringBuffer(year_Str);
                    sb.deleteCharAt(sb.length() - 1);
                    Seniority = sb.toString();
                }

                if (area_Str.equals("")) {
                    Region = "";
                } else {
                    Region = area_Str;
                }

                Other = idEditCoachName.getText().toString();

                dialog.show();

                Resquest.getCoachList(handler2, Type, Region, Seniority, Other, 1, 20);

                idMenu.toggle();

                hideSoftInput();

                break;
            case R.id.id_rightLayout:
                idMenu.toggle();
                break;
        }
    }

    private void getYearList() {
        int a = 41;
        yearList_Str.add(0, "不限教龄");

        for (int i = 1; i < a; i++) {

            yearList_Str.add(i + "年");
        }

        adapter_Year.notifyDataSetChanged();
    }

    private void hideSoftInput() {
        if (inputMethodManager != null) {
            View v = CoachList_Activity.this.getCurrentFocus();
            if (v == null) {
                return;
            }

            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
            spinnerSchool.setText(area_Str2);

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


    private void initCheckBox() {

        checkBoxsKe[0] = (CheckBox) findViewById(R.id.type_Ke1);
        checkBoxsKe[1] = (CheckBox) findViewById(R.id.type_Ke2);
        checkBoxsKe[2] = (CheckBox) findViewById(R.id.type_Ke3);

        for (int j = 0; j < checkBoxsKe.length; j++) {

            checkBoxsKe[j].setOnCheckedChangeListener(new MyCheckChangeListenerKe());
        }

    }

    class MyCheckChangeListenerKe implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.type_Ke1:
                    checksKe[0] = isChecked;
                    if (checkBoxsKe[0].isChecked()) {
                        checkBoxsKe[1].setChecked(false);
                        checkBoxsKe[2].setChecked(false);
                        boxKeList.clear();
                        boxKeList.add(checkKeStr[0]);

                    } else {
                        boxKeList.clear();
                    }

                    break;
                case R.id.type_Ke2:

                    checksKe[1] = isChecked;
                    if (checkBoxsKe[1].isChecked()) {
                        checkBoxsKe[0].setChecked(false);
                        checkBoxsKe[2].setChecked(false);

                        boxKeList.clear();
                        boxKeList.add(checkKeStr[1]);

                    } else {
                        boxKeList.clear();
                    }

                    break;
                case R.id.type_Ke3:
                    checksKe[2] = isChecked;
                    if (checkBoxsKe[2].isChecked()) {
                        checkBoxsKe[1].setChecked(false);
                        checkBoxsKe[0].setChecked(false);
                        boxKeList.clear();
                        boxKeList.add(checkKeStr[2]);

                    } else {
                        boxKeList.clear();
                    }

                    break;
            }

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
                            Type type = new TypeToken<List<RegionList_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<RegionList_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            ArrayList<String> tempList = new ArrayList<>(); // 地区列表

                            RegionList_Bean bean = new RegionList_Bean("", "不限地区", 0 + "");

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
                            idCoachlisttxt.setVisibility(View.GONE);
                            firstCoachlistview.setVisibility(View.VISIBLE);

                            Type type = new TypeToken<List<CoachList_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<CoachList_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            adapter.setData(mList);
                            adapter.notifyDataSetChanged();
                        }
                        if (Code.equals("8")) {
                            idCoachlisttxt.setVisibility(View.VISIBLE);
                            firstCoachlistview.setVisibility(View.GONE);
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

    /**
     * 设置轮播图片
     *
     * @param bean
     */
    private void setBannerList(CarouselList_Bean bean) {

        ArrayList<BannerBean> list = new ArrayList<>();
        List<String> tempBannerList = Arrays.asList(bean.getBanner().split(",")); // 首页轮播图片
        String http = bean.getHttp();


        for (int i = 0; i < tempBannerList.size(); i++) {
            BannerBean bannerBean = new BannerBean();
            bannerBean.setId(i);
            bannerBean.setImage(http + tempBannerList.get(i));
            list.add(bannerBean);
        }
        fragmentBanner.sendImgList(list);
    }

    Handler handlerBanner = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    System.out.println(jsonObject.toString() + "  首页轮播获取的json");
                    try {
                        Type type = new TypeToken<List<CarouselList_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<CarouselList_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        setBannerList(mList.get(0));

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

}
