package com.example.alarmedmobileapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmedmobileapp.Data.AlarmList
import com.example.alarmedmobileapp.Data.Alarm
import com.example.alarmedmobileapp.Data.Days
import com.example.alarmedmobileapp.R

class AlarmAdapter(
    private val context: Context,
    private var alarmLists: MutableList<AlarmList>, // Each AlarmList contains a name and a list of alarms
    applyBtn: Button
) : RecyclerView.Adapter<AlarmAdapter.AlarmListViewHolder>() {
    val applyBtn=applyBtn
    private val expandedStates = mutableSetOf<Int>() // Tracks which lists are expanded
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.alarm_list_item, parent, false)

        return AlarmListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmListViewHolder, position: Int) {
        val alarmList = alarmLists[position]

        holder.listNameTextView.text = alarmList.title

        // Show or hide the inner list of alarms based on expanded state
        if (expandedStates.contains(position)) {
            holder.alarmsRecyclerView.visibility = View.VISIBLE
            holder.expandCollapseButton.text = "Collapse"
        } else {
            holder.alarmsRecyclerView.visibility = View.GONE
            holder.expandCollapseButton.text = "Expand"
        }

        // Set up the inner RecyclerView for alarms
        val alarmAdapter = InnerAlarmAdapter(alarmList,this.context)
        holder.alarmsRecyclerView.adapter = alarmAdapter
        holder.alarmsRecyclerView.layoutManager = LinearLayoutManager(context)

        // Toggle expanded state on button click
        holder.expandCollapseButton.setOnClickListener {
            if (expandedStates.contains(position)) {
                expandedStates.remove(position)
            } else {
                expandedStates.add(position)
            }
            notifyItemChanged(position)
        }
        holder.deleteListBtn.setOnClickListener{
            alarmLists.removeAt(holder.adapterPosition)
            alarmAdapter.notifyItemRemoved(holder.adapterPosition)
            alarmAdapter.notifyItemRangeChanged(position, alarmLists.size)
            applyBtn.isClickable=true
            applyBtn.isEnabled=true
        }
        holder.addBtn.setOnClickListener{
            showAddAlarmDialog(alarmList,alarmAdapter)
        }
    }

    override fun getItemCount(): Int = alarmLists.size

    // ViewHolder for each list of alarms
    class AlarmListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val listNameTextView: TextView = view.findViewById(R.id.listNameTextView)
        val expandCollapseButton: TextView = view.findViewById(R.id.expandCollapseButton)
        val alarmsRecyclerView: RecyclerView = view.findViewById(R.id.alarmsRecyclerView)
        val deleteListBtn : ImageButton =view.findViewById(R.id.deleteListBtn)
        val addBtn: ImageButton =view.findViewById(R.id.addBtn)
    }

    private fun showAddAlarmDialog( alarmList: AlarmList,alarmAdapter: InnerAlarmAdapter) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_alarm, null)
        val timePicker = dialogView.findViewById<TimePicker>(R.id.time_picker)
        timePicker.setIs24HourView(true)
        val repeatCheckboxes = listOf(
            dialogView.findViewById<CheckBox>(R.id.checkbox_monday),
            dialogView.findViewById<CheckBox>(R.id.checkbox_tuesday),
            dialogView.findViewById<CheckBox>(R.id.checkbox_wednesday),
            dialogView.findViewById<CheckBox>(R.id.checkbox_thursday),
            dialogView.findViewById<CheckBox>(R.id.checkbox_friday),
            dialogView.findViewById<CheckBox>(R.id.checkbox_saturday),
            dialogView.findViewById<CheckBox>(R.id.checkbox_sunday),
        )

        // Build the dialog
        val dialog = AlertDialog.Builder(this.context)
            .setTitle("Add Alarm")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                // Get alarm name

                // Get time
                val hour = timePicker.hour
                var am = (hour < 12)
                val minute = timePicker.minute

                // Get repeat days
                val selectedDays = repeatCheckboxes.filter { it.isChecked }
                    .map { Days.valueOf(it.text.toString()) }

                // Add the new alarm to your list
                val newAlarm = Alarm(
                    hour = hour,
                    min = minute,
                    repeat = selectedDays.toMutableList(),
                    isEnabled = true
                )
                alarmList.alarms.add(newAlarm)
                alarmList.alarms.sortBy { alarm ->
                    alarm.hour * 60 + alarm.min
                }
                alarmAdapter.notifyDataSetChanged()
                applyBtn.isClickable=true
                applyBtn.isEnabled=true
                // Convert time to total minutes for comparison
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

}

