# Jenkinsfile library modernization

This branch updates the historical Jenkins shared library for compatibility with current Jenkins and Java toolchains while preserving the simple, fluent DSL.

## What changed

### Build configuration
- Updated to Java 11 (from Java 1.7)
- Updated Groovy from 1.8.9 to 2.4.21
- Updated Jenkins core to 2.440.3 from 2.2
- Updated all pipeline plugins to current versions
- Modernized Maven plugins (added gmavenplus for Groovy compilation)
- Changed repository URLs from HTTP to HTTPS

### API simplification
- Removed nested Builder pattern — simpler to understand and use
- Pure fluent DSL — directly instantiate `CookbookPipeline(this)` and chain methods
- Language and build-system selection are enforced through enums
- Language-version selection allows explicit toolchain versions per project
- Branch-aware execution allows conditional publish/test behavior

## Usage

### Simple fluent syntax
```groovy
@Library('jenkinsfile-library') _

def pipeline = new org.mbsoft.jenkins.CookbookPipeline(this)
pipeline.gitCheckout()
        .build()
        .publish()
        .execute()
```

### With custom stages
```groovy
@Library('jenkinsfile-library') _

def pipeline = new org.mbsoft.jenkins.CookbookPipeline(this)
pipeline.gitCheckout()
        .build()
        .docker('maven:3.8.1-jdk-11', '-p 8080:8080', 'mvn clean install')
        .publish()
        .execute()
```

### Typed language + builder + version selection
```groovy
@Library('jenkinsfile-library') _

import org.mbsoft.jenkins.config.Language
import org.mbsoft.jenkins.config.BuildSystem
import org.mbsoft.jenkins.config.BranchPattern
import org.mbsoft.jenkins.config.LanguageVersions

new org.mbsoft.jenkins.CookbookPipeline(this)
    .language(Language.JAVA)
    .languageVersion(LanguageVersions.JAVA_17)
    .buildSystem(BuildSystem.MAVEN)
    .gitCheckout()
    .build()
    .test()
    .publishOnBranches(BranchPattern.DEVELOP, BranchPattern.MASTER, BranchPattern.MAIN)
    .execute()
```

### Go example
```groovy
@Library('jenkinsfile-library') _

import org.mbsoft.jenkins.config.Language
import org.mbsoft.jenkins.config.BuildSystem
import org.mbsoft.jenkins.config.LanguageVersions

new org.mbsoft.jenkins.CookbookPipeline(this)
    .language(Language.GO)
    .languageVersion(LanguageVersions.GO_1_21)
    .buildSystem(BuildSystem.GO)
    .gitCheckout()
    .build()
    .test()
    .execute()
```

### C++ example
```groovy
@Library('jenkinsfile-library') _

import org.mbsoft.jenkins.config.Language
import org.mbsoft.jenkins.config.BuildSystem
import org.mbsoft.jenkins.config.LanguageVersions

new org.mbsoft.jenkins.CookbookPipeline(this)
    .language(Language.CPLUS_PLUS)
    .languageVersion(LanguageVersions.CPP_20)
    .buildSystem(BuildSystem.CMAKE)
    .gitCheckout()
    .build()
    .test()
    .execute()
```

### Default pipeline
```groovy
@Library('jenkinsfile-library') _

new org.mbsoft.jenkins.CookbookPipeline(this).executeDefault()
```

## Branch-aware behavior

The library supports conditional execution based on the current branch:

```groovy
@Library('jenkinsfile-library') _

import org.mbsoft.jenkins.config.Language
import org.mbsoft.jenkins.config.BuildSystem
import org.mbsoft.jenkins.config.BranchPattern
import org.mbsoft.jenkins.config.LanguageVersions

new org.mbsoft.jenkins.CookbookPipeline(this)
    .language(Language.JAVA)
    .languageVersion(LanguageVersions.JAVA_17)
    .buildSystem(BuildSystem.MAVEN)
    .gitCheckout()
    .build()
    .testOnBranches(BranchPattern.DEVELOP, BranchPattern.RELEASE)
    .publishOnBranches(BranchPattern.DEVELOP, BranchPattern.MASTER, BranchPattern.MAIN)
    .execute()
```

This lets you:
- skip publication on feature branches
- run heavier integration tests only on develop or release branches
- keep the default pipeline simple while remaining branch-aware

## Testing

To test in a Jenkins instance with this library configured:

1. Create a Jenkinsfile in your project:
   ```groovy
   @Library('jenkinsfile-library') _
   new org.mbsoft.jenkins.CookbookPipeline(this)
       .language(org.mbsoft.jenkins.config.Language.JAVA)
       .languageVersion(org.mbsoft.jenkins.config.LanguageVersions.JAVA_17)
       .buildSystem(org.mbsoft.jenkins.config.BuildSystem.MAVEN)
       .executeDefault()
   ```

2. Ensure Jenkins has:
   - The library configured as a Global Library pointing to this repo
   - Maven and JDK 17 installed
   - Standard pipeline plugins (Pipeline, Git, Credentials, etc.)

3. Run a build from a Git repository — it will checkout, build, and publish selectively based on the branch rules
