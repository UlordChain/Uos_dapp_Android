package io.eblock.eos4j.blockchain.net;

import java.io.Serializable;

/**
 * Created by xuhuixiang@ulord.net on 2018/4/2.
 */


public class ResponseBean<T> implements Serializable {

    public int code;
    public String message;
    public T data = null;

}