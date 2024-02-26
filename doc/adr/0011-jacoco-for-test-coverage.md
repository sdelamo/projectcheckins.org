# 11. Jacoco for Test Coverage

Date: 2024-02-26
Creator: Sergio del Amo
Reviewer: Guillermo Calvo

## Status

Accepted

## Context

We want a way to measure code coverage not coupled to the IDE so that every developer can get the code coverage.

## Decision

We use Jacoco via the [Jacoco Gradle Plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html). The team has experience using Jacoco, and Jacoco seems stable.

## Consequences

We will aim to maintain as close as possible to 100%.
