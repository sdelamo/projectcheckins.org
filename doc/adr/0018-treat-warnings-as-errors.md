# 18. Treat Warnings as Errors

Date: 2024-02-28
Creator: Guillermo Calvo
Reviewer: Sergio del Amo

## Status

Accepted


## Context

We want to avoid as many bugs as possible.

Some compilation warnings rarely cause any problems, but some others can lead to program malfunction.

There are codebases in which a regular compilation generates a lot of "known warnings" that have to be ignored.
In those scenarios, new warnings tend to be completely ignored.

We'd like to make sure that new compilation warnings are looked into.

While a sensible correction of the offending code is the most desirable outcome of the research,
it is possible that the specific warning has to be suppressed via Java annotations or a similar mechanism.
In any case, solving or suppressing each warning must be the result of a thoughtful decision.


## Decision

We will treat all compilation warnings as errors.


## Consequences

- The build will fail if changes introduce any compilation warnings.
- Adopting this philosophy early in the project will make it easier to maintain overall quality throughout development.
