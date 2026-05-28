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

- **`domain/`** — Pure Kotlin/JVM module. Contains interfaces (`WorkoutRepository`, `ExerciseTypeRepository`, `WorkoutDomain`), domain models (`Workout`, `Exercise`, `Set`, `ExerciseType`), and coroutine dispatcher qualifiers. Has no Android dependencies.
- **`data/`** — Room database implementation. Contains `WorkoutRepositoryImpl`, `ExerciseTypeRepositoryImpl`, Room entities, DAOs, and entity-to-domain mappers. Provides `@Binds` for both repository interfaces.
- **`core/`** — Shared Compose UI: Material3 theme, colors, typography.
- **`feature/workout/`** — All workout UI: list, details, and exercise-type-list screens, ViewModels, UI state classes, MVI action/side-effect sealed interfaces, and `NavigationDestinations`.
- **`app/`** — Entry point. Hosts `MainActivity`, `NavDisplay` (Navigation3), and the Room database Hilt module.

### State Management Pattern

**WorkoutListViewModel** uses a reactive Flow pattern:
- `WorkoutRepository.getWorkoutsFlow()` → mapped to `StateFlow<WorkoutListUIState>` via `stateIn(WhileSubscribed(5000L))`

**WorkoutDetailsViewModel** uses an MVI action pattern:
- Exposes `onAction(WorkoutDetailsUIAction)` which handles a sealed interface (`UpdateSet`, `AddSet`, `DeleteWorkout`)
- Internal state is a `MutableStateFlow<WorkoutDetailsUIState>` mutated via `_state.update { }`
- Flattens the hierarchical Exercise→Sets structure into a flat `ImmutableList<WorkoutFlatListItem>` for `LazyColumn` display (items: `ExerciseHeader`, `ExerciseSet`, `AddSetButton`)
- Set updates are **debounced 1 second** via a `_setsToUpdate` `MutableStateFlow` before being persisted to avoid excessive DB writes
- Side effects (e.g. `NavigateBack` after deletion) are sent via a `Channel<WorkoutDetailsSideEffect>`

**ExerciseListViewModel** uses an MVI action pattern:
- Manages `ExerciseType` selection for adding exercises to a workout
- Actions: `SelectExercise`, `AddNewExerciseType`, `SaveExerciseToWorkout`, `DeleteExerciseType`
- Combines `getExerciseTypeFlow()` with a `_selectedIds: MutableStateFlow<Set<Int>>` using `combine()` to produce UI state
- Side effects (`NavigateBack`, `ShowToast`) sent via a `Channel<ExerciseListSideEffect>`
- Deletion is guarded — shows a `ShowToast` if the exercise type is used in an active workout

UI state classes use `ImmutableList` from `kotlinx-collections-immutable` to minimize unnecessary recomposition.

### AssistedFactory Pattern

ViewModels that require runtime parameters (e.g. `workoutId`) use Hilt's `@AssistedInject` / `@HiltViewModel(assistedFactory = ...)`:

```kotlin
@HiltViewModel(assistedFactory = ExerciseListViewModel.Factory::class)
class ExerciseListViewModel @AssistedInject constructor(
    @Assisted val workoutId: Int,
    ...
) : ViewModel() {
    @AssistedFactory interface Factory { fun create(workoutId: Int): ExerciseListViewModel }
}
```

In Compose, the factory is invoked via:
```kotlin
hiltViewModel<VM, VM.Factory>(creationCallback = { factory -> factory.create(workoutId) })
```

Both `WorkoutDetailsViewModel` and `ExerciseListViewModel` use this pattern.

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
| `BindsModule` (data) | `data/di/` | `WorkoutRepositoryImpl → WorkoutRepository`, `ExerciseTypeRepositoryImpl → ExerciseTypeRepository` |
| `DataBaseModule` | `app/.../database/di/` | `AppDatabase`, `WorkoutDao`, `ExerciseTypesDao` (Room setup) |

All modules install into `SingletonComponent`. The app class is `App : HiltAndroidApp`.

### Room Database Schema

Four entities; database is at **version 5**:
- `WorkoutEntity` (workoutId PK, date)
  - → `ExerciseEntity` (exerciseId PK, workoutOwnerId FK, name)
    - → `SetEntity` (setId PK, exerciseOwnerId FK, weight, reps)
- `ExerciseTypeEntity` (exerciseTypeId PK, name UNIQUE) — standalone table, no FK to workouts

Relational queries use `@Transaction` with `WorkoutWithExercises` and `ExerciseWithSets` embedded-relation classes. DAOs return `Flow<>` for reactive queries and `suspend` functions for one-shot operations.

`ExerciseTypesDao` provides:
- `getExerciseTypeFlow()` — reactive list
- `getExerciseTypes()` — one-shot suspend
- `insertExerciseType()` / `deleteExerciseType()` — suspend mutations

## Key Versions

- Kotlin: 2.2.10, AGP: 8.11.2
- Hilt: 2.57.1, Room: 2.8.4, KSP: 2.2.10-2.0.2
- Compose BOM: 2024.09.00, Navigation3: 1.0.0
- `kotlinx-coroutines`: 1.10.2, `kotlinx-collections-immutable`: 0.4.0

All versions are managed via the version catalog at `gradle/libs.versions.toml`.