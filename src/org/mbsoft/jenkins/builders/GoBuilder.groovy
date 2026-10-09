package org.mbsoft.jenkins.builders

import org.mbsoft.jenkins.config.LanguageVersion

/**
 * Go builder implementation with version support
 */
class GoBuilder implements Builder {
    
    def script
    LanguageVersion languageVersion
    
    GoBuilder(script) {
        this.script = script
    }
    
    @Override
    String getName() {
        return 'Go'
    }
    
    @Override
    void setLanguageVersion(LanguageVersion version) {
        this.languageVersion = version
    }
    
    @Override
    LanguageVersion getLanguageVersion() {
        return languageVersion
    }
    
    private String getGoVersion() {
        if (languageVersion) {
            return languageVersion.version
        }
        return 'latest'
    }
    
    @Override
    void build() {
        def goVersion = getGoVersion()
        script.echo "Building with Go ${goVersion}"
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
