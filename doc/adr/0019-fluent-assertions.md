# 19. Fluent Assertions

Date: 2024-02-28
Creator: Guillermo Calvo
Reviewer: Sergio del Amo


## Status

Accepted


## Context

We would like to make the development of tests as easy as possible.

This will allow us to keep a high code coverage percentage to avoid obvious errors.

Fluent assertions provide a lightway DSL that can save us from most boilerplate code.


## Decision

We will use [AssertJ](https://assertj.github.io/doc/) consistently in all parts of the project where it makes sense.


## Consequences

- Code that tests parts of the application will be shorter, more concise, and easier to read.
- We will be able to have more tests to cover more scenarios with less effort.
- Tests that fail will yield a meaningful error message.
