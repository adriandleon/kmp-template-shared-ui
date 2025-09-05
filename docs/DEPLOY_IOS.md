# Deploy iOS Version

# iOS TestFlight Release Process

This document outlines the process for releasing the MisionVida app to TestFlight.

Table of Contents
-----------------

- [Prerequisites](#prerequisites)
- [Environment Setup](#environment-setup)
- [App Store Connect API Key Authentication](#app-store-connect-api-key-authentication)
  - [Creating an App Store Connect API Key](#creating-an-app-store-connect-api-key)
  - [Setting Up the API Key in the Project](#setting-up-the-api-key-in-the-project)
  - [Verifying the API Key Setup](#verifying-the-api-key-setup)
- [Managing Certificates and Provisioning Profiles](#managing-certificates-and-provisioning-profiles)
   - [Initial Setup](#initial-setup)
   - [Adding New Devices](#adding-new-devices)
   - [Common Match Commands](#common-match-commands)
   - [Troubleshooting Match Issues](#troubleshooting-match-issues)
- [Release Process](#release-process)
  - [1. Update Version and Build Number](#1-update-version-and-build-number)
  - [2. Update Changelog](#2-update-changelog)
  - [3. Run Tests and Linting](#3-run-tests-and-linting)
  - [4. Release to TestFlight](#4-release-to-testflight)
  - [5. Monitor TestFlight Build](#5-monitor-testflight-build)
- [Troubleshooting](#troubleshooting)
  - [Code Signing Issues](#code-signing-issues)
  - [Build Issues](#build-issues)
- [Additional Commands](#additional-commands)
- [Notes](#notes)

## Prerequisites

Before starting the release process, ensure you have:

1. Xcode installed on your Mac
2. Fastlane installed (`gem install fastlane`) or (`brew install fastlane`)
3. Access to the Apple Developer account
4. Access to the certificates repository
5. The latest version of the code checked out

## Environment Setup

1. Navigate to the iOS app directory:
   ```bash
   cd iosApp
   ```

2. Install dependencies:
   ```bash
   bundle install
   ```

## App Store Connect API Key Authentication

To authenticate with App Store Connect, we use an API Key instead of username/password authentication. This is more secure and works better with CI/CD systems.

### Creating an App Store Connect API Key

1. Log in to [App Store Connect](https://appstoreconnect.apple.com)
2. Navigate to Users and Access > Keys
3. Click the "+" button to create a new key
4. Give the key a name (e.g., "Fastlane CI")
5. Select the following access levels:
   - App Manager
   - Developer
6. Click "Generate"
7. Download the API Key file (it will be named `AuthKey_XXXXXXXXXX.p8`)
8. Save the Key ID and Issuer ID shown on the screen

### Setting Up the API Key in the Project

You can set up the API Key in one of two ways:

1. Convert the .p8 file content to base64:
   ```bash
   # On macOS/Linux
   base64 -i AuthKey_XXXXXXXXXX.p8 | pbcopy
   ```

2. Create a `.env` file in the `iosApp/fastlane` directory if it doesn't exist

3. Add the following variables to the `.env` file:
   ```
   APP_STORE_CONNECT_API_KEY_ID=XXXXXXXXXX
   APP_STORE_CONNECT_ISSUER_ID=XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX
   APP_STORE_CONNECT_API_KEY_CONTENT=<base64-encoded-content>
   APP_STORE_CONNECT_API_KEY_IS_KEY_CONTENT_BASE64=true
   ```
   Replace the values with your actual Key ID, Issuer ID, and the base64-encoded content of your API Key file.

### Verifying the API Key Setup

To verify the API Key is working correctly:

```bash
fastlane ios release
```

If the authentication is successful, the process will continue. If there are issues, check:
1. The API Key file permissions
2. The base64 encoding
3. The values in the `.env` file
4. The API Key's access permissions in App Store Connect

## Managing Certificates and Provisioning Profiles

We use fastlane match to manage certificates and provisioning profiles. This ensures consistent code signing across the team and CI/CD systems.

### Initial Setup

1. Ensure you have access to the certificates repository:
   ```
   https://github.com/japsystem/apple-certificates.git
   ```

2. If you're setting up a new machine or need to reset certificates:
   ```bash
   fastlane ios sync_all_development
   ```
   This will:
   - Register all devices from the Devicefile
   - Download and install all necessary certificates and provisioning profiles
   - Set up your local environment for development

### Adding New Devices

1. Add the new device information to `iosApp/fastlane/Devicefile`
2. Run the sync command:
   ```bash
   fastlane ios sync_all_development
   ```

### Common Match Commands

- **Sync certificates and profiles**:
  ```bash
  fastlane match development
  fastlane match appstore
  fastlane match adhoc
  ```

- **Force reset certificates** (use with caution):
  ```bash
  fastlane match development --force
  fastlane match appstore --force
  fastlane match adhoc --force
  ```

- **Revoke all certificates and profiles for a specific environment** (use with caution):
  ```bash
  fastlane match nuke development
  fastlane match nuke appstore
  fastlane match nuke addhoc
  ```

### Troubleshooting Match Issues

If you encounter issues with certificates:

1. Check your access to the certificates repository
2. Verify your Apple Developer account permissions
3. Try cleaning the match repository:
   ```bash
   fastlane match nuke development
   fastlane match nuke appstore
   ```
4. Re-run the sync command:
   ```bash
   fastlane ios sync_all_development
   ```

## Release Process

### 1. Update Version and Build Number

The build number is automatically incremented during the release process. The version number should be updated in the Xcode project settings if needed.

### 2. Update Changelog

Before releasing, ensure the changelog is up to date. The changelog is read from:
```
composeApp/release/whatsNew/whatsnew-es-419
```

### 3. Run Tests and Linting

It's recommended to run the following checks before releasing:

```bash
fastlane ios tests
fastlane ios lint
```

### 4. Release to TestFlight

To release to TestFlight, run:

```bash
fastlane ios release
```

This command will:
1. Increment the build number
2. Sync code signing certificates
3. Build the iOS app
4. Upload to TestFlight with the changelog

### 5. Monitor TestFlight Build

After uploading:
1. Log in to [App Store Connect](https://appstoreconnect.apple.com)
2. Navigate to the MisionVida app
3. Go to TestFlight tab
4. Wait for the build to process (usually takes 15-30 minutes)
5. Once processed, you can distribute the build to testers

## Troubleshooting

### Code Signing Issues

If you encounter code signing issues:
1. Ensure you have access to the certificates repository
2. Run `fastlane ios sync_all_development` to sync certificates and devices
3. Clean the build folder and derived data:
   ```bash
   fastlane ios clean
   ```

### Build Issues

If the build fails:
1. Check Xcode logs for specific errors
2. Ensure all dependencies are properly installed
3. Try cleaning the project and rebuilding:
   ```bash
   fastlane ios clean
   fastlane ios release
   ```

## Additional Commands

- **Sync Devices**: `fastlane ios sync_device_info`
- **Run Linting**: `fastlane ios lint`
- **Auto-correct Linting**: `fastlane ios lint_autocorrect`
- **Clean Build**: `fastlane ios clean`

## Notes

- The release process uses fastlane match for code signing
- Certificates are stored in a private repository
- The app identifier is `uy.jorgemarquez.MisionVida`
- The Apple Developer account email is `invjapsystem@gmail.com`