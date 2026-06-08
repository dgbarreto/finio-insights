package dev.finio.insights

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform