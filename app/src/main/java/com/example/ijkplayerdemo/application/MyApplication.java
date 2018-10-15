package com.example.ijkplayerdemo.application;

import android.app.Application;

/**
 * author: tonydeng
 * mail : tonydeng@hxy.com
 * 2018/10/10
 */
public class MyApplication extends Application{
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
