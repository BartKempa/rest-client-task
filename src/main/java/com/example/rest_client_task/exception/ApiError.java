package com.example.rest_client_task.exception;

public record ApiError(int status, String message) {
}
