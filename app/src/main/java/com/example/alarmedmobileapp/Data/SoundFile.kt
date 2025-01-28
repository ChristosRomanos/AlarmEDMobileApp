package com.example.alarmedmobileapp.Data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

data class SoundFile(
    val name: String,
    val file: String,
    var chosen: Boolean
)

fun loadSoundFiles(context: Context): List<SoundFile> {
    val sourceFile = File(context.filesDir, "sounds.json")
    if(sourceFile.exists()){
        println("File exists")
    }else {
        val assetManager = context.assets
        val inputStream = assetManager.open("sounds.json")
        val json = inputStream.bufferedReader().use { it.readText() }
        sourceFile.writeText(json)
    }
    val jsonContent = sourceFile.readText()
        // Use Gson to parse the JSON into a list of SoundFile
    val gson = Gson()
    val type = object : TypeToken<List<SoundFile>>() {}.type
    return gson.fromJson(jsonContent, type) // Returns List<SoundFile>
}
fun overwriteSoundsJsonFile(context:Context, soundFiles: List<SoundFile>, filePath: String) {
    val gson = Gson()

    // Convert the list to a JSON string
    val json = gson.toJson(soundFiles)

    // Write the JSON string to the file
    val file = File(context.filesDir,"sounds.json")
    file.writeText(json)
    println("Wrote data: "+json)
}
