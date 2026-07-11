O README atual está bem estruturado. Precisa atualizar o Koin setup, os domain models completos e o loadAll correto. Aqui está o README atualizado:
markdown# finio-insights

Kotlin Multiplatform insights module for the Finio platform. Encapsulates all financial analytics logic — spending by category, monthly evolution, and period summary — published to GitHub Packages (Maven) for consumption by `finio-app`.

## Stack

- **Language**: Kotlin Multiplatform
- **HTTP**: Ktor Client
- **Serialization**: kotlinx.serialization
- **Coroutines**: kotlinx.coroutines
- **DI**: Koin
- **CI/CD**: Bitrise
- **Publication**: GitHub Packages (Maven)

## Targets

| Target | Status |
|--------|--------|
| Android | ✅ |
| iOS Arm64 | ✅ |
| iOS Simulator Arm64 | ✅ |

## Module structure
insights/src/
commonMain/kotlin/dev/finio/insights/
data/
dto/                              ← API request/response DTOs
mapper/                           ← DTO → domain model mappers
remote/
InsightsRemoteDataSource.kt     ← Ktor API calls
repository/
InsightsRepositoryImpl.kt       ← repository implementation
di/
InsightsModule.kt                 ← Koin module (insightsModule(baseUrl))
domain/
model/
SpendingByCategory.kt           ← category, total, percentage
MonthlyEvolution.kt             ← year, month, income, expenses, balance
InsightsSummary.kt              ← totalIncome, totalExpenses, balance, topCategory
repository/
InsightsRepository.kt           ← interface
presentation/
InsightsViewModel.kt              ← StateFlow<InsightsUiState>
InsightsUiState.kt

## Domain models

```kotlin
data class SpendingByCategory(
    val category: String,
    val total: Double,
    val percentage: Int
)

data class MonthlyEvolution(
    val year: Int,
    val month: Int,
    val income: Double,
    val expenses: Double,
    val balance: Double
)

data class InsightsSummary(
    val totalIncome: Double,
    val totalExpenses: Double,
    val balance: Double,
    val topCategory: String?
)

data class InsightsUiState(
    val spendingByCategory: List<SpendingByCategory> = emptyList(),
    val monthlyEvolution: List<MonthlyEvolution> = emptyList(),
    val summary: InsightsSummary? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
```

## API endpoints

All endpoints served by `finio-api` deployed on Railway.

| Method | Route | Description | Auth |
|--------|-------|-------------|------|
| POST | `/insights/spending-by-category` | Spending by category in period | ✓ |
| GET | `/insights/monthly-evolution` | Monthly evolution (`months` query param) | ✓ |
| POST | `/insights/summary` | Financial summary for period | ✓ |

> `POST` is used for endpoints that require `startDate`/`endDate` in the request body.

## ViewModel

```kotlin
class InsightsViewModel(repository: InsightsRepository) {
    val state: StateFlow<InsightsUiState>

    fun loadAll(startDate: String, endDate: String, months: Int = 6)
    // Fetches spendingByCategory, monthlyEvolution and summary in parallel
    // startDate/endDate format: ISO-8601 UTC (e.g. "2026-07-01T00:00:00.000Z")
}
```

## Koin setup

```kotlin
startKoin {
    modules(
        insightsModule(baseUrl = "https://finio-api-production.up.railway.app")
    )
}
```

Resolve and use:

```kotlin
val viewModel: InsightsViewModel = get()
viewModel.loadAll(
    startDate = "2026-07-01T00:00:00.000Z",
    endDate = "2026-07-31T00:00:00.000Z",
    months = 6
)
```

## Published artifacts

| Artifact | Description |
|----------|-------------|
| `dev.finio:insights-android` | Android AAR |
| `dev.finio:insights-iosarm64` | iOS Arm64 framework |
| `dev.finio:insights-iossimulatorarm64` | iOS Simulator framework |
| `dev.finio:insights-kmp` | KMP metadata |

## Publishing

```bash
git tag 1.0.0
git push origin 1.0.0
```

Local publish:
```properties
# local.properties
version=1.0.0
github.actor=your_username
github.token=your_token
```
