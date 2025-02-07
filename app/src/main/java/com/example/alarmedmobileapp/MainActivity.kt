package com.example.alarmedmobileapp

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.alarmedmobileapp.Adapters.MatchAdapter
import com.example.alarmedmobileapp.Adapters.MathGameAdapter
import com.example.alarmedmobileapp.Adapters.OrderAdapter
import com.example.alarmedmobileapp.Adapters.Repeat
import com.example.alarmedmobileapp.Adapters.ViewPagerAdapter
import com.example.alarmedmobileapp.Data.Alarm
import com.example.alarmedmobileapp.Data.AlarmList
import com.example.alarmedmobileapp.Data.Days
import com.example.alarmedmobileapp.Data.loadAlarmLists
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope

import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import kotlin.random.Random
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var viewPager2: ViewPager2
        val tasksRemaing = MutableLiveData(0)
        var alarmOn = false
        var tasksDone: MutableList<Int> = mutableListOf()
        var difficulties: MutableList<Int> = mutableListOf(1, 1, 1, 1)
        lateinit var enabledTasks: MutableList<Int>
        fun overwriteTasksJsonFile(context: Context,tasks: MutableList<Int>) {
            val gson = Gson()
            // Convert the list to a JSON string
            val json = gson.toJson(tasks)

            // Write the JSON string to the file
            val file = File(context.filesDir,"tasks.json")
            file.writeText(json)
            println("Wrote data: "+json)
        }
    }


    suspend fun waitForZeroLiveData() {
        suspendCancellableCoroutine<Unit> { continuation ->
            tasksRemaing.observe(this@MainActivity) { remaining ->
                if (remaining == 0) {
                    tasksRemaing.removeObservers(this@MainActivity) // Clean up observer
                    continuation.resume(Unit) // Resume coroutine
                }
            }
        }
    }

    fun normalFlow() {
        setContentView(R.layout.ok_alternative)
        var layout = findViewById<LinearLayout>(R.id.home_container)
        var button=findViewById<ImageButton>(R.id.btnMain)
        layout.setBackgroundColor(Color.CYAN)
        button.isClickable = false
        var recent: Alarm
        var sampleAlarmLists = loadAlarmLists(this)
        println(sampleAlarmLists)
        if (sampleAlarmLists.isNotEmpty() && sampleAlarmLists.get(0).alarms.isNotEmpty()) {
            recent = sampleAlarmLists.get(0).alarms.get(0)
            var time = Int.MAX_VALUE
            var found = false
            for (alarmList in sampleAlarmLists) {
                for (alarm in alarmList.alarms) {
                    if (alarm.isEnabled) {
                        var timeToAlarm = alarm.getTime()
                        if (timeToAlarm < time) {
                            recent = alarm
                            time = timeToAlarm
                            found = true
                        }
                    }
                }
            }
            header = findViewById(R.id.textView2)
            var day: Days
            if (recent.repeat.isEmpty()) {
                if (LocalTime.now().isBefore(LocalTime.of(recent.hour, recent.min))) {
                    day = Days.fromInt(LocalDate.now().dayOfWeek.ordinal)!!
                } else {
                    day = Days.fromInt(LocalDate.now().dayOfWeek.ordinal + 1)!!
                }
            } else {
                day = recent.repeat[0]
            }
            if (recent.hour < 12 && found) {
                header.text = String.format("%02d:%02d AM\n%s", recent.hour, recent.min, day)
            } else {
                header.text =
                    String.format("%02d:%02d PM\n%s", recent.hour - 12, recent.min, day)
            }
        }

        val cyan_color = button.background

        // Initialize ViewPager2 and FragmentStateAdapter
        viewPager2 = findViewById(R.id.viewPager)
        footerButtons = findViewById(R.id.footer)

        fragmentAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 8  // We have 4 fragments
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> ViewPagerAdapter.MainFragment()
                    1 -> ViewPagerAdapter.AlarmFragment()
                    2 -> ViewPagerAdapter.SoundsFragment()
                    3 -> ViewPagerAdapter.EmergencyFragment()
                    4 -> MathGameAdapter()
                    5 -> MatchAdapter( )
                    6 -> OrderAdapter()
                    7 -> Repeat()


                    else -> ViewPagerAdapter.MainFragment() // Default case
                }
            }
        }
        viewPager2.adapter = fragmentAdapter
        viewPager2.currentItem=3
        viewPager2.currentItem=0
        // Set up footer button click listeners
        findViewById<ImageButton>(R.id.btnMain).setOnClickListener {
            layout.setBackgroundColor(Color.WHITE)
            button.isClickable = true
            button = it as ImageButton
            layout=findViewById(R.id.home_container)
            layout.setBackgroundColor(Color.CYAN)
            button.isClickable = false
            viewPager2.currentItem = 0  // Go to MainFragment
        }
        findViewById<ImageButton>(R.id.btnAlarm).setOnClickListener {
            layout.setBackgroundColor(Color.WHITE)
            button.isClickable = true
            button = it as ImageButton
            layout=findViewById(R.id.alarm_container)
            layout.setBackgroundColor(Color.CYAN)
            button.isClickable = false
            viewPager2.currentItem = 1  // Go to AlarmFragment
        }
        findViewById<ImageButton>(R.id.btnSounds).setOnClickListener {
            layout.setBackgroundColor(Color.WHITE)
            button.isClickable = true
            button = it as ImageButton
            layout=findViewById(R.id.sounds_container)
            layout.setBackgroundColor(Color.CYAN)
            button.isClickable = false
            viewPager2.currentItem = 2  // Go to SoundsFragment
        }
        findViewById<ImageButton>(R.id.btnEmergency).setOnClickListener {
            layout.setBackgroundColor(Color.WHITE)
            button.isClickable = true
            button = it as ImageButton
            layout=findViewById(R.id.games_container)
            layout.setBackgroundColor(Color.CYAN)
            button.isClickable = false
            viewPager2.currentItem = 3  // Go to EmergencyFragment
        }
    }


    private lateinit var fragmentAdapter: FragmentStateAdapter
    private lateinit var footerButtons: LinearLayout
    private lateinit var header: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        // Usage
        enabledTasks = loadTasks(this)
        for(i in enabledTasks){
            if (i>0){
                tasksRemaing.value = tasksRemaing.value?.plus(1)
            }
        }
        super.onCreate(savedInstanceState)
        if (alarmOn) {
            setContentView(R.layout.games) // Set the game layout first
            viewPager2 = findViewById(R.id.viewPager)

            fragmentAdapter = object : FragmentStateAdapter(this) {
                override fun getItemCount(): Int = 4  // 4 game fragments
                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> OrderAdapter()
                        1 -> Repeat()
                        2 -> MatchAdapter()
                        3 -> MathGameAdapter()
                        else -> {
                            OrderAdapter()
                        }
                    }
                }
            }
            viewPager2.adapter = fragmentAdapter
            viewPager2.isUserInputEnabled=false
            viewPager2.setCurrentItem(Random.nextInt(4),false)

            // ✅ Wait until tasksRemaining == 0 before switching layouts
            MainScope().launch {
                waitForZeroLiveData()
                alarmOn = false
                normalFlow()
            }
        } else {
            normalFlow()
        }
    }
    fun loadTasks(context: Context): MutableList<Int> {
        val sourceFile = File(context.filesDir, "tasks.json")
        if(sourceFile.exists()){
            println("File exists")
        }else {
            val assetManager = context.assets
            val inputStream = assetManager.open("tasks.json")
            val json = inputStream.bufferedReader().use { it.readText() }
            sourceFile.writeText(json)
        }
        val jsonContent = sourceFile.readText()
        // Use Gson to parse the JSON into a list of SoundFile
        val gson = Gson()
        val type = object : TypeToken<MutableList<Int>>() {}.type
        return gson.fromJson(jsonContent, type) // Returns List<SoundFile>

    }
}