package com.example.ijkplayerdemo;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.ijkplayerdemo.permission.FloatWindowManager;
import com.example.ijkplayerdemo.widget.media.IMediaController;
import com.example.ijkplayerdemo.widget.media.IjkVideoView;

import java.util.List;


/**
 * author: tonydeng
 * mail : tonydeng@hxy.com
 * 2018/10/10
 */
public class PipListActivity extends AppCompatActivity implements FloatRecyclerViewAdapter.OnChildViewClickListener, View.OnClickListener{
    private final static String TAG = "PipListActivity";
    private FrameLayout mFlPlayer;
    private ImageView mPlay, mPause, mthumb;
    private List<VideoBean> mVideoList;
    private IjkVideoView mIjkVideoView;
    private FloatView mFloatView;
    private boolean isResume;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        initView();
    }

    private void initView() {
        mIjkVideoView = new IjkVideoView(this);
        MyMeadiaController myMeadiaController = new MyMeadiaController(this);
        mIjkVideoView.setMediaController(myMeadiaController);
        myMeadiaController.attchVideoView(mIjkVideoView);
        mFloatView = new FloatView(this, 0 , 0);

        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mVideoList = DataUtil.getVideoList();
        FloatRecyclerViewAdapter floatRecyclerViewAdapter = new FloatRecyclerViewAdapter(mVideoList, this);
        floatRecyclerViewAdapter.setOnChildViewClickListener(this);
        recyclerView.setAdapter(floatRecyclerViewAdapter);
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

                FrameLayout flplayer = view.findViewById(R.id.player_container);
                ImageView play = view.findViewById(R.id.play);
                ImageView pause = view.findViewById(R.id.pause);
                ImageView thumb = view.findViewById(R.id.thumb);

                int postion = (int) flplayer.getTag(R.id.key_position);
                if(postion == 0){
                    Log.d(TAG, "onChildViewAttachedToWindow");
                    Log.d(TAG, "postion:" + postion);
                    mFlPlayer = flplayer;
                    mPlay = play;
                    mPause = pause;
                    mthumb = thumb;
                    hideFloatView();
                    mIjkVideoView.setVideoURI(Uri.parse(mVideoList.get(postion).getUrl()));
                    mFlPlayer.addView(mIjkVideoView);
                }

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {

                FrameLayout flplayer = view.findViewById(R.id.player_container);
                ImageView play = view.findViewById(R.id.play);
                ImageView pause = view.findViewById(R.id.pause);
                ImageView thumb = view.findViewById(R.id.thumb);

                if (flplayer == null || play == null || pause == null) return;
                int position = (int) flplayer.getTag(R.id.key_position);
                if (position == 0) {
                    Log.d(TAG, "onChildViewDetachedFromWindow");
                    Log.d(TAG, "postion:" + position);
                    mFlPlayer = flplayer;
                    mPlay = play;
                    mPause = pause;
                    mthumb = thumb;

                    if(mIjkVideoView.isPlaying()){
                        addFloatView();
                    }

                }
            }
        });


        mIjkVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPause();
            }
        });
    }

    // 添加浮窗
    private void addFloatView(){
        removePlayerFormParent();
        mFloatView.addView(mIjkVideoView);
        if(FloatWindowManager.getInstance().checkPermission(PipListActivity.this)){
            mFloatView.addToWindow();
        }else {
            FloatWindowManager.getInstance().applyPermission(PipListActivity.this);
        }
    }

    // 隐藏浮窗
    private void hideFloatView(){
        removePlayerFormParent();
        mFloatView.removeAllViews();
        mFloatView.removeFromWindow();
    }
    private void removePlayerFormParent() {
        ViewParent parent = mIjkVideoView.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(mIjkVideoView);
        }
    }

    @Override
    public void onChildViewClick(View itemView, ImageView mplay, int position) {
        if(isResume){
            mIjkVideoView.resume();
        }else {
            hideplay();
            mIjkVideoView.start();
        }

    }

    // 隐藏播放控件
    private void hideplay(){
        mPlay.setVisibility(View.GONE);
        mthumb.setVisibility(View.GONE);
    }

    // 显示暂停按钮
    private void showPause(){
        mPlay.setVisibility(View.GONE);
        mPause.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

    }
}
