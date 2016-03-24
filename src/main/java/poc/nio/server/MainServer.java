package poc.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by anghelc on 23/03/16.
 * https://www.youtube.com/watch?v=nUI4zO6abH0
 * https://www.youtube.com/watch?v=AofvCRyvkAk
 */
public class MainServer {


    ServerSocketChannel serverSocketChannel;
    Selector selector;
    static Map<SelectionKey, ClientSession> clientMap = new HashMap<>();

    public MainServer(InetSocketAddress listenAddress)  throws Throwable{
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector = Selector.open(), SelectionKey.OP_ACCEPT);
        serverSocketChannel.bind(listenAddress);

        System.out.println("Listening...");

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                loop();
            } catch (IOException t) {
                t.printStackTrace();
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    public void loop() throws IOException {
        selector.selectNow(); // selectNow will not block if no keys are ready

        for(SelectionKey selectionKey : selector.selectedKeys()) {
            if (!selectionKey.isValid()) continue;

            if (selectionKey.isAcceptable()) {
                SocketChannel acceptedSocketChannel = serverSocketChannel.accept();
                if (acceptedSocketChannel == null) continue;
                acceptedSocketChannel.configureBlocking(false);
                SelectionKey readKey = acceptedSocketChannel.register(selector, SelectionKey.OP_READ);
                clientMap.put(readKey, new ClientSession(readKey, acceptedSocketChannel));
                System.out.println("New client ip = " + acceptedSocketChannel.getRemoteAddress() + ", total clients = " + MainServer.clientMap.size());
            }

            if (selectionKey.isReadable()) {
                clientMap.get(selectionKey).read();
            }
        }
        selector.selectedKeys().clear();
    }

    public static void main(String... args) throws Throwable{

        new MainServer(new InetSocketAddress("localhost", 1337));
    }
}
