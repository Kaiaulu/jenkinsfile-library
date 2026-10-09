package org.mbsoft.jenkins

import org.mbsoft.jenkins.config.Language
import org.mbsoft.jenkins.config.BuildSystem
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
 * The API is type-safe: you can only select a BuildSystem that is compatible
 * with the chosen Language.
 * 
 * Usage with Language and BuildSystem enums:
 *   @Library('jenkinsfile-library') _
 *   def pipeline = new org.mbsoft.jenkins.CookbookPipeline(this)
 *   pipeline.language(Language.JAVA)
 *           .buildSystem(BuildSystem.MAVEN)
 *           .gitCheckout()
 *           .build()
 *           .test()
 *           .publish()
 *           .execute()
 * 
 * Or use the shorthand default pipeline:
 *   @Library('jenkinsfile-library') _
 *   new org.mbsoft.jenkins.CookbookPipeline(this)
 *       .language(Language.GO)
 *       .buildSystem(BuildSystem.GO)
 *       .executeDefault()
 */
class CookbookPipeline implements Serializable {

    def script
    List<Stage> stages = []
    JenkinsHelper jenkinsHelper
    Language language
    BuildSystem buildSystem
    Builder builder

    CookbookPipeline(script) {
        this.script = script
        this.stages = []
        this.jenkinsHelper = new JenkinsHelper(script)
    }

    /**
     * Sets the programming language for the project
     * @param lang The Language enum value (JAVA, GO, CPLUS_PLUS)
     */
    def language(Language lang) {
        this.language = lang
        script.echo "Pipeline configured for language: ${lang.id}"
        return this
    }

    /**
     * Sets the build system for the project
     * Validates that the build system is compatible with the selected language
     * @param buildSys The BuildSystem enum value
     * @throws IllegalArgumentException if build system is not compatible with language
     * @throws IllegalStateException if language is not set
     */
    def buildSystem(BuildSystem buildSys) {
        if (!language) {
            throw new IllegalStateException('Language must be set before build system. Call .language() first.')
        }
        
        BuilderFactory.validateCompatibility(language, buildSys)
        
        this.buildSystem = buildSys
        this.builder = BuilderFactory.createBuilder(buildSys, script)
        script.echo "Build system configured: ${buildSys.id} (${buildSys.description})"
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
            throw new IllegalStateException('Build system must be configured before build stage. Call .buildSystem() first.')
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
            throw new IllegalStateException('Build system must be configured before test stage. Call .buildSystem() first.')
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
            throw new IllegalStateException('Build system must be configured before publish stage. Call .buildSystem() first.')
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
     * Executes the default pipeline for the language and build system
     */
    def executeDefault() {
        if (!language) {
            throw new IllegalStateException('Language must be set before executing. Call .language() first.')
        }
        if (!buildSystem) {
            throw new IllegalStateException('Build system must be set before executing. Call .buildSystem() first.')
        }
        
        gitCheckout()
        build()
        test()
        publish()
        execute()
    }
}
