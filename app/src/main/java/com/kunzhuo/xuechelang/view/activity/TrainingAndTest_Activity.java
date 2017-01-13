package com.kunzhuo.xuechelang.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.view.fragment.ExamRoom_Fragment;
import com.kunzhuo.xuechelang.view.fragment.FirstSubject_Fragment;
import com.kunzhuo.xuechelang.view.fragment.FourSubject_Fragment;
import com.kunzhuo.xuechelang.view.fragment.SecondSubject_Fragment;
import com.kunzhuo.xuechelang.view.fragment.ThreeSubject_Fragment;
import com.kunzhuo.xuechelang.view.fragment.Train_Frament;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public class TrainingAndTest_Activity extends BaseActivity {


    @BindView(R.id.id_back)
    ImageView idBack;
    @BindView(R.id.id_leftBtn)
    TextView idLeftBtn;
    @BindView(R.id.id_rightBtn)
    TextView idRightBtn;
    @BindView(R.id.container)
    FrameLayout container;

    public final static String INTENT_INDEX = "index";
    private final static int FRAGMENT_COUNT = 4;
    private FragmentManager manager;
    private int selectPosition = 0;
    private int Video_Type = 2;

    @Override
    protected int setLayoutId() {
        return R.layout.train_layout;
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

        Intent intent = getIntent();
        int page = intent.getIntExtra("index", 0);// 第一个参数是取值的key,第二个参数是默认值
        Video_Type = intent.getIntExtra("Video_Type", 2);

        manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.add(R.id.container, getFragment(page));
        trans.commit();

    }


    @OnClick({R.id.id_back, R.id.id_leftBtn, R.id.id_rightBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_back:
                finish();
                break;
            case R.id.id_leftBtn:
                replaceFragment(0);
                break;
            case R.id.id_rightBtn:
                replaceFragment(1);
                break;
        }
    }

    private Fragment getFragment(int selectPosition) {
        this.selectPosition = selectPosition;

        Fragment select;

        switch (selectPosition) {
            case 0:
                select = new Train_Frament();
                Bundle bundle = new Bundle();
                bundle.putInt("Video_Category", 1);
                bundle.putInt("Video_Type", Video_Type);
                select.setArguments(bundle);

                idLeftBtn.setTextColor(Color.rgb(29, 172, 250));
                idLeftBtn.setBackgroundColor(Color.rgb(255, 255, 255));
                idRightBtn.setTextColor(Color.rgb(255, 255, 255));
                idRightBtn.setBackgroundResource(R.drawable.btn_bluestn);

                break;
            case 1:
                select = new ExamRoom_Fragment();

                Bundle bundle2 = new Bundle();
                bundle2.putInt("Video_Category", 2);
                bundle2.putInt("Video_Type", Video_Type);
                select.setArguments(bundle2);

                idRightBtn.setTextColor(Color.rgb(29, 172, 250));
                idRightBtn.setBackgroundColor(Color.rgb(255, 255, 255));

                idLeftBtn.setTextColor(Color.rgb(255, 255, 255));
                idLeftBtn.setBackgroundResource(R.drawable.btn_bluestn);


                break;
            default:
                select = new Train_Frament();
                Bundle bundle3 = new Bundle();
                bundle3.putInt("Video_Category", 1);
                bundle3.putInt("Video_Type", Video_Type);

                select.setArguments(bundle3);

                idLeftBtn.setTextColor(Color.rgb(29, 172, 250));
                idLeftBtn.setBackgroundColor(Color.rgb(255, 255, 255));
                idRightBtn.setTextColor(Color.rgb(255, 255, 255));
                idRightBtn.setBackgroundResource(R.drawable.btn_bluestn);
        }


        return select;
    }

    /**
     * @param selectPosition
     */
    private void replaceFragment(int selectPosition) {
        FragmentTransaction trans = manager.beginTransaction();
        trans.replace(R.id.container, getFragment(selectPosition));
        trans.commit();
    }
}
