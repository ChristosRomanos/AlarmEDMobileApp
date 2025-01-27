package com.example.alarmedmobileapp

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColor
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var fragmentAdapter: FragmentStateAdapter
    private lateinit var footerButtons: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ok)
        var button =findViewById<ImageButton>(R.id.btnMain)
        button.setBackgroundColor(Color.GREEN)
        // Initialize ViewPager2 and FragmentStateAdapter
        viewPager2 = findViewById(R.id.viewPager)
        footerButtons = findViewById(R.id.footer)

        fragmentAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 4  // We have 4 fragments
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> ViewPagerAdapter.MainFragment()
                    1 -> ViewPagerAdapter.AlarmFragment()
                    2 -> ViewPagerAdapter.SoundsFragment()
                    3 -> ViewPagerAdapter.EmergencyFragment()
                    else -> ViewPagerAdapter.MainFragment() // Default case
                }
            }
        }
        viewPager2.adapter = fragmentAdapter
        // Set up footer button click listeners
        findViewById<ImageButton>(R.id.btnMain).setOnClickListener {
            button.setBackgroundColor(Color.WHITE)
            button.isClickable=true
            button = it as ImageButton
            button.setBackgroundColor(Color.GREEN)
            button.isClickable=false
            viewPager2.currentItem = 0  // Go to MainFragment
        }
        findViewById<ImageButton>(R.id.btnAlarm).setOnClickListener {
            button.setBackgroundColor(Color.WHITE)
            button.isClickable=true
            button = it as ImageButton
            button.setBackgroundColor(Color.GREEN)
            button.isClickable=false
            viewPager2.currentItem = 1  // Go to AlarmFragment
        }
        findViewById<ImageButton>(R.id.btnSounds).setOnClickListener {
            button.setBackgroundColor(Color.WHITE)
            button.isClickable=true
            button = it as ImageButton
            button.setBackgroundColor(Color.GREEN)
            button.isClickable=false
            viewPager2.currentItem = 2  // Go to SoundsFragment
        }
        findViewById<ImageButton>(R.id.btnEmergency).setOnClickListener {
            button.setBackgroundColor(Color.WHITE)
            button.isClickable=true
            button = it as ImageButton
            button.setBackgroundColor(Color.GREEN)
            button.isClickable=false
            viewPager2.currentItem = 3  // Go to EmergencyFragment
        }
    }

}