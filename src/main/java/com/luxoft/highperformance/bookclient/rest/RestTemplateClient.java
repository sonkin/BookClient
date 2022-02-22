package com.luxoft.highperformance.bookclient.rest;

import com.luxoft.highperformance.bookclient.model.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class RestTemplateClient {

    @RequestMapping(value = "books-webclient/{amount}")
    public Book[] getBooks(@PathVariable int amount) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/books/books/"+amount;
        ResponseEntity<Book[]> response =
                restTemplate.getForEntity(url, Book[].class);
        return response.getBody();
    }

}
