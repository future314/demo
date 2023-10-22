package discardServer;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class discardServerTest {

    @Test
    public void test_discardServer() {
        new NettyDiscardServer(50889).runServer();
    }

    @Test
    public void test_discardClient() throws UnsupportedEncodingException, InterruptedException {
        NettyDiscardClient.getInstance().runClient("127.0.0.1", 50889);
    }
}
