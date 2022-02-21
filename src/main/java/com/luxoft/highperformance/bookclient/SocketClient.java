package com.luxoft.highperformance.bookclient;

import com.luxoft.highperformance.bookclient.model.Book;
import one.nio.serial.DeserializeStream;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.List;

@Service
public class SocketClient {

    public Book[] getBooks(int amount) {
        try {
            Socket socket = new Socket();
            socket.connect(
                new InetSocketAddress("127.0.0.1",9100),
                50);

            DataInputStream in = new DataInputStream(
                    new BufferedInputStream(
                            socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(
                    new BufferedOutputStream(
                            socket.getOutputStream()));
            out.writeInt(amount); // send how many books to retrieve
            out.flush();
            Book[] books = new Book[amount];

            int size = in.readInt();// get size of array
            byte[] buf = new byte[size];
            int bytes_read = 0;
            while (bytes_read < size) {
                int bytes_read_now =
                    in.read(buf, bytes_read, size-bytes_read);
                if (bytes_read_now<=0) break;
                bytes_read += bytes_read_now;
            }
            ByteBuffer buffer = ByteBuffer.wrap(buf);

            for (int i=0; i<amount; i++) {
                books[i] = Book.fromBinary(buffer);
            }

            return books;
        } catch(SocketTimeoutException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
