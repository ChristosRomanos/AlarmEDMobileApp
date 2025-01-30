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
import com.example.alarmedmobileapp.R


class MatchAdapter: Fragment() {

    override fun onCreateView(layoutInflater: LayoutInflater,container:ViewGroup?, savedInstanceState: Bundle?): View?  {
        super.onCreate(savedInstanceState)
        val view=layoutInflater.inflate(R.layout.tile_layout_easy,container)

        val images: MutableList<Int> =
            mutableListOf(R.drawable.anchor, R.drawable.danger, R.drawable.up,
                R.drawable.down,R.drawable.anchor, R.drawable.danger, R.drawable.up,
                R.drawable.down)// R.drawable.hourglass, R.drawable.light,
//                R.drawable.lightningbolt ,R.drawable.star)

        val button1: Button = view.findViewById(R.id.button1)
        val button2: Button = view.findViewById(R.id.button2)
        val button3: Button = view.findViewById(R.id.button3)
        val button4: Button = view.findViewById(R.id.button4)
        val button5: Button = view.findViewById(R.id.button5)
        val button6: Button = view.findViewById(R.id.button6)
        val button7: Button = view.findViewById(R.id.button7)
        val button8: Button = view.findViewById(R.id.button8)



        val buttons = arrayOf(button1, button2, button3, button4, button5, button6, button7, button8)

        val cardBack = R.drawable.baseline_memory_24
        var clicked = 0
        var turnOver = false
        var lastClicked = -1
        val handler=Handler(Looper.getMainLooper())
        images.shuffle()
        for(i in 0..7){
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