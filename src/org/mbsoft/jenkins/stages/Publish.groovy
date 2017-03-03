package org.mbsoft.jenkins.stages

import org.mbsoft.jenkins.helpers.JenkinsHelper
import org.mbsoft.jenkins.shared.AbstractStage
import hudson.plugins.git.GitSCM

/**
 * Created by marc on 28/02/17.
 */
class Publish extends AbstractStage {

    Publish(Object script, JenkinsHelper jenkinsHelper) {
        super(script, 'Publish', jenkinsHelper)
    }

    @Override
    void execute() {
        script.stage(stageName) {
            publish()
        }
    }

    private publish(){
        script.node {
            def mvnHome = script.tool('Maven 3')
            def gitBranch = ((GitSCM)script.scm).getBranches().get(0).getName()
            if (gitBranch.matches(Branches.DEVELOP.branch()) || gitBranch.matches(Branches.TAGS.branch())) {
                script.echo("Branch is ${gitBranch}, publishing")
                script.sh("'${mvnHome}/bin/mvn' -DskipTests deploy")
            }
            else {
                script.echo("Branch is ${gitBranch}, NOT publishing")
            }
        }
    }
}