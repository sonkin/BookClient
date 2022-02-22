package com.luxoft.highperformance.bookclient.rest;

import com.luxoft.highperformance.bookclient.model.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@FeignClient(name = "books", url = "http://localhost:8080/books")
public interface BookClient {

    @RequestMapping("baseline/{s}")
    String baseline(@PathVariable String s);

    @RequestMapping("all")
    List<Book> getAllBooks();

    @RequestMapping(value = "books/{amount}")
    List<Book> getBooks(@PathVariable int amount);

    @RequestMapping("create-index-all")
    public void createIndexForAllBooks();
}

