package org.mbsoft.jenkins.stages

import org.mbsoft.jenkins.builders.Builder
import org.mbsoft.jenkins.helpers.JenkinsHelper
import org.mbsoft.jenkins.shared.AbstractStage

/**
 * Test stage. Delegates implementation to the selected build system.
 */
class Tests extends AbstractStage {

    private final Builder builder

    Tests(Object script, String stageName, JenkinsHelper jenkinsHelper, Builder builder) {
        super(script, stageName, jenkinsHelper)
        this.builder = builder
    }

    @Override
    void execute() {
        script.stage(stageName) {
            builder.test()
        }
    }
}
