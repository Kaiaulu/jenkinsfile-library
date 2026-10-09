package org.mbsoft.jenkins.config

/**
 * Branch-based pipeline configuration
 * Determines which stages run based on the current Git branch
 */
class BranchConfig implements Serializable {
    
    private def script
    private String currentBranch
    private Map<BranchPattern, Set<String>> stagesByBranch = [:]

    BranchConfig(script) {
        this.script = script
        this.currentBranch = detectBranch()
    }

    /**
     * Detect the current Git branch from Jenkins environment
     */
    private String detectBranch() {
        // Try common Jenkins branch variables
        String branch = script.env.BRANCH_NAME ?: 
                       script.env.GIT_BRANCH ?: 
                       script.env.GIT_LOCAL_BRANCH ?: 
                       'unknown'
        
        // Clean up branch name (remove 'origin/' prefix if present)
        if (branch.startsWith('origin/')) {
            branch = branch.substring(7)
        }
        
        script.echo "Detected branch: ${branch}"
        return branch
    }

    /**
     * Get the current branch name
     */
    String getCurrentBranch() {
        return currentBranch
    }

    /**
     * Check if current branch matches a specific pattern
     */
    boolean isBranch(BranchPattern pattern) {
        return pattern.matches(currentBranch)
    }

    /**
     * Check if current branch is a feature branch
     */
    boolean isFeatureBranch() {
        return isBranch(BranchPattern.FEATURE)
    }

    /**
     * Check if current branch is develop
     */
    boolean isDevelopBranch() {
        return isBranch(BranchPattern.DEVELOP)
    }

    /**
     * Check if current branch is master or main
     */
    boolean isMainBranch() {
        return isBranch(BranchPattern.MASTER) || isBranch(BranchPattern.MAIN)
    }

    /**
     * Check if current branch is a release branch
     */
    boolean isReleaseBranch() {
        return isBranch(BranchPattern.RELEASE)
    }

    /**
     * Check if current branch is a hotfix branch
     */
    boolean isHotfixBranch() {
        return isBranch(BranchPattern.HOTFIX)
    }
}
