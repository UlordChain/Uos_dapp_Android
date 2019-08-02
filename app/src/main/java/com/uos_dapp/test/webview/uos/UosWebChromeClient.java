package com.uos_dapp.test.webview.uos;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.eblock.eos4j.utils.LogUtils;

/**
 * Created by xuhuixiang@ulord.net on 2018/4/11.
 */

public class UosWebChromeClient extends WebChromeClient {
    Context mContext;
    ProgressBar mProgressBar;
    ActionBar mTextView;

    //暂时没有下面两个参数的使用地方，如果后续需要，针对这个类进行修正
    String mInfo = null;
    String mAccount = null;


    public UosWebChromeClient(Context context, ProgressBar progress, ActionBar textView, String info, String account) {
        this.mContext = context;
        this.mProgressBar = progress;
        this.mTextView = textView;
        this.mInfo = info;
        this.mAccount = account;
    }

    /**
     * 当前 WebView 加载网页进度
     *
     * @param view
     * @param newProgress
     */
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        setProgress(newProgress);
    }

    private void setProgress(int progress){
        if(mProgressBar!=null){
            if(progress == 100) {
                mProgressBar.setVisibility(View.GONE);
            }else{
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(progress);
            }
        }
    }

    /**
     * 接收web页面的 Title
     *
     * @param view
     * @param title
     */
    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        setTitle(title);
    }

    private void setTitle(String title){
        if(mTextView!=null){
            mTextView.setTitle(title);
        }
    }

    /**
     * 接收web页面的icon
     *
     * @param view
     * @param icon
     */
    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
    }

    /**
     * Js 中调用 alert() 函数，产生的对话框
     *
     * @param view
     * @param url
     * @param message
     * @param result
     * @return
     */
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }

    /**
     * 处理 Js 中的 Confirm 对话框
     *
     * @param view
     * @param url
     * @param message
     * @param result
     * @return
     */
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return super.onJsConfirm(view, url, message, result);
    }

    /**
     * 处理 JS 中的 Prompt对话框
     *
     * @param view
     * @param url
     * @param message
     * @param defaultValue
     * @param result
     * @return
     */
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        LogUtils.w(String.format("------console------\nlevel:-->%s<--\nmsg:-->%s<--\nlineNumber:-->%s<--\nsourceId:-->%s<--\n--------",
                consoleMessage.messageLevel().name(),
                consoleMessage.message(),
                consoleMessage.lineNumber()+"",
                consoleMessage.sourceId()));
        return super.onConsoleMessage(consoleMessage);
    }

}