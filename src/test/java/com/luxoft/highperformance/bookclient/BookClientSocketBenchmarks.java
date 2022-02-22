package com.luxoft.highperformance.bookclient;

import com.luxoft.highperformance.bookclient.tcp.SocketClient;
import com.luxoft.highperformance.bookclient.tcp.async.AsyncClient;
import org.junit.runner.RunWith;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 *
 Benchmark                                 Mode  Cnt      Score        Error  Units
 BookClientSocketBenchmarks.get1000Books   avgt    3  12946,934 ± 235540,410  us/op
 BookClientSocketBenchmarks.get10000Books  avgt    3  28026,213 ±   6185,732  us/op
 BookClientSocketBenchmarks.get100000Books  avgt   3  402192,150 ± 146359,711  us/op

 */
@SpringBootTest
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@Fork(1)
@Threads(4)
@RunWith(SpringRunner.class)
public class BookClientSocketBenchmarks extends AbstractBenchmark {

    public static SocketClient socketClient;
    public static AsyncClient asyncClient = new AsyncClient();

    @Autowired
    public void setSocketClient(SocketClient socketClient) {
        BookClientSocketBenchmarks.socketClient = socketClient;
    }

    @Benchmark
    public void get10Books(Blackhole bh) {
        bh.consume(asyncClient.getBooks(10));
//        bh.consume(socketClient.getBooks(10));
    }

    @Benchmark
    public void get100Books(Blackhole bh) {
        bh.consume(asyncClient.getBooks(100));
    }

    @Benchmark
    public void get1000Books(Blackhole bh) throws InterruptedException {
        bh.consume(asyncClient.getBooks(1000));
        //Thread.sleep(20);
    }

    @Benchmark
    public void get10000Books(Blackhole bh) {
        //bh.consume(socketClient.getBooks(10000));
        bh.consume(asyncClient.getBooks(10000));
    }

    @Benchmark
    public void get100000Books(Blackhole bh) {
        bh.consume(asyncClient.getBooks(100000));
    }

}

