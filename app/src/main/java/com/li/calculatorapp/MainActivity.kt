package com.li.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.li.calculatorapp.R
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
            button_0,
            button_1,
            button_2,
            button_3,
            button_4,
            button_5,
            button_6,
            button_7,
            button_8,
            button_9,
            button_Add,
            button_Attack,
            button_Divide,
            button_Multi,
            button_Result,
            button_Delete
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
                text_Result.text = ""
                resetValue()
            }
            else -> {
                if (firstInput.toString() == "" || secondInput.toString() == "")
                    text_Result.text = this.resources.getString(R.string.alert_input)
                else
                    showResult()
            }
        }
    }

    private fun handleCalculate(text: String) {
        if (firstInput.toString() == "") {
            text_Result.text = this.resources.getString(R.string.alert_input)
        } else {
            when (text) {
                this.resources.getString(R.string.title_add) -> {
                    operator = ADD_VALUE
                    disableUnClickOperator(button_Attack, button_Multi, button_Divide)
                }
                this.resources.getString(R.string.title_attack) -> {
                    operator = ATTACK_VALUE
                    disableUnClickOperator(button_Add, button_Multi, button_Divide)
                }
                this.resources.getString(R.string.title_multi) -> {
                    operator = MULTI_VALUE
                    disableUnClickOperator(button_Add, button_Attack, button_Divide)
                }
                this.resources.getString(R.string.title_divide) -> {
                    operator = DIVIDE_VALUE
                    disableUnClickOperator(button_Add, button_Attack, button_Multi)
                }

            }
        }
    }

    private fun handleInputNumber(text: String) {
        if (operator > 0) {
            secondInput.append(text)
            text_Result.text = secondInput.toString()
        } else {
            firstInput.append(text)
            text_Result.text = firstInput.toString()
        }
    }

    private fun showResult() {
        val firstNumber = firstInput.toString().toInt()
        val secondNumber = secondInput.toString().toInt()
        when (operator) {
            ADD_VALUE -> {
                resultNumber = Calculators.add(firstNumber, secondNumber)
                text_Result.text = resultNumber.toString()
            }
            ATTACK_VALUE -> {
                resultNumber = Calculators.attack(firstNumber, secondNumber)
                text_Result.text = resultNumber.toString()
            }
            MULTI_VALUE -> {
                resultNumber = Calculators.multi(firstNumber, secondNumber)
                text_Result.text = resultNumber.toString()
            }
            DIVIDE_VALUE -> {
                handleDivide(firstNumber, secondNumber)
            }

        }
        resetValue()
        enableClickOperator(button_Attack, button_Add, button_Multi, button_Divide)
    }

    private fun handleDivide(firstNumber: Int, secondNumber: Int) {
        try {
            resultNumber = Calculators.divide(firstNumber, secondNumber)
            text_Result.text = resultNumber.toString()
        } catch (e: Exception) {
            text_Result.text = this.resources.getString(R.string.warning)
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