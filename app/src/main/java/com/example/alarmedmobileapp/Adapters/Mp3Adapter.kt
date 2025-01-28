package com.example.alarmedmobileapp.Adapters

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmedmobileapp.Data.SoundFile
import com.example.alarmedmobileapp.R

class Mp3Adapter(
    private val mp3Resources: List<SoundFile>,
    private val context: Context,
    private val onPlayClick: (Int) -> Unit
) : RecyclerView.Adapter<Mp3Adapter.Mp3ViewHolder>() {

    private var selectedPosition: Int = -1

    inner class Mp3ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playButton: ImageButton = itemView.findViewById(R.id.playButton)
        val songTitle: TextView = itemView.findViewById(R.id.songTitle)
        val activeButton: RadioButton = itemView.findViewById(R.id.activeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Mp3ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mp3_row_item, parent, false)
        return Mp3ViewHolder(view)
    }

    override fun onBindViewHolder(holder: Mp3ViewHolder, position: Int) {
        val resourceId = mp3Resources[position]

        // Set a placeholder song title (you can customize this further)
        holder.songTitle.text = resourceId.name

        // Play Button Click Listener
        holder.playButton.setOnClickListener {
            val assetPath = resourceId.file
            val assetManager = context.assets
            val mediaPlayer = MediaPlayer()
            val assetFileDescriptor = assetManager.openFd(assetPath)
            mediaPlayer.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
            mediaPlayer.prepare()
            mediaPlayer.start()
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

    override fun getItemCount(): Int = mp3Resources.size
}
