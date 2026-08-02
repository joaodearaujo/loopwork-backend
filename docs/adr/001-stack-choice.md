# ADR 001: Stack choice

## Status
Accepted

## Context
A technical stack needed to be chosen for Loopwork. There was prior
experience with React + Spring Boot from a previous project (habit-forge).

## Options considered
1. Reuse the habit-forge stack (React, TanStack Query/Router, Zod,
   TypeScript, TailwindCSS + Spring Boot)
2. Swap the backend for a different language/framework (e.g., Node.js,
   Django) to broaden the range of known technologies

## Decision
Reuse the habit-forge stack.

## Consequences
- **Gain:** less time spent on the learning curve of new syntax/frameworks,
  allowing focus to stay on architecture, domain modeling, and concurrency
  decisions — the central goal of this project.
- **Loss:** this project does not expand the breadth of known technologies;
  it prioritizes depth of technical decision-making over stack variety.
