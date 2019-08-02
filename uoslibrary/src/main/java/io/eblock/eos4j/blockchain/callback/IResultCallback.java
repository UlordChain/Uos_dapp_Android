package io.eblock.eos4j.blockchain.callback;

import okhttp3.Callback;

/**
 * @author by huangzhou@ulord.net
 * @package com.thgy.uos.net.callback
 * @create 2019-04-16 11:37
 * @Description
 */

public interface IResultCallback extends Callback {
    void requestSuccess(String result);
    void requestFail(String requestUrl, String failMessage);
}
