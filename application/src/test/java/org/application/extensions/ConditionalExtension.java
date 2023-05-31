package org.application.extensions;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ConditionalExtension implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext extensionContext) {
        var res = Math.random();

        System.out.println("===");
        System.out.println(res);
        System.out.println("===");

        if(res>0.5){
            return ConditionEvaluationResult.enabled("Test enabled");
        }
        return ConditionEvaluationResult.disabled("Test disabled");
    }
}
