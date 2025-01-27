package com.example.alarmedmobileapp.Data

enum class Days(i: Int) {
    Monday(0),
    Tuesday(1),
    Wednesday(2),
    Thursday(3),
    Friday(4),
    Saturday(5),
    Sunday(6);

    companion object {
        // Function to get the enum from an integer value
        fun fromInt(value: Int): Days? {
            return entries.find { it.ordinal == value }
        }
    }
}