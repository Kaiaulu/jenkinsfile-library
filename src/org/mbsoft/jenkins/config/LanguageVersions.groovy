package org.mbsoft.jenkins.config

/**
 * Pre-defined language version configurations
 * Provides common version combinations for Java, Go, and C++
 */
class LanguageVersions {
    
    // Java versions
    static final LanguageVersion JAVA_8 = new LanguageVersion(Language.JAVA, '8')
            .withTool('maven', '3.8.1')
            .withTool('gradle', '7.6')
    
    static final LanguageVersion JAVA_11 = new LanguageVersion(Language.JAVA, '11')
            .withTool('maven', '3.8.1')
            .withTool('gradle', '7.6')
    
    static final LanguageVersion JAVA_17 = new LanguageVersion(Language.JAVA, '17')
            .withTool('maven', '3.9.2')
            .withTool('gradle', '8.1')
    
    static final LanguageVersion JAVA_21 = new LanguageVersion(Language.JAVA, '21')
            .withTool('maven', '3.9.2')
            .withTool('gradle', '8.1')
    
    // Go versions
    static final LanguageVersion GO_1_16 = new LanguageVersion(Language.GO, '1.16')
    static final LanguageVersion GO_1_19 = new LanguageVersion(Language.GO, '1.19')
    static final LanguageVersion GO_1_20 = new LanguageVersion(Language.GO, '1.20')
    static final LanguageVersion GO_1_21 = new LanguageVersion(Language.GO, '1.21')
    
    // C++ versions
    static final LanguageVersion CPP_11 = new LanguageVersion(Language.CPLUS_PLUS, 'C++11')
            .withTool('cmake', '3.20')
    
    static final LanguageVersion CPP_17 = new LanguageVersion(Language.CPLUS_PLUS, 'C++17')
            .withTool('cmake', '3.20')
    
    static final LanguageVersion CPP_20 = new LanguageVersion(Language.CPLUS_PLUS, 'C++20')
            .withTool('cmake', '3.24')
}
