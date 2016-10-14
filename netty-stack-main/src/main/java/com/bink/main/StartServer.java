package com.bink.main;

import com.bink.netty.stack.NettyHttpServer;
import com.bink.service.WeHttpHandler;
import com.bink.utils.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by chenbinghao on 16/10/11.下午9:35
 */
public class StartServer {

    private static Logger LOG = LogManager.getLogger();

//    static ClassPathXmlApplicationContext context;

    public static void main(String[] args) throws Exception {
//        context = new ClassPathXmlApplicationContext("application.xml");
//        LOG.info("application start success......");
//        context.start();
        Integer port = Config.getInteger("http_port");
        new NettyHttpServer().start(port, new WeHttpHandler());
    }

}
