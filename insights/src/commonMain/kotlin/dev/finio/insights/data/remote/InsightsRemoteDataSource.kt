package dev.finio.insights.data.remote

import dev.finio.insights.data.dto.InsightsSummaryDto
import dev.finio.insights.data.dto.MonthlyEvolutionDto
import dev.finio.insights.data.dto.PeriodRequestDto
import dev.finio.insights.data.dto.SpendingByCategoryDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class InsightsRemoteDataSource(
    private val client: HttpClient,
    private val baseUrl: String
){
    suspend fun getSpendingByCategory(
        token: String,
        startDate: String,
        endDate: String
    ): List<SpendingByCategoryDto> =
        client.post("$baseUrl/insights/spending-by-category"){
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(PeriodRequestDto(startDate, endDate))
        }.body()

    suspend fun getMonthlyEvolution(
        token: String,
        months: Int = 6
    ): List<MonthlyEvolutionDto> =
        client.get("$baseUrl/monthly-evolution?months=$months"){
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()

    suspend fun getSummary(
        token: String,
        startDate: String,
        endDate: String
    ): InsightsSummaryDto =
        client.post("$baseUrl/insights/summary"){
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(PeriodRequestDto(startDate, endDate))
        }.body()
}