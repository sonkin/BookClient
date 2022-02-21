package com.luxoft.highperformance.bookclient;

import com.luxoft.highperformance.bookclient.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import reactor.core.publisher.Flux;

import java.util.List;

public class RSocketClientTest {

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
