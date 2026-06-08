package dev.finio.insights.di

import dev.finio.insights.data.remote.InsightsRemoteDataSource
import dev.finio.insights.data.repository.InsightsRepositoryImpl
import dev.finio.insights.domain.repository.InsightsRepository
import dev.finio.insights.presentation.InsightsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

fun insightsModule(
    baseUrl: String,
    tokenProvider: () -> String?
): Module = module{
    single{
        HttpClient{
            install(ContentNegotiation){
                json(Json { ignoreUnknownKeys = true })
            }
            install(Logging){
                level = LogLevel.BODY
                logger = object : Logger{
                    override fun log(message: String) {
                        println("[Finio Insights] $message")
                    }
                }
            }
        }
    }

    single {
        InsightsRemoteDataSource(client = get(), baseUrl = baseUrl)
    }

    single<InsightsRepository>{
        InsightsRepositoryImpl(
            remoteDataSource = get(),
            tokenProvider = tokenProvider
        )
    }

    factory {
        InsightsViewModel(repository = get())
    }
}