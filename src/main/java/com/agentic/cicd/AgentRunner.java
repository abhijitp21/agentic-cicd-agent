package com.agentic.cicd;

import com.agentic.cicd.model.WorkflowRun;
import com.agentic.cicd.service.AlertService;
import com.agentic.cicd.service.DiagnosisService;
import com.agentic.cicd.service.GitHubService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AgentRunner {

    private final GitHubService gitHubService;
    private final DiagnosisService diagnosisService;
    private final AlertService alertService;

    public AgentRunner(GitHubService gitHubService, DiagnosisService diagnosisService, AlertService alertService) {
        this.gitHubService = gitHubService;
        this.diagnosisService = diagnosisService;
        this.alertService = alertService;
    }

    @Scheduled(fixedRate = 60000)
    public void runAgent() {
        List<WorkflowRun> runs = gitHubService.fetchRecentRuns();
        long failures = runs.stream().filter(r -> "failure".equalsIgnoreCase(r.getConclusion())).count();

        if (failures >= 3) {
            String logs = gitHubService.fetchLogsForRun(runs.get(0));
            String reason = diagnosisService.analyze(logs);
            alertService.send("🚨 3+ CI failures detected.
Reason: " + reason);
            gitHubService.createGitHubIssue("CI/CD Failure Diagnosed", reason);
        }
    }
}
