package com.kunzhuo.xuechelang.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.User_Bean;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.view.activity.CarList_Activity;
import com.kunzhuo.xuechelang.view.activity.CoachList_Activity;
import com.kunzhuo.xuechelang.view.activity.Login_Activity;
import com.kunzhuo.xuechelang.view.activity.Peilian_Activity;
import com.kunzhuo.xuechelang.view.activity.TextDrive_Activity;
import com.kunzhuo.xuechelang.view.activity.TextStudy_Activity;
import com.kunzhuo.xuechelang.view.activity.WebView_Activity;
import com.kunzhuo.xuechelang.widget.CustomImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/28 0028.
 * 】学员
 */
public class CoachMainStu_Framgent extends BaseFragment {

    @BindView(R.id.id_Avtar)
    CustomImageView idAvtar;
    @BindView(R.id.id_Name)
    TextView idName;
    @BindView(R.id.id_firstBtn)
    LinearLayout idFirstBtn;
    @BindView(R.id.id_secondBtn)
    LinearLayout idSecondBtn;
    @BindView(R.id.id_threeBtn)
    LinearLayout idThreeBtn;
    @BindView(R.id.id_fourBtn)
    LinearLayout idFourBtn;
    @BindView(R.id.id_fiveBtn)
    LinearLayout idFiveBtn;
    @BindView(R.id.id_sixBtn)
    LinearLayout idSixBtn;
    @BindView(R.id.id_sevenBtn)
    LinearLayout idSevenBtn;
    @BindView(R.id.id_IconLayout)
    LinearLayout idIconLayout;

    private Context context;
    private String Uid = "";

    @Override
    protected int setLayoutId() {
        return R.layout.coachmain_layout;
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
    protected void upDetoryViews() {

    }

    private void initViews() {
        context = getActivity();

        Uid = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.USER_ID);

        String userJson = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.USER_JSONMSG);

        getUserPhone(userJson);
    }

    public void getUserPhone(String userJson) {

        if (!userJson.equals("")) {
            Gson gson = new Gson();
            User_Bean bean = gson.fromJson(userJson, User_Bean.class);

            if (!bean.getUnickname().equals("")) {
                idName.setText(bean.getUnickname());
            } else {
                idName.setText(bean.getUaccount().subSequence(0, 3)
                        + "****" + bean.getUaccount().subSequence(7, 11));
            }

            if (!bean.getWx_nickname().equals("")) {
                idName.setText(bean.getWx_nickname());
            }

            if (!bean.getPicSrc().equals("")) {

                ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getPicSrc(), idAvtar);

            } else {

                ImageLoader.getInstance().displayImage(bean.getWx_portrait(), idAvtar);
            }

        } else {
            idName.setText("注册/登录");

            idIconLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, Login_Activity.class);

                    startActivity(intent);
                }
            });
        }

    }


    @OnClick({R.id.id_firstBtn, R.id.id_secondBtn, R.id.id_threeBtn, R.id.id_fourBtn, R.id.id_fiveBtn, R.id.id_sixBtn, R.id.id_sevenBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_firstBtn:

                Intent intent1 = new Intent(context, CoachList_Activity.class);

                startActivity(intent1);

                break;
            case R.id.id_secondBtn:

                Intent intent2 = new Intent(context, TextStudy_Activity.class);

                startActivity(intent2);

                break;
            case R.id.id_threeBtn:
                Intent intent3 = new Intent(context, TextDrive_Activity.class);

                startActivity(intent3);
                break;
            case R.id.id_fourBtn:

                if (!Uid.equals("")) {
                    Intent intent4 = new Intent(context, Peilian_Activity.class);

                    startActivity(intent4);
                } else {
                    ToastUtil.show(context, "您还未登录");
                    Intent intent = new Intent(context, Login_Activity.class);
                    startActivity(intent);
                }

                break;
            case R.id.id_fiveBtn:

                Intent intent = new Intent(context, CarList_Activity.class);
                startActivity(intent);

                break;

            case R.id.id_sixBtn:

                Intent intentself1 = new Intent(getContext(), WebView_Activity.class);

                intentself1.putExtra("Url", "http://xueche555.com/Physical/PhysicalListAndroid.aspx");
                intentself1.putExtra("Title", "驾驶体检点");

                startActivity(intentself1);
                break;
            case R.id.id_sevenBtn:

                Intent intentself2 = new Intent(getContext(), WebView_Activity.class);

                intentself2.putExtra("Url", "http://www.vzan.com/f/s-727927?typeId=0");
                intentself2.putExtra("Title", "学车论坛");

                startActivity(intentself2);

                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
