# DOBBLESHOP NEO VISION - Development Container

This devcontainer provides a complete Android development environment for building and testing the DOBBLESHOP NEO VISION app in GitHub Codespaces or locally.

## 🚀 Quick Start

### Using GitHub Codespaces
1. Go to your repository on GitHub
2. Click **Code** → **Codespaces** → **Create codespace on main**
3. Wait for the container to build (~5-10 minutes)
4. VS Code will open with Android development tools ready

### Local Development with Docker
```bash
# Open in devcontainer locally (requires Docker and Dev Containers extension)
# Open in VS Code → F1 → "Dev Containers: Reopen in Container"
```

## 📋 What's Included

### Core Tools
- **JDK 17** - Java Development Kit
- **Android SDK 36** - Latest Android API level
- **Android SDK 26** - Minimum supported API level
- **Build Tools 36.0.0** - Latest build toolchain
- **Android Emulator** - x86_64 system image for testing
- **ADB (Android Debug Bridge)** - Device communication
- **Gradle** - Build system (wrapper managed)

### VS Code Extensions
- Android NDK Manager
- Kotlin Language Support
- Gradle for Java
- Docker Support
- Git Lens

### System Dependencies
- Ubuntu 22.04 LTS base
- Build essentials (gcc, g++, make)
- Git, curl, wget, unzip
- QEMU for emulation support

## 🔨 Building and Testing

### Build the App
```bash
cd /workspace
./gradlew build
```

### Build Debug APK
```bash
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

### Run Unit Tests
```bash
./gradlew test
```

### Run Instrumented Tests (requires emulator)
```bash
./gradlew connectedAndroidTest
```

## 📱 Using the Android Emulator

### Start Emulator
```bash
# List available emulators
emulator -list-avds

# Start Android 14 API 36 emulator
emulator -avd Android_14_API_36
```

### Connect via ADB
```bash
# List connected devices
adb devices

# Install APK
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Launch app
adb shell am start -n com.dobbleshop.neovision.debug/com.dobbleshop.neovision.MainActivity
```

## 🐛 Troubleshooting

### Gradle Build Fails
```bash
# Clean and rebuild
./gradlew clean build

# Check Java version
java -version  # Should be JDK 17+

# Check Android SDK
${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin/sdkmanager --list_installed
```

### Emulator Issues
```bash
# Check emulator availability
emulator -list-avds

# View emulator logs
adb logcat

# Restart ADB
adb kill-server
adb start-server
```

### Out of Memory
The devcontainer is configured with 4GB JVM heap. If you hit OOM:
```bash
# Increase in gradle.properties
org.gradle.jvmargs=-Xmx6g -XX:MaxMetaspaceSize=1g
```

## 📦 Gradle Configuration

Key settings from `gradle.properties`:
- **JVM Heap**: 4GB (Xmx4g)
- **Metaspace**: 1GB max
- **Parallel Builds**: Enabled
- **Build Caching**: Enabled
- **Incremental Kotlin**: Enabled

## 🌐 Port Forwarding

Automatically forwarded ports:
- **5554** - Emulator Console
- **5555** - ADB Server
- **8080** - Local Debug Server

## 📂 Directory Structure

```
/workspace/
├── .devcontainer/          # This directory
├── app/                    # Main app module
├── build.gradle.kts        # Top-level build config
├── gradle.properties       # Gradle settings
├── gradlew                 # Gradle wrapper
└── local.properties        # Local SDK paths (git-ignored)
```

## 🔐 Security

- Container runs with minimal required privileges
- Android SDK licenses pre-accepted
- Gradle cache persisted between sessions for faster builds
- No hardcoded credentials (use environment variables)

## 📖 Resources

- [Android Developer Docs](https://developer.android.com/)
- [Gradle Documentation](https://gradle.org/)
- [Kotlin Documentation](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)

---

For issues or improvements, update the Dockerfile or devcontainer.json and commit to the repository.
