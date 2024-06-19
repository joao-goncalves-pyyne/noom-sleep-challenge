package com.noom.interview.fullstack.sleep.dtos

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.noom.interview.fullstack.sleep.utils.CustomDurationSerializer
import java.time.LocalDate
import java.time.LocalTime

data class AverageSleepStats(
    val startDate: LocalDate,
    val endDate: LocalDate,
    @JsonSerialize(using = CustomDurationSerializer::class)
    val averageTotalTimeInBed: Long, // in seconds
    val averageTimeInBedStart: LocalTime,
    val averageTimeInBedEnd: LocalTime,
    val morningFeelingFrequencies: Map<String, Int>
)
