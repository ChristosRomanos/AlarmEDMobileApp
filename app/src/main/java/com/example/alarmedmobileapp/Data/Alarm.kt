package com.example.alarmedmobileapp.Data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

data class Alarm(
    var hour: Int,
    var min : Int,// Time of the alarm (e.g., "6:00 AM")
    var isEnabled: Boolean, // Whether the alarm is enabled or disabled
    var repeat: MutableList<Days> // List of days the alarm repeats (e.g., ["Monday", "Wednesday"])
){
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTime():Int{
        val now = LocalDate.now()
        var targetDay =DayOfWeek.MONDAY
        var minTimeDifference=Int.MAX_VALUE
        for (i in listOf(0,1,2,3,4,5,6)){
            for(day in repeat){
                if(day.ordinal==now.plusDays(i.toLong()).dayOfWeek.ordinal){
                    if (i==0){
                        if (LocalTime.now().isBefore(LocalTime.of(hour,min))){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                                repeat.remove(day)
                                repeat.addFirst(day)
                                return ChronoUnit.MINUTES.between(LocalDate.now(),
                                    LocalDate.now().plusDays(i.toLong()).atTime(hour,min)).toInt()
                            }
                        }
                    }else{
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                            repeat.remove(day)
                            repeat.addFirst(day)
                            return ChronoUnit.MINUTES.between(LocalDateTime.now(),
                                LocalDate.now().plusDays(i.toLong()).atTime(hour,min)).toInt()
                        }
                    }
                }
            }
            if (repeat.isEmpty()){
                return ChronoUnit.MINUTES.between(LocalDateTime.now(),
                    LocalDate.now().plusDays(1).atTime(hour,min)).toInt()
            }
        }
        return minTimeDifference
    }
}
