package com.sculling.sculling.util.netty.tcp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
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
public class NettyWebSocketServer {


    @Autowired
    private WebSocketServerHandler webSocketServerHandler;
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
                    pipeline.addLast("HttpServerCodec",new HttpServerCodec())
                            //以块方式写 添加ChunkedWriteHandler处理器
                            .addLast("ChunkedWriteHandler", new ChunkedWriteHandler())
                            //http传输过程中会分段 HttpObjectAggregator可以将多个段聚合
                            //这就是为什么，当浏览器发送大量数据时，就会发多次http请求
                            .addLast("HttpObjectAggregator",new HttpObjectAggregator(8192))
                            //对应websocket 它的数据是以帧(WebSocketFrame)形式传递
                            //浏览器请求时ws://localhost:7000/hello表示请求的uri
                            //WebSocketServerProtocolHandler的核心功能是将http协议升级为ws协议 并保持长连接
                            //是通过一个状态码 101
                            .addLast("WebSocketServerProtocolHandler",new WebSocketServerProtocolHandler("/ws"))
                            //自定义的handler做业务逻辑处理
                            .addLast(webSocketServerHandler);

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
