package com.bink.netty.stack;

import com.bink.netty.entity.NettyResponse;
import com.bink.netty.service.HttpServiceHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {

    private Logger LOG = LogManager.getLogger();

    private HttpRequest request;

    private HttpServiceHandler httpServiceHandler;

    public HttpServerInboundHandler(HttpServiceHandler httpServiceHandler) {
        this.httpServiceHandler = httpServiceHandler;
    }

    private void response(ChannelHandlerContext ctx, NettyResponse response) throws UnsupportedEncodingException {

        // 获取netty返回码
        HttpResponseStatus responseStatus;
        if (response.getStatusCode() == null) {
            responseStatus = OK;
        } else if (response.getStatusCode() != null && StringUtils.isEmpty(response.getMessage())) {
            responseStatus = HttpResponseStatus.valueOf(response.getStatusCode());
        } else {
            responseStatus = new HttpResponseStatus(response.getStatusCode().intValue(), response.getMessage());
        }
        FullHttpResponse httpResponse = new DefaultFullHttpResponse
                (HTTP_1_1, responseStatus, Unpooled.wrappedBuffer(response.getResBody().getBytes("UTF-8")));
        httpResponse.headers().set(CONTENT_TYPE, "text/plain");
        httpResponse.headers().set(CONTENT_LENGTH,
        httpResponse.content().readableBytes());
        if (HttpHeaders.isKeepAlive(request)) {
            httpResponse.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        ctx.write(httpResponse);
        ctx.flush();

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        try {
            NettyResponse nettyResponse = new NettyResponse();
            if (msg instanceof HttpRequest) {
                request = (HttpRequest) msg;
                // 暂时只对GET方法进行处理
                LOG.debug("request method:{}", request.getMethod());
                if (request.getMethod() == HttpMethod.GET) {
                    String uri = request.getUri();
                    LOG.debug("GET Request uri:{}", uri);
                    httpServiceHandler.getMethodHandler(nettyResponse, uri);
                    response(ctx, nettyResponse);
                }
            }

            if (msg instanceof HttpContent) {
                HttpContent httpContent = (HttpContent)msg;
                ByteBuf buf = httpContent.content();
                String content = buf.toString(CharsetUtil.UTF_8);
                LOG.debug("request body:{}", content);
                if (request.getMethod() == HttpMethod.POST) {
                    LOG.debug("request method is post");
                    httpServiceHandler.postMethodHandler(nettyResponse, content);
                    response(ctx, nettyResponse);
                }
                if (request.getMethod() == HttpMethod.PUT) {
                    // TODO: 16/10/12
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println(cause.getMessage());
        ctx.close();
    }

}