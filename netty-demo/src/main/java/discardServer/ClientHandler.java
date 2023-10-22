package discardServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.nio.charset.StandardCharsets;

/**
 * @author LR
 */
public class ClientHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println(msg);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(((String) msg).getBytes(StandardCharsets.UTF_8));
        super.write(ctx, byteBuf, promise);
    }
}
