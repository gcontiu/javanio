package poc.nio.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * min 6.30
 */
public class QuickClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1337);
        OutputStream out = socket.getOutputStream();
        out.write(55);
        out.write(54);
        out.write(34);
        out.write(23);
        out.write(2);
        out.write(7);
        out.flush();

        InputStream in = socket.getInputStream();
        int read = -1;
        while ((read = in.read()) != -1) {
            System.out.println(read);
        }
    }
}
