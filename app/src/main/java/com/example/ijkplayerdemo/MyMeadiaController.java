package com.example.ijkplayerdemo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;

import com.example.ijkplayerdemo.widget.media.IMediaController;
import com.example.ijkplayerdemo.widget.media.IjkVideoView;

import java.util.Formatter;
import java.util.Locale;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * author: tonydeng
 * mail : tonydeng@hxy.com
 * 2018/10/12
 */
public class MyMeadiaController implements IMediaController, SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private static final String TAG = "MyMeadiaController";
    private View controllerView;
    private ImageView mPause, mPlay;
    private AppCompatTextView mStart, mEnd;
    private AppCompatSeekBar mSeekBar;
    private MediaController.MediaPlayerControl player;
    private boolean isShow;
    private PlayStatusListner mPlayStatusListner;

    //是否继续播放视频
    private boolean isPlay;

    // 控制器可见
    private final static int MESSAGE_VIEW_VISIABLE = 0;
    private final static int MESSAGE_VIEW_HIDE = 1;
    // 更新进度
    private final static int MESSAGE_PROGRESS = 2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_VIEW_VISIABLE:
                    controllerView.setVisibility(View.VISIBLE);
                    isShow = true;
                    break;
                case MESSAGE_VIEW_HIDE:
                    controllerView.setVisibility(View.GONE);
                    isShow = false;
                    break;
                case MESSAGE_PROGRESS:
                    setProgress();
                    break;
            }

        }
    };

    public interface PlayStatusListner {
        void onPlaying();
        void onPlayPause();
        void onPlayResume();
    }

    public MyMeadiaController(Context context) {
        controllerView = LayoutInflater.from(context).inflate(R.layout.layout_controller, null);

        mPlay = controllerView.findViewById(R.id.iv_play);
        mPause = controllerView.findViewById(R.id.pause);
        mStart = controllerView.findViewById(R.id.tv_start);
        mEnd = controllerView.findViewById(R.id.tv_end);
        mSeekBar = controllerView.findViewById(R.id.seek_bar);
        mSeekBar.setOnSeekBarChangeListener(this);
        mPlay.setOnClickListener(this);
        mPause.setOnClickListener(this);

    }

    public void setmPlayStatusListner(PlayStatusListner mPlayStatusListner) {
        this.mPlayStatusListner = mPlayStatusListner;
    }

    public void attchVideoView(IjkVideoView ijkVideoView) {
        ijkVideoView.addView(controllerView);
        ijkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                Log.d(TAG, "onPrepared");

            }
        });

        ijkVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                Log.d(TAG, "onCompletion");
            }
        });

    }

    public void disAttachVideoView(IjkVideoView ijkVideoView) {
        ijkVideoView.removeAllViews();
        handler.removeMessages(MESSAGE_VIEW_VISIABLE);
        handler = null;
    }

    Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "player.isPlaying():" + player.isPlaying());
            if (player.isPlaying()) {
                setProgress();
                handler.postDelayed(this, 1000);
            }else {
                playstop();
            }
        }
    };

    // 播放结束
    private void playstop(){
        Log.d(TAG, "playstop");
        int  currentPostion = player.getCurrentPosition();
        int allduration = player.getDuration();
        Log.d(TAG, "currentPostion:" + currentPostion);
        Log.d(TAG, "allduration:" + allduration);
        if(currentPostion == allduration){
            handler.removeCallbacks(progressRunnable);
        }
    }


    private void setProgress() {
        long postion = player.getCurrentPosition();
        int pos = (int) postion;
        Log.d(TAG, "pos:" + pos);

        mSeekBar.setProgress(pos);
        int precent = player.getBufferPercentage();
        mSeekBar.setSecondaryProgress(precent);

        String starttime = formatTime((int) postion);
        mStart.setText(starttime);
    }

    private void setEndTime() {
        int duration = player.getDuration();
        Log.d(TAG, "duration:" + duration);
        String time = formatTime(duration);
        mEnd.setText(time);
        mSeekBar.setMax(duration);
    }

    @Override
    public void hide() {
        Log.d(TAG, "hide");
        handler.sendEmptyMessage(MESSAGE_VIEW_HIDE);
    }

    @Override
    public boolean isShowing() {
        return isShow;
    }

    @Override
    public void setAnchorView(View view) {
        Log.d(TAG, "setAnchorView");
    }

    @Override
    public void setEnabled(boolean enabled) {
        Log.d(TAG, "setEnabled");
    }

    @Override
    public void setMediaPlayer(MediaController.MediaPlayerControl player) {
        this.player = player;
    }

    @Override
    public void show(int timeout) {
        Log.d(TAG, "timeout:" + timeout);
    }

    @Override
    public void show() {
        Log.d(TAG, "show");
        handler.sendEmptyMessage(MESSAGE_VIEW_VISIABLE);
    }

    @Override
    public void showOnce(View view) {
        Log.d(TAG, "showOnce");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d(TAG, "progress:" + progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private String formatTime(int time) {
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = (int) (time / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play:
                if (!isPlay) {
                    mPlay.setVisibility(View.GONE);
                    mPause.setVisibility(View.VISIBLE);
                    if (mPlayStatusListner != null) {
                        mPlayStatusListner.onPlaying();
                    }

                    setEndTime();
                    handler.postDelayed(progressRunnable, 1000);
                } else {
                    Log.d(TAG, "onPlayResume");
                    mPlayStatusListner.onPlayResume();
                }
                break;
            case R.id.pause:
                isPlay = true;
                mPlay.setVisibility(View.VISIBLE);
                mPause.setVisibility(View.GONE);
                if(mPlayStatusListner != null){
                    mPlayStatusListner.onPlayPause();
                }
                break;
        }
    }
}
