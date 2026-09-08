package com.example.calculator

/**
 * 计算器核心引擎：递归下降解析器，正确处理运算符优先级。
 *
 * 运算符：
 *   + -         : 加减（优先级低）
 *   × ÷         : 乘除（优先级高）
 *   %           : 百分比（一元后缀，作用于左侧数值，如 20% = 0.2）
 *   . ( )       : 小数点、括号
 *
 * 百分比语义（贴近系统计算器）：
 *   - 作为二元运算符时：a % b = a * b / 100   （如 50 × 20% = 50 × 0.2 = 10）
 *   - 作为一元后缀时：% 将左侧数值除以 100     （如 20% = 0.2）
 */
class CalculatorEngine {

    private var pos = 0
    private lateinit var input: String

    fun evaluate(expression: String): String {
        return try {
            input = expression.replace("×", "*").replace("÷", "/")
            pos = 0
            val result = parseExpression()
            if (result == result.toLong().toDouble()) {
                result.toLong().toString()
            } else {
                String.format("%.8f", result).trimEnd('0').trimEnd('.')
            }
        } catch (e: Exception) {
            "错误"
        }
    }

    // 表达式: 项 ( (+|-) 项 )*
    private fun parseExpression(): Double {
        var value = parseTerm()
        while (pos < input.length) {
            val c = input[pos]
            if (c == '+' || c == '-') {
                pos++
                val next = parseTerm()
                value = if (c == '+') value + next else value - next
            } else {
                break
            }
        }
        return value
    }

    // 项: 因子 ( (*|/) 因子 )*
    // 注：% 在 parseFactor 中作为一元后缀处理
    private fun parseTerm(): Double {
        var value = parseFactor()
        while (pos < input.length) {
            val c = input[pos]
            if (c == '*' || c == '/') {
                pos++
                val next = parseFactor()
                value = if (c == '*') value * next else value / next
            } else {
                break
            }
        }
        return value
    }

    // 因子: 数字 [%] | ( 表达式 )
    // % 作为可选的一元后缀：将当前数值转为百分比（除以 100）
    private fun parseFactor(): Double {
        skipWhitespace()
        if (pos >= input.length) throw IllegalArgumentException("表达式不完整")

        val value = when (input[pos]) {
            '(' -> {
                pos++
                val v = parseExpression()
                if (pos < input.length && input[pos] == ')') pos++
                v
            }
            else -> parseNumber()
        }

        // 检查 % 后缀（一元，作用于刚解析出的数值）
        if (pos < input.length && input[pos] == '%') {
            pos++
            return value / 100.0
        }

        return value
    }

    // 解析数字（含小数点）
    private fun parseNumber(): Double {
        val start = pos
        while (pos < input.length &&
            (input[pos].isDigit() || input[pos] == '.')) {
            pos++
        }
        if (start == pos) throw IllegalArgumentException("无效字符")
        return input.substring(start, pos).toDouble()
    }

    private fun skipWhitespace() {
        while (pos < input.length && input[pos] == ' ') pos++
    }
}
