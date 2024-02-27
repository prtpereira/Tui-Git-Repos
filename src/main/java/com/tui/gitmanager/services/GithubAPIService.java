package com.tui.gitmanager.services;

import com.tui.gitmanager.model.GitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GithubAPIService {

    public Page<GitRepository> getRepositories(String username, Pageable page) throws Exception;
}
