package com.tui.gitmanager.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tui.gitmanager.exceptions.CustomException;
import com.tui.gitmanager.exceptions.UsernameNotFoundException;
import com.tui.gitmanager.model.GitRepository;
import com.tui.gitmanager.util.EnvVarFetcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GithubAPIServiceImpl implements GithubAPIService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpEntity<String> requestEntity;

    public static final String API_GITHUB_USERS_ENDPOINT = EnvVarFetcher.
            getEnvStrElseDefault("API_GITHUB_USERS_ENDPOINT", "https://api.github.com/users/");
    public static final String API_GITHUB_REPOS_ENDPOINT = EnvVarFetcher.
            getEnvStrElseDefault("API_GITHUB_REPOS_ENDPOINT", "https://api.github.com/repos/");
    public static final String GITHUB_TOKEN = EnvVarFetcher.
            getEnvStrElseDefault("GITHUB_TOKEN", "Bearer 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");

    public GithubAPIServiceImpl() {
        super();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", GITHUB_TOKEN);
        requestEntity = new HttpEntity<>(null, headers);
    }

    @Override
    public Page<GitRepository> getRepositories(String username, Pageable page) throws Exception {
        List<GitRepository> repos = new ArrayList<>();

        try {
            List<Map<String, Object>> userReposResponse = getUserRepos(username, page);

            for (Map<String, Object> repoObj : userReposResponse) {
                Map<String, String> branches = new HashMap<>();
                String repositoryName = (String) repoObj.get("name");
                Boolean fork = (Boolean) repoObj.get("fork");

                if (!fork) {
                    List<Map<String, Object>> userBranchesResponse = getUserBranches(username, repositoryName);

                    for (Map<String, Object> branchObj : userBranchesResponse) {
                        String branchName = branchObj.get("name").toString();
                        HashMap<String, Object> commit = (HashMap<String, Object>) branchObj.get("commit");
                        String shaCommit = commit.get("sha").toString();

                        branches.put(branchName, shaCommit);
                    }

                    repos.add( new GitRepository(repositoryName, branches) );
                }

            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                System.err.println("Error. Username not found within Github services...");
                throw new UsernameNotFoundException("username_not_found", "Username '" + username + "' does not exist on Github services.");
            } else {
                System.err.println("Error while making a request to Github API servers... Error: "  + e.getMessage());
                throw new CustomException("request_error", e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error while making a request to Github API servers... Error: "  + e.getMessage());
            throw new CustomException("server_error", e.getMessage());
        }

        return new PageImpl<>(repos, page, repos.size());
    }


    private List<Map<String, Object>> getUserRepos(String username, Pageable page) throws JsonProcessingException {
        String response = restTemplate.exchange(
                API_GITHUB_USERS_ENDPOINT + username + "/repos?page=" + page.getPageNumber() + "&per_page=" + page.getPageSize(),
                HttpMethod.GET, requestEntity, String.class).getBody();

        return parseReponseDataToList(response);
    }

    private List<Map<String, Object>> getUserBranches(String username, String repositoryName) throws JsonProcessingException {
        String response = restTemplate.exchange(API_GITHUB_REPOS_ENDPOINT + username + "/" + repositoryName + "/branches",
                HttpMethod.GET, requestEntity, String.class).getBody();

        return parseReponseDataToList(response);
    }

    private List<Map<String, Object>> parseReponseDataToList(String response) throws JsonProcessingException {
        return objectMapper.readValue(response, new com.fasterxml.jackson.core.type.TypeReference<>() {});
    }

}
