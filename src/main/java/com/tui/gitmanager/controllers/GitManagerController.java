package com.tui.gitmanager.controllers;

import com.tui.gitmanager.model.GitRepository;
import com.tui.gitmanager.representers.RepositoriesOutputRepresenter;
import com.tui.gitmanager.services.GitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api")
public class GitManagerController {
    private final GitService gitService;

    public GitManagerController(GitService gitService) {
        this.gitService = gitService;
    }

    @GetMapping("/users/{username}/repos")
    public ResponseEntity<Object> listRepositories(@PathVariable String username, Pageable page) throws Exception {

        Page<GitRepository> gitRepositories = gitService.getGithubRepositories(username, page);

        return ResponseEntity.ok().body(new RepositoriesOutputRepresenter(username, gitRepositories));

    }
}