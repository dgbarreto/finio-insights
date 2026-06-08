package dev.finio.insights.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SpendingByCategoryDto(
    val category: String,
    val total: Double,
    val percentage: Int
)

@Serializable
data class MonthlyEvolutionDto(
    val year: Int,
    val month: Int,
    val income: Double,
    val expenses: Double,
    val balance: Double
)

@Serializable
data class InsightsSummaryDto(
    val totalIncome: Double,
    val totalExpenses: Double,
    val balance: Double,
    val topCategory: String? = null
)

@Serializable
data class PeriodRequestDto(
    val startDate: String,
    val endDate: String
)