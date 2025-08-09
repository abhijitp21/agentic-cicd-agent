package com.agentic.cicd.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import com.agentic.cicd.model.WorkflowRun;

import java.util.List;
import java.util.Map;

public class GitHubService {

    @Value("${github.token}")
    private String token;

    @Value("${github.repository}")
    private String repo;

    private final WebClient webClient;

    public GitHubService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.github.com/repos/" + repo)
                .defaultHeader("Authorization", "Bearer " + token)
                .defaultHeader("Accept", "application/vnd.github+json")
                .build();
    }

    public List<WorkflowRun> fetchRecentRuns() {
        Map<String, Object> response = webClient.get()
                .uri("/actions/runs")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        var workflowRuns = (List<Map<String, Object>>) response.get("workflow_runs");

        return workflowRuns.stream().limit(5).map(run -> new WorkflowRun(
                (String) run.get("status"),
                (String) run.get("conclusion"),
                (String) run.get("html_url")
        )).toList();
    }

    public String fetchLogsForRun(WorkflowRun run) {
        // Replace with an actual API call if required
        return "Sample log output from failed CI/CD pipeline for diagnosis...";
    }

    public void createGitHubIssue(String title, String body) {
        Map<String, String> payload = Map.of("title", title, "body", body);

        webClient.post()
                .uri("/issues")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}