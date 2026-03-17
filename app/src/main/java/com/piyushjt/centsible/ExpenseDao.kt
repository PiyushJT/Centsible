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

    @Query("SELECT 0 - SUM(amount) FROM expense WHERE date = :date AND amount < 0")
    suspend fun getAmount(date: Long) : Float

    @Query("SELECT SUM(amount) FROM expense")
    fun getTotalAmountFlow() : Flow<Float?>

    @Query("SELECT date, 0 - SUM(amount) as amount FROM expense WHERE date BETWEEN :startDate AND :endDate AND amount < 0 GROUP BY date")
    suspend fun getAmountsInRange(startDate: Long, endDate: Long) : List<DateAmount>

    @Query("SELECT AVG(weekly_sum) FROM (SELECT SUM(0 - amount) as weekly_sum FROM expense WHERE amount < 0 GROUP BY strftime('%Y-%W', substr(CAST(date AS TEXT), 1, 4) || '-' || substr(CAST(date AS TEXT), 5, 2) || '-' || substr(CAST(date AS TEXT), 7, 2)))")
    suspend fun getWeeklyAverage() : Float?

    @Query("SELECT AVG(monthly_sum) FROM (SELECT SUM(0 - amount) as monthly_sum FROM expense WHERE amount < 0 GROUP BY substr(CAST(date AS TEXT), 1, 6))")
    suspend fun getMonthlyAverage() : Float?

}

data class DateAmount(
    val date: Long,
    val amount: Float
)