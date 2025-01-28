package com.example.alarmedmobileapp.Data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class SoundFile(
    val name: String,
    val file: String
)

fun loadSoundFiles(context: Context): List<SoundFile> {
    val assetManager = context.assets
    val inputStream = assetManager.open("sounds.json")
    val json = inputStream.bufferedReader().use { it.readText() }
    val gson = Gson()
    val type = object : TypeToken<List<SoundFile>>() {}.type
    return gson.fromJson(json, type)
}
