package com.piyushjt.centsible

sealed interface ExpenseEvent {

    data class SaveExpense(val expense: Expense) : ExpenseEvent
    data class UpdateExpense(val expense: Expense): ExpenseEvent
    data class DeleteExpense(val expense: Expense): ExpenseEvent
    data class ChangePeriod(val startDate: Long, val endDate: Long): ExpenseEvent
    data class ChangeDateForPeriod(val date: Long): ExpenseEvent
    data class SetAmounts(val dates: List<Long>): ExpenseEvent

}