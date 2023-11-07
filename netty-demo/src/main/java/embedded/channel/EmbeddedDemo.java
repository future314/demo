package embedded.channel;

import discardServer.ClientHandler;
import discardServer.DiscardHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;

import java.util.Arrays;

/**
 * @author LR
 */
public class EmbeddedDemo {

    public static void runInEmbeddedChannel() {
        ChannelInitializer ci = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new ClientHandler());
                ch.pipeline().addLast(new DiscardHandler());
            }
        };
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(ci);
        embeddedChannel.writeOutbound("hello");
        ByteBuf result = embeddedChannel.readOutbound();
        byte[] bytes = new byte[result.readableBytes()];
        result.getBytes(result.readerIndex(), bytes);
        System.out.println(Arrays.toString(bytes));
        embeddedChannel.writeInbound(result);
        embeddedChannel.close();
    }
}
