package org.mbsoft.jenkins.stages

import org.mbsoft.jenkins.helpers.JenkinsHelper
import org.mbsoft.jenkins.shared.AbstractStage
import hudson.plugins.git.GitSCM

/**
 * Created by marc on 28/02/17.
 */
class Build extends AbstractStage {

    Build(Object script, JenkinsHelper jenkinsHelper) {
        super(script, 'Build', jenkinsHelper);
    }

    @Override
    void execute() {
        script.stage(stageName) {
            build();
        }
    }

    private build(){
        script.node {
            def mvnHome = script.tool('Maven 3')
            def gitBranch = ((GitSCM)script.scm).getBranches().get(0).getName()
            if (gitBranch.matches(Branches.DEVELOP.branch()) || gitBranch.matches(Branches.FEATURE.branch())) {
                script.echo("${gitBranch} branch, omitting SNAPSHOT check")
                script.sh("'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean install")
            }
            else {
                script.echo("${gitBranch} branch, checking for SNAPSHOT")
                script.sh("'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean install -Prelease")
            }
        }
    }
}