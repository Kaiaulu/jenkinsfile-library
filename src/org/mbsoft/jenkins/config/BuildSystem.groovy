package org.mbsoft.jenkins.config

/**
 * Supported build systems, grouped by compatible language
 */
enum BuildSystem {
    // Java builders
    MAVEN('maven', Language.JAVA, 'Maven Build System'),
    GRADLE('gradle', Language.JAVA, 'Gradle Build System'),
    
    // Go builders
    GO('go', Language.GO, 'Go Build System'),
    GO_MAKE('make', Language.GO, 'Make Build System (Go)'),
    
    // C++ builders
    CMAKE('cmake', Language.CPLUS_PLUS, 'CMake Build System'),
    CPP_MAKE('make', Language.CPLUS_PLUS, 'Make Build System (C++)')

    private String id
    private Language language
    private String description

    BuildSystem(String id, Language language, String description) {
        this.id = id
        this.language = language
        this.description = description
    }

    String getId() {
        return id
    }

    Language getLanguage() {
        return language
    }

    String getDescription() {
        return description
    }
    
    /**
     * Get all builders compatible with a specific language
     */
    static List<BuildSystem> getForLanguage(Language lang) {
        return values().findAll { it.language == lang }
    }
}
