package io.eblock.eos4j.blockchain.uosmanager.net;


import java.util.concurrent.TimeUnit;

import io.eblock.eos4j.blockchain.callback.impl.DefaultCallbackImpl;
import io.eblock.eos4j.blockchain.util.Ulog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 网络请求框架移出来 统一管理
 * Created by xuhuixiang@ulord.net on 2016/9/29.
 */
public class HttpNetworkClient {

    private static final byte[] LOCKER = new byte[0];
    public final static int CONNECT_TIMEOUT = 60;
    public final static int READ_TIMEOUT = 60;
    public final static int WRITE_TIMEOUT = 60;
    OkHttpClient mOkHttpClient;
    /**
     * 以下 开8个网络请求线程池 提高网络交互速度 可以理解为是本地并发
     */
    private static HttpNetworkClient mInstance;

    /**
     * 以上 开8个网络请求线程池 提高网络交互速度 可以理解为是本地并发
     */

    private HttpNetworkClient() {
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .build();
    }


    /**
     * 单例模式获取NetUtils
     *
     * @return
     */
    public static HttpNetworkClient getInstance() {
        synchronized (LOCKER) {
            if (mInstance == null) {
                mInstance = new HttpNetworkClient();
            }
        }
        return mInstance;
    }


    /**
     * 网络交互 post json String(保证原有的不会存在异常)
     */
    public void postAsynHttpString(String url, String map, Callback b) {
        try {
            RequestBody body = FormBody.create(MediaType.parse("application/json"), map);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Ulog.i("Post Request：" + request);
            Ulog.i("Post Params：" + request);
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(b);
        } catch (Exception ex) {
            Ulog.e("NetException ex  " + ex);
        }
    }

    public void get(String url, Callback b) {

        Request request = new Request.Builder()
                .url(url)
                .build();
        Ulog.i("Get Request：" + request);
        mOkHttpClient.newCall(request).enqueue(b);
    }

    //------------------------------------------------------------------------------------
    // 下面的方法针对线程切换做了处理（回调获得的结果直接就在主线程中了）


    /**
     * 网络交互 post json String  （）
     */
    public void postAsynHttpString(String url, String map, DefaultCallbackImpl b) {
        try {
            RequestBody body = FormBody.create(MediaType.parse("application/json"), map);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Ulog.i("Post Request：" + request);
            Ulog.i("Post Params：" + map);
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(b);
        } catch (Exception ex) {
            Ulog.e("NetException ex  " + ex);
        }
    }

    /**
     * @param url
     * @param body 辅助使用RequestUtil 来构建对应的请求体，兼容不同形式的
     * @param b
     */
    public void postAsynHttpString(String url, RequestBody body, DefaultCallbackImpl b) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Ulog.i("Post Request：" + request);
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(b);
        } catch (Exception ex) {
            Ulog.e("NetException ex  " + ex);
        }
    }

    public void get(String url, DefaultCallbackImpl b) {

        Request request = new Request.Builder()
                .url(url)
                .build();
        Ulog.i("Get Request：" + request);
        mOkHttpClient.newCall(request).enqueue(b);
    }



}
