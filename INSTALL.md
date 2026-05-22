# DOBBLESHOP NEO VISION - Installation Guide

## 📱 APK File
**File Name:** `DOBBLESHOP-NEO-VISION.apk`  
**Size:** 21 MB  
**Version:** Debug Build (May 22, 2026)

## 🚀 Quick Install (Android Device)

### Method 1: Direct Install on Device
1. Copy `DOBBLESHOP-NEO-VISION.apk` to your Android device
2. Open the APK file on your device
3. Allow installation from unknown sources if prompted
4. Tap "Install"
5. Tap "Open" when installation completes

### Method 2: Install via USB (Windows)
1. Connect your Android device via USB
2. Enable USB debugging on your device
3. Run `install-to-device.bat` (Windows) 
4. The app will install automatically

### Method 3: Install via ADB Command Line
```bash
adb install -r DOBBLESHOP-NEO-VISION.apk
```

## ⚙️ Requirements
- **Android Version:** 8.0 (API 26) or higher
- **Recommended:** Android 14+ for best experience
- **Storage:** 50 MB free space
- **Permissions:** Camera, Microphone, Internet access

## 🔧 Enable Unknown Sources (if needed)

### Android 8.0 - 12:
1. Go to **Settings** > **Security**
2. Enable **Unknown Sources** or **Install Unknown Apps**
3. Allow installation from Files/Chrome/etc.

### Android 13+:
1. When you tap the APK, you'll be prompted automatically
2. Tap **Settings** in the prompt
3. Enable **Allow from this source**
4. Go back and tap Install

## 📲 What's Included
- ✅ Dashboard with pet management
- ✅ Feeding schedules and manual distribution
- ✅ Camera feed with audio controls (UI ready)
- ✅ Security settings (mmWave sensor integration)
- ✅ Complete device connectivity infrastructure
- ✅ Mock data for testing without backend

## ⚠️ Note
This is a **DEBUG** build for testing purposes. It includes:
- Mock pet data (Léo the cat) for demonstration
- Full UI with vivid colorful design
- DeviceRepository integration ready for backend
- Expected errors when trying device operations (no backend connected)

## 🔗 Sharing Instructions
You can share this APK via:
- Email attachment
- Cloud storage (Google Drive, Dropbox, etc.)
- USB transfer
- Messaging apps (WhatsApp, Telegram, etc.)
- AirDrop/Nearby Share

## 🆘 Troubleshooting

**Installation blocked?**
- Make sure "Install Unknown Apps" is enabled for the source app

**App won't open?**
- Check Android version is 8.0+
- Clear app data and reinstall

**"Device not connected" errors?**
- This is expected! The app needs a backend server to connect to actual hardware
- All UI features work, device operations show expected error messages

## 📧 Support
For issues or questions about the DOBBLESHOP NEO VISION app, contact your development team.

---
**Built on:** May 22, 2026  
**Package:** com.dobbleshop.neovision.debug  
**Target SDK:** Android 14 (API 36)
