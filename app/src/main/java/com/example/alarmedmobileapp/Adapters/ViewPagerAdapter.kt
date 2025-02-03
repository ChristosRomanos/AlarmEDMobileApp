package com.example.alarmedmobileapp.Adapters

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.alarmedmobileapp.Data.Alarm
import com.example.alarmedmobileapp.Data.AlarmList
import com.example.alarmedmobileapp.Data.loadAlarmLists
import com.example.alarmedmobileapp.Data.loadSoundFiles
import com.example.alarmedmobileapp.Data.overwriteAlarmsJsonFile
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
            4 -> MatchAdapter(3)
            5 -> MathGameAdapter(3)
            6 -> OrderAdapter(3)
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }

    class MainFragment : Fragment(R.layout.main) {

    }


    class SoundsFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.sounds, container, false)
            super.onCreate(savedInstanceState)
            val applyBtn: Button = view.findViewById(R.id.applyBtn)
            val recyclerView: RecyclerView = view.findViewById(R.id.mp3RecyclerView)

            // List of raw resource IDs
            val soundFiles = loadSoundFiles(this.requireContext())
            applyBtn.isClickable = false
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = Mp3Adapter(soundFiles, this.requireContext(), applyBtn)
            return view
        }
        private fun playMp3(resourceId: Int) {
            val mediaPlayer = MediaPlayer.create(this.requireContext(), resourceId)
            mediaPlayer.start()
        }
    }

    class EmergencyFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.emergency, container, false)
            val mathsButton :ImageButton =view.findViewById(R.id.mathsBtn)
            mathsButton.setOnClickListener{
                MainActivity.viewPager2.currentItem=4
                MainActivity.viewPager2.setCurrentItem(4,true)
                println(MainActivity.viewPager2.currentItem)
            }
            return view
        }
    }


    class AlarmFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            var alarms_changed=false
            var sampleAlarmLists= loadAlarmLists(this.requireContext())
            val view = inflater.inflate(R.layout.alarms, container, false)
            val addListButton: Button = view.findViewById(R.id.addListBtn)
            val addListImageBtn: ImageButton = view.findViewById(R.id.imageAddListBtn)
            val dynamicContainer: LinearLayout = view.findViewById(R.id.dynamicContainer)
            val alarmRecyclerView = view.findViewById<RecyclerView>(R.id.alarmRecyclerView)
            val applyBtn :Button=view.findViewById(R.id.applyAlarmChangesBtn)

            alarmRecyclerView.layoutManager = LinearLayoutManager(context)
            val alarmAdapter = AlarmAdapter(requireContext(), sampleAlarmLists,applyBtn)
            alarmRecyclerView.adapter = alarmAdapter
            applyBtn.isClickable=alarms_changed
            applyBtn.isEnabled=alarms_changed
            addListImageBtn.setOnClickListener {
                // Create a new row
                val newRow = LayoutInflater.from(context).inflate(R.layout.dynamic_row, null)
                // Add row to the dynamic container
                dynamicContainer.addView(newRow)
                addListImageBtn.isClickable = false
                addListButton.isClickable = false
                val acceptBtn = dynamicContainer.findViewById<Button>(R.id.rowConfirmBtn)
                val cancelBtn = dynamicContainer.findViewById<Button>(R.id.rowCancelBtn)
                val nameBox = dynamicContainer.findViewById<EditText>(R.id.rowEditText)

                alarmRecyclerView.adapter = alarmAdapter
                acceptBtn.setOnClickListener {
                    if (nameBox.text.isNotEmpty()) {
                        sampleAlarmLists.add(
                            AlarmList(
                                title = nameBox.text.toString(),
                                alarms = emptyList<Alarm>().toMutableList()
                            )
                        )
                        alarmAdapter.notifyItemChanged(alarmAdapter.itemCount-1)
                        dynamicContainer.removeView(newRow)
                        addListImageBtn.isClickable = true
                        addListButton.isClickable = true
                        applyBtn.isClickable=true
                        applyBtn.isEnabled=true
                    }
                }
                cancelBtn.setOnClickListener {
                    dynamicContainer.removeView(newRow)
                    addListImageBtn.isClickable = true
                    addListButton.isClickable = true
                }
            }
            // Sample data for alarm lists
            addListButton.setOnClickListener {
                // Create a new row
                val newRow = LayoutInflater.from(context).inflate(R.layout.dynamic_row, null)
                // Add row to the dynamic container
                dynamicContainer.addView(newRow)
                addListImageBtn.isClickable = false
                addListButton.isClickable = false
                val acceptBtn = dynamicContainer.findViewById<Button>(R.id.rowConfirmBtn)
                val cancelBtn = dynamicContainer.findViewById<Button>(R.id.rowCancelBtn)
                val nameBox = dynamicContainer.findViewById<EditText>(R.id.rowEditText)
                alarmRecyclerView.adapter = alarmAdapter
                acceptBtn.setOnClickListener {
                    if (nameBox.text.isNotEmpty()) {
                        sampleAlarmLists.add(
                            AlarmList(
                                title = nameBox.text.toString(),
                                alarms = emptyList<Alarm>().toMutableList()
                            )
                        )
                        dynamicContainer.removeView(newRow)
                        addListImageBtn.isClickable = true
                        addListButton.isClickable = true
                        alarms_changed=true
                        applyBtn.isClickable=true
                        applyBtn.isEnabled=true
                        alarmAdapter.notifyItemChanged(alarmRecyclerView.id)
                    }
                }
                cancelBtn.setOnClickListener {
                    dynamicContainer.removeView(newRow)
                    addListImageBtn.isClickable = true
                    addListButton.isClickable = true
                }
            }
            applyBtn.setOnClickListener{
                overwriteAlarmsJsonFile(this.requireContext(),sampleAlarmLists)
                it.isClickable=false
                it.isEnabled=false
            }
            return view
        }
    }
    }

