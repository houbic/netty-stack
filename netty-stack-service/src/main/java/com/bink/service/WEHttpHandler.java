package com.bink.service;

import com.alibaba.fastjson.JSONObject;
import com.bink.netty.entity.NettyResponse;
import com.bink.netty.service.HttpServiceHandler;
import com.bink.utils.JsonMapper;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.springframework.stereotype.Component;

/**
 * Created by chenbinghao on 16/10/12.下午10:07
 */
@Component
public class WeHttpHandler implements HttpServiceHandler{


    @Override
    public void getMethodHandler(NettyResponse nettyResponse, String requestUri) {
        String path = requestUri.substring(0, requestUri.indexOf("?"));
        if (path.endsWith("demo")) {
            JSONObject json = new JSONObject();
            json.put("name", "bink");
            nettyResponse.setResBody(JsonMapper.toJson(json));
            // TODO: 16/10/13
        } else {
            nettyResponse.setStatusCode(HttpResponseStatus.BAD_GATEWAY.code());
        }
    }

    @Override
    public void postMethodHandler(NettyResponse nettyResponse, String content) {
        // 目前不支持POST办法
        nettyResponse.setStatusCode(HttpResponseStatus.METHOD_NOT_ALLOWED.code());
    }

}
