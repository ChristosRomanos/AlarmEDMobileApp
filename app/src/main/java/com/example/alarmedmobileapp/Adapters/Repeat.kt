package com.example.alarmedmobileapp.Adapters

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import com.example.alarmedmobileapp.R
import kotlinx.coroutines.*


class Repeat(difficulty:Int): Fragment() {
    val difficulty=difficulty
    var buttons_to_press=4+2*(difficulty-1)
    var started=false
    lateinit var list_numbers:MutableList<Int>
    lateinit var color:Drawable
    override fun onCreateView(layoutInflater: LayoutInflater,container:ViewGroup?, savedInstanceState: Bundle?): View?  {
        super.onCreate(savedInstanceState)
        val view=layoutInflater.inflate(R.layout.tiles_with_start,container)
        val button1: Button = view.findViewById(R.id.button1)
        val button2: Button = view.findViewById(R.id.button2)
        val button3: Button = view.findViewById(R.id.button3)
        val button4: Button = view.findViewById(R.id.button4)
        val button5: Button = view.findViewById(R.id.button5)
        val button6: Button = view.findViewById(R.id.button6)
        val button7: Button = view.findViewById(R.id.button7)
        val button8: Button = view.findViewById(R.id.button8)


        val buttons =
            mutableListOf(button1, button2, button3, button4, button5, button6, button7, button8)
        var buttons_pressed=0

        if(difficulty>1){

            val button9:Button=view.findViewById(R.id.button9)
            button9.isClickable=true
            button9.isEnabled=true
            button9.isVisible=true

            val button10:Button=view.findViewById(R.id.button10)
            button10.isClickable=true
            button10.isEnabled=true
            button10.isVisible=true

            val button11:Button=view.findViewById(R.id.button11)
            button11.isClickable=true
            button11.isEnabled=true
            button11.isVisible=true

            val button12:Button=view.findViewById(R.id.button12)
            button12.isClickable=true
            button12.isEnabled=true
            button12.isVisible=true
            buttons.addAll(listOf(button9,button10,button11,button12))
        }
        if(difficulty>2){

            val button13:Button=view.findViewById(R.id.button13)
            button13.isClickable=true
            button13.isEnabled=true
            button13.isVisible=true

            val button14:Button=view.findViewById(R.id.button14)
            button14.isClickable=true
            button14.isEnabled=true
            button14.isVisible=true

            val button15:Button=view.findViewById(R.id.button15)
            button15.isClickable=true
            button15.isEnabled=true
            button15.isVisible=true

            val button16:Button=view.findViewById(R.id.button16)
            button16.isClickable=true
            button16.isEnabled=true
            button16.isVisible=true

            buttons.addAll(listOf(button13,button14,button15,button16))
        }






        val startBtn:Button=view.findViewById(R.id.startBtn)
        color = buttons[0].background
        var handler=Handler(Looper.getMainLooper())
        startBtn.setOnClickListener {
            start_sequence(buttons,startBtn)
        }
        for (i in 0..buttons.size - 1) {
            buttons[i].setOnClickListener {
                if (started) {
                    if (list_numbers[buttons_pressed] == i) {
                        buttons_pressed += 1
                        if (buttons_pressed == buttons_to_press) {
                            for (button in buttons) {
                                startBtn.text="FINISHED"
                                button.isClickable = false
                                button.isEnabled = false
                            }
                        }
                        buttons[i].setBackgroundColor(Color.CYAN)
                        buttons[i].isClickable=false
                        buttons[i].isEnabled=false
                    } else {
                        started = false
                        for (i in 0..buttons.size - 1) {

                            buttons[i].setBackgroundColor(Color.RED)
                            buttons[i].isClickable = true
                            buttons[i].isEnabled = true
                        }
                        handler.postDelayed({

                            for (i in 0..buttons.size - 1) {
                                buttons[i].background=color
                            }
                            buttons_pressed=0
                            start_sequence(buttons,startBtn)
                        },1000)


                    }
                }
            }
        }
        return view
    }
    fun start_sequence(buttons: MutableList<Button>,startBtn:Button){
        list_numbers = mutableListOf()
        for (i in 0..buttons.size-1){
            list_numbers.add(i)
        }
        if (!started) {
            startBtn.isClickable = false
            started = true
            list_numbers.shuffle()
            GlobalScope.launch(Dispatchers.Main) {
                delay(500)
                for (i in 0..buttons_to_press-1) {

                    buttons[list_numbers[i]].setBackgroundColor(Color.CYAN)
                    delay(500) // Suspend without blocking

                    buttons[list_numbers[i]].background = color // Restore original color
                }
            }
        }

    }
}