package com.uos_dapp.test;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.eblock.eos4j.blockchain.callback.INetResultCallback;
import io.eblock.eos4j.blockchain.uosmanager.UOSTransactionsManager;
import io.eblock.eos4j.utils.LogUtils;

/**
 * Created by xuhuixiang@ulord.net on 2018/4/25.
 */
public class DappUosInterface {

    private WebView mBaseWebView;
    private DappForUosActivity mContext;
    String mPrivateKey = "";
    String mPublicKey = "";
    String mAccount = "";

    private String getInfoUrl;
    private String getAbiJsonToBinUrl;
    private String pushTransactionUrl;

    public DappUosInterface(WebView baseWebView, DappForUosActivity context, String getInfoUrl, String getAbiJsonToBinUrl, String pushTransactionUrl, String privateKey,String walletName,String pubKey) {
        mBaseWebView = baseWebView;
        mContext = context;
        this.getInfoUrl = getInfoUrl;
        this.getAbiJsonToBinUrl = getAbiJsonToBinUrl;
        this.pushTransactionUrl = pushTransactionUrl;

        mPrivateKey =privateKey;//UOS私钥
        mAccount = walletName;//UOS账户名
        mPublicKey = pubKey;//公钥
        LogUtils.i(String.format("accountName：-->%s<--\nprivateKey：-->%s<--\npublicKey：-->%s<--\n",
                mAccount,
                mPrivateKey,
                mPublicKey));
    }

    @JavascriptInterface
    public void pushMessage(final String serialNumber, String params, String methodName) {//TODO DAPP交互的入口 js调用的方法
        LogUtils.i(String.format("DAPP交互原始请求数据：serialNumber:-->%s<--\nparams:-->%s<--\nmethodName:-->%s<--\n", serialNumber, params, methodName));
        if ("getUosAccount".equals(methodName)) {
            getUosAccount(serialNumber, params);
        } else if ("pushActions".equals(methodName)) {
            pushActions(serialNumber, params, methodName);
        } else if ("pushTransfer".equals(methodName)) {
            pushTransfer(serialNumber, params, methodName);
        } else if ("requestSignature".equals(methodName)) {
            requestSignature(serialNumber, params, methodName);
        } else if("turnToTxPage".equals(methodName)){
            turnToTxPage(serialNumber, params, methodName);
        }
    }

    /**
     * 将DAPP发送的获取用户信息回调回去
     * @param serialNumber
     * @param params
     */
    private void getUosAccount(String serialNumber, String params) {
        final HashMap<String, String> result = new HashMap<>();
        result.put("authority", "owner");//TODO 也可以是active 根据权限传
        result.put("blockchain", "uos");//默认给的
        result.put("name", mAccount);//TODO 这里是变量，传入当前的这个UOS账户的名称
        result.put("publicKey", mPublicKey);//TODO 这里是变量，传入档期这个UOS账户的公钥
        onSuccess(serialNumber, new Gson().toJson(result));
    }

    /**
     * 推送一条信息到链上
     * @param serialNumber
     * @param params
     * @param methodName
     */
    private void pushActions(final String serialNumber, String params, String methodName) {
        try {
            JSONObject paramsJson = new JSONObject(params);

            JSONObject options = paramsJson.getJSONObject("options");

            String[] permission = new String[]{paramsJson.getJSONArray("authorization").getString(0)};

            new UOSTransactionsManager(getInfoUrl,
                    getAbiJsonToBinUrl,
                    pushTransactionUrl,
                    4,
                    options.toString(),
                    permission,
                    mPrivateKey,
                    new INetResultCallback() {
                        @Override
                        public void success(final String result) {
                            if (result != null && result.contains("transaction_id")) {//确保交易成功了
                                onSuccess(serialNumber, result);
                            } else {
                                onFail(serialNumber,result);
                            }
                        }

                        @Override
                        public void fail(int businessCode, String errorMsg) {
                            onFail(serialNumber, errorMsg);
                        }
                    }).
                    doTransaction(paramsJson.getString("transfer_contract"),
                            paramsJson.getString("action"));
        } catch (JSONException e) {
            e.printStackTrace();
            onFail(serialNumber, e != null ? e.getMessage() : "json error");
        }
    }

    /**
     * 转账一笔交易到UOS链上
     * @param serialNumber
     * @param params
     * @param methodName
     */
    private void pushTransfer(final String serialNumber, String params, String methodName) {
        try {
            JSONObject paramsJson = new JSONObject(params);

            JSONObject options = paramsJson.getJSONObject("options");

            String[] permission = new String[]{paramsJson.getJSONArray("authorization").getString(0)};

            new UOSTransactionsManager(getInfoUrl,
                    getAbiJsonToBinUrl,
                    pushTransactionUrl,
                    3,
                    options.toString(),
                    permission,
                    mPrivateKey,
                    new INetResultCallback() {
                        @Override
                        public void success(final String result) {
                            onSuccess(serialNumber, result);
                        }

                        @Override
                        public void fail(int businessCode, String errorMsg) {
                            onFail(serialNumber, errorMsg);
                        }
                    }).
                    doTransaction(paramsJson.getString("transfer_contract"),
                            paramsJson.getString("action"));
        } catch (JSONException e) {
            e.printStackTrace();
            onFail(serialNumber, e != null ? e.getMessage() : "json error");
        }
    }

    /**
     * 签名一笔交易
     * @param serialNumber
     * @param params
     * @param methodName
     */
    private void requestSignature(final String serialNumber, String params, String methodName) {
        try {
            JSONObject paramsJson = new JSONObject(params);

            JSONObject options = paramsJson.getJSONObject("options");

            String[] permission = new String[]{paramsJson.getJSONArray("authorization").getString(0)};

            new UOSTransactionsManager(getInfoUrl,
                    getAbiJsonToBinUrl,
                    pushTransactionUrl,
                    3,
                    options.toString(),
                    permission,
                    mPrivateKey,
                    new INetResultCallback() {
                        @Override
                        public void success(final String result) {
                            onSuccess(serialNumber, result);
                        }

                        @Override
                        public void fail(int businessCode, String errorMsg) {
                            onFail(serialNumber, errorMsg);
                        }
                    }).
                    doTransaction(paramsJson.getString("transfer_contract"),
                            paramsJson.getString("action"));
        } catch (JSONException e) {
            e.printStackTrace();
            onFail(serialNumber, e != null ? e.getMessage() : "json error");
        }
    }

    /**
     * 跳转页面申请  目前除了内部 DAPP 其他的应该用不到
     * @param serialNumber
     * @param params
     * @param methodName
     */
    private void turnToTxPage(String serialNumber,final String params, String methodName){
        //跳转到指定页面
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //  {"block_num":9124492,"transaction_id":"d5b8519ecdb4d7fb595a66091b931d4926d8aa67fc905db69aaa1b2ecabc1c2b"}
                try {
                    LogUtils.w("获取到的毁掉信息："+new Gson().toJson(params));
                    JSONObject result = new JSONObject(params);
                    if(result.has("block_num") && result.getInt("block_num")>0 &&
                            result.has("transaction_id") && result.getString("transaction_id")!=null && result.getString("transaction_id").length()>0){
                        //TODO 需要将params 中的数据进行解析，得到区块号，和交易ID
//                        Toast.makeText(mContext, "获取到的区块号："+result.getInt("；交易ID：")+result.getString("transaction_id"), Toast.LENGTH_SHORT).show();
                        LogUtils.w("获取到的区块号："+result.getInt("；交易ID：")+result.getString("transaction_id"));
                    }else{
                        Toast.makeText(mContext, "Invalid Result", Toast.LENGTH_SHORT).show();
                        LogUtils.w("Invalid Result");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void onSuccess(final String serialNumber, final String result) {
        LogUtils.i("onSuccess:" + Thread.currentThread().getName());
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    LogUtils.i("onSuccess:" + Thread.currentThread().getName());
                    LogUtils.i("DAPP交互成功回调:" + result);
//                    Toast.makeText(mContext, "本地交易成功："+result, Toast.LENGTH_SHORT).show();

                    mBaseWebView.loadUrl("javascript:callbackResult('" + serialNumber + "'," + new JSONObject(result) + ")");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onFail(final String serialNumber, final String error) {
        LogUtils.i("onFail:" + Thread.currentThread().getName());
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogUtils.i("onFail:" + Thread.currentThread().getName());
                LogUtils.i("DAPP交互失败回调:" + error);
//                Toast.makeText(mContext, "本地交易失败："+error, Toast.LENGTH_SHORT).show();

                mBaseWebView.loadUrl("javascript:callbackResult('" + serialNumber + "'," + new JSONObject() + ",'" + error + "')");
            }
        });
    }
}
