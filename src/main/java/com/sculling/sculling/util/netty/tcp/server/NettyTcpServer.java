package com.sculling.sculling.util.netty.tcp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author hpdata
 * @DATE 2023/5/1111:31
 */
@Slf4j
@Component
public class NettyTcpServer {


    @Autowired
    private NettyServerHandler nettyServerHandler;
    private static boolean isStart = false;

    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    public boolean serverStart(int port){
        try{
            if(isStart) return true;
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.option(ChannelOption.SO_BACKLOG, 2048).option(ChannelOption.SO_REUSEADDR, true).childOption(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.channel(NioServerSocketChannel.class);
            ChannelInitializer<SocketChannel> channelChannelInitializer = new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new IdleStateHandler(60, 60, 60, TimeUnit.SECONDS));
//                    pipeline.addLast(new HeatBeatHandler());
//                    pipeline.addLast(new LineBasedFrameDecoder(1024));
//                    pipeline.addLast(new StringDecoder());
//                    pipeline.addLast(new StringEncoder());
                    pipeline.addLast(new ByteArrayEncoder());
                    pipeline.addLast(new ByteArrayDecoder());
                    pipeline.addLast(nettyServerHandler);
                }
            };
            bootstrap.childHandler(channelChannelInitializer);
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            log.info("Netty Tcp Server start success on port：{}", port);
            isStart = true;
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean serverStop() {
        try {
            if (!isStart) return true;
            Future<?> future = this.workerGroup.shutdownGracefully().await();
            if (!future.isSuccess()) {
                log.error("<---- netty workerGroup cannot be stopped", future.cause());
                return false;
            }
            future = this.bossGroup.shutdownGracefully().await();
            if (!future.isSuccess()) {
                log.error("<---- netty bossGroup cannot be stopped", future.cause());
                return false;
            }
            log.info("关闭Netty Tcp 服务端成功");
            isStart = false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
