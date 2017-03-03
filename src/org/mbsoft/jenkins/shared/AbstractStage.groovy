package org.mbsoft.jenkins.shared

import org.mbsoft.jenkins.helpers.JenkinsHelper

/**
 * Created by marc on 28/02/17.
 */
abstract class AbstractStage implements Stage {

    def stageName
    def script
    JenkinsHelper jenkinsHelper

    AbstractStage(script, String stageName, JenkinsHelper jenkinsHelper) {
        this.script = script
        this.stageName = stageName
        this.jenkinsHelper = jenkinsHelper
    }

    abstract void execute()

}