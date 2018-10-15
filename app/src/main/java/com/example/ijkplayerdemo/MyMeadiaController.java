package com.example.ijkplayerdemo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;

import com.example.ijkplayerdemo.widget.media.IMediaController;
import com.example.ijkplayerdemo.widget.media.IjkVideoView;

import java.util.Locale;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * author: tonydeng
 * mail : tonydeng@hxy.com
 * 2018/10/12
 */
public class MyMeadiaController implements IMediaController, SeekBar.OnSeekBarChangeListener {
    private View controllerView;
    private ImageView mPause;
    private AppCompatTextView mStart, mEnd;
    private AppCompatSeekBar mSeekBar;
    private MediaController.MediaPlayerControl player;
    private boolean isShow;

    // 控制器可见
    private final static int MESSAGE_VIEW_VISIABLE = 0;
    private final static int MESSAGE_VIEW_HIDE = 1 ;
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
                    break;
            }

        }
    };

    public MyMeadiaController(Context context) {
        controllerView = LayoutInflater.from(context).inflate(R.layout.layout_controller, null);

        mPause = controllerView.findViewById(R.id.pause);
        mStart = controllerView.findViewById(R.id.tv_start);
        mEnd = controllerView.findViewById(R.id.tv_end);
        mSeekBar = controllerView.findViewById(R.id.seek_bar);
        mSeekBar.setOnSeekBarChangeListener(this);

    }

    public void attchVideoView(IjkVideoView ijkVideoView) {
        ijkVideoView.addView(controllerView);
        ijkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
               // isShow = true;
               // handler.sendEmptyMessage(MESSAGE_VIEW_VISIABLE);
            }
        });

    }

    public void disAttachVideoView(IjkVideoView ijkVideoView) {
        ijkVideoView.removeAllViews();
        handler.removeMessages(MESSAGE_VIEW_VISIABLE);
        handler = null;
    }


    @Override
    public void hide() {
        handler.sendEmptyMessage(MESSAGE_VIEW_HIDE);
    }

    @Override
    public boolean isShowing() {
        return isShow;
    }

    @Override
    public void setAnchorView(View view) {

    }

    @Override
    public void setEnabled(boolean enabled) {

    }

    @Override
    public void setMediaPlayer(MediaController.MediaPlayerControl player) {
        this.player = player;
    }

    @Override
    public void show(int timeout) {

    }

    @Override
    public void show() {
        handler.sendEmptyMessage(MESSAGE_VIEW_VISIABLE);
    }

    @Override
    public void showOnce(View view) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private String formatTime(long time){
        int totalseconds = (int) (time / 1000);
        int seconds = totalseconds % 60;
        int minutes = (totalseconds / 60) % 60;
        int hours = totalseconds / 3600;
        return hours > 0 ? String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
                : String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

    }

}
