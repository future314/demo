package echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.nio.charset.StandardCharsets;

/**
 * @author LR
 */
public class EchoClientHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("send:" + msg);
        ByteBuf byteBuf = ctx.alloc().heapBuffer();
        byteBuf.writeBytes(((String) msg).getBytes(StandardCharsets.UTF_8));
        super.write(ctx, byteBuf, promise);
    }
}
