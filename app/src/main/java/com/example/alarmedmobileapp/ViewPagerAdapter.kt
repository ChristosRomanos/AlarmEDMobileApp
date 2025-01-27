package com.example.alarmedmobileapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 4  // Total number of views

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MainFragment()
            1 -> AlarmFragment()
            2 -> SoundsFragment()
            3 -> EmergencyFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }

    class MainFragment : Fragment(R.layout.main) {

    }


    class SoundsFragment : Fragment(R.layout.sounds) {

    }

    class EmergencyFragment : Fragment(R.layout.emergency) {

    }

    class AlarmFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.alarms, container, false)

            val alarmRecyclerView = view.findViewById<RecyclerView>(R.id.alarmRecyclerView)
            alarmRecyclerView.layoutManager = LinearLayoutManager(context)

            // Sample data for alarm lists
            val sampleAlarmLists = listOf(
                AlarmList(
                    title = "Morning Alarms",
                    alarms = listOf(
                        Alarm(
                            hour=6,
                            min=0,
                            isEnabled = true,
                            repeat = listOf(Days.Monday, Days.Wednesday, Days.Friday) // Repeat on specific days
                        ),
                        Alarm(
                            hour=6,
                            min=30,
                            isEnabled = false,
                            repeat = emptyList() // No repeat, only once
                        ),
                        Alarm(
                            hour =7,
                            min=30,
                            isEnabled = true,
                            repeat = listOf(Days.Tuesday, Days.Friday) // Repeat on specific days
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
                            repeat = listOf(Days.Monday, Days.Wednesday, Days.Friday) // Repeat on specific days
                        ),
                        Alarm(
                            hour=20,
                            min=30,
                            isEnabled = true,
                            repeat = emptyList() // No repeat, only once
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
                            repeat = listOf(Days.Saturday, Days.Sunday) // Weekend repeat
                        ),
                        Alarm(
                            hour=10,
                            min=0,
                            isEnabled = false,
                            repeat = emptyList() // No repeat, only once
                        )
                    ).toMutableList()
                )
            ).toMutableList()


            val alarmAdapter = AlarmAdapter(requireContext(), sampleAlarmLists)
            alarmRecyclerView.adapter = alarmAdapter

            return view
        }
        }
    }

