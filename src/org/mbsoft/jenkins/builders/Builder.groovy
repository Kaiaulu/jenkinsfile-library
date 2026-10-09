package org.mbsoft.jenkins.builders

/**
 * Abstract builder interface for different build systems
 */
interface Builder extends Serializable {
    
    /**
     * Get the builder name
     */
    String getName()
    
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
