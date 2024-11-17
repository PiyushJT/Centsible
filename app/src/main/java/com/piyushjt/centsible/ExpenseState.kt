package com.piyushjt.centsible

data class ExpenseState(
    val expenses: List<Expense> = emptyList(),
    val id : Int = -1,
    val title: String = "Title",
    val description: String? = null,
    val date: Long = 30072007,
    val type: String = "ent",
    val amount: Float = 100.0f,
    val sortType: SortType = SortType.DATE,
    val navFilled: String = "home"
)