package org.mbsoft.jenkins.stages

import hudson.plugins.git.GitSCM
import org.mbsoft.jenkins.helpers.JenkinsHelper
import org.mbsoft.jenkins.shared.AbstractStage

/**
 * Created by marc on 28/02/17.
 */
class Docker extends AbstractStage {

    def image // maven/3-jdk-8
    def params // -p 8080:8080
    def commands // cp x x

    Docker(Object script, JenkinsHelper jenkinsHelper, String image, String params, commands) {
        super(script, 'Docker', jenkinsHelper)
        this.image = image
        this.params = params
        this.commands = commands
    }

    @Override
    void execute() {
        script.stage(stageName) {
            docker()
        }
    }

    private docker(){
        script.node {
            script.echo("Creating Docker image : ${image} with params : ${params}")
            def docker = script.docker.image(image)
            docker.inside(params) {
                script.sh(commands)
            }
        }
    }
}