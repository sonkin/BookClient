package com.luxoft.highperformance.bookclient;

import com.luxoft.highperformance.bookclient.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class SocketClientTest {

    @Autowired
    BookClient bookClient;

    @Autowired
    SocketClient socketClient;

    @Test
    void testGetBooks() {
        Book[] books = socketClient.getBooks(3);
        for (Book book: books) {
            System.out.println(book);
        }
    }

}