package com.uos_dapp.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author xuhuixiang@ulord.net
 */
public class MainActivity extends AppCompatActivity {
    /**UOS测试链地址*/
    public static String DAPP_BASE_CHAIN_URL = "https://testrpc1.uosio.org:20580/v1";

    /**
     * 官方demo地址
     */
    public static String  TEST_DAPP_DEMO_PATH="http://175.6.135.205:8011/demo/#/";

    //UOS测试账号信息
    public static String testWalletName = "jssdktester1";
    public static String testWalletPublicKey = "UOS7rS4ZYqPU2unm63z4p6ZkP8XRxAEhZXTPHrfYeD4abE6vKetVX";
    public static String testWalletPrivitelicKey = "5JCPmjsECKPxLaaLwvznEwyRVAYmo374HvS4AQqvmj97z86vbYP";

    //UOS测试账号信息备选
//    public static String testWalletName="jssdktester2";
//    public static String testWalletPublicKey="UOS5oZXMDvqKPU44ywjnLBXEHxJyKEjhyhDrLN2Rh77Z97KkNdZhz";
//    public static String testWalletPrivitelicKey="5JLnbgcxiUScFqpxk1VoSztdmhAx7H2xbRkUCxiZbDiAGYnn6A2";

    private EditText mDappUrl;
    private TextView mWalletName;
    private TextView mWalletPublic;
    private TextView mWalletPrivate;
    private Button mToDapp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    private void initView() {
        mDappUrl = (EditText) findViewById(R.id.dapp_url);
        mWalletName = (TextView) findViewById(R.id.wallet_name);
        mWalletPublic = (TextView) findViewById(R.id.wallet_public);
        mWalletPrivate = (TextView) findViewById(R.id.wallet_private);
        mToDapp = (Button) findViewById(R.id.to_dapp);
        mDappUrl.setText(TEST_DAPP_DEMO_PATH);
        mWalletName.setText(testWalletName);
        mWalletPublic.setText(testWalletPublicKey);
        mWalletPrivate.setText(testWalletPrivitelicKey);
        mToDapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DappForUosActivity.startActivity(MainActivity.this, "测试DAPP",
                        mDappUrl.getText().toString().trim()
                );
            }
        });

    }
}
