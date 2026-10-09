package org.mbsoft.jenkins.config

/**
 * Supported programming languages for the Jenkins pipeline library
 */
enum Language {
    JAVA('java'),
    GO('go'),
    CPLUS_PLUS('cpp')

    private String id

    Language(String id) {
        this.id = id
    }

    String getId() {
        return id
    }
    
    /**
     * Get all available builders for this language
     */
    List<BuildSystem> getAvailableBuilders() {
        return BuildSystem.getForLanguage(this)
    }
}
