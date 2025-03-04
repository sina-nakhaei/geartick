# GearTick - A Lightweight Monitoring Library for Android

![Version](https://img.shields.io/badge/version-1.0.0-blue)
![Platform](https://img.shields.io/badge/platform-Android-brightgreen)
![Kotlin](https://img.shields.io/badge/language-Kotlin-purple)
![JitPack](https://img.shields.io/badge/dependency-JitPack-yellow)

`GearTick` is a simple, easy-to-use library for monitoring system performance metrics like **FPS**, **RAM usage**, and **Network usage** on Android devices. It provides real-time updates, making it useful for applications that need to monitor performance data continuously.

## Features
- **FPS Monitoring**: Track the frame rate of your application to analyze the GPU performance.
- **RAM Monitoring**: Monitor memory usage in real-time, showing both used and available RAM.
- **Network Monitoring**: Track network usage and speeds (download/upload).

## Installation 📦

To use this library in your project, follow these steps:

### Step 1: Add the JitPack repository

In your root `settings.gradle` file, add the JitPack repository:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2: Add the dependency

In your `build.gradle` (Module level), add the dependency:

```gradle
dependencies {
     implementation 'com.github.sina-nakhaei:geartick:$version'
}
```


## Usage 

### Basic Setup
To start using GearTick, simply create a `GearTick` Composable in your UI where you want the system performance data displayed.

### Create the Composable UI
Here's a basic implementation:


```kotlin
Box {
  GearTick()
}
```

## Methods

### FPS Monitoring
To start FPS monitoring, create an instance of `FpsMonitor` and use the `launch` function:

```kotlin
val fpsMonitor = FpsMonitor()

fpsMonitor.launch { fps: Int ->
    // Handle FPS update here
}
```
This function starts monitoring the FPS and invokes the callback with the frame rate.

### RAM Monitoring
To start RAM monitoring, create an instance of `RamMonitor` and use the `launch` function:

```kotlin
val ramMonitor = RamMonitor(context)

ramMonitor.launch { used: Long, available: Long ->
    // Handle RAM update here
}
```

This function starts monitoring the RAM usage and invokes the callback with the used and available memory.

### Network Monitoring
To start network monitoring, create an instance of `NetworkUsageMonitor` and use the `launch` function:

```kotlin
val networkUsageMonitor = NetworkUsageMonitor()

networkUsageMonitor.launch { totalReceivedBytes: Long, totalSentBytes: Long, downloadSpeedKb: Double, uploadSpeedKb: Double ->
    // Handle network update here
}
```

This function starts monitoring network usage and invokes the callback with the total bytes received/sent and the download/upload speeds.

### Stop Monitoring
To stop any monitoring process, use the `stop` function for the respective monitor:

```kotlin
fpsMonitor.stopMonitoring()
ramMonitor.stop()
networkUsageMonitor.stopMonitoring()
```
This will stop the monitoring for FPS, RAM, and network usage.

## 🤝 Contributing
Found a bug? Have an idea? Open an issue or submit a pull request.
