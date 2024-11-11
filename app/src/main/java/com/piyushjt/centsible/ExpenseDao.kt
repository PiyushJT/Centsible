package com.piyushjt.centsible

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Upsert
    suspend fun upsertExpense(todo: Expense)

    @Update
    suspend fun updateExpense(todo: Expense)

    @Delete
    suspend fun deleteExpense(todo: Expense)

    @Query("SELECT * FROM expense ORDER BY date ASC")
    fun getAllExpense() : Flow<List<Expense>>
}