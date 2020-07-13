package com.faffo.shifter.utils

enum class ShiftType(val hours: Int) {
    FULL(8),
    FULL_LONGWEEK(6),
    HALF(4),
    CUSTOM(-1)
}