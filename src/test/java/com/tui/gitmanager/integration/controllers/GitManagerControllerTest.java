package com.tui.gitmanager.integration.controllers;

import com.tui.gitmanager.controllers.GitManagerController;
import com.tui.gitmanager.model.GitRepository;
import com.tui.gitmanager.services.GitService;
import com.tui.gitmanager.utils.DataMock;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GitManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final GitManagerController gitManagerController;

    @MockBean
    private GitService gitService;

    public GitManagerControllerTest() {
        gitService = Mockito.mock(GitService.class);
        gitManagerController = new GitManagerController(gitService);
    }
    @Before

    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(gitManagerController).build();
    }


    @Test
    public void listRepositories() throws Exception {
        final String username = "prtpereira";
        final int pageNumber = 1;
        final int pageSize = 3;

        final String GET_USER_REPOS = "/api/users/" + username + "/repos";
        final String pagination = "?page=" + pageNumber + "&size=" + pageSize;
        final Pageable page = PageRequest.of(pageNumber, pageSize);

        final List<GitRepository> repos = DataMock.getMockedRepositoriesPrtpereira();

        Mockito.when(gitService.getGithubRepositories(username, page)).thenReturn(new PageImpl<>(repos, page, repos.size()));

        mockMvc.perform(
            MockMvcRequestBuilders
                .get(GET_USER_REPOS + pagination)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.owner_login", Matchers.is(username)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.repositories.content", Matchers.equalTo(repos)))
        ;
    }

}
