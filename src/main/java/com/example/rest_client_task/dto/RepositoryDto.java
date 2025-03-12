package com.example.rest_client_task.dto;

import java.util.List;

public record RepositoryDto(String repositoryName, String ownerLogin, List<BranchDto> branches) {
}
