package com.tui.gitmanager.representers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tui.gitmanager.model.GitRepository;
import org.springframework.data.domain.Page;

public class RepositoriesOutputRepresenter {

    private Page<GitRepository> repositories;
    @JsonProperty(value = "owner_login")
    private String ownerLogin;

    public RepositoriesOutputRepresenter(String ownerLogin, Page<GitRepository> repositories) {
        this.ownerLogin = ownerLogin;
        this.repositories = repositories;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public Page<GitRepository> getRepositories() {
        return repositories;
    }

    public void setRepositories(Page<GitRepository> repositories) {
        this.repositories = repositories;
    }

}
