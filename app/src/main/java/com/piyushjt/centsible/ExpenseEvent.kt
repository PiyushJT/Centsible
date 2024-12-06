package com.piyushjt.centsible

sealed interface ExpenseEvent {
    data class SetTitle(val title: String) : ExpenseEvent
    data class SetID(val id: Int) : ExpenseEvent
    data class SetType(val type: String) : ExpenseEvent
    data class SetDescription(val description: String) : ExpenseEvent
    data class SetAmount(val amount: String) : ExpenseEvent
    data class SetDate(val date: Long) : ExpenseEvent
    object SaveExpense : ExpenseEvent
    data class SortExpense(val sortType: SortType = SortType.DATE): ExpenseEvent
    data class DeleteExpense(val expense: Expense): ExpenseEvent
    data class ChangeNavState(val navFilled: String): ExpenseEvent
    data class SetTypeBoxExpanded(val expanded: Boolean): ExpenseEvent
}