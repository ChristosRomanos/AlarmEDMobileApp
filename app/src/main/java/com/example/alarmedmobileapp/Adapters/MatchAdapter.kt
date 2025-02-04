package com.example.alarmedmobileapp.Adapters

import android.graphics.Color
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import com.example.alarmedmobileapp.MainActivity
import com.example.alarmedmobileapp.R
import kotlin.random.Random


class MatchAdapter(difficulty:Int): Fragment() {
    val difficulty=difficulty
    var buttonsFlipped =0
    override fun onCreateView(layoutInflater: LayoutInflater,container:ViewGroup?, savedInstanceState: Bundle?): View?  {
        super.onCreate(savedInstanceState)
        if (MainActivity.alarmOn) {
            MainActivity.tasksDone.add(MainActivity.viewPager2.currentItem)
        }
        val view=layoutInflater.inflate(R.layout.tile_layout_easy,container)
        val images: MutableList<Int> =
            mutableListOf(
                R.drawable.anchor, R.drawable.danger, R.drawable.up,
                R.drawable.down, R.drawable.anchor, R.drawable.danger, R.drawable.up,
                R.drawable.down
            )
//                )

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


        if(difficulty>1){
            images.addAll(listOf(R.drawable.hourglass, R.drawable.light,
                R.drawable.hourglass, R.drawable.light))

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
            images.addAll(listOf(R.drawable.lightningbolt ,R.drawable.star,
                R.drawable.lightningbolt ,R.drawable.star))

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



        val cardBack = R.drawable.baseline_memory_24
        var clicked = 0
        var turnOver = false
        var lastClicked = -1
        val handler=Handler(Looper.getMainLooper())
        images.shuffle()
        for(i in 0..buttons.size-1){
            buttons[i].setBackgroundResource(cardBack)
            buttons[i].text="cardBack"
            buttons[i].setOnClickListener {
                if (buttons[i].text == "cardBack" && !turnOver) {
                    buttons[i].setBackgroundResource(images[i])
                    buttons[i].setText(images[i])
                    buttons[i].setTextColor(Color.TRANSPARENT)
                    if (clicked == 0) {
                        lastClicked = i
                    }
                    clicked++
                } else if (buttons[i].text !in "cardBack") {
                    buttons[i].setBackgroundResource(cardBack)
                    buttons[i].text = "cardBack"
                    clicked--
                }

                if (clicked == 2) {
                    turnOver = true
                    if (buttons[i].text == buttons[lastClicked].text) {
                        buttons[i].isClickable = false
                        buttons[lastClicked].isClickable = false
                        buttonsFlipped+=2
                        if(buttonsFlipped==buttons.size){
                            MainActivity.tasksRemaing.value = MainActivity.tasksRemaing.value?.plus(
                                -1
                            )
                            var next= Random.nextInt(4)
                            while (MainActivity.tasksDone.contains(next)){
                                next= Random.nextInt(4)
                            }
                            MainActivity.viewPager2.setCurrentItem(next,false)
                        }
                        turnOver = false
                        clicked = 0
                    }
                    else{
                        handler.postDelayed({
                        buttons[i].setBackgroundResource(cardBack)
                        buttons[lastClicked].setBackgroundResource(cardBack)
                        buttons[i].text="cardBack"
                        buttons[lastClicked].text="cardBack"
                        turnOver=false
                        clicked=0
                    },1000)
                    }
                } else if (clicked == 0) {
                    turnOver = false

                }
            }
        }
    return view
}
}