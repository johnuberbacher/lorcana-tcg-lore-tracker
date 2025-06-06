# Lorcana TCG Lore Tracker

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)

A minimal Wear OS app built in Kotlin to help players track scores in the card game **Lorcana**.

Instead of using physical dice or counters, this app provides a quick and accessible way to keep
track of lore points directly on your watch.

---

## Features

- [x] Tap to increment or decrement scores for two players
- [x] Tap the score cap to cycle between win conditions (20 / 25 / 15 / 10)
- [x] Dark/OLED-friendly theme
- [x] Tile and complication support for Wear OS
- [x] Built with Jetpack Compose and Material 3
- [x] Option to mute sound effects
- [x] Save score screen to device as an image

---

## Planned Features

- [ ] Customizable background images

---

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/lorcana-tcg-tracker.git
   ```

2. Open in Android Studio.

3. Connect a Wear OS device or emulator.

4. Run the project.

---

## Building & Installing Locally

Follow these steps to build the app locally and install it on a connected Wear OS device or emulator using `adb`.

1. Clone the Repository

```bash
git clone https://github.com/yourusername/lorcana-tcg-tracker.git
cd lorcana-tcg-tracker
```

2. Build the APK

Use Gradle to build the debug APK:

```bash
./gradlew assembleDebug
```

After building, you'll find the APK at:

```
app/build/outputs/apk/debug/app-debug.apk
```

3. Connect Your Wear OS Device or Start an Emulator

- Ensure **USB debugging** is enabled on your Wear OS device.
- Check that your device is recognized:

```bash
adb devices
```

4. Install the APK via ADB

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```
