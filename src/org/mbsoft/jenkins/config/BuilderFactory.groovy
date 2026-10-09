package org.mbsoft.jenkins.config

import org.mbsoft.jenkins.builders.Builder
import org.mbsoft.jenkins.builders.MavenBuilder
import org.mbsoft.jenkins.builders.GradleBuilder
import org.mbsoft.jenkins.builders.GoBuilder
import org.mbsoft.jenkins.builders.MakeBuilder
import org.mbsoft.jenkins.builders.CMakeBuilder

/**
 * Factory for creating language-specific builders from BuildSystem enum
 * Ensures type safety: only builders compatible with the selected language can be created
 */
class BuilderFactory {

    /**
     * Create a builder for the given build system
     * @param buildSystem The BuildSystem enum value
     * @param script Jenkins script context
     * @return A Builder instance compatible with the build system
     */
    static Builder createBuilder(BuildSystem buildSystem, def script) {
        switch (buildSystem) {
            case BuildSystem.MAVEN:
                return new MavenBuilder(script)
            case BuildSystem.GRADLE:
                return new GradleBuilder(script)
            case BuildSystem.GO:
                return new GoBuilder(script)
            case [BuildSystem.GO_MAKE, BuildSystem.CPP_MAKE]:
                return new MakeBuilder(script)
            case BuildSystem.CMAKE:
                return new CMakeBuilder(script)
            default:
                throw new IllegalArgumentException("Unknown build system: ${buildSystem}")
        }
    }

    /**
     * Validate that a build system is compatible with a language
     * @param language The Language enum value
     * @param buildSystem The BuildSystem enum value
     * @throws IllegalArgumentException if not compatible
     */
    static void validateCompatibility(Language language, BuildSystem buildSystem) {
        if (buildSystem.language != language) {
            List<String> available = language.getAvailableBuilders().collect { it.id }
            throw new IllegalArgumentException(
                "BuildSystem '${buildSystem.id}' (${buildSystem.language.id}) is not compatible with language '${language.id}'. " +
                "Available builders: ${available.join(', ')}"
            )
        }
    }
}
