package io.eblock.eos4j.blockchain.callback;

/**
 * @author by huangzhou@ulord.net
 * @package com.thgy.uos.manager
 * @create 2019-04-17 15:59
 * @Description
 */

public interface INetResultCallback {
    void success(String result);

    void fail(int businessCode, String errorMsg);
}
