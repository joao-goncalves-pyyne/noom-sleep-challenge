package com.noom.interview.fullstack.sleep.services

import com.noom.interview.fullstack.sleep.entities.SleepLog
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
            morningFeeling = "GOOD"
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
            morningFeeling = "GOOD"
        )

        `when`(sleepLogRepository.save(any(SleepLog::class.java))).thenThrow(DataIntegrityViolationException::class.java)

        val exception = assertThrows<IllegalArgumentException> {
            sleepLogService.logSleep(sleepLog)
        }

        assertEquals("A sleep log for the user on this date already exists.", exception.message)
        verify(sleepLogRepository, times(1)).save(any(SleepLog::class.java))
    }
}
