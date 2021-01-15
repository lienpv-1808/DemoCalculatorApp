package com.li.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val firstInput = StringBuilder()
    private val secondInput = StringBuilder()
    private var operator = DEFAULT_VALUE
    private var resultNumber = DEFAULT_VALUE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onMultiClick(
            button0,
            button1,
            button2,
            button3,
            button4,
            button5,
            button6,
            button7,
            button8,
            button9,
            buttonAdd,
            buttonAttack,
            buttonDivide,
            buttonMulti,
            buttonResult,
            buttonDelete
        )
    }

    private fun View.OnClickListener.onMultiClick(vararg views: View) {
        views.forEach { it.setOnClickListener(this) }
    }

    override fun onClick(p0: View?) {
        val clicked = p0 as Button
        when (val text = clicked.text.toString()) {
            in ARRAY_NUMBER_INPUT -> handleInputNumber(text)
            in ARRAY_OPERATOR -> handleCalculate(text)
            this.resources.getString(R.string.title_delete) -> {
                textResult.text = ""
                resetValue()
            }
            else -> {
                if (firstInput.toString() == "" || secondInput.toString() == "")
                    textResult.text = this.resources.getString(R.string.alert_input)
                else
                    showResult()
            }
        }
    }

    private fun handleCalculate(text: String) {
        if (firstInput.toString() == "") {
            textResult.text = this.resources.getString(R.string.alert_input)
        } else {
            when (text) {
                this.resources.getString(R.string.title_add) -> {
                    operator = ADD_VALUE
                    disableUnClickOperator(buttonAttack, buttonMulti, buttonDivide)
                }
                this.resources.getString(R.string.title_attack) -> {
                    operator = ATTACK_VALUE
                    disableUnClickOperator(buttonAdd, buttonMulti, buttonDivide)
                }
                this.resources.getString(R.string.title_multi) -> {
                    operator = MULTI_VALUE
                    disableUnClickOperator(buttonAdd, buttonAttack, buttonDivide)
                }
                this.resources.getString(R.string.title_divide) -> {
                    operator = DIVIDE_VALUE
                    disableUnClickOperator(buttonAdd, buttonAttack, buttonMulti)
                }

            }
        }
    }

    private fun handleInputNumber(text: String) {
        if (operator > 0) {
            secondInput.append(text)
            textResult.text = secondInput.toString()
        } else {
            firstInput.append(text)
            textResult.text = firstInput.toString()
        }
    }

    private fun showResult() {
        val firstNumber = firstInput.toString().toInt()
        val secondNumber = secondInput.toString().toInt()
        when (operator) {
            ADD_VALUE -> {
                resultNumber = Calculators.add(firstNumber, secondNumber)
                textResult.text = resultNumber.toString()
            }
            ATTACK_VALUE -> {
                resultNumber = Calculators.attack(firstNumber, secondNumber)
                textResult.text = resultNumber.toString()
            }
            MULTI_VALUE -> {
                resultNumber = Calculators.multi(firstNumber, secondNumber)
                textResult.text = resultNumber.toString()
            }
            DIVIDE_VALUE -> {
                handleDivide(firstNumber, secondNumber)
            }

        }
        resetValue()
        enableClickOperator(buttonAttack, buttonAdd, buttonMulti, buttonDivide)
    }

    private fun handleDivide(firstNumber: Int, secondNumber: Int) {
        try {
            resultNumber = Calculators.divide(firstNumber, secondNumber)
            textResult.text = resultNumber.toString()
        } catch (e: Exception) {
            textResult.text = this.resources.getString(R.string.warning)
        }
    }

    private fun disableUnClickOperator(vararg views: View) {
        views.forEach { it.isEnabled = false }
    }

    private fun enableClickOperator(vararg views: View) {
        views.forEach {
            if (!it.isEnabled) it.isEnabled = true
        }
    }

    private fun resetValue() {
        firstInput.clear()
        secondInput.clear()
        operator = DEFAULT_VALUE
    }

    companion object {
        const val DEFAULT_VALUE = 0
        const val ADD_VALUE = 1
        const val ATTACK_VALUE = 2
        const val MULTI_VALUE = 3
        const val DIVIDE_VALUE = 4
        val ARRAY_NUMBER_INPUT = arrayListOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
        val ARRAY_OPERATOR = arrayListOf("+", "-", "X", ":")
    }
}
