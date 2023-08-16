package com.sculling.sculling.util.netty.tcp.server;

import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hpdata
 * @DATE 2023/5/1111:32
 */

@Slf4j
@Component
@ChannelHandler.Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static final ConcurrentHashMap<String, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务端接收到消息"+msg.text());
        //回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间 "+ LocalDateTime.now()+" "+msg.text()));
    }

    //当web客户端连接后触发该方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        String channelId =  ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().toString().replace("/","");
        if (!CHANNEL_MAP.containsKey(channelId)) {
            CHANNEL_MAP.put(channelId, ctx);
            log.info("新的连接加入了：[{}]", channelId);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String channelId =  ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().toString().replace("/","");
        if (CHANNEL_MAP.containsKey(channelId)) {
            //删除连接
            CHANNEL_MAP.remove(channelId);
            log.info("连接已断开：[{}]", channelId);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生 "+cause.getMessage());
        //关闭连接
        ctx.close();
    }

}
