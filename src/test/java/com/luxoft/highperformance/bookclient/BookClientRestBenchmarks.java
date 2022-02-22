package com.luxoft.highperformance.bookclient;

import com.luxoft.highperformance.bookclient.rest.BookClient;
import org.junit.runner.RunWith;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class BookClientRestBenchmarks extends AbstractBenchmark {

    String keywords1 = "Book83125";
    String keywords2 = "AuthorName76100 AuthorSurname88148";
    String keywords3 = "AuthorSurname88148 AuthorName76100 Book83125";

    public static BookClient bookClient;

    @Autowired
    public void setBookClient(BookClient bookClient) {
        BookClientRestBenchmarks.bookClient = bookClient;
    }

    @Setup(Level.Trial)
    public void setupBenchmark() {
        //bookClient.createIndexForAllBooks();
    }

    //@Benchmark
    public void baseline(Blackhole bh) {
        bh.consume(bookClient.baseline("hello"));
    }

   //@Benchmark
    public void getAllBooks(Blackhole bh) {
        bh.consume(bookClient.getAllBooks());
    }

    //@Benchmark
    public void get10Books(Blackhole bh) {
        bh.consume(bookClient.getBooks(10));
    }

    //@Benchmark
    public void get100Books(Blackhole bh) {
        bh.consume(bookClient.getBooks(100));
    }

    //@Benchmark
    public void get1000Books(Blackhole bh) {
        bh.consume(bookClient.getBooks(1000));
    }

    @Benchmark
    public void get10000Books(Blackhole bh) {
        bh.consume(bookClient.getBooks(10_000));
    }

}

