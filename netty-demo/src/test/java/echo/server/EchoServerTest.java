package echo.server;

import discardServer.NettyDiscardClient;
import discardServer.NettyDiscardServer;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class EchoServerTest {
    @Test
    public void test_echoServer() {
        new EchoServer(50889).runServer();
    }

    @Test
    public void test_echoClient() throws UnsupportedEncodingException, InterruptedException {
        EchoClient.getInstance().runClient("127.0.0.1", 50889);
    }
}
