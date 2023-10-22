package consumer.demo;

import cn.hutool.core.util.StrUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author LR
 */
public class ConsumerStop {
    public static void main(String[] args) throws IOException {
        Socket sock = new Socket("localhost", 6666);
        OutputStream outputStream = sock.getOutputStream();
        outputStream.write("stop".getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
