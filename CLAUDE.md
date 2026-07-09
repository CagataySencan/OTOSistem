# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

All commands run from the project root via Gradle wrapper:

```bash
# Assemble debug APK
./gradlew assembleDebug

# Install on connected device/emulator
./gradlew installDebug

# Run all unit tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.cagataysencan.template.ExampleUnitTest"

# Run instrumented tests (requires connected device)
./gradlew connectedAndroidTest

# Lint check
./gradlew lint
```

On Windows use `gradlew.bat` instead of `./gradlew`.

## Architecture Overview

This is a **single-Activity, multi-Fragment Android app** following Clean Architecture with MVVM.

### Layer boundaries

```
feature/          → UI (Fragment + ViewModel)
domain/           → Use cases + repository interfaces + domain models
data/             → Repository implementations, remote/local data sources, mappers
core/             → Base classes, managers, network utilities, UI helpers
di/               → Hilt modules and entry points
```

### Data flow

`Fragment` observes `StateFlow<ResultState<T>>` from `ViewModel` → ViewModel calls use case → use case invokes `Repository` interface → `RepositoryImpl` coordinates `RemoteDataSource` (Retrofit + `safeApiCall`) and/or `LocalDataSource` (Room DAO) → mappers convert DTOs/entities to domain models → results flow back as `Flow<T>`.

`ResultState` is the UI state envelope: `Idle | Loading | Success(data) | Error(message, throwable)`. Always emit through the `MutableStateFlow.launchFlow()` extension on `BaseViewModel` — it handles IO dispatch, `onStart` loading, and `catch` → error mapping automatically.

### Navigation

Single `MainActivity` hosts a Navigation Component `NavHostFragment`. Splash → Auth → Home is the primary nav graph. Navigation events from ViewModels use `Channel<SealedEvent>.receiveAsFlow()` collected in the fragment (one-shot events, not state).

### Dependency injection

Hilt, single `SingletonComponent`. All bindings live in `AppModule`. Room is lazy via `DatabaseProvider` (not initialized at app start). Coroutine dispatchers are injected as `DispatchersProvider` — never hardcode `Dispatchers.IO` in classes; inject the provider instead.

### Key infrastructure

| Class | Role |
|---|---|
| `BaseViewModel.launchFlow()` | Collects a use case `Flow` on IO, drives `ResultState` |
| `BaseBindingFragment<VB>` | ViewBinding + `collectOnStarted()` for safe Flow collection |
| `safeApiCall { }` | Wraps Retrofit calls into `NetworkResponse<T>`, handles HTTP/IO/unknown exceptions |
| `FeatureFlagManager` | Build-time defaults from `BuildConfig`, overridable at runtime via DataStore, cached in memory |
| `SessionManager` | Login state stored in DataStore |
| `ThemeManager` / `LocalizationManager` | Theme and locale preferences, also DataStore-backed |
| `NetworkMonitor` | Connectivity check; call `isCurrentlyOnline()` before network requests |

### Feature flags

Flags are defined in `FeatureFlag` enum. Build-time defaults live in `app/build.gradle.kts` as `buildConfigField`. Runtime overrides persist to DataStore. Check flags in ViewModels via `featureFlagManager.isEnabled(FeatureFlag.X)`.

### Adding a new feature

1. Create `domain/model/Foo.kt`, `domain/repository/FooRepository.kt`, `domain/usecase/GetFooUseCase.kt`.
2. Add `data/remote/dto/FooDto.kt`, `data/remote/source/FooRemoteDataSource.kt`, `data/mapper/FooMapper.kt`, `data/repository/FooRepositoryImpl.kt`.
3. Bind `FooRepository → FooRepositoryImpl` in `AppModule`.
4. Create `feature/foo/FooFragment.kt` (extend `BaseBindingFragment`) and `FooFragment.kt` (extend `BaseViewModel`), annotate ViewModel with `@HiltViewModel`.
5. Add the destination to the nav graph XML.

### Room schema

Schema exports are in `app/schemas/`. When changing `AppDatabase`, increment the version and provide a migration — do not use `fallbackToDestructiveMigration` unless the schema is intentionally reset.

### Network

Base URL is a `BuildConfig` field (`BASE_URL`). OkHttp is configured in `OkHttpClientProvider`; Chucker is wired in debug builds only. Add new endpoints to `ApiService`.
