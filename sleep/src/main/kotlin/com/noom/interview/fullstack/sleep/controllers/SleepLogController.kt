package com.noom.interview.fullstack.sleep.controllers

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
}
