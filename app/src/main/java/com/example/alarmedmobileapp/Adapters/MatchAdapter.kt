package com.example.alarmedmobileapp.Adapters

import androidx.fragment.app.Fragment
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.alarmedmobileapp.R


class MatchAdapter: Fragment() {

    override fun onCreate(layoutInflater: LayoutInflater,container:ViewGroup?, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.tile_layout_easy,container)

        val images: MutableList<Int> =
            mutableListOf(R.drawable.anchor, R.drawable.danger, R.drawable.up,
                R.drawable.down, R.drawable.hourglass, R.drawable.light,
                R.drawable.lightningbolt ,R.drawable.star)

        val buttons = arrayOf(button1, button2, button3, button4, button5, button6, button7, button8)

        val cardBack = code
        var clicked = 0
        var turnOver = false
        var lastClicked = -1

        images.shuffle()
        for(i in 0..11){
            buttons[i].setBackgroundResource(cardBack)
            buttons[i].text = "cardBack"
            buttons[i].textSize = 0.0F
            buttons[i].setOnClickListener {
                if (buttons[i].text == "cardBack" && !turnOver) {
                    buttons[i].setBackgroundResource(images[i])
                    buttons[i].setText(images[i])
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
                } else if (clicked == 0) {
                    turnOver = false
                }
            }
        }

}