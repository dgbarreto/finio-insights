package dev.finio.insights.domain.repository

import dev.finio.insights.domain.model.InsightsSummary
import dev.finio.insights.domain.model.MonthlyEvolution
import dev.finio.insights.domain.model.SpendingByCategory

interface InsightsRepository{
    suspend fun getSpendingByCategory(startDate: String, endDate: String): List<SpendingByCategory>
    suspend fun getMonthlyEvolution(months: Int = 6): List<MonthlyEvolution>
    suspend fun getSummary(startDate: String, endDate: String): InsightsSummary
}