# Jenkinsfile library modernization

This branch updates the historical Jenkins shared library for compatibility with current Jenkins and Java toolchains.

## Highlights

- bumped Maven compiler target to Java 11
- updated Groovy and Jenkins core dependencies
- replaced the legacy Jenkins CI repository URL with the current HTTPS endpoint
- modernized the Maven plugin stack

## Notes

The original project was written for Jenkins around the 2016/2017 era and relied on very old plugin versions. This branch keeps the same library structure, but updates the build configuration to better match current Jenkins environments.

## Usage

The library still exposes the same `CookbookPipeline` entry point:

```groovy
@Library('jenkinsfile-library') _
org.mbsoft.jenkins.CookbookPipeline.builder(this, steps)
    .buildDefaultPipeline()
    .execute()
```
