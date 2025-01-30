package com.example.alarmedmobileapp.Data

import com.example.alarmedmobileapp.R

class Task(val name: String,
           val iconResId: Int,
           var isSelected: Boolean = false) {

    companion object {
        fun getDefaultTasks(): List<Task> {
            return listOf(
                Task("Maths", R.drawable.baseline_calculate_24),
//                Task("Memory", R.drawable.),
//                Task("Order", R.drawable.),
//                Task("Repeat", R.drawable.),
//                Task("Barcode", R.drawable.),
//                Task("Shake", R.drawable.),
//                Task("Rewrite", R.drawable.),
//                Task("Match", R.drawable.)
            )
        }

    }


}