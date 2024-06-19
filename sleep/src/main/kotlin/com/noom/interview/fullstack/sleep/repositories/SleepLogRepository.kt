package com.noom.interview.fullstack.sleep.repositories

import com.noom.interview.fullstack.sleep.entities.SleepLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface SleepLogRepository : JpaRepository<SleepLog, Long> {
    fun findByUserIdAndDate(userId: Long, date: LocalDate): SleepLog?
}
