import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JavaNIOServer {
    private static Selector selector;
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        selector = Selector.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(5050));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("JavaNIOServer has started on: " + serverSocketChannel.getLocalAddress());

        do {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                if (key.isAcceptable()) {
                    handleAccept(serverSocketChannel, key);
                } else if (key.isReadable()) {
                    handleRead(key);
                }
                iter.remove();
            }
        }while (true);
    }
    public static void handleAccept(ServerSocketChannel serverSocketChannel, SelectionKey key) throws IOException {
        serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);

        client.register(selector, SelectionKey.OP_READ);
        System.out.println("Client Connected: " + client.getRemoteAddress());
    }

    public static void handleRead(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        int bytesRead = client.read(buffer);

        if (bytesRead == -1) {
            System.out.println(client.getRemoteAddress() + " Disconnected");
            client.close();
            return;
        }
        else {
            buffer.flip();
            byte[] rawBytes = new byte[buffer.remaining()];
            buffer.get(rawBytes);
            workerPool.submit(() -> {
                String msg = new String(rawBytes).trim();
                System.out.println(msg);
                try {
                    client.write(ByteBuffer.wrap(rawBytes));
                } catch (IOException ignored) {}

            });
        }

    }
    public static ExecutorService workerPool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );
}
