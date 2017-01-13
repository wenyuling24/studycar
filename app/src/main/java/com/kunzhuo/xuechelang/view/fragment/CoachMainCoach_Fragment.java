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
import com.kunzhuo.xuechelang.view.activity.CoachVideo_Activity;
import com.kunzhuo.xuechelang.view.activity.Login_Activity;
import com.kunzhuo.xuechelang.view.activity.SpOrderCoach_Activity;
import com.kunzhuo.xuechelang.view.activity.TryTolearn_Activity;
import com.kunzhuo.xuechelang.view.activity.UpCoachImgRen_Activity;
import com.kunzhuo.xuechelang.widget.CustomImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/28 0028.
 * 教练
 */
public class CoachMainCoach_Fragment extends BaseFragment {

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

    private Context context;
    private String Uid = "";
    private int Tication = 0;

    @Override
    protected int setLayoutId() {
        return R.layout.coachmainforcoach_layout;
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

            Tication = bean.getTication();

        } else {
            idName.setText("注册/登录");
        }

    }

    @OnClick({R.id.id_firstBtn, R.id.id_secondBtn, R.id.id_threeBtn, R.id.id_fourBtn })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_firstBtn:

                if (!Uid.equals("")) {

                    if (Tication == 1) {
                        Intent intenSx = new Intent(context, CoachVideo_Activity.class);
                        intenSx.putExtra("Uid", Uid);
                        startActivity(intenSx);

                    } else {
                        ToastUtil.show(context, "您还未通过认证");
                        Intent intent = new Intent(context, UpCoachImgRen_Activity.class);
                        intent.putExtra("Uid", Uid);
                        intent.putExtra("LocationType", 0);
                        startActivity(intent);

                    }

                } else {
                    ToastUtil.show(context, "您还未登录");
                    Intent intent = new Intent(context, Login_Activity.class);
                    startActivity(intent);
                }

                break;
            case R.id.id_secondBtn:

                if (!Uid.equals("")) {

                    if (Tication == 1) {
                        Intent intent = new Intent(context, TryTolearn_Activity.class);
                        intent.putExtra("Uid", Uid);
                        startActivity(intent);

                    } else {

                        ToastUtil.show(context, "您还未通过认证");

                        Intent intent = new Intent(context, UpCoachImgRen_Activity.class);
                        intent.putExtra("Uid", Uid);
                        intent.putExtra("LocationType", 0);
                        startActivity(intent);
                    }

                } else {
                    ToastUtil.show(context, "您还未登录");
                    Intent intent = new Intent(context, Login_Activity.class);
                    startActivity(intent);
                }
                break;
            case R.id.id_threeBtn:

                if (!Uid.equals("")) {

                    if (Tication == 1) {
                        Intent intent = new Intent(context, SpOrderCoach_Activity.class);
                        intent.putExtra("Uid", Uid);
                        startActivity(intent);

                    } else {

                        ToastUtil.show(context, "您还未通过认证");

                        Intent intent = new Intent(context, UpCoachImgRen_Activity.class);
                        intent.putExtra("Uid", Uid);
                        intent.putExtra("LocationType", 0);
                        startActivity(intent);
                    }


                } else {
                    ToastUtil.show(context, "您还未登录");
                    Intent intent = new Intent(context, Login_Activity.class);
                    startActivity(intent);
                }

                break;
            case R.id.id_fourBtn:
                Intent intent = new Intent(context, CarList_Activity.class);

                startActivity(intent);
                break;

        }
    }
}
