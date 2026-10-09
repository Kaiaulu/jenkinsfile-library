package org.mbsoft.jenkins

import org.mbsoft.jenkins.config.Language
import org.mbsoft.jenkins.config.BuildSystem
import org.mbsoft.jenkins.config.BranchConfig
import org.mbsoft.jenkins.config.BranchPattern
import org.mbsoft.jenkins.config.LanguageVersion
import org.mbsoft.jenkins.config.BuilderFactory
import org.mbsoft.jenkins.builders.Builder
import org.mbsoft.jenkins.helpers.JenkinsHelper
import org.mbsoft.jenkins.shared.Stage
import org.mbsoft.jenkins.stages.Build
import org.mbsoft.jenkins.stages.GitCheckout
import org.mbsoft.jenkins.stages.Publish
import org.mbsoft.jenkins.stages.Tests
import org.mbsoft.jenkins.stages.ConditionalStage

/**
 * Jenkins Pipeline Global Library DSL
 * 
 * Provides a simple, fluent interface for building Jenkins declarative pipelines
 * with language and build tool selection, language version configuration, and
 * branch-aware stage execution.
 * 
 * The API is type-safe: you can only select a BuildSystem that is compatible
 * with the chosen Language.
 * 
 * Branch-aware execution allows different behavior based on the current Git branch:
 * - Skip publish on feature branches
 * - Run integration tests only on develop/main
 * - Run benchmarks only on release branches
 * 
 * Language version selection allows specifying exact tool versions:
 * - Java with Maven 3.9.2
 * - Go 1.21
 * - C++20 with CMake 3.24
 * 
 * Usage with Language, Version and BuildSystem enums:
 *   @Library('jenkinsfile-library') _
 *   import org.mbsoft.jenkins.config.LanguageVersions
 *   def pipeline = new org.mbsoft.jenkins.CookbookPipeline(this)
 *   pipeline.language(Language.JAVA)
 *           .languageVersion(LanguageVersions.JAVA_17)
 *           .buildSystem(BuildSystem.MAVEN)
 *           .gitCheckout()
 *           .build()
 *           .test()
 *           .publishOnBranches(BranchPattern.DEVELOP, BranchPattern.MASTER)
 *           .execute()
 * 
 * Or with custom version:
 *   def pipeline = new org.mbsoft.jenkins.CookbookPipeline(this)
 *   pipeline.language(Language.GO)
 *           .languageVersion(new LanguageVersion(Language.GO, '1.20'))
 *           .buildSystem(BuildSystem.GO)
 *           .executeDefault()
 */
class CookbookPipeline implements Serializable {

    def script
    List<Stage> stages = []
    JenkinsHelper jenkinsHelper
    BranchConfig branchConfig
    Language language
    LanguageVersion languageVersion
    BuildSystem buildSystem
    Builder builder

    CookbookPipeline(script) {
        this.script = script
        this.stages = []
        this.jenkinsHelper = new JenkinsHelper(script)
        this.branchConfig = new BranchConfig(script)
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
     * Sets the language version configuration
     * @param version The LanguageVersion configuration
     */
    def languageVersion(LanguageVersion version) {
        if (!language) {
            throw new IllegalStateException('Language must be set before language version. Call .language() first.')
        }
        
        if (version.language != language) {
            throw new IllegalArgumentException(
                "Language version '${version}' (${version.language.id}) does not match selected language '${language.id}'"
            )
        }
        
        this.languageVersion = version
        script.echo "Language version configured: ${version}"
        script.echo "Tool versions: ${version.allToolVersions}"
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
        
        if (languageVersion) {
            builder.setLanguageVersion(languageVersion)
        }
        
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
     * Adds a test stage that only runs on specific branches
     * @param patterns Branch patterns on which to run tests
     */
    def testOnBranches(BranchPattern... patterns) {
        if (!builder) {
            throw new IllegalStateException('Build system must be configured before test stage. Call .buildSystem() first.')
        }
        stages << new ConditionalStage(
            new Tests(script, 'Test', jenkinsHelper, builder),
            { branchConfig.isBranch(it) },
            patterns as List
        )
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
     * Adds a publish/deploy stage that only runs on specific branches
     * Useful for skipping publish on feature branches
     * @param patterns Branch patterns on which to publish
     */
    def publishOnBranches(BranchPattern... patterns) {
        if (!builder) {
            throw new IllegalStateException('Build system must be configured before publish stage. Call .buildSystem() first.')
        }
        stages << new ConditionalStage(
            new Publish(script, jenkinsHelper, builder),
            { branchConfig.isBranch(it) },
            patterns as List
        )
        return this
    }

    /**
     * Adds a custom stage that only runs on specific branches
     * @param stage The stage to add
     * @param patterns Branch patterns on which to run the stage
     */
    def onBranches(Stage stage, BranchPattern... patterns) {
        stages << new ConditionalStage(stage, { branchConfig.isBranch(it) }, patterns as List)
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
     * Default behavior:
     * - Always: gitCheckout, build, test
     * - Only on develop/master/main: publish
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
        publishOnBranches(BranchPattern.DEVELOP, BranchPattern.MASTER, BranchPattern.MAIN)
        execute()
    }
}
