package org.mbsoft.jenkins.builders

import org.mbsoft.jenkins.config.LanguageVersion

/**
 * Maven builder implementation with version support
 */
class MavenBuilder implements Builder {
    
    def script
    LanguageVersion languageVersion
    
    MavenBuilder(script) {
        this.script = script
    }
    
    @Override
    String getName() {
        return 'Maven'
    }
    
    @Override
    void setLanguageVersion(LanguageVersion version) {
        this.languageVersion = version
    }
    
    @Override
    LanguageVersion getLanguageVersion() {
        return languageVersion
    }
    
    private String getMavenHome() {
        String mavenVersion = 'Maven 3'
        if (languageVersion && languageVersion.hasToolVersion('maven')) {
            mavenVersion = "Maven ${languageVersion.getToolVersion('maven')}"
        }
        return script.tool(mavenVersion)
    }
    
    private String getJavaOptions() {
        if (!languageVersion) {
            return ''
        }
        
        String javaVersion = languageVersion.version
        if (javaVersion == '8') {
            return '-source 1.8 -target 1.8'
        } else {
            return "-source ${javaVersion} -target ${javaVersion}"
        }
    }
    
    @Override
    void build() {
        def mvnHome = getMavenHome()
        script.sh("'${mvnHome}/bin/mvn' clean install -DskipTests")
    }
    
    @Override
    void test() {
        def mvnHome = getMavenHome()
        script.sh("'${mvnHome}/bin/mvn' test")
    }
    
    @Override
    void publish() {
        def mvnHome = getMavenHome()
        script.sh("'${mvnHome}/bin/mvn' deploy -DskipTests")
    }
}
