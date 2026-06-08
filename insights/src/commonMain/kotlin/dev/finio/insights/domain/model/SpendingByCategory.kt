package dev.finio.insights.domain.model

data class SpendingByCategory(
    val category: String,
    val total: Double,
    val percentage: Int
)