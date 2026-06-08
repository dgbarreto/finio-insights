package dev.finio.insights.domain.model

data class MonthlyEvolution(
    val year: Int,
    val month: Int,
    val income: Double,
    val expenses: Double,
    val balance: Double
)