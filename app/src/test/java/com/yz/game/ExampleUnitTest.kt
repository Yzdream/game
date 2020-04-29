package com.yz.game

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun froTest() {
        var sum = 3
        for (index in 1..sum) {
            if (index == 2 && sum == 3) {
                sum++
            }
            System.out.print("==$index===$sum")
        }
    }

    @Test
    fun arrayTest() {
        val map: Array<IntArray> = Array(3) { IntArray(3) }
        System.out.print("=====" + map[2][2])
    }

    @Test
    fun main(){
    }

}
