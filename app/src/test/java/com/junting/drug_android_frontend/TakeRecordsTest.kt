package com.junting.drug_android_frontend

import com.junting.drug_android_frontend.model.take_record.TakeRecord
import com.junting.drug_android_frontend.services.ITakeRecordService
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
@RunWith(JUnit4::class)

@ExperimentalCoroutinesApi
class TakeRecordsTest {
    private val takeRecordService = ITakeRecordService.getInstance()

    @Test
    fun testGetTakeRecords() = runBlocking {
        // Arrange

        // Act
        val takeRecords : List<TakeRecord> = takeRecordService.getTakeRecords()

        // Assert
        assertNotNull(takeRecords)

        val takeRecordsGroupedByDrug = takeRecords.groupBy { it.drug }
        val takeRecordsGroupedByDrugAndDate = takeRecordsGroupedByDrug.mapValues { (_, records) ->
            records.groupBy { it.date }
                .toSortedMap(compareByDescending { it })
        }   // Map<Drug, Map<LocalDate, List<TakeRecord>>>
        println("groupedByDrugAndDate: $takeRecordsGroupedByDrugAndDate")

        takeRecordsGroupedByDrugAndDate.forEach { (drug, recordsByDate) ->
            println("藥物：${drug.name}")
            recordsByDate.forEach { (date, records) ->
                println("日期：$date")
                records.forEach { record ->
                    println("TakeRecord: $record")
                }
            }
            println() // 空行，用於分隔每個藥物的結果
        }


    }
}