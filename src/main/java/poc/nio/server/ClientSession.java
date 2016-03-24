package poc.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Every client will have a ClientSession
 */
public class ClientSession {

    SelectionKey selectionKey;
    SocketChannel socketChannel;
    ByteBuffer byteBuffer;

    public ClientSession(SelectionKey selectionKey, SocketChannel socketChannel) throws IOException {
        this.selectionKey = selectionKey;
        this.socketChannel = (SocketChannel) socketChannel.configureBlocking(false); // async & non-blocking
        byteBuffer = ByteBuffer.allocateDirect(64); //64 byte capacity
    }

    void disconnect() {
        MainServer.clientMap.remove(selectionKey);
        try {
            if (selectionKey != null) {
                selectionKey.cancel();
            }
            if (socketChannel == null) {
                return;
            }
            System.out.println("Bye bye... " + (InetSocketAddress) socketChannel.getRemoteAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * When the selector lets us know that there is a read event pending, to call this method
     */
    void read() {
        try {
            // handle reading from a channel
            int ammountRead = -1;
            try {
                ammountRead = socketChannel.read((ByteBuffer) byteBuffer.clear());
            } catch (Throwable t) {
                // TODO
            }
            if (ammountRead == -1) {
                disconnect();
            }

            if (ammountRead < 1) {
                return;
            }

            System.out.println("sending back " + byteBuffer.position() + " bytes") ;

            // turn this bus right around and send it back !
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
        } catch (Throwable t) {
            disconnect();
            t.printStackTrace();
        }
    }
}
