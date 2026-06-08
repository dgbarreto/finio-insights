package dev.finio.insights.domain.model

data class InsightsSummary(
    val totalIncome: Double,
    val totalExpenses: Double,
    val balance: Double,
    val topCategory: String?
)