package org.mbsoft.jenkins.stages

import org.mbsoft.jenkins.helpers.JenkinsHelper
import org.mbsoft.jenkins.shared.AbstractStage

/**
 * Created by marc on 28/02/17.
 */
class Tests extends AbstractStage {

    Tests(Object script, String stageName, JenkinsHelper jenkinsHelper) {
        super(script, stageName, jenkinsHelper)
    }

    @Override
    void execute() {
        script.stage(stageName) {
            test()
        }
    }

    private test() {
        script.node {
            script.junit('**/target/surefire-reports/TEST-*.xml')
        }
    }
}
