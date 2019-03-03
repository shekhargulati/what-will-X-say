package com.shekhargulati.wwxs;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WhatWillXSayApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void should_find_time_to_read_for_github_page() {
        webTestClient
                .get().uri("/api/v1/suggest?input=congress")
                .accept(MediaType.valueOf("image/svg+xml"))
                .exchange()
                .expectStatus().isOk();
    }

}