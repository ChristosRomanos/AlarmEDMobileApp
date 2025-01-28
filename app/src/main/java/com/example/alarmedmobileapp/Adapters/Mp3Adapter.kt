package com.example.alarmedmobileapp.Adapters

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmedmobileapp.Data.SoundFile
import com.example.alarmedmobileapp.Data.overwriteSoundsJsonFile
import com.example.alarmedmobileapp.R

class Mp3Adapter(
    private val mp3Resources: List<SoundFile>,
    private val context: Context,
    private val applyBtn: Button
) : RecyclerView.Adapter<Mp3Adapter.Mp3ViewHolder>() {

    private var selectedPosition: Int = -1
    var playing=false
    lateinit var chosen_song_holder : Mp3ViewHolder
    var initial_chosen_song : SoundFile? =null
    var chosen_song : SoundFile? =null

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
        if (resourceId.chosen){
            chosen_song_holder=holder
            chosen_song=resourceId
            holder.activeButton.isClickable=false
            if (initial_chosen_song==null){
                initial_chosen_song=resourceId
            }
        }
        applyBtn.isEnabled=initial_chosen_song!=chosen_song
        applyBtn.isClickable=initial_chosen_song!=chosen_song
        // Set a placeholder song title (you can customize this further)
        holder.songTitle.text = resourceId.name
        val mediaPlayer = MediaPlayer()
        // Play Button Click Listener
        holder.playButton.setOnClickListener {
            if(!playing) {
                val assetPath = resourceId.file
                val assetManager = context.assets
                val assetFileDescriptor = assetManager.openFd(assetPath)
                holder.playButton.setImageResource(R.drawable.baseline_pause_24)
                mediaPlayer.setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                mediaPlayer.prepare()
                mediaPlayer.start()
                playing=true
            }else{
                mediaPlayer.stop()
                playing=false
                holder.playButton.setImageResource(R.drawable.baseline_play_arrow_24)
            }
        }
        applyBtn.setOnClickListener{
            println(initial_chosen_song?.name +"  :  "+chosen_song?.name)
            overwriteSoundsJsonFile(context,mp3Resources,"sounds.json")
            initial_chosen_song=resourceId
            notifyItemChanged(holder.adapterPosition)
        }
        // Active Button State
        holder.activeButton.isChecked = resourceId.chosen

        // Active Button Click Listener
        holder.activeButton.setOnClickListener {

            chosen_song_holder.activeButton.isClickable=true
            chosen_song_holder.activeButton.isChecked=false
            chosen_song!!.chosen =false
            chosen_song=resourceId
            chosen_song!!.chosen=true
            chosen_song_holder=holder
            holder.activeButton.isChecked=true
            holder.activeButton.isClickable=false
            notifyItemChanged(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int = mp3Resources.size
}
