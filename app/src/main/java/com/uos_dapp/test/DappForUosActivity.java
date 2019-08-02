package com.uos_dapp.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uos_dapp.test.webview.BaseWebView;
import com.uos_dapp.test.webview.uos.UosWebChromeClient;
import com.uos_dapp.test.webview.uos.UosWebSetting;
import com.uos_dapp.test.webview.uos.UosWebViewClient;

import java.util.ArrayList;

import io.eblock.eos4j.utils.LogUtils;

/**
 * @author xuhuixiang@ulord.net
 */
public class DappForUosActivity extends AppCompatActivity implements View.OnClickListener {


    BaseWebView mWebDappDetails;

    private String title = "";
    private String url = null;
    private String accountName = "";
    private int businessType;
    ActionBar actionBar;
    private ArrayList<Integer> linkJump = new ArrayList<>();

    private DappUosInterface dappUosInterface;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dapp_uos);
        initViews();
        initView();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initViews() {
        url = getIntent().getStringExtra(PARAMS_URL);
        title = getIntent().getStringExtra(PARAMS_TITLE);
        accountName = getIntent().getStringExtra(PARAMS_ACCOUNT);
        mWebDappDetails = (BaseWebView) findViewById(R.id.web_dapp_details);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        initView();
        initWebView(accountName);
        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }


    private void initView() {
        linkJump.add(businessType);
    }



    public void initWebView(String account) {
        BaseWebView.disableAccessibility(this);
        new UosWebSetting(mWebDappDetails, DappForUosActivity.this, false);
        mWebDappDetails.getSettings().setUserAgentString("UlordUosAndroid");
        mWebDappDetails.setWebViewClient(new UosWebViewClient(this));

        // TODO 链相关的URL地址需要进行配置
        dappUosInterface = new DappUosInterface(mWebDappDetails, this,
                String.format("%s/chain/get_info", MainActivity.DAPP_BASE_CHAIN_URL),//TODO 这里的地址需要通过配置进行拼接后传递
                String.format("%s/chain/abi_json_to_bin", MainActivity.DAPP_BASE_CHAIN_URL),
                String.format("%s/chain/push_transaction", MainActivity.DAPP_BASE_CHAIN_URL),
                MainActivity.testWalletPrivitelicKey, MainActivity.testWalletName, MainActivity.testWalletPublicKey);
        mWebDappDetails.addJavascriptInterface(dappUosInterface, //UOS钱包的bean
                "DappJsBridge");
        mWebDappDetails.setWebChromeClient(new UosWebChromeClient(this, mProgressBar, actionBar, null, account));
        LogUtils.e("url:" + url);
        mWebDappDetails.loadUrl(url);
    }

    private void rollback() {
        //回滚记录，让状态栏跟着变化【在大于1时生效，保证至少有一种状态栏的样式能够被保持】
        LogUtils.e("--->" + linkJump.size());
        if (linkJump.size() > 1) {
            linkJump.remove(linkJump.size() - 1);
        }
    }

    //设置返回键动作（防止按返回键直接退出程序)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebDappDetails.canGoBack()) {//当webview不是处于第一页面时，返回上一个页面
                mWebDappDetails.goBack();
                rollback();
                return true;
            } else {//当webview处于第一页面时,直接退出程序
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    public void onPause() {
        super.onPause();
        mWebDappDetails.onPause();
        mWebDappDetails.pauseTimers();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebDappDetails.resumeTimers();
        mWebDappDetails.onResume();
    }

    @Override
    protected void onDestroy() {
        mWebDappDetails.destroy();
        mWebDappDetails = null;
        super.onDestroy();
    }

    //保持与 原DappActivity统一，
    private static final String PARAMS_TITLE = "title";
    private static final String PARAMS_URL = "url";
    private static final String PARAMS_ACCOUNT = "account";


    public static void startActivity(@NonNull Context context, @NonNull String title, @NonNull String url) {
        Intent intent = new Intent(context, DappForUosActivity.class);
        intent.putExtra(PARAMS_TITLE, title);
        intent.putExtra(PARAMS_URL, url);
        context.startActivity(intent);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }
}
