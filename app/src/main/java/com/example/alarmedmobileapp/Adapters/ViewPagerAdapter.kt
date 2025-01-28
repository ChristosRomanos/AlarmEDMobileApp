package com.example.alarmedmobileapp.Adapters

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
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
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.alarmedmobileapp.Data.Alarm
import com.example.alarmedmobileapp.Data.AlarmList
import com.example.alarmedmobileapp.Data.Days
import com.example.alarmedmobileapp.MainActivity
import com.example.alarmedmobileapp.R
import com.example.alarmedmobileapp.sampleAlarmLists
import java.io.File


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


    class SoundsFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.sounds, container, false)
            super.onCreate(savedInstanceState)

            val recyclerView: RecyclerView = view.findViewById(R.id.mp3RecyclerView)
            val mp3Folder = File(Environment.getExternalStorageDirectory(), "YourMp3Folder") // Change path
            val mp3Files = mp3Folder.listFiles { file -> file.extension == "mp3" }?.toList() ?: emptyList()

            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = Mp3Adapter(mp3Files) { file ->
                // Handle Play Button Click
                playMp3(file)
            }
            return view
        }

        private fun playMp3(file: File) {
            MediaPlayer().apply {
                setDataSource(file.absolutePath)
                prepare()
                start()
            }
        }
    }

    class EmergencyFragment : Fragment(R.layout.emergency) {

    }

    class AlarmFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.alarms, container, false)
            val addListContainer: LinearLayout = view.findViewById(R.id.addListContainer)
            val addListButton: Button = addListContainer.findViewById(R.id.addListBtn)
            val addListImageBtn: ImageButton = addListContainer.findViewById(R.id.imageAddListBtn)
            val dynamicContainer: LinearLayout = view.findViewById(R.id.dynamicContainer)
            val alarmRecyclerView = view.findViewById<RecyclerView>(R.id.alarmRecyclerView)
            alarmRecyclerView.layoutManager = LinearLayoutManager(context)
            val alarmAdapter = AlarmAdapter(requireContext(), MainActivity.getAlarmLists())
            alarmRecyclerView.adapter = alarmAdapter
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
                        dynamicContainer.removeView(newRow)
                        addListImageBtn.isClickable = true
                        addListButton.isClickable = true
                        alarmAdapter.notifyItemChanged(alarmRecyclerView.id)
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
                        alarmAdapter.notifyItemChanged(alarmRecyclerView.id)
                    }
                }
                cancelBtn.setOnClickListener {
                    dynamicContainer.removeView(newRow)
                    addListImageBtn.isClickable = true
                    addListButton.isClickable = true
                }
            }
            return view
        }
    }
    }

