package com.example.ijkplayerdemo;

import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.ijkplayerdemo.application.MyApplication;
import com.example.ijkplayerdemo.widget.media.IjkVideoView;

/**
 * author: tonydeng
 * mail : tonydeng@hxy.com
 * 2018/10/10
 */
public class PIPManager {
    private static PIPManager instance;
    private IjkVideoView ijkVideoView;
    private FloatView floatView;
    private boolean isShowing;
    private int mPlayingPosition = -1;

    private PIPManager(){
        ijkVideoView = new IjkVideoView(MyApplication.getInstance());
        floatView = new FloatView(MyApplication.getInstance(), 0, 0);
    }

    public static PIPManager getInstance(){
        if(instance == null){
            synchronized (PIPManager.class){
                if(instance == null){
                    instance = new PIPManager();
                }
            }
        }
        return instance;
    }

    public IjkVideoView getIjkVideoView() {
        return ijkVideoView;
    }

    public void startFloatWindow(){
        if(isShowing) return;
        removePlayerFormParent();
        floatView.addView(ijkVideoView);
        floatView.addToWindow();
        isShowing = true;
    }
    public void stopFloatWindow(){
        if(!isShowing) return;
        floatView.removeFromWindow();
        removePlayerFormParent();
    }
    private void removePlayerFormParent(){
        ViewParent parent = ijkVideoView.getParent();
        if(parent != null && parent instanceof ViewGroup){
            ((ViewGroup) parent).removeView(ijkVideoView);
        }
    }

    public int getmPlayingPosition() {
        return mPlayingPosition;
    }

    public void setmPlayingPosition(int mPlayingPosition) {
        this.mPlayingPosition = mPlayingPosition;
    }
}
