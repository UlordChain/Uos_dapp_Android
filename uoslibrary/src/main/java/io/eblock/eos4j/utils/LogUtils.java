package io.eblock.eos4j.utils;

import android.util.Log;


/**
 * Log统一管理类
 * Created by xuhuixiang@ulord.net on 2015/10/19 0019.
 */
public class LogUtils {

    private LogUtils() { throw new UnsupportedOperationException("cannot be instantiated"); }

    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
    private static final String TAG = "UOS_DAPP_AndroidRuntime";

//    /**
//     * 默认tag的函数
//     * @param msg 打印信息
//     */
    public static void v(String msg) { if (isDebug) Log.v(TAG, msg); }
    public static void d(String msg) { if (isDebug) Log.d(TAG, msg); }
    public static void i(String msg) { if (isDebug)
        Log.i(TAG, msg);
//        System.out.println(msg);
    }
    public static void w(String msg) { if (isDebug) Log.w(TAG, msg); }
    public static void e(String msg) {
        if (isDebug) {
            if (msg.length() > 4000) {
                Log.e(TAG, "sb.length = " + msg.length());
                int chunkCount = msg.length() / 4000;     // integer division
                for (int i = 0; i <= chunkCount; i++) {
                    int max = 4000 * (i + 1);

                    if (max >= msg.length()) {
                        Log.e(TAG, "XHXchunk " + i + " of " + chunkCount + ":" + msg.substring(4000 * i));
                    } else {
                        Log.e(TAG, "XHXchunk " + i + " of " + chunkCount + ":" + msg.substring(4000 * i, max));
                    }
                }
            } else {
                Log.e(TAG,"XHX"+ msg.toString());
            }

        }

    }
    /**
     * 默认tag的函数
     * @param msg 打印信息
     */
//    public static void v(String msg) {
//        if (isDebug)
//            System.out.println(msg);
//    }
//    public static void d(String msg) {
//        if (isDebug)
//            System.out.println(msg);
//    }
//    public static void i(String msg) {
//        if (isDebug)
//            System.out.println(msg);
//    }
//    public static void w(String msg) {
//        if (isDebug)
//            System.out.println(msg);
//    }
//    public static void e(String msg) {
//        if (isDebug) {
//            System.out.println(msg);
//
//        }
//
//    }


    /**
     * 自定义lag的函数
     * @param tag tag
     * @param msg 打印信息
     */
    public static void v(String tag, String msg) { if (isDebug) Log.v(tag, msg); }
    public static void d(String tag, String msg) { if (isDebug) Log.d(tag, msg); }
    public static void i(String tag, String msg) { if (isDebug) Log.i(tag, msg); }
    public static void w(String tag, String msg) { if (isDebug) Log.w(tag, msg); }
    public static void e(String tag, String msg) { if (isDebug) Log.e(tag, msg); }

    /**
     * 自定义lag的函数
     * @param clazz 类
     * @param msg 打印信息
     */
    public static void v(Class<?> clazz,String msg) { if(isDebug) Log.v(clazz.getSimpleName(), msg); }
    public static void d(Class<?> clazz,String msg) { if(isDebug) Log.d(clazz.getSimpleName(), msg); }
    public static void i(Class<?> clazz,String msg) { if(isDebug) Log.i(clazz.getSimpleName(), msg); }
    public static void w(Class<?> clazz,String msg) { if(isDebug) Log.w(clazz.getSimpleName(), msg); }
    public static void e(Class<?> clazz,String msg) { if(isDebug) Log.e(clazz.getSimpleName(), msg); }

}