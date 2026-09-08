package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

/**
 * 批量求和计算界面：
 * 1. 输入单价
 * 2. 逐个添加若干数字（模拟"多个数相加时增加输入框"）
 * 3. 点击计算后展示：
 *    - 加法总和
 *    - 总和 × 单价
 *    - 总和平均值（总和 ÷ 个数）
 *    - 平均值 × 单价
 */
class BatchCalculateActivity : AppCompatActivity() {

    private lateinit var unitPriceInput: EditText
    private lateinit var numberInput: EditText
    private lateinit var numbersListText: TextView
    private lateinit var resultBlock: LinearLayout
    private lateinit var resSum: TextView
    private lateinit var resSumTimesPrice: TextView
    private lateinit var resAverage: TextView
    private lateinit var resAverageTimesPrice: TextView

    // 保存已添加的数字
    private val numbers = mutableListOf<Double>()

    // 格式化输出，避免 0.30000000000000004 之类
    private val formatter = DecimalFormat("#.##########")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batch)

        unitPriceInput = findViewById(R.id.unitPriceInput)
        numberInput = findViewById(R.id.numberInput)
        numbersListText = findViewById(R.id.numbersListText)
        resultBlock = findViewById(R.id.resultBlock)
        resSum = findViewById(R.id.resSum)
        resSumTimesPrice = findViewById(R.id.resSumTimesPrice)
        resAverage = findViewById(R.id.resAverage)
        resAverageTimesPrice = findViewById(R.id.resAverageTimesPrice)

        findViewById<Button>(R.id.btnAddNumber).setOnClickListener { addNumber() }
        findViewById<Button>(R.id.btnCalculate).setOnClickListener { calculate() }
        findViewById<Button>(R.id.btnClearAll).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }
    }

    /** 添加一个数字到列表 */
    private fun addNumber() {
        val text = numberInput.text.toString().trim()
        if (text.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_numbers, Toast.LENGTH_SHORT).show()
            return
        }
        val value = text.toDoubleOrNull()
        if (value == null) {
            Toast.makeText(this, "请输入有效的数字", Toast.LENGTH_SHORT).show()
            return
        }
        numbers.add(value)
        numberInput.text.clear()
        refreshNumbersList()
        resultBlock.visibility = View.GONE
    }

    /** 刷新已添加数字列表显示 */
    private fun refreshNumbersList() {
        if (numbers.isEmpty()) {
            numbersListText.text = getString(R.string.empty_list_hint)
            return
        }
        val sb = StringBuilder()
        sb.append(getString(R.string.list_header, numbers.size))
        numbers.forEachIndexed { index, d ->
            sb.append("${index + 1}. ${formatter.format(d)}")
            if (index < numbers.size - 1) sb.append("\n")
        }
        // 顺便在列表底部显示当前累加和，便于对照
        sb.append("\n\n当前累加和 = ${formatter.format(numbers.sum())}")
        numbersListText.text = sb.toString()
    }

    /** 执行计算并显示结果 */
    private fun calculate() {
        if (numbers.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_numbers, Toast.LENGTH_SHORT).show()
            return
        }
        val priceText = unitPriceInput.text.toString().trim()
        if (priceText.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_price, Toast.LENGTH_SHORT).show()
            return
        }
        val price = priceText.toDoubleOrNull()
        if (price == null) {
            Toast.makeText(this, "请输入有效的单价", Toast.LENGTH_SHORT).show()
            return
        }

        val sum = numbers.sum()
        val count = numbers.size.toDouble()
        val average = sum / count
        val sumTimesPrice = sum * price
        val averageTimesPrice = average * price

        resSum.text = getString(R.string.result_sum, formatter.format(sum))
        resSumTimesPrice.text = getString(R.string.result_sum_times_price, formatter.format(sumTimesPrice))
        resAverage.text = getString(R.string.result_average, formatter.format(average))
        resAverageTimesPrice.text = getString(R.string.result_average_times_price, formatter.format(averageTimesPrice))

        resultBlock.visibility = View.VISIBLE
    }

    /** 清空所有输入和数字 */
    private fun clearAll() {
        numbers.clear()
        numberInput.text.clear()
        unitPriceInput.text.clear()
        resultBlock.visibility = View.GONE
        refreshNumbersList()
    }
}
