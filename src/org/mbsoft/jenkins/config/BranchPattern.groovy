package org.mbsoft.jenkins.config

/**
 * Branch patterns for conditional pipeline behavior
 */
enum BranchPattern {
    DEVELOP('develop', 'Development branch'),
    FEATURE('^feature\\/.*$', 'Feature branch'),
    RELEASE('^release\\/.*$', 'Release branch'),
    HOTFIX('^hotfix\\/.*$', 'Hotfix branch'),
    MASTER('master', 'Master/Main branch'),
    TAGS('tags', 'Tags'),
    MAIN('main', 'Main branch')

    private String pattern
    private String description

    BranchPattern(String pattern, String description) {
        this.pattern = pattern
        this.description = description
    }

    String getPattern() {
        return pattern
    }

    String getDescription() {
        return description
    }

    /**
     * Check if the given branch name matches this pattern
     */
    boolean matches(String branchName) {
        return branchName.matches(pattern)
    }
}
