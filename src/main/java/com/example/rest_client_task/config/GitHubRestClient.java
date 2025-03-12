package com.example.rest_client_task.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GitHubRestClient {

    private final RestClient restClient;

    public GitHubRestClient(@Value("${github.token}") String token, @Value("${github.baseUrl}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", token)
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
    }

    public RestClient getRestClient() {
        return restClient;
    }
}
