package io.eblock.eos4j.blockchain.uosmanager;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import io.eblock.eos4j.GsonUtils;
import io.eblock.eos4j.blockchain.api.EosChainInfo;
import io.eblock.eos4j.blockchain.bean.JsonToBeanResultBean;
import io.eblock.eos4j.blockchain.bean.JsonToBinRequest;
import io.eblock.eos4j.blockchain.callback.INetResultCallback;
import io.eblock.eos4j.blockchain.callback.impl.DefaultCallbackImpl;
import io.eblock.eos4j.blockchain.chain.Action;
import io.eblock.eos4j.blockchain.chain.PackedTransaction;
import io.eblock.eos4j.blockchain.chain.SignedTransaction;
import io.eblock.eos4j.blockchain.cypto.ec.UosPrivateKey;
import io.eblock.eos4j.blockchain.types.TypeChainId;
import io.eblock.eos4j.blockchain.uosmanager.net.HttpNetworkClient;
import io.eblock.eos4j.blockchain.util.GsonEosTypeAdapterFactory;
import io.eblock.eos4j.blockchain.util.Ulog;

/**
 * @author by huangzhou@ulord.net
 * @package com.thgy.uos.manager
 * @create 2019-04-17 10:55
 * @Description 统一的步骤控制输出【可以指定在第几步就返回输出结果】
 */

public class UOSTransactionsManager {

    private String get_infoUrl;// URL for  get_info
    private String abi_json_to_binUrl;// URL for  get_info
    private String push_transactionUrl;// URL for  get_info
    private String transactionMessage;
    private String uosPrivateKey;
    private INetResultCallback callback;

    private String action;
    private String contract;

    private int stepForFinish = -1;//用于标记中止的步骤

    private String[] permissions;

    private Gson mGson = new GsonBuilder()
            .registerTypeAdapterFactory(new GsonEosTypeAdapterFactory())
            .excludeFieldsWithoutExposeAnnotation().create();

    private EosChainInfo mChainInfoBean = new EosChainInfo();
    private JsonToBeanResultBean mJsonToBeanResultBean = new JsonToBeanResultBean();
    private SignedTransaction txnBeforeSign;

    /**
     * @param get_infoUrl         第一步URL【获取线上区块信息】
     * @param abi_json_to_binUrl  将参数进行abiJsonToBin转换的URL
     * @param push_transactionUrl push交易的URL
     * @param stepForFinish       指定在第几步进行返回
     * @param transactionMessage  交易的原始数据
     * @param permissions         交易权限
     * @param uosPrivateKey       私钥
     * @param callback            结果回调
     */
    public UOSTransactionsManager(@NonNull String get_infoUrl,
                                  @NonNull String abi_json_to_binUrl,
                                  @NonNull String push_transactionUrl,
                                  @IntRange(from = 1) int stepForFinish,//控制在第几步进行返回，当>=4时，证明需要push交易
                                  String transactionMessage,
                                  String[] permissions,//这里的东西需要与私钥进行对应
                                  String uosPrivateKey,
                                  @NonNull INetResultCallback callback) {
        this.get_infoUrl = get_infoUrl;
        this.abi_json_to_binUrl = abi_json_to_binUrl;
        this.push_transactionUrl = push_transactionUrl;
        this.stepForFinish = stepForFinish;
        this.transactionMessage = transactionMessage;
        this.uosPrivateKey = uosPrivateKey;
        this.callback = callback;
        this.permissions = permissions;

    }

    //-----------------------------------------------------------------------------

    /**
     * 执行交易(通用交易)
     */
    public void doTransaction(String currentContract, String currentAction) {
        contract = currentContract;
        action = currentAction;
        // 第一层数据控制
        if(TextUtils.isEmpty(contract) || TextUtils.isEmpty(action)){// 控制协议、action非空【但并没有校验协议的有效性】
            if (callback != null) {
                callback.fail(1, "empty contract or action");
            }
            return ;
        }
        getChainInfo();
    }

    //-----------------------------------------------------------------------------

    /**
     * 获取区块信息
     */
    private void getChainInfo() {
        String url = get_infoUrl;
        HttpNetworkClient.getInstance().get(url, new DefaultCallbackImpl() {
            @Override
            public void requestSuccess(String result) {
                Ulog.i(String.format("ChainInfo --->\n%s", result));

                mChainInfoBean = GsonUtils.getObjFromJSON(result, EosChainInfo.class);//获取信息
                if (stepForFinish == 1) {
                    if (callback != null) {
                        callback.success(result);
                    }
                } else {
                    getAbiJsonToBin();
                }
            }

            @Override
            public void requestFail(String requestUrl, String failMessage) {
                Ulog.i(String.format("ChainInfo fail--->\n%s", failMessage));
                if (callback != null) {
                    callback.fail(1, failMessage);
                }
            }
        });
    }

    /**
     * 签名交易数据
     */
    private void getAbiJsonToBin() {
        JsonToBinRequest jsonToBinRequest = new JsonToBinRequest(contract, action, transactionMessage.replaceAll("\\r|\\n", ""));
        String url = abi_json_to_binUrl;
        HttpNetworkClient.getInstance().postAsynHttpString(url, mGson.toJson(jsonToBinRequest), new DefaultCallbackImpl() {
            @Override
            public void requestSuccess(String result) {
                Ulog.i(String.format("AbiJsonToBin --->\n%s", result));

                mJsonToBeanResultBean = GsonUtils.getObjFromJSON(result, JsonToBeanResultBean.class);
                //控制ChainInfoBean 和AbiJsonToBin 的Bean不为空
                if (mJsonToBeanResultBean == null || TextUtils.isEmpty(mJsonToBeanResultBean.getBinargs())||
                        mChainInfoBean == null || TextUtils.isEmpty(mChainInfoBean.getHeadBlockId())||TextUtils.isEmpty(mChainInfoBean.getChain_id())|| TextUtils.isEmpty(mChainInfoBean.getTimeAfterHeadBlockTime(60000))) {
                    Ulog.i(String.format("AbiJsonToBin fail--->\n%s", "empty bean"));
                    if (callback != null) {
                        callback.fail(2, "empty bean");
                    }
                    return;
                }
                txnBeforeSign = createTransaction(contract, action, mJsonToBeanResultBean.getBinargs(), permissions, mChainInfoBean);
                if (stepForFinish == 2) {
                    if (callback != null) {
                        callback.success(result);
                    }
                } else {
                    getRequreKey();
                }
            }

            @Override
            public void requestFail(String requestUrl, String failMessage) {
                Ulog.i(String.format("AbiJsonToBin fail--->\n%s", failMessage));
                if (callback != null) {
                    callback.fail(2, failMessage);
                }
            }
        });
    }

    /**
     * 创建交易实体
     *
     * @param contract    合约
     * @param actionName  执行人账号名称
     * @param dataAsHex   交易hex
     * @param permissions 权限文本
     * @param chainInfo   区块信息
     * @return
     */
    private SignedTransaction createTransaction(String contract, String actionName, String dataAsHex,
                                                String[] permissions, EosChainInfo chainInfo) {
        Action action;
        if (contract.equals("eosio")) {
            action = new Action(contract, actionName);
            action.setAuthorization(permissions);
            action.setData(dataAsHex);
        } else {
            action = new Action(contract, actionName, true);
            action.setAuthorization(permissions);
            action.setData(dataAsHex);
        }

        SignedTransaction txn = new SignedTransaction();
        txn.addAction(action);
        txn.putSignatures(new ArrayList<String>());


        if (null != chainInfo) {
            txn.setReferenceBlock(chainInfo.getHeadBlockId());
            txn.setExpiration(chainInfo.getTimeAfterHeadBlockTime(60000));
        }
        return txn;
    }

    /**
     * 签名交易
     */
    private void getRequreKey() {
        UosPrivateKey eosPrivateKey = new UosPrivateKey(uosPrivateKey);
        txnBeforeSign.sign(eosPrivateKey, new TypeChainId(mChainInfoBean.getChain_id()));
        //控制签名交易不为空
        if(txnBeforeSign==null){
            if (callback != null) {
                callback.fail(4, "invalid signed transaction");
            }
            return ;
        }
        if (stepForFinish == 3) {
            if (callback != null) {
                callback.success(mGson.toJson(new PackedTransaction(txnBeforeSign)));
            }
        } else {
            pushTransactionRetJson(new PackedTransaction(txnBeforeSign));
        }
    }

    /**
     * 最后面发送交易 推送
     *
     * @param body
     */
    private void pushTransactionRetJson(PackedTransaction body) {
        String url = push_transactionUrl;//暂时中止
        HttpNetworkClient.getInstance().postAsynHttpString(url, mGson.toJson(body), new DefaultCallbackImpl() {
            @Override
            public void requestSuccess(String result) {
                Ulog.i(String.format("pushTransaction --->\n%s", result));
                if (callback != null) {
                    callback.success(result);
                }
            }

            @Override
            public void requestFail(String requestUrl, String failMessage) {
                Ulog.i(String.format("pushTransaction fail--->\n%s", failMessage));
                if (callback != null) {
                    callback.fail(4, failMessage);
                }
            }
        });
    }

    /**
     * 辅助获取相应回调结果的
     */
}
