package org.mbsoft.jenkins.stages

import org.mbsoft.jenkins.config.BranchPattern
import org.mbsoft.jenkins.shared.Stage

/**
 * Wrapper for conditional stage execution based on branch patterns
 * Allows stages to run only on specific branches
 */
class ConditionalStage implements Stage {
    
    private Stage wrappedStage
    private Closure<Boolean> predicate
    private List<BranchPattern> allowedBranches

    ConditionalStage(Stage wrappedStage, Closure<Boolean> predicate, List<BranchPattern> allowedBranches) {
        this.wrappedStage = wrappedStage
        this.predicate = predicate
        this.allowedBranches = allowedBranches
    }

    @Override
    void execute() {
        boolean shouldExecute = allowedBranches.any { pattern ->
            predicate(pattern)
        }
        
        if (shouldExecute) {
            wrappedStage.execute()
        } else {
            def script = wrappedStage.script
            script.echo "Skipping stage (${wrappedStage.stageName}) - not applicable for this branch"
        }
    }
}
