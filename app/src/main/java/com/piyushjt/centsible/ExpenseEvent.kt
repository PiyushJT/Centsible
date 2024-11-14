package com.piyushjt.centsible

sealed interface ExpenseEvent {
    data class SetTitle(val title: String) : ExpenseEvent
    data class SetID(val id: Int) : ExpenseEvent
    data class SetDescription(val description: String) : ExpenseEvent
    object SaveExpense : ExpenseEvent
    data class SortExpense(val sortType: SortType = SortType.DATE): ExpenseEvent
    data class DeleteExpense(val expense: Expense): ExpenseEvent
    data class ChangeNavState(val navFilled: String): ExpenseEvent
}