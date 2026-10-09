package org.mbsoft.jenkins.builders

import org.mbsoft.jenkins.config.LanguageVersion

/**
 * CMake builder implementation with version support
 */
class CMakeBuilder implements Builder {
    
    def script
    LanguageVersion languageVersion
    
    CMakeBuilder(script) {
        this.script = script
    }
    
    @Override
    String getName() {
        return 'CMake'
    }
    
    @Override
    void setLanguageVersion(LanguageVersion version) {
        this.languageVersion = version
    }
    
    @Override
    LanguageVersion getLanguageVersion() {
        return languageVersion
    }
    
    private String getCMakeVersion() {
        if (languageVersion && languageVersion.hasToolVersion('cmake')) {
            return languageVersion.getToolVersion('cmake')
        }
        return 'latest'
    }
    
    private String getCppStandard() {
        if (!languageVersion) {
            return 'c++14'
        }
        
        String standard = languageVersion.version.toLowerCase()
        if (standard.contains('20')) {
            return 'c++20'
        } else if (standard.contains('17')) {
            return 'c++17'
        } else if (standard.contains('11')) {
            return 'c++11'
        }
        return 'c++14'
    }
    
    @Override
    void build() {
        def cppStandard = getCppStandard()
        script.echo "Building C++ with standard: ${cppStandard}"
        script.sh("mkdir -p build && cd build && cmake -DCMAKE_CXX_STANDARD=${cppStandard.replace('+', '').replace('c', '')} .. && make")
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
