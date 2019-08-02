package io.eblock.eos4j.utils;

import java.util.concurrent.TimeUnit;

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
    /**以下 开8个网络请求线程池 提高网络交互速度 可以理解为是本地并发*/
    private static HttpNetworkClient mInstance;

    /**以上 开8个网络请求线程池 提高网络交互速度 可以理解为是本地并发*/

    private HttpNetworkClient() {
        mOkHttpClient= new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .build();
    }


    /**
     * 单例模式获取NetUtils
     * @return
     */
    public static   HttpNetworkClient getInstance() {
//        if (mInstance == null) {
            synchronized (LOCKER) {
                if (mInstance == null) {
                    mInstance = new HttpNetworkClient();
                }
            }
//        }
        return mInstance;

    }


    /**
     * 网络交互 post json String
     */
    public void postAsynHttpString(String url, String map,Callback b) {

        try {
//            String pram = new JSONObject(map).toString();
            LogUtils.i("XHXJSON参数：" + map);
            RequestBody body = FormBody.create(MediaType.parse("application/json"), map);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            LogUtils.i("request：" + request);

            Call call = mOkHttpClient.newCall(request);
            call.enqueue(b);
        } catch (Exception ex) {
            System.out.println("网络Exception ex  " + ex);

        }
    }

    public void get(String url, Callback b) {

        Request request = new Request.Builder()
                .url(url)
                .build();
        LogUtils.i("request：" + request);

        mOkHttpClient.newCall(request).enqueue(b);
    }

}
