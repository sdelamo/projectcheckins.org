# 9. GitHub Actions as CI

Date: 2024-02-26
Creator: Sergio del Amo
Reviewer: Guillermo Calvo

## Status

Accepted

## Context

We want a CI tool to build the project when pushing code to the remote repository.

## Decision

We will use GitHub Actions as our CI tool. We are hosting the code on GitHub, and the team has experience using GitHub Actions.
We will put as little logic as possible into the GitHub Actions workflow files to prevent coupling. For example, we could use AWS Code Build to build and deploy in the future.

## Consequences
Since the project is currently private, it may have some costs. 
