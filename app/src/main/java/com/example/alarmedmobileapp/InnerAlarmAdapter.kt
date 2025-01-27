package com.example.alarmedmobileapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class InnerAlarmAdapter(
    private var alarmList: AlarmList, // Updated data class to include "enabled" and "repeat" fields
    private var context: Context
) : RecyclerView.Adapter<InnerAlarmAdapter.AlarmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_item, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {

        val alarm =alarmList.alarms[position]
        if (alarm.hour<12) {
            holder.timeTextView.text = String.format("%02d:%02d AM",alarm.hour,alarm.min)
        }else{
            holder.timeTextView.text = String.format("%02d:%02d PM",alarm.hour-12,alarm.min)
        }
        holder.repeatTextView.text =
            if (alarm.repeat.isEmpty()) "Once"
            else alarm.repeat.joinToString(", ") { day -> day.name.take(3)}
        holder.alarmSwitch.isChecked = alarm.isEnabled

        // Handle enabling/disabling the alarm
        holder.alarmSwitch.setOnCheckedChangeListener { _, isChecked ->
            alarm.isEnabled = isChecked
        }

        // Handle click on repeat field (you can open a dialog or activity to select days)
        holder.repeatTextView.setOnClickListener {
            // TODO: Open a dialog to let the user select repeat days
        }
        holder.deleteBtn.setOnClickListener{
            alarmList.alarms.remove(alarm)
            notifyItemRemoved(holder.adapterPosition)
        }
        holder.timeTextView.setOnClickListener{
            showEditAlarmDialog(alarmList,alarm)
        }
    }

    override fun getItemCount(): Int = alarmList.alarms.size

    // ViewHolder for each alarm item
    class AlarmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)
        val repeatTextView: TextView = view.findViewById(R.id.repeatTextView)
        val alarmSwitch: Switch = view.findViewById(R.id.alarmSwitch)
        val deleteBtn : ImageButton = view.findViewById(R.id.deleteBtn)
    }
    private fun showEditAlarmDialog( alarmList: AlarmList,alarm : Alarm) {
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
        for (checkBox in repeatCheckboxes ){
                checkBox.isChecked=alarm.repeat.contains(Days.valueOf(checkBox.text.toString()))
        }
        timePicker.hour=alarm.hour
        timePicker.minute=alarm.min
        // Build the dialog
        val dialog = AlertDialog.Builder(context)
            .setTitle("Edit Alarm")
            .setView(dialogView)
            .setPositiveButton("Confirm") { _, _ ->
                // Get alarm name

                // Get time
                val hour = timePicker.hour
                var am = (hour < 12)
                val minute = timePicker.minute

                // Get repeat days
                val selectedDays = repeatCheckboxes.filter { it.isChecked }
                    .map { Days.valueOf(it.text.toString()) }

                // Add the new alarm to your list

                alarm.hour = hour
                alarm.min = minute
                alarm.repeat = selectedDays
                alarm.isEnabled = true
                alarmList.alarms.sortBy { alarm ->
                    alarm.hour * 60 + alarm.min
                }
                // Convert time to total minutes for comparison
                notifyDataSetChanged()
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }


}

