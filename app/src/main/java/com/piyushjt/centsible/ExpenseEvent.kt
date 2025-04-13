package com.piyushjt.centsible

import android.content.Context

sealed interface ExpenseEvent {

    data class SaveExpense(val expense: Expense) : ExpenseEvent
    data class UpdateExpense(val expense: Expense): ExpenseEvent
    data class DeleteExpense(val expense: Expense): ExpenseEvent
    data class ChangePeriod(val startDate: Long, val endDate: Long): ExpenseEvent
    data class ChangeDateForPeriod(val date: Long): ExpenseEvent
    data class SetAmounts(val dates: List<Long>): ExpenseEvent
    data class SetLastAmounts(val dates: List<Long>): ExpenseEvent
    object SetTotalAmount: ExpenseEvent

    data class ExportData(val context: Context): ExpenseEvent
    object DeleteAllExpenses: ExpenseEvent

}