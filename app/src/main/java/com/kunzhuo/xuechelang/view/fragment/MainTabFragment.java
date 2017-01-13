package com.kunzhuo.xuechelang.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/22 0022.
 */
public class MainTabFragment extends BaseFragment {

    final static String TAG = MainTabFragment.class.getSimpleName();
    public static final int FIRST_POSITION = 0;
    public static final int SECOND_POSITION = 1;
    public static final int THREE_POSITION = 2;
    public static final int FOURL_POSITION = 3;

    @BindView(R.id.id_firstTxt)
    TextView idFirstTxt;
    @BindView(R.id.id_firstColor)
    View idFirstColor;
    @BindView(R.id.id_firstBtn)
    RelativeLayout idFirstBtn;
    @BindView(R.id.id_secondTxt)
    TextView idSecondTxt;
    @BindView(R.id.id_secondColor)
    View idSecondColor;
    @BindView(R.id.id_secondBtn)
    RelativeLayout idSecondBtn;
    @BindView(R.id.id_threeTxt)
    TextView idThreeTxt;
    @BindView(R.id.id_threeColor)
    View idThreeColor;
    @BindView(R.id.id_threeBtn)
    RelativeLayout idThreeBtn;
    @BindView(R.id.id_fourTxt)
    TextView idFourTxt;
    @BindView(R.id.id_fourColor)
    View idFourColor;
    @BindView(R.id.id_fourBtn)
    RelativeLayout idFourBtn;

    private OnBottomItemClickListener bottomItemClickListener;

    @Override
    protected int setLayoutId() {
        return R.layout.mainbtn_layout;
    }

    @Override
    protected void setDefaultViews() {

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

    public void setOnBottomItemClickListener(
            OnBottomItemClickListener bottomItemClickListener) {
        this.bottomItemClickListener = bottomItemClickListener;
    }

    public void setSelected(int position) {
        switch (position) {
            case FIRST_POSITION:

                idFirstTxt.setTextColor(Color.rgb(228, 72, 74));
                idFirstColor.setBackgroundColor(Color.rgb(228, 72, 74));
                idSecondTxt.setTextColor(Color.rgb(51, 51, 51));
                idSecondColor.setBackgroundColor(Color.rgb(255, 255, 255));
                idThreeTxt.setTextColor(Color.rgb(51, 51, 51));
                idThreeColor.setBackgroundColor(Color.rgb(255, 255, 255));
                idFourTxt.setTextColor(Color.rgb(51, 51, 51));
                idFourColor.setBackgroundColor(Color.rgb(255, 255, 255));

                break;
            case SECOND_POSITION:

                idFirstTxt.setTextColor(Color.rgb(51, 51, 51));
                idFirstColor.setBackgroundColor(Color.rgb(255, 255, 255));
                idSecondTxt.setTextColor(Color.rgb(228, 72, 74));
                idSecondColor.setBackgroundColor(Color.rgb(228, 72, 74));
                idThreeTxt.setTextColor(Color.rgb(51, 51, 51));
                idThreeColor.setBackgroundColor(Color.rgb(255, 255, 255));
                idFourTxt.setTextColor(Color.rgb(51, 51, 51));
                idFourColor.setBackgroundColor(Color.rgb(255, 255, 255));
                break;
            case THREE_POSITION:

                idFirstTxt.setTextColor(Color.rgb(51, 51, 51));
                idFirstColor.setBackgroundColor(Color.rgb(255, 255, 255));
                idSecondTxt.setTextColor(Color.rgb(51, 51, 51));
                idSecondColor.setBackgroundColor(Color.rgb(255, 255, 255));
                idThreeTxt.setTextColor(Color.rgb(228, 72, 74));
                idThreeColor.setBackgroundColor(Color.rgb(228, 72, 74));
                idFourTxt.setTextColor(Color.rgb(51, 51, 51));
                idFourColor.setBackgroundColor(Color.rgb(255, 255, 255));
                break;
            case FOURL_POSITION:

                idFirstTxt.setTextColor(Color.rgb(51, 51, 51));
                idFirstColor.setBackgroundColor(Color.rgb(255, 255, 255));
                idSecondTxt.setTextColor(Color.rgb(51, 51, 51));
                idSecondColor.setBackgroundColor(Color.rgb(255, 255, 255));
                idThreeTxt.setTextColor(Color.rgb(51, 51, 51));
                idThreeColor.setBackgroundColor(Color.rgb(255, 255, 255));
                idFourTxt.setTextColor(Color.rgb(228, 72, 74));
                idFourColor.setBackgroundColor(Color.rgb(228, 72, 74));

                break;
        }
    }


    @OnClick({R.id.id_firstBtn, R.id.id_secondBtn, R.id.id_threeBtn, R.id.id_fourBtn})
    public void onClick(View view) {
        View v = idFirstBtn;
        int position = 0;
        switch (view.getId()) {
            case R.id.id_firstBtn:
                v = idFirstBtn;
                position = FIRST_POSITION;
                break;
            case R.id.id_secondBtn:
                v = idSecondBtn;
                position = SECOND_POSITION;
                break;
            case R.id.id_threeBtn:
                v = idThreeBtn;
                position = THREE_POSITION;
                break;
            case R.id.id_fourBtn:
                v = idFourBtn;
                position = FOURL_POSITION;
                break;
        }

        if (v != null && bottomItemClickListener != null) {
            bottomItemClickListener.onBottomItemClick(v, position);
        }
    }

    public interface OnBottomItemClickListener {
        void onBottomItemClick(View view, int position);
    }
}
