# finio-insights

KMP insights module for the Finio platform. Encapsulates all financial analysis logic — spending by category, monthly evolution, and period summary — published to Maven for consumption by `finio-app`.

## Stack

- **Language**: Kotlin Multiplatform
- **HTTP**: Ktor Client 3.1.3
- **Serialization**: kotlinx.serialization 1.8.1
- **Coroutines**: kotlinx.coroutines 1.10.2
- **DI**: Koin 4.0.0
- **Publication**: GitHub Packages (Maven)
- **CI/CD**: Bitrise

## Targets

| Target | Status |
|--------|--------|
| Android | ✅ |
| iOS Arm64 | ✅ |
| iOS Simulator Arm64 | ✅ |

## Module structure

```
insights/src/
  commonMain/
    kotlin/dev/finio/insights/
      data/
        dto/
          InsightsDtos.kt                  ← API request and response DTOs
        mapper/
          InsightsMapper.kt                ← DTO → domain model mappers
        remote/
          InsightsRemoteDataSource.kt      ← Ktor API calls
        repository/
          InsightsRepositoryImpl.kt        ← Repository implementation
      di/
        InsightsModule.kt                  ← Koin module definition
      domain/
        model/
          SpendingByCategory.kt            ← category, total, percentage
          MonthlyEvolution.kt              ← year, month, income, expenses, balance
          InsightsSummary.kt               ← totalIncome, totalExpenses, balance, topCategory
        repository/
          InsightsRepository.kt            ← Repository interface
      presentation/
        InsightsViewModel.kt               ← ViewModel with InsightsUiState
```

## API endpoints

All endpoints are served by `finio-api` deployed on Railway.

| Method | Route | Description | Auth |
|--------|-------|-------------|------|
| POST | `/insights/spending-by-category` | Spending by category in period | ✓ |
| GET | `/insights/monthly-evolution` | Monthly evolution (query param: `months`) | ✓ |
| POST | `/insights/summary` | Financial summary for period | ✓ |

> POST is used for endpoints that require `startDate`/`endDate` in the request body.

## ViewModel state

```kotlin
data class InsightsUiState(
    val spendingByCategory: List<SpendingByCategory> = emptyList(),
    val monthlyEvolution: List<MonthlyEvolution> = emptyList(),
    val summary: InsightsSummary? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
```

## DI usage

Initialize from your app shell (not from this module):

```kotlin
// Android — inside Application.onCreate()
initKoin {
    modules(insightsModule(
        baseUrl = "https://your-api.railway.app",
        tokenProvider = { tokenStorage.getToken() }
    ))
}
```

Then resolve the ViewModel via Koin:

```kotlin
val viewModel: InsightsViewModel = get()
viewModel.loadAll(
    startDate = "2024-06-01T00:00:00Z",
    endDate = "2024-06-30T23:59:59Z",
    months = 6
)
```

## Maven artifacts

Published to GitHub Packages under `dev.finio` group:

| Artifact | Description |
|----------|-------------|
| `insights-android` | Android AAR |
| `insights-iosarm64` | iOS Arm64 klib |
| `insights-iossimulatorarm64` | iOS Simulator Arm64 klib |
| `insights-kmp` | KMP metadata |

## CI/CD

| Trigger | Workflow | Action |
|---------|----------|--------|
| Push to `main` | `ci` | Compiles Android AAR + iOS Arm64 |
| Any tag (e.g. `0.1.0`) | `release` | Publishes all artifacts to GitHub Packages |

## Build

```bash
# Compile all targets
./gradlew :insights:assemble

# Publish to local Maven (~/.m2)
./gradlew :insights:publishToMavenLocal

# Publish to GitHub Packages (requires GITHUB_ACTOR and GITHUB_TOKEN)
./gradlew :insights:publish
```

## Key versions

```toml
kotlin = "2.3.21"
agp = "9.0.1"
ktor = "3.1.3"
koin = "4.0.0"
kotlinx-coroutines = "1.10.2"
kotlinx-serialization = "1.8.1"
```