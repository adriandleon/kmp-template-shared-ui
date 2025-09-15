# Swift Lint & Format

Table of Contents
-----------------

- [Pre-commit Hooks](#pre-commit-hooks)
- [Formatting](#formatting)
    - [SwiftFormat Installation](#swiftformat-installation)
    - [Configuration options and rules](#configuration-options-and-rules)
    - [Running SwiftFormat](#running-swiftformat)
- [Linting](#linting)
    - [SwiftLint Installation](#swiftlint-installation)
    - [Running SwiftLint](#running-swiftlint)

## Pre-commit Hooks

1. Install [pre-commit CLI](https://pre-commit.com/#install) using homebrew

```shell
brew install pre-commit
```

2. Run pre-commit install to set up the git hook scripts:

```shell
pre-commit install -f
```

The pre-commit hooks are in `.pre-commit-config.yaml` in the root directory.

## Formatting

We use [SwiftFormat](https://github.com/nicklockwood/SwiftFormat) to format code locally before 
every commit, and then we use [SwiftFormat Lint](https://github.com/nicklockwood/SwiftFormat?tab=readme-ov-file#linting) 
on CI to ensure that the code is formatted correctly. 

SwiftFormat is the most popular formatter tool for Swift code.

### SwiftFormat Installation

Install [SwiftFormat](https://github.com/nicklockwood/SwiftFormat#how-do-i-install-it) in your local machine using homebrew

```shell
brew install swiftformat
```

### Configuration options and rules

A file `.swiftformat` located in config folder contains the configuration options and rules for SwiftFormat.

### Running SwiftFormat

- Runs locally manually when you want to format the code

```shell
swiftformat iosApp/ --config config/.swiftformat
```

- Runs locally automatically on every commit

Make sure to install the [pre-commit hooks](#pre-commit-hooks). This will install the pre-commit hooks in your .git/hooks folder. The pre-commit hooks are in `.pre-commit-config.yaml` and contains the following entry for SwiftFormat:

```yaml
- repo: https://github.com/nicklockwood/SwiftFormat
  rev: 0.55.4
  hooks:
  - id: swiftformat
    entry: swiftformat iosApp/ --config config/.swiftformat
```

- Runs on CI on every Pull Request

In the workflow file `.github/workflows/shared_test_lint.yml` there is a job `swift-check` with a step named `Swift Format Check` that runs SwiftFormat Lint on every Pull Request.

```yaml
- name: Swift format check
  run: swiftformat --lint iosApp/ --config config/.swiftformat --reporter github-actions-log
```

## Linting

We use [SwiftLint](https://github.com/realm/SwiftLint) to lint code locally before every commit, and then we use SwiftLint on CI to ensure that the code is linted correctly.

SwiftLint is a tool to enforce Swift style and conventions. It's the most popular linter tool for Swift code in the community.

### SwiftLint Installation

Install [SwiftLint](https://github.com/realm/SwiftLint?tab=readme-ov-file#homebrew) in your local machine using homebrew

```shell
brew install swiftlint
```

SwiftLint configuration file is located in the conf folder as `.swiftlint.yml`.

> **Note:** Modify any existing rules based on your app and test schemas.

### Running SwiftLint

- Runs locally manually when you want to lint the code

```shell
swiftlint iosApp/ --strict --config config/.swiftlint.yml
```

- Runs locally automatically on every commit

Make sure to install the [pre-commit hooks](#pre-commit-hooks). This will install the pre-commit hooks in your .git/hooks folder. The pre-commit hooks are in `.pre-commit-config.yaml` and contains the following entry for SwiftLint:

```yaml
- repo: https://github.com/realm/SwiftLint
  rev: 0.58.1
  hooks:
    - id: swiftlint
      entry: swiftlint --fix --strict --config config/.swiftlint.yml
```

- Runs on CI on every Pull Request

In the workflow file `.github/workflows/shared_test_lint.yml` there is a job `swift-check` with a step named `Swift Lint` that runs SwiftLint on every Pull Request.

```yaml
  run: brew install swiftlint
- name: Install SwiftLint

- name: Swift Lint
  run: swiftlint iosApp/ --strict --config config/.swiftlint.yml
```