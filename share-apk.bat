@echo off
echo ================================================
echo  DOBBLESHOP NEO VISION - Share APK
echo ================================================
echo.

set APK_PATH=app\build\outputs\apk\debug\app-debug.apk

if not exist %APK_PATH% (
    echo APK not found. Building...
    echo.
    call gradlew.bat assembleDebug
    if %ERRORLEVEL% NEQ 0 (
        echo Build failed!
        pause
        exit /b 1
    )
)

echo.
echo ✓ APK ready!
echo.
echo Location: %APK_PATH%
echo.
for %%A in (%APK_PATH%) do echo Size: %%~zA bytes (%%~zA / 1048576 MB)
echo.
echo ================================================
echo  SHARING OPTIONS:
echo ================================================
echo.
echo 1. Email
echo    - Attach the APK file above
echo    - Recipient: Enable "Install unknown apps" → Open APK
echo.
echo 2. Google Drive / Dropbox / OneDrive
echo    - Upload the APK file
echo    - Share link with recipient
echo    - Right-click file above → "Send to" → your cloud service
echo.
echo 3. WhatsApp / Telegram / Signal
echo    - Send as file attachment
echo    - Works for files under 100MB
echo.
echo 4. Local network
echo    - Copy APK to USB drive
echo    - Transfer via Wi-Fi Direct
echo    - Use "Nearby Share" on Android
echo.
echo 5. QR Code (advanced)
echo    - Host APK on local web server
echo    - Generate QR code with URL
echo    - Recipients scan to download
echo.
echo ================================================
echo.

echo Opening APK folder...
explorer.exe /select,%APK_PATH%

echo.
echo Press any key to copy APK path to clipboard...
pause >nul
echo %CD%\%APK_PATH% | clip
echo ✓ Path copied to clipboard!
echo.
pause
