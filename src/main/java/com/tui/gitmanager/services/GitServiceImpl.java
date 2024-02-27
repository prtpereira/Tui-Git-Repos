package com.tui.gitmanager.services;

import com.tui.gitmanager.model.GitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GitServiceImpl implements GitService {

    private final GithubAPIService githubAPIService;

    public GitServiceImpl(GithubAPIService githubAPIService) {
        this.githubAPIService = githubAPIService;
    }

    @Override
    public Page<GitRepository> getGithubRepositories(String username, Pageable page) throws Exception {

        return githubAPIService.getRepositories(username, page);
    }

}
