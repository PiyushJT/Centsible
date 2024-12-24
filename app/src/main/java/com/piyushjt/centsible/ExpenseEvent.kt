package com.piyushjt.centsible

sealed interface ExpenseEvent {

    data class SetTitle(val title: String) : ExpenseEvent
    data class SetDescription(val description: String?) : ExpenseEvent
    data class SetType(val type: String) : ExpenseEvent
    data class SetAmount(val amount: String) : ExpenseEvent
    data class SetDate(val date: Long) : ExpenseEvent
    data class SetID(val id: Int) : ExpenseEvent


    data class SetConstrainedExpenses(val startDate: Long, val endDate: Long) : ExpenseEvent
    data class SetStatsDuration(val duration: String) : ExpenseEvent
    data class SetStatsDate(val date: Long) : ExpenseEvent


    object SaveExpense : ExpenseEvent
    data class DeleteExpense(val expense: Expense): ExpenseEvent


    data class SetTypeBoxExpanded(val expanded: Boolean): ExpenseEvent
    data class SetIsDialogVisible(val visible: Boolean): ExpenseEvent
    data class ChangeNavState(val navFilled: String): ExpenseEvent
    object ClearState: ExpenseEvent

}