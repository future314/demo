package echo.server;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.UnsupportedEncodingException;

/**
 * @author LR
 */
public class EchoClient {
    private static EchoClient instance = new EchoClient();

    private Bootstrap b = new Bootstrap();


    public static EchoClient getInstance() {
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
                socketChannel.pipeline().addLast(new EchoClientHandler());
                socketChannel.pipeline().addLast(new EchoClientInHandler());
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
