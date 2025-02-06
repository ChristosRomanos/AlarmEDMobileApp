package com.example.alarmedmobileapp.Adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.alarmedmobileapp.MainActivity
import com.example.alarmedmobileapp.MainActivity.Companion.viewPager2
import com.example.alarmedmobileapp.R
import kotlin.random.Random

class MathGameAdapter() : Fragment(){
    private lateinit var questionTextView: TextView
    private lateinit var answerEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var scoreTextView: TextView
    private lateinit var finishBtn: Button
    val difficulty = 0

    private var currentQuestion = ""
    private var correctAnswer = 0
    private var score = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.math_quiz, container, false)
        viewPager2.isUserInputEnabled=false
        if (MainActivity.alarmOn) {
            MainActivity.tasksDone.add(viewPager2.currentItem)
            MainActivity.difficulties[0]=MainActivity.enabledTasks[0]
        }else{
            MainActivity.difficulties[0]=MainActivity.difficulties[0]
        }
        finishBtn=view.findViewById(R.id.finishButton)
        questionTextView = view.findViewById(R.id.questionTextView)
        answerEditText = view.findViewById(R.id.answerEditText)
        submitButton = view.findViewById(R.id.submitButton)
        scoreTextView = view.findViewById(R.id.scoreTextView)
        finishBtn.visibility=View.INVISIBLE
        finishBtn.isClickable=false
        generateQuestion()

        submitButton.setOnClickListener {
            checkAnswer()
        }

        return view
    }

    private fun generateQuestion() {
        if(score==3){
            currentQuestion="Finished"
            questionTextView.text=currentQuestion
            answerEditText.isEnabled=false
            answerEditText.visibility=View.INVISIBLE
            submitButton.isClickable=false
            return
        }
        val (numRange, operators) = when (difficulty) {
            1 -> 10..50 to listOf("+")
            2 -> 1..15 to listOf("-", "*")
            3 -> 10..50 to listOf("*")
            else -> 1..10 to listOf("+")
        }

        val num1 = Random.nextInt(numRange.first, numRange.last + 1)
        val num2 = Random.nextInt(numRange.first, numRange.last + 1)
        val operator = operators.random()

        currentQuestion = "$num1 $operator $num2"
        when (operator) {
            "+" -> {
                currentQuestion = "$num1 + $num2"
                correctAnswer = num1 + num2
            }
            "-" -> {
                val (larger, smaller) = if (num1 >= num2) num1 to num2 else num2 to num1
                currentQuestion = "$larger - $smaller"
                correctAnswer = larger - smaller
            }
            "*" -> {
                currentQuestion = "$num1 * $num2"
                correctAnswer = num1 * num2
            }
        }

        questionTextView.text = currentQuestion
    }
    @SuppressLint("SetTextI18n")
    private fun checkAnswer() {

        val userAnswer = answerEditText.text.toString().toIntOrNull()
        if (userAnswer == correctAnswer) {
            score++
            scoreTextView.text = "Score: $score"
            generateQuestion()
        }
        if (score==3){
            if (MainActivity.alarmOn) {
                MainActivity.tasksRemaing.value = MainActivity.tasksRemaing.value?.plus(
                    -1
                )
                var next = Random.nextInt(4)
                while (MainActivity.tasksDone.contains(next)) {
                    next = Random.nextInt(4)
                }
                MainActivity.viewPager2.setCurrentItem(next, false)
            }else{
                viewPager2.isUserInputEnabled=true
                finishBtn.visibility=View.VISIBLE
                finishBtn.isClickable=true
                finishBtn.setOnClickListener {
                    viewPager2.currentItem=3
                }
            }
        }
        answerEditText.text.clear()
    }

}