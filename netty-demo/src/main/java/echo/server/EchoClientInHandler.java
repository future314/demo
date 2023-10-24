package echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * @author LR
 */
public class EchoClientInHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        byte[] bytes = new byte[in.readableBytes()];
        in.getBytes(0, bytes);
        System.out.println("receive: " + new String(bytes, StandardCharsets.UTF_8));
        super.channelRead(ctx, msg);
    }
}
