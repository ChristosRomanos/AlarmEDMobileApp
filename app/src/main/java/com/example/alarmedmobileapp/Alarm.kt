package com.example.alarmedmobileapp

data class Alarm(
    var hour: Int,
    var min : Int,// Time of the alarm (e.g., "6:00 AM")
    var isEnabled: Boolean, // Whether the alarm is enabled or disabled
    var repeat: List<Days> // List of days the alarm repeats (e.g., ["Monday", "Wednesday"])
)
