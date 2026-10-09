package org.mbsoft.jenkins.config

import org.mbsoft.jenkins.builders.Builder
import org.mbsoft.jenkins.builders.MavenBuilder
import org.mbsoft.jenkins.builders.GradleBuilder
import org.mbsoft.jenkins.builders.GoBuilder
import org.mbsoft.jenkins.builders.MakeBuilder
import org.mbsoft.jenkins.builders.CMakeBuilder

/**
 * Factory for creating language-specific builders
 * Ensures type safety: only builders compatible with the selected language can be created
 */
class BuilderFactory {

    /**
     * Create a builder for the given language
     * @param language The programming language
     * @param builderName The builder to use
     * @param script Jenkins script context
     * @return A Builder instance compatible with the language
     * @throws IllegalArgumentException if builder is not compatible with language
     */
    static Builder createBuilder(Language language, String builderName, def script) {
        String normalizedName = builderName.toLowerCase()
        
        if (!language.supportsBuilder(normalizedName)) {
            throw new IllegalArgumentException(
                "Builder '${builderName}' is not compatible with language '${language.id}'. " +
                "Compatible builders: ${language.getCompatibleBuilders().join(', ')}"
            )
        }

        switch (normalizedName) {
            case 'maven':
                return new MavenBuilder(script)
            case 'gradle':
                return new GradleBuilder(script)
            case 'go':
                return new GoBuilder(script)
            case 'make':
                return new MakeBuilder(script)
            case 'cmake':
                return new CMakeBuilder(script)
            default:
                throw new IllegalArgumentException("Unknown builder: ${builderName}")
        }
    }
}
