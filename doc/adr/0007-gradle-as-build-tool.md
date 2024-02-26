# 7. Gradle as Build tool

Date: 2024-02-26

## Status

Accepted

## Context

We need to choose a build tool supported by Micronaut Framework.

## Decision

Micronaut supports both Gradle and Maven. For this project, Gradle was chosen because we want to build a modularised build, which we consider Gradle superior for, and also, the team has experience with Gradle as a build tool. 

## Consequences

Gradle tends to change things, and they have a release cadence which we will follow. 
