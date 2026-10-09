package org.mbsoft.jenkins.builders

/**
 * Gradle builder implementation
 */
class GradleBuilder implements Builder {
    
    def script
    
    GradleBuilder(script) {
        this.script = script
    }
    
    @Override
    String getName() {
        return 'Gradle'
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
