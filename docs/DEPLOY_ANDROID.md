# Deploy Android Version

Table of Contents
-----------------

- [Generate a signed Bundle](#generate-a-signed-bundle)
- [Authenticating with Google Cloud](#authenticating-with-google-cloud)
- [Running Tests on Firebase](#running-tests-on-firebase)
- [Manual Initial Release](#manual-initial-release)
- [Complete the Google Play Store presence](#complete-the-google-play-store-presence)

## Generate a signed Bundle

- Create a new KeyStore with an Alias to sign the App, store the keystore file, keystore password, 
the alias name and the alias password securely.
- In Android Studio create a `release` variant signed bundle: `Build > Generate Signed App Bundle`
- Or use Gradle: `./gradlew :androidApp:bundleRelease`
- Create a txt file with base64 encoded signing key used to sign your app, running this command in the folder where you have your keystore file:
```shell
base64 -i adrian-keystore -o keystore_base64.txt
```
- Add these as `secrets` in GitHub
  - `ANDROID_KEYSTORE`: Put here the content of `keystore_base64.txt` file 
  - `KEYSTORE_PASSWORD`: Put here the password for your keystore
  - `ANDROID_KEY_ALIAS`: The alias name of your app in the keystore
  - `ANDROID_KEY_PASSWORD`: The password for your alias in the keystore

## Authenticating with Google Cloud

- Create a new project in Google Cloud console
- Inside the project created, search `Google Play Android Developer API` and enable it
- Also enable the `Cloud Tool Results API` and `Firebase Management API` inside the project
- In Google Cloud console go to `Service Accounts` and there should be one created with the name that 
starts with `firebase-adminsdk` or create a new service account
- In the tab permissions add the `Editor` permission to that service account
- Inside the service account create, go to `Keys` and create a new JSON Key and download the JSON file
- Add a new secret in the github repository named `GOOGLE_SERVICE_ACCOUNT` and put inside the value 
of the downloaded service account key file.
- Add a new secret in the github repository named `GOOGLE_PROJECT_ID` and put the your Google Cloud project ID.
- Log in into your Google Play Console account. go to `Users and permissions`, invite a new user
with the email of the service account created in Google Cloud, and give it permissions to create releases.

## Running Tests on Firebase

- Log in to Firebase console
- Add Firebase to Google Cloud project already created
- Make sure to add the secret in GitHub `GOOGLE_PROJECT_ID` with the Google Cloud project ID

## Manual Initial Release

- Go to Google Play Console and log in
- Create new app and fill the basic details
- Go to `Test and release - Testing` and in `Internal testing` create a new release
- Upload the bundle file created in [a previous step](#generate-a-signed-bundle), save a publish

## Complete the Google Play Store presence

- Upload assets for Google Play: icon 512x512px, banner and app screenshots
- Create a Privacy Policy for the application a publish it
- Complete all the store presence checks