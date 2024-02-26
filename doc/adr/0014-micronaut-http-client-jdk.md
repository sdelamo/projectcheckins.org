# 14. Micronaut HTTP Client JDK

Date: 2024-02-26
Creator: Sergio del Amo
Reviewer: Guillermo Calvo

## Status

Accepted

## Context

Micronaut supports two HTTP clients: Micronaut HTTP Client based on Netty and Micronaut HTTP Client JDK.

## Decision

Micronaut HTTP Client JDK is based on the built-in Java HTTP client introduced in Java 11. It is more lightweight than the Netty one, and currently, we don't need the advanced features that the Netty-based client offers.
Initially, we will use the HTTP Client only for testing purposes.

## Consequences
