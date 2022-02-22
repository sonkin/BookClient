package com.luxoft.highperformance.bookclient;

import com.luxoft.highperformance.bookclient.model.Book;
import com.luxoft.highperformance.bookclient.rest.BookClient;
import com.luxoft.highperformance.bookclient.rest.RestTemplateClient;
import com.luxoft.highperformance.bookclient.rest.RestWebClient;
import com.luxoft.highperformance.bookclient.tcp.SocketClient;
import com.luxoft.highperformance.bookclient.tcp.async.AsyncClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;

import java.util.List;


@SpringBootTest
public class ClientsTest {

    @Autowired
    BookClient bookClient;

    @Autowired
    SocketClient socketClient;

    @Autowired
    RestTemplateClient restTemplateClient;
    
    @Autowired
    RestWebClient restWebClient;

    @Test
    void testGetBooks() {
        Book[] books = new AsyncClient().getBooks(3);
        for (Book book: books) {
            System.out.println(book);
        }
    }

    @Test
    void testGetBooksSocket() {
        Book[] books = socketClient.getBooks(3);
        for (Book book: books) {
            System.out.println(book);
        }
    }

//    @Test
//    void testGetBooksMemory() {
//        Book[] books = memoryClient.getBooks(3);
//        for (Book book: books) {
//            System.out.println(book);
//        }
//    }

    @Test
    public void testRSocket() {
        RSocketStrategies strategies = RSocketStrategies.builder()
                .encoders(encoders -> encoders.add(new Jackson2CborEncoder()))
                .decoders(decoders -> decoders.add(new Jackson2CborDecoder()))
                .build();

        RSocketRequester rsocketRequester = RSocketRequester.builder()
                //.dataMimeType(MediaType.APPLICATION_JSON)
                .rsocketStrategies(strategies)
                .tcp("127.0.0.1", 7000);

        rsocketRequester.route("books")
                .data(10)
                .retrieveFlux(Book.class)
                .doOnNext(System.out::println)
                .blockLast();

    }
}