package AysncServerPackage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class AsyncServer {

  public AsyncServer() {
    System.out.println("Async Server Running..");
    try (AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open()) {
      InetSocketAddress hostAddress = new InetSocketAddress("localhost", 5000);
      serverChannel.bind(hostAddress);

      System.out.println("Waiting for client to connect... ");
      Future<AsynchronousSocketChannel> acceptResult = serverChannel.accept();

      try (AsynchronousSocketChannel clientChannel = (AsynchronousSocketChannel) acceptResult.get()) {
        // ...
        System.out.println("Messages from client: ");
        while ((clientChannel != null) && (clientChannel.isOpen())) {
          ByteBuffer buffer = ByteBuffer.allocate(32);
          Future<Integer> result = clientChannel.read(buffer);

          while (!result.isDone()) {
            /* Do Nothing: Wait until buffer is ready... */
          }
          Thread.sleep(5000);
          buffer.flip();
          String message = new String(buffer.array()).trim();
          System.out.println(message);
          if (message.equals("quit")) {
            break;
          }
        }
      }

    } catch (IOException | InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new AsyncServer();
  }
}
