package com.agentic.cicd.service;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class DiagnosisService {

    private final OpenAiChatModel model = OpenAiChatModel.withApiKey("YOUR_OPENAI_API_KEY");

    public String analyze(String logs) {
        String prompt = "Analyze this CI/CD failure log and explain the root cause:
" + logs;
        return model.generate(prompt);
    }
}
