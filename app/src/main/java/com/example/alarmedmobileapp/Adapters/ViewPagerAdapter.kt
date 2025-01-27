package com.example.alarmedmobileapp.Adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.alarmedmobileapp.Data.Alarm
import com.example.alarmedmobileapp.Data.AlarmList
import com.example.alarmedmobileapp.Data.Days
import com.example.alarmedmobileapp.MainActivity
import com.example.alarmedmobileapp.R



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



            val alarmAdapter = AlarmAdapter(requireContext(), MainActivity.getAlarmLists())
            alarmRecyclerView.adapter = alarmAdapter

            return view
        }
        }
    }

