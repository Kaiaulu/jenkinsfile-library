package org.mbsoft.jenkins.config

/**
 * Supported programming languages for the Jenkins pipeline library
 */
enum Language {
    JAVA('java', ['maven', 'gradle']),
    GO('go', ['go', 'make']),
    CPLUS_PLUS('cpp', ['cmake', 'make', 'gcc'])

    private String id
    private List<String> compatibleBuilders

    Language(String id, List<String> compatibleBuilders) {
        this.id = id
        this.compatibleBuilders = compatibleBuilders
    }

    String getId() {
        return id
    }

    List<String> getCompatibleBuilders() {
        return compatibleBuilders
    }

    boolean supportsBuilder(String builderName) {
        return compatibleBuilders.contains(builderName.toLowerCase())
    }
}
