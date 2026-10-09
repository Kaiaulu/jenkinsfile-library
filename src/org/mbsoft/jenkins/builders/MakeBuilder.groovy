package org.mbsoft.jenkins.builders

import org.mbsoft.jenkins.config.LanguageVersion

/**
 * Make builder implementation with version support
 */
class MakeBuilder implements Builder {
    
    def script
    LanguageVersion languageVersion
    
    MakeBuilder(script) {
        this.script = script
    }
    
    @Override
    String getName() {
        return 'Make'
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
