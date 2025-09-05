# Pre Commit Hooks

Table of Contents
-----------------

- [Setup Pre-commit](#setup-pre-commit)
- [Hooks List](#hooks-list)
- [Swift Format and Lint](#swift-format-and-lint)
- [Kotlin Format and Lint](#kotlin-format-and-lint)
- [Detect Secrets](#detect-secrets)

## Setup Pre-commit

1. Install [pre-commit CLI](https://pre-commit.com/#install) using homebrew

```shell
brew install pre-commit
```

2. Run pre-commit install to set up the git hook scripts:

```shell
pre-commit install -f
```

The pre-commit hooks are in `.pre-commit-config.yaml` in the root directory.

## Hooks List

This are the hooks that runs in every pre-commit action, in this order:

1. Swift Format
2. Swift Lint
3. Kotlin Format
4. Kotlin Lint
5. Yaml Format
6. Detect Private Keys
7. Detect Secrets

## Swift Format and Lint

Refer to document file [SWIFT_FORMAT_LINT.md](SWIFT_FORMAT_LINT.md) for more details.

## Kotlin Format and Lint

Refer to document file [KOTLIN_FORMAT_LINT.md](KOTLIN_FORMAT_LINT.md) for more details. 

## Detect Secrets

Analyze the source code looking for leaked secrets using [detect-secrets](https://github.com/Yelp/detect-secrets)

Install locally using homebrew:

```bash
brew install detect-secrets
```

Create a baseline of potential secrets currently found in your git repository.
The baseline line is stored in the config folder: `config/.secrets.baseline`

```bash
detect-secrets scan > config/.secrets.baseline
```

To update or add new secrets to baseline run the following command:

```bash
detect-secrets scan --baseline config/.secrets.baseline
```

For more info refer to [detect-secrets documentation](https://github.com/Yelp/detect-secrets)
