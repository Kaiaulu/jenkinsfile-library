package org.mbsoft.jenkins.builders

/**
 * CMake builder implementation (for C/C++)
 */
class CMakeBuilder implements Builder {
    
    def script
    
    CMakeBuilder(script) {
        this.script = script
    }
    
    @Override
    String getName() {
        return 'CMake'
    }
    
    @Override
    void build() {
        script.sh('mkdir -p build && cd build && cmake .. && make')
    }
    
    @Override
    void test() {
        script.sh('cd build && ctest')
    }
    
    @Override
    void publish() {
        script.sh('cd build && make package')
    }
}
