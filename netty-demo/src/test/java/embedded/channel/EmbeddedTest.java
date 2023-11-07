package embedded.channel;

import discardServer.ClientHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 * @author LR
 */
public class EmbeddedTest {

    @Test
    public void runInEmbeddedChannel() {
        EmbeddedDemo.runInEmbeddedChannel();
    }
}
