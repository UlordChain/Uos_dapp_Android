package io.eblock.eos4j.blockchain.callback.impl;

import android.os.Handler;


import java.io.IOException;

import io.eblock.eos4j.blockchain.callback.IResultCallback;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author by huangzhou@ulord.net
 * @package com.thgy.uos.net.impl
 * @create 2019-04-16 11:40
 * @Description
 */

public abstract class DefaultCallbackImpl implements IResultCallback {

    private Handler mHandler = new Handler();

    @Override
    public void onFailure(final Call call,final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                requestFail(
                        (call != null && call.request() != null && call.request().url() != null) ?
                                call.request().url().toString() : "", e != null ? e.getMessage() : "IOException");
                mHandler.removeCallbacksAndMessages(null);
            }
        });

    }

    @Override
    public void onResponse(final Call call,final Response response) throws IOException {
        if (response != null && response.body() != null) {
            final String result = response.body().string();//这里是子线程
            mHandler.post(new Runnable() {
                @Override
                public void run() {//使用Handler转移到主线程中
                    if (result != null && result.length() > 0) {
                        requestSuccess(result);
                    } else {
                        requestFail(
                                (call != null && call.request() != null && call.request().url() != null) ?
                                        call.request().url().toString() : "", "Get Empty Result!");
                    }
                    mHandler.removeCallbacksAndMessages(null);
                }
            });
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    requestFail(
                            (call != null && call.request() != null && call.request().url() != null) ?
                                    call.request().url().toString() : "", "Get Null Response!");
                    mHandler.removeCallbacksAndMessages(null);
                }
            });
        }
    }
}
