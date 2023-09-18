package com.irfan.storyapp.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

object DateFormatter {
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(inputDateString: String, targetTimeZone: String): String {
        val instant = Instant.parse(inputDateString)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm")
            .withZone(ZoneId.of(targetTimeZone))
        return formatter.format(instant)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDateFromISOInstantToSimpleFormat(inputDateString: String, pattern: String): String {
        val isoInstantFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val patternFormat = SimpleDateFormat(pattern)

        return try {
            val date = isoInstantFormat.parse(inputDateString)
            val formattedDate = patternFormat.format(date as Date)
            formattedDate
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}