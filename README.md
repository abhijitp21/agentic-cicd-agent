# agentic-cicd-intelligent-agent


# AgentRunner CI/CD Monitor

This Spring Boot component (`agentic-cicd-intelligent-agent
`) is designed to monitor GitHub CI/CD workflows. It periodically checks recent workflow runs and triggers diagnostic and alerting services if multiple failures are detected.

---

## 🧠 Mind Map

![agent_runner_mindmap.png](../../../../../Downloads/agent_runner_mindmap.png)

## 🧩 Architecture Overview

### 🔧 Components
- **GitHubService**: Fetches workflow runs and logs from GitHub.
- **DiagnosisService**: Analyzes log data to determine the reason for failure.
- **AlertService**: Sends alerts when a critical failure threshold is met.

---

## ⏱ Scheduled Job

```java
@Scheduled(fixedRate = 60000)
public void runAgent() {
    List<WorkflowRun> runs = gitHubService.fetchRecentRuns();
    long failures = runs.stream().filter(r -> "failure".equalsIgnoreCase(r.getConclusion())).count();

    if (failures >= 3) {
        String logs = gitHubService.fetchLogsForRun(runs.get(0));
        String reason = diagnosisService.analyze(logs);
        alertService.send("🚨 3+ CI failures detected. Reason: " + reason);
        gitHubService.createGitHubIssue("CI/CD Failure Diagnosed", reason);
    }
}
```

---

## 🔁 Workflow Summary

1. Fetch recent workflow runs from GitHub.
2. Count the number of failed runs.
3. If failures >= 3:
    - Fetch logs from the latest run.
    - Diagnose the failure reason.
    - Send an alert notification.
    - Create a GitHub issue with the analysis.

---

## 🧾 Requirements

- Java 17+
- Spring Boot 3.x
- Scheduler enabled via `@Scheduled`
- Services:
    - GitHubService
    - DiagnosisService
    - AlertService
