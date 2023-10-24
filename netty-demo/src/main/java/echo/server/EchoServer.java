package echo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author LR
 */
public class EchoServer {
    private final int serverPort;
    ServerBootstrap b = new ServerBootstrap();

    public EchoServer(int port) {
        this.serverPort = port;
    }

    public void runServer() {
        b.group(new NioEventLoopGroup(1), new NioEventLoopGroup());
        b.channel(NioServerSocketChannel.class);
        b.localAddress(serverPort);
        b.childOption(ChannelOption.SO_KEEPALIVE, true);
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new EchoServerHandler());
            }
        });
        try {
            ChannelFuture channelFuture = b.bind().sync();
            System.out.println("server starting...");
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
