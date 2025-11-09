# Supabase Integration

Table of Contents
-----------------

- [Installing Supabase](#installing-supabase)
  - [Adding Ktor Client Engine](#adding-ktor-client-engine)
- [Setting up the Credentials](#setting-up-the-credentials)
  - [Credentials for Development environment](#credentials-for-development-environment)
  - [Credentials for Production environment](#credentials-for-production-environment)
- [Setting up the Supabase Client](#setting-up-the-supabase-client)
- [Local Development with Supabase](#local-development-with-supabase)
  - [Initialize a local project](#initialize-a-local-project)
  - [Start the local stack](#start-the-local-stack)
  - [Stop the local stack](#stop-the-local-stack)
  - [Setup the Android Emulator and iOS Simulator](#setup-the-android-emulator-and-ios-simulator)
- [Working with Database migrations](#working-with-database-migrations)
- [Sync local changes to remote instance](#sync-local-changes-to-remote-instance)
  - [Link your local project to remote Supabase](#link-your-local-project-to-remote-supabase)
  - [Deploy database changes](#deploy-database-changes)
  - [Updating local database from remote](#updating-local-database-from-remote)
  - [Local storage bucket](#local-storage-bucket)
  - [Other sync options](#other-sync-options)
- [Analytics Integration](#analytics-integration)

## Installing Supabase

[Supabase](https://github.com/supabase/supabase) is an open source backend-as-a-service with modules
for postgres database, storage, authentication, realtime database and edge functions.

[supabase-kt](https://github.com/supabase-community/supabase-kt) is a client for Kotlin Multiplatform

Supabase dependencies are already configured in the project. The current versions are defined in `gradle/libs.versions.toml`:

```toml
[versions]
supabase = "3.2.6"

[libraries]
supabase-auth = { group = "io.github.jan-tennert.supabase", name = "auth-kt", version.ref = "supabase" }
supabase-postgrest = { group = "io.github.jan-tennert.supabase", name = "postgrest-kt", version.ref = "supabase" }
```

The dependencies are already included in the `commonMain` source set in the `composeApp` module's `build.gradle.kts` file:

```kotlin
commonMain.dependencies {
    implementation(libs.supabase.auth)
    implementation(libs.supabase.postgrest)
}
```

### Adding Ktor Client Engine

Ktor Client engines are already configured for Supabase. The current Ktor version is defined in `gradle/libs.versions.toml`:

```toml
[versions]
ktor = "3.3.2"

[libraries]
ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
ktor-client-darwin = { module = "io.ktor:ktor-client-darwin", version.ref = "ktor" }
ktor-client-mock = { module = "io.ktor:ktor-client-mock", version.ref = "ktor" }
ktor-client-okhttp = { module = "io.ktor:ktor-client-okhttp", version.ref = "ktor" }
ktor-serialization-kotlinx-json = { module = "io.ktor:ktor-serialization-kotlinx-json", version.ref = "ktor" }
```

The Ktor clients are already configured in the `composeApp` module's `build.gradle.kts`:

```kotlin
sourceSets {
    androidMain.dependencies {
        implementation(libs.ktor.client.okhttp)
    }

    iosMain.dependencies {
        implementation(libs.ktor.client.darwin)
    }

    commonTest.dependencies {
        implementation(libs.ktor.client.mock)
    }
}
```

## Setting up the Credentials

To avoid exposing the credentials in the code, the gradle plugin [BuildKonfig](https://github.com/yshrsmz/BuildKonfig)
is used to load the credentials in compile time and autogenerate a `BuildKonfig.kt` object.

Add the latest version of BuildKonfig plugin in the version catalog file:

```toml
[versions]
buildkonfig = "0.17.1"
[plugins]
buildkonfig = { id = "com.codingfeline.buildkonfig", version.ref = "buildkonfig" }
```

Add the plugin to root `build.gradle.kts` file.

```kotlin
plugins {
    alias(libs.plugins.buildkonfig) apply false
}
```

Include the plugin in the composeApp module `build.gradle.kts` file:

```kotlin
plugins {
    alias(libs.plugins.buildkonfig)
}
```

To get the secret keys, first we look in `local.properties` variables, if the variable if not present 
then we look in the system environment variables.

Add this function at the end of the composeApp module `build.gradle.kts` file:

```kotlin
fun getSecret(key: String): String {
  return gradleLocalProperties(rootDir, providers).getProperty(key) ?: System.getenv(key)
  ?: throw Exception("Missing secret $key in local.properties or environment variables")
}
```

The logic behind this function, is to load the variables from local properties, these properties can be stored locally in the file `local.properties`
in the root folder of the project ([see section for development credentials](#credentials-for-development-environment)).
Otherwise, try to load the variables from the system environment variables if we are running in CI 
environment and use the secrets from Github Actions for example ([see section for production credentials](#credentials-for-production-environment)). 
If the variable is not found in any of these places then the build will fail with an Exception.

### Credentials for Development environment

See the section below [Local Development with Supabase](#local-development-with-supabase) to obtain
your credentials for local development.

And your local credentials to the `local.properties` file:

```properties
# Supabase Development Credentials
SUPABASE_URL_DEV_AND=YOUR_LOCAL_SUPABASE_URL_FOR_ANDROID
SUPABASE_URL_DEV_IOS=YOUR_LOCAL_SUPABASE_URL_FOR_IOS
SUPABASE_KEY_DEV=YOUR_LOCAL_SUPABASE_KEY
```

> Do NOT commit this file into git. Be sure to configure your .gitignore to exclude this file.

> To access localhost from Android Emulator change host IP `127.0.0.1` by `10.0.2.2`

> To access localhost from iOS Simulator just use localhost IP `127.0.0.1`

You can also test your debug application with your `Production` Supabase environment.

Add your productions credentials in the `local.properties` file:

```properties
# Supabase Production Credentials
SUPABASE_URL_PROD=YOUR_REMOTE_SUPABASE_URL
SUPABASE_KEY_PROD=YOUR_REMOTE_SUPABASE_KEY
```

To build the app with Supabase in `Development` environment, set the property in 
`gradle.properties` file to `debug`:

```properties
# BuildKonfig
buildkonfig.flavor=debug
```

To build the app with Supabase in `Production` environment, set the property in
`gradle.properties` file to `release`:

```properties
# BuildKonfig
buildkonfig.flavor=release
```

### Credentials for Production environment

- Log in to your [Supabase Dashboard](https://supabase.com/dashboard/projects) and obtain the credentials for your project.
- Log in to the GitHub repository of the project and [create secrets](https://docs.github.com/en/actions/security-for-github-actions/security-guides/using-secrets-in-github-actions#creating-secrets-for-a-repository) for Github Actions.
- Because we only need to build against remote Supabase from CI we copy the same value for both environments:
  - Add secrets `SUPABASE_URL_PROD` and `SUPABASE_KEY_PROD` with the Supabase project remote URL and project anon key
- In each workflow that compiles the project, add this env variables and the beginning of the workflow:

```yaml
on: pull_request

env:
  SUPABASE_URL_DEV_AND: ${{ secrets.SUPABASE_URL_PROD }}
  SUPABASE_URL_DEV_IOS: ${{ secrets.SUPABASE_URL_PROD }}
  SUPABASE_KEY_DEV: ${{ secrets.SUPABASE_KEY_PROD }}
  SUPABASE_URL_PROD: ${{ secrets.SUPABASE_URL_PROD }}
  SUPABASE_KEY_PROD: ${{ secrets.SUPABASE_KEY_PROD }}

jobs:
```

## Setting up the Supabase client

To create an instance of the Supabase Client, we use the `createSupabaseClient` helper function and 
pass the `supabaseUrl` and `supabaseKey` params reading their values from the object `BuildKonfig` like this:

```kotlin
fun supabaseClient(): SupabaseClient = createSupabaseClient(
    supabaseUrl = BuildKonfig.SUPABASE_URL,
    supabaseKey = BuildKonfig.SUPABASE_KEY
) {     
    // Install your modules and settings
    install(Postgrest)
    install(Auth)
    defaultSerializer = KotlinXSerializer()
}
```

Then we can use something like Koin DI to create just one instance of the client and inject it where needed:

```kotlin
internal val myKoinModule = module {
    singleOf(::supabaseClient)
}
```

## Local Development with Supabase

- Install the Supabase CLI locally following [this guide](https://supabase.com/docs/guides/local-development/cli/getting-started)
- The Supabase CLI uses Docker containers to manage the local development stack. So you also need to 
install [Docker](https://docs.docker.com/desktop/) in your local machine.

### Initialize a local project

- In the root folder of the project, run this command in the terminal:

```shell
supabase init
```
This will create a new supabase folder. It's safe to commit this folder to your version control system.

**Note:** If the file `config.toml` triggers Detect Secrets pre-commit hook errors, first make sure 
that all error are false positives, the add a comment next to line with the error:

```toml
openai_api_key = "env(OPENAI_API_KEY)" # pragma: allowlist-secret the secret is stored in .env
```

To store the actual secrets, follow the guide to [Managing config and secrets](https://supabase.com/docs/guides/local-development/managing-config)

> You can reference environment variables within the config.toml file using the env() function. This will detect any values stored in an .env file at the root of your project directory.

### Start the local stack

- Now, to start the Supabase stack, run:

```shell
supabase start
```

Once all of the Supabase services are running, you'll see output containing your local Supabase credentials.

- Use `API URL` and `anon key` to [setup development credentials](#credentials-for-development-environment) in you `local.properties` file.
- To access your local Supabase Studio go to [http://localhost:54323](http://localhost:54323)

### Stop the local stack

- When you are finished working on your Supabase project, you can stop the stack (without resetting your local database):

```shell
supabase stop
```

### Setup the Android Emulator and iOS Simulator

According to the [Android Emulator documentation](https://developer.android.com/studio/run/emulator-networking.html#networkaddresses) 
to access your computer's localhost from an Android emulator using "localhost," you can use the 
special IP address 10.0.2.2, which is an alias to your host machine's loopback interface (localhost).

- To run the iOS Simulator against the local Supabase instance, we have to use the localhost IP `127.0.0.1`
- In order to be able to test our application in the Android Emulator connected to the local instance of Supabase,
instead of using localhost IP `127.0.0.1` we have to change it for `10.0.2.2`.
- So, we have to add two different properties to define the supabase host url in the `local.properties` file. 

Example:

```properties
# Supabase Development Credentials
SUPABASE_URL_DEV_IOS=http://127.0.0.1:54321
SUPABASE_URL_DEV_AND=http://10.0.2.2:54321
SUPABASE_KEY_DEV=YOUR_PROJECT_SUPABASE_KEY
```

- Create an xml file `composeApp/src/androidMain/res/xml/network_security_config.xml` with the content:

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">10.0.2.2</domain>
    </domain-config>
</network-security-config>
```

- In the `AndroidManifest.xml` file and under the `<application>` tag, add the property 
`networkSecurityConfig` to specify the network configuration file which enables HTTP connection 
in the 10.0.2.2 domain:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
  
  <application android:networkSecurityConfig="@xml/network_security_config">
  </application>

</manifest>
```

You can refer to [Network Security Configuration](https://developer.android.com/privacy-and-security/security-config) to learn more.

## Working with Database migrations

More info in the Supabase [documentation](https://supabase.com/docs/guides/local-development/overview#database-migrations)

- With the local Supabase instance running, create a new database table with the Table editor or the 
SQL editor in the local Supabase dashboard.
- Once we are happy with our database migration schema we can create the migration file with the 
supabase `db diff` command:

```shell
supabase db diff --use-migra initial_schema -f initial_schema
```

- Now that you have a migration file inside the `supabase/migrations` folder, you can added to git 
control version
- Run `supabase db reset` to verify that the new migration does not generate errors
- We can also add seed data in the table creating a file `supabase/seed.sql` and all the SQL we add 
in this file will run after we execute `supabase start` command.
- Run `supabase db reset` again and you will see your table populated with data.
- Later if we add a column to our table or create a new table. Run the diff command again to generate
another migration file

```shell
supabase db diff --use-migra new_table -f new_table
```

## Sync local changes to remote instance

### Link your local project to remote Supabase

Associate your local project with your remote project.

- First login the Supabase CLI to you Supabase 
remote account:

```shell
supabase login
```

- Then, get the `<project-id>` from your project's dashboard URL:
[https://supabase.com/dashboard/project/<project-id>](https://supabase.com/dashboard/project/<project-id>) 
and use the supabase link command:

```shell
supabase link --project-ref <project-id>
```

### Deploy database changes

Deploy any local database migrations using `db push`:

```shell
supabase db push
```

### Updating local database from remote

To capture any changes that you have made to your remote database before

- First, get all the changes from your remote database:

```shell
supabase db pull
```

`supabase/migrations` is now populated with a migration in `<timestamp>_remote_schema.sql`. This 
migration captures any changes required for your local database to match the schema of your remote Supabase project.

- Review the generated migration file and once happy, apply the changes to your local instance:

```shell
supabase migration up
```

- Reset your local database completely

```shell
supabase db reset
```

### Local storage bucket

1. First, pull any remote changes in your storage schema:

```shell
supabase db pull --schema storage
```

2. Download files from the remote storage bucket you want to put in your local bucket, and place 
those files in the local directory `/supabase/images`

3. Seed your local bucket from with the files in `/supabase/images` directory:

```shell
supabase seed buckets
```

For more info, consult the [Sync storage buckets](https://supabase.com/docs/guides/local-development/overview#sync-storage-buckets) documentation.

### Other sync options

Refer to official [Supabase documentation](https://supabase.com/docs/guides/local-development/overview) 
to always get the latest information about sync options. deploy Edge Functions, use auth locally, 
sync storage buckets, sync storage buckets, sync any schema with `--schema`

## Analytics Integration

The project uses Firebase Analytics to track user events and interactions. For detailed information about the analytics system, including:

- How to track events
- Available event types
- How to implement custom providers
- Best practices
- Firebase Analytics integration

Please refer to the [Analytics Integration Guide](ANALYTICS_INTEGRATION.md).
