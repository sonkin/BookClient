package com.luxoft.highperformance.bookclient;

import com.luxoft.highperformance.bookclient.model.Book;
import org.junit.runner.RunWith;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 *
 Benchmark                           Mode  Cnt       Score       Error  Units
 BookClientBenchmarks.baseline       avgt    3     168,025 ±    65,448  us/op
 BookClientBenchmarks.get10Books     avgt    3     400,136 ±  2505,030  us/op
 BookClientBenchmarks.get100Books    avgt    3     677,290 ±   171,614  us/op
 BookClientBenchmarks.get1000Books   avgt    3    3946,730 ±  1065,618  us/op
 BookClientBenchmarks.get10000Books  avgt    3   39754,116 ± 22581,088  us/op
 BookClientBenchmarks.getAllBooks    avgt    3  504187,496 ± 52937,045  us/op

 */
@SpringBootTest
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@RunWith(SpringRunner.class)
public class RSocketBenchmarks extends AbstractBenchmark {

    public static SocketClient socketClient;
    public static RSocketRequester rsocketRequester;
    public static RSocketStrategies strategies;

    @Setup
    public void setup() {
        strategies = RSocketStrategies.builder()
                .encoders(encoders -> encoders.add(new Jackson2CborEncoder()))
                .decoders(decoders -> decoders.add(new Jackson2CborDecoder()))
                .build();
        rsocketRequester = RSocketRequester.builder()
                //.dataMimeType(MediaType.APPLICATION_JSON)
                .rsocketStrategies(strategies)
                .tcp("127.0.0.1", 7000);
    }

    @Autowired
    public void setSocketClient(SocketClient socketClient) {
        RSocketBenchmarks.socketClient = socketClient;
    }
//
//    @Benchmark
//    public void get10Books(Blackhole bh) {
//        bh.consume(socketClient.getBooks(10));
//    }
//
//    @Benchmark
//    public void get100Books(Blackhole bh) {
//        bh.consume(socketClient.getBooks(100));
//    }
//
//    @Benchmark
//    public void get1000Books(Blackhole bh) {
//        bh.consume(socketClient.getBooks(1000));
//    }

    @Benchmark
    public void get10000Books(Blackhole bh) {
        bh.consume(
                rsocketRequester.route("books")
                .data(10_000)
                .retrieveFlux(Book.class)
                .blockLast());
    }

}

