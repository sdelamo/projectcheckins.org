# 15. JUnit as a Testing framework

Date: 2024-02-26
Creator: Sergio del Amo
Reviewer: Guillermo Calvo

## Status

Accepted

## Context

We need to choose a testing framework. Micronaut supports JUnit5, KoTest, and Spock.

## Decision

We chose JUnit5 to avoid introducing another program into the application.
We consider JUnit5 an industry standard, and we are familiar with it.
It also integrates well with Jacoco, which we use for code coverage.
Moreover, the JUnit5 @Suite feature may allow us to prove different deployment scenarios running the same batter of tests.

## Consequences

The tests may be more verbose than with KoTest or Spock.
