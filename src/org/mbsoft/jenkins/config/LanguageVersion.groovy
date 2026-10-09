package org.mbsoft.jenkins.config

/**
 * Language version configuration
 * Allows selection of specific tool versions per language
 */
class LanguageVersion implements Serializable {
    
    private Language language
    private String version
    private Map<String, String> additionalTools = [:]

    /**
     * Create a language version configuration
     * @param language The Language enum value
     * @param version The version string (e.g., '11', '1.19', '14')
     */
    LanguageVersion(Language language, String version) {
        this.language = language
        this.version = version
    }

    Language getLanguage() {
        return language
    }

    String getVersion() {
        return version
    }

    /**
     * Add a tool-specific version (e.g., Maven 3.8.1)
     * @param toolName The tool name (e.g., 'maven', 'gradle')
     * @param toolVersion The tool version
     */
    LanguageVersion withTool(String toolName, String toolVersion) {
        additionalTools[toolName.toLowerCase()] = toolVersion
        return this
    }

    /**
     * Get a specific tool version
     */
    String getToolVersion(String toolName) {
        return additionalTools[toolName.toLowerCase()]
    }

    /**
     * Check if a tool version is configured
     */
    boolean hasToolVersion(String toolName) {
        return additionalTools.containsKey(toolName.toLowerCase())
    }

    Map<String, String> getAllToolVersions() {
        return additionalTools.asImmutable()
    }

    @Override
    String toString() {
        return "LanguageVersion(${language.id}-${version})"
    }
}
