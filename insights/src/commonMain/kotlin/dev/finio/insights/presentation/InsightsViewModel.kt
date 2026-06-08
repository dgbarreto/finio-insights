package dev.finio.insights.presentation

import dev.finio.insights.domain.model.InsightsSummary
import dev.finio.insights.domain.model.MonthlyEvolution
import dev.finio.insights.domain.model.SpendingByCategory
import dev.finio.insights.domain.repository.InsightsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class InsightsUiState(
    val spendingByCategory: List<SpendingByCategory> = emptyList(),
    val monthlyEvolution: List<MonthlyEvolution> = emptyList(),
    val summary: InsightsSummary? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class InsightsViewModel(
    private val repository: InsightsRepository
){
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow(InsightsUiState())
    val state: StateFlow<InsightsUiState> = _state.asStateFlow()

    fun loadAll(startDate: String, endDate: String, months: Int = 6){
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                val spending = repository.getSpendingByCategory(startDate, endDate)
                val evolution = repository.getMonthlyEvolution(months)
                val summary = repository.getSummary(startDate, endDate)

                _state.value = InsightsUiState(
                    spendingByCategory = spending,
                    monthlyEvolution = evolution,
                    summary = summary,
                    isLoading = false
                )
            } catch (e: Exception){
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load insights"
                )
            }
        }
    }
}