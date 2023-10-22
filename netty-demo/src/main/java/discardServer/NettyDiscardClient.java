package discardServer;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author LR
 */
public class NettyDiscardClient {
    private static NettyDiscardClient instance = new NettyDiscardClient();

    private Bootstrap b = new Bootstrap();


    public static NettyDiscardClient getInstance() {
        return instance;
    }

    public void runClient(String ip, int port) throws UnsupportedEncodingException, InterruptedException {
        b.group(new NioEventLoopGroup());
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.remoteAddress(ip, port);
        b.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ClientHandler());
            }
        });
        ChannelFuture f = b.connect();
        f.addListener((listener) -> {
            if (listener.isSuccess()) {
                System.out.println("连接成功");
            } else {
                System.out.println("连接失败");
            }
        });
        try {
            f.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Channel channel = f.channel();
        while (true) {
            channel.writeAndFlush("hello");
            Thread.sleep(1000);
        }
    }
}
