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
import org.springframework.http.MediaType;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.json.Jackson2SmileDecoder;
import org.springframework.http.codec.json.Jackson2SmileEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;

import java.util.List;


/**
 * For BOOKS_AMOUNT = 100000, LOOPS = 10:
 * TCP               3096 ms
 * TCPAsync          3013 ms
 * REST Feign        3235 ms
 * REST Webclient    3515 ms
 * REST Template     3939 ms
 * RSocket           22981 ms
 *
 * For BOOKS_AMOUNT = 10000, LOOPS = 100:
 * TCP              2612 ms
 * TCPAsync         2743 ms
 * REST Feign       2654 ms
 * WebClient        3246 ms
 * REST Template    3202 ms
 * RSocket          22600 ms
 *
 * For BOOKS_AMOUNT = 1000, LOOPS = 1000:
 * TCP          2792 ms
 * TCPAsync     2941 ms
 * REST         3429 ms
 *
 * For BOOKS_AMOUNT = 100, LOOPS = 1000:
 * TCP          552 ms
 * TCPAsync     574 ms
 * REST         857 ms
 *
 * For BOOKS_AMOUNT = 10, LOOPS = 1000:
 * TCP           139 ms
 * TCPAsync      175 ms
 * REST Feign    357 ms
 * RSocket       750 ms
 * RESTTemplate  1167 ms
 * WebClient     647 ms
 *
 * For BOOKS_AMOUNT = 1, LOOPS = 1000:
 * TCP              118 ms
 * TCPAsync         149 ms
 * RESTWebClient    561 ms
 * RESTFeign        313 ms
 * RESTTemplate     955 ms
 * RSocket          564 ms
 *
 */

@SpringBootTest
public class ClientBenchmarks {

    int BOOKS_AMOUNT = 1;
    int LOOPS = 1000;

    @Autowired
    BookClient bookClient;

    @Autowired
    SocketClient socketClient;

    @Autowired
    RestTemplateClient restTemplateClient;
    
    @Autowired
    RestWebClient restWebClient;

    @Test
    void measureRESTFeign() {
        int total = 0;
        for (int i=0;i<LOOPS; i++) {
            List<Book> books = bookClient.getBooks(BOOKS_AMOUNT);
            total += books.size();
        }
        long start = System.nanoTime();
        for (int i=0;i<LOOPS; i++) {
            List<Book> books = bookClient.getBooks(BOOKS_AMOUNT);
            total += books.size();
        }
        System.out.println((System.nanoTime()-start)/1000_000 + ", total="+total);
    }

    @Test
    void measureTCPAsync() {
        int total=0;
        AsyncClient asyncClient = new AsyncClient();
        for (int i=0;i<LOOPS; i++) {
            Book[] books = asyncClient.getBooks(BOOKS_AMOUNT);
            total += books.length;
        }
        long start = System.nanoTime();
        for (int i=0;i<LOOPS; i++) {
            Book[] books = asyncClient.getBooks(BOOKS_AMOUNT);
            total += books.length;
        }
        System.out.println((System.nanoTime()-start)/1000_000 + ", total="+total);
    }

    @Test
    void measureTCP() {
        int total=0;
        SocketClient socketClient = new SocketClient();
        for (int i=0;i<LOOPS; i++) {
            Book[] books = socketClient.getBooks(BOOKS_AMOUNT);
            total += books.length;
        }
        long start = System.nanoTime();
        for (int i=0;i<LOOPS; i++) {
            Book[] books = socketClient.getBooks(BOOKS_AMOUNT);
            total += books.length;
        }
        System.out.println((System.nanoTime()-start)/1000_000 + ", total="+total);
    }

    @Test
    void measureRESTTemplate() {
        int total=0;
        for (int i=0;i<LOOPS; i++) {
            Book[] books = restTemplateClient.getBooks(BOOKS_AMOUNT);
            total += books.length;
        }
        long start = System.nanoTime();
        for (int i=0;i<LOOPS; i++) {
            Book[] books = restTemplateClient.getBooks(BOOKS_AMOUNT);
            total += books.length;
        }
        System.out.println((System.nanoTime()-start)/1000_000 + ", total="+total);
    }

    @Test
    void measureRESTWebClient() {
        int total=0;
        for (int i=0;i<LOOPS; i++) {
            List<Book> books = restWebClient.getBooks(BOOKS_AMOUNT);
            total += books.size();
        }
        long start = System.nanoTime();
        for (int i=0;i<LOOPS; i++) {
            List<Book> books = restWebClient.getBooks(BOOKS_AMOUNT);
            total += books.size();
        }
        System.out.println((System.nanoTime()-start)/1000_000 + ", total="+total);
    }

    //@Test
    void measureRSocket() {
        RSocketStrategies strategies = RSocketStrategies.builder()
                .encoders(encoders -> encoders.add(new Jackson2CborEncoder()))
                .decoders(decoders -> decoders.add(new Jackson2CborDecoder()))
//                .encoders(encoders -> encoders.add(new Jackson2JsonEncoder()))
//                .decoders(decoders -> decoders.add(new Jackson2JsonDecoder()))
                .build();
        RSocketRequester requester = RSocketRequester.builder()
                //.dataMimeType(MediaType.APPLICATION_JSON)
                .rsocketStrategies(strategies)
                .tcp("127.0.0.1", 7000);

        int total=0;
        for (int i=0;i<LOOPS; i++) {
            List<Book> books = requester.route("books")
                    .data(BOOKS_AMOUNT)
                    .retrieveFlux(Book.class)
                    .collectList()
                    .block();
            total += books.size();
        }
        long start = System.nanoTime();
        for (int i=0;i<LOOPS; i++) {
            List<Book> books = requester.route("books")
                    .data(BOOKS_AMOUNT)
                    .retrieveFlux(Book.class)
                    .collectList()
                    .block();
            total += books.size();
        }
        System.out.println((System.nanoTime()-start)/1000_000 + ", total="+total);
    }

}