package com.example.calculator

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.DecimalFormat

/**
 * 批量求和计算逻辑测试（抽离纯计算部分，与 Activity 解耦）
 */
class BatchCalculateLogicTest {

    private val formatter = DecimalFormat("#.##########")

    /** 加法总和 */
    private fun sum(list: List<Double>): Double = list.sum()

    /** 总和 × 单价 */
    private fun sumTimesPrice(list: List<Double>, price: Double): Double = list.sum() * price

    /** 总和平均值 */
    private fun average(list: List<Double>): Double = if (list.isEmpty()) 0.0 else list.sum() / list.size

    /** 平均值 × 单价 */
    private fun averageTimesPrice(list: List<Double>, price: Double): Double =
        if (list.isEmpty()) 0.0 else (list.sum() / list.size) * price

    @Test
    fun `sum of numbers is correct`() {
        val numbers = listOf(10.0, 20.0, 30.0, 40.0)
        assertEquals(100.0, sum(numbers), 1e-9)
    }

    @Test
    fun `sum times unit price is correct`() {
        val numbers = listOf(10.0, 20.0, 30.0)
        val price = 5.0
        // 总和 60 × 5 = 300
        assertEquals(300.0, sumTimesPrice(numbers, price), 1e-9)
    }

    @Test
    fun `average is correct`() {
        val numbers = listOf(10.0, 20.0, 30.0)
        // 平均值 = 60 / 3 = 20
        assertEquals(20.0, average(numbers), 1e-9)
    }

    @Test
    fun `average times unit price is correct`() {
        val numbers = listOf(10.0, 20.0, 30.0)
        val price = 5.0
        // 平均值 20 × 5 = 100
        assertEquals(100.0, averageTimesPrice(numbers, price), 1e-9)
    }

    @Test
    fun `single number edge case`() {
        val numbers = listOf(7.0)
        val price = 3.0
        assertEquals(7.0, sum(numbers), 1e-9)
        assertEquals(21.0, sumTimesPrice(numbers, price), 1e-9)
        assertEquals(7.0, average(numbers), 1e-9)
        assertEquals(21.0, averageTimesPrice(numbers, price), 1e-9)
    }

    @Test
    fun `floating point precision handled`() {
        val numbers = listOf(0.1, 0.2, 0.3)
        // 总和应为 0.6，而非 0.6000000000000001
        assertEquals("0.6", formatter.format(sum(numbers)))
    }

    @Test
    fun `empty list returns zero`() {
        val numbers = emptyList<Double>()
        assertEquals(0.0, average(numbers), 1e-9)
        assertEquals(0.0, averageTimesPrice(numbers, 5.0), 1e-9)
    }
}
