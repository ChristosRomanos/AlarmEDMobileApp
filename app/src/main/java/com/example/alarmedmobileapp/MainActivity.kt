package com.example.alarmedmobileapp

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.alarmedmobileapp.Adapters.ViewPagerAdapter
import com.example.alarmedmobileapp.Data.Alarm
import com.example.alarmedmobileapp.Data.AlarmList
import com.example.alarmedmobileapp.Data.Days
import java.time.LocalDate
import java.time.LocalTime

var sampleAlarmLists = listOf(
    AlarmList(
        title = "Morning Alarms",
        alarms = listOf(
            Alarm(
                hour=6,
                min=0,
                isEnabled = true,
                repeat = listOf(Days.Monday, Days.Wednesday, Days.Friday).toMutableList() // Repeat on specific days
            ),
            Alarm(
                hour=6,
                min=30,
                isEnabled = false,
                repeat = listOf<Days>().toMutableList() // No repeat, only once
            ),
            Alarm(
                hour =7,
                min=30,
                isEnabled = true,
                repeat = listOf(Days.Tuesday, Days.Friday).toMutableList() // Repeat on specific days
            )
        ).toMutableList()
    ),
    AlarmList(
        title = "Work Alarms",
        alarms = listOf(
            Alarm(
                hour = 8,
                min=0,
                isEnabled = true,
                repeat = listOf(Days.Monday, Days.Wednesday, Days.Friday).toMutableList() // Repeat on specific days
            ),
            Alarm(
                hour=20,
                min=30,
                isEnabled = true,
                repeat = listOf<Days>().toMutableList() // No repeat, only once
            )
        ).toMutableList()
    ),
    AlarmList(
        title = "Weekend Alarms",
        alarms = listOf(
            Alarm(
                hour=9,
                min=0,
                isEnabled = true,
                repeat = listOf(Days.Saturday, Days.Sunday).toMutableList() // Weekend repeat
            ),
            Alarm(
                hour=10,
                min=0,
                isEnabled = false,
                repeat = listOf<Days>().toMutableList() // No repeat, only once
            )
        ).toMutableList()
    )
).toMutableList()
class MainActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var fragmentAdapter: FragmentStateAdapter
    private lateinit var footerButtons: LinearLayout
    private lateinit var header: TextView
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ok)
        var button =findViewById<ImageButton>(R.id.btnMain)
        var recent :Alarm
        if (sampleAlarmLists.isNotEmpty()&& sampleAlarmLists.get(0).alarms.isNotEmpty()) {
            recent = sampleAlarmLists.get(0).alarms.get(0)
            var time = Int.MAX_VALUE
            var found=false
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
            header= findViewById(R.id.textView2)
            var day : Days
            if (recent.repeat.isEmpty()){
                if(LocalTime.now().isBefore(LocalTime.of(recent.hour,recent.min))){
                    day= Days.fromInt(LocalDate.now().dayOfWeek.ordinal)!!
                }
                else{
                    day=Days.fromInt(LocalDate.now().dayOfWeek.ordinal+1)!!
                }
            }else{
                day=recent.repeat[0]
            }
            if (recent.hour<12&&found) {
                header.text = String.format("%02d:%02d AM\n%s",recent.hour,recent.min,day)
            }
            else{
                header.text = String.format("%02d:%02d PM\n%s",recent.hour-12,recent.min,day)
            }
        }

        button.setBackgroundColor(Color.GREEN)
        // Initialize ViewPager2 and FragmentStateAdapter
        viewPager2 = findViewById(R.id.viewPager)
        footerButtons = findViewById(R.id.footer)

        fragmentAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 4  // We have 4 fragments
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> ViewPagerAdapter.MainFragment()
                    1 -> ViewPagerAdapter.AlarmFragment()
                    2 -> ViewPagerAdapter.SoundsFragment()
                    3 -> ViewPagerAdapter.EmergencyFragment()
                    else -> ViewPagerAdapter.MainFragment() // Default case
                }
            }
        }
        viewPager2.adapter = fragmentAdapter
        // Set up footer button click listeners
        findViewById<ImageButton>(R.id.btnMain).setOnClickListener {
            button.setBackgroundColor(Color.WHITE)
            button.isClickable=true
            button = it as ImageButton
            button.setBackgroundColor(Color.GREEN)
            button.isClickable=false
            viewPager2.currentItem = 0  // Go to MainFragment
        }
        findViewById<ImageButton>(R.id.btnAlarm).setOnClickListener {
            button.setBackgroundColor(Color.WHITE)
            button.isClickable=true
            button = it as ImageButton
            button.setBackgroundColor(Color.GREEN)
            button.isClickable=false
            viewPager2.currentItem = 1  // Go to AlarmFragment
        }
        findViewById<ImageButton>(R.id.btnSounds).setOnClickListener {
            button.setBackgroundColor(Color.WHITE)
            button.isClickable=true
            button = it as ImageButton
            button.setBackgroundColor(Color.GREEN)
            button.isClickable=false
            viewPager2.currentItem = 2  // Go to SoundsFragment
        }
        findViewById<ImageButton>(R.id.btnEmergency).setOnClickListener {
            button.setBackgroundColor(Color.WHITE)
            button.isClickable=true
            button = it as ImageButton
            button.setBackgroundColor(Color.GREEN)
            button.isClickable=false
            viewPager2.currentItem = 3  // Go to EmergencyFragment
        }
    }
    companion object {
        fun getAlarmLists(): MutableList<AlarmList> {
            return sampleAlarmLists
        }
    }

}