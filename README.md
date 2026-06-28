# Scientific Calculator – Android App

**Course:** CSC 104 / SEN 104 & 214
**Developer:** Ajibola David Chinedu
**Built with:** AIDE (Android IDE for mobile), Java, XML

## Overview
A multi-mode calculator app supporting basic arithmetic, scientific/trigonometric functions, matrix operations, and statistics/combinatorics — built entirely on a phone using AIDE rather than Android Studio.

## Installation & Setup

### Option A — Build from source
1. Clone or download this repository
2. Open the project in any Android development environment that supports standard Gradle Android projects (e.g. Android Studio, AIDE, or similar)
3. Sync/build the project
4. Run on a connected device or emulator

**Note:** This project was developed and tested in AIDE (Android IDE for mobile) on an Android device. It uses standard Gradle build files (`build.gradle`, `settings.gradle`) and plain Java with the Android framework's base `Activity` class (no AndroidX/AppCompat dependencies), so it should also build normally in Android Studio or other standard Android IDEs.

### Option B — Install the signed APK directly
1. Download the signed APK from the [Releases](https://github.com/oyekaleblessing/ScientificCalculator/releases) page
2. On an Android device, enable **Install from unknown sources** if prompted
3. Open the downloaded APK file to install
4. Launch "ScientificCalculator" from the app drawer

**Minimum Android version:** API 24 (Android 7.0 Nougat) or higher


## Screenshots

| Main Menu | Basic Calculator |
|---|---|
| 

![Main Menu](screenshots/main_menu.jpg)

 | 

![Basic Calculator](screenshots/basic_calculator.jpg)

 |

| Scientific Calculator | Matrix Calculator |
|---|---|
| 

![Scientific](screenshots/scientific_calculator.jpg)

 | 

![Matrix](screenshots/matrix_calculator.jpg)

 |

| Statistics | Settings |
|---|---|
| 

![Statistics](screenshots/stats.jpg)

 | 

![Settings](screenshots/settings.jpg)

 |

## Features

### Basic Calculator
Addition, subtraction, multiplication, division, decimal input, backspace, and proper "undefined" handling for divide-by-zero.

### Scientific Calculator
- Trigonometric functions (sin, cos, tan) with DEG/RAD toggle
- Hyperbolic functions (sinh, cosh, tanh)
- Logarithms (log base 10, natural log)
- Square root, square, power (xʸ), reciprocal (1/x)
- Constants: π and e
- Supports both "number first" and "function first" input order

### Matrix Calculator
- Supports 2×2, 3×3, and 4×4 matrices
- Operations: addition, subtraction, multiplication, determinant (via cofactor expansion)
- Input grids are generated dynamically in Java based on selected size
- Invalid input shows an inline error instead of crashing

### Statistics & Combinatorics
- Permutations (nPr), Combinations (nCr), Factorial (n!)
- Dataset statistics from comma-separated input: count, mean, median, mode, variance, standard deviation, min, max, range

### Settings
- Key press sound toggle
- Haptic vibration toggle
- Dark mode / Light mode toggle
- Preferences persist using `SharedPreferences`

## Architecture

| File | Purpose |
|---|---|
| `MainActivity.java` | Entry point, navigation menu to all 4 calculator modes + Settings |
| `basiccalculatoractivity.java` | Basic arithmetic logic |
| `scientificactivity.java` | Scientific/trig functions |
| `matrixactivity.java` | Matrix operations, dynamic grid generation |
| `statsactivity.java` | Combinatorics + dataset statistics |
| `settingsactivity.java` | User preference toggles |
| `feedbackhelper.java` | Shared utility — attaches sound/haptic feedback to all buttons app-wide |
| `themehelper.java` | Shared utility — manages dark/light theme state and colors |

All activities extend plain `Activity` (not `AppCompatActivity`) and use anonymous inner classes instead of lambdas, to maintain compatibility with AIDE's build toolchain on mobile.

## Lifecycle Logging
Each activity logs its lifecycle transitions (`onCreate`, `onStart`, `onResume`, `onPause`, `onStop`) via `Log.d()`, viewable in Logcat, to demonstrate the Android Activity lifecycle taught in the course.

## Build Notes
Developed and tested entirely within AIDE on an Android device — no Android Studio or desktop environment was used at any stage.
