package org.mbsoft.jenkins.builders

/**
 * Maven builder implementation
 */
class MavenBuilder implements Builder {
    
    def script
    
    MavenBuilder(script) {
        this.script = script
    }
    
    @Override
    String getName() {
        return 'Maven'
    }
    
    @Override
    void build() {
        def mvnHome = script.tool('Maven 3')
        script.sh("'${mvnHome}/bin/mvn' clean install -DskipTests")
    }
    
    @Override
    void test() {
        def mvnHome = script.tool('Maven 3')
        script.sh("'${mvnHome}/bin/mvn' test")
    }
    
    @Override
    void publish() {
        def mvnHome = script.tool('Maven 3')
        script.sh("'${mvnHome}/bin/mvn' deploy -DskipTests")
    }
}
