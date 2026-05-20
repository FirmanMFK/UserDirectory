# User Directory App

A robust, production-grade Android application showcasing a user directory with offline-first capabilities, built using Clean Architecture and modern Jetpack Compose.

## a. How to Use the Application

1.  **View User List**: When first opened, the app loads data from the local cache (offline-first) and automatically synchronizes the latest data from the API.
2.  **Search**: Use the search bar at the top to filter users by name in real-time (with a 500ms debounce).
3.  **City Filter**: Click the "Filter by City" button to show a Bottom Sheet containing a list of cities fetched from the API. Select a city to filter the list.
4.  **Sorting**: Click the "Sort" button to sort user names A-Z or Z-A. You can clear the sort filter to return to the original API order (based on ID).
5.  **User Details**: Click the "See Detail" button on a user card to view the full profile, contact information, and location.
6.  **Add User**: Click the Floating Action Button (+) to open the "Add New User" form. Fill in all data (Name, Email, Phone, Address, City, Gender) until the "Save" button becomes active.
7.  **Pull to Refresh**: Pull the list down to force a re-synchronization of data from the server.
8.  **Dark Mode**: Use the theme icon in the Top Bar to switch between Light and Dark modes.

## b. Technologies Used (Tech Stack)

*   **UI & Framework**: 
    *   **Jetpack Compose**: For building a modern declarative UI.
    *   **Material 3**: Google's latest design standard for consistent UI components.
*   **Architecture**:
    *   **Clean Architecture**: Clear separation of layers (`Data`, `Domain`, `Presentation`).
    *   **MVVM (Model-View-ViewModel)**: UI state management pattern.
    *   **Package by Feature**: Code organization based on features for ease of scalability.
*   **Dependency Injection**:
    *   **Koin**: A lightweight, Kotlin-friendly DI framework for managing dependencies between layers.
*   **Networking & Data**:
    *   **Retrofit & OKHttp**: For RESTful API communication.
    *   **Kotlinx Serialization**: For type-safe JSON parsing.
    *   **Room Database**: As the *Single Source of Truth* for local data storage (Offline-First).
*   **Background Tasks**:
    *   **WorkManager**: Used for automatic background data synchronization every 1 hour.
*   **Navigation**:
    *   **Type-Safe Compose Navigation**: Navigation between screens using Kotlin Serialization for safe data object transfer.

## c. UI & Interaction Philosophy (UX/UI Rationale)

*   **Offline-First Experience**: Users don't have to wait for an empty loading screen when opening the app. Data from the local database is displayed immediately while the app updates in the background. This gives the impression of an "instant" and reliable app.
*   **Shimmer Loading & Skeleton**: Instead of using traditional, boring spinners, the app uses a **Shimmer Skeleton UI**. This provides a visual indication of the content structure that will appear, reducing the user's perceived waiting time.
*   **Debounced Search**: Search has a short delay (debounce) to avoid overloading the database/processor on every keystroke, keeping performance smooth.
*   **Sticky Bottom Buttons**: On the add user form, the Save/Cancel buttons are made *sticky* at the bottom of the screen to be easily reachable by the thumb (*thumb zone*) even if the form is very long.
*   **Visual Feedback (Error & Empty States)**: The app provides clear messages if data is empty or a network disruption occurs, complete with a "Retry" button to facilitate functional recovery.
*   **Transactional Sync**: The process of clearing and refilling data is performed in a single *database transaction* so that users never see an empty or broken list during the synchronization process.

## d. Project Structure

The project follows **Clean Architecture** principles organized by **Feature**, ensuring a clear separation of concerns and high scalability.

```text
com.firman.directoryuser/
├── core/                       # Shared modules and infrastructure
│   ├── di/                     # Koin Dependency Injection modules
│   ├── navigation/             # Type-safe Compose Navigation setup
│   └── theme/                  # Material 3 Theme and Design System
└── feature/                    # Feature-based modules
    └── user/                   # User Directory Feature
        ├── data/               # Data Layer: APIs, Room DB, Repositories, Workers
        │   ├── local/          # Room DAO and Entities
        │   ├── mapper/         # Data Mappers (DTO <-> Domain <-> Entity)
        │   ├── remote/         # Retrofit Service and DTOs
        │   ├── repository/     # Repository implementations
        │   └── worker/         # Background Sync Workers
        ├── domain/             # Domain Layer: Business Logic (Pure Kotlin)
        │   ├── model/          # Domain Models
        │   ├── repository/     # Repository Interfaces
        │   └── usecase/        # Specific Business Use Cases
        └── presentation/       # Presentation Layer: UI/UX (Jetpack Compose)
            ├── add/            # Add New User Screen & ViewModel
            ├── components/     # Reusable UI Components
            ├── detail/         # User Detail Screen
            └── list/           # User List Screen & ViewModel
```

## Demo Videos

![User List & Navigation Demo](C:/Users/Firman/AppData/Local/Google/AndroidStudio2025.3.4/projects/userdirectory.7b7407a8/.artifacts/20260520-091945-1923f883-bc6a-48e2-8cd9-0fc22179a7d2/Screenrecorder-2026-05-20-22-39-10-98.mp4)

![Add User & Validation Demo](C:/Users/Firman/AppData/Local/Google/AndroidStudio2025.3.4/projects/userdirectory.7b7407a8/.artifacts/20260520-091945-1923f883-bc6a-48e2-8cd9-0fc22179a7d2/Screenrecorder-2026-05-20-22-45-54-84.mp4)
