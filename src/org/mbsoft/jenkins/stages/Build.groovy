package org.mbsoft.jenkins.stages

import org.mbsoft.jenkins.builders.Builder
import org.mbsoft.jenkins.helpers.JenkinsHelper
import org.mbsoft.jenkins.shared.AbstractStage

/**
 * Build stage. Delegates implementation to the selected build system.
 */
class Build extends AbstractStage {

    private final Builder builder

    Build(Object script, JenkinsHelper jenkinsHelper, Builder builder) {
        super(script, 'Build', jenkinsHelper)
        this.builder = builder
    }

    @Override
    void execute() {
        script.stage(stageName) {
            builder.build()
        }
    }
}
