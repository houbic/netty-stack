package com.bink.netty.stack;

import com.bink.netty.service.HttpServiceHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Created by chenbinghao on 16/7/12.
 */
public class NettyHttpServer {

    //private static Log log = LogFactory.getLog(HttpServer.class);

    public void start(int port, HttpServiceHandler handler) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
                            channel.pipeline().addLast(new HttpResponseEncoder());
                            // channel.pipeline().addLast(new HttpObjectAggregator(65));
                            // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
                            channel.pipeline().addLast(new HttpRequestDecoder());
                            channel.pipeline().addLast(new HttpServerInboundHandler(handler));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync();

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
