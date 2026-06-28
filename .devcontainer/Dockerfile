# Android Development Container
FROM ubuntu:22.04

# Set environment variables
ENV ANDROID_SDK_ROOT=/opt/android-sdk \
    ANDROID_HOME=/opt/android-sdk \
    JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 \
    PATH=$PATH:/opt/android-sdk/cmdline-tools/latest/bin:/opt/android-sdk/platform-tools:/opt/android-sdk/tools/bin:/usr/lib/jvm/java-17-openjdk-amd64/bin \
    GRADLE_USER_HOME=/workspace/.gradle

# Install system dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    # Base tools
    curl \
    wget \
    git \
    zip \
    unzip \
    # Java 17
    openjdk-17-jdk-headless \
    openjdk-17-jre-headless \
    # Build tools
    build-essential \
    pkg-config \
    libstdc++6 \
    lib32stdc++6 \
    libx11-6 \
    libx11-dev \
    libgl1-mesa-glx \
    libgl1-mesa-dev \
    libxext6 \
    libxext-dev \
    # For Android emulator (if needed)
    qemu-system-x86 \
    libglib2.0-0 \
    libpulse0 \
    libxkbcommon0 \
    # Additional utilities
    vim \
    nano \
    which \
    && rm -rf /var/lib/apt/lists/*

# Create Android SDK directory
RUN mkdir -p ${ANDROID_SDK_ROOT}

# Download and install Android cmdline-tools
RUN cd /tmp && \
    curl -L -o cmdline-tools.zip https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip && \
    unzip -q cmdline-tools.zip && \
    mkdir -p ${ANDROID_SDK_ROOT}/cmdline-tools && \
    mv cmdline-tools ${ANDROID_SDK_ROOT}/cmdline-tools/latest && \
    rm cmdline-tools.zip

# Accept Android licenses
RUN mkdir -p ${ANDROID_SDK_ROOT}/licenses && \
    echo "8933bad161af4d5ce126e23e7a931aad" > ${ANDROID_SDK_ROOT}/licenses/android-sdk-license && \
    echo "d56f5187479451eabf01fb78af6dfcb131b33910" > ${ANDROID_SDK_ROOT}/licenses/android-sdk-preview-license && \
    echo "33b6a2b64607f11b759f72e1205dda6edd3a3d99" > ${ANDROID_SDK_ROOT}/licenses/google-android-sdk-license

# Install Android SDK components
RUN ${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin/sdkmanager --sdk_root=${ANDROID_SDK_ROOT} \
    "platforms;android-26" \
    "platforms;android-36" \
    "build-tools;36.0.0" \
    "tools" \
    "platform-tools" \
    "cmdline-tools;latest" \
    "system-images;android-36;default;x86_64" \
    "emulator" \
    && echo "SDK installation complete"

# Verify installations
RUN echo "Java version:" && java -version && \
    echo "\nAndroid SDK Manager:" && ${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin/sdkmanager --list_installed

WORKDIR /workspace
