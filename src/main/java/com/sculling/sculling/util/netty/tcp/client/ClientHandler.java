package com.sculling.sculling.util.netty.tcp.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hpdata
 * @DATE 2023/5/1014:05
 */
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<Byte[]> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Byte[] s) throws Exception {
        log.info("服务端发过来的消息：{}",s);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(".......................tcp断开连接.........................");
        //移除
        Channel channel = ctx.channel();
        channel.close().sync();
        super.channelInactive(ctx);
    }
}
