@echo off
echo ================================================
echo  DOBBLESHOP NEO VISION - Install to Device
echo ================================================
echo.

echo Checking for connected devices...
"C:\Users\frtheola\AppData\Local\Android\Sdk\platform-tools\adb.exe" devices
echo.

echo Installing app...
"C:\Users\frtheola\AppData\Local\Android\Sdk\platform-tools\adb.exe" install -r app\build\outputs\apk\debug\app-debug.apk

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✓ Installation successful!
    echo.
    echo Launching app...
    "C:\Users\frtheola\AppData\Local\Android\Sdk\platform-tools\adb.exe" shell am start -n com.dobbleshop.neovision/.MainActivity
    echo.
    echo ✓ App launched! Check your device.
) else (
    echo.
    echo ✗ Installation failed. Make sure USB debugging is enabled.
)

echo.
pause
