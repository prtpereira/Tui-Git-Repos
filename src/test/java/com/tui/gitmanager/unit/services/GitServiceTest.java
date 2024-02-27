package com.tui.gitmanager.unit.services;

import com.tui.gitmanager.model.GitRepository;
import com.tui.gitmanager.services.GitService;
import com.tui.gitmanager.services.GithubAPIService;
import com.tui.gitmanager.utils.DataMock;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class GitServiceTest {

    @Autowired
    private GithubAPIService githubAPIService;

    @Test
    public void getRepositories() throws Exception {
        final String username = "prtpereira";
        final Pageable page = PageRequest.of(1, 3);

        final List<GitRepository> expectedRepositories = DataMock.getMockedRepositoriesPrtpereira();

        Page<GitRepository> pageableRepositories = githubAPIService.getRepositories(username, page);

        Assertions.assertEquals(1, pageableRepositories.getNumber());
        Assertions.assertEquals(3, pageableRepositories.getSize());
        Assertions.assertEquals(expectedRepositories, pageableRepositories.getContent());
    }

}