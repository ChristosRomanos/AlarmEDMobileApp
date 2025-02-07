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
import androidx.core.view.isVisible
import com.example.alarmedmobileapp.MainActivity
import com.example.alarmedmobileapp.R
import kotlinx.coroutines.delay
import kotlin.properties.Delegates
import kotlin.random.Random

class OrderAdapter(): Fragment() {

    var difficulty:Int = 0
    private var numbers:MutableList<Int> = mutableListOf()
    private lateinit var buttons: MutableList<Button>
    private var firstClicked=-1
    var buttons_locked=0

    override fun onCreateView(layoutInflater: LayoutInflater,container:ViewGroup?, savedInstanceState: Bundle?): View?  {
        super.onCreate(savedInstanceState)
        if (MainActivity.alarmOn) {
            MainActivity.tasksDone.add(MainActivity.viewPager2.currentItem)
            difficulty=MainActivity.enabledTasks[2]
        }else{
            difficulty=MainActivity.difficulties[2]
        }
        val view=layoutInflater.inflate(R.layout.tile_layout_easy,container)
        numbers=(1..(8+4*(difficulty-1))).shuffled().toMutableList()

        val finishBtn: Button =view.findViewById(R.id.startBtn)
        finishBtn.visibility=View.INVISIBLE
        finishBtn.isClickable=false

        val button1: Button = view.findViewById(R.id.button1)
        val button2: Button = view.findViewById(R.id.button2)
        val button3: Button = view.findViewById(R.id.button3)
        val button4: Button = view.findViewById(R.id.button4)
        val button5: Button = view.findViewById(R.id.button5)
        val button6: Button = view.findViewById(R.id.button6)
        val button7: Button = view.findViewById(R.id.button7)
        val button8: Button = view.findViewById(R.id.button8)

        buttons = mutableListOf(button1, button2, button3, button4, button5, button6, button7, button8)

        val color=button1.background
        var secondClicked = -1

        // val handler=Handler(Looper.getMainLooper())
        if (difficulty > 1) {


            val button9: Button = view.findViewById(R.id.button9)
            button9.isClickable = true
            button9.isEnabled = true
            button9.isVisible = true

            val button10: Button = view.findViewById(R.id.button10)
            button10.isClickable = true
            button10.isEnabled = true
            button10.isVisible = true

            val button11: Button = view.findViewById(R.id.button11)
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
        print(difficulty)
        println(buttons.size)
        for (i in 0..buttons.size-1){
            buttons[i].text = numbers[i].toString()
            buttons[i].setTextColor(Color.WHITE)
            buttons[i].textSize=32.0F
//            updateButtonColor(buttons[i], i)

            buttons[i].setOnClickListener {
                if (firstClicked == -1) {
                    firstClicked = i
                    buttons[i].setBackgroundColor(Color.GRAY)
                } else if (secondClicked == -1 && i != firstClicked) {
                    secondClicked = i
                    buttons[i].background = (color)
                    buttons[firstClicked].background = color

                    if (canSwap(firstClicked, secondClicked, 4)) {
                        val temp = buttons[firstClicked].text
                        buttons[firstClicked].text = buttons[secondClicked].text
                        buttons[secondClicked].text = temp
//                        checkOrder(i, firstClicked)
                        for (i in 0..buttons.size - 1) {
                            if (buttons[i].isClickable && buttons_locked == i && buttons[i].text.equals(
                                    (i + 1).toString()
                                )
                            ) {
                                buttons[i].setBackgroundColor(Color.CYAN)
                                buttons[i].isClickable = false
                                buttons_locked += 1

                            }
                        }
                        if (buttons_locked==buttons.size){
                            if(MainActivity.alarmOn){
                                MainActivity.tasksRemaing.value = MainActivity.tasksRemaing.value?.plus(
                                    -1
                                )
                                var next= Random.nextInt(4)
                                while (MainActivity.tasksDone.contains(next)){
                                    next= Random.nextInt(4)
                                }
                                MainActivity.viewPager2.setCurrentItem(next,false)
                            }else{
                                finishBtn.text="FINISHED"
                                finishBtn.visibility=View.VISIBLE
                                finishBtn.isClickable=true
                                finishBtn.setOnClickListener({
                                    MainActivity.viewPager2.currentItem=3
                                })
                            }
                        }
                    }

                    firstClicked = -1
                    secondClicked = -1

                }
            }
        }
        return view
    }
    private fun checkOrder(i:Int,j:Int) {
        if (i < j) {
            if (i == buttons_locked && buttons[i].text.toString()
                    .equals((buttons_locked + 1).toString())
            ) {
                buttons[i].isClickable = false
                buttons[i].setBackgroundColor(Color.CYAN)
                buttons_locked += 1
            }
            if (j == buttons_locked && buttons[j].text.toString()
                    .equals((buttons_locked + 1).toString())
            ) {
                buttons[j].isClickable = false
                buttons[j].setBackgroundColor(Color.CYAN)
                buttons_locked += 1
            }
        } else {
            if (j == buttons_locked && buttons[j].text.toString()
                    .equals((buttons_locked + 1).toString())
            ) {
                buttons[j].isClickable = false
                buttons[j].setBackgroundColor(Color.CYAN)
                buttons_locked += 1

            }
            if (i == buttons_locked && buttons[i].text.toString()
                    .equals((buttons_locked + 1).toString())
            ) {
                buttons[i].isClickable = false
                buttons[i].setBackgroundColor(Color.CYAN)
                buttons_locked += 1

            }
        }
    }

    private fun canSwap(firstIndex: Int, secondIndex: Int, columnNum: Int): Boolean{
        val row1 = firstIndex / columnNum
        val row2 = secondIndex / columnNum
        val col1 = firstIndex % columnNum
        val col2 = secondIndex % columnNum

        val isHorizontalNeighbour = kotlin.math.abs(col1 - col2) == 1 && row1 == row2
        val isVerticalNeighbour = kotlin.math.abs(row1 - row2) == 1 && col1 == col2

        return isHorizontalNeighbour || isVerticalNeighbour
    }

}