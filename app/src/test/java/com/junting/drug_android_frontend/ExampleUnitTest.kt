package com.junting.drug_android_frontend

import org.junit.Test

import org.junit.Assert.*
import com.junting.drug_android_frontend.services.DrugService

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
    fun getData(){
        val drugService = DrugService.getInstance()
        print(drugService.getDrugs())
    }
}