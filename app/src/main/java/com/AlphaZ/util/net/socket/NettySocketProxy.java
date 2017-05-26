package com.AlphaZ.util.net.socket;

import com.AlphaZ.util.net.http.HttpProxy;
import com.AlphaZ.util.valid.ValideHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * ProjectName: AlphaZ
 * PackageName: com.AlphaZ.com.AlphaZ.util.net.socket
 * User: C0dEr
 * Date: 2017/4/7
 * Time: 下午5:17
 * Description:This is a class of com.AlphaZ.com.AlphaZ.util.net.socket
 */
public class NettySocketProxy {
    private static NettySocketProxy nettySocketProxy = null;//单例
    private static Map<String, SocketParameters> parameters;
    protected Logger logger = Logger.getLogger(this.getClass());
    public SocketParameters ps;
    private SocketChannel socketChannel;

    private NettySocketProxy() {

    }

    public static NettySocketProxy Instance(String apiName) {
        if (nettySocketProxy == null) {
            nettySocketProxy = new NettySocketProxy();
            init();
        }
        nettySocketProxy.ps = parameters.get(apiName) == null ? new SocketParameters() : parameters.get(apiName);
        return nettySocketProxy;
    }

    private static void init() {
        try {
            byte[] json = Files.readAllBytes(Paths.get(HttpProxy.class.getResource("/socketConfig.json").toURI()));
            parameters = new ObjectMapper().readValue(new String(json), new TypeReference<Map<String, SocketParameters>>() {
            });
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Do request api response model.
     *
     * @return the api response model
     */
    public void DoRequest(ChannelHandler... handlers) {
        if (ValideHelper.isNullOrEmpty(ps.getUrl())) {
            return;
        }
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_TIMEOUT, 6 * 10 * 1000);
            bootstrap.group(eventLoopGroup);
            bootstrap.remoteAddress(ps.getUrl(), ps.getPort());
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel)
                        throws Exception {
                    socketChannel.pipeline().addFirst(handlers);
                }
            });
            ChannelFuture future;
            try {
                future = bootstrap.connect(ps.getUrl(), ps.getPort()).sync();
                if (future.isSuccess()) {
                    nettySocketProxy.socketChannel = (SocketChannel) future.channel();
                    logger.info("----------------连接建立----------------");
                }
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

}
