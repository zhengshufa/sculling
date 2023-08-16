package com.sculling.sculling.util.netty.tcp.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hpdata
 * @DATE 2023/5/1111:32
 */

@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    public static final ConcurrentHashMap<String, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
        //在这里做业务处理
        String heart = new String((byte[]) msg);
        if("xintiaobao".equals(heart)){
            //心跳包
        }else{
        }

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String channelId =  ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().toString().replace("/","");
        if (!CHANNEL_MAP.containsKey(channelId)) {
            CHANNEL_MAP.put(channelId, ctx);
            log.info("新的连接加入了：[{}]", channelId);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        String channelId =  ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().toString().replace("/","");
        if (CHANNEL_MAP.containsKey(channelId)) {
            //删除连接
            CHANNEL_MAP.remove(channelId);
            log.info("连接已断开：[{}]", channelId);
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        System.out.println("注销");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (!(evt instanceof IdleStateEvent)) {
            super.userEventTriggered(ctx, evt);
            return;
        }
        if (((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {
            ctx.disconnect();
        }
    }
}
