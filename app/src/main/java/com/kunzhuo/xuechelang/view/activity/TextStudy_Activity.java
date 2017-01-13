package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.RegionList_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.utils.ToolsUtils;
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
 * Created by Administrator on 2016/11/2 0002.
 */
public class TextStudy_Activity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_editName)
    EditText idEditName;
    @BindView(R.id.id_editPhone)
    EditText idEditPhone;
    @BindView(R.id.spinnerArea)
    CustomerSpinner spinnerArea;
    @BindView(R.id.id_editArea)
    EditText idEditArea;
    @BindView(R.id.id_TextDriveBtn)
    Button idTextDriveBtn;

    private Context context;
    private ProgressDialog dialog;
    private ArrayList<String> areaList_Str2 = new ArrayList<>(); // 地区列表Str
    private ArrayAdapter<String> adapter_Area;
    private String DistriCoach = "";

    @Override
    protected int setLayoutId() {
        return R.layout.textstudy_layout;
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

        itemTitlemsg.setText("学车郎—试学");

        DistriCoach = getIntent().getStringExtra("DistriCoach");

        adapter_Area = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, areaList_Str2);

        spinnerArea.setList(areaList_Str2);
        spinnerArea.setAdapter(adapter_Area);

        spinnerArea.setOnItemSelectedListener(new AreaSpinnerSelectedListener());

        idEditPhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        dialog = new ProgressDialog(context);
        dialog.setMessage("正在加载, 请稍后...");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Resquest.getAreaRegionList(handler);
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

                            adapter_Area.notifyDataSetChanged();

                            mList.clear();
                            tempList.clear();
                        }
                        if (Code.equals("8")) {
                            areaList_Str2.clear();
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


    /**
     * 地区
     *
     * @author waaa
     */
    class AreaSpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {

            String area_Str2 = areaList_Str2.get(position);
            spinnerArea.setText(area_Str2);

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    @OnClick({R.id.item_back, R.id.id_TextDriveBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_back:
                finish();
                break;
            case R.id.id_TextDriveBtn:
                String Name = idEditName.getText().toString();
                String Phone = idEditPhone.getText().toString();
                String Area = idEditArea.getText().toString();

                if (!Name.equals("")) {

                    if (!Phone.equals("") && ToolsUtils.isMobileNO(Phone)) {

                        if (DistriCoach == null || DistriCoach.equals("")) {
                            dialog.show();
                            Resquest.testTryTolearn_Add(handler2, Name, Phone, spinnerArea.getText().toString(), Area, "");
                        } else {
                            dialog.show();
                            Resquest.testTryTolearn_Add(handler2, Name, Phone, spinnerArea.getText().toString(), Area, DistriCoach);
                        }


                    } else {
                        ToastUtil.show(context, "请输入正确的电话号码");

                    }

                } else {
                    ToastUtil.show(context, "姓名不能为空");
                }


                break;
        }
    }

    Handler handler2 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        String Code = jsonObject.getString("Code");
                        if (Code.equals("0")) {

                            ToastUtil.show(context, "提交成功, 我们的客服会在24小时内和您联系");
                            finish();
                        }
                        if (Code.equals("8")) {


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
