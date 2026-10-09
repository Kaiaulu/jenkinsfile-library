package org.mbsoft.jenkins

import org.mbsoft.jenkins.config.Language
import org.mbsoft.jenkins.config.BuilderFactory
import org.mbsoft.jenkins.builders.Builder
import org.mbsoft.jenkins.helpers.JenkinsHelper
import org.mbsoft.jenkins.shared.Stage
import org.mbsoft.jenkins.stages.Build
import org.mbsoft.jenkins.stages.GitCheckout
import org.mbsoft.jenkins.stages.Publish
import org.mbsoft.jenkins.stages.Tests

/**
 * Jenkins Pipeline Global Library DSL
 * 
 * Provides a simple, fluent interface for building Jenkins declarative pipelines
 * with language and build tool selection.
 * 
 * Usage:
 *   @Library('jenkinsfile-library') _
 *   def pipeline = new org.mbsoft.jenkins.CookbookPipeline(this)
 *   pipeline.language(Language.JAVA)
 *           .builder('maven')
 *           .gitCheckout()
 *           .build()
 *           .test()
 *           .publish()
 *           .execute()
 */
class CookbookPipeline implements Serializable {

    def script
    List<Stage> stages = []
    JenkinsHelper jenkinsHelper
    Language language
    Builder builder

    CookbookPipeline(script) {
        this.script = script
        this.stages = []
        this.jenkinsHelper = new JenkinsHelper(script)
    }

    /**
     * Sets the programming language for the project
     * @param lang The Language enum value
     */
    def language(Language lang) {
        this.language = lang
        script.echo "Pipeline configured for language: ${lang.id}"
        return this
    }

    /**
     * Sets the build tool/builder for the project
     * Validates that the builder is compatible with the selected language
     * @param builderName The name of the builder (e.g., 'maven', 'gradle', 'go')
     * @throws IllegalArgumentException if builder is not compatible with language
     */
    def builder(String builderName) {
        if (!language) {
            throw new IllegalStateException('Language must be set before builder. Call .language() first.')
        }
        
        this.builder = BuilderFactory.createBuilder(language, builderName, script)
        script.echo "Builder configured: ${builder.name} (compatible with ${language.id})"
        return this
    }

    /**
     * Adds a Git checkout stage
     */
    def gitCheckout() {
        stages << new GitCheckout(script, jenkinsHelper)
        return this
    }

    /**
     * Adds a build stage
     * Uses the configured builder
     */
    def build() {
        if (!builder) {
            throw new IllegalStateException('Builder must be configured before build stage. Call .builder() first.')
        }
        stages << new Build(script, jenkinsHelper, builder)
        return this
    }

    /**
     * Adds a test stage
     * Uses the configured builder
     */
    def test() {
        if (!builder) {
            throw new IllegalStateException('Builder must be configured before test stage. Call .builder() first.')
        }
        stages << new Tests(script, 'Test', jenkinsHelper, builder)
        return this
    }

    /**
     * Adds a publish/deploy stage
     * Uses the configured builder
     */
    def publish() {
        if (!builder) {
            throw new IllegalStateException('Builder must be configured before publish stage. Call .builder() first.')
        }
        stages << new Publish(script, jenkinsHelper, builder)
        return this
    }

    /**
     * Executes all configured stages
     */
    def execute() {
        for (Stage stage : stages) {
            try {
                stage.execute()
            }
            catch (err) {
                script.currentBuild.result = 'FAILURE'
                script.error "Build failed: ${err.getMessage()}"
            }
        }
    }

    /**
     * Executes the default pipeline for the language and builder
     */
    def executeDefault() {
        if (!language) {
            throw new IllegalStateException('Language must be set before executing. Call .language() first.')
        }
        if (!builder) {
            throw new IllegalStateException('Builder must be set before executing. Call .builder() first.')
        }
        
        gitCheckout()
        build()
        test()
        publish()
        execute()
    }
}
