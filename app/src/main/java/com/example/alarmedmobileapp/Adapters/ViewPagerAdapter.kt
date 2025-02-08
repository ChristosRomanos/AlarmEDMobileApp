package com.example.alarmedmobileapp.Adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog.Builder
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.alarmedmobileapp.Data.Alarm
import com.example.alarmedmobileapp.Data.AlarmList
import com.example.alarmedmobileapp.Data.loadAlarmLists
import com.example.alarmedmobileapp.Data.loadSoundFiles
import com.example.alarmedmobileapp.Data.overwriteAlarmsJsonFile
import com.example.alarmedmobileapp.MainActivity
import com.example.alarmedmobileapp.MainActivity.Companion.difficulties
import com.example.alarmedmobileapp.MainActivity.Companion.enabledTasks
import com.example.alarmedmobileapp.MainActivity.Companion.viewPager2
import com.example.alarmedmobileapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 8  // Total number of views

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MainFragment()
            1 -> AlarmFragment()
            2 -> SoundsFragment()
            3 -> EmergencyFragment()
            4 -> MathGameAdapter()
            5 -> MatchAdapter()
            6 -> OrderAdapter()
            7 -> Repeat()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }

    class MainFragment : Fragment(R.layout.main) {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.main, container, false)
        }
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
        lateinit var color: Drawable
        lateinit var mathsLayout: ConstraintLayout
        lateinit var orderLayout: ConstraintLayout
        lateinit var matchLayout: ConstraintLayout
        lateinit var repeatLayout: ConstraintLayout
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            viewPager2.isUserInputEnabled = true
            val view = inflater.inflate(R.layout.emergency, container, false)
            val mathsButton:ImageButton  =view.findViewById(R.id.mathsBtn)
            val orderButton:ImageButton  =view.findViewById(R.id.orderBtn)
            val matchButton:ImageButton  =view.findViewById(R.id.memoryBtn)
            val repeatButton:ImageButton =view.findViewById(R.id.repeatBtn)
            val remainingView:TextView=view.findViewById(R.id.remainingText)
            remainingView.text="Amount of tasks needed to be completed: "+(enabledTasks.count { it!=0 }).toString()
            mathsLayout=view.findViewById(R.id.mathsBtnLayout)
            orderLayout=view.findViewById(R.id.orderLayout)
            matchLayout=view.findViewById(R.id.memoryLayout)
            repeatLayout=view.findViewById(R.id.repeatLayout)
            color= repeatLayout.background

            for (i in 0..3){
                if (enabledTasks[i]!=0){
                    when(i){
                        0->mathsLayout.setBackgroundColor(Color.CYAN)
                        1->matchLayout.setBackgroundColor(Color.CYAN)
                        2->orderLayout.setBackgroundColor(Color.CYAN)
                        3->repeatLayout.setBackgroundColor(Color.CYAN)
                    }
                }else {
                    when (i) {
                        0 -> mathsLayout.setBackgroundColor(Color.GRAY)
                        1 -> matchLayout.setBackgroundColor(Color.GRAY)
                        2 -> orderLayout.setBackgroundColor(Color.GRAY)
                        3 -> repeatLayout.setBackgroundColor(Color.GRAY)
                    }
                }
            }
            mathsButton.setOnClickListener{
                showPopup(this.requireContext(),0)
            }
            orderButton.setOnClickListener{
                showPopup(this.requireContext(),2)
            }
            matchButton.setOnClickListener{
                showPopup(this.requireContext(),1)
            }
            repeatButton.setOnClickListener{
                showPopup(this.requireContext(),3)
            }
            return view
        }
        fun showPopup(context: Context?,task:Int) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.popout, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            // Initialize Spinners
            val difficultySpinner1 = view.findViewById<Spinner>(R.id.difficultySpinner1)
            val difficultySpinner2 = view.findViewById<Spinner>(R.id.difficultySpinner2)

            // Set up adapter
            val adapter = ArrayAdapter.createFromResource(
                this.requireContext(), R.array.difficulty_levels, android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // Assign adapter to Spinners
            difficultySpinner1.adapter = adapter
            difficultySpinner2.adapter = adapter

            // Set "Difficulty" as default
            difficultySpinner1.setSelection(0)
            difficultySpinner2.setSelection(0)

            // Buttons
            val tryButton = view.findViewById<Button>(R.id.tryButton)
            val enabledButton = view.findViewById<Button>(R.id.enabledButton)
            val goBackButton = view.findViewById<Button>(R.id.goBackButton)
            val disableBtn=view.findViewById<Button>(R.id.disableBtn)
            if (MainActivity.enabledTasks[task]==0){
                disableBtn.visibility=View.GONE
            }else{
                disableBtn.visibility=View.VISIBLE
            }
            difficultySpinner1
            // Prevent invalid selection
            tryButton.setOnClickListener {
                if (difficultySpinner1.selectedItemPosition == 0) {

                    Toast.makeText(context, "Please select a difficulty level", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val selectedDifficulty = difficultySpinner1.selectedItem.toString()
                    difficulties[task] = difficultySpinner1.selectedItemPosition
                     Toast.makeText(context, "Selected: $selectedDifficulty", Toast.LENGTH_SHORT)
                        .show()
                    dialog.dismiss()
                    viewPager2.adapter=MainActivity.fragmentAdapter2
                    viewPager2.isUserInputEnabled = false
                    viewPager2.adapter?.notifyItemChanged(task)
                    viewPager2.currentItem = task

                }
            }
            goBackButton.setOnClickListener {
                dialog.dismiss()
            }

            enabledButton.setOnClickListener {
                if (difficultySpinner2.selectedItemPosition == 0) {
                    Toast.makeText(context, "Please select a difficulty level", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    print(task)
                    val selectedDifficulty = difficultySpinner2.selectedItem.toString()
                    Toast.makeText(context, "Enabled: $selectedDifficulty", Toast.LENGTH_SHORT)
                        .show()
                    enabledTasks[task]=difficultySpinner2.selectedItemPosition
                    when(task){
                        0->mathsLayout.setBackgroundColor(Color.CYAN)
                        1->matchLayout.setBackgroundColor(Color.CYAN)
                        2->orderLayout.setBackgroundColor(Color.CYAN)
                        3->repeatLayout.setBackgroundColor(Color.CYAN)
                    }
                    MainActivity.overwriteTasksJsonFile(this.requireContext(),enabledTasks)
                    dialog.dismiss()
                }
            }
            disableBtn.setOnClickListener{
                print(task)
                enabledTasks[task]=0
                when(task){

                    0->mathsLayout.setBackgroundColor(Color.GRAY)
                    1->matchLayout.setBackgroundColor(Color.GRAY)
                    2->orderLayout.setBackgroundColor(Color.GRAY)
                    3->repeatLayout.setBackgroundColor(Color.GRAY)
                }
                MainActivity.overwriteTasksJsonFile(this.requireContext(),enabledTasks)
                dialog.dismiss()
            }
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

