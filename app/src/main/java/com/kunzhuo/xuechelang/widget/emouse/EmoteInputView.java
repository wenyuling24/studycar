package com.kunzhuo.xuechelang.widget.emouse;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.kunzhuo.xuechelang.AndroidApplication;
import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.view.adapter.EmoteAdapter;
import com.kunzhuo.xuechelang.widget.PageControlView;
import com.kunzhuo.xuechelang.widget.ScrollLayout;

/**
 * 表情View
 *
 * @author Administrator
 */
public class EmoteInputView extends LinearLayout {

    // private GridView mGvDisplay;
    private EmoteAdapter mDefaultAdapter;
    private EmoticonsEditText mEEtView;
    private static final float APP_PAGE_SIZE = 27;
    private ScrollLayout mScrollLayout;
    private PageControlView pageControl;
    private DataLoading dataLoad;
    public MyHandler myHandler;

    public EmoteInputView(Context context) {
        super(context);
        init();
    }

    public EmoteInputView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EmoteInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {

        inflate(getContext(), R.layout.common_emotionbar, this);

        dataLoad = new DataLoading();
        mScrollLayout = (ScrollLayout) findViewById(R.id.ScrollLayoutTest);
        myHandler = new MyHandler(getContext(), 1);

        // 起一个线程更新数据
        MyThread m = new MyThread();
        new Thread(m).start();
    }

    /**
     * gridView 的onItemLick响应事件
     */
    public OnItemClickListener listener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            String text = null;
            // text = BaseApplication.mEmoticons_Zem.get(position);
            text = (String) parent.getItemAtPosition(position);

            if (text.equals("删除")) {

                if (mEEtView.getText().toString().equals("")) {

                } else {

                    // 先判断是字符串还是表情代码 [BQ/勾引][BQ/勾引]
                    int selectionStart = mEEtView.getSelectionStart();// 获取光标的位置
                    if (selectionStart > 0) {
                        String body = mEEtView.getText().toString();
                        if (!TextUtils.isEmpty(body)) {
                            String tempStr = body.substring(0, selectionStart);
                            int i = tempStr.lastIndexOf("/");// 获取最后一个表情的位置
                            if (i != -1) {
                                CharSequence cs = tempStr.subSequence(i,
                                        selectionStart);
                                if (isBiaoqing(cs.toString())) {// 判断是否是一个表情
                                    mEEtView.getEditableText().delete(i,
                                            selectionStart);
                                    return;
                                }
                            }
                            mEEtView.getEditableText().delete(
                                    tempStr.length() - 1, selectionStart);

                        }
                    }

                    String str = mEEtView.getText().toString()
                            .substring(0, mEEtView.getText().length());// 截取内容中的最后一位

                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = str;
                    handler.sendMessage(msg); // 通知handler刷新EditText中的数据
                }

            } else {
                if (mEEtView != null && !TextUtils.isEmpty(text)) {
                    int start = mEEtView.getSelectionStart();
                    CharSequence content = mEEtView.getText().insert(start,
                            text);
                    mEEtView.setText(content);
                    // 定位光标位置
                    CharSequence info = mEEtView.getText();
                    if (info instanceof Spannable) {
                        Spannable spanText = (Spannable) info;
                        Selection.setSelection(spanText, start + text.length());
                    }
                }
            }

        }

    };

    public void setEditText(EmoticonsEditText editText) {
        mEEtView = editText;
    }

    // 更新后台数据
    class MyThread implements Runnable {
        public void run() {
            try {
                Thread.sleep(1000 * 3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String msglist = "1";
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putString("rmsg", msglist);
            msg.setData(b);
            // getContext().myHandler.sendMessage(msg); // 向Handler发送消息,更新UI
            myHandler.sendMessage(msg); // 向Handler发送消息,更新UI

        }
    }

    /**
     * 验证表情代码格式
     *
     * @param num
     * @return
     */
    public boolean isBiaoqing(String num) {

//		String idRegex = "\\[BQ/[a-zA-Z0-9\\u4e00-\\u9fa5]+\\]";
        String idRegex = "/:.{1,10}";

        if (TextUtils.isEmpty(num))
            return false;
        else
            return num.matches(idRegex);
    }

    class MyHandler extends Handler {
        private Context mContext;

        public MyHandler(Context conn, int a) {
            mContext = conn;
        }

        public MyHandler(Looper L) {
            super(L);
        }

        // 子类必须重写此方法,接受数据
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b = msg.getData();
            String rmsg = b.getString("rmsg");
            if ("1".equals(rmsg)) {
                // do nothing

                List<String> list = new ArrayList<String>();

                for (int i = 0; i < AndroidApplication.mEmoticons_Zem.size(); i++) {

                    list.add(AndroidApplication.mEmoticons_Zem.get(i));
                }

                int pageNo = (int) Math.ceil(list.size() / APP_PAGE_SIZE);

                // System.out.println(pageNo + " 页数");
                for (int i = 0; i < pageNo; i++) {
                    // GridView mGvDisplay = (GridView)
                    // findViewById(R.id.emotionbar_gv_display);
                    // 27 55 83 最后一个
                    GridView mGvDisplay = new GridView(mContext);
                    mDefaultAdapter = new EmoteAdapter(getContext(), list, i);
                    mGvDisplay.setAdapter(mDefaultAdapter);
                    mGvDisplay.setNumColumns(7);
                    mGvDisplay.setOnItemClickListener(listener);
                    mScrollLayout.addView(mGvDisplay);
                }
                // 加载分页
                pageControl = (PageControlView) findViewById(R.id.pageControl);
                pageControl.bindScrollViewGroup(mScrollLayout);
                // 加载分页数据
                dataLoad.bindScrollViewGroup(mScrollLayout);
            }
        }

    }

    // 分页数据
    class DataLoading {
        private int count;

        public void bindScrollViewGroup(ScrollLayout scrollViewGroup) {
            this.count = scrollViewGroup.getChildCount();
            scrollViewGroup
                    .setOnScreenChangeListenerDataLoad(new ScrollLayout.OnScreenChangeListenerDataLoad() {
                        public void onScreenChange(int currentIndex) {
                            // generatePageControl(currentIndex);
                        }
                    });
        }

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mEEtView.setText(msg.obj.toString());
                    mEEtView.setSelection(mEEtView.getText().length());
                    break;

                default:
                    break;
            }
        }

        ;
    };
}
