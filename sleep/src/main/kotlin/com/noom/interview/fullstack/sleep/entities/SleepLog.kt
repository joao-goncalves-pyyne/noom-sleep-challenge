package com.noom.interview.fullstack.sleep.entities

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.noom.interview.fullstack.sleep.enums.MorningFeeling
import java.time.LocalDate
import java.time.LocalTime
import javax.persistence.*

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(columnNames = ["userId", "date"])]
)
data class SleepLog(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val userId: Long = 0,
    val date: LocalDate = LocalDate.now(),
    val timeInBedStart: LocalTime = LocalTime.MIDNIGHT,
    val timeInBedEnd: LocalTime = LocalTime.MIDNIGHT,
    val totalTimeInBed: Long = 0L,
    @Enumerated(EnumType.STRING)
    val morningFeeling: MorningFeeling = MorningFeeling.GOOD
) {
    // No-arg constructor for JPA
    constructor() : this(0, 0, LocalDate.now(), LocalTime.MIDNIGHT, LocalTime.MIDNIGHT, 0L, MorningFeeling.GOOD)
}