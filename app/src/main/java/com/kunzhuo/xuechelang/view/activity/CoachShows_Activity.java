package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.CoachShows_Bean;
import com.kunzhuo.xuechelang.model.Driver_School_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.adapter.MapList_Adapter;
import com.kunzhuo.xuechelang.widget.MyListView;
import com.kunzhuo.xuechelang.widget.SlidingMenu;
import com.kunzhuo.xuechelang.widget.spinner.CustomerSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/11 0011.
 */
public class CoachShows_Activity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_maplistview)
    MyListView idMaplistview;
    @BindView(R.id.id_rightLayout)
    RelativeLayout idRightLayout;
    @BindView(R.id.id_menu)
    SlidingMenu idMenu;
    @BindView(R.id.spinnerSchool)
    CustomerSpinner spinnerSchool;
    @BindView(R.id.spinnerYear)
    CustomerSpinner spinnerYear;
    @BindView(R.id.id_coachrightRelease)
    TextView idCoachrightRelease;
    @BindView(R.id.id_coachrightSure)
    TextView idCoachrightSure;
    @BindView(R.id.id_mapNoList)
    TextView idMapNoList;

    private Context context;
    private ProgressDialog dialog;
    private double latitude = 0;
    private double longitude = 0;
    private MapList_Adapter adapter;
    private ArrayList<String> schoolList_Str = new ArrayList<String>(); // 驾校名称列表
    private ArrayList<String> yearList_Str = new ArrayList<>(); // 教龄列表
    private ArrayAdapter<String> adapter_School;
    private ArrayAdapter<String> adapter_Year;
    private String school_Str = "";
    private String year_Str = "";

    String[] checkTypeStr = new String[]{"C1", "C2", "0"};
    // 分配空间1
    CheckBox[] checkBoxsType = new CheckBox[3];
    // 用来保存那些被选中的
    boolean[] checksType = new boolean[3];
    List<String> boxTypeList = new ArrayList<String>();


    String[] checkKeStr = new String[]{"2", "3", "0"};
    // 分配空间2
    CheckBox[] checkBoxsKe = new CheckBox[3];
    // 用来保存那些被选中的
    boolean[] checksKe = new boolean[3];
    List<String> boxKeList = new ArrayList<String>();

    private String openid = "";
    private List<CoachShows_Bean> selfList = new ArrayList<>();

    @Override
    protected int setLayoutId() {
        return R.layout.coachshows_layout;
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

    @Override
    protected void onResume() {
        super.onResume();
        httpRepost();
    }

    private void initViews() {

        context = this;
        openid = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.OPEN_ID);

        dialog = new ProgressDialog(context);
        longitude = getIntent().getDoubleExtra("longitude", 0);
        latitude = getIntent().getDoubleExtra("latitude", 0);
        itemTitlemsg.setText("教练展示");
        idRightLayout.setVisibility(View.VISIBLE);
        adapter = new MapList_Adapter(context, null);
        idMaplistview.setAdapter(adapter);

        adapter_School = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, schoolList_Str);

        adapter_Year = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, yearList_Str);

        spinnerSchool.setList(schoolList_Str);
        spinnerSchool.setAdapter(adapter_School);
        spinnerSchool.setOnItemSelectedListener(new SchoolSpinnerSelectedListener());

        spinnerYear.setList(yearList_Str);
        spinnerYear.setAdapter(adapter_Year);
        spinnerYear.setOnItemSelectedListener(new YearSpinnerSelectedListener());

        dialog.setMessage("正在加载, 请稍后...");
        dialog.show();

        getYearList();

        Resquest.getUser_School(handler2);

        initCheckBox();

        idMenu.toggle();

    }

    private void httpRepost() {

        if (!openid.equals("")) {

            Resquest.getMapUser_I(handler3, openid);
        }

        Resquest.getMapListUser(handler, "", "", "", "", longitude, latitude);

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

                boxKeList.clear();
                boxTypeList.clear();
                checkBoxsType[0].setChecked(false);
                checkBoxsType[1].setChecked(false);
                checkBoxsType[2].setChecked(false);
                checkBoxsKe[0].setChecked(false);
                checkBoxsKe[1].setChecked(false);
                checkBoxsKe[2].setChecked(false);
                school_Str = "不限驾校";
                year_Str = "不限教龄";
                spinnerSchool.setText(school_Str);
                spinnerYear.setText(year_Str);

                break;
            case R.id.id_coachrightSure:

                String ke = "";
                String type = "";
                String Driver_school = "";
                String Seniority = "";

                if (boxKeList.size() == 0) {
                    ke = "";
                } else {
                    ke = boxKeList.get(0);
                }
                if (boxTypeList.size() == 0) {
                    type = "";
                } else {
                    type = boxTypeList.get(0);
                }

                if (school_Str.equals("不限驾校")) {
                    Driver_school = "";

                } else {

                    Driver_school = school_Str;

                }

                if (year_Str.equals("不限教龄")) {

                    Seniority = "";

                } else {

                    StringBuffer sb = new StringBuffer(year_Str);
                    sb.deleteCharAt(sb.length() - 1);

                    Seniority = sb.toString();
                }

                Resquest.getMapListUser(handler, type, ke, Seniority, Driver_school, longitude, latitude);

                idMenu.toggle();
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
                            idMapNoList.setVisibility(View.GONE);
                            idMaplistview.setVisibility(View.VISIBLE);
                            Type type = new TypeToken<List<CoachShows_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<CoachShows_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            if (selfList.size() == 1) {

                                List<CoachShows_Bean> tempList = new ArrayList<>();

                                String selfID = selfList.get(0).getID();

                                for (int i = 0; i < mList.size(); i++) {

                                    CoachShows_Bean bean = mList.get(i);

                                    if (bean.getID().equals(selfID)) {

                                        mList.remove(i);

                                        tempList.add(0, selfList.get(0));

                                    } else {
                                        tempList.add(mList.get(i));
                                    }
                                }

                                adapter.setData(tempList);
                                adapter.notifyDataSetChanged();
                            } else {
                                adapter.setData(mList);
                                adapter.notifyDataSetChanged();
                            }

                        }
                        if (Code.equals("8")) {
                            schoolList_Str.clear();
                            adapter.notifyDataSetChanged();
                            idMapNoList.setVisibility(View.VISIBLE);
                            idMaplistview.setVisibility(View.GONE);
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

    Handler handler2 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {
                            Type type = new TypeToken<List<Driver_School_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<Driver_School_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            ArrayList<String> tempList = new ArrayList<String>(); // 驾校名称列表

                            tempList.add(0, "不限驾校");

                            for (int i = 0; i < mList.size(); i++) {

                                String schoolStr = mList.get(i).getDriver_school();
                                tempList.add(schoolStr);
                            }

                            schoolList_Str.clear();
                            schoolList_Str.addAll(tempList);
                            adapter_School.notifyDataSetChanged();

                            tempList.clear();
                        }
                        if (Code.equals("8")) {
                            schoolList_Str.clear();
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

                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {
                            idMapNoList.setVisibility(View.GONE);
                            idMaplistview.setVisibility(View.VISIBLE);
                            Type type = new TypeToken<List<CoachShows_Bean>>() {
                            }.getType();

                            SimpleDataMapper mapper = new SimpleDataMapper(type);
                            List<CoachShows_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            selfList.clear();
                            selfList.add(mList.get(0));

                        }
                        if (Code.equals("8")) {
                            schoolList_Str.clear();
                            adapter.notifyDataSetChanged();

                            idMapNoList.setVisibility(View.VISIBLE);
                            idMaplistview.setVisibility(View.GONE);
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


    private void getYearList() {
        int a = 41;
        yearList_Str.add(0, "不限教龄");

        for (int i = 1; i < a; i++) {

            yearList_Str.add(i + "年");
        }

        adapter_Year.notifyDataSetChanged();
    }

    /**
     * 驾校
     *
     * @author waaa
     */
    class SchoolSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {

            school_Str = schoolList_Str.get(position);

            spinnerSchool.setText(school_Str);

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

        checkBoxsType[0] = (CheckBox) findViewById(R.id.type_subjectC1);
        checkBoxsType[1] = (CheckBox) findViewById(R.id.type_subjectC2);
        checkBoxsType[2] = (CheckBox) findViewById(R.id.type_subjectC3);
        checkBoxsKe[0] = (CheckBox) findViewById(R.id.type_Ke1);
        checkBoxsKe[1] = (CheckBox) findViewById(R.id.type_Ke2);
        checkBoxsKe[2] = (CheckBox) findViewById(R.id.type_Ke3);

        for (int i = 0; i < checkBoxsType.length; i++) {

            checkBoxsType[i].setOnCheckedChangeListener(new MyCheckChangeListenerType());
        }

        for (int j = 0; j < checkBoxsKe.length; j++) {

            checkBoxsKe[j].setOnCheckedChangeListener(new MyCheckChangeListenerKe());
        }
    }

    class MyCheckChangeListenerType implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.type_subjectC1:
                    checksType[0] = isChecked;
                    if (checkBoxsType[0].isChecked()) {
                        checkBoxsType[1].setChecked(false);
                        checkBoxsType[2].setChecked(false);

                        boxTypeList.clear();
                        boxTypeList.add(checkTypeStr[0]);
                    } else {
                        boxTypeList.clear();
                    }

                    break;
                case R.id.type_subjectC2:
                    checksType[1] = isChecked;
                    if (checkBoxsType[1].isChecked()) {

                        checkBoxsType[0].setChecked(false);
                        checkBoxsType[2].setChecked(false);

                        boxTypeList.clear();
                        boxTypeList.add(checkTypeStr[1]);
                    } else {
                        boxTypeList.clear();
                    }
                    break;
                case R.id.type_subjectC3:
                    checksType[2] = isChecked;
                    if (checkBoxsType[2].isChecked()) {

                        checkBoxsType[1].setChecked(false);
                        checkBoxsType[0].setChecked(false);

                        boxTypeList.clear();
                        boxTypeList.add(checkTypeStr[2]);

                    } else {
                        boxTypeList.clear();
                    }
                    break;
            }

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


}
