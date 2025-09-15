# Pull Request Danger Checks

Every time a Pull Request is opened, the metadata of the pull request is checked to ensure that it 
meets the minimum standards to be merged.

[danger-kotlin](https://github.com/danger/kotlin) runs on the CI environment every time a PR is pushed

In the workflow file `.github/workflows/shared_test_lint.yml` 
there is a job `danger` with a step named `Danger Checks` that runs danger-kotlin on every Pull Request and prints its output in the PR itself.

## Grant permissions for Danger

Grant `read and write` permissions to the GITHUB_TOKEN for workflows in GitHub:

- Go to `Settings -> Code and automation -> Actions -> General` for your repository
- In the `Workflow permissions` section, select `Read and write permissions` and save.

## Danger Checks

This are the checks the Danger-kotlin runs in every pull request:

### Thank the author of the PR

This line just prints a thank you message with the name of the Pull Request author:

```kotlin
message("Thanks: @${pullRequest.user.login}!")
```

Check if the body content of the Pull Request is empty:

```kotlin
if (pullRequest.body.isNullOrBlank()) {
    fail("📝 Please provide a summary in the Pull Request description.")
}
```

Warm the user if the modified lines is greater than 700

```kotlin
if ((pullRequest.additions ?: 0) - (pullRequest.deletions ?: 0) > 700) {
    warn("✂️ Please consider breaking up this pull request.")
}
```

Warn message if Pull Request has no labels

```kotlin
if (issue.labels.isEmpty()) {
    warn("🏷️ Please add labels to this pull request.")
}
```

Add an Android label when Android files are modified

```kotlin
if (androidModified && !hasAndroidLabel) {
    warn("🏷️ Please add an android label to this pull request.")
}
```

Add an iOS label when iOS files are modified

```kotlin
if (iosModified && !hasIosLabel) {
    warn("🏷️ Please add an iOS label to this pull request.")
}
```

Detect if pull request consist of a code clean up

```kotlin
if ((pullRequest.deletions ?: 0) > (pullRequest.additions ?: 0)) {
    message("🎉 Code Cleanup!")    
}
```

Work in progress check

```kotlin
if (pullRequest.title.contains("WIP", false) || hasWipLabel) {
    warn("⌛️ PR is classed as Work in Progress")
}
```

Check if What's New file for release to Android Play Store has been modified. This file should be located in 
`composeApp/release/whatsNew/` directory for different language versions.

```kotlin
if ((sharedModified || androidModified) && !whatsNewEnglish) {
    message("🚀 Please add whats new information for release.\nYou can find it at composeApp/release/whatsNew/ for your language version")
}
```