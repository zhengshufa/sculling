package com.sculling.sculling.util.netty.tcp.client;

import com.sculling.sculling.util.netty.tcp.server.ChannelManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author hpdata
 * @DATE 2023/5/1014:04
 */
@Slf4j
@Component
public class SocketClient {

    private static Bootstrap bootstrap=null;


    @PostConstruct
    public void init() {

        //worker
        EventLoopGroup worker = new NioEventLoopGroup();

        bootstrap = new Bootstrap();
        //设置线程池
        bootstrap.group(worker);

        //设置socket工厂
        bootstrap.channel(NioSocketChannel.class);

        //设置管道
        bootstrap.handler(new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(new StringEncoder());
                ch.pipeline().addLast(new ClientHandler());
                ch.pipeline().addLast(new ByteArrayEncoder());
            }
        });
    }

    /**
     * 获取会话 （获取或者创建一个会话）
     */
    public Channel createChannel(String ip,Integer port) {
        try {
            Channel channel = bootstrap.connect(ip, port).sync().channel();
            log.info("创建tcp连接通道：ip：{}，port：{}",ip,port);
            return channel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String clientReq(String key,byte[] data,String ip,Integer port){
        Channel channel = ChannelManager.getChannel(key);
        //将通道存入
        if(channel==null){
            SocketClient client = new SocketClient();
            channel = client.createChannel(ip,port);
            ChannelManager.putChannel(key, channel);
        }
        if (channel != null && channel.isActive()) {
            //发送数据
            channel.writeAndFlush(data);

            return "成功";
        }
        return null;
    }
}
