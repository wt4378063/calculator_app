package com.example.calculator

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorEngineTest {

    private val engine = CalculatorEngine()

    @Test
    fun testAddition() {
        assertEquals("5", engine.evaluate("2+3"))
    }

    @Test
    fun testSubtraction() {
        assertEquals("6", engine.evaluate("10-4"))
    }

    @Test
    fun testMultiplication() {
        assertEquals("42", engine.evaluate("6×7"))
    }

    @Test
    fun testDivision() {
        assertEquals("5", engine.evaluate("15÷3"))
    }

    @Test
    fun testPercentageUnary() {
        // 20% = 0.2
        assertEquals("0.2", engine.evaluate("20%"))
    }

    @Test
    fun testPercentageInExpression() {
        // 50 × 20% = 50 × 0.2 = 10
        assertEquals("10", engine.evaluate("50×20%"))
    }

    @Test
    fun testPrecedence() {
        // 2 + 3 × 4 = 14
        assertEquals("14", engine.evaluate("2+3×4"))
    }

    @Test
    fun testDivisionByZero() {
        assertEquals("错误", engine.evaluate("1÷0"))
    }

    @Test
    fun testDecimal() {
        assertEquals("0.3", engine.evaluate("0.1+0.2"))
    }

    @Test
    fun testPercentOfResult() {
        // (2+3) × 20% = 5 × 0.2 = 1
        assertEquals("1", engine.evaluate("(2+3)×20%"))
    }
}
