package com.example.alarmedmobileapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmedmobileapp.R
import java.io.File

class Mp3Adapter(private val mp3Files: List<File>, private val onPlayClick: (File) -> Unit) :
    RecyclerView.Adapter<Mp3Adapter.Mp3ViewHolder>() {

    private var selectedPosition: Int = -1

    inner class Mp3ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playButton: ImageButton = itemView.findViewById(R.id.playButton)
        val songTitle: TextView = itemView.findViewById(R.id.songTitle)
        val activeButton: ImageButton = itemView.findViewById(R.id.activeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Mp3ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mp3_row_item, parent, false)
        return Mp3ViewHolder(view)
    }

    override fun onBindViewHolder(holder: Mp3ViewHolder, position: Int) {
        val file = mp3Files[position]

        // Set the song title
        holder.songTitle.text = file.nameWithoutExtension

        // Play Button Click Listener
        holder.playButton.setOnClickListener {
            onPlayClick(file)
        }

        // Active Button State
        holder.activeButton.isSelected = position == selectedPosition

        // Active Button Click Listener
        holder.activeButton.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = if (selectedPosition == position) -1 else position

            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount(): Int = mp3Files.size
}
