# 13. Micronaut Serialization

Date: 2024-02-26

## Status

Accepted

## Context

Micronaut Framework supports two serialization engines: Micronaut Jackson Databind and Micronaut Serialization.

## Decision

We will use [Micronaut Serialization](https://micronaut-projects.github.io/micronaut-serialization/latest/guide). The framework defaults to Micronaut Serialization. Thus, we will use Micronaut Serialization to avoid migration work if Micronaut Jackson Databind is potentially deprecated.

## Consequences

We will need to annotate explicitly the classes we want to serialize and deserialize.
```
