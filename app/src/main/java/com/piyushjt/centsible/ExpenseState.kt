// This is State of all values

package com.piyushjt.centsible

data class ExpenseState(

    val expenses: List<Expense> = emptyList(),
    val constrainedExpenses: List<Expense> = emptyList(),

    val title: String = "",
    val description: String? = null,
    val type: String = "ent",
    val amount: Float = -100.0f,
    val date: Long = Utils.getCurrentDate(),
    val id: Int = -1,

    val typeBoxExpanded: Boolean = false,
    val isDialogVisible: Boolean = false,
    val navFilled: String = "stats",

    val amountToShow: String = "-100",

)