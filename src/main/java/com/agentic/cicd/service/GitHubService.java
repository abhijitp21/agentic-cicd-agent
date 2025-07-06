package com.agentic.cicd.service;

import com.agentic.cicd.model.WorkflowRun;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GitHubService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String token = "YOUR_GITHUB_TOKEN";
    private final String repo = "YOUR_ORG/YOUR_REPO";

    public List<WorkflowRun> fetchRecentRuns() {
        String url = "https://api.github.com/repos/" + repo + "/actions/runs";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            url, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {}
        );
        var workflowRuns = (List<Map<String, Object>>) response.getBody().get("workflow_runs");

        return workflowRuns.stream().limit(5).map(run -> new WorkflowRun(
            (String) run.get("status"),
            (String) run.get("conclusion"),
            (String) run.get("html_url")
        )).toList();
    }

    public String fetchLogsForRun(WorkflowRun run) {
        return "Sample log output from failed CI/CD pipeline for diagnosis...";
    }

    public void createGitHubIssue(String title, String body) {
        String url = "https://api.github.com/repos/" + repo + "/issues";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> payload = Map.of("title", title, "body", body);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);
        restTemplate.postForEntity(url, entity, String.class);
    }
}
