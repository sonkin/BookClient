package com.luxoft.highperformance.bookclient.rest;

import com.luxoft.highperformance.bookclient.model.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class RestWebClient {

    @RequestMapping(value = "books/{amount}")
    public List<Book> getBooks(@PathVariable int amount) {
        WebClient client = WebClient.create("http://localhost:8080");
        List<Book> books = client
            .get()
            .uri("/books/books/" + amount)
            .retrieve().toEntityList(Book.class).block().getBody();
        return books;
    }

}
