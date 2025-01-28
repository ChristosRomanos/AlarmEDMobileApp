package com.example.alarmedmobileapp.Data

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
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
fun loadAlarmLists(context: Context): MutableList<AlarmList> {
    val sourceFile = File(context.filesDir, "alarms.json")
    if(sourceFile.exists()){
        println("File exists")
    }else {
        val assetManager = context.assets
        val inputStream = assetManager.open("alarms.json")
        val json = inputStream.bufferedReader().use { it.readText() }
        sourceFile.writeText(json)
    }
    val jsonContent = sourceFile.readText()
    // Use Gson to parse the JSON into a list of SoundFile
    val gson = Gson()
    val type = object : TypeToken<MutableList<AlarmList>>() {}.type
    return gson.fromJson(jsonContent, type) // Returns List<SoundFile>
}fun overwriteAlarmsJsonFile(context:Context,alarms: MutableList<AlarmList>) {
    val gson = Gson()
    // Convert the list to a JSON string
    val json = gson.toJson(alarms)

    // Write the JSON string to the file
    val file = File(context.filesDir,"alarms.json")
    file.writeText(json)
    println("Wrote data: "+json)
}
