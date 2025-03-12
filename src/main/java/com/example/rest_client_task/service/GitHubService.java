package com.example.rest_client_task.service;

import com.example.rest_client_task.config.GitHubRestClient;
import com.example.rest_client_task.dto.BranchDto;
import com.example.rest_client_task.dto.RepositoryDto;
import com.example.rest_client_task.exception.UserNotFoundException;
import com.example.rest_client_task.model.Branch;
import com.example.rest_client_task.model.Repository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GitHubService {

    private final RestClient restClient;

    public GitHubService(GitHubRestClient gitHubRestClient) {

        this.restClient = gitHubRestClient.getRestClient();
    }

    public List<RepositoryDto> getUserRepositoryDto(String username){
        List<Repository> repositoryList = getUserRepositories(username);
        return repositoryList
                .stream()
                .filter(repository -> !repository.isFork())
                .map(this::getRepositoryDto).toList();
    }

    private RepositoryDto getRepositoryDto(Repository repository) {
        return new RepositoryDto(
                repository.name(),
                repository.owner().login(),
                getBranchDtoList(repository.owner().login(), repository.name()));
    }

    private List<BranchDto> getBranchDtoList(String repositoryOwner, String repositoryName) {
        List<Branch> branchList = getBranchList(repositoryOwner, repositoryName);
        return branchList.stream()
                .map(GitHubService::getBranchDto)
                .toList();
    }

    private static BranchDto getBranchDto(Branch branch) {
        return new BranchDto(branch.name(), branch.commit().sha());
    }

    private List<Branch> getBranchList(String username, String repositoryName) {
        return restClient.get()
                .uri("/repos/{owner}/{repo}/branches", username, repositoryName)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    private List<Repository> getUserRepositories(String username){
        return restClient.get()
                .uri("/users/{USERNAME}/repos", username)
                .retrieve()
                .onStatus(
                        httpStatusCode -> httpStatusCode.value()  == 404,
                        ((request, response) -> {
                            throw new UserNotFoundException("Username does not exist");
                        }))
                .body(new ParameterizedTypeReference<>() {});
    }
}

