package org.mbsoft.jenkins.builders

import org.mbsoft.jenkins.config.LanguageVersion

/**
 * Abstract builder interface for different build systems
 * Now version-aware
 */
interface Builder extends Serializable {
    
    /**
     * Get the builder name
     */
    String getName()
    
    /**
     * Set the language version configuration
     */
    void setLanguageVersion(LanguageVersion version)
    
    /**
     * Get the language version configuration
     */
    LanguageVersion getLanguageVersion()
    
    /**
     * Build command for the project
     */
    void build()
    
    /**
     * Test command for the project
     */
    void test()
    
    /**
     * Publish/package command for the project
     */
    void publish()
}
