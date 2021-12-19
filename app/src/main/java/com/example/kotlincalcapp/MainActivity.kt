package com.example.kotlincalcapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun allClearAction(view:View) {

        operationTV.text = ""
        resultsTV.text = ""

    }

    fun numberAction(view:View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal)
                    operationTV.append(view.text)

                canAddDecimal = false
            } else
                operationTV.append(view.text)

            canAddOperation = true

        }
    }

    fun operationAction(view:View) {

        if (view is Button && canAddOperation) {

            operationTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun equalAction(view: View) {
        resultsTV.text = calculateResult()
    }

    private fun calculateResult(): String {
        val digitsOperators = digitOperators()
        if (digitsOperators.isEmpty()) return ""

        val multiplyDivide = multiplyDivideCalculate(digitsOperators)
        if (digitsOperators.isEmpty()) return ""

        val result = addSubtractCalculate(multiplyDivide)
        return result.toString()

    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit

            }

        }
        return result


    }


    private fun multiplyDivideCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList
        while (list.contains('x') || list.contains('/'))
        {
            list = calcTimesDivide(list)
        }

        return list


    }

    private fun calcTimesDivide(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()

        var restartIndex = passedList.size

        for (i in passedList.indices)
        {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i-1] as Float
                val nextDigit = passedList[i+1] as Float
                when (operator) {
                    'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1

                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }

                }

            }

            if (i > restartIndex)
                newList.add(passedList[i])


        }
        return newList


    }

    private fun digitOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in operationTV.text) {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }

        }

        if (currentDigit != "")
            list.add(currentDigit.toFloat())

            return list


    }

    fun backSpaceAction(view: View) {

        val length = operationTV.length()
        if (length > 0)
            operationTV.text = operationTV.text.subSequence(0, length - 1)

    }
}

