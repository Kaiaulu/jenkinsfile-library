package org.mbsoft.jenkins

import org.mbsoft.jenkins.helpers.JenkinsHelper
import org.mbsoft.jenkins.shared.Stage
import org.mbsoft.jenkins.stages.Build
import org.mbsoft.jenkins.stages.Docker
import org.mbsoft.jenkins.stages.GitCheckout
import org.mbsoft.jenkins.stages.Publish

import org.jenkinsci.plugins.workflow.cps.DSL

/**
 * Created by marc on 27/02/17.
 */
class CookbookPipeline implements Serializable {

    def script

    def stages = []

    DSL steps

    JenkinsHelper jenkinsHelper

    static builder(script, DSL steps) {
        return new Builder(script, steps)
    }

    static class Builder implements Serializable {

        def stages = []

        def script

        DSL steps

        JenkinsHelper jenkinsHelper

        Builder(def script, DSL steps) {
            this.script = script
            this.steps = steps
            this.jenkinsHelper = new JenkinsHelper(script)
        }

        def withGitCheckoutStage() {
            stages << new GitCheckout(script, jenkinsHelper)
            return this
        }

        def withBuildStage() {
            stages << new Build(script, jenkinsHelper)
            return this
        }

        def withPublishStage() {
            stages << new Publish(script, jenkinsHelper)
            return this
        }

        def withDockerStage(def image, def path) {
            stages << new Docker(script, jenkinsHelper)
            return this
        }

        def build() {
            return new CookbookPipeline(this)
        }

        def buildDefaultPipeline() {
            withGitCheckoutStage()
            withBuildStage()
            withPublishStage()
            return new CookbookPipeline(this)
        }
    }

    private CookbookPipeline(Builder builder) {
        this.script = builder.script
        this.stages = builder.stages
        this.steps = builder.steps
        this.jenkinsHelper = builder.jenkinsHelper
    }

    void execute() {

        for (Stage stage : stages) {
            try {
                stage.execute()
            }
            catch (err) {
                script.currentBuild.result = "FAILURE"
                script.error "Build failed: ${err.getMessage()}"
            }
        }
    }
}