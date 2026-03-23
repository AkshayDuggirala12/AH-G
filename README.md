# HARA-GYM Android App

A clean, modern gym mobile app built with Jetpack Compose, MVVM, Hilt, and Retrofit.

## Features
- **Login/Register**: JWT authentication.
- **My Workout Plan**: See your assigned plan and exercise days.
- **Workout Details**: View exercises, sets, reps, and toggle completion status.
- **My Diet Plan**: View your personalized nutrition plan.
- **Weekly Progress**: Track your completion percentages across the week.
- **Profile**: Manage your account and logout.

## Backend Connection
- **Base URL**: The default base URL is set to `http://10.0.2.2:8000/` in `NetworkModule.kt` for Android emulator testing. 
- **Change URL**: To point to a physical device or different server, update the `BASE_URL` constant in `com.example.hara_gym.di.NetworkModule`.

## How Login Works
1. User enters email/password.
2. App calls `POST /auth/login`.
3. The backend returns a `access_token`.
4. The `TokenManager` stores this token using **Android DataStore Preferences**.
5. An `AuthInterceptor` automatically retrieves the token and adds it to the `Authorization: Bearer <token>` header for all subsequent API calls.

## Tech Stack
- **Kotlin** & **Jetpack Compose**
- **Hilt** (Dependency Injection)
- **Retrofit** & **OkHttp** (Networking)
- **DataStore** (Token Persistence)
- **Navigation Compose**
- **Coroutines** & **StateFlow**

## Getting Started
1. Open this project in Android Studio.
2. Ensure your HARA-GYM FastAPI backend is running.
3. If using an emulator, `10.0.2.2` should work.
4. Sync Gradle and Run the app.
