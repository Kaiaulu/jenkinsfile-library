package org.mbsoft.jenkins.builders

import org.mbsoft.jenkins.config.LanguageVersion

/**
 * Gradle builder implementation with version support
 */
class GradleBuilder implements Builder {
    
    def script
    LanguageVersion languageVersion
    
    GradleBuilder(script) {
        this.script = script
    }
    
    @Override
    String getName() {
        return 'Gradle'
    }
    
    @Override
    void setLanguageVersion(LanguageVersion version) {
        this.languageVersion = version
    }
    
    @Override
    LanguageVersion getLanguageVersion() {
        return languageVersion
    }
    
    @Override
    void build() {
        script.sh('./gradlew clean build -x test')
    }
    
    @Override
    void test() {
        script.sh('./gradlew test')
    }
    
    @Override
    void publish() {
        script.sh('./gradlew publish')
    }
}
