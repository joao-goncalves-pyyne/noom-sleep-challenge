package com.noom.interview.fullstack.sleep.services

import com.noom.interview.fullstack.sleep.entities.SleepLog
import com.noom.interview.fullstack.sleep.repositories.SleepLogRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class SleepLogService(private val repository: SleepLogRepository) {

    fun logSleep(sleepLog: SleepLog): SleepLog {
        val duration = calculateDuration(sleepLog.date, sleepLog.timeInBedStart, sleepLog.timeInBedEnd)
        val totalTimeInBedInSeconds = duration.seconds
        val adjustedSleepLog = sleepLog.copy(totalTimeInBed = totalTimeInBedInSeconds)

        return try {
            repository.save(adjustedSleepLog)
        } catch (ex: DataIntegrityViolationException) {
            throw IllegalArgumentException("A sleep log for the user on this date already exists.")
        }
    }

    fun getLastNightSleep(userId: Long): SleepLog? =
        repository.findByUserIdAndDate(userId, LocalDate.now().minusDays(1))

    private fun calculateDuration(date: LocalDate, start: LocalTime, end: LocalTime): Duration {
        val startDateTime = LocalDateTime.of(date, start)
        val endDateTime = if (end.isAfter(start) || end == start) {
            LocalDateTime.of(date, end)
        } else {
            LocalDateTime.of(date.plusDays(1), end)
        }
        return Duration.between(startDateTime, endDateTime)
    }
}