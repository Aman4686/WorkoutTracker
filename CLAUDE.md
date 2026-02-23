# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Development Commands

```bash
# Build
./gradlew assembleDebug

# Install on connected device/emulator
./gradlew installDebug

# Run all unit tests
./gradlew test

# Run unit tests for a specific module
./gradlew :domain:test
./gradlew :data:test
./gradlew :feature:workout:test

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Lint
./gradlew lint

# Clean build
./gradlew clean assembleDebug
```

## Architecture

The project uses **Clean Architecture** with a **multi-module** setup and an **MVVM + MVI action pattern** for UI state.

### Module Dependency Graph

```
app → domain, data, core, feature:workout
feature:workout → domain, core
data → domain
domain → (pure JVM, no Android)
core → (Android library, Compose UI/theme only)
```

### Layer Responsibilities

- **`domain/`** — Pure Kotlin/JVM module. Contains interfaces (`WorkoutRepository`, `WorkoutDomain`), domain models (`Workout`, `Exercise`, `Set`), and coroutine dispatcher qualifiers. Has no Android dependencies.
- **`data/`** — Room database implementation. Contains `WorkoutRepositoryImpl`, Room entities, DAOs, and entity-to-domain mappers. Provides `@Binds` to connect `WorkoutRepositoryImpl → WorkoutRepository`.
- **`core/`** — Shared Compose UI: Material3 theme, colors, typography.
- **`feature/workout/`** — All workout UI: list and details screens, ViewModels, UI state classes, MVI action sealed interfaces, and `NavigationDestinations`.
- **`app/`** — Entry point. Hosts `MainActivity`, `NavDisplay` (Navigation3), and the Room database Hilt module.

### State Management Pattern

**WorkoutListViewModel** uses a reactive Flow pattern:
- `WorkoutRepository.getWorkoutsFlow()` → mapped to `StateFlow<WorkoutListUIState>` via `stateIn(WhileSubscribed(5000L))`

**WorkoutDetailsViewModel** uses an MVI action pattern:
- Exposes `onAction(WorkoutDetailsUIAction)` which handles a sealed interface (`LoadWorkout`, `AddExercise`, `AddSet`, `SaveWorkout`)
- Internal state is a `MutableStateFlow<WorkoutDetailsUIState>` mutated via `_state.update { }`

UI state classes use `ImmutableList` from `kotlinx-collections-immutable` to minimize unnecessary recomposition.

### Navigation

Uses **Navigation3** (experimental, `androidx.navigation3:navigation3-ui:1.0.0`) — not the standard `androidx.navigation`.

Routes are defined as a `sealed interface Route : NavKey` with `@Serializable` data classes/objects in `feature/workout/.../navigation/NavigationDestinations.kt`.

Navigation is set up in `app/.../navigation/MainScreen.kt` using `NavDisplay` with `entryProvider` DSL and two decorators:
- `rememberSaveableStateHolderNavEntryDecorator()` — preserves UI state across recompositions
- `rememberViewModelStoreNavEntryDecorator()` — preserves ViewModels per back-stack entry

Back stack is managed with `rememberNavBackStack(Route.WorkoutList)`.

### Dependency Injection

Hilt is used throughout. Key modules:

| Module | Location | Provides |
|--------|----------|---------|
| `DispatcherModule` | `domain/di/` | `@DefaultDispatcher`, `@IoDispatcher`, `@MainDispatcher` qualifiers |
| `BindsModule` (domain) | `domain/di/` | `WorkoutDomainImpl → WorkoutDomain` |
| `BindsModule` (data) | `data/di/` | `WorkoutRepositoryImpl → WorkoutRepository` |
| `DataBaseModule` | `app/.../database/di/` | `AppDatabase`, `WorkoutDao` (Room setup) |

All modules install into `SingletonComponent`. The app class is `App : HiltAndroidApp`.

### Room Database Schema

Three entities with cascade-delete foreign keys:
- `WorkoutEntity` (workoutId PK, date)
  - → `ExerciseEntity` (exerciseId PK, workoutOwnerId FK, name)
    - → `SetEntity` (setId PK, exerciseOwnerId FK, count, weight, reps)

Relational queries use `@Transaction` with `WorkoutWithExercises` and `ExerciseWithSets` embedded-relation classes. DAO returns `Flow<>` for reactive queries and `suspend` functions for one-shot operations.

## Key Versions

- Kotlin: 2.2.10, AGP: 8.11.2
- Hilt: 2.57.1, Room: 2.8.4, KSP: 2.2.10-2.0.2
- Compose BOM: 2024.09.00, Navigation3: 1.0.0
- `kotlinx-coroutines`: 1.10.2, `kotlinx-collections-immutable`: 0.4.0

All versions are managed via the version catalog at `gradle/libs.versions.toml`.