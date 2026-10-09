package org.mbsoft.jenkins.builders

/**
 * Make builder implementation (for C/C++)
 */
class MakeBuilder implements Builder {
    
    def script
    
    MakeBuilder(script) {
        this.script = script
    }
    
    @Override
    String getName() {
        return 'Make'
    }
    
    @Override
    void build() {
        script.sh('make clean && make')
    }
    
    @Override
    void test() {
        script.sh('make test')
    }
    
    @Override
    void publish() {
        script.sh('make package')
    }
}
