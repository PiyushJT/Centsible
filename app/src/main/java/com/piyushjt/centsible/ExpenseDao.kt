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

}