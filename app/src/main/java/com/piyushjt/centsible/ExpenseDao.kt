// This is the Data Access Object (DAO) used to access the database

package com.piyushjt.centsible

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Upsert
    suspend fun upsertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expense ORDER BY date DESC, id DESC")
    fun getAllExpense() : Flow<List<Expense>>

    @Query("SELECT * FROM expense WHERE amount < 0 AND date BETWEEN :startDate AND :endDate ORDER BY date DESC, id DESC")
    suspend fun getExpenseInPeriod(startDate: Long, endDate: Long) : List<Expense>

    @Query("SELECT 0 - SUM(amount) FROM expense WHERE date = :date")
    suspend fun getAmount(date: Long) : Float

    @Query("SELECT SUM(amount) FROM expense")
    suspend fun getTotalAmount() : Float

}