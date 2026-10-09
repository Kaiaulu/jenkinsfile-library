package org.mbsoft.jenkins.builders

/**
 * Go builder implementation
 */
class GoBuilder implements Builder {
    
    def script
    
    GoBuilder(script) {
        this.script = script
    }
    
    @Override
    String getName() {
        return 'Go'
    }
    
    @Override
    void build() {
        script.sh('go build ./...')
    }
    
    @Override
    void test() {
        script.sh('go test ./...')
    }
    
    @Override
    void publish() {
        script.sh('go build ./... && tar -czf artifact.tar.gz .')
    }
}
