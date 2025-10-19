package com.samuelsumbane.ssptdesktop.repositories

import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.cacheControl
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import java.net.InetSocketAddress
import java.net.Socket
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random

fun getCurrentTimestamp(): Long {
    return System.currentTimeMillis()
}

val fifteenDaysInMillis = 15 * 24 * 60 * 60 * 1000L


fun longTimeToString(longTime: Long, timeZoneId: String = "Africa/Harare"): String {
    val date = Date(longTime)
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone(timeZoneId)
    return formatter.format(date)
}


fun dateLongTimeToString(longTime: Long, timeZoneId: String = "Africa/Harare"): String {
    val date = Date(longTime)
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone(timeZoneId)
    return formatter.format(date)
}

fun longTimeToMonth(longTime: Long, timeZoneId: String): String {
    val date = Date(longTime)
    val formatter = SimpleDateFormat("MM yyyy", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone(timeZoneId)
    return formatter.format(date)
}

fun generateSixDigitsCode(): String = Random.nextInt(100000, 900000).toString()

fun generateTimestamp(date: String, time: String, timeZoneId: String = "Africa/Harare"): Long {
    val dateTimeString = "$date $time"
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val localDateTime = LocalDateTime.parse(dateTimeString, formatter)
    val zonedDateTime = localDateTime.atZone(ZoneId.of(timeZoneId))
    return zonedDateTime.toEpochSecond() * 1000
}

fun stringDateToLong(date: String, timeZoneId: String = "Africa/Harare"): Long {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val localDate = LocalDate.parse(date, formatter)
    val zonedDateTime = localDate.atStartOfDay(ZoneId.of(timeZoneId))
    return zonedDateTime.toEpochSecond() * 1000
}


fun formatToDateTime(dateTime: Long, timeZoneId: String): String {
    val date = Date(dateTime)
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone(timeZoneId)
    return formatter.format(date)
}

suspend fun hasInternet(): Boolean {
    return try {
        withTimeout(3000) {
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500) // Google DNS
            socket.close()
            true
        }
    } catch (e: Exception) {
        false
    }
}

suspend inline fun <reified T> ApplicationCall.respondWithCache(
    data: T,
    cacheSeconds: Int = 60,
    status: HttpStatusCode = HttpStatusCode.OK,
) {
    val jsonText = Json.encodeToString(data)
    val etag = hashString("SHA-256", jsonText)
    val clientETag = request.headers["If-None-Match"]

    if (clientETag == "\"$etag\"") {
        respond(HttpStatusCode.NotModified)
    } else {
        response.cacheControl(CacheControl.MaxAge(maxAgeSeconds = cacheSeconds))
        response.headers.append(HttpHeaders.ETag,  "\"$etag\"")
        respondText(jsonText, ContentType.Application.Json, status)
    }
}


fun hashString(algorithm: String, input: String): String {
    val bytes = MessageDigest.getInstance(algorithm).digest(input.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}

