
package com.agentic.cicd.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkflowRun {
    private String status;
    private String conclusion;
    private String url;
}
