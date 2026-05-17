@echo off
echo ================================================
echo  DOBBLESHOP NEO VISION - Generate Release APK
echo ================================================
echo.

echo This will create a production-ready APK
echo.

REM Check if keystore exists
if not exist keystore.jks (
    echo Creating new signing keystore...
    echo.
    echo You will be prompted for:
    echo - Keystore password (remember this!)
    echo - Key alias (e.g., "dobbleshop-key")
    echo - Key password (can be same as keystore)
    echo - Your name and organization details
    echo.
    
    "C:\Program Files\Android\Android Studio\jbr\bin\keytool.exe" -genkey -v -keystore keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias dobbleshop-key
    
    if %ERRORLEVEL% NEQ 0 (
        echo.
        echo ✗ Keystore creation failed.
        pause
        exit /b 1
    )
    
    echo.
    echo ✓ Keystore created successfully!
    echo.
    echo IMPORTANT: Back up these files securely:
    echo   - keystore.jks
    echo   - Your keystore password
    echo   - Key alias: dobbleshop-key
    echo.
    echo You'll need these to update the app in the future!
    echo.
)

echo Building release APK...
echo.
call gradlew.bat assembleRelease

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✓ Release APK built successfully!
    echo.
    echo Location: app\build\outputs\apk\release\app-release.apk
    echo.
    echo This APK can be:
    echo   1. Shared with power users (smaller size, optimized)
    echo   2. Uploaded to Play Store
    echo   3. Distributed via website/link
    echo.
) else (
    echo.
    echo ✗ Build failed. Check the errors above.
)

pause
