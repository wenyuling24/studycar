package com.kunzhuo.xuechelang.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.Video_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.utils.ToolsUtils;
import com.kunzhuo.xuechelang.view.activity.PlayVideo_Activity;
import com.kunzhuo.xuechelang.view.adapter.VideoSecond_Adapter1;
import com.kunzhuo.xuechelang.view.adapter.VideoSecond_Adapter1_2;
import com.kunzhuo.xuechelang.view.adapter.VideoSecond_Adapter2;
import com.kunzhuo.xuechelang.view.adapter.VideoSecond_Adapter2_2;
import com.kunzhuo.xuechelang.widget.MyGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/10/8 0008.
 */
public class TrainDetail_Fragment extends BaseFragment {


    @BindView(R.id.detail_gridCourseView1)
    MyGridView detailGridCourseView1;
    @BindView(R.id.detail_gridCourseView2)
    MyGridView detailGridCourseView2;
    @BindView(R.id.detail_gridCourseView3)
    MyGridView detailGridCourseView3;
    @BindView(R.id.detail_gridCourseView4)
    MyGridView detailGridCourseView4;
    @BindView(R.id.detail_gridCourseView5)
    MyGridView detailGridCourseView5;

    private VideoSecond_Adapter1 adapter1;
    private VideoSecond_Adapter2 adapter2;
    private VideoSecond_Adapter1 adapter3;
    private VideoSecond_Adapter2_2 adapter4;
    private VideoSecond_Adapter1 adapter5;
    private String TypeID = "";

    @Override
    protected int setLayoutId() {
        return R.layout.detail_fragment;
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

        Bundle bundle = getArguments();
        TypeID = bundle.getString("ID");

        adapter1 = new VideoSecond_Adapter1(getActivity(), null);
        detailGridCourseView1.setAdapter(adapter1);

        adapter2 = new VideoSecond_Adapter2(getActivity(), null);
        detailGridCourseView2.setAdapter(adapter2);

        adapter3 = new VideoSecond_Adapter1(getActivity(), null);
        detailGridCourseView3.setAdapter(adapter3);

        adapter4 = new VideoSecond_Adapter2_2(getActivity(), null);
        detailGridCourseView4.setAdapter(adapter4);

        adapter5 = new VideoSecond_Adapter1(getActivity(), null);
        detailGridCourseView5.setAdapter(adapter5);

        detailGridCourseView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Video_Bean bean = (Video_Bean) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getActivity(), PlayVideo_Activity.class);
                intent.putExtra("Vid", bean.getVid());
                intent.putExtra("Type", 1);

                startActivity(intent);
            }
        });

        detailGridCourseView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Video_Bean bean = (Video_Bean) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getActivity(), PlayVideo_Activity.class);
                intent.putExtra("Vid", bean.getVid());
                intent.putExtra("Type", 1);

                startActivity(intent);
            }
        });

        detailGridCourseView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Video_Bean bean = (Video_Bean) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getActivity(), PlayVideo_Activity.class);
                intent.putExtra("Vid", bean.getVid());
                intent.putExtra("Type", 1);

                startActivity(intent);
            }
        });

        detailGridCourseView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Video_Bean bean = (Video_Bean) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getActivity(), PlayVideo_Activity.class);
                intent.putExtra("Vid", bean.getVid());
                intent.putExtra("Type", 1);

                startActivity(intent);
            }
        });

        detailGridCourseView5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Video_Bean bean = (Video_Bean) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getActivity(), PlayVideo_Activity.class);
                intent.putExtra("Vid", bean.getVid());
                intent.putExtra("Type", 1);

                startActivity(intent);
            }
        });

//        httpRepost();
    }

    @Override
    public void onResume() {
        super.onResume();
        httpRepost();
    }

    public void httpRepost() {

        Resquest.getVideoListType(handler, TypeID, ToolsUtils.getRandom(1, 3), 3);
        Resquest.getVideoListType(handler2, TypeID, ToolsUtils.getRandom(3, 6), 1);
        Resquest.getVideoListType(handler3, TypeID, ToolsUtils.getRandom(6, 11), 3);
        Resquest.getVideoListType(handler4, TypeID, ToolsUtils.getRandom(11, 14), 1);
        Resquest.getVideoListType(handler5, TypeID, ToolsUtils.getRandom(14, 16), 3);
    }

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        Type type = new TypeToken<List<Video_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<Video_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        adapter1.setData(mList);
                        adapter1.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(getActivity(), "网络异常");
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
                        Type type = new TypeToken<List<Video_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<Video_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        adapter2.setData(mList);
                        adapter2.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(getActivity(), "网络异常");
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
                        Type type = new TypeToken<List<Video_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<Video_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        adapter3.setData(mList);
                        adapter3.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(getActivity(), "网络异常");
                    break;
            }

            return true;
        }
    });

    Handler handler4 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        Type type = new TypeToken<List<Video_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<Video_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        adapter4.setData(mList);
                        adapter4.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(getActivity(), "网络异常");
                    break;
            }

            return true;
        }
    });

    Handler handler5 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        Type type = new TypeToken<List<Video_Bean>>() {
                        }.getType();
                        SimpleDataMapper mapper = new SimpleDataMapper(type);
                        List<Video_Bean> mList = mapper.transformCollection(jsonObject, "List");
                        adapter5.setData(mList);
                        adapter5.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(getActivity(), "网络异常");
                    break;
            }

            return true;
        }
    });

}
