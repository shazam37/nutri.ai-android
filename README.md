# NutriAI Android

Native Kotlin + Jetpack Compose POC frontend for the NutriAI backend.

## Open

Open the `android/` directory in Android Studio and let Gradle sync.

The debug backend URL is configured in `app/build.gradle.kts`:

```kotlin
buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:8000/api/v1/\"")
```

Use `10.0.2.2` for the Android emulator to reach a backend running on your machine at `localhost:8000`.

## Current POC Scope

- Signup and login
- Token persistence
- Home dashboard with daily coach card
- Analyze before save meal review flow
- What can I eat now suggestions
- Inventory list and add item
- Meal plan generation and acceptance
- Recent daily history
