package com.example.alarmedmobileapp.Data

data class ExpandableListData(
    val title: String,
    val groups: List<String>,          // Group titles
    val children: List<List<String>>  // Children for each group
)
