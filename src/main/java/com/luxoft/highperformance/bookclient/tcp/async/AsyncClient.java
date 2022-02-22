package com.luxoft.highperformance.bookclient.tcp.async;

import com.luxoft.highperformance.bookclient.model.Book;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class AsyncClient {

    public Book[] getBooks(int amount) {
        try {
            AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 3883);
            ByteBuffer amountBuffer = ByteBuffer.allocate(4);
            amountBuffer.putInt(amount);
            amountBuffer.position(0);
            return NioCF.connect(channel, address)
                    .thenCompose(v -> NioCF.writeBuffer(amountBuffer, channel))
                    .thenCompose(v -> NioCF.readInt(channel))
                    .thenCompose(size -> NioCF.readBuffer(size, channel))
                    .thenApply(buffer -> {
                        NioCF.close(channel);
                        buffer.limit(buffer.capacity());
                        Book[] books = new Book[amount];
                        for (int i = 0; i < amount; i++) {
                            books[i] = Book.fromBinary(buffer);
                        }
                        return books;
                    }).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
