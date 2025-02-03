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
import androidx.fragment.app.Fragment
import com.example.alarmedmobileapp.R
import kotlin.random.Random

class MathGameAdapter : Fragment(){
    private lateinit var questionTextView: TextView
    private lateinit var answerEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var scoreTextView: TextView
    private lateinit var difficultySpinner: Spinner

    private var currentQuestion = ""
    private var correctAnswer = 0
    private var score = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.math_quiz, container, false)

        questionTextView = view.findViewById(R.id.questionTextView)
        answerEditText = view.findViewById(R.id.answerEditText)
        submitButton = view.findViewById(R.id.submitButton)
        scoreTextView = view.findViewById(R.id.scoreTextView)
        difficultySpinner = view.findViewById(R.id.difficultySpinner)

        val difficultyLevels = listOf("Easy", "Medium", "Hard")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, difficultyLevels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        difficultySpinner.adapter = adapter

        difficultySpinner.setSelection(0)
        difficultySpinner.setOnItemSelectedListener(null)

        difficultySpinner.post {
           generateQuestion()
        }

        submitButton.setOnClickListener {
            checkAnswer()
        }

        return view
    }

    private fun generateQuestion() {
        val difficulty = difficultySpinner.selectedItem?.toString() ?: "Easy"
        val (numRange, operators) = when (difficulty) {
            "Easy" -> 1..10 to listOf("+")
            "Medium" -> 1..20 to listOf("+", "-")
            "Hard" -> 1..50 to listOf("+", "-", "*")
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
        answerEditText.text.clear()
    }

}