package com.uos_dapp.test.webview.uos;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by xuhuixiang@ulord.net on 2018/4/11.
 */

public class UosWebSetting {
    WebView mBaseWebView;
    Context mContext;
    boolean isCache =true;
    public UosWebSetting(WebView baseWebView, Context context, boolean isCache) {
        mBaseWebView = baseWebView;
        mContext = context;
        this.isCache = isCache;
        initWebSettings();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initWebSettings() {
        WebSettings webSettings = mBaseWebView.getSettings();
        if (webSettings == null) return;
        //设置字体缩放倍数，默认100
        webSettings.setTextZoom(100);
        // 支持 Js 使用
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(true);
        // 开启DOM缓存
        webSettings.setDomStorageEnabled(true);
        // 开启数据库缓存
        webSettings.setDatabaseEnabled(true);
        // 支持自动加载图片
        webSettings.setLoadsImagesAutomatically(hasKitkat());
        // 自适应屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);


        if (isCache) {
            // 设置 WebView 的缓存模式
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            // 支持启用缓存模式
            webSettings.setAppCacheEnabled(true);
            // 设置 AppCache 最大缓存值(现在官方已经不提倡使用，已废弃)
            webSettings.setAppCacheMaxSize(8 * 1024 * 1024);
            // Android 私有缓存存储，如果你不调用setAppCachePath方法，WebView将不会产生这个目录
            webSettings.setAppCachePath(mContext.getCacheDir().getAbsolutePath());
        }else {
            // 设置 WebView 的缓存模式
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            // 支持启用缓存模式
            webSettings.setAppCacheEnabled(false);
        }
        // 数据库路径
        if (!hasKitkat()) {
            webSettings.setDatabasePath(mContext.getDatabasePath("html").getPath());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        } else {
            try {
                Class<?> clazz = webSettings.getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", new Class[] {Boolean.TYPE});
                if(method != null) {
                    method.invoke(webSettings, new Object[] {Boolean.valueOf(true)});
                }
            } catch(NoSuchMethodException e) {
                e.printStackTrace();
            } catch(InvocationTargetException e) {
                e.printStackTrace();
            } catch(IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        // 关闭密码保存提醒功能
        webSettings.setSavePassword(false);
        // 支持缩放
        webSettings.setSupportZoom(false);
        // 隐藏原声缩放控件
        webSettings.setDisplayZoomControls(false);
        // 支持内容重新布局
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.supportMultipleWindows();
        webSettings.setSupportMultipleWindows(true);
        // 支持插件
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 设置 UserAgent 属性
        webSettings.setUserAgentString("PocketUosAndroid");
        // 允许加载本地 html 文件/false
        webSettings.setAllowFileAccess(true);
        // 允许通过 file url 加载的 Javascript 读取其他的本地文件,Android 4.1 之前默认是true，在 Android 4.1 及以后默认是false,也就是禁止
        webSettings.setAllowFileAccessFromFileURLs(true);
        // 允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源，
        // Android 4.1 之前默认是true，在 Android 4.1 及以后默认是false,也就是禁止
        // 如果此设置是允许，则 setAllowFileAccessFromFileURLs 不起做用
//        webSettings.setAllowUniversalAccessFromFileURLs(false);
        //同步请求图片
        webSettings.setBlockNetworkImage(false);
        // 设定编码格式
        webSettings.setDefaultTextEncodingName("UTF-8");

    }

    private static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= 19;
    }
}
