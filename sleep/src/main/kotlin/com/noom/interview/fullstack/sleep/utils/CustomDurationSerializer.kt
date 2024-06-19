package com.noom.interview.fullstack.sleep.utils

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.io.IOException
import java.time.Duration

class CustomDurationSerializer : JsonSerializer<Long>() {
    @Throws(IOException::class)
    override fun serialize(value: Long, gen: JsonGenerator, serializers: SerializerProvider) {
        val duration = Duration.ofSeconds(value)
        val hours = duration.toHours()
        val minutes = (duration.toMinutes() % 60)
        val seconds = (duration.seconds % 60)
        val formattedDuration = StringBuilder()

        if (hours > 0) {
            formattedDuration.append("$hours hours")
        }
        if (minutes > 0) {
            if (formattedDuration.isNotEmpty()) {
                formattedDuration.append(", ")
            }
            formattedDuration.append("$minutes minutes")
        }
        if (seconds > 0 || formattedDuration.isEmpty()) {
            if (formattedDuration.isNotEmpty()) {
                formattedDuration.append(", ")
            }
            formattedDuration.append("$seconds seconds")
        }

        gen.writeString(formattedDuration.toString())
    }
}
