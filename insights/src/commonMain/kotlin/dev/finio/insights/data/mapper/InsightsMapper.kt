package dev.finio.insights.data.mapper

import dev.finio.insights.data.dto.InsightsSummaryDto
import dev.finio.insights.data.dto.MonthlyEvolutionDto
import dev.finio.insights.data.dto.SpendingByCategoryDto
import dev.finio.insights.domain.model.InsightsSummary
import dev.finio.insights.domain.model.MonthlyEvolution
import dev.finio.insights.domain.model.SpendingByCategory
import kotlin.math.round


fun SpendingByCategoryDto.toDomain() = SpendingByCategory(
    category = category,
    total = total,
    percentage = round(percentage).toInt()
)

fun MonthlyEvolutionDto.toDomain() = MonthlyEvolution(
    year = year,
    month = month,
    income = incomes,
    expenses = expenses,
    balance = balance
)

fun InsightsSummaryDto.toDomain() = InsightsSummary(
    totalIncome = totalIncomes,
    totalExpenses = totalExpenses,
    balance = balance,
    topCategory = topCategory
)