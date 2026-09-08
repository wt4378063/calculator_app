package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var expressionText: TextView
    private lateinit var resultText: TextView
    private val engine = CalculatorEngine()

    // 当前输入的表达式
    private var expression = StringBuilder()
    // 是否已经显示了计算结果（用于判断是否开始新计算）
    private var justCalculated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        expressionText = findViewById(R.id.expressionText)
        resultText = findViewById(R.id.resultText)

        setupNumberButtons()
        setupOperatorButtons()
        setupFunctionButtons()
        setupBatchEntry()
    }

    private fun setupBatchEntry() {
        findViewById<Button>(R.id.btnOpenBatch).setOnClickListener {
            startActivity(Intent(this, BatchCalculateActivity::class.java))
        }
    }

    private fun setupNumberButtons() {
        val numberIds = listOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2",
            R.id.btn3 to "3", R.id.btn4 to "4", R.id.btn5 to "5",
            R.id.btn6 to "6", R.id.btn7 to "7", R.id.btn8 to "8",
            R.id.btn9 to "9", R.id.btnDot to "."
        )
        for ((id, value) in numberIds) {
            findViewById<Button>(id).setOnClickListener {
                if (justCalculated) {
                    expression.clear()
                    justCalculated = false
                }
                // 防止多个小数点
                if (value == ".") {
                    val lastNumber = expression.split(Regex("[+−×÷%]")).last()
                    if (lastNumber.contains(".")) return@setOnClickListener
                    if (lastNumber.isEmpty()) expression.append("0")
                }
                expression.append(value)
                expressionText.text = expression.toString()
            }
        }
    }

    private fun setupOperatorButtons() {
        val operatorMap = mapOf(
            R.id.btnAdd to "+",
            R.id.btnSubtract to "−",
            R.id.btnMultiply to "×",
            R.id.btnDivide to "÷",
            R.id.btnPercent to "%"
        )
        for ((id, op) in operatorMap) {
            findViewById<Button>(id).setOnClickListener {
                if (justCalculated) {
                    // 继续基于结果计算
                    justCalculated = false
                }
                if (expression.isNotEmpty()) {
                    val last = expression.last()
                    // 替换末尾运算符，避免连续运算符
                    if (last in "+−×÷%") {
                        expression[expression.length - 1] = op[0]
                    } else {
                        expression.append(op)
                    }
                    expressionText.text = expression.toString()
                }
            }
        }
    }

    private fun setupFunctionButtons() {
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            expression.clear()
            expressionText.text = ""
            resultText.text = "0"
            justCalculated = false
        }

        findViewById<Button>(R.id.btnDel).setOnClickListener {
            if (expression.isNotEmpty() && !justCalculated) {
                expression.deleteCharAt(expression.length - 1)
                expressionText.text = expression.toString()
            }
        }

        findViewById<Button>(R.id.btnPlusMinus).setOnClickListener {
            // 对当前最后一个数字取反
            if (expression.isEmpty()) return@setOnClickListener
            val regex = Regex("([+−×÷%])([^+−×÷%]+)$")
            val match = regex.find(expression)
            if (match != null) {
                val op = match.groupValues[1]
                val num = match.groupValues[2]
                val start = match.range.first + 1
                if (num.startsWith("-")) {
                    expression.replace(start, start + num.length, num.removePrefix("-"))
                } else {
                    expression.insert(start, "-")
                }
            } else {
                // 整个表达式就是一个数字
                if (expression.startsWith("-")) {
                    expression.deleteCharAt(0)
                } else {
                    expression.insert(0, "-")
                }
            }
            expressionText.text = expression.toString()
        }

        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            if (expression.isEmpty()) return@setOnClickListener
            // 移除末尾运算符后计算
            val expr = expression.trimEnd('+', '−', '×', '÷', '%')
            val result = engine.evaluate(expr)
            resultText.text = result
            expression.clear().append(result)
            justCalculated = true
        }
    }
}
