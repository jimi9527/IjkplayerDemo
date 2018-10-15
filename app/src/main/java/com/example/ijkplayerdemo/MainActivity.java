package com.example.ijkplayerdemo;

import android.Manifest;
import android.content.Intent;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.ijkplayerdemo.permission.FloatWindowManager;
import com.example.ijkplayerdemo.widget.media.IjkVideoView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static int REQUEST_CODE = 1000;
    private final static String PATH_VIDEO = "http://mov.bn.netease.com/open-movie/nos/flv/2017/01/03/SC8U8K7BC_hd.flv";
    private IjkVideoView mIjkVideoView;
    private ImageView mPlay, mPasue;
    private ProgressBar mProgress;
    private FloatView floatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {

        mPasue = findViewById(R.id.iv_pause);
        mPlay = findViewById(R.id.iv_play);
        mPlay.setOnClickListener(this);
        mProgress = findViewById(R.id.pb_num);

        mIjkVideoView = findViewById(R.id.videoview);
        mIjkVideoView.setVideoURI(Uri.parse(PATH_VIDEO));
        mIjkVideoView.setOnClickListener(this);
        MyMeadiaController myMeadiaController = new MyMeadiaController(this);
        mIjkVideoView.setMediaController(myMeadiaController);
        myMeadiaController.attchVideoView(mIjkVideoView);


     /*   IjkVideoView ijkVideoView = new IjkVideoView(this);
        floatView = new FloatView(this, 0 , 0);
        floatView.removeAllViews();
        floatView.addView(ijkVideoView);*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == REQUEST_CODE){
            floatView.addToWindow();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.videoview:
                break;
            case R.id.iv_play:
                mPlay.setVisibility(View.GONE);
                mIjkVideoView.start();
                break;
            case R.id.iv_pause:

                break;
        }
    }


}
