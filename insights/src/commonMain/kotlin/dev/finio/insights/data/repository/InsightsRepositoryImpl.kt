package dev.finio.insights.data.repository

import dev.finio.insights.data.mapper.toDomain
import dev.finio.insights.data.remote.InsightsRemoteDataSource
import dev.finio.insights.domain.model.InsightsSummary
import dev.finio.insights.domain.model.MonthlyEvolution
import dev.finio.insights.domain.model.SpendingByCategory
import dev.finio.insights.domain.repository.InsightsRepository

class InsightsRepositoryImpl(
    private val remoteDataSource: InsightsRemoteDataSource,
    private val tokenProvider: () -> String?
): InsightsRepository{
    override suspend fun getSpendingByCategory(
        startDate: String,
        endDate: String
    ): List<SpendingByCategory> {
        val token = tokenProvider() ?: error("Not authenticated")
        return remoteDataSource.getSpendingByCategory(token, startDate, endDate)
            .map { it.toDomain() }
    }

    override suspend fun getMonthlyEvolution(months: Int): List<MonthlyEvolution> {
        val token = tokenProvider() ?: error("Not authenticated")
        return remoteDataSource.getMonthlyEvolution(token, months)
            .map { it.toDomain() }
    }

    override suspend fun getSummary(
        startDate: String,
        endDate: String
    ): InsightsSummary {
        val token = tokenProvider() ?: error("Not authenticated")
        return remoteDataSource.getSummary(token, startDate, endDate).toDomain()
    }
}