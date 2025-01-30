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
import android.widget.Toast
import androidx.compose.ui.tooling.preview.Preview
import com.example.alarmedmobileapp.R

class OrderAdapter(difficulty:Int): Fragment() {

    // val difficulty=difficulty
    private val numbers = (1..8).shuffled().toMutableList()
    private lateinit var buttons: Array<Button>
    private var firstClickIndex: Int? = null

    override fun onCreateView(layoutInflater: LayoutInflater,container:ViewGroup?, savedInstanceState: Bundle?): View?  {
        super.onCreate(savedInstanceState)
        val view=layoutInflater.inflate(R.layout.tile_layout_easy,container)



        val button1: Button = view.findViewById(R.id.button1)
        val button2: Button = view.findViewById(R.id.button2)
        val button3: Button = view.findViewById(R.id.button3)
        val button4: Button = view.findViewById(R.id.button4)
        val button5: Button = view.findViewById(R.id.button5)
        val button6: Button = view.findViewById(R.id.button6)
        val button7: Button = view.findViewById(R.id.button7)
        val button8: Button = view.findViewById(R.id.button8)
        // val button9: Button = view.findViewById(R.id.button9)

        val buttons = arrayOf(button1, button2, button3, button4, button5, button6, button7, button8)

        var firstClicked = -1
        var secondClicked = -1

        // val handler=Handler(Looper.getMainLooper())



        for (i in buttons.indices){
            buttons[i].text = numbers[i].toString()
            updateButtonColor(buttons[i], i)

            buttons[i].setOnClickListener{
                if(firstClicked == -1){
                    firstClicked = i
                    buttons[i].setBackgroundColor(Color.GRAY)
            } else if (secondClicked == -1 && i != firstClicked){
                    secondClicked = i
                    buttons[i].setBackgroundColor(Color.GRAY)

                    if (canSwap(firstClicked,secondClicked, 3)) {
                        val temp = buttons[firstClicked].text
                        buttons[firstClicked].text = buttons[secondClicked].text
                        buttons[secondClicked].text = temp
                    }

                    firstClicked = -1
                    secondClicked = -1

                    checkOrder(buttons)
                }
            }
        }
        return view
    }
    private fun checkOrder(buttons: Array<Button>){
        var isOrdered = true;
        for (i in buttons.indices){
            if(buttons[i].text.toString().toInt() != i+1)
                isOrdered = false
                break
        }
        if(isOrdered){
            println("Game Complete")
        }

        for(index in buttons.indices){
            updateButtonColor(buttons[index], index)
        }
    }

    private fun updateButtonColor(button: Button, index: Int){
        if(button.text.toString().toInt() == index + 1){
            button.setBackgroundColor(Color.CYAN)
        }else{
            button.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun canSwap(firstIndex: Int, secondIndex: Int, columnNum: Int): Boolean{
        val row1 = firstIndex / columnNum
        val row2 = secondIndex / columnNum
        val col1 = firstIndex % columnNum
        val col2 = secondIndex % columnNum

        val isHorizontalNeighbour = kotlin.math.abs(firstIndex - secondIndex) == 1
        val isVerticalNeighbour = row1 == row2 || col1 == col2

        return isHorizontalNeighbour || isVerticalNeighbour
    }

}