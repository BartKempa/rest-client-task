package com.example.rest_client_task.model;

public record Repository(String name, Owner owner, boolean fork) {
    public boolean isFork(){
        return fork;
    }
}

