# 16. Thymeleaf as view rendering engine

Date: 2024-02-26
Creator: Sergio del Amo
Reviewer: Guillermo Calvo

## Status
Accepted

## Context
The project is going to render server-side HTML.

## Decision

We will use [Thymeleaf](https://www.thymeleaf.org) because:

- Micronaut Framework supports it
- It has good internationalization support. We want to be able to support multiple languages in the future.
- It integrates Micronaut Views Form generation capabilities with templates that are already available.
- It has good fragment support, which allows it to integrate with something such as Turbo or HTMX.

## Consequences

Thymeleaf is not the highest-performance server-side rendering engine. Moreover, it uses reflection to access the view models. Thus, if we decide to use GraalVM Native Image generation in the future, we will need to allow the refection access.  
