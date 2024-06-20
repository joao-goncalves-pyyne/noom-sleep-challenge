package com.noom.interview.fullstack.sleep.controllers

import com.noom.interview.fullstack.sleep.dtos.AverageSleepStats
import com.noom.interview.fullstack.sleep.entities.SleepLog
import com.noom.interview.fullstack.sleep.services.SleepLogService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sleep")
class SleepLogController(private val sleepLogService: SleepLogService) {

    @PostMapping
    fun logSleep(@RequestBody sleepLog: SleepLog): ResponseEntity<SleepLog> {
        return ResponseEntity.ok(sleepLogService.logSleep(sleepLog))
    }

    @GetMapping("/last-night")
    fun getLastNightSleep(@RequestParam userId: Long): ResponseEntity<SleepLog?> {
        return ResponseEntity.ok(sleepLogService.getLastNightSleep(userId))
    }

    @GetMapping("/30-day-averages")
    fun get30DayAverages(@RequestParam userId: Long): ResponseEntity<AverageSleepStats> {
        return ResponseEntity.ok(sleepLogService.get30DayAverages(userId))
    }
}
