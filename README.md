# Daily Focus: Habit & Streak Tracker

**CP3406 / CP5307 Assessment 1: Utility App**

"Daily Focus" is a lightweight, at-a-glance Android utility application designed to help users stay committed to a single daily core habit (e.g., *Read 20 Mins*, *Drink 2L Water*, *Morning Workout*) alongside a daily motivational focus quote fetched dynamically from the public ZenQuotes API.

---

## 📱 App Overview & Utility Purpose

Utility apps provide rapid, focused information to users with minimal friction. "Daily Focus" adheres strictly to this principle:
- **Instant Glanceability:** The main screen immediately informs the user whether today's habit is finished, their current streak count, and their progress towards their streak goal.
- **One-Tap Interaction:** A prominent toggle button allows the user to mark the habit done or undo it instantly.
- **Daily Inspiration:** A curated daily motivational quote fetched from an external web API keeps the user engaged without cluttering the interface.

---

## ✨ Core Features

1. **At-a-Glance Dashboard (`UtilityScreen`)**:
   - **Today's Habit Card:** Displays the current active habit with animated state transitions between "Mark as Done" and "Completed Today!".
   - **Streak & Goal Progress:** Visual streak counter and `LinearProgressIndicator` calculating percentage completion towards the user's target goal.
   - **Daily Focus Quote Card:** Asynchronously fetches today's quote from the ZenQuotes API with loading indicators, error handling, and manual refresh/retry support.
2. **Customizable Settings (`SettingsScreen`)**:
   - **Custom Habit Input:** `OutlinedTextField` allowing users to name any daily habit.
   - **Quick Suggestion Presets:** `FilterChip` buttons for common habits (*Read 20 Mins*, *Drink 2L Water*, *Daily Workout*, *10m Meditation*, *Code Practice*).
   - **Streak Target Slider:** Interactive `Slider` allowing goals from 3 to 30 days.
   - **In-Memory State:** As specified in the assignment requirements, settings update session state in memory without complex persistent storage.
3. **Bottom Bar Navigation (`UtilityApp`)**:
   - Seamless toggling between `Utility` and `Settings` using Material 3 `NavigationBar`.

---

## 🛠️ Architecture & Technical Stack

The project demonstrates mastery of the introductory Android syllabus (Weeks 1–5):

| Component / Layer | Technology / Pattern | Implementation Details |
|---|---|---|
| **UI Framework** | Jetpack Compose & Material 3 | Declarative UI with `ElevatedCard`, `Button`, `Slider`, `LinearProgressIndicator`, `OutlinedTextField` |
| **State Management** | `ViewModel` + `StateFlow` | `HabitViewModel` exposes `HabitUiState` to Compose via `collectAsState()` |
| **Data Layer** | Repository Pattern | `HabitRepository` acts as the single source of truth for in-memory habit state and remote quote data |
| **Dependency Injection** | Manual DI | `HabitViewModelFactory` implementing `ViewModelProvider.Factory` (no third-party DI frameworks) |
| **Networking** | Retrofit 2 + Gson | `ZenQuotesApiService` queries `https://zenquotes.io/api/today` via Kotlin Coroutines |

---

## 📂 Project Structure

```
app/src/main/java/au/edu/jcu/assessment/utilityapp/
├── MainActivity.kt               // Activity & Scaffold with Bottom Navigation
├── data/
│   ├── model/
│   │   ├── Habit.kt             // In-memory habit data model
│   │   └── Quote.kt             // ZenQuotes API response data class
│   ├── network/
│   │   └── ZenQuotesApiService.kt // Retrofit interface & RetrofitInstance singleton
│   └── repository/
│       └── HabitRepository.kt   // Single source of truth (remote quote + in-memory habit)
└── ui/
    ├── HabitUiState.kt           // Immutable UI state representation
    ├── HabitViewModel.kt         // ViewModel & manual HabitViewModelFactory
    ├── screens/
    │   ├── UtilityScreen.kt      // Primary utility screen
    │   └── SettingsScreen.kt     // Configuration & preferences screen
    └── theme/                    // Material 3 theme, colors, typography
```

---

## 🚀 How to Run

1. Open the project in **Android Studio** (Ladybug / Koala or newer).
2. Allow Gradle to sync the dependencies (`Retrofit`, `converter-gson`, and `lifecycle-viewmodel-compose`).
3. Select an emulator or connected physical device running **Android 8.0 (API 26)** or higher.
4. Click **Run** (`Shift + F10`).
