package com.example.alarmedmobileapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpandableListRecyclerAdapter(
    private val context: Context,
    private val data: List<ExpandableListData>
) : RecyclerView.Adapter<ExpandableListRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val expandableListView: ExpandableListView = view.findViewById(R.id.expandableListView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.expandable_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.title.text = item.title

        // Set the adapter for the ExpandableListView
        holder.expandableListView.setAdapter(object : BaseExpandableListAdapter() {
            override fun getGroupCount(): Int = item.groups.size

            override fun getChildrenCount(groupPosition: Int): Int = item.children[groupPosition].size

            override fun getGroup(groupPosition: Int): Any = item.groups[groupPosition]

            override fun getChild(groupPosition: Int, childPosition: Int): Any =
                item.children[groupPosition][childPosition]

            override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

            override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

            override fun hasStableIds(): Boolean = true

            override fun getGroupView(
                groupPosition: Int,
                isExpanded: Boolean,
                convertView: View?,
                parent: ViewGroup?
            ): View {
                val groupText = getGroup(groupPosition) as String
                val textView = TextView(context)
                textView.text = groupText
                textView.setPadding(32, 16, 16, 16)
                return textView
            }

            override fun getChildView(
                groupPosition: Int,
                childPosition: Int,
                isLastChild: Boolean,
                convertView: View?,
                parent: ViewGroup?
            ): View {
                val childText = getChild(groupPosition, childPosition) as String
                val textView = TextView(context)
                textView.text = childText
                textView.setPadding(64, 16, 16, 16)
                return textView
            }

            override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true
        })
    }

    override fun getItemCount(): Int = data.size
}
