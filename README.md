# DOBBLESHOP NEO VISION - Android App

Premium connected pet feeder application built with Jetpack Compose and MVVM architecture.

## 📱 Project Setup

### Requirements
- **Android Studio**: Hedgehog (2023.1.1) or newer
- **JDK**: 17 or higher
- **Gradle**: 8.7
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)

### Tech Stack
- **Kotlin**: 2.0.0
- **Jetpack Compose**: Latest with Material 3
- **Hilt**: 2.51.1 for dependency injection
- **Navigation Compose**: 2.7.7
- **Coil**: 2.7.0 for image loading
- **Coroutines**: 1.8.1

## 🚀 Getting Started

### 1. Open in Android Studio
```bash
cd dobbleshop-neo-vision-app
# Open this directory in Android Studio
```

### 2. Sync Gradle
- Let Android Studio sync gradle dependencies
- This may take a few minutes on first run

### 3. Run the App
- Select an emulator or physical device
- Click Run (green play button)
- App should launch with bottom navigation

## 📂 Project Structure

```
app/src/main/
├── java/com/dobbleshop/neovision/
│   ├── DobbleShopApplication.kt    # Application class
│   ├── MainActivity.kt              # Main activity
│   └── ui/
│       ├── DobbleShopApp.kt        # Main app scaffold
│       ├── navigation/              # Navigation setup
│       ├── screens/                 # UI screens
│       └── theme/                   # Design system
└── res/
    ├── values/
    │   ├── colors.xml
    │   ├── strings.xml
    │   └── themes.xml
    └── xml/
        └── backup_rules.xml
```

## 🎨 Design System

### Colors
- **Primary**: #1E6FFF (Blue)
- **Secondary**: #00B3A4 (Teal)
- **Background**: #F6F8FC (Light Gray)
- **Surface**: #FFFFFF (White)
- **Status OK**: #10B981 (Green)
- **Status Warning**: #F59E0B (Amber)
- **Status Critical**: #EF4444 (Red)

### Typography
- Material 3 typography with custom scales
- Font weights: Regular, Medium, SemiBold, Bold

## 🏗️ Current Status (Phase 1 Complete)

✅ Project structure created
✅ Gradle configuration complete
✅ Design system implemented
✅ Navigation setup complete
✅ Bottom navigation working
✅ Placeholder screens created

## 📋 Next Steps (Phase 2)

- [ ] Create domain models
- [ ] Implement repositories
- [ ] Set up mock data providers
- [ ] Configure Hilt modules

## 🧪 Running Tests

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

## 📄 License

Private - DOBBLESHOP NEO VISION

## 👥 Development

Follow the DEVELOPMENT_PLAN.md for detailed phase-by-phase implementation guide.
