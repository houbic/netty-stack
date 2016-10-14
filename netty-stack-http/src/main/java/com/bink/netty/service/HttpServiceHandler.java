package com.bink.netty.service;

import com.bink.netty.entity.NettyResponse;

/**
 * Created by chenbinghao on 16/10/11.下午6:06
 */
public interface HttpServiceHandler {

    void getMethodHandler(NettyResponse nettyResponse, String requestUri);

    void postMethodHandler(NettyResponse nettyResponse, String content);

}
