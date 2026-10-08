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
- Backward-compatible method names (`gitCheckout()`, `build()`, `publish()`) remain the same
- Added `docker()` method with proper parameter handling

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

### Default pipeline
```groovy
@Library('jenkinsfile-library') _

new org.mbsoft.jenkins.CookbookPipeline(this).executeDefault()
```

## Testing

To test in a Jenkins instance with this library configured:

1. Create a Jenkinsfile in your project:
   ```groovy
   @Library('jenkinsfile-library') _
   new org.mbsoft.jenkins.CookbookPipeline(this).executeDefault()
   ```

2. Ensure Jenkins has:
   - The library configured as a Global Library pointing to this repo
   - Maven and JDK 11+ installed
   - Standard pipeline plugins (Pipeline, Git, Credentials, etc.)

3. Run a build from a Git repository — it will checkout, build, and publish when the branch matches the project rules
