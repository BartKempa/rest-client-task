package com.example.rest_client_task.controller;

import com.example.rest_client_task.dto.RepositoryDto;
import com.example.rest_client_task.service.GitHubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repositories")
public class GitHubController {

    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/{username}")
    ResponseEntity<List<RepositoryDto>> getUserRepositories(@PathVariable String username){
        return ResponseEntity.ok(gitHubService.getUserRepositoryDto(username));
    }
}
