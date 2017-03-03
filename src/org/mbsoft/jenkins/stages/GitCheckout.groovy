package org.mbsoft.jenkins.stages

import org.mbsoft.jenkins.helpers.JenkinsHelper
import org.mbsoft.jenkins.shared.AbstractStage

/**
 * Created by marc on 28/02/17.
 */
class GitCheckout extends AbstractStage {

    GitCheckout(Object script, JenkinsHelper jenkinsHelper) {
        super(script, 'Git Checkout', jenkinsHelper)
    }

    @Override
    void execute() {
        script.stage(stageName) {
            git()
        }
    }

    private git() {
        script.node {
            script.checkout(script.scm)
        }
    }
}