package echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * @author LR
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        byte[] bytes = new byte[in.readableBytes()];
        in.getBytes(0, bytes);
        System.out.println("receive: " + new String(bytes, StandardCharsets.UTF_8));
        in.retain();
        ChannelFuture future = ctx.writeAndFlush(msg);
        future.addListener(e -> {
            if (e.isSuccess()) {
                System.out.println("write msg successful!");
            }
        });
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " has error");
        super.exceptionCaught(ctx, cause);
    }
}
