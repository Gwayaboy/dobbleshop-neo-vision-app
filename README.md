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

## 🔌 Hardware Integration

### ESP32-S3 (Main Controller)
- **Connectivity**: BLE 5.0 + WiFi 802.11 b/g/n
- **Controls**: Servo motor (food dispenser), peristaltic pump (water)
- **Sensors**: 
  - HX711 load cell (bowl weight detection)
  - VL53L0X ToF (food reservoir level)
  - Water level sensor (reservoir monitoring)
  - DS3231 RTC (scheduled feeding timing)
- **Power**: Battery + USB-C with INA219 monitoring

### Raspberry Pi Zero 2W (Media & Security)
- **Camera**: USB camera with H.264 encoding → HLS streaming
- **Audio In**: INMP441 MEMS microphone (hear pet environment)
- **Audio Out**: MAX98357A I2S amplifier (speak to pet)
- **Security**: LD2410C mmWave presence sensor (home security mode)
- **Streaming**: HLS for remote viewing, MJPEG for local access

### Communication Protocols
- **Cloud ↔ Device**: MQTT or CoAP over WiFi
- **App ↔ Cloud**: HTTPS REST + WebSocket (telemetry)
- **App ↔ Device (Local)**: BLE GATT characteristics
- **Camera Stream**: HLS (remote) or HTTP (local WiFi)

## 🏗️ Current Status

### Phase 1 - UI Complete ✅
✅ Project structure created  
✅ Gradle configuration complete  
✅ Design system implemented (vivid colors matching mock)  
✅ Navigation setup complete (4 tabs)  
✅ All main screens implemented:
  - DashboardScreen (Home with device status, animal selection, activity)
  - FeedingScreen (Repas - schedules, manual distribution)
  - CameraScreen (live feed, audio, security controls)
  - SettingsScreen (complete menu)
  - ReservoirsDetailScreen (food/water/bowl levels)
  - HistoryScreen (charts and distribution journal)

### Phase 1.5 - Data & Connectivity ✅
✅ Domain models (Device, Pet, DeviceStatus, Schedule, Alert)  
✅ Room database with MVVM architecture  
✅ **Connectivity architecture implemented** (Internet + Bluetooth)  
✅ Retrofit API service with complete endpoint coverage  
✅ Network state monitoring  
✅ Connection manager (cloud/Bluetooth switching)  
✅ Unified DeviceRepository abstraction layer  
✅ Hilt dependency injection configured  

### Phase 2 - Backend Integration (Next)
- [ ] Deploy cloud backend API
- [ ] Implement WebSocket for real-time telemetry
- [ ] Set up JWT authentication flow
- [ ] Test API endpoints with real backend
- [ ] Implement token refresh logic
- [ ] Add error retry and offline queuing

### Phase 3 - Bluetooth & Hardware (Future)
- [ ] Implement BluetoothDeviceService (BLE GATT)
- [ ] WiFi provisioning flow during onboarding
- [ ] ESP32-S3 firmware integration
- [ ] Raspberry Pi camera streaming (HLS)
- [ ] Two-way audio (WebRTC)
- [ ] End-to-end hardware testing

## 📡 Connectivity Architecture

This app communicates with physical DOBBLESHOP NEO VISION pet feeders through **dual connectivity**:

### Internet (Cloud Mode) 🌐
- Remote access from anywhere
- REST API + WebSocket for real-time telemetry
- HLS camera streaming from Raspberry Pi
- Two-way audio via WebRTC
- Full feature access (schedules, history, security)

### Bluetooth (Local Mode) 📶
- Direct BLE connection when nearby
- WiFi provisioning during device setup
- Manual feeding/water commands
- Real-time sensor data
- Works without internet

### Hybrid Mode 🔄
- Automatic switching based on availability
- Prefers cloud (lower battery drain)
- Seamless fallback to Bluetooth
- Smart latency optimization

**See [`CONNECTIVITY_ARCHITECTURE.md`](CONNECTIVITY_ARCHITECTURE.md) for full details.**  
**See [`CONNECTIVITY_IMPLEMENTATION.md`](CONNECTIVITY_IMPLEMENTATION.md) for implementation guide.**

## 📋 Architecture

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
