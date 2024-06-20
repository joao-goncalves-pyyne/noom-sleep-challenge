package com.noom.interview.fullstack.sleep.services

import com.noom.interview.fullstack.sleep.entities.SleepLog
import com.noom.interview.fullstack.sleep.enums.MorningFeeling
import com.noom.interview.fullstack.sleep.repositories.SleepLogRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import java.time.LocalDate
import java.time.LocalTime

@SpringBootTest
class SleepLogServiceTest {

    private lateinit var sleepLogRepository: SleepLogRepository
    private lateinit var sleepLogService: SleepLogService

    @BeforeEach
    fun setUp() {
        sleepLogRepository = mock(SleepLogRepository::class.java)
        sleepLogService = SleepLogService(sleepLogRepository)
    }

    @Test
    fun `logSleep should save and return sleep log`() {
        val sleepLog = SleepLog(
            userId = 1,
            date = LocalDate.of(2023, 6, 18),
            timeInBedStart = LocalTime.of(22, 30),
            timeInBedEnd = LocalTime.of(6, 30),
            morningFeeling = MorningFeeling.GOOD
        )

        val savedSleepLog = sleepLog.copy(totalTimeInBed = 28800L) // 8 hours in seconds

        `when`(sleepLogRepository.save(any(SleepLog::class.java))).thenReturn(savedSleepLog)

        val result = sleepLogService.logSleep(sleepLog)
        assertNotNull(result)
        assertEquals(savedSleepLog, result)
        verify(sleepLogRepository, times(1)).save(any(SleepLog::class.java))
    }

    @Test
    fun `logSleep should throw exception if sleep log for same user and date exists`() {
        val sleepLog = SleepLog(
            userId = 1,
            date = LocalDate.of(2023, 6, 18),
            timeInBedStart = LocalTime.of(22, 30),
            timeInBedEnd = LocalTime.of(6, 30),
            morningFeeling = MorningFeeling.GOOD
        )

        `when`(sleepLogRepository.save(any(SleepLog::class.java))).thenThrow(DataIntegrityViolationException::class.java)

        val exception = assertThrows<IllegalArgumentException> {
            sleepLogService.logSleep(sleepLog)
        }

        assertEquals("A sleep log for the user on this date already exists.", exception.message)
        verify(sleepLogRepository, times(1)).save(any(SleepLog::class.java))
    }

    @Test
    fun `getLastNightSleep should return sleep log for last night`() {
        val userId: Long = 1
        val sleepLog = SleepLog(
            userId = userId,
            date = LocalDate.now().minusDays(1),
            timeInBedStart = LocalTime.of(22, 30),
            timeInBedEnd = LocalTime.of(6, 30),
            totalTimeInBed = 28800L, // 8 hours in seconds
            morningFeeling = MorningFeeling.GOOD
        )

        `when`(sleepLogRepository.findByUserIdAndDate(userId, LocalDate.now().minusDays(1))).thenReturn(sleepLog)

        val result = sleepLogService.getLastNightSleep(userId)
        assertNotNull(result)
        assertEquals(sleepLog, result)
        verify(sleepLogRepository, times(1)).findByUserIdAndDate(userId, LocalDate.now().minusDays(1))
    }

    @Test
    fun `getLastNightSleep should return null if no sleep log found for last night`() {
        val userId: Long = 1

        `when`(sleepLogRepository.findByUserIdAndDate(userId, LocalDate.now().minusDays(1))).thenReturn(null)

        val result = sleepLogService.getLastNightSleep(userId)
        assertNull(result)
        verify(sleepLogRepository, times(1)).findByUserIdAndDate(userId, LocalDate.now().minusDays(1))
    }


    @Test
    fun `get30DayAverages should return average sleep stats for last 30 days`() {
        val userId: Long = 1
        val sleepLogs = listOf(
            SleepLog(
                userId = userId,
                date = LocalDate.now().minusDays(1),
                timeInBedStart = LocalTime.of(22, 30),
                timeInBedEnd = LocalTime.of(3, 30),
                totalTimeInBed = 18000L, // 5 hours in seconds
                morningFeeling = MorningFeeling.BAD
            ),
            SleepLog(
                userId = userId,
                date = LocalDate.now().minusDays(2),
                timeInBedStart = LocalTime.of(23, 0),
                timeInBedEnd = LocalTime.of(7, 0),
                totalTimeInBed = 28800L, // 8 hours in seconds
                morningFeeling = MorningFeeling.OK
            )
        )

        `when`(sleepLogRepository.findLast30DaysLogs(userId, LocalDate.now().minusDays(30))).thenReturn(sleepLogs)

        val result = sleepLogService.get30DayAverages(userId)
        assertNotNull(result)
        assertEquals(LocalDate.now().minusDays(30), result.startDate)
        assertEquals(LocalDate.now(), result.endDate)
        assertEquals(23400L, result.averageTotalTimeInBed) // Average of 18000 and 28800
        assertEquals(LocalTime.of(22, 45), result.averageTimeInBedStart) // Average time
        assertEquals(LocalTime.of(5, 15), result.averageTimeInBedEnd) // Average time
        assertEquals(mapOf("BAD" to 1, "OK" to 1), result.morningFeelingFrequencies)
        verify(sleepLogRepository, times(1)).findLast30DaysLogs(userId, LocalDate.now().minusDays(30))
    }
}