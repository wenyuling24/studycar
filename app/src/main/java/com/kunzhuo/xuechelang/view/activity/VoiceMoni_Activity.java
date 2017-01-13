package com.kunzhuo.xuechelang.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.model.VoiceMoni_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.utils.ToolsUtils;
import com.kunzhuo.xuechelang.view.adapter.Voice_Adapter;
import com.kunzhuo.xuechelang.widget.MyGridView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/27 0027.
 */
public class VoiceMoni_Activity extends BaseActivity {

    @BindView(R.id.item_back)
    ImageView itemBack;
    @BindView(R.id.item_titlemsg)
    TextView itemTitlemsg;
    @BindView(R.id.id_maplistview)
    MyGridView idMaplistview;

    private Context context;
    private Voice_Adapter adapter;
    private ProgressDialog dialog;
    private int TypeThe = 0;
    private MediaPlayer mediaPlayer; // = new MediaPlayer();
    private int position;

    @Override
    protected int setLayoutId() {
        return R.layout.voice_layout;
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

        TypeThe = getIntent().getIntExtra("TypeThe", 0);

        adapter = new Voice_Adapter(context, null);
        idMaplistview.setAdapter(adapter);

        mediaPlayer = new MediaPlayer();

        idMaplistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                VoiceMoni_Bean bean = (VoiceMoni_Bean) adapterView.getItemAtPosition(i);

                // 获取第几个位置并改变图标
                ImageView imageView = (ImageView) view.findViewById(R.id.id_voiceImg);
                ImageView imagePlayView = (ImageView) view.findViewById(R.id.id_voicePlayImg);

                imageView.setVisibility(View.INVISIBLE);
                imagePlayView.setVisibility(View.VISIBLE);

                String path = bean.getVoiceMp3();
                position = i;

                Uri uri = Uri.parse(path);

                try {

                    mediaPlayer.setDataSource(context, uri);
//                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        if (TypeThe == 0) {
            itemTitlemsg.setText("语音模拟");
            Resquest.getVoiceList(handler);
        }
        if (TypeThe == 1) {

            itemTitlemsg.setText("灯光操作");
            Resquest.getLightingList(handler);
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {

                View tempView = idMaplistview.getChildAt(position);

                // 获取第几个位置并改变图标
                ImageView imageView = (ImageView) tempView.findViewById(R.id.id_voiceImg);
                ImageView imagePlayView = (ImageView) tempView.findViewById(R.id.id_voicePlayImg);

                imageView.setVisibility(View.VISIBLE);
                imagePlayView.setVisibility(View.GONE);

//                try {
//                    player.setDataSource(path);
//                    player.prepare();
//                    player.start();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                mediaPlayer.reset();

            }
        });
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

                            String VoiceImg = jsonObject.getString("VoiceImg");
                            String VoiceMp3 = jsonObject.getString("VoiceMp3");
                            String StrList = jsonObject.getString("StrList");

                            ArrayList<VoiceMoni_Bean> list = new ArrayList<>();
                            List<String> tempList = Arrays.asList(StrList.split(","));

                            for (int i = 0; i < tempList.size(); i++) {

                                VoiceMoni_Bean bean = new VoiceMoni_Bean();

                                String name = tempList.get(i);
                                bean.setVoiceName(name);
                                bean.setVoiceImg(VoiceImg + ToolsUtils.urlEnodeUTF8(name) + ".jpg");
                                bean.setVoiceMp3(VoiceMp3 + ToolsUtils.urlEnodeUTF8(name) + ".mp3");
                                list.add(bean);

                            }

                            adapter.setData(list);
                            adapter.notifyDataSetChanged();

                        }


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
}
