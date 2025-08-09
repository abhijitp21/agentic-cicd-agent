package com.agentic.cicd;

import com.agentic.cicd.model.WorkflowRun;
import com.agentic.cicd.service.AlertService;
import com.agentic.cicd.service.DiagnosisService;
import com.agentic.cicd.service.GitHubService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The {@code AgentRunner} class is responsible for monitoring CI/CD workflows executed via GitHub.
 * It periodically checks for recent workflow runs, analyzes failures, alerts the appropriate team,
 * and creates GitHub issues if multiple pipeline failures are detected.
 */
@Component
public class AgentRunner {

    private final GitHubService gitHubService;
    private final DiagnosisService diagnosisService;
    private final AlertService alertService;

    /**
     * Constructs an {@code AgentRunner} object with the required dependent services.
     *
     * @param gitHubService    the service for interacting with GitHub workflows and issues
     * @param diagnosisService the service for diagnosing issues based on workflow logs
     * @param alertService     the service for sending alerts to relevant stakeholders
     */
    public AgentRunner(GitHubService gitHubService, DiagnosisService diagnosisService, AlertService alertService) {
        this.gitHubService = gitHubService;
        this.diagnosisService = diagnosisService;
        this.alertService = alertService;
    }

    /**
     * Periodically monitors CI/CD workflow runs and takes action on multiple pipeline failures.
     * <p>
     * This method:
     * <ul>
     *     <li>Fetches the latest workflow runs from GitHub</li>
     *     <li>Counts the failures among the recent runs</li>
     *     <li>If 3 or more failures are detected:
     *         <ul>
     *             <li>Fetches logs of the latest workflow run</li>
     *             <li>Analyzes the logs to diagnose the issue</li>
     *             <li>Sends an alert with the diagnosis</li>
     *             <li>Creates a GitHub issue to document the problem</li>
     *         </ul>
     *     </li>
     * </ul>
     * </p>
     *
     * The method is scheduled to execute every 60 seconds using the {@link Scheduled} annotation.
     */
    @Scheduled(fixedRate = 60000)
    public void runAgent() {
        // Fetch recent workflow runs
        List<WorkflowRun> runs = gitHubService.fetchRecentRuns();

        // Count the number of runs that failed
        long failures = runs.stream().filter(r -> "failure".equalsIgnoreCase(r.getConclusion())).count();

        // If 3 or more failures are detected, take further diagnostic and alerting actions
        if (failures >= 3) {
            // Fetch logs of the latest workflow run
            String logs = gitHubService.fetchLogsForRun(runs.get(0));

            // Analyze the logs to determine the reason for failure
            String reason = diagnosisService.analyze(logs);

            // Send an alert to notify stakeholders about the failures
            alertService.send("🚨 3+ CI failures detected. Reason: " + reason);

            // Create a GitHub issue to document the detected problem
            gitHubService.createGitHubIssue("CI/CD Failure Diagnosed", reason);
        }
    }
}