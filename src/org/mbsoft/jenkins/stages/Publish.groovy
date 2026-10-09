package org.mbsoft.jenkins.stages

import org.mbsoft.jenkins.builders.Builder
import org.mbsoft.jenkins.helpers.JenkinsHelper
import org.mbsoft.jenkins.shared.AbstractStage

/**
 * Publish stage. Delegates implementation to the selected build system.
 */
class Publish extends AbstractStage {

    private final Builder builder

    Publish(Object script, JenkinsHelper jenkinsHelper, Builder builder) {
        super(script, 'Publish', jenkinsHelper)
        this.builder = builder
    }

    @Override
    void execute() {
        script.stage(stageName) {
            builder.publish()
        }
    }
}
