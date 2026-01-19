#!/bin/bash

# =============================================================================
# Compose Multiplatform Template Project Setup Script
# =============================================================================
# This script transforms the Compose Multiplatform template project into a new 
# project with custom names, package identifiers, folder structure, and deeplink schemas.
#
# Usage: ./setup_new_project.sh [--dry-run] [--verbose] [--help]
# =============================================================================

set -euo pipefail  # Exit on error, undefined vars, pipe failures

# Script configuration
readonly SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
readonly SCRIPT_NAME="$(basename "$0")"
readonly LOG_FILE="${SCRIPT_DIR}/setup_script.log"
readonly BACKUP_DIR="${SCRIPT_DIR}/.backup_$(date +%Y%m%d_%H%M%S)"

# Global variables
DRY_RUN=false
VERBOSE=false
OLD_PACKAGE="com.example.project"
OLD_PROJECT_NAME="CMP-Template"
OLD_BUNDLE_ID="com.example.project.CMP-Template"
OLD_DOMAIN="project.example.com"

# Colors for output
readonly RED='\033[0;31m'
readonly GREEN='\033[0;32m'
readonly YELLOW='\033[1;33m'
readonly BLUE='\033[0;34m'
readonly PURPLE='\033[0;35m'
readonly CYAN='\033[0;36m'
readonly NC='\033[0m' # No Color

# Logging functions
log() {
    local level="$1"
    shift
    local message="$*"
    local timestamp=$(date '+%Y-%m-%d %H:%M:%S')
    echo "[$timestamp] [$level] $message" >> "$LOG_FILE"
    
    if [[ "$VERBOSE" == "true" ]]; then
        echo "[$timestamp] [$level] $message" >&2
    fi
}

log_info() { log "INFO" "$@"; }
log_warn() { log "WARN" "$@"; }
log_error() { log "ERROR" "$@"; }

# Enhanced output functions
print_step() {
    echo -e "${BLUE}==>${NC} $1"
    log_info "STEP: $1"
}

print_success() {
    echo -e "${GREEN}✓${NC} $1"
    log_info "SUCCESS: $1"
}

print_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
    log_warn "$1"
}

print_error() {
    echo -e "${RED}✗${NC} $1"
    log_error "$1"
}

print_info() {
    echo -e "${CYAN}ℹ${NC} $1"
    log_info "INFO: $1"
}

# Error handling
handle_error() {
    local line_number="$1"
    local error_code="$2"
    print_error "Error on line $line_number with exit code $error_code"
    log_error "Script failed on line $line_number with exit code $error_code"
    
    if [[ -d "$BACKUP_DIR" ]]; then
        print_info "Backup directory created at: $BACKUP_DIR"
    fi
    
    exit "$error_code"
}

trap 'handle_error ${LINENO} $?' ERR

# Utility functions
validate_input() {
    local input="$1"
    local pattern="$2"
    local error_msg="$3"
    
    if [[ ! $input =~ $pattern ]]; then
        print_error "$error_msg"
        return 1
    fi
    return 0
}

to_package_name() {
    echo "$1" | tr '[:upper:]' '[:lower:]' | sed 's/[^a-z0-9.]//g' | sed 's/\.\.*/\./g' | sed 's/^\.\|\.$//g'
}

to_project_name() {
    echo "$1" | sed 's/[^a-zA-Z0-9]//g' | sed 's/^[0-9]*//g'
}

to_bundle_id() {
    echo "$1" | tr '[:upper:]' '[:lower:]' | sed 's/[^a-z0-9.]//g' | sed 's/\.\.*/\./g' | sed 's/^\.\|\.$//g'
}

get_domain() {
    echo "$1" | tr '.' '\n' | tail -r | tr '\n' '.' | sed 's/\.$//'
}

get_app_name() {
    echo "$1" | rev | cut -d'.' -f1 | rev
}

# File operations with better error handling
safe_file_operation() {
    local operation="$1"
    local file="$2"
    shift 2
    local args=("$@")
    
    if [[ ! -f "$file" ]]; then
        print_warning "File not found: $file"
        return 1
    fi
    
    if [[ "$DRY_RUN" == "true" ]]; then
        print_info "DRY RUN: Would $operation $file"
        return 0
    fi
    
    # Create backup
    if [[ "$operation" == "update" ]]; then
        cp "$file" "$file.backup" 2>/dev/null || true
    fi
    
    # Perform operation
    case "$operation" in
        "update")
            sed -i.tmp "${args[@]}" "$file" && rm -f "$file.tmp"
            ;;
        "copy")
            cp "${args[@]}" "$file"
            ;;
        "move")
            mv "${args[@]}" "$file"
            ;;
        *)
            print_error "Unknown operation: $operation"
            return 1
            ;;
    esac
}

# Optimized directory operations
create_directory_structure() {
    local old_package="$1"
    local new_package="$2"
    local old_path="${old_package//.//}"
    local new_path="${new_package//.//}"
    
    print_step "Creating new directory structure..."
    
    # Use arrays for better performance
    # AGP 9.0: androidApp/src/main contains the Android app, composeApp has multiplatform library code
    local -a composeapp_source_sets=("commonMain" "iosMain" "androidMain" "commonTest" "androidInstrumentedTest")
    
    # Handle composeApp source sets
    for source_set in "${composeapp_source_sets[@]}"; do
        local source_dir="composeApp/src/$source_set/kotlin/$old_path"
        local target_dir="composeApp/src/$source_set/kotlin/$new_path"
        
        if [[ -d "$source_dir" ]]; then
            if [[ "$DRY_RUN" == "true" ]]; then
                print_info "DRY RUN: Would create $target_dir"
            else
                mkdir -p "$target_dir"
                cp -r "$source_dir"/* "$target_dir/" 2>/dev/null || true
                print_success "Created composeApp/$source_set directory structure: $new_path"
            fi
        fi
    done
    
    # Handle androidApp/src/main (AGP 9.0 structure)
    local android_source_dir="androidApp/src/main/kotlin/$old_path"
    local android_target_dir="androidApp/src/main/kotlin/$new_path"
    
    if [[ -d "$android_source_dir" ]]; then
        if [[ "$DRY_RUN" == "true" ]]; then
            print_info "DRY RUN: Would create $android_target_dir"
        else
            mkdir -p "$android_target_dir"
            cp -r "$android_source_dir"/* "$android_target_dir/" 2>/dev/null || true
            print_success "Created androidApp/src/main directory structure: $new_path"
        fi
    fi
}

remove_old_directories() {
    local old_package="$1"
    local old_path="${old_package//.//}"
    
    print_step "Removing old directory structure..."

    # AGP 9.0: androidApp/src/main contains the Android app, composeApp has multiplatform library code
    local -a composeapp_source_sets=("commonMain" "iosMain" "androidMain" "commonTest" "androidInstrumentedTest")
    
    for source_set in "${composeapp_source_sets[@]}"; do
        local dir_to_remove="composeApp/src/$source_set/kotlin/$old_path"
        if [[ -d "$dir_to_remove" ]]; then
            if [[ "$DRY_RUN" == "true" ]]; then
                print_info "DRY RUN: Would remove $dir_to_remove"
            else
                rm -rf "$dir_to_remove"
            fi
        fi
    done
    
    # Remove androidApp old directory structure
    local android_dir_to_remove="androidApp/src/main/kotlin/$old_path"
    if [[ -d "$android_dir_to_remove" ]]; then
        if [[ "$DRY_RUN" == "true" ]]; then
            print_info "DRY RUN: Would remove $android_dir_to_remove"
        else
            rm -rf "$android_dir_to_remove"
        fi
    fi
    
    # Clean up empty directories
    cleanup_empty_directories
    print_success "Removed old directory structure: $old_path"
}

cleanup_empty_directories() {
    local base_path="${1:-.}"
    
    if [[ ! -d "$base_path" ]]; then
        return 0
    fi
    
    # More efficient empty directory cleanup
    find "$base_path" -type d -empty -delete 2>/dev/null || true
    
    # Additional cleanup for nested empty directories
    local empty_count
    while true; do
        empty_count=$(find "$base_path" -type d -empty 2>/dev/null | wc -l)
        if [[ "$empty_count" -eq 0 ]]; then
            break
        fi
        find "$base_path" -type d -empty -delete 2>/dev/null || true
    done
}

# Optimized file content updates
update_file_contents() {
    local file="$1"
    shift
    local replacements=("$@")
    
    if [[ ! -f "$file" ]]; then
        print_warning "File not found: $file"
        return 1
    fi
    
    if [[ "$DRY_RUN" == "true" ]]; then
        print_info "DRY RUN: Would update $file"
        return 0
    fi
    
    # Create backup
    cp "$file" "$file.backup"
    
    # Apply all replacements in one sed command for better performance
    local sed_commands=()
    for replacement in "${replacements[@]}"; do
        sed_commands+=("-e" "$replacement")
    done
    
    sed -i.tmp "${sed_commands[@]}" "$file"
    rm -f "$file.tmp"
    
    print_success "Updated: $file"
}

# Batch file operations for better performance
update_files_batch() {
    local pattern="$1"
    local replacements=("${@:2}")
    
    print_step "Updating files matching pattern: $pattern"
    
    local -a files_to_update
    while IFS= read -r -d '' file; do
        files_to_update+=("$file")
    done < <(find . -name "$pattern" -type f -print0)
    
    if [[ ${#files_to_update[@]} -eq 0 ]]; then
        print_info "No files found matching pattern: $pattern"
        return 0
    fi
    
    print_info "Found ${#files_to_update[@]} files to update"
    
    for file in "${files_to_update[@]}"; do
        update_file_contents "$file" "${replacements[@]}"
    done
}

# Optimized deeplink schema updates
update_deeplink_schemas() {
    local old_project_name="$1"
    local new_project_name="$2"
    local old_domain="$3"
    local new_domain="$4"
    local new_bundle_id="$5"
    
    print_step "Updating deeplink schemas..."
    
    # Android deeplink updates (AGP 9.0: AndroidManifest is in androidApp/src/main)
    local android_manifest="androidApp/src/main/AndroidManifest.xml"
    if [[ -f "$android_manifest" ]]; then
        local old_scheme="cmptemplate"
        local new_scheme="$(echo "$new_project_name" | tr '[:upper:]' '[:lower:]')"  # Convert to lowercase

        update_file_contents "$android_manifest" \
            "s|android:host=\"www\.example\.com\"|android:host=\"www.$new_domain\"|g" \
            "s|android:scheme=\"$old_scheme\"|android:scheme=\"$new_scheme\"|g"
    fi
    
    # iOS deeplink updates
    local ios_plist="iosApp/$new_project_name/Info.plist"
    if [[ -f "$ios_plist" ]]; then
        local old_scheme="cmptemplate"
        local new_scheme="$(echo "$new_project_name" | tr '[:upper:]' '[:lower:]')"  # Convert to lowercase

        update_file_contents "$ios_plist" \
            "s|<string>$old_scheme</string>|<string>$new_scheme</string>|g" \
            "s|<string>com\.example\.project</string>|<string>$new_bundle_id</string>|g"
    fi
    
    print_success "Updated deeplink schemas"
}

# Optimized import statement updates
update_import_statements() {
    local old_package="$1"
    local new_package="$2"
    
    print_step "Updating import statements with new package name..."
    
    local old_import_package="cmptemplate"
    local new_import_package="$(echo "$new_package" | tr '[:upper:]' '[:lower:]')"  # Convert to lowercase
    new_import_package="${new_import_package//./}"  # Remove dots
    
    if [[ -z "$new_import_package" ]]; then
        print_error "New import package name is empty. Cannot update import statements."
        return 1
    fi
    
    print_info "Converting package '$new_package' to import package '$new_import_package'"
    
    # Use batch file operations for better performance
    update_files_batch "*.kt" \
        "s|import $old_import_package\.composeapp\.generated\.resources|import $new_import_package\.composeapp\.generated\.resources|g"
    
    print_success "Updated import statements"
}

# Optimized deeplink comment updates
update_deeplink_comments() {
    local old_project_name="$1"
    local new_project_name="$2"
    
    print_step "Updating deeplink route comments in Kotlin files..."

    local old_scheme="cmptemplate"
    local new_scheme="$(echo "$new_project_name" | tr '[:upper:]' '[:lower:]')"  # Convert to lowercase
    
    # Use batch file operations
    update_files_batch "*.kt" \
        "s|Deeplink URL: \"$old_scheme://|Deeplink URL: \"$new_scheme://|g"
    
    print_success "Updated deeplink route comments"
}

# Optimized app name updates
update_app_name_strings() {
    local old_project_name="$1"
    local new_project_name="$2"

    print_step "Updating app name in strings.xml..."

    # AGP 9.0: strings.xml is in androidApp/src/main/res/values
    local strings_file="androidApp/src/main/res/values/strings.xml"
    if [[ -f "$strings_file" ]]; then
        update_file_contents "$strings_file" \
            "s|<string name=\"app_name\">$old_project_name</string>|<string name=\"app_name\">$new_project_name</string>|g"
    fi

    print_success "Updated app name in strings.xml"
}

# Update compose resources package
update_compose_resources_package() {
    local old_package="$1"
    local new_package="$2"

    print_step "Updating compose.resources package configuration..."

    local build_file="composeApp/build.gradle.kts"
    if [[ -f "$build_file" ]]; then
        update_file_contents "$build_file" \
            "s|compose.resources { packageOfResClass = \"$old_package.resources\" }|compose.resources { packageOfResClass = \"$new_package.resources\" }|g"
    fi

    print_success "Updated compose.resources package configuration"
}

# Optimized Xcode project updates
update_xcode_project() {
    local old_project_name="$1"
    local new_project_name="$2"
    local old_bundle_id="$3"
    local new_bundle_id="$4"
    
    print_step "Updating Xcode project..."
    
    # Update project.pbxproj
    local project_file="iosApp/$old_project_name.xcodeproj/project.pbxproj"
    if [[ -f "$project_file" ]]; then
        update_file_contents "$project_file" \
            "s|com.adriandeleon.template|$new_bundle_id|g" \
            "s|$old_project_name|$new_project_name|g" \
            "s|com.adriandeleon.template.Template|$new_bundle_id.$new_project_name|g" \
            "s|adriandeleon|$(echo $new_bundle_id | cut -d'.' -f1)|g"
        
        # Rename Xcode project directory
        if [[ "$DRY_RUN" == "true" ]]; then
            print_info "DRY RUN: Would rename iosApp/$old_project_name.xcodeproj to iosApp/$new_project_name.xcodeproj"
        else
            mv "iosApp/$old_project_name.xcodeproj" "iosApp/$new_project_name.xcodeproj"
            print_success "Renamed Xcode project directory"
        fi
    fi
    
    # Update other iOS files
    local -a ios_files=(
        "iosApp/Configuration/Config.xcconfig"
        "iosApp/$new_project_name/Info.plist"
    )
    
    for file in "${ios_files[@]}"; do
        if [[ -f "$file" ]]; then
            update_file_contents "$file" \
                "s|com.adriandeleon.template|$new_bundle_id|g" \
                "s|$old_project_name|$new_project_name|g" \
                "s|com.adriandeleon.template.Template|$new_bundle_id.$new_project_name|g" \
                "s|adriandeleon|$(echo $new_bundle_id | cut -d'.' -f1)|g"
        fi
    done
    
    # Update Config.xcconfig PRODUCT_BUNDLE_IDENTIFIER specifically
    local config_file="iosApp/Configuration/Config.xcconfig"
    if [[ -f "$config_file" ]]; then
        update_file_contents "$config_file" \
            "s|PRODUCT_BUNDLE_IDENTIFIER=.*|PRODUCT_BUNDLE_IDENTIFIER=$new_bundle_id.$new_project_name\$(TEAM_ID)|g"
    fi
    
    # Rename iOS app folder
    if [[ -d "iosApp/$old_project_name" ]]; then
        if [[ "$DRY_RUN" == "true" ]]; then
            print_info "DRY RUN: Would rename iosApp/$old_project_name to iosApp/$new_project_name"
        else
            mv "iosApp/$old_project_name" "iosApp/$new_project_name"
            print_success "Renamed iOS app folder"
        fi
    fi
    
    # Rename Swift file
    local old_swift_file="iosApp/$new_project_name/$old_project_name.swift"
    local new_swift_file="iosApp/$new_project_name/$new_project_name.swift"
    if [[ -f "$old_swift_file" ]]; then
        if [[ "$DRY_RUN" == "true" ]]; then
            print_info "DRY RUN: Would rename $old_swift_file to $new_swift_file"
        else
            mv "$old_swift_file" "$new_swift_file"
            print_success "Renamed Swift file: $old_project_name.swift -> $new_project_name.swift"
        fi
    fi
}

# Optimized source file updates
update_source_files() {
    local old_package="$1"
    local new_package="$2"
    local old_project_name="$3"
    local new_project_name="$4"
    local old_bundle_id="$5"
    local new_bundle_id="$6"
    local old_domain="$7"
    local new_domain="$8"
    
    print_step "Updating source files..."
    
    # Batch update Kotlin files
    update_files_batch "*.kt" \
        "s|$old_package|$new_package|g" \
        "s|$old_project_name|$new_project_name|g" \
        "s|$old_bundle_id|$new_bundle_id|g" \
        "s|$old_domain|$new_domain|g"
    
    # Batch update Swift files
    update_files_batch "*.swift" \
        "s|$old_package|$new_package|g" \
        "s|$old_project_name|$new_project_name|g" \
        "s|$old_bundle_id|$new_bundle_id|g" \
        "s|$old_domain|$new_domain|g"
    
    print_success "Updated all source files"
}

# Optimized configuration file updates
update_config_files() {
    local old_package="$1"
    local new_package="$2"
    local old_project_name="$3"
    local new_project_name="$4"
    local old_bundle_id="$5"
    local new_bundle_id="$6"
    local old_domain="$7"
    local new_domain="$8"
    
    print_step "Updating configuration files..."
    
    # Batch update Gradle files
    local -a gradle_files=(
        "build.gradle.kts"
        "settings.gradle.kts"
        "composeApp/build.gradle.kts"
        "androidApp/build.gradle.kts"
    )
    
    for file in "${gradle_files[@]}"; do
        if [[ -f "$file" ]]; then
            update_file_contents "$file" \
                "s|$old_package|$new_package|g" \
                "s|$old_project_name|$new_project_name|g" \
                "s|$old_bundle_id|$new_bundle_id|g" \
                "s|$old_domain|$new_domain|g"
        fi
    done
    
    # Update Android manifest (AGP 9.0: AndroidManifest is in androidApp/src/main)
    local android_manifest="androidApp/src/main/AndroidManifest.xml"
    if [[ -f "$android_manifest" ]]; then
        update_file_contents "$android_manifest" \
            "s|$old_package|$new_package|g" \
            "s|$old_project_name|$new_project_name|g" \
            "s|$old_bundle_id|$new_bundle_id|g" \
            "s|$old_domain|$new_domain|g"
    fi
    
    print_success "Updated configuration files"
}

# Optimized documentation updates
update_documentation() {
    local old_package="$1"
    local new_package="$2"
    local old_project_name="$3"
    local new_project_name="$4"
    local old_bundle_id="$5"
    local new_bundle_id="$6"
    local old_domain="$7"
    local new_domain="$8"
    
    print_step "Updating documentation files..."
    
    # Batch update markdown files
    update_files_batch "*.md" \
        "s|$old_package|$new_package|g" \
        "s|$old_project_name|$new_project_name|g" \
        "s|$old_bundle_id|$new_bundle_id|g" \
        "s|$old_domain|$new_domain|g"
    
    print_success "Updated documentation files"
}

# Optimized workflow updates
update_workflows() {
    local old_package="$1"
    local new_package="$2"
    local old_project_name="$3"
    local new_project_name="$4"
    local old_bundle_id="$5"
    local new_bundle_id="$6"
    local old_domain="$7"
    local new_domain="$8"
    
    print_step "Updating CI/CD workflows..."
    
    # Update GitHub Actions workflows
    if [[ -d ".github/workflows" ]]; then
        update_files_batch "*.yml" \
            "s|$old_package|$new_package|g" \
            "s|$old_project_name|$new_project_name|g" \
            "s|$old_bundle_id|$new_bundle_id|g" \
            "s|$old_domain|$new_domain|g"
    fi
    
    # Update Dangerfile
    local dangerfile="config/Dangerfile.df.kts"
    if [[ -f "$dangerfile" ]]; then
        update_file_contents "$dangerfile" \
            "s|$old_package|$new_package|g" \
            "s|$old_project_name|$new_project_name|g" \
            "s|$old_bundle_id|$new_bundle_id|g" \
            "s|$old_domain|$new_domain|g"
    fi
    
    print_success "Updated CI/CD workflows"
}

# Optimized other config updates
update_other_configs() {
    local old_package="$1"
    local new_package="$2"
    local old_project_name="$3"
    local new_project_name="$4"
    local old_bundle_id="$5"
    local new_bundle_id="$6"
    local old_domain="$7"
    local new_domain="$8"
    
    print_step "Updating other configuration files..."
    
    # Batch update configuration files
    local -a config_files=(
        "buildServer.json"
        "gradle.properties"
        "local.properties"
    )
    
    for file in "${config_files[@]}"; do
        if [[ -f "$file" ]]; then
            update_file_contents "$file" \
                "s|$old_package|$new_package|g" \
                "s|$old_project_name|$new_project_name|g" \
                "s|$old_bundle_id|$new_bundle_id|g" \
                "s|$old_domain|$new_domain|g"
        fi
    done
    
    print_success "Updated other configuration files"
}

# Firebase configuration functions (unchanged for compatibility)
create_firebase_configs() {
    local new_package="$1"
    local new_project_name="$2"
    local new_bundle_id="$3"
    
    print_step "Creating Firebase configuration files..."
    
    # Create google-services.json template
    create_google_services_json "$new_package"
    
    # Create GoogleService-Info.plist template
    create_google_service_info_plist "$new_bundle_id" "$new_project_name"
    
    print_success "Created Firebase configuration files"
}

create_google_services_json() {
    local package_name="$1"
    
    local google_services_content='{
  "project_info": {
    "project_number": "123456789012",
    "firebase_url": "https://your-project-id.firebaseio.com",
    "project_id": "your-project-id",
    "storage_bucket": "your-project-id.appspot.com"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "1:123456789012:android:abcdef1234567890",
        "android_client_info": {
          "package_name": "'"$package_name"'"
        }
      },
      "oauth_client": [
        {
          "client_id": "123456789012-yourclientid.apps.googleusercontent.com",
          "client_type": 3
        }
      ],
      "api_key": [
        {
          "current_key": "A-replace-this-string-with-your-api-key"
        }
      ],
      "services": {
        "analytics_service": {
          "status": 2
        },
        "appinvite_service": {
          "status": 2,
          "other_platform_oauth_client": []
        },
        "ads_service": {
          "status": 1
        }
      }
    }
  ],
  "configuration_version": "1"
}'
    
    if [[ "$DRY_RUN" == "true" ]]; then
        print_info "DRY RUN: Would create google-services.json with package name: $package_name"
    else
        # AGP 9.0: google-services.json is in androidApp, not composeApp
        echo "$google_services_content" > "androidApp/google-services.json"
        print_success "Created google-services.json with package name: $package_name (AGP 9.0 structure)"
    fi
}

create_google_service_info_plist() {
    local bundle_id="$1"
    local project_name="$2"
    
    local plist_content='<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
	<key>API_KEY</key>
	<string>A-replace-this-string-with-your-api-key</string>
	<key>GCM_SENDER_ID</key>
	<string>123456789012</string>
	<key>PLIST_VERSION</key>
	<string>1</string>
	<key>BUNDLE_ID</key>
	<string>'"$bundle_id"."$project_name"'</string>
	<key>PROJECT_ID</key>
	<string>your-project-id</string>
	<key>STORAGE_BUCKET</key>
	<string>your-project-id.firebasestorage.app</string>
	<key>IS_ADS_ENABLED</key>
	<false></false>
	<key>IS_ANALYTICS_ENABLED</key>
	<false></false>
	<key>IS_APPINVITE_ENABLED</key>
	<true></true>
	<key>IS_GCM_ENABLED</key>
	<true></true>
	<key>IS_SIGNIN_ENABLED</key>
	<true></true>
	<key>GOOGLE_APP_ID</key>
	<string>1:123456789012:ios:abcdef1234567890</string>
</dict>
</plist>'
    
    if [[ "$DRY_RUN" == "true" ]]; then
        print_info "DRY RUN: Would create GoogleService-Info.plist with bundle ID: $bundle_id.$project_name"
    else
        echo "$plist_content" > "iosApp/$project_name/GoogleService-Info.plist"
        print_success "Created GoogleService-Info.plist with bundle ID: $bundle_id.$project_name"
    fi
}

create_local_properties() {
    print_step "Creating local.properties with API key placeholders..."
    
    local local_props_content='###############################

# Supabase Development Credentials
SUPABASE_URL_DEV_AND=supabase-url-placeholder
SUPABASE_URL_DEV_IOS=supabase-url-placeholder
SUPABASE_KEY_DEV=supabase-key-placeholder

# Supabase Production Credentials
SUPABASE_URL_PROD=supabase-url-placeholder
SUPABASE_KEY_PROD=supabase-key-placeholder

# ConfigCat SDK Keys
CONFIGCAT_LIVE_KEY=configcat-key-placeholder
CONFIGCAT_TEST_KEY=configcat-key-placeholder'
    
    if [[ -f "local.properties" ]]; then
        if ! grep -q "SUPABASE_URL_DEV_AND" "local.properties"; then
            if [[ "$DRY_RUN" == "true" ]]; then
                print_info "DRY RUN: Would append API key placeholders to existing local.properties"
            else
                echo "$local_props_content" >> "local.properties"
                print_success "Added API key placeholders to existing local.properties"
            fi
        else
            print_success "API key placeholders already exist in local.properties"
        fi
    else
        if [[ "$DRY_RUN" == "true" ]]; then
            print_info "DRY RUN: Would create local.properties with API key placeholders"
        else
            echo "$local_props_content" > "local.properties"
            print_success "Created local.properties with API key placeholders"
        fi
    fi
}

# Optimized cleanup
cleanup_backups() {
    print_step "Cleaning up backup files..."
    
    if [[ "$DRY_RUN" == "true" ]]; then
        print_info "DRY RUN: Would clean up backup files"
    else
        find . -name "*.backup" -type f -delete 2>/dev/null || true
        print_success "Cleaned up backup files"
    fi
}

# Enhanced validation
validate_transformation() {
    local new_package="$1"
    local new_project_name="$2"
    local new_bundle_id="$3"
    
    print_step "Validating transformation..."
    
    local new_path="${new_package//.//}"
    # AGP 9.0: androidApp/src/main contains the Android app, composeApp has multiplatform library code
    local -a composeapp_source_sets=("commonMain" "iosMain" "androidMain" "commonTest" "androidInstrumentedTest")
    local all_dirs_exist=true
    
    # Check composeApp module directories
    for source_set in "${composeapp_source_sets[@]}"; do
        local dir="composeApp/src/$source_set/kotlin/$new_path"
        if [[ -d "$dir" ]]; then
            print_success "New composeApp/$source_set directory structure created successfully"
        else
            print_error "New composeApp/$source_set directory structure not found"
            all_dirs_exist=false
        fi
    done
    
    # Check androidApp module directory (AGP 9.0 structure)
    local android_dir="androidApp/src/main/kotlin/$new_path"
    if [[ -d "$android_dir" ]]; then
        print_success "New androidApp/src/main directory structure created successfully"
    else
        print_error "New androidApp/src/main directory structure not found"
        all_dirs_exist=false
    fi
    
    if [[ "$all_dirs_exist" == "false" ]]; then
        return 1
    fi
    
    # Check if Xcode project was renamed
    if [[ -d "iosApp/$new_project_name.xcodeproj" ]]; then
        print_success "Xcode project renamed successfully"
    else
        print_error "Xcode project not renamed"
        return 1
    fi
    
    # Check if iOS app folder was renamed
    if [[ -d "iosApp/$new_project_name" ]]; then
        print_success "iOS app folder renamed successfully"
    else
        print_error "iOS app folder not renamed"
        return 1
    fi
    
    print_success "Transformation validation completed"
}

# Enhanced final instructions
show_final_instructions() {
    local new_package="$1"
    local new_project_name="$2"
    local new_bundle_id="$3"
    
    echo ""
    echo -e "${GREEN}🎉 Project transformation completed successfully!${NC}"
    echo ""
    echo -e "${CYAN}📋 Next Steps:${NC}"
    echo ""
    echo -e "${YELLOW}1. Update your IDE:${NC}"
    echo "   - Close and reopen Android Studio"
    echo "   - Sync Gradle files"
    echo "   - Open the new Xcode project: iosApp/$new_project_name.xcodeproj"
    echo ""
    echo -e "${YELLOW}2. Update configuration files:${NC}"
    echo "   - Replace androidApp/google-services.json with your actual Firebase config (AGP 9.0 structure)"
    echo "   - Replace iosApp/$new_project_name/GoogleService-Info.plist with your actual Firebase config"
    echo "   - Update local.properties with your actual API keys (placeholders were added)"
    echo "   - Note: Template files were created with correct package names and bundle IDs"
    echo "   - Note: Deeplink schemas and route comments were updated with your app name and domain"
    echo "   - Note: Import statements and app name in strings.xml were updated"
    echo ""
    echo -e "${YELLOW}3. Update GitHub repository:${NC}"
    echo "   - Update repository secrets in GitHub Settings"
    echo "   - Update workflow variables if needed"
    echo "   - Update repository name and description"
    echo ""
    echo -e "${YELLOW}4. Test your setup:${NC}"
    echo "   - Run: ./gradlew clean build"
    echo "   - Test Android build: ./gradlew :androidApp:assembleDebug (AGP 9.0 structure)"
    echo "   - Test iOS build in Xcode"
    echo ""
    echo -e "${CYAN}📊 Project Details:${NC}"
    echo "   Package Name: $new_package"
    echo "   Project Name: $new_project_name"
    echo "   Bundle ID: $new_bundle_id"
    echo ""
    echo -e "${GREEN}Happy coding! 🚀${NC}"
}

# Help function
show_help() {
    cat << EOF
Usage: $SCRIPT_NAME [OPTIONS]

Transform a Compose Multiplatform template project into a new project with custom names,
package identifiers, folder structure, and deeplink schemas.

OPTIONS:
    --dry-run       Show what would be done without making changes
    --verbose       Enable verbose logging
    --help          Show this help message

EXAMPLES:
    $SCRIPT_NAME                    # Run with interactive prompts
    $SCRIPT_NAME --dry-run          # Preview changes without applying them
    $SCRIPT_NAME --verbose          # Run with detailed logging

For more information, see the documentation in SETUP_SCRIPT_README.md
EOF
}

# Parse command line arguments
parse_arguments() {
    while [[ $# -gt 0 ]]; do
        case $1 in
            --dry-run)
                DRY_RUN=true
                shift
                ;;
            --verbose)
                VERBOSE=true
                shift
                ;;
            --help)
                show_help
                exit 0
                ;;
            *)
                print_error "Unknown option: $1"
                show_help
                exit 1
                ;;
        esac
    done
}

# Main function
main() {
    # Initialize logging
    echo "Starting setup script at $(date)" > "$LOG_FILE"
    
    # Parse arguments
    parse_arguments "$@"
    
    echo -e "${PURPLE}==================================================================${NC}"
    echo -e "${PURPLE}    Compose Multiplatform Template Project Setup Script ${NC}"
    echo -e "${PURPLE}==================================================================${NC}"
    echo ""
    
    if [[ "$DRY_RUN" == "true" ]]; then
        print_warning "DRY RUN MODE: No changes will be made"
        echo ""
    fi
    
    # Get user input
    echo -e "${CYAN}Please provide the following information for your new project:${NC}"
    echo ""
    
    # Get project name
    while true; do
        read -p "Enter your project name (e.g., MyAwesomeApp): " NEW_PROJECT_NAME_RAW
        NEW_PROJECT_NAME=$(to_project_name "$NEW_PROJECT_NAME_RAW")
        
        if validate_input "$NEW_PROJECT_NAME" "^[a-zA-Z][a-zA-Z0-9]*$" "Invalid project name. Must start with a letter and contain only alphanumeric characters"; then
            break
        fi
    done

    # Get package name
    while true; do
        read -p "Enter your package name (e.g., org.example.project): " NEW_PACKAGE_RAW
        NEW_PACKAGE=$(to_package_name "$NEW_PACKAGE_RAW")
        
        if validate_input "$NEW_PACKAGE" "^[a-z][a-z0-9]*(\.[a-z][a-z0-9]*)*$" "Invalid package name. Must be in format: com.yourcompany.yourapp"; then
            break
        fi
    done
    
    # Use package name as bundle identifier (standard practice)
    NEW_BUNDLE_ID="$NEW_PACKAGE"
    
    # Extract domain from package name
    NEW_DOMAIN=$(get_domain "$NEW_PACKAGE")
    
    echo ""
    echo -e "${CYAN}📋 Configuration Summary:${NC}"
    echo "   Project Name: $NEW_PROJECT_NAME"
    echo "   Package Name: $NEW_PACKAGE"
    echo "   Bundle ID: $NEW_BUNDLE_ID.$NEW_PROJECT_NAME"
    echo "   Domain: $NEW_DOMAIN"
    echo ""
    
    # Confirm before proceeding
    read -p "Do you want to proceed with the transformation? (y/N): " confirm
    if [[ ! $confirm =~ ^[Yy]$ ]]; then
        print_info "Transformation cancelled by user"
        exit 0
    fi
    
    echo ""
    print_step "Starting project transformation..."
    
    # Create backup directory
    if [[ "$DRY_RUN" == "false" ]]; then
        mkdir -p "$BACKUP_DIR"
        print_info "Backup directory created: $BACKUP_DIR"
    fi
    
    # Execute transformation steps
    create_directory_structure "$OLD_PACKAGE" "$NEW_PACKAGE"
    update_source_files "$OLD_PACKAGE" "$NEW_PACKAGE" "$OLD_PROJECT_NAME" "$NEW_PROJECT_NAME" "$OLD_BUNDLE_ID" "$NEW_BUNDLE_ID" "$OLD_DOMAIN" "$NEW_DOMAIN"
    update_config_files "$OLD_PACKAGE" "$NEW_PACKAGE" "$OLD_PROJECT_NAME" "$NEW_PROJECT_NAME" "$OLD_BUNDLE_ID" "$NEW_BUNDLE_ID" "$OLD_DOMAIN" "$NEW_DOMAIN"
    update_documentation "$OLD_PACKAGE" "$NEW_PACKAGE" "$OLD_PROJECT_NAME" "$NEW_PROJECT_NAME" "$OLD_BUNDLE_ID" "$NEW_BUNDLE_ID" "$OLD_DOMAIN" "$NEW_DOMAIN"
    update_workflows "$OLD_PACKAGE" "$NEW_PACKAGE" "$OLD_PROJECT_NAME" "$NEW_PROJECT_NAME" "$OLD_BUNDLE_ID" "$NEW_BUNDLE_ID" "$OLD_DOMAIN" "$NEW_DOMAIN"
    update_other_configs "$OLD_PACKAGE" "$NEW_PACKAGE" "$OLD_PROJECT_NAME" "$NEW_PROJECT_NAME" "$OLD_BUNDLE_ID" "$NEW_BUNDLE_ID" "$OLD_DOMAIN" "$NEW_DOMAIN"
    
    # Update Xcode project (must be done after other updates)
    update_xcode_project "$OLD_PROJECT_NAME" "$NEW_PROJECT_NAME" "$OLD_BUNDLE_ID" "$NEW_BUNDLE_ID"
    
    # Update deeplink schemas
    update_deeplink_schemas "$OLD_PROJECT_NAME" "$NEW_PROJECT_NAME" "$OLD_DOMAIN" "$NEW_DOMAIN" "$NEW_BUNDLE_ID"
    
    # Update deeplink route comments in Kotlin files
    update_deeplink_comments "$OLD_PROJECT_NAME" "$NEW_PROJECT_NAME"
    
    # Update import statements with new package name
    update_import_statements "$OLD_PACKAGE" "$NEW_PACKAGE"
    
    # Update app name in strings.xml
    update_app_name_strings "$OLD_PROJECT_NAME" "$NEW_PROJECT_NAME"

    # Update compose.resources package configuration
    update_compose_resources_package "$OLD_PACKAGE" "$NEW_PACKAGE"

    # Create Firebase configuration files
    create_firebase_configs "$NEW_PACKAGE" "$NEW_PROJECT_NAME" "$NEW_BUNDLE_ID"
    
    # Create local.properties with API key placeholders
    create_local_properties
    
    # Remove old directories
    remove_old_directories "$OLD_PACKAGE"
    
    # Clean up backup files
    cleanup_backups
    
    # Validate transformation
    if validate_transformation "$NEW_PACKAGE" "$NEW_PROJECT_NAME" "$NEW_BUNDLE_ID"; then
        show_final_instructions "$NEW_PACKAGE" "$NEW_PROJECT_NAME" "$NEW_BUNDLE_ID"
        log_info "Script completed successfully"
    else
        print_error "Transformation validation failed. Please check the output above for errors."
        log_error "Transformation validation failed"
        exit 1
    fi
}

# Run main function
main "$@"
