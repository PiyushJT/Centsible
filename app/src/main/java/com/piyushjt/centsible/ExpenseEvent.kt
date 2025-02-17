package com.piyushjt.centsible

sealed interface ExpenseEvent {

    data class SaveExpense(val expense: Expense) : ExpenseEvent
    data class UpdateExpense(val expense: Expense): ExpenseEvent
    data class DeleteExpense(val expense: Expense): ExpenseEvent

}