/*
 * Copyright (C) 2013 yixia.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunzhuo.xuechelang.video;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kunzhuo.xuechelang.R;
import com.kunzhuo.xuechelang.utils.LogUtils;
import com.kunzhuo.xuechelang.utils.ToastUtil;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.utils.StringUtils;
import io.vov.vitamio.widget.VideoView;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 * 播放在线视频流界面
 */
public class VideoViewActivity extends Activity {
    public static final String VIDEO_PATH = "videoName";
    public static final String VIDEO_PATH2 = "videoName2";

    private Context context;
    private VideoView mVideoView;
    private RelativeLayout idWrapLayout;
    private LinearLayout mLoadingLayout;
    private ImageView mLoadingImg;
    private ObjectAnimator mOjectAnimator;
    /**
     * 当前进度
     */
    private Long currentPosition = (long) 0;
    private String mVideoPath = "";
    private String mVideoPath2 = "";
    /**
     * setting
     */
    private boolean needResume;

    /**
     * 视频播放控制界面
     */
    CustomMediaController mediaController;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        context = this;
        if (!LibsChecker.checkVitamioLibs(this)) return;
        setContentView(R.layout.video_play_layout);
        getDataFromIntent();
        initviews();
        initTanMuViews();
        initVideoSettings();
    }

    /**
     * 初始化弹幕相关
     */
    private void initTanMuViews() {
        mediaController = new CustomMediaController(this);
    }

    private void getDataFromIntent() {
        Intent Intent = getIntent();
        if (Intent != null && Intent.getExtras().containsKey(VIDEO_PATH)) {
            mVideoPath = Intent.getExtras().getString(VIDEO_PATH);
//            ToastUtil.show(context, mVideoPath + " 传过来的地址");
            mVideoPath2 = Intent.getExtras().getString(VIDEO_PATH2);

        }
    }

    private void initviews() {
        idWrapLayout = (RelativeLayout) findViewById(R.id.id_WrapLayout);
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        mLoadingLayout = (LinearLayout) findViewById(R.id.loading_LinearLayout);
        mLoadingImg = (ImageView) findViewById(R.id.loading_image);
    }

    private void initVideoSettings() {
        mVideoView.requestFocus();
        mVideoView.setBufferSize(1024 * 1024);
        mVideoView.setVideoChroma(MediaPlayer.VIDEOCHROMA_RGB565);
        mVideoView.setVideoPath(mVideoPath2);
        mVideoView.setMediaController(mediaController);
        mVideoView.getHolder().setFixedSize(360, 240);  //设置分辨率

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        int h = (int) (screenWidth / 1.7);

//        int w = DensityUtil.dip2px(context, screenWidth);
//        int h = DensityUtil.dip2px(context, screenHeight / 3);

//        ToastUtil.show(context, screenWidth + " 宽度 " + h + " 高度");

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(screenWidth, h);
        idWrapLayout.setLayoutParams(lp);
    }

    public void onResume() {
        super.onResume();
        preparePlayVideo();

    }

    private void preparePlayVideo() {
        startLoadingAnimator();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // TODO Auto-generated method stub
                stopLoadingAnimator();

                if (currentPosition > 0) {
                    mVideoView.seekTo(currentPosition);
                } else {
                    mediaPlayer.setPlaybackSpeed(1.0f);
                }
                startPlay();
            }
        });
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
                switch (arg1) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        //开始缓存，暂停播放
                        LogUtils.i(LogUtils.LOG_TAG, "开始缓存");
                        startLoadingAnimator();
                        if (mVideoView.isPlaying()) {
                            stopPlay();
                            needResume = true;
                        }
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        //缓存完成，继续播放
                        stopLoadingAnimator();
                        if (needResume) startPlay();
                        LogUtils.i(LogUtils.LOG_TAG, "缓存完成");
                        break;
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        //显示 下载速度
                        LogUtils.i("download rate:" + arg2);
                        break;
                }
                return true;
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                ToastUtil.show(context, " 播放结束");
                mVideoView.setVideoPath(mVideoPath);
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
//                LogUtils.i(LogUtils.LOG_TAG, "what=" + what);
                return false;
            }
        });
        mVideoView.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
            }
        });
        mVideoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                LogUtils.i(LogUtils.LOG_TAG, "percent" + percent);
            }
        });

        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH); // 高画质
        // VIDEOQUALITY_LOW
        // 流畅
        // VIDEOQUALITY_MEDIUM
        // 普通
    }

    @NonNull
    private void startLoadingAnimator() {
        if (mOjectAnimator == null) {
            mOjectAnimator = ObjectAnimator.ofFloat(mLoadingImg, "rotation", 0f, 360f);
        }
        mLoadingLayout.setVisibility(View.VISIBLE);

        mOjectAnimator.setDuration(1000);
        mOjectAnimator.setRepeatCount(-1);
        mOjectAnimator.start();
    }

    private void stopLoadingAnimator() {
        mLoadingLayout.setVisibility(View.GONE);
        mOjectAnimator.cancel();
    }

    private void startPlay() {
        mVideoView.start();
    }

    private void stopPlay() {
        mVideoView.pause();
    }

    public void onPause() {
        super.onPause();
        currentPosition = mVideoView.getCurrentPosition();
        mVideoView.pause();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        //切换为竖屏

        if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_PORTRAIT) {

            VideoViewActivity.this.setVideoPageSize(1);
//            T.showShort(context, "竖屏");

        }

        //切换为横屏

        else if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_LANDSCAPE) {

            VideoViewActivity.this.setVideoPageSize(2);
            mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
//            T.showShort(context, "横屏");
        }

    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.stopPlayback();
            mVideoView = null;
        }
    }

    /**
     * 获取视频当前帧
     *
     * @return
     */
    public Bitmap getCurrentFrame() {
        if (mVideoView != null) {
            MediaPlayer mediaPlayer = mVideoView.getmMediaPlayer();
            return mediaPlayer.getCurrentFrame();
        }
        return null;
    }

    /**
     * 快退(每次都快进视频总时长的1%)
     */
    public void speedVideo() {
        if (mVideoView != null) {
            long duration = mVideoView.getDuration();
            long currentPosition = mVideoView.getCurrentPosition();
            long goalduration = currentPosition + duration / 10;
            if (goalduration >= duration) {
                mVideoView.seekTo(duration);
            } else {
                mVideoView.seekTo(goalduration);
            }
            ToastUtil.showToastMsgShort(this, StringUtils.generateTime(goalduration));
        }
    }

    /**
     * 快退(每次都快退视频总时长的1%)
     */
    public void reverseVideo() {
        if (mVideoView != null) {
            long duration = mVideoView.getDuration();
            long currentPosition = mVideoView.getCurrentPosition();
            long goalduration = currentPosition - duration / 10;
            if (goalduration <= 0) {
                mVideoView.seekTo(0);
            } else {
                mVideoView.seekTo(goalduration);
            }
            ToastUtil.showToastMsgShort(this, StringUtils.generateTime(goalduration));
        }
    }

    /**
     * 设置屏幕的显示大小
     */
    public void setVideoPageSize(int currentPageSize) {
        if (mVideoView != null) {
            mVideoView.setVideoLayout(currentPageSize, 0);
        }
    }
}
